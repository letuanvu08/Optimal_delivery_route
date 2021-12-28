package com.optimalDeliveryRoute.WebAPI.Repository;

import com.optimalDeliveryRoute.WebAPI.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
@Service
@Repository
@Transactional
public interface UserRepository extends
        JpaRepository<User, Long> {

    @Query(value ="SELECT * FROM user WHERE user.username = ?1",nativeQuery = true)
    Optional<User> findUserByName(String name);

    @Modifying
    @Query(value = "update user set user.password= ?2 where user.username= ?1",nativeQuery = true)
    void ChangePassword(String username,String newPassword);
}
