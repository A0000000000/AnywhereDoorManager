package cn.maoyanluo.anywhere.door.repository;

import cn.maoyanluo.anywhere.door.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {

    List<Config> findAllByUserId(Integer userId);
    List<Config> findAllByUserIdAndTypeAndTargetId(Integer userId, Integer type, Integer targetId);
    Config findConfigByConfigKeyAndUserIdAndTargetIdAndType(String configKey, Integer userId, Integer targetId, Integer type);
    @Transactional
    @Modifying
    void deleteConfigByTargetIdAndTypeAndUserId(Integer targetId, Integer type, Integer userId);

}
