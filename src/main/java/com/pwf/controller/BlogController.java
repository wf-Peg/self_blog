package com.pwf.controller;

import com.pwf.domain.*;
import com.pwf.service.BlogService;
import com.pwf.service.FileUploadService;
import com.pwf.service.UserService;
import com.pwf.util.ConstraintViolationExceptionHandler;
import com.pwf.vo.BlogVo;
import com.pwf.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by PWF on 2018/11/10.
 */
@Controller
@RequestMapping("/blog")
@Api(tags = "博文逻辑控制层")
public class BlogController {
    @Autowired
    private BlogService service;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    FileUploadService fileUploadService;

    @ApiOperation("跳转发布博文页面")
    @RequestMapping("/blogForm")
    private String blogForm(Model model) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", principal.getUsername());
        return "background/blog-form";
    }

    @ApiOperation("查询所有博文")
    @RequestMapping("/list")
    private String findAll(PageBean pageBean, Model model) {
        Page<Blog> all = service.pageFindAll(pageBean);
        model.addAttribute("blogList", all.getContent());
        model.addAttribute("totalPage", all.getTotalPages());
        model.addAttribute("currentPage", pageBean.getPage());
        return "/background/blog-tables";
    }

    @ApiOperation("根据分类查询所有博文")
    @RequestMapping("/orderList")
    private String orderList(Model model, PageBean pageBean,
                             @RequestParam(value = "category", required = false, defaultValue = "") String category) {
        if (category.equals("生活") || category.equals("技术") || category.equals("游戏")) {
            Page<Blog> blogs = service.findByCategory(pageBean, category);
            model.addAttribute("blogList", blogs.getContent());
            model.addAttribute("totalPage", blogs.getTotalPages());
            model.addAttribute("currentPage", pageBean.getPage());
            return "index :: #content";
        }
        if (category.equals("最热")) {
            Page<Blog> blogs = service.hotlist(pageBean);
            model.addAttribute("blogList", blogs.getContent());
            model.addAttribute("totalPage", blogs.getTotalPages());
            model.addAttribute("currentPage", pageBean.getPage());
            return "index :: #content";
        }
        if (category.equals("最新")) {
//            Pageable pageable = PageRequest.of(0, 10, sort);
            Page<Blog> blogs = service.newlist(pageBean);
//            PageResult<Blog> pageResult = new PageResult<>(blogs.getTotalElements(),blogs.getContent());
            model.addAttribute("blogList", blogs.getContent());
            model.addAttribute("totalPage", blogs.getTotalPages());
            model.addAttribute("currentPage", pageBean.getPage());
            return "index :: #content";
        }

        Page<Blog> all = service.pageFindAll(pageBean);
        model.addAttribute("blogList", all.getContent());
//        model.addAttribute("totalPage", all.getTotalPages());
//        model.addAttribute("currentPage", pageBean.getPage());
        return "index :: #content";
    }

    @ApiOperation("前台根据博文id查看博文详情页面")
    @RequestMapping(value = "/{id}")
    public String findById(Model model, @PathVariable long id) {
//        Blog blog = (Blog)redisTemplate.opsForValue().get("blog_" + id);
//        if (blog==null){
        Blog blog = service.findById(id);
//            redisTemplate.opsForValue().set("VisitsCount");
//        }
//        Blog blog = service.findById(id);
        List<Comment> comments = blog.getCommentList();
        model.addAttribute("blogDetail", blog);
        model.addAttribute("comments", comments);
        service.readingIncrease(id);
        return "blog-detail";
    }

    @ApiOperation("后台根据博文id查询并转发到博文编辑页面")
    @GetMapping("/blogDetail")
    public ModelAndView editForm(@RequestParam("id") long id, Model model) throws ParseException {
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Blog blog = service.findById(id);
//        String strReleaseTime = blog.getReleaseTime().toString().substring(0,10);
//        blog.setReleaseTime(sdf.parse(strReleaseTime));
//        System.out.println(strReleaseTime);
        //todo redis
        model.addAttribute("blog", blog);
        return new ModelAndView("background/blog-edit", "blogModel", model);
    }

    @ApiOperation("保存博文")
    @PostMapping
    @ResponseBody
    public ResultVO save(BlogVo blogVo, MultipartFile uploadFile) {
        String filePath = null;
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Blog blog = Blog.getInstance();

        String strDate = blogVo.getReleaseTime();//给定一个字符串日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseTime;
        if (blogVo.getId() == null) {
            try {
                releaseTime = sdf.parse(strDate);//字符串转成date对象类型
                //普通更新用户的业务,把blogVo的值拷贝到blog上
                BeanUtils.copyProperties(blogVo, blog);
                if ("".equals(blogVo.getKeylink())) {
                    blog.setKeylink(null);
                }
                //上传博客图片
                filePath = fileUploadService.upload(uploadFile);
                blog.setImage(filePath);
                blog.setReleaseTime(releaseTime);
                blog.setUpdateTime(new Date());
                blog.setIsVisible(false);
                service.save(blog);
                //根据当前用户名，为该用户添加博文数量
                userService.addBlogCount(principal.getUsername());
                return new ResultVO(true, "发布成功,请耐心等待管理员的审核！");
            } catch (ConstraintViolationException e) {
                return new ResultVO(false, ConstraintViolationExceptionHandler.getMessage(e));
            } catch (Exception e) {
                return new ResultVO(false, e.getMessage());
            }
        } else {
            try {
                BeanUtils.copyProperties(blogVo, blog);
                releaseTime = sdf.parse(strDate);
                if (!"".equals(uploadFile.getOriginalFilename())) {
                    filePath = fileUploadService.upload(uploadFile);
                    blog.setImage(filePath);
                }
                if ("".equals(blogVo.getKeylink())) {
                    blog.setKeylink(null);
                }
                blog.setReleaseTime(releaseTime);
                blog.setUpdateTime(new Date());
                service.update(blog);
            } catch (ConstraintViolationException e) {
                return new ResultVO(false, ConstraintViolationExceptionHandler.getMessage(e));
            } catch (Exception e) {
                return new ResultVO(false, e.getMessage());
            }
        }

        return new ResultVO(true, "提交更新成功");
    }

    @ApiOperation("删除博文")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResultVO deleteById(@PathVariable long id) {
        Blog blogDetail = service.findById(id);
        String author = blogDetail.getAuthor();
        service.deleteById(id);
        userService.decreaseUserBlogCount(author);
        return new ResultVO(true, "删除成功");
    }

    @ApiOperation("修改博文")
    @PutMapping(value = "/{id}")
    public ResultVO update(@PathVariable long id, @RequestBody Blog blog) {
        blog.setId(id);
        service.update(blog);
        return new ResultVO(true, "修改成功");
    }

//    @ApiOperation("根据blog名称、关键词、分类、摘要进行查询")
//    @GetMapping("/search")
//    public String findSearch(@RequestParam(value = "searchText", required = false) String searchText,Model model) {
//        List<Blog> list = service.findSearch(searchText);
//        model.addAttribute("blogList", list);
//        return "background/blog-tables";
////        return new ResultVO(true, "search成功", list);
//    }

    @ApiOperation("后台根据blog标题进行查询")
    @GetMapping("/search")
    public String findSearch(@RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                             PageBean pageBean,Model model) {
//        List<Blog> list = service.findSearch(searchText);
//        model.addAttribute("blogList", list);
//        model.addAttribute("totalPage", list.getTotalPages());
//        model.addAttribute("currentPage", pageBean.getPage());
        List<Blog> list = service.findByTitleContainingOrSummaryContainingOrContentContaining(searchText, pageBean);
        model.addAttribute("blogList", list);
        model.addAttribute("totalPage", 1);
        model.addAttribute("currentPage", pageBean.getPage());
        return "background/blog-tables";
    }

    @ApiOperation("根据blog关键词进行查询")
    @GetMapping("/keySearch")
    public String keySearch(@RequestParam(value = "keyword", required = true) String keyword, Model model, PageBean pageBean) {
        Page<Blog> list = service.findSearch(keyword, pageBean);
        model.addAttribute("blogList", list.getContent());
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageBean.getPage());
        return "index :: #content";
    }

    @ApiOperation("前台根据blog名称、关键词、分类、摘要、作者进行查询")
    @GetMapping("/esSearch")
    public String esSearch(@RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                           PageBean pageBean, Model model) {
        List<Blog> list = service.findByTitleContainingOrSummaryContainingOrContentContaining(searchText, pageBean);
        model.addAttribute("blogList", list);
        model.addAttribute("totalPage", 1);
        model.addAttribute("currentPage", pageBean.getPage());
        return "index :: #content";
    }

//    @ApiOperation("根据blog名称、关键词、分类、摘要进行分页条件查询")
//    @RequestMapping("/search/{page}/{size}")
//    public ResultVO pageSearch(@RequestBody Blog blog, @PathVariable int page, @PathVariable int size) {
//        Page<Blog> pageData = service.pageSearch(blog, page, size);
//        return new ResultVO(true, "分页search成功", new PageResult<Blog>(pageData.
//                getTotalElements(), pageData.getContent()));
//    }

    @ApiOperation("查询需要审核的文章")
    @RequestMapping(value = "/examine")
    public String allExamine(PageBean pageBean, Model model) {
        Pageable pageable = PageRequest.of(pageBean.getPage(), pageBean.getSize());
        Page<Blog> blogs = service.findBlogsByIsVisibleIsFalse(pageable);
        model.addAttribute("blogList", blogs.getContent());
        model.addAttribute("totalPage", blogs.getTotalPages());
        model.addAttribute("currentPage", pageBean.getPage());
        return "background/blog-tables-examine";
    }

    @ApiOperation("文章审核")
    @PutMapping(value = "/examine/{id}")
    @ResponseBody
    public ResultVO examine(@PathVariable Long id) {
        service.examine(id, true);
        return new ResultVO(true, "审核成功");
    }

    @ApiOperation("文章点赞")
    @ResponseBody
    @PutMapping(value = "/likes/{id}")
    public ResultVO updateLikes(@PathVariable Long id) {
        service.updateLikes(id);
        return new ResultVO(true, "点赞成功");
    }

}
