package jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcNewsRepository implements NewsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Post findById(Long id) {
        try {
            Post post = jdbcTemplate.queryForObject("SELECT * FROM post WHERE id_post=? and status not in ('Deleted');",
                    BeanPropertyRowMapper.newInstance(Post.class), id);
            return post;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Post> findByTitleContaining(String title) {
        String q = "SELECT * from post WHERE title LIKE '%" + title + "%' and status not in ('Deleted')";
        return jdbcTemplate.query(q, BeanPropertyRowMapper.newInstance(Post.class));
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query("SELECT * from post where status not in ('Deleted')",
                BeanPropertyRowMapper.newInstance(Post.class));
    }

    @Override
    public int save(Post post) {
        return jdbcTemplate.update(
                "INSERT INTO post (title,content,timeline,status,id_account,image_url) VALUES(?,?,?,?,?,?)",
                new Object[] { post.getTitle(), post.getContent(), post.getTimeline(), post.getStatus(),
                        post.getIdAccount(), post.getImageUrl() });
    }

    @Override
    public int updatePost(Post post) {
        return jdbcTemplate.update(
                "UPDATE post SET title=?, content=?, timeline=?,status=?, image_url=? WHERE id_post=?",
                new Object[] { post.getTitle(), post.getContent(), java.time.LocalDateTime.now(), post.getStatus(),
                        post.getImageUrl(), post.getIdPost() });
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM post WHERE id=?", id);
    }

    @Override
    public int savecmt(Comment comment) {
        return jdbcTemplate.update(
                "INSERT INTO comment (id_post,nameuser,content,timeline,evaluate,id_cmt_parent) VALUES(?,?,?,?,?,?)",
                new Object[] { comment.getIdPost(), comment.getNameuser(), comment.getContent(), comment.getTimeline(),
                        comment.getEvaluate(), comment.getIdCmtParent() });
    }

    @Override
    public List<Account> checklogin(String username, String password) {
        String w = "SELECT * FROM account WHERE username = '" + username + "' AND password = '" + password + "'";
        return jdbcTemplate.query(w, BeanPropertyRowMapper.newInstance(Account.class));
    }

    @Override
    public List<Comment> findByIdpost(long idpost) {
        try {
            String a = "SELECT * FROM comment WHERE id_post='" + idpost + "'";
            return jdbcTemplate.query(a, BeanPropertyRowMapper.newInstance(Comment.class));
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public int updatePostStatus(String status, Long id) {
        return jdbcTemplate.update("UPDATE post SET status = ? WHERE id_post = ?", new Object[] { status, id });
    }

}
