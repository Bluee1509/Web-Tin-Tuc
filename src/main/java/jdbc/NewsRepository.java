package jdbc;

import java.util.List;

public interface NewsRepository {

    int deleteById(Long id);

    int updatePost(Post a);

    Post findById(Long id);

    List<Post> findByTitleContaining(String title);

    List<Post> findAll();

    int save(Post a);

    int savecmt(Comment b);

    List<Account> checklogin(String username, String password);

    List<Comment> findByIdpost(long idpost);

    int updatePostStatus(String status, Long id);

}
