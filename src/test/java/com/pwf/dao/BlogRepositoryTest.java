package com.pwf.dao;

import com.pwf.domain.Blog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by PWF on 2018/11/10.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BlogRepositoryTest {
    @Autowired
    private BlogRepository repository;
    @Before
    public void init(){
        repository.deleteAll();
        repository.save(new Blog("hello","123"));
        repository.save(new Blog("hi","234"));
        repository.save(new Blog("hi1","234"));
    }
    @Test
    public void testFindByTitleLikeOrContentLike(){
        Pageable pageable=PageRequest.of(0,10);
        Page<Blog> page = repository.findByTitleLikeOrContentLike("hi1", "234", pageable);
        Assert.assertEquals(page.getTotalElements(),2);
        for (Blog blog:page.getContent()){
            System.out.println(blog);
        }

    }
}