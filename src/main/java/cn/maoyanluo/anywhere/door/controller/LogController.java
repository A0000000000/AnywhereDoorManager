package cn.maoyanluo.anywhere.door.controller;

import cn.maoyanluo.anywhere.door.bean.Response;
import cn.maoyanluo.anywhere.door.constant.ErrorCode;
import cn.maoyanluo.anywhere.door.constant.ErrorMessage;
import cn.maoyanluo.anywhere.door.entity.Log;
import cn.maoyanluo.anywhere.door.entity.User;
import cn.maoyanluo.anywhere.door.repository.LogRepository;
import cn.maoyanluo.anywhere.door.repository.UserRepository;
import cn.maoyanluo.anywhere.door.component.LogTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {

    private final LogRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public LogController(LogRepository repository, UserRepository userRepository, LogTools logTools) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @GetMapping("/getLogList/{type}/{id}")
    public Response<List<Log>> getLogList(@RequestAttribute("username") String username, @PathVariable("type") int type, @PathVariable("id") int id) {
        if (type != Log.TYPE_MANAGER && type != Log.TYPE_CONTROL_PLANE && type != Log.TYPE_PLUGIN && type != Log.TYPE_IMSDK) {
            return Response.failed(ErrorCode.ERROR_TYPE, ErrorMessage.ERROR_TYPE);
        }
        if (type == Log.TYPE_MANAGER || type == Log.TYPE_CONTROL_PLANE) {
            id = 0;
        }
        User user = userRepository.findByUsername(username);
        return Response.success(repository.findAllByUserIdAndTypeAndTargetId(user.getId(), type, id));
    }

    @GetMapping("/getLogByLevel/{type}/{id}/{levels}")
    public Response<List<Log>> getLogListByLevel(@RequestAttribute("username") String username, @PathVariable("type") int type, @PathVariable("id") int id, @PathVariable("levels") List<Integer> levels) {
        if (type != Log.TYPE_MANAGER && type != Log.TYPE_CONTROL_PLANE && type != Log.TYPE_PLUGIN && type != Log.TYPE_IMSDK) {
            return Response.failed(ErrorCode.ERROR_TYPE, ErrorMessage.ERROR_TYPE);
        }
        if (levels != null && !levels.isEmpty()) {
            for (int i = 0; i < levels.size(); i++) {
                if (levels.get(i) < 1) {
                    levels.set(i, 1);
                }
                if (levels.get(i) > 4) {
                    levels.set(i, 4);
                }
            }
        }
        if (type == Log.TYPE_MANAGER || type == Log.TYPE_CONTROL_PLANE) {
            id = 0;
        }
        User user = userRepository.findByUsername(username);
        return Response.success(repository.findLogsByOneItem(user.getId(), type, id, levels));
    }

    @DeleteMapping("/deleteLogList/{type}/{id}")
    @Transactional
    public Response<Void> deleteLogList(@RequestAttribute("username") String username, @PathVariable("type") int type, @PathVariable("id") int id) {
        if (type != Log.TYPE_MANAGER && type != Log.TYPE_CONTROL_PLANE && type != Log.TYPE_PLUGIN && type != Log.TYPE_IMSDK) {
            return Response.failed(ErrorCode.ERROR_TYPE, ErrorMessage.ERROR_TYPE);
        }
        if (type == Log.TYPE_MANAGER || type == Log.TYPE_CONTROL_PLANE) {
            id = 0;
        }
        User user = userRepository.findByUsername(username);
        repository.deleteByUserIdAndTypeAndTargetId(user.getId(), type, id);
        return Response.success(null);
    }

    @DeleteMapping("/deleteLogByLevel/{type}/{id}/{levels}")
    @Transactional
    public Response<Void> deleteLogList(@RequestAttribute("username") String username, @PathVariable("type") int type, @PathVariable("id") int id, @PathVariable("levels") List<Integer> levels) {
        if (type != Log.TYPE_MANAGER && type != Log.TYPE_CONTROL_PLANE && type != Log.TYPE_PLUGIN && type != Log.TYPE_IMSDK) {
            return Response.failed(ErrorCode.ERROR_TYPE, ErrorMessage.ERROR_TYPE);
        }
        if (levels != null && !levels.isEmpty()) {
            for (int i = 0; i < levels.size(); i++) {
                if (levels.get(i) < 1) {
                    levels.set(i, 1);
                }
                if (levels.get(i) > 4) {
                    levels.set(i, 4);
                }
            }
        }
        if (type == Log.TYPE_MANAGER || type == Log.TYPE_CONTROL_PLANE) {
            id = 0;
        }
        User user = userRepository.findByUsername(username);
        repository.deleteLogsByOneItem(user.getId(), type, id, levels);
        return Response.success(null);
    }

}
