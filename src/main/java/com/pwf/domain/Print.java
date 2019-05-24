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
@Table(name = "print")
public class Print implements Serializable{

    private static final long serialVersionUID = -6858593469611808785L;
    //图片id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "文件名称不能为空")
    private String filename;

    @NotEmpty(message = "文件不能为空")
    private String filepath;

//    private String filegroup;

    protected Print() {
    }

    public Print(@NotEmpty(message = "文件名称不能为空") String filename, String filepath) {
        this.filename = filename;
        this.filepath = filepath;
//        this.filegroup = filegroup;
    }
}
