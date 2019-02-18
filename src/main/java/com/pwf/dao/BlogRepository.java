package com.pwf.dao;

import com.pwf.domain.Blog;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.EAN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by PWF on 2018/11/10.
 * Blog 存储库.
 */
public interface BlogRepository extends JpaRepository<Blog,Long>,JpaSpecificationExecutor<Blog>{
//public interface BlogRepository extends ElasticsearchRepository<Blog, String>,JpaSpecificationExecutor<Blog>{
	@ApiOperation(value = "根据博文标题或博文内容分页查找数据")
	Page<Blog> findByTitleContainingOrSummaryContainingOrContentContaining(String title,String summary, String content, Pageable pageable);
	/**
	 * 修改文章的状态
	 * @param blogId
	 */
	@Query(value = "update blog set is_visible = ?2 where id = ?1 ",nativeQuery = true)
	@Modifying
	void updateBlogState(Long blogId,Boolean isVisible);

	/**
	 * 文章点赞
	 * @param blogId
	 */
	@Query(value = "update blog set likes = likes+1 where id = ?1 ",nativeQuery = true)
	@Modifying
	void upateLikes(Long blogId);

	/**
	 * 查询需要审核的文章
	 */
	Page<Blog> findBlogsByIsVisibleIsFalse(Pageable pageable);
}
