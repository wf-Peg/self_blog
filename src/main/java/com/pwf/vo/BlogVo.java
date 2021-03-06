package com.pwf.vo;

import com.github.rjeschke.txtmark.Processor;
import com.pwf.domain.Comment;
import lombok.Data;
import org.hibernate.annotations.Proxy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by PWF on 2018/11/10.
 */
@Data
@Entity
@Proxy(lazy = false)
@Document(indexName = "blog", type = "blog")//标识为文档
//@Document(indexName = "blog", type = "blog", shards = 1, replicas = 0, refreshInterval = "-1")
//@XmlRootElement // MediaType 转为 XML
public class BlogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id  // 主键
    private Long id; // 唯一标识 ES规定的string类型

//    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
//    @JoinColumn(name="user_id")
//    private User user;

    @NotEmpty(message = "标题不能为空")
    private String title;

    private String keylink;    //会员专享链接

    private String author;

    @NotEmpty(message = "摘要不能为空")
    private String summary;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
//    @Basic(fetch= FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    private String content;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
//    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    private String htmlContent; // 将 md 转为 html

    private int reading = 0; // 访问量、阅读量

    private int comments = 0;  // 评论量

    //    name是本类（表）在数据库的字段名，referencedColumnName 是关联类（表）在数据库中的关联字段名
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> commentList;

    private int likes = 0;  // 点赞量

    private String category;    //分类

    private String keywords;    //关键词

    //    @org.hibernate.annotations.CreationTimestamp
    @NotEmpty(message = "发布时间不能为空")
    private String releaseTime;    //发布时间

    private Date updateTime;    //发布时间

    private String image;    //图片地址

    private Boolean isVisible;    //设置博客是否可见

    public BlogVo() {  // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
    }

    public BlogVo(String title, String summary, String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public BlogVo(long id, String title, String summary, String content) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
        this.htmlContent = Processor.process(content);
    }

    public void setComments(List<Comment> commentList) {
        this.commentList = commentList;
        this.comments = this.commentList.size();
    }

    /**
     * 添加评论
     *
     * @param comment
     */
    public void addComment(Comment comment) {
        this.commentList.add(comment);
        this.comments = this.commentList.size();
    }

    /**
     * 删除评论
     *
     * @param commentId
     */
    public void removeComment(Long commentId) {
        for (int index = 0; index < this.commentList.size(); index++) {
            if (commentList.get(index).getId() == commentId) {
                this.commentList.remove(index);
                break;
            }
        }

        this.comments = this.commentList.size();
    }
}