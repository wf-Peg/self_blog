package com.pwf.controller;

import com.pwf.domain.Banner;
import com.pwf.domain.Blog;
import com.pwf.domain.Message;
import com.pwf.domain.User;
import com.pwf.service.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@Api(tags = "页面跳转控制层")
public class IndexController {
    private final String PREFIX = "background/";
    @Autowired
    private BannerService bannerService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CommentService commentService;

    @RequestMapping({"/","index"})
    @ApiOperation(value = "跳转前台首页")
    public String index(Model model) {
        Page<Banner> bannerList = bannerService.findAll(PageRequest.of(0,30));
//        List<Banner> bannerList = bannerService.findAll();
        Iterable<Blog> blogList=blogService.findBlogsByIsVisibleIsTrue(PageRequest.of(0,10));
        StringBuilder sb = new StringBuilder();
        for (Blog blog:blogService.getTop30Keywords()){
            String keywords = blog.getKeywords();
            String[] strs = keywords.split(",");
            for (String keyword:strs) {
                sb.append(keyword+",");
            }
        }

        model.addAttribute("bannerList", bannerList.getContent());
//        model.addAttribute("bannerList", bannerList);
        model.addAttribute("blogList", blogList);
        model.addAttribute("keywordList", sb);
        return "index";
    }

//    @GetMapping("/bannerIndex")
//    @ApiOperation(value = "跳转前台banner")
//    public String bannerIndex(Model model) {
//        List<Banner> bannerList = bannerService.findAll();
//        model.addAttribute("bannerList", bannerList);
//        return "index :: #portfolio";
//    }

    @RequestMapping("/userlogin")
    @ApiOperation(value = "跳转登陆页")
    public String loginPage() {
        return PREFIX + "login";
    }


    @RequestMapping("/background/index")
    @ApiOperation(value = "跳转后台首页")
    public String backgroundIndex(Model model) {
        Page<Blog> blogs = blogService.findBlogsByIsVisibleIsTrue(PageRequest.of(0, 5));
        Integer needLooks = blogService.findBlogsByIsVisibleIsFalseCount();
        User mostBlogsUser=userService.findMostBlogsUser();
        Integer messageCount = messageService.findAllCount();
        List<Message> allMessage = messageService.findAll();
        model.addAttribute("blogList",blogs.getContent());
        model.addAttribute("allBlogCount",blogService.findAllCount());
        model.addAttribute("allBannerCount",bannerService.findAllCount());
        model.addAttribute("allCommentCount",commentService.findAllCount());
        model.addAttribute("allUserCount",userService.count());
        model.addAttribute("mostBlogsUser",mostBlogsUser);
        model.addAttribute("needLooks",needLooks);
        model.addAttribute("messageCount",messageCount);
        model.addAttribute("allMessage",allMessage);
        return PREFIX + "index";
    }

    @RequestMapping("/blogDetail")
    @ApiOperation(value = "跳转博客详情")
    public String blogIndex() {
        return  "blog-detail";
    }

    @RequestMapping("/forgotPasswordIndex")
    @ApiOperation(value = "跳转忘记密码页面")
    public String forgotPassword() {
        return PREFIX + "forgot-password";
    }

    @RequestMapping("/portfolio-1")
    @ApiOperation(value = "跳转前台电影推荐页")
    public String getPortfolio() {
        return "portfolio-1";
    }

    @RequestMapping("/registerIndex")
    @ApiOperation(value = "跳转注册用户页面")
    private String register(){
        return PREFIX + "sign-up-admin";
    }

    @RequestMapping("/registerUserIndex")
    @ApiOperation(value = "跳转注册用户页面")
    private String registerUserIndex(){
        return PREFIX + "sign-up-user";
    }

}
