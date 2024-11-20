package cn.maoyanluo.anywhere.door.controller;

import cn.maoyanluo.anywhere.door.bean.Response;
import cn.maoyanluo.anywhere.door.entity.Plugin;
import cn.maoyanluo.anywhere.door.entity.User;
import cn.maoyanluo.anywhere.door.repository.PluginRepository;
import cn.maoyanluo.anywhere.door.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/plugin")
public class PluginController {

    @Autowired
    public PluginController(PluginRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    private final PluginRepository repository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public Response<Plugin> getPluginById(@PathVariable("id") Integer id, @RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        Optional<Plugin> plugin = repository.findById(id);
        if (plugin.isPresent() && user != null) {
            if (plugin.get().getUserId().equals(user.getId())) {
                return Response.success(plugin.get());
            } else {
                Response<Plugin> response = new Response<>();
                response.setCode(-4);
                response.setMsg("NO_PERMISSION");
                return response;
            }
        } else {
            Response<Plugin> response = new Response<>();
            response.setCode(-5);
            response.setMsg("NO_ENTITY");
            return response;
        }
    }

    @GetMapping
    public Response<List<Plugin>> getPlugins(@RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        List<Plugin> plugins = repository.findByUserId(user.getId());
        return Response.success(plugins);
    }

    @PostMapping("/create")
    public Response<Plugin> create(@RequestBody Plugin plugin, @RequestAttribute("username") String username) {
        if (StringUtils.isEmpty(plugin.getPluginName())
        || StringUtils.isEmpty(plugin.getPluginHost())
        || plugin.getPluginPort() == null
        || StringUtils.isEmpty(plugin.getPluginToken())
        ) {
            Response<Plugin> response = new Response<>();
            response.setCode(-3);
            response.setMsg("MISS_PARAMETER");
            return response;
        }
        if (plugin.getPluginName().contains(" ")) {
            Response<Plugin> response = new Response<>();
            response.setCode(-5);
            response.setMsg("WRONG_NAME");
            return response;
        }
        User user = userRepository.findByUsername(username);
        Integer userId = user.getId();
        plugin.setUserId(userId);
        plugin.setIsActive(1);
        plugin = repository.save(plugin);
        return Response.success(plugin);
    }

    @PutMapping("/update")
    public Response<Plugin> update(@RequestBody Plugin plugin, @RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        Optional<Plugin> currentPluginOptional = repository.findById(plugin.getId());
        if (currentPluginOptional.isEmpty()) {
            Response<Plugin> response = new Response<>();
            response.setCode(-3);
            response.setMsg("MISS_PARAMETER");
            return response;
        }
        Plugin currentPlugin = currentPluginOptional.get();
        if (!Objects.equals(currentPlugin.getUserId(), user.getId())) {
            Response<Plugin> response = new Response<>();
            response.setCode(-4);
            response.setMsg("NO_PERMISSION");
            return response;
        }

        if (!StringUtils.isEmpty(plugin.getPluginName())) {
            currentPlugin.setPluginName(plugin.getPluginName());
        }

        if (!StringUtils.isEmpty(plugin.getPluginDescribe())) {
            currentPlugin.setPluginDescribe(plugin.getPluginDescribe());
        }

        if (!StringUtils.isEmpty(plugin.getPluginHost())) {
            currentPlugin.setPluginHost(plugin.getPluginHost());
        }

        if (plugin.getPluginPort() != null) {
            currentPlugin.setPluginPort(plugin.getPluginPort());
        }

        if (!StringUtils.isEmpty(plugin.getPluginPrefix())) {
            currentPlugin.setPluginPrefix(plugin.getPluginPrefix());
        }

        if (!StringUtils.isEmpty(plugin.getPluginToken())) {
            currentPlugin.setPluginToken(plugin.getPluginToken());
        }

        if (plugin.getIsActive() != null) {
            currentPlugin.setIsActive(plugin.getIsActive());
        }

        repository.save(currentPlugin);

        return Response.success(currentPlugin);
    }

    @DeleteMapping("/{id}")
    public Response<Plugin> delete(@PathVariable("id") Integer id ,@RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        Optional<Plugin> currentPluginOptional = repository.findById(id);
        if (currentPluginOptional.isEmpty()) {
            return Response.success(null);
        }
        Plugin currentPlugin = currentPluginOptional.get();
        if (!Objects.equals(currentPlugin.getUserId(), user.getId())) {
            Response<Plugin> response = new Response<>();
            response.setCode(-4);
            response.setMsg("NO_PERMISSION");
            return response;
        }
        repository.delete(currentPlugin);
        return Response.success(currentPlugin);
    }

}
