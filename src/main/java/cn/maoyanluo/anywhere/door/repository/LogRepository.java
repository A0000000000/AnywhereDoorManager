package cn.maoyanluo.anywhere.door.repository;

import cn.maoyanluo.anywhere.door.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByUserIdAndTypeAndTargetId(Integer userId, Integer type, Integer targetId);

    @Query(
            value = "select * from t_log where user_id = :user_id and type = :type and target_id = :target_id and level in (:levels)",
            nativeQuery = true
    )
    List<Log> findLogsByOneItem(@Param("user_id") Integer userId, @Param("type") Integer type, @Param("target_id") Integer targetId, @Param("levels") List<Integer> levels);

    @Modifying
    void deleteByUserIdAndTypeAndTargetId(Integer userId, Integer type, Integer targetId);


    @Modifying
    @Query(
            value = "delete from t_log where user_id = :user_id and type = :type and target_id = :target_id and level in (:levels)",
            nativeQuery = true
    )
    void deleteLogsByOneItem(@Param("user_id") Integer userId, @Param("type") Integer type, @Param("target_id") Integer targetId, @Param("levels") List<Integer> levels);

}
