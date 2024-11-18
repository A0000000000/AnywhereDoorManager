package cn.maoyanluo.anywhere.door.controller;

import cn.maoyanluo.anywhere.door.bean.Response;
import cn.maoyanluo.anywhere.door.entity.Imsdk;
import cn.maoyanluo.anywhere.door.entity.User;
import cn.maoyanluo.anywhere.door.repository.ImsdkRepository;
import cn.maoyanluo.anywhere.door.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/imsdk")
public class ImsdkController {

    @Autowired
    public ImsdkController(ImsdkRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    private final ImsdkRepository repository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public Response<Imsdk> getImsdkById(@PathVariable("id") Integer id, @RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        Optional<Imsdk> imsdk = repository.findById(id);
        if (imsdk.isPresent() && user != null) {
            if (imsdk.get().getUserId().equals(user.getId())) {
                return Response.success(imsdk.get());
            } else {
                Response<Imsdk> response = new Response<>();
                response.setCode(-4);
                response.setMsg("NO_PERMISSION");
                return response;
            }
        } else {
            Response<Imsdk> response = new Response<>();
            response.setCode(-5);
            response.setMsg("NO_ENTITY");
            return response;
        }
    }

    @GetMapping
    public Response<List<Imsdk>> getImsdks(@RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        List<Imsdk> plugins = repository.findByUserId(user.getId());
        return Response.success(plugins);
    }

    @PostMapping("/create")
    public Response<Imsdk> create(@RequestBody Imsdk plugin, @RequestAttribute("username") String username) {
        if (StringUtils.isEmpty(plugin.getImsdkName())
        || StringUtils.isEmpty(plugin.getImsdkHost())
        || plugin.getImsdkPort() != null
        || StringUtils.isEmpty(plugin.getImsdkToken())
        ) {
            Response<Imsdk> response = new Response<>();
            response.setCode(-3);
            response.setMsg("MISS_PARAMETER");
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
    public Response<Imsdk> update(@RequestBody Imsdk plugin, @RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        Optional<Imsdk> currentImsdkOptional = repository.findById(plugin.getId());
        if (currentImsdkOptional.isEmpty()) {
            Response<Imsdk> response = new Response<>();
            response.setCode(-3);
            response.setMsg("MISS_PARAMETER");
            return response;
        }
        Imsdk currentImsdk = currentImsdkOptional.get();
        if (!Objects.equals(currentImsdk.getUserId(), user.getId())) {
            Response<Imsdk> response = new Response<>();
            response.setCode(-4);
            response.setMsg("NO_PERMISSION");
            return response;
        }

        if (!StringUtils.isEmpty(plugin.getImsdkName())) {
            currentImsdk.setImsdkName(plugin.getImsdkName());
        }

        if (!StringUtils.isEmpty(plugin.getImsdkDescribe())) {
            currentImsdk.setImsdkDescribe(plugin.getImsdkDescribe());
        }

        if (!StringUtils.isEmpty(plugin.getImsdkHost())) {
            currentImsdk.setImsdkHost(plugin.getImsdkHost());
        }

        if (plugin.getImsdkPort() != null) {
            currentImsdk.setImsdkPort(plugin.getImsdkPort());
        }

        if (!StringUtils.isEmpty(plugin.getImsdkPrefix())) {
            currentImsdk.setImsdkPrefix(plugin.getImsdkPrefix());
        }

        if (!StringUtils.isEmpty(plugin.getImsdkToken())) {
            currentImsdk.setImsdkToken(plugin.getImsdkToken());
        }

        if (plugin.getIsActive() != null) {
            currentImsdk.setIsActive(plugin.getIsActive());
        }

        repository.save(currentImsdk);

        return Response.success(currentImsdk);
    }

    @DeleteMapping("/{id}")
    public Response<Imsdk> delete(@PathVariable("id") Integer id ,@RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        Optional<Imsdk> currentImsdkOptional = repository.findById(id);
        if (currentImsdkOptional.isEmpty()) {
            return Response.success(null);
        }
        Imsdk currentImsdk = currentImsdkOptional.get();
        if (!Objects.equals(currentImsdk.getUserId(), user.getId())) {
            Response<Imsdk> response = new Response<>();
            response.setCode(-4);
            response.setMsg("NO_PERMISSION");
            return response;
        }
        repository.delete(currentImsdk);
        return Response.success(currentImsdk);
    }

}
