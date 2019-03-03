//package com.pwf.dao;
//
//import com.pwf.domain.Blog;
//import com.pwf.service.BlogService;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * Created by PWF on 2018/11/10.
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class BlogRepositoryTest {
//    @Autowired
//    private BlogRepository repository;
//    private BlogService service;
//    @Before
//    public void init(){
////        service.deleteAll();
////        repository.save(new Blog("hello","123"));
////        repository.save(new Blog("hi","234"));
////        repository.save(new Blog("hi1","234"));
//    }
//    @Test
//    public void testFindByTitleLikeOrContentLike(){
////        service.examine(33L,true);
////        service.updateLikes(Long.parseLong("33"));
////        Pageable pageable=PageRequest.of(0,2);
////        Page<Blog> page = repository.findBlogsByIsVisibleIsFalse(pageable);
//////        Assert.assertEquals(page.getTotalElements(),2);
////        for (Blog blog:page.getContent()){
////            System.out.println(blog);
////        }
//
//    }
//}