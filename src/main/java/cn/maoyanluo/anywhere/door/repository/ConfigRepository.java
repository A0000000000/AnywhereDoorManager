package cn.maoyanluo.anywhere.door.repository;

import cn.maoyanluo.anywhere.door.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {

    List<Config> findAllByUserId(Integer userId);
    Config findConfigByConfigKeyAndUserId(String configKey, int userId);

}
