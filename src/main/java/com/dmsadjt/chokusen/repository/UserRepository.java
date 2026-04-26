package com.dmsadjt.chokusen.repository;

import com.dmsadjt.chokusen.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    public User findByEmail(String email);
    public User findByUsername(String username);
}
