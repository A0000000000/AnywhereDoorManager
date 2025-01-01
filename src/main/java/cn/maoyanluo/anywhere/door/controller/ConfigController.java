package cn.maoyanluo.anywhere.door.controller;


import cn.maoyanluo.anywhere.door.bean.Response;
import cn.maoyanluo.anywhere.door.constant.ErrorCode;
import cn.maoyanluo.anywhere.door.constant.ErrorMessage;
import cn.maoyanluo.anywhere.door.entity.Config;
import cn.maoyanluo.anywhere.door.entity.Imsdk;
import cn.maoyanluo.anywhere.door.entity.Plugin;
import cn.maoyanluo.anywhere.door.entity.User;
import cn.maoyanluo.anywhere.door.repository.ConfigRepository;
import cn.maoyanluo.anywhere.door.repository.ImsdkRepository;
import cn.maoyanluo.anywhere.door.repository.PluginRepository;
import cn.maoyanluo.anywhere.door.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ConfigRepository repository;
    private final UserRepository userRepository;
    private final PluginRepository pluginRepository;
    private final ImsdkRepository imsdkRepository;

    @Autowired
    public ConfigController(ConfigRepository repository, UserRepository userRepository, PluginRepository pluginRepository, ImsdkRepository imsdkRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.pluginRepository = pluginRepository;
        this.imsdkRepository = imsdkRepository;
    }

    @GetMapping
    public Response<List<Config>> get(@RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        return Response.success(repository.findAllByUserId(user.getId()));
    }

    @GetMapping("/getByTypeAndTarget")
    public Response<List<Config>> getConfigsByTypeAndTargetId(@RequestParam("type") Integer type, @RequestParam("target_id") Integer targetId, @RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        return Response.success(repository.findAllByUserIdAndTypeAndTargetId(user.getId(), type, targetId));
    }

    @GetMapping("/{id}")
    public Response<Config> getConfigById(@PathVariable("id") Integer id, @RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        Config config = repository.findById(id).orElse(null);
        if (config != null && Objects.equals(config.getUserId(), user.getId())) {
            return Response.success(config);
        }
        return Response.success(null);
    }

    @PostMapping("/create")
    public Response<Config> create(@RequestBody Config config, @RequestAttribute("username") String username) {
        if (config == null || StringUtils.isEmpty(config.getConfigKey()) || StringUtils.isEmpty(config.getConfigValue()) || config.getTargetId() == null) {
            return Response.failed(ErrorCode.MISS_PARAMETER, ErrorMessage.MISS_PARAMETER);
        }
        if (config.getType() == null || (config.getType() != Config.TYPE_IMSDK && config.getType() != Config.TYPE_PLUGIN)) {
            return Response.failed(ErrorCode.PARAMETER_ERROR, ErrorMessage.PARAMETER_ERROR);
        }
        User user = userRepository.findByUsername(username);
        Config existConfig = repository.findConfigByConfigKeyAndUserIdAndTargetIdAndType(config.getConfigKey(), user.getId(), config.getTargetId(), config.getType());
        if (existConfig != null) {
            return Response.failed(ErrorCode.PARAMETER_ERROR, ErrorMessage.PARAMETER_ERROR);
        }
        if (config.getType() == Config.TYPE_PLUGIN) {
            Optional<Plugin> plugin = pluginRepository.findById(config.getTargetId());
            if (plugin.isEmpty()) {
                return Response.failed(ErrorCode.PARAMETER_ERROR, ErrorMessage.PARAMETER_ERROR);
            }
        }
        if (config.getType() == Config.TYPE_IMSDK) {
            Optional<Imsdk> imsdk = imsdkRepository.findById(config.getTargetId());
            if (imsdk.isEmpty()) {
                return Response.failed(ErrorCode.PARAMETER_ERROR, ErrorMessage.PARAMETER_ERROR);
            }
        }
        config.setUserId(user.getId());
        config = repository.save(config);
        return Response.success(config);
    }

    @PutMapping("/update")
    public Response<Config> update(@RequestBody Config config, @RequestAttribute("username") String username) {
        if (config == null || config.getId() == null) {
            return Response.failed(ErrorCode.MISS_PARAMETER, ErrorMessage.MISS_PARAMETER);
        }
        User user = userRepository.findByUsername(username);
        Config existConfig = repository.findById(config.getId()).orElse(null);
        if (existConfig == null || !Objects.equals(existConfig.getUserId(), user.getId())) {
            return Response.failed(ErrorCode.PARAMETER_ERROR, ErrorMessage.PARAMETER_ERROR);
        }
        if (!StringUtils.isEmpty(config.getConfigKey()) && !Objects.equals(config.getConfigKey(), existConfig.getConfigKey())) {
            Config configByKey = repository.findConfigByConfigKeyAndUserIdAndTargetIdAndType(config.getConfigKey(), user.getId(), existConfig.getTargetId(), existConfig.getType());
            if (configByKey != null) {
                return Response.failed(ErrorCode.PARAMETER_ERROR, ErrorMessage.PARAMETER_ERROR);
            } else {
                existConfig.setConfigKey(config.getConfigKey());
            }
        }
        if (!StringUtils.isEmpty(config.getConfigValue())) {
            existConfig.setConfigValue(config.getConfigValue());
        }
        existConfig = repository.save(existConfig);
        return Response.success(existConfig);
    }

    @DeleteMapping("/{id}")
    public Response<Config> delete(@PathVariable("id") Integer id, @RequestAttribute("username") String username) {
        Config config = repository.findById(id).orElse(null);
        if (config == null) {
            return Response.success(null);
        }
        User user = userRepository.findByUsername(username);
        if (!Objects.equals(config.getUserId(), user.getId())) {
            return Response.success(null);
        }
        repository.delete(config);
        return Response.success(config);
    }

}
