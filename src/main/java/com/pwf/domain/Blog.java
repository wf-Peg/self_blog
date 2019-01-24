package com.pwf.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by PWF on 2018/11/10.
 */
@Data
@Document(indexName = "blog", type = "blog", shards = 1, replicas = 0, refreshInterval = "-1")
//@XmlRootElement // MediaType 转为 XML
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id  // 主键
    private String id; // 用户的唯一标识 ES规定的string类型

    @NotEmpty(message = "标题不能为空")
    @Size(min=2, max=50)
    @Column(nullable = false, length = 50) // 映射为字段，值不能为空
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(min=2, max=300)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String summary;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch= FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String htmlContent; // 将 md 转为 html

    @Column(name="reading")
    private Long reading = 0L; // 访问量、阅读量

    @Column(name="comments")
    private Long comments = 0L;  // 评论量

    @Column(name="likes")
    private Long likes = 0L;  // 点赞量

    @Column(name="blogCategory")
    private String blogCategory;    //分类

    @Column(name="keywords")
    private String keywords;    //关键词

    @Column(name="releaseTime")
    private Date releaseTime;    //发布时间

    @Column(name="updateTime")
    private Date updateTime;    //发布时间

    @Column(name="image")
    private String image;    //图片地址

    @Column(name="isVisible")
    private Boolean isVisible;    //设置博客是否可见

    protected Blog() {  // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
    }

    public Blog(String name, String content) {
        this.title = name;
        this.content = content;
    }

    public Blog(String id, String name, String content) {
        this.id = id;
        this.title = name;
        this.content = content;
    }
}