package com.huashang.coursecompetition.permission;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author linjianhua
 * @date 2022/9/15
 */
@Component("rbacPermission")
public class RbacPermission {

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        return true;
    }

}
