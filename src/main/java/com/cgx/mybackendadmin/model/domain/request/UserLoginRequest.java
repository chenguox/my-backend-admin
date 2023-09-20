package com.cgx.mybackendadmin.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserLoginRequest implements Serializable {
    // 序列化：把对象转化为可传输的字节序列过程称为序列化.
    // 反序列化：把字节序列还原为对象的过程称为反序列化。
    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;
    private String userPassword;
}
