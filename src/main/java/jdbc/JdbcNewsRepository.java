package jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
    public Post save(Post post) {
        String sql = "INSERT INTO post (title, content, timeline, status, id_account, thumbnail_url) VALUES(?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setTimestamp(3, Timestamp.valueOf(post.getTimeline()));
            ps.setString(4, post.getStatus());
            ps.setLong(5, post.getIdAccount());
            ps.setString(6, post.getThumbnailUrl());
            return ps;
        }, keyHolder);

        // Retrieve the generated ID (if needed)
        Number generatedId = keyHolder.getKey();
        
        // Set the generated ID to the model (if needed)
        post.setIdPost(generatedId.longValue());
        return post;
    }

    @Override
    public int updatePost(Post post) {
        return jdbcTemplate.update(
                "UPDATE post SET title=?, content=?, timeline=?,status=?, thumbnail_url=? WHERE id_post=?",
                new Object[] { post.getTitle(), post.getContent(), java.time.LocalDateTime.now(), post.getStatus(),
                        post.getThumbnailUrl(), post.getIdPost() });
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM post WHERE id=?", id);
    }

    @Override
    public int savecmt(Comment comment) {
        return jdbcTemplate.update(
                "INSERT INTO comment (id_post,nameuser,content,timeline,id_cmt_parent) VALUES(?,?,?,?,?)",
                new Object[] { comment.getIdPost(), comment.getNameuser(), comment.getContent(), comment.getTimeline(),
                         comment.getIdCmtParent() });
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

    @Override
    public int saveImageUrls(Image image) {
        return jdbcTemplate.update("INSERT INTO image (id_post, image_url) VALUES (?,?)",
                new Object[] { image.getIdPost(), image.getImageUrl() });
    }

    @Override
    public List<String> getImageUrlsByIdPost(Long idPost) {
        try {
            String sql = "SELECT image_url FROM image WHERE id_post='" + idPost + "'";
            return jdbcTemplate.queryForList(sql, String.class);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public int deleteImageByIdPostAndImageUrl(Long idPost, String imageUrl) {
        return jdbcTemplate.update("DELETE FROM image WHERE id_post=? and image_url=?", idPost, imageUrl);
    }

}
