package io.github.marwlod.memoapp.service;

import io.github.marwlod.memoapp.entity.Role;
import io.github.marwlod.memoapp.entity.User;
import io.github.marwlod.memoapp.repository.RoleRepository;
import io.github.marwlod.memoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;

@Service("userService")
public class UserServiceImpl  implements UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        Role role = roleRepository.findByName("USER");
        if (role == null) {
            role = new Role();
            role.setName("USER");
        }
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        userRepository.save(user);
    }

}