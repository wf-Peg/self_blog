package com.pwf.dao;

import com.pwf.domain.Blog;
import com.pwf.domain.PageBean;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.EAN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by PWF on 2018/11/10.
 * Blog 存储库.
 */
public interface BlogRepository extends JpaRepository<Blog,Long>,JpaSpecificationExecutor<Blog>{
//public interface BlogRepository extends ElasticsearchRepository<Blog, String>,JpaSpecificationExecutor<Blog>{
	@ApiOperation(value = "根据博文标题、作者、摘要分页查找数据")

	@Query(value = "SELECT * FROM blog WHERE content LIKE ?1 OR title LIKE ?1 OR author LIKE ?1 OR summary LIKE ?1 OR keywords LIKE ?1 limit ?2,?3",nativeQuery = true)
	List<Blog> findByAttr(String text,int page, int pageSize);

	@Query(value = "SELECT * FROM blog WHERE CONCAT(title,summary,content) LIKE :text",nativeQuery = true)
	Page<Blog> findByAttr2(@Param("text") String text, Pageable pageable);

	Page<Blog> findBlogsByKeywordsLikeAndIsVisibleIsTrue(String keyword, Pageable pageable);


	@ApiOperation(value = "修改文章的状态")
	@Query(value = "update blog set is_visible = ?2 where id = ?1 ",nativeQuery = true)
	@Modifying
	void updateBlogState(Long blogId,Boolean isVisible);

	@ApiOperation(value = "文章点赞")
	@Query(value = "update blog set likes = likes+1 where id = ?1 ",nativeQuery = true)
	@Modifying
	void upateLikes(Long blogId);

	@ApiOperation(value = "分页查询需要审核的文章")
	Page<Blog> findBlogsByIsVisibleIsFalse(Pageable pageable);

	@ApiOperation(value = "查询需要审核的文章")
	List<Blog> findBlogsByIsVisibleIsFalse();

	@ApiOperation(value = "查询通过审核的文章")
	Page<Blog> findBlogsByIsVisibleIsTrue(Pageable pageable);

	@ApiOperation(value = "根据分类查询文章")
	Page<Blog> findBlogsByCategoryAndIsVisibleIsTrue(Pageable pageable,String category);

	@ApiOperation(value = "增加评论量")
	@Query(value = "update blog set comments = comments+1 where id = ?1 ",nativeQuery = true)
	@Modifying
	void addCommentCount(Long blogId);

//	@ApiOperation(value = "增加评论量")
//	@Query(value = "update blog set comments = comments+1 where id = ?1 ",nativeQuery = true)
//	@Modifying
//	void findBlogsByUpdateTime(Long blogId);

//	Page<Blog> findByIshot(PageRequest pageRequest);

//	Page<Blog> findByReadingContainingOrLikesContainingOrCommentsContaining(String text,);

}
