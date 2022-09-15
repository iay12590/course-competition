package com.huashang.coursecompetition.servier.impl;

import com.huashang.coursecompetition.dao.UserDao;
import com.huashang.coursecompetition.domain.dto.UserDto;
import com.huashang.coursecompetition.domain.po.UserPo;
import com.huashang.coursecompetition.servier.UserService;
import org.springframework.stereotype.Service;

/**
 * @author linjianhua
 * @date 2022/9/15
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public int save(UserDto userDto) {
        UserPo userPo = toPo(userDto);
        return userDao.save(userPo);
    }

    private UserPo toPo(UserDto userDto) {
        UserPo userPo = new UserPo();
        userPo.setUsername(userDto.getUsername());
        userPo.setPassword(userDto.getPassword());
        return userPo;
    }

}
