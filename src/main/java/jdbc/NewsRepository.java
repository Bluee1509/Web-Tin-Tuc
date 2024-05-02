package jdbc;

import java.util.List;
import java.util.Map;

public interface NewsRepository {

    int deleteById(Long id);

    int updatePost(Post a);

    Post findById(Long id);

    List<Post> findByTitleContaining(String title, int pageNumber, int pageSize);

    List<Post> findAll(int pageNumber, int pageSize);

    Post save(Post post);

    int savecmt(Comment b);

    List<Account> checklogin(String username, String password);

    List<Comment> findByIdpost(long idpost);

    int updatePostStatus(String status, Long id);

    int saveImageUrls(Image image);

    List<String> getImageUrlsByIdPost(Long idPost);

    int deleteImageByIdPostAndImageUrl(Long idPost, String imageUrl);
    
    Map<String, Integer> numberOfPostBaseOnStatus();

    List<Post> findByStatus(String status, int pageNumber, int pageSize);

    int countPostbyStatus(String status);

    int countAll();
    
    int countPostbyTitle(String title);

	List<Comment> findByIdCmtParent(long id_cmt_parent);

}
