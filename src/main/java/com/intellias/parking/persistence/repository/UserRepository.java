package com.intellias.parking.persistence.repository;

import com.intellias.parking.persistence.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
