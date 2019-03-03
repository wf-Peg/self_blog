package com.pwf.domain;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by PWF on 2018/11/10.
 */
@Data
@Entity
@Proxy(lazy = false)
@Table(name = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 4671083981028788168L;

    @Id  // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "发件人昵称不能为空")
    private String contactUsername;

    @Email
    private String email;

    @NotEmpty(message = "邮件内容不能为空")
    private String message;

    @org.hibernate.annotations.CreationTimestamp
    private Date sendTime;    //发布时间

    protected Message() {
    }

    public Message(@NotEmpty(message = "标题不能为空") String contactUsername, @Email String email, @NotEmpty(message = "内容不能为空") String message) {
        this.contactUsername = contactUsername;
        this.email = email;
        this.message = message;
    }
}