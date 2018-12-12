package io.github.marwlod.memoapp.repository;

import io.github.marwlod.memoapp.entity.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
