package com.pwf.service;

import com.pwf.domain.Banner;
import com.pwf.domain.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by PWF on 2019/1/17.
 */
public interface BannerService {
    Page<Banner> findAll(Pageable pageable);
    List<Banner> findAll();
    Banner findOne(Integer id);
    void save(Banner banner);
    void update(Banner banner);
    void delete(Integer id);
    @ApiOperation(value = "根据图片名进行分页模糊查询")
    Page<Banner> listBannerByNameLike(String searchText, Pageable pageable);

    Integer findAllCount();
}
