package com.pwf.dao;

import com.pwf.domain.Blog;
import com.pwf.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Comment 仓库.
 */
public interface CommentRepository extends JpaRepository<Comment, Long>{

    Page<Comment> findByContentLike(String searchText, Pageable pageable);
    List<Comment> getCommentsByBlogId(Long id);
}
