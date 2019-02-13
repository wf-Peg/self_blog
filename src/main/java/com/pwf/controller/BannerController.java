package com.pwf.controller;

import com.pwf.domain.Banner;
import com.pwf.service.BannerService;
import com.pwf.service.FileUploadService;
import com.pwf.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
@RequestMapping("/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @Autowired
    FileUploadService fileUploadService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Banner> list = bannerService.findAll();
        model.addAttribute("bannerList", list);
        return "background/banner-tables";
    }

    @GetMapping("/add")
    public String add() {
        return "background/banner_add";
    }

    @PostMapping("/add")
//    public String add(Banner banner, MultipartFile uploadFile) throws IOException {
    public String add(Banner banner, MultipartFile uploadFile) throws IOException {
//        上传文件到本地服务器
//        String fileName = uploadFile.getOriginalFilename();
//        uploadFile.transferTo(new File("E:\\" + fileName));
        String filePath = fileUploadService.upload(uploadFile);
        banner.setImg(filePath);
        banner.setBannerkey("\""+banner.getBannerkey()+"\"");

        bannerService.save(banner);
        return "redirect:/banner/list";
    }


    @GetMapping("/update")
    public String update(Integer id, Model model) {
        model.addAttribute("banner", bannerService.findOne(id));
        return "background/banner_update";
    }

    @PostMapping("/update")
    @ResponseBody // 响应给客户端的是数据
    public Map<String, Object> update(Banner banner, MultipartFile uploadFile) throws IOException {
        //如果有新的文件，进行上传
        if(!"".equals(uploadFile.getOriginalFilename())){
            String filePath = fileUploadService.upload(uploadFile);
            banner.setImg(filePath);
        }
//        banner.setBannerkey("\""+banner.getBannerkey()+"\'");
        bannerService.update(banner);
        Map<String, Object> r = new HashMap<>();
        r.put("code", 0);//成功
        r.put("msg", "更新Banner成功");
        return r;
//        return "redirect:/banner/list";
    }

    @GetMapping("/delete")
    @ResponseBody // 响应给客户端的是数据
    public Map<String, Object> delete(Integer id) {
        bannerService.delete(id);
        // 删除成功以后，返回数据
        Map<String, Object> r = new HashMap<>();
        r.put("code", 0);//删除成功
        r.put("msg", "删除Banner成功");
        return r;
    }

    @GetMapping("/bannerSearch")
    public String list(/*@RequestParam(value="async",required=false) boolean async,
                             @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                             @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,*/
                             @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                             Model model) {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Banner> page = bannerService.listBannerByNameLike(searchText, pageable);
        List<Banner> list = page.getContent();    // 当前所在页面数据列表
//        model.addAttribute("page", page);
        model.addAttribute("bannerList", list);
        return "background/banner-tables";
    }
}
