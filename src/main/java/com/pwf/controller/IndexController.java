package com.pwf.controller;

import com.pwf.domain.*;
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

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Pageable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

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
    @Autowired
    private HttpServletRequest request;


    @RequestMapping({"/","index"})
    @ApiOperation(value = "跳转前台首页")
    public String index(Model model,HttpServletRequest request) {
        //todo redis 优化
        //取出前三十篇热门的文章的所有标签
        StringBuilder sb = null;
        try {
            sb = new StringBuilder();
            for (Blog blog:blogService.getTop30Keywords()){
                String keywords = blog.getKeywords();
                String[] strs = keywords.split(",");
                for (String keyword:strs) {
                    sb.append(keyword+",");
                }
            }
            System.out.println(sb);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("关键词为空导致空指针");
        }
        /*准备将所有标签去重,步骤：根据逗号转化为数组，准备第二个StringBuilder存储去重数据，
        最后使用LinkedHashSet去重并删除首个逗号*/
        String[]  arr = sb.toString().split(",");
        Set<String> set = new LinkedHashSet<>(Arrays.asList(arr));
        StringBuilder sb2 = new StringBuilder();
        for (String string : set) {
            sb2.append(",").append(string);
        }
        sb2.deleteCharAt(0);
        System.out.println(sb2);

        Page<Banner> bannerList = bannerService.findAll(new PageBean(0,40));
        model.addAttribute("bannerList", bannerList.getContent());
        model.addAttribute("bannerTotalPage", bannerList.getTotalPages());
        model.addAttribute("bannerCurrentPage", 0);

//        model.addAttribute("blogList", blogList.getContent());
        model.addAttribute("keywordList", sb2);

        return "index";
    }

//    @GetMapping("/bannerIndex")
//    @ApiOperation(value = "跳转前台banner")
//    public String bannerIndex(Model model,PageBean pageBean) {
//        Page<Banner> bannerList = bannerService.findAll(pageBean);
//        model.addAttribute("bannerList", bannerList.getContent());
//        return "index :: #portfolio_grid";
//    }

    @RequestMapping("/userlogin")
    @ApiOperation(value = "跳转登陆页")
    public String loginPage() {
        return PREFIX + "login";
    }


    @RequestMapping("/background/index")
    @ApiOperation(value = "跳转后台首页")
    public String backgroundIndex(Model model) {
        Page<Blog> blogs = blogService.findBlogsByIsVisibleIsTrue(new PageBean(0,5));
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
        //获取ip
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if(ip.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip= inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
//        System.out.println(ip);
        model.addAttribute("ip", ip);

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
