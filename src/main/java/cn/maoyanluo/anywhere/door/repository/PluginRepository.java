package cn.maoyanluo.anywhere.door.repository;

import cn.maoyanluo.anywhere.door.entity.Plugin;
import cn.maoyanluo.anywhere.door.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PluginRepository extends JpaRepository<Plugin, Integer> {

    List<Plugin> findByUserId(Integer userId);
    Plugin findByPluginNameAndUserId(String pluginName, Integer userId);

}
