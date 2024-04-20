package jdbc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    NewsRepository newsRepository;
    @Autowired
    NewsServices newsServices;

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getNewsById(@PathVariable("id") long id) {
        Post post = newsServices.getPost(id);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // dang nhap
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestParam String username, @RequestParam String password) {
        List<Account> result = new ArrayList<>();
        result.addAll(newsRepository.checklogin(username, password));
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            Account account = new Account();
            account.setIdAccount(result.get(0).getIdAccount());
            account.setName(result.get(0).getName());
            account.setPassword(result.get(0).getPassword());
            account.setRole(result.get(0).getRole());
            account.setUsername(result.get(0).getUsername());
            account.setAccessRight(result.get(0).getAccessRight());
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
    }

    // tim kiem theo title
    @GetMapping("/post")
    public ResponseEntity<List<Post>> getTitle(@RequestParam(required = false) String title) {
        try {
            List<Post> posts = new ArrayList<Post>();
            if (title == null) {
                newsRepository.findAll().forEach(posts::add);
            } else {
                newsRepository.findByTitleContaining(title).forEach(posts::add);
            }
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                for (Post post : posts) {
                    post.setImageUrls(newsServices.getImageUrlsByIdPost(post.getIdPost()));
                }
                return new ResponseEntity<>(posts, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // post bai
    @PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> createPost(@RequestPart Post post, @RequestPart MultipartFile thumbnail,
            @RequestPart MultipartFile[] images) {
        String thumbnailUrl = newsServices.uploadImage(thumbnail);
        List<String> imagesUrls = new ArrayList<String>();
        for (MultipartFile image : images) {
            imagesUrls.add(newsServices.uploadImage(image));
        }
        Post postInserted = newsRepository.save(new Post(post.getTitle(), post.getContent(),
                java.time.LocalDateTime.now(), "Pending", thumbnailUrl, post.getIdAccount()));
        post.setIdPost(postInserted.getIdPost());
        for (String imageUrl : imagesUrls) {
            newsRepository.saveImageUrls(new Image(post.getIdPost(), imageUrl));
        }
        return new ResponseEntity<>("Post was created successfully.", HttpStatus.CREATED);
    }

    // duyetbai
    @PutMapping("/post/{id}/approve")
    public ResponseEntity<String> approvePost(@PathVariable("id") long id, @RequestParam String status) {
        Post post = newsRepository.findById(id);
        if (post != null) {
            post.setStatus(status);
            newsRepository.updatePost(post);
            return new ResponseEntity<>("Post da duoc duyet", HttpStatus.OK);
        } else
            return new ResponseEntity<>("Cannot find News with id=" + id, HttpStatus.NOT_FOUND);
    }

    // update post
    @PutMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> updateNews(@RequestPart Post post, @RequestPart MultipartFile thumbnail, @RequestPart MultipartFile[] images) {
        Post oldPost = newsServices.getPost(post.getIdPost());
        if (oldPost != null) {
            String oldThumbnailUrl = oldPost.getThumbnailUrl();
            List<String> oldImageUrls = oldPost.getImageUrls();
            boolean isDeletedImage = false;
            isDeletedImage = newsServices.deleteImage(oldThumbnailUrl);
            for (String imageUrl : oldImageUrls) {
                isDeletedImage = newsServices.deleteImage(imageUrl);
                newsRepository.deleteImageByIdPostAndImageUrl(post.getIdPost(), imageUrl);
            }
            if (isDeletedImage) {
                String newThumbnailUrl = newsServices.uploadImage(thumbnail);
                List<String> newImagesUrls = new ArrayList<String>();
                for (MultipartFile image : images) {
                    newImagesUrls.add(newsServices.uploadImage(image));
                }
                for (String imageUrl : newImagesUrls) {
                    newsRepository.saveImageUrls(new Image(post.getIdAccount(), imageUrl));
                }
                oldPost.setTitle(post.getTitle());
                oldPost.setContent(post.getContent());
                oldPost.setTimeline(post.getTimeline());
                oldPost.setStatus(post.getStatus());
                oldPost.setIdAccount(post.getIdAccount());
                oldPost.setThumbnailUrl(newThumbnailUrl);
                newsRepository.updatePost(oldPost);
            }
            return new ResponseEntity<>("News was updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot find News with id=" + post.getIdPost(), HttpStatus.NOT_FOUND);
        }
    }

    // delete post
    @DeleteMapping(value = "/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id) {
        try {
            String status = "Deleted";
            int result = newsRepository.updatePostStatus(status, id);
            if (result == 0) {
                return new ResponseEntity<>("Cannot find Post with id=" + id, HttpStatus.OK);
            }
            return new ResponseEntity<>("Post was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete Post.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // show cmt cua 1 post theo idpost
    @GetMapping("/comment/{idpost}")
    public ResponseEntity<List<Comment>> showComment(@PathVariable("idpost") long idpost) {
        List<Comment> comment = newsRepository.findByIdpost(idpost);
        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // create cmt
    @PostMapping("/comment")
    public ResponseEntity<String> createComment(@RequestBody Comment comment) {
        newsRepository.savecmt(new Comment(comment.getIdPost(), comment.getNameuser(), comment.getContent(),
                java.time.LocalDateTime.now(), comment.getIdCmtParent()));
        return new ResponseEntity<>("Comment was created successfully.", HttpStatus.CREATED);
    }

}
