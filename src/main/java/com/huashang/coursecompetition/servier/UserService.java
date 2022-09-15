package com.huashang.coursecompetition.servier;

import com.huashang.coursecompetition.domain.dto.UserDto;

/**
 * @author linjianhua
 * @date 2022/9/15
 */
public interface UserService {

    /**
     * 保存用户
     * @param userDto
     * @return
     */
    int save(UserDto userDto);

}
