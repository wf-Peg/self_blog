package com.pwf.domain;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Entity
@Data
@Proxy(lazy = false)
public class User implements Serializable {
    private static final long serialVersionUID = -5889846202649492920L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String userName;

    @Column(length = 20)
    private String passWord;

    @Email(message = "不是一个合法的电子邮件地址")
    private String email;

    private String userRole;

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
