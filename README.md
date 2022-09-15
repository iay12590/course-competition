# course-competition
#### 1、生成项目

打开https://start.spring.io/，选在SpringBoot版本、Jdk版本，填写相关项目信息，在右侧依赖管理中搜索WEB，添加spring web依赖
<img width="1253" alt="image" src="https://user-images.githubusercontent.com/20528525/190489250-1b5d7d3c-0a15-4877-ad06-d05ee5b2442c.png">

#### 2、下载项目，导入到idea

下载项目后，在pom.xml上右键使用idea打开，此时默认加载maven项目，加载介绍之后点击maven reload按钮，重新加载maven依赖
如果加载maven依赖错误：
- 确认网络情况
- 在pom.xml添加以下信息配置国内镜像，然后再重新加载maven依赖
```
<repositories>
		<repository>
			<id>central</id>
			<name>aliyun maven</name>
			<url>http://maven.aliyun.com/nexus/content/groups/public/</url>
			<layout>default</layout>
			<!-- 是否开启发布版构件下载 -->
			<releases>
				<enabled>true</enabled>
			</releases>
			<!-- 是否开启快照版构件下载 -->
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
		</repository>
	</repositories>
```
#### 3、集成Spring web
<img width="442" alt="image" src="https://user-images.githubusercontent.com/20528525/190490980-47ae441a-7b96-453a-8403-27622febb4c3.png">
创建UserController,添加两个接口，以此验证web环境是否生效
```
@RestController()
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") String id) {
        return id;
    }
}
```
通过http://127.0.0.1:8080/user/12访问，返回12表示正常

#### 4、集成Mybatis
##### 在pom.xml添加以下依赖
```
<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>2.2.2</version>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>
```
##### 修改application.yaml
```
spring:
  # 数据库配置
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://127.0.0.1:3306/cc_test?characterEncoding=UTF-8&autoReconnect=true&useSSL=false&allowMultiQueries=true
    driver-class-name: com.mysql.cj.jdbc.Driver
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.huashang.coursecompetition.domain.po
  configuration:
    map-underscore-to-camel-case: true
```
#### 在启动类CourseCompetitionApplication添加mapper扫描注解
```
@MapperScan("com.huashang.coursecompetition.dao")
```
#### 在resources目录下创建mapper文件夹
在mapper文件夹创建UserDaoMapper.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.huashang.coursecompetition.dao.UserDao">
    <insert id="save">
        insert into t_user(username, password) values (#{username}, #{password})
    </insert>
</mapper>
```
分别创建一下类：
```
package com.huashang.coursecompetition.dao;

import com.huashang.coursecompetition.domain.po.UserPo;

/**
 * @author linjianhua
 * @date 2022/9/15
 */
public interface UserDao {

    int save(UserPo userPo);

}
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

```
注：省略实体类，两个都是
