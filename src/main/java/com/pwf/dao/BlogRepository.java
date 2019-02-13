package com.pwf.dao;

import com.pwf.domain.Blog;
import com.pwf.domain.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by PWF on 2018/11/10.
 * Blog 存储库.
 */
public interface BlogRepository extends JpaRepository<Blog,String>,JpaSpecificationExecutor<Blog>{
//public interface BlogRepository extends ElasticsearchRepository<Blog, String>,JpaSpecificationExecutor<Blog>{
	@ApiOperation(value = "根据博文标题或博文内容分页查找数据")
	Page<Blog> findByTitleContainingOrSummaryContainingOrContentContaining(String title,String summary, String content, Pageable pageable);
}
