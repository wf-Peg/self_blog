package com.pwf.controller;

import com.pwf.domain.Banner;
import com.pwf.domain.PageBean;
import com.pwf.service.BannerService;
import com.pwf.service.FileUploadService;
import com.pwf.util.ConstraintViolationExceptionHandler;
import com.pwf.vo.ResultVO;
import com.sun.org.apache.xpath.internal.operations.Mod;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PWF on 2019/1/17.
 */
@Controller
@CrossOrigin
@Api(tags = "墙角图片控制层")
@RequestMapping("/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @Autowired
    FileUploadService fileUploadService;

    @GetMapping("/list")
    public String list(Model model, PageBean pageBean) {
        Page<Banner> list = bannerService.findAll(pageBean);
        model.addAttribute("bannerList", list.getContent());
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageBean.getPage());
        System.out.println(pageBean.getPage());
        return "background/banner-tables";
    }

    //    @GetMapping("/bannerList")
//    @ResponseBody
//    public ResultVO bannerList(PageBean pageBean) {
//        Page<Banner> list = bannerService.findAll(pageBean);
//
////        model.addAttribute("bannerList", list.getContent());
////        model.addAttribute("totalPage", list.getTotalPages());
////        model.addAttribute("currentPage", pageBean.getPage());
//        return new ResultVO(true,"读取图片成功",list.getContent());
//        return "index :: #portfolio_grid";
//    }
    @GetMapping("/bannerList")
    public String bannerList(PageBean pageBean, Model model) {
        Page<Banner> list = bannerService.findAll(pageBean);

        model.addAttribute("bannerList", list.getContent());
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageBean.getPage());
        return "index :: #portfolio_grid";
    }

    @GetMapping("/add")
    public String add() {
        return "background/banner_add";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResultVO add(Banner banner, MultipartFile uploadFile) {
//        上传文件到本地服务器
//        String fileName = uploadFile.getOriginalFilenaKme();
//        uploadFile.transferTo(new File("E:\\" + fileName));
        String filePath = null;
        try {
            filePath = fileUploadService.upload(uploadFile);
            banner.setImg(filePath);
            banner.setBannerkey("\"" + banner.getBannerkey() + "\"");
            bannerService.save(banner);
        } catch (ConstraintViolationException e) {
            return new ResultVO(false, ConstraintViolationExceptionHandler.getMessage(e));
        } catch (IOException e) {
            return new ResultVO(false, e.getMessage());
        }
        return new ResultVO(true, "提交banner成功");
    }

    @GetMapping("/update")
    public String update(Integer id, Model model) {
        Banner banner = bannerService.findOne(id);
        banner.setBannerkey(banner.getBannerkey().replace("\"", ""));
        model.addAttribute("banner", banner);
        return "background/banner_update";
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> update(Banner banner, MultipartFile uploadFile) throws IOException {
        //如果有新的文件，进行上传
        if (!"".equals(uploadFile.getOriginalFilename())) {
            String filePath = fileUploadService.upload(uploadFile);
            banner.setImg(filePath);
        }
        banner.setBannerkey("\"" + banner.getBannerkey() + "\"");
        bannerService.update(banner);
        Map<String, Object> r = new HashMap<>();
        r.put("code", 0);//成功
        r.put("msg", "更新Banner成功");
        return r;
//        return "redirect:/banner/list";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResultVO delete(Integer id) {
        bannerService.delete(id);
        return new ResultVO(true, "删除Banner成功");
    }

    @GetMapping("/bannerSearch")
    public String list(PageBean pageBean,
                       @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                       Model model) {
        Page<Banner> page = bannerService.listBannerByNameLike(searchText, pageBean);
//        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("currentPage", pageBean.getPage());
        model.addAttribute("bannerList", page.getContent());
        return "background/banner-tables";
    }
}
