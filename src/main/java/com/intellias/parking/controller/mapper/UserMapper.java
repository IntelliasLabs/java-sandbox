package com.intellias.parking.controller.mapper;

import com.intellias.parking.controller.dto.UserDTO;
import com.intellias.parking.persistence.entity.user.User;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(final User user);

    List<UserDTO> listToDTO(final List<User> users);

    User toEntity(UserDTO userById);
}
