package cn.maoyanluo.anywhere.door.controller;

import cn.maoyanluo.anywhere.door.bean.Response;
import cn.maoyanluo.anywhere.door.constant.ErrorCode;
import cn.maoyanluo.anywhere.door.constant.ErrorMessage;
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
                return Response.failed(ErrorCode.NO_PERMISSION, ErrorMessage.NO_PERMISSION);
            }
        } else {
            return Response.failed(ErrorCode.NO_ENTITY, ErrorMessage.NO_ENTITY);
        }
    }

    @GetMapping
    public Response<List<Imsdk>> getImsdks(@RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        List<Imsdk> imsdks = repository.findByUserId(user.getId());
        return Response.success(imsdks);
    }

    @PostMapping("/create")
    public Response<Imsdk> create(@RequestBody Imsdk imsdk, @RequestAttribute("username") String username) {
        if (StringUtils.isEmpty(imsdk.getImsdkName())
        || StringUtils.isEmpty(imsdk.getImsdkHost())
        || imsdk.getImsdkPort() == null
        || StringUtils.isEmpty(imsdk.getImsdkToken())
        ) {
            return Response.failed(ErrorCode.MISS_PARAMETER, ErrorMessage.MISS_PARAMETER);
        }
        if (imsdk.getImsdkName().contains(" ")) {
            return Response.failed(ErrorCode.WRONG_NAME, ErrorMessage.WRONG_NAME);
        }
        User user = userRepository.findByUsername(username);
        Integer userId = user.getId();
        Imsdk byImsdkName = repository.findByImsdkNameAndUserId(imsdk.getImsdkName(), userId);
        if (byImsdkName != null) {
            return Response.failed(ErrorCode.NAME_EXISTS, ErrorMessage.NAME_EXISTS);
        }
        imsdk.setUserId(userId);
        imsdk.setIsActive(1);
        imsdk = repository.save(imsdk);
        return Response.success(imsdk);
    }

    @PutMapping("/update")
    public Response<Imsdk> update(@RequestBody Imsdk imsdk, @RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        Optional<Imsdk> currentImsdkOptional = repository.findById(imsdk.getId());
        if (currentImsdkOptional.isEmpty()) {
            return Response.failed(ErrorCode.MISS_PARAMETER, ErrorMessage.MISS_PARAMETER);
        }
        Imsdk currentImsdk = currentImsdkOptional.get();
        if (!Objects.equals(currentImsdk.getUserId(), user.getId())) {
            return Response.failed(ErrorCode.NO_PERMISSION, ErrorMessage.NO_PERMISSION);
        }
        Imsdk byImsdkName = repository.findByImsdkNameAndUserId(imsdk.getImsdkName(), user.getId());
        if (byImsdkName != null) {
            return Response.failed(ErrorCode.NAME_EXISTS, ErrorMessage.NAME_EXISTS);
        }
        if (!StringUtils.isEmpty(imsdk.getImsdkName())) {
            currentImsdk.setImsdkName(imsdk.getImsdkName());
        }
        if (!StringUtils.isEmpty(imsdk.getImsdkDescribe())) {
            currentImsdk.setImsdkDescribe(imsdk.getImsdkDescribe());
        }
        if (!StringUtils.isEmpty(imsdk.getImsdkHost())) {
            currentImsdk.setImsdkHost(imsdk.getImsdkHost());
        }
        if (imsdk.getImsdkPort() != null) {
            currentImsdk.setImsdkPort(imsdk.getImsdkPort());
        }
        if (!StringUtils.isEmpty(imsdk.getImsdkPrefix())) {
            currentImsdk.setImsdkPrefix(imsdk.getImsdkPrefix());
        }
        if (!StringUtils.isEmpty(imsdk.getImsdkToken())) {
            currentImsdk.setImsdkToken(imsdk.getImsdkToken());
        }
        if (imsdk.getIsActive() != null) {
            currentImsdk.setIsActive(imsdk.getIsActive());
        }
        currentImsdk = repository.save(currentImsdk);
        return Response.success(currentImsdk);
    }

    @DeleteMapping("/{id}")
    public Response<Imsdk> delete(@PathVariable("id") Integer id, @RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        Optional<Imsdk> currentImsdkOptional = repository.findById(id);
        if (currentImsdkOptional.isEmpty()) {
            return Response.success(null);
        }
        Imsdk currentImsdk = currentImsdkOptional.get();
        if (!Objects.equals(currentImsdk.getUserId(), user.getId())) {
            return Response.failed(ErrorCode.NO_PERMISSION, ErrorMessage.NO_PERMISSION);
        }
        repository.delete(currentImsdk);
        return Response.success(currentImsdk);
    }

}
