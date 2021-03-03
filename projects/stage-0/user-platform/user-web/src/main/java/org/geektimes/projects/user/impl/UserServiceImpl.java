package org.geektimes.projects.user.impl;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.UserRepository;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.web.mvc.myannotation.MyService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author GH
 */
@MyService
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;


    @Override
    public boolean register(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User queryUserById(Long id) {
        return null;
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }
}
