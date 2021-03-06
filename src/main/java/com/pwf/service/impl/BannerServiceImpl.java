package com.pwf.service.impl;

import com.pwf.dao.BannerRepository;
import com.pwf.domain.Banner;
import com.pwf.domain.PageBean;
import com.pwf.service.BannerService;
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
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerRepository repository;

    @Override
    public Page<Banner> findAll(PageBean pageBean) {
        return repository.findAll(PageRequest.of(pageBean.getPage(),pageBean.getSize()));
    }

    @Override
    public List<Banner> findAll() {
        return repository.findAll();
    }

    @Override
    public Banner findOne(Integer id) {
        return repository.getOne(id);
    }

    @Override
    public void save(Banner banner) {
        repository.save(banner);
    }

    @Override
    public void update(Banner newBanner) {
        Banner updateBanner = repository.getOne(newBanner.getId());
        //前赋值后
        BeanUtils.copyProperties(newBanner, updateBanner);
        repository.saveAndFlush(updateBanner);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Banner> listBannerByNameLike(String searchText, PageBean pageBean) {
        // 模糊查询
        searchText = "%" + searchText + "%";
        Page<Banner> banners = repository.findByNameLike(searchText, PageRequest.of(pageBean.getPage(),pageBean.getSize()));
        return banners;
    }

    @Override
    public Integer findAllCount() {
        return repository.findAll().size();
    }

}
