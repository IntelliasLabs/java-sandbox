package com.intellias.parking.service.user;

import com.intellias.parking.controller.dto.UserDTO;
import com.intellias.parking.controller.mapper.UserMapper;
import com.intellias.parking.persistence.entity.user.User;
import com.intellias.parking.persistence.repository.UserRepository;
import com.intellias.parking.service.exception.RecordNotFoundException;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userMapper.listToDTO(userRepository.findAll());
    }

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("user", userId));

        return userMapper.toDTO(user);
    }
}
