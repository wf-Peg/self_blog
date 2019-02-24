package com.pwf.service;

import com.pwf.dao.BlogRepository;
import com.pwf.dao.CommentRepository;
import com.pwf.domain.Blog;
import com.pwf.domain.Comment;
import com.pwf.domain.User;
import com.pwf.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by PWF on 2019/2/2.
 */
@Service
@Transactional
public class BlogService {
    @Autowired
    private BlogRepository repository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private IdWorker idWorker;

    public Integer findAllCount(){
        return repository.findAll().size();
    }

    public Page<Blog> pageFindAll(int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return repository.findAll(pageable);
    }

    public List<Blog> findByTitleContainingOrSummaryContainingOrContentContaining(String text,int page,int pageSize){
        text = "%" + text + "%";
        return repository.findByAttr(text,page,pageSize);
    }

    public Blog findById(Long id){
        Optional<Blog> byId = repository.findById(id);
        return byId.isPresent()?byId.get():null;
    }

    /**
     * 阅读量递增
     * @param id
     */
    public void readingIncrease(Long id) {
        Blog blog = repository.getOne(id);
        blog.setReading(blog.getReading()+1);
        repository.save(blog);
    }

    public void save(Blog blog){
        //对象中没id就是保存
//        blog.setId(idWorker.nextId());
//        blog.setId(idWorker.nextId()+"");
//        System.out.println(idWorker.nextId());
        repository.save(blog);
    }
    public void update(Blog blog){
        //对象中有id就是更新
        repository.saveAndFlush(blog);
//        repository.save(blog);
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }


    //JPA根据blog名称和关键词进行条件查询
    public List<Blog> findSearch(String searchText) {
        return repository.findAll(new Specification<Blog>() {
            /**
             *
             * @param root 根对象，获取属性的对象，root.get（泛型对象的属性名）
             * @param query 封装查询的关键字 group by order by 少用，直接写在sql语句中
             * @param cb 封装条件对象，如果返回null表示不需要任何条件
             * @return 分页数据数组对象
             */
            @Nullable
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list=new ArrayList<>();
                if (searchText!=null&&!"".equals(searchText)){
                    //等同于where title like "%xx%"
                    Predicate predicate = cb.like(root.get("title").as(String.class), searchText);
                    Predicate predicate1 = cb.like(root.get("author").as(String.class), searchText);
                    Predicate predicate2 = cb.like(root.get("content").as(String.class),  searchText);
                    Predicate predicate3 = cb.like(root.get("summary").as(String.class), searchText);
                    list.add(predicate3);
                    list.add(predicate2);
                    list.add(predicate1);
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                //list直接转为数组
                list.toArray(predicates);
                return cb.and(predicates);
            }
        });
    }

    //根据blog名称、关键词、分类、摘要进行分页条件查询：
    public Page<Blog> pageSearch(Blog blog, int page, int size) {
        //封装分页对象
        Pageable pageable= PageRequest.of(page-1,size);//框架中第一页是从零开始算的
        return repository.findAll(new Specification<Blog>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list=new ArrayList<>();
                if (blog.getTitle()!=null&&!"".equals(blog.getTitle())){
                    //等同于where labelname like "%xx%"
                    Predicate predicate = cb.like(root.get("title").as(String.class), "%" + blog.getTitle() + "%");
                    list.add(predicate);
                }
                if (blog.getKeywords()!=null&&!"".equals(blog.getKeywords())){
                    //等同于where state = "1"
                    Predicate predicate = cb.equal(root.get("keywords").as(String.class),  blog.getTitle());
                    list.add(predicate);
                }
                if (blog.getCategory()!=null&&!"".equals(blog.getCategory())){
                    //等同于where state = "1"
                    Predicate predicate = cb.equal(root.get("category").as(String.class),  blog.getCategory());
                    list.add(predicate);
                }
                if (blog.getSummary()!=null&&"".equals(blog.getSummary())){
                    Predicate predicate = cb.equal(root.get("summary").as(String.class), blog.getSummary());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                //list直接转为数组
                list.toArray(predicates);
                return cb.and(predicates);
            }
        }, pageable);
    }

    public Blog createComment(Long blogId, String commentUsername,String commentContent) {
        Blog originalBlog = repository.findById(blogId).get();
        Comment comment = new Comment(commentUsername, commentContent,blogId);
//        commentRepository.save(comment);
        originalBlog.addComment(comment);
//        originalBlog.setComments(originalBlog.getComments()+1);
//        repository.addCommentCount(blogId);
        return repository.save(originalBlog);
    }

    public void removeComment(Long blogId, Long commentId) {
        Blog originalBlog = repository.findById(blogId).get();
        originalBlog.removeComment(commentId);
        repository.save(originalBlog);
    }

    public void examine(Long id,Boolean isVisible) {
        repository.updateBlogState(id,isVisible);
    }

    public Page<Blog> findBlogsByIsVisibleIsFalse(Pageable pageable){
        return repository.findBlogsByIsVisibleIsFalse(pageable);
    }

    public Integer findBlogsByIsVisibleIsFalseCount(){
        return repository.findBlogsByIsVisibleIsFalse().size();
    }

    public void updateLikes(Long id) {
        repository.upateLikes(id);
    }

    public Page<Blog> findBlogsByIsVisibleIsTrue(Pageable pageable) {
        return repository.findBlogsByIsVisibleIsTrue(pageable);
    }

    public Page<Blog> findByCategory(Pageable pageable,String category) {
        return repository.findBlogsByCategory(pageable,category);
    }

    public Page<Blog> hotlist(int page,int size) {
        //1.创建分页对象
        Sort sort = new Sort(Sort.Direction.DESC,"likes","reading","comments");
        PageRequest pageRequest = PageRequest.of(page-1,size,sort);
        //2.调用持久层查询,并返回
        return repository.findAll(pageRequest);
    }

    public List<Blog> getTop30Keywords() {
        //1.创建分页对象
        Sort sort = new Sort(Sort.Direction.DESC,"reading","likes","comments");
        PageRequest pageRequest = PageRequest.of(0,30,sort);
        //2.调用持久层查询,并返回
        Page<Blog> top30 = repository.findAll(pageRequest);
        return top30.getContent();
    }

    public Page<Blog> newlist(int page,int size) {
        //1.创建分页对象
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        PageRequest pageRequest = PageRequest.of(page-1,size,sort);
        //2.调用持久层查询,并返回
        return repository.findAll(pageRequest);
    }

    public Page<Blog> findSearch(String keyword,int page,int size) {
        // 模糊查询
        keyword = "%" + keyword + "%";
        return repository.findBlogsByKeywordsLike(keyword,PageRequest.of(page-1,size));
    }
}
