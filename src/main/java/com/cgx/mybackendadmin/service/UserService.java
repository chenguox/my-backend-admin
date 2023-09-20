package com.cgx.mybackendadmin.service;

import com.cgx.mybackendadmin.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;


/** 用户服务
* @author 52948
* @description 针对表【user】的数据库操作Service
* @createDate 2023-09-19 10:02:55
*/
public interface UserService extends IService<User> {
    /**
     * 用户注释
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验棉麻
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);
}
