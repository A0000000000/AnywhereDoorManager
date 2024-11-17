package cn.maoyanluo.anywhere.door.repository;

import cn.maoyanluo.anywhere.door.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}
