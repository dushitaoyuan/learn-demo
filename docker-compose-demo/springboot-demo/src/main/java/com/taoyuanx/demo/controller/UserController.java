package com.taoyuanx.demo.controller;

import com.taoyuanx.demo.entity.UserEntity;
import com.taoyuanx.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author dushitaoyuan
 * @date 2020/8/11
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("{id}")
    public UserEntity get(@PathVariable("id") Long id) {
        return userService.getById(id);
    }
    @PutMapping
    public String save(@RequestBody  UserEntity userEntity) {
         userService.save(userEntity);
         return "ok";
    }

    @PostMapping
    public String update(@RequestBody  UserEntity userEntity) {
        userService.update(userEntity);
        return "ok";
    }
}
