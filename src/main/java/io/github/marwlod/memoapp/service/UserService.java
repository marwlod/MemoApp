package io.github.marwlod.memoapp.service;

import io.github.marwlod.memoapp.entity.User;

public interface UserService {
    User findUserByEmail(String email);

    void saveUser(User user);
}
