package com.pwf.domain;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Data
@Proxy(lazy = false)
public class User implements Serializable {
    private static final long serialVersionUID = -5889846202649492920L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Size(min=2, max=20,message = "用户名要求2-20个字符")
    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @Column(length = 20)
    @NotEmpty(message = "密码不能为空")
    @Size(min=3, max=20,message = "密码要求3-20个字符")
    private String passWord;

    @NotEmpty(message = "邮件地址必填")
    @Email(message = "不是一个合法的电子邮件地址")
    private String email;

    private String userRole;

    private Integer blogCount=0;

    protected User() {
    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.userRole = "user";
    }

    public User(String userName, String passWord, String userRole) {
        this.userName = userName;
        this.passWord = passWord;
        this.userRole = userRole;
    }
}
