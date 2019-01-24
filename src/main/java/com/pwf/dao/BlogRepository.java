package com.pwf.dao;

import com.pwf.domain.Blog;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by PWF on 2018/11/10.
 * Blog 存储库.
 */
public interface BlogRepository extends ElasticsearchRepository<Blog, String> {
	@ApiOperation(value = "根据博文标题或博文内容分页查找数据")
	Page<Blog> findByTitleLikeOrContentLike(String title, String content, Pageable pageable);
}
