package com.gricko.telegram.service;

import com.gricko.telegram.model.User;
import com.gricko.telegram.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private  UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public User findByChatId(long id){
        return userRepo.findByChatId(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers(){
        return userRepo.findAll();
    }

    @Transactional
    public List<User> findNewUsers(){
        List<User> users = userRepo.findNewUsers();

        users.forEach((user) -> user.setNotified(true));
        userRepo.saveAll(users);

        return users;
    }

    @Transactional
    public void addUser(User user){
        user.setAdmin(userRepo.count() == 0);
        userRepo.save(user);
    }

    @Transactional
    public void updateUser(User user){
        userRepo.save(user);
    }
}
