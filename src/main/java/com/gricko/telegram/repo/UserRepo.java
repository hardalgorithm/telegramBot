package com.gricko.telegram.repo;

import com.gricko.telegram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    @Query("SELECT u from User u WHERE u.notified = false AND u.phone IS NOT NULL AND u.email IS NOT NULL")
    List<User> findNewUsers();

    User findByChatId(long id);
}
