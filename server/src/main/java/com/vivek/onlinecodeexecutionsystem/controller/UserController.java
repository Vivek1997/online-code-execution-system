package com.vivek.onlinecodeexecutionsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("user")
public class UserController {
    @GetMapping("/getHello")
    public ResponseEntity<String> getHello() {
        return ResponseEntity.ok("Hello");
    }

}
