package com.intellias.parking.controller;

import com.intellias.parking.controller.dto.UserDTO;
import com.intellias.parking.service.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.API_VERSION + UserController.PATH)
@RequiredArgsConstructor
public class UserController {

    public final static String PATH = "users";
    public final static String API_VERSION = "/api/v1/";
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
