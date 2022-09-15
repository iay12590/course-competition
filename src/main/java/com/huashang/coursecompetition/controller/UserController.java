package com.huashang.coursecompetition.controller;

import com.huashang.coursecompetition.domain.dto.ApiResult;
import com.huashang.coursecompetition.domain.dto.UserDto;
import com.huashang.coursecompetition.servier.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linjianhua
 * @date 2022/9/15
 */
@RestController()
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResult<?> createUser(@RequestBody UserDto userDto) {
        userService.save(userDto);
        return ApiResult.success();
    }

    @GetMapping("/{id}")
    public ApiResult<?> getUser(@PathVariable("id") String id) {
        return ApiResult.success(id);
    }

}
