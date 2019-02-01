package com.pwf.domain;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by PWF on 2019/1/17.
 */
@Data
@Entity
@Proxy(lazy = false)
public class Banner implements Serializable{

    private static final long serialVersionUID = -6858593469611808385L;
    //图片id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //图片名字:title名（项目名）、
    private String name;
    //图片链接:img文件地址、
    private String img;
    //图片key：data-groups、标签名、i 图标class的值
    private String bannerkey;

    protected Banner() {
    }

    public Banner(String name, String img, String bannerkey) {
        this.name = name;
        this.img = img;
        this.bannerkey = bannerkey;
    }
}
