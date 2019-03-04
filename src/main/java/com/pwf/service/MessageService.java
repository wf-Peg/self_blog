package com.pwf.service;

import com.pwf.dao.BlogRepository;
import com.pwf.dao.CommentRepository;
import com.pwf.dao.MessageRepository;
import com.pwf.domain.Blog;
import com.pwf.domain.Comment;
import com.pwf.domain.Message;
import com.pwf.domain.PageBean;
import com.pwf.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.Optional;

/**
 * Created by PWF on 2019/2/2.
 */
@Service
@Transactional
public class MessageService {
    @Autowired
    private MessageRepository repository;

    public Page<Message> pageFindAll(int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return repository.findAll(pageable);
    }

    public Integer findAllCount(){
        return repository.findAll().size();
    }



    public Message findById(Integer id){
        Optional<Message> byId = repository.findById(id);
        return byId.isPresent()?byId.get():null;
    }


    public void save(Message message){
        repository.save(message);
    }


    public void deleteById(Integer id){
        repository.deleteById(id);
    }

    public Page<Message> findAll(PageBean pageBean) {
        return repository.findAll(PageRequest.of(pageBean.getPage(),pageBean.getSize()));
    }
    public List<Message> findAll() {
        return repository.findAll();
    }
}
