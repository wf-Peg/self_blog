package com.pwf.controller;

import com.itextpdf.text.pdf.PdfReader;
import com.pwf.domain.Banner;
import com.pwf.domain.PageBean;
import com.pwf.domain.Print;
import com.pwf.service.BannerService;
import com.pwf.service.FileUploadService;
import com.pwf.service.PrintService;
import com.pwf.util.ConstraintViolationExceptionHandler;
import com.pwf.util.Word2PdfUtil;
import com.pwf.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import static jdk.nashorn.internal.objects.NativeArray.lastIndexOf;

/**
 * Created by PWF on 2019/1/17.
 */
@Controller
@CrossOrigin
@RequestMapping("/print")
public class PrintController {
    @Autowired
    private PrintService printService;

    @Autowired
    FileUploadService fileUploadService;

    @GetMapping("/list")
    public String list(Model model,PageBean pageBean) {
        Page<Print> list = printService.findAll(pageBean);
        model.addAttribute("printList", list.getContent());
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageBean.getPage());
        return "background/print_tables";
    }

    @GetMapping("/add")
    public String add() {
        return "background/print_add";
    }

    @GetMapping("/pay")
    public String pay(int page,Model model) {
        model.addAttribute("page",page);
        DecimalFormat decimalFormat=new DecimalFormat("##0.00");
        String count=decimalFormat.format(page*0.2f);
        model.addAttribute("count",count+"元");
        return "background/print_pay";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResultVO add(Print print, MultipartFile uploadFile) {
//        上传文件到本地服务器
//        String fileName = uploadFile.getOriginalFilename();
//        uploadFile.transferTo(new File("E:\\" + fileName));
        int pagecount = 0;
        PdfReader reader;

        String filePath = null;
        try {
            filePath = fileUploadService.upload(uploadFile);
            print.setFilepath(filePath);
            printService.save(print);
        } catch (ConstraintViolationException e) {
            return new ResultVO(false, ConstraintViolationExceptionHandler.getMessage(e));
        } catch (IOException e) {
            return new ResultVO(false, e.getMessage());
        }
        //解析文件
        //获取文件后缀名
        String originalFilename = uploadFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String s = UUID.randomUUID().toString();
        if ("doc"==suffix||suffix.equals("doc")||"docx"==suffix||suffix.equals("docx")){
            boolean word2pdf = Word2PdfUtil.word2pdf(filePath, "D:\\"+s+".pdf");
            if (word2pdf){
                try {
                    reader = new PdfReader("D:\\"+s+".pdf");
                    pagecount= reader.getNumberOfPages();
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResultVO(false, "word转换失败");
                }
            }
        }else {
            try {
                reader = new PdfReader(filePath);
                pagecount= reader.getNumberOfPages();
            } catch (IOException e) {
                e.printStackTrace();
                return new ResultVO(false, "pdf转换失败");
            }
//            System.out.println(pagecount);
        }
        System.out.println(new ResultVO(true, "提交print成功",pagecount));
        return new ResultVO(true, "提交print成功",pagecount);
    }

//    @GetMapping("/update")
//    public String update(Integer id, Model model) {
//        Banner banner = printService.findOne(id);
//        banner.setBannerkey(banner.getBannerkey().replace("\"", ""));
//        model.addAttribute("banner", banner);
//        return "background/banner_update";
//    }

//    @PostMapping("/update")
//    @ResponseBody // 响应给客户端的是数据
//    public Map<String, Object> update(Banner banner, MultipartFile uploadFile) throws IOException {
//        //如果有新的文件，进行上传
//        if (!"".equals(uploadFile.getOriginalFilename())) {
//            String filePath = fileUploadService.upload(uploadFile);
//            banner.setImg(filePath);
//        }
////        banner.setBannerkey("\""+banner.getBannerkey()+"\'");
//        banner.setBannerkey("\"" + banner.getBannerkey() + "\"");
//        bannerService.update(banner);
//        Map<String, Object> r = new HashMap<>();
//        r.put("code", 0);//成功
//        r.put("msg", "更新Banner成功");
//        return r;
////        return "redirect:/banner/list";
//    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResultVO delete(Integer id) {
        printService.delete(id);
        return new ResultVO(true,"删除print成功");
    }

    @GetMapping("/printSearch")
    public String list(PageBean pageBean,
                       @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                       Model model) {
        Page<Print> list = printService.listPrintByFilenameLike(searchText, pageBean);
        model.addAttribute("printList", list.getContent());
//        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageBean.getPage());
        return "background/print_tables";
    }
}
