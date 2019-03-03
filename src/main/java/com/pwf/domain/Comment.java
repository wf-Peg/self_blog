package com.pwf.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Comment 实体
 */
@Entity // 实体
@Data
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
	private Long id; // 用户的唯一标识

	@Size(min=2, max=500,message = "内容不能为空，至少两个字符")
	@Column(nullable = false) // 映射为字段，值不能为空
	private String content;

	@Size(min=2, max=50,message = "用户名不能为空，至少两个字符" )
	private String commentUsername;
	
	@Column(nullable = false) // 映射为字段，值不能为空
	@org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
	private Timestamp createTime;

	private Long blogId;
 
	protected Comment() {
	}
	public Comment(String commentUsername, String content) {
		this.content = content;
		this.commentUsername = commentUsername;
	}

	public Comment(String commentUsername, String content,Long blogId) {
		this.content = content;
		this.commentUsername = commentUsername;
		this.blogId=blogId;
	}
 
}
