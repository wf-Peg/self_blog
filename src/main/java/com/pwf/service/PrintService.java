package com.pwf.service;

import com.pwf.dao.BannerRepository;
import com.pwf.dao.PrintRepository;
import com.pwf.domain.Banner;
import com.pwf.domain.PageBean;
import com.pwf.domain.Print;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PWF on 2019/1/17.
 */
@Service
public class PrintService {
    @Autowired
    private PrintRepository repository;

    public Page<Print> findAll(PageBean pageBean) {
        return repository.findAll(PageRequest.of(pageBean.getPage(),pageBean.getSize()));
    }

    public List<Print> findAll() {
        return repository.findAll();
    }

    public Print findOne(Integer id) {
        return repository.getOne(id);
    }

    public void save(Print banner) {
        repository.save(banner);
    }

    public void update(Print newPrint) {
        Print updatePrint = repository.getOne(newPrint.getId());
        //前赋值后
        BeanUtils.copyProperties(newPrint, updatePrint);
        repository.saveAndFlush(updatePrint);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Page<Print> listPrintByFilenameLike(String searchText, PageBean pageBean) {
        // 模糊查询
        searchText = "%" + searchText + "%";
        Page<Print> prints = repository.findByFilenameLike(searchText, PageRequest.of(pageBean.getPage(),pageBean.getSize()));
        return prints;
    }

    public Integer findAllCount() {
        return repository.findAll().size();
    }

}
