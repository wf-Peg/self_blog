package com.pwf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import com.pwf.domain.Blog;
import com.pwf.domain.Comment;
import com.pwf.domain.User;
import com.pwf.service.BlogService;
import com.pwf.service.CommentService;
import com.pwf.util.ConstraintViolationExceptionHandler;
import com.pwf.vo.ResultVO;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.runtime.Log;

/**
 * 主页控制器.
 * 
 * @since 1.0.0 2017年3月8日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
@Controller
@RequestMapping("/comments")
public class CommentController {
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CommentService commentService;

	/**
	 * 前台获取评论列表
	 * @return
	 */
	@GetMapping
	public String getComments(@RequestParam(value="blogId",required=true) Long blogId, Model model) {
//		Blog blog = blogService.findById(blogId);
//		List<Comment> comments = blog.getCommentList();
//		在此处通过commentService查不用blogService增加数据读取效率，“根据博文id查看博文详情页面”这个借口则可以使用redis
//		blogService.readingIncrease(blogId);
		List<Comment> comments = commentService.getCommentByBlogId(blogId);
		model.addAttribute("comments", comments);
		return "blog-detail :: #commentContainer";
	}

	/**
	 * 后台获取评论列表
	 * @return
	 */
	@GetMapping("/list")
	public String listComments(Model model) {
		model.addAttribute("comments", commentService.findAll());
		return "background/comments-tables";
	}

	/**
	 * 发表评论
	 * @param blogId
	 * @param commentUsername
	 * @param content
	 * @return
	 */
	@PostMapping
	@ResponseBody
	public ResultVO createComment(String blogId, String commentUsername,String content) {
		try {
			blogService.createComment(Long.valueOf(blogId),commentUsername, content);
		} catch (ConstraintViolationException e)  {
			return  new ResultVO(false, ConstraintViolationExceptionHandler.getMessage(e));
		} catch (Exception e) {
			return new ResultVO(false, e.getMessage());
		}
//		return ResponseEntity.ok().body(new ResultVO(true, "发表评论成功!",commentService.getCommentById(blogId)));
		return new ResultVO(true, "发表评论成功!");
	}



	/**
	 * 删除评论
	 * @return
	 */
	@DeleteMapping("/{id}")
	@ResponseBody
	public Map<String, Object> deleteBlog(@PathVariable("id") Long id, Long blogId) throws Exception{
		Map<String, Object> r = new HashMap<>();
		try {
			blogService.removeComment(blogId, id);
			commentService.removeComment(id);
		} catch (Exception e)  {
			r.put("code", 1);//删除成功
			r.put("msg", "删除评论失败");
			return r;
		}
		r.put("code", 0);//删除成功
		r.put("msg", "删除评论成功");
		return r;
	}


	@GetMapping("/commentSearch")
	public String list(@RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
							 Model model) {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Comment> page = commentService.listCommentsByNameLike(searchText, pageable);
		List<Comment> list = page.getContent();    // 当前所在页面数据列表
		model.addAttribute("comments", list);
		return "background/comments-tables";
	}

}
