package jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class jdbcNewsRepository implements NewsRepository {
	  @Autowired
	  private JdbcTemplate jdbcTemplate;
	  @Override
	  public Post findById(Long id) {
		try {
		      Post post = jdbcTemplate.queryForObject("SELECT * FROM post WHERE id=?",
		          BeanPropertyRowMapper.newInstance(Post.class), id);

		      return post;
		    } catch (IncorrectResultSizeDataAccessException e) {
		      return null;
		    }
	}
	  
	  @Override
	  public List<Post> findByTitleContaining(String title) {
	    String q = "SELECT * from post WHERE title LIKE '%" + title + "%'";

	    return jdbcTemplate.query(q, BeanPropertyRowMapper.newInstance(Post.class));
	  }
	  
	  @Override
	  public List<Post> findAll() {
	    return jdbcTemplate.query("SELECT * from post", BeanPropertyRowMapper.newInstance(Post.class));
	  }

	  @Override
	  public int save(Post post) {
	    return jdbcTemplate.update("INSERT INTO post (title,username,content,timeline,status) VALUES(?,?,?,?,?)",
	        new Object[] { post.gettitle(), post.getUsername(),post.getContent(),post.getTimeline(),post.getStatus() });
	  }
	  @Override
	  public int updatePost(Post post) {
	    return jdbcTemplate.update("UPDATE post SET title=?, content=?, username=?,timeline=?,status=? WHERE id=?",
	        new Object[] { post.gettitle(), post.getContent(), post.getUsername(),java.time.LocalDateTime.now(),post.getStatus(),post.getId()});
	  }
	  @Override
	  public int deleteById(Long id) {
	    return jdbcTemplate.update("DELETE FROM post WHERE id=?", id);
	  }
	  @Override
	  public int savecmt(Comment comment) {
	    return jdbcTemplate.update("INSERT INTO comment (idpost,idcmt,nameuser,content,timeline,evaluate) VALUES(?,?,?,?,?,?)",
	        new Object[] { comment.getIdpost(),comment.getIdcmt(),comment.getNameuser(),comment.getContent(),comment.getTimeline(),comment.getEvaluate() });
	  }
	  @Override
	  public List<Account> checklogin(String username,String password){
		  String w = "SELECT * FROM account WHERE username = '"+username+"' AND password = '"+password+"'";
		  return jdbcTemplate.query(w, BeanPropertyRowMapper.newInstance(Account.class));
	  }
	  @Override
	  public List<Comment> findByIdpost(long idpost) {
			  try{
//				  List<Comment> comment = (List<Comment>) jdbcTemplate.queryForObject("SELECT * FROM comment WHERE idpost=?",
//			          BeanPropertyRowMapper.newInstance(Comment.class), idpost);
				  String a="SELECT * FROM comment WHERE idpost='"+idpost+"'";
				  	return jdbcTemplate.query(a,BeanPropertyRowMapper.newInstance(Comment.class));
			  }catch (IncorrectResultSizeDataAccessException e){
				  return null;
			  }
		  
	  }
	  
	  
	  
	

}
