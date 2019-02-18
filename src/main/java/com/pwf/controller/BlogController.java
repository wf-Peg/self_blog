package com.pwf.controller;

import com.pwf.domain.Blog;
import com.pwf.domain.Comment;
import com.pwf.domain.PageBean;
import com.pwf.domain.PageResult;
import com.pwf.service.BlogService;
import com.pwf.vo.ResultVO;
import com.sun.org.apache.xpath.internal.operations.Mod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @ApiOperation("查询所有博文")
    @GetMapping
    @RequestMapping("/list")
    private String findAll(Model model){
        Iterable<Blog> all = service.findAll();
        model.addAttribute("blogList",all);
        return"/background/blog-tables";
//        return new ResultVO(true,"查询全部成功",all);

    }


    @ApiOperation("根据博文id查看博文详情页面")
    @RequestMapping(value = "/{id}")
    public String findById(Model model,@PathVariable long id){
//        Blog blog = (Blog)redisTemplate.opsForValue().get("blog_" + id);
//        if (blog==null){
            Blog blog=service.findById(id);
//            redisTemplate.opsForValue().set("blog_"+id,blog);
//        }
//        Blog blog = service.findById(id);
        List<Comment> comments=blog.getCommentList();
        model.addAttribute("blogDetail",blog);
        model.addAttribute("comments",comments);
        service.readingIncrease(id);
        return "blog-detail";
    }

    @ApiOperation("根据博文id查询并转发到博文编辑页面")
    @GetMapping("/blogDetail")
    public ModelAndView editForm(@RequestParam("id") long id, Model model) {
                Blog blog = service.findById(id);
                model.addAttribute("blog", blog);
        return new ModelAndView("background/blog-edit", "blogModel", model);
    }

    @PostMapping
    public ResultVO save(@RequestBody Blog blog){
        service.save(blog);
        return new ResultVO(true,"添加成功");
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResultVO deleteById(@PathVariable long id){
        service.deleteById(id);
        return new ResultVO(true,"删除成功");
    }

    @PutMapping(value = "/{id}")
    public ResultVO update(@PathVariable long id, @RequestBody Blog blog){
        blog.setId(id);
        service.update(blog);
        return new ResultVO(true,"修改成功");
    }

    @PostMapping("/search")
    public ResultVO findSearch(@RequestBody Blog blog){
        List<Blog> list = service.findSearch(blog);
        return new ResultVO(true,"search成功",list);
    }

   //根据blog名称、关键词、分类、摘要进行分页条件查询：
    @RequestMapping("/search/{page}/{size}")
    public ResultVO pageSearch(@RequestBody Blog blog, @PathVariable int page, @PathVariable int size){
        Page<Blog> pageData = service.pageSearch(blog,page,size);
        return new ResultVO(true,"分页search成功",new PageResult<Blog>(pageData.
                getTotalElements(),pageData.getContent()));
    }

    /**
     * 查询需要审核的文章
     */
    @RequestMapping(value="/examine")
    public String allExamine(PageBean pageBean, Model model){
//        service.examine(id,true);
        Pageable pageable = PageRequest.of(pageBean.getPage(), pageBean.getSize());
        Page<Blog> blogs = service.findBlogsByIsVisibleIsFalse(pageable);
        model.addAttribute("blogList",blogs.getContent());
        return "background/blog-tables-examine";
    }

    /**
     * 文章审核
     * @param id
     */
    @PutMapping(value="/examine/{id}")
    @ResponseBody
    public ResultVO examine(@PathVariable Long id){
        service.examine(id,true);
        return new ResultVO(true,"审核成功");
    }

    /**
     * 文章点赞
     * @param id
     */
    @ResponseBody
    @PutMapping(value="/likes/{id}")
    public ResultVO updateLikes(@PathVariable Long id ){
        service.updateLikes(id);
        return new ResultVO(true,"点赞成功");
    }

//    @GetMapping("/esSearch")
//    @ApiOperation(value = "根据title和content进行全文搜索")
//    public List<Blog> findBlogs(@RequestParam("title") String title,
//                                @RequestParam("summary") String summary,
//                                @RequestParam("content") String content,
//                                @RequestParam(value = "pageIndex",defaultValue = "0") int pageIndex,
//                                @RequestParam(value = "pageSize",defaultValue = "15") int pageSize
//                                ){
//        Pageable pageable= PageRequest.of(pageIndex,pageSize);
//        Page<Blog> page = service.findByTitleContainingOrSummaryContainingOrContentContaining(title,summary, content, pageable);
//        return page.getContent();
//    }

//    @GetMapping
//    @RequestMapping("/list")
//    public String listBlogs(@RequestParam(value="order",required=false,defaultValue="new") String order,
//                            @RequestParam(value="keywords",required=false) String keywords) {
//        System.out.print("order:" +order + ";keywords:" +keywords );
//        return "redirect:/index?order="+order+"&keywords="+keywords;
//    }

}
