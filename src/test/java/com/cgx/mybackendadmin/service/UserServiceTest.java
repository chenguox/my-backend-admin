package com.cgx.mybackendadmin.service;

import com.cgx.mybackendadmin.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("cgx");
        user.setUserAccount("condercgx");
        user.setAvatarUrl("https://codercgx-1308086317.cos.ap-nanjing.myqcloud.com/code/202307200119778.jpg");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setEmail("12345678@qq.com");
        user.setPhone("15011111111");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {
        // 密码为空
        String userAccount = "codercgx";
        String userPassword = "";
        String checkPassword = "123456";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 账号长度小于4为
        userAccount = "cc";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 密码长度小于 8位
        userAccount = "codercgx";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 账号有空格
        userAccount = "yu pi";
        userPassword = "1234567890";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 两次密码不一致
        userAccount = "codercgx";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 正确
        userAccount = "codercgx";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);
    }
}