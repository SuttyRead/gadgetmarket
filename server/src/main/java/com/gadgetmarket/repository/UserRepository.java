package com.gadgetmarket.repository;


import com.gadgetmarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);
    void deleteUserById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsById(Long id);

    Optional<User> findOneByUsername(String username);
}
