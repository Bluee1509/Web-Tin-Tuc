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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getNewsById(@PathVariable("id") long id) {
        Post post = newsRepository.findById(id);

        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //dang nhap
    @PostMapping("/login")
   public ResponseEntity<Account> login(@RequestParam String username,@RequestParam String password) {

        List<Account> result= new ArrayList<>();
        result.addAll(newsRepository.checklogin(username, password));
        if(result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }else {
            Account account = new Account();
            account.setIdAccount(result.get(0).getIdAccount());
            account.setName(result.get(0).getName());
            account.setPassword(result.get(0).getPassword());
            account.setRole(result.get(0).getRole());
            account.setUsername(result.get(0).getUsername());
            account.setAccessRight(result.get(0).getAccessRight());
            return new ResponseEntity<>(account,HttpStatus.OK);
        }
    }
    // tim kiem theo title
    @GetMapping("/post")
    public ResponseEntity<List<Post>> getTitle(@RequestParam(required = false) String title) {
        try {
            List<Post> post = new ArrayList<Post>();

            if (title == null) {
                newsRepository.findAll().forEach(post::add);
//	    	  List<Tutorial> a = tutorialRepository.findAll();
//	    	  a.forEach(tutorials::add);

            } else {
                newsRepository.findByTitleContaining(title).forEach(post::add);
            }
            if (post.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                return new ResponseEntity<>(post, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // post bai
    @PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> createPost(@RequestPart Post post, @RequestPart MultipartFile file) {
        String imageUrl = null;
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadDir = "src/main/resources/static/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                imageUrl = "/images/" + fileName; // URL để truy cập hình ảnh từ frontend
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
        if (imageUrl != null) {
            newsRepository.save(new Post(post.getTitle(), post.getContent(), java.time.LocalDateTime.now(), "pending",
                    imageUrl, post.getIdAccount()));
        }
        return new ResponseEntity<>("Post was created successfully.", HttpStatus.CREATED);

    }

    //duyetbai
    @PutMapping("/post/{id}/approve")
    public ResponseEntity<String> approvePost(@PathVariable("id")long id, @RequestParam String status){
        Post post = newsRepository.findById(id);
        if(post !=null) {
            post.setStatus(status);
            newsRepository.updatePost(post);
            return new ResponseEntity<>("Post da duoc duyet", HttpStatus.OK);
        }else return new ResponseEntity<>("Cannot find News with id=" + id, HttpStatus.NOT_FOUND);
    }
    //update post
    @PutMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> updateNews(@RequestPart Post post, @RequestPart MultipartFile file){
        Post _post = newsRepository.findById(post.getIdPost());
        // Construct the file path of the image in the resources directory
        String filePath = "src/main/resources/static/" + _post.getImageUrl();
        File imageFile = new File(filePath);

        // Check if the file exists
   
        if (!imageFile.exists()) {
            System.out.println("Image file does not exist");
            return new ResponseEntity<>("Image file does not exist.", HttpStatus.NOT_FOUND);
        }
        boolean deleted = imageFile.delete();
        String imageUrl = null;
        if (deleted) {
            
            try {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                String uploadDir = "src/main/resources/static/images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = file.getInputStream()) {
                    Path filePath_2 = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath_2, StandardCopyOption.REPLACE_EXISTING);
                    imageUrl = "/images/" + fileName; // URL để truy cập hình ảnh từ frontend
                }
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
            }
        }
        if(_post != null) {
            _post.setTitle(post.getTitle());
            _post.setContent(post.getContent());
            _post.setTimeline(post.getTimeline());
            _post.setStatus(post.getStatus());
            _post.setIdAccount(post.getIdAccount());
            _post.setImageUrl(imageUrl);
            newsRepository.updatePost(_post);
            return new ResponseEntity<>("News was updated successfully.", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Cannot find News with id=" + post.getIdPost(), HttpStatus.NOT_FOUND);
        }
    }
    //delete post
    @DeleteMapping(value = "/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id){
        try {
            Post _post = newsRepository.findById(id);
            String status = "Deleted";
            int result = newsRepository.updatePostStatus(status, id);
            if(result == 0) {
                return new ResponseEntity<>("Cannot find Post with id=" + id, HttpStatus.OK);
            }
            return new ResponseEntity<>("Post was deleted successfully.", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("Cannot delete Post.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //show cmt cua 1 post theo idpost
    @GetMapping("/comment/{idpost}")
    public ResponseEntity<List<Comment>> showComment(@PathVariable("idpost") long idpost){
        List<Comment> comment = newsRepository.findByIdpost(idpost);
        if( comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //create cmt
    @PostMapping("/comment")
    public ResponseEntity<String> createComment(@RequestBody Comment comment){

        newsRepository.savecmt(new Comment(comment.getIdPost(),comment.getNameuser(),comment.getContent(),java.time.LocalDateTime.now(),comment.getEvaluate(), comment.getIdCmtParent()));
        return new ResponseEntity<>("Comment was created successfully.", HttpStatus.CREATED);
    }

}
