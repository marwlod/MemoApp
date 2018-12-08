package io.github.marwlod.memoapp.repository;

import io.github.marwlod.memoapp.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String roleName);
}
