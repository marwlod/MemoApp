package io.github.marwlod.memoapp.service;

import io.github.marwlod.memoapp.entity.User;
import io.github.marwlod.memoapp.entity.VerificationToken;

public interface UserService {

    User findUserByEmail(String email);

    User saveUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredUser(User user);
}
