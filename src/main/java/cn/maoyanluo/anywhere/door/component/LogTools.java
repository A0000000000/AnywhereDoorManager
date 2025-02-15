package cn.maoyanluo.anywhere.door.component;

import cn.maoyanluo.anywhere.door.entity.Log;
import cn.maoyanluo.anywhere.door.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogTools {

    private static final Integer GLOBAL_LOG_USER_ID = -1;

    private final LogRepository logRepository;

    @Autowired
    public LogTools(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    private Log createLogInstance(String tag, String msg, Integer level, int userId) {
        Log log = new Log();
        log.setType(Log.TYPE_MANAGER);
        log.setTargetId(0);
        log.setTag(tag);
        log.setTimestamp(System.currentTimeMillis());
        log.setLog(msg);
        log.setLevel(level);
        log.setUserId(userId);
        return log;
    }

    public void d(String tag, String log, int userId) {
        logRepository.save(createLogInstance(tag, log, Log.LEVEL_DEBUG, userId));
    }

    public void i(String tag, String log, int userId) {
        logRepository.save(createLogInstance(tag, log, Log.LEVEL_INFO, userId));
    }

    public void w(String tag, String log, int userId) {
        logRepository.save(createLogInstance(tag, log, Log.LEVEL_WARN, userId));
    }

    public void e(String tag, String log, int userId) {
        logRepository.save(createLogInstance(tag, log, Log.LEVEL_ERROR, userId));
    }

    public void d(String tag, String log) {
        logRepository.save(createLogInstance(tag, log, Log.LEVEL_DEBUG, GLOBAL_LOG_USER_ID));
    }

    public void i(String tag, String log) {
        logRepository.save(createLogInstance(tag, log, Log.LEVEL_INFO, GLOBAL_LOG_USER_ID));
    }

    public void w(String tag, String log) {
        logRepository.save(createLogInstance(tag, log, Log.LEVEL_WARN, GLOBAL_LOG_USER_ID));
    }

    public void e(String tag, String log) {
        logRepository.save(createLogInstance(tag, log, Log.LEVEL_ERROR, GLOBAL_LOG_USER_ID));
    }

}
