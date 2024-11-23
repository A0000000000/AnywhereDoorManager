package cn.maoyanluo.anywhere.door.repository;

import cn.maoyanluo.anywhere.door.entity.Imsdk;
import cn.maoyanluo.anywhere.door.entity.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImsdkRepository extends JpaRepository<Imsdk, Integer> {

    List<Imsdk> findByUserId(Integer userId);
    Imsdk findByImsdkNameAndUserId(String imsdkName, Integer userId);

}
