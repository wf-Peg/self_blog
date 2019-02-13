package com.pwf.controller;

import com.pwf.domain.Blog;
import com.pwf.domain.PageResult;
import com.pwf.service.BlogService;
import com.pwf.vo.ResultVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by PWF on 2018/11/10.
 */
@RestController
@RequestMapping("/blog")
@Api(tags = "博文逻辑控制层")
public class BlogController {
    @Autowired
    private BlogService service;


    @GetMapping
    private ResultVO findAll(){
        Iterable<Blog> all = service.findAll();
//        Map<String, Object> r = new HashMap<>();
//        r.put("code", 0);//删除成功
//        r.put("msg", "删除用户成功");
//        r.put("obj", all);
//        return r;
        return new ResultVO(true,"查询全部成功",all);
    }


    @RequestMapping(value = "/{id}")
    public ModelAndView findById(Model model,@PathVariable String id){
        Blog blog = service.findById(id);
//        return new ResultVO(true,"查询单个成功",blog);
        model.addAttribute("blogDetail",blog);
        return new ModelAndView("blog-detail","model",model);
    }

    @PostMapping
    public ResultVO save(@RequestBody Blog blog){
        service.save(blog);
        return new ResultVO(true,"添加成功");
    }

    @DeleteMapping(value = "/{id}")
    public ResultVO deleteById(@PathVariable String id){
        service.deleteById(id);
        return new ResultVO(true,"删除成功");
    }

    @PutMapping(value = "/{id}")
    public ResultVO update(@PathVariable String id, @RequestBody Blog blog){
        blog.setId(id);
        service.update(blog);
        return new ResultVO(true,"修改成功");
    }

    @PostMapping("/search")
    public ResultVO findSearch(@RequestBody Blog blog){
        List<Blog> list = service.findSearch(blog);
        return new ResultVO(true,"search成功",list);
    }

    @RequestMapping("/search/{page}/{size}")
    public ResultVO pageSearch(@RequestBody Blog blog, @PathVariable int page, @PathVariable int size){
        Page<Blog> pageData = service.pageSearch(blog,page,size);
        return new ResultVO(true,"分页search成功",new PageResult<Blog>(pageData.
                getTotalElements(),pageData.getContent()));
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
