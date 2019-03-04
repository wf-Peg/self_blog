package com.pwf.domain;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by PWF on 2019/1/17.
 */
@Data
@Entity
@Proxy(lazy = false)
@Table(name = "banner")
public class Banner implements Serializable{

    private static final long serialVersionUID = -6858593469611808385L;
    //图片id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //图片名字:title名（项目名）、

    @NotEmpty(message = "图片名称不能为空")
    private String name;
    //图片链接:img文件地址、

//    @NotEmpty(message = "图片不能为空")
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
