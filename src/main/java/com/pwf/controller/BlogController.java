package com.pwf.controller;

import com.pwf.dao.BlogRepository;
import com.pwf.domain.Blog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by PWF on 2018/11/10.
 */
@RestController
@RequestMapping("/blogs")
@Api(tags = "博文逻辑控制层")
public class BlogController {
    @Autowired
    private BlogRepository repository;
    @GetMapping
    @ApiOperation(value = "根据title和content进行全文搜索")
    public List<Blog> findBlogs(@RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam(value = "pageIndex",defaultValue = "0") int pageIndex,
                                @RequestParam(value = "pageSize",defaultValue = "15") int pageSize
                                ){
        Pageable pageable= PageRequest.of(pageIndex,pageSize);
        Page<Blog> page = repository.findByTitleLikeOrContentLike(title, content, pageable);
        return page.getContent();
    }

    @GetMapping
    @RequestMapping("/list")
    public String listBlogs(@RequestParam(value="order",required=false,defaultValue="new") String order,
                            @RequestParam(value="keywords",required=false) String keywords) {
        System.out.print("order:" +order + ";keywords:" +keywords );
        return "redirect:/index?order="+order+"&keywords="+keywords;
    }

}
