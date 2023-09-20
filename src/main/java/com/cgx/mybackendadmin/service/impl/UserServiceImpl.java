package com.cgx.mybackendadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cgx.mybackendadmin.model.domain.User;
import com.cgx.mybackendadmin.service.UserService;
import com.cgx.mybackendadmin.Mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 52948
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-09-19 10:02:55
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "cgx";

    /**
     * 用户登录状态键
     */
    public static final String USER_LOGIN_STATE = "userLoginState";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        if (matcher.find()) {
            return -1;
        }

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount); // =sql where
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }

        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        // todo 修改为自定义异常
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }

        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        if (matcher.find()) {
            return null;
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount Cannot match userPassword");
            return null;
        }

        // 3. 用户脱敏
        User satetyUser = new User();
        satetyUser.setId(user.getId());
        satetyUser.setUsername(user.getUsername());
        satetyUser.setUserAccount(user.getUserAccount());
        satetyUser.setAvatarUrl(user.getAvatarUrl());
        satetyUser.setGender(user.getGender());
        satetyUser.setEmail(user.getEmail());
        satetyUser.setUserStatus(user.getUserStatus());
        satetyUser.setPhone(user.getPhone());
        satetyUser.setCreateTime(user.getCreateTime());

        // 4. 记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, satetyUser);

        return satetyUser;
    }
}




