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
分别创建以下类：
```
/**
 * @author linjianhua
 * @date 2022/9/15
 */
public interface UserDao {

    int save(UserPo userPo);

}

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

```
注：省略实体类

通过Postman或者idea tools httpclient分别调用两个接口：
```
Post: http://127.0.0.1:8080/user
{
    "username": "lin",
    "password": "123456"
}

Get: http://127.0.0.1:8080/user/12
```

#### 集成spring security
##### 添加maven依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

##### 继承 WebSecurityConfigurerAdapter 自定义 Spring Security 配置

```
/**
 * @author linjianhua
 * @date 2022/9/15
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final UserDetailsService userDetailsService;

    public SecurityConfig(CustomAccessDeniedHandler customAccessDeniedHandler, UserDetailsService userDetailsService) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                // 不拦截登陆请求
                .antMatchers("/login")
                .permitAll()
                // user接口必须有USER角色
                .antMatchers("/user", "/user/**")
                .hasAuthority("USER")
                // 其他接口通过rbacPermission.hasPermission判定是否有权限
                .anyRequest()
                .access("@rbacPermission.hasPermission(request, authentication)")
                .and()
                // 表单登陆
                .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                // 登陆成功跳转页面
                .defaultSuccessUrl("/index.html")
                .and()
                // 异常处理
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .logout()
                // 登出
                .logoutUrl("/login?logout");
    }
    /**
    * 密码加密算法
    */
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

##### 自定义实现 UserDetails 接口，扩展用户属性
```
public class UserEntity implements UserDetails {

    private String username;
    private String password;

    private String role;
    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    public UserEntity(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.authorities.add(new SimpleGrantedAuthority(role));
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

#### 自定义实现 UserDetailsService 接口
```
@Component
public class UserDetailServiceImpl implements UserDetailsService {
    /**
    * 根据用户名称查询用户信息(密码、权限、角色)
    */
    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("lin".equals(username)) {
            return new UserEntity(username, new BCryptPasswordEncoder().encode("123456"), "USER");
        }
        return new UserEntity(username, new BCryptPasswordEncoder().encode("12345678"), "ADMIN");
    }
}
```