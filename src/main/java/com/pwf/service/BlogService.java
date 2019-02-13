package com.pwf.service;

import com.pwf.dao.BlogRepository;
import com.pwf.domain.Blog;
import com.pwf.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PWF on 2019/2/2.
 */
@Service
@Transactional
public class BlogService {
    @Autowired
    private BlogRepository repository;
    @Autowired
    private IdWorker idWorker;

    public Iterable<Blog> findAll(){
        return repository.findAll();
    }
    public Page<Blog> pageFindAll(int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return repository.findAll(pageable);
    }

//    public Page<Blog> findByTitleLikeOrContentLike(String title, String content, Pageable pageable){
//        return repository.findByTitleLikeOrContentLike(title,content,pageable);
//    }
//    public Page<Blog> findByTitleContainingOrSummaryContainingOrContentContaining(String title,String summary, String content, Pageable pageable){
//        return repository.findByTitleContainingOrSummaryContainingOrContentContaining(title,summary,content,pageable);
//    }

    public Blog findById(String id){
        return repository.findById(id).get();
    }
    public void save(Blog blog){
        //对象中没id就是保存
        blog.setId(idWorker.nextId()+"");
        System.out.println(idWorker.nextId());
        repository.save(blog);
    }
    public void update(Blog blog){
        //对象中有id就是更新
//        repository.saveAndFlush(blog);
        repository.save(blog);
    }
    public void deleteById(String id){
        repository.deleteById(id);
    }

    //JPA根据blog名称和关键词进行条件查询
    public List<Blog> findSearch(Blog blog) {
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
                if (blog.getTitle()!=null&&!"".equals(blog.getTitle())){
                    //等同于where title like "%xx%"
                    Predicate predicate = cb.like(root.get("title").as(String.class), "%" + blog.getTitle() + "%");
                    list.add(predicate);
                }
                if (blog.getKeywords()!=null&&!"".equals(blog.getKeywords())){
                    //等同于where state = "1"
                    Predicate predicate = cb.equal(root.get("keywords").as(String.class),  blog.getKeywords());
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
        });
    }

    //根据blog名称、关键词进行分页条件查询：
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


}
