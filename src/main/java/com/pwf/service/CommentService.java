package com.pwf.service;

import javax.transaction.Transactional;

import com.pwf.dao.CommentRepository;
import com.pwf.domain.Blog;
import com.pwf.domain.Comment;
import com.pwf.domain.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Comment 服务.
 */
@Service
@Transactional
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	public void removeComment(Long id) {
		commentRepository.deleteById(id);
	}

	public Page<Comment> findAll(PageBean pageBean) {
		return commentRepository.findAll(PageRequest.of(pageBean.getPage(),pageBean.getSize()));
	}

	public Comment getCommentById(Long id) {
		Optional<Comment> byId = commentRepository.findById(id);
		return byId.isPresent()?byId.get():null;
	}
	public List<Comment> getCommentByBlogId(Long id) {
		List<Comment> byId = commentRepository.getCommentsByBlogId(id);
		return byId;
	}


	public Page<Comment> listCommentsByNameLike(String searchText, Pageable pageable) {
		// 模糊查询
		searchText = "%" + searchText + "%";
//        Page<User> users = repository.findByNameLike(name, pageable);
		Page<Comment> blogs = commentRepository.findByContentLike(searchText, pageable);
		return blogs;
	}

	public Integer findAllCount() {
		return commentRepository.findAll().size();
	}

}
