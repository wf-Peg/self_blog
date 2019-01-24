package com.pwf.controller;

import com.pwf.domain.Banner;
import com.pwf.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Api(tags = "页面跳转控制层")
public class IndexController {
    private final String PREFIX = "background/";
    @Autowired
    private BannerService bannerService;

    @RequestMapping({"/","index"})
    @ApiOperation(value = "跳转前台首页")
    public String index(Model model) {
        List<Banner> list = bannerService.findAll();
        model.addAttribute("bannerList", list);
        return "index";
    }

//    @RequestMapping({"/banner"})
//    @ApiOperation(value = "跳转前台banner")
//    public String banner(Model model) {
//        List<Banner> list = bannerService.findAll();
//        model.addAttribute("bannerList", list);
//        return "banner";
//    }

    @RequestMapping("/userlogin")
    @ApiOperation(value = "跳转登陆页")
    public String loginPage() {
        return PREFIX + "login";
    }


    @RequestMapping("/background/index")
    @ApiOperation(value = "跳转后台首页")
    public String backgroundIndex() {
        return PREFIX + "index";
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
