package jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jdbc.NewsRepository;
import jdbc.Post;
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
			  account.setName(result.get(0).getName());
			  account.setPassword(result.get(0).getPassword());
			  account.setRole(result.get(0).getRole());
			  account.setUsername(result.get(0).getUsername());
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
	  //post bai
	  @PostMapping("/post")
	  public ResponseEntity<String> createPost(@RequestBody Post post){
		  
		        newsRepository.save(new Post(post.gettitle(),post.getContent(),post.getUsername(),java.time.LocalDateTime.now(),"pending"));
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
	  @PutMapping("/post/{id}")
	  public ResponseEntity<String> updateNews(@PathVariable("id") long id, @RequestBody Post post){
		  Post _post = newsRepository.findById(id);
		  if(_post != null) {
			  
			  _post.setId(id);
			  _post.settitle(post.gettitle());
			  _post.setContent(post.getContent());
			  _post.setUsername(post.getUsername());
			  _post.setTimeline(post.getTimeline());
			  newsRepository.updatePost(_post);
			  return new ResponseEntity<>("News was updated successfully.", HttpStatus.OK);
		  }else {
			  return new ResponseEntity<>("Cannot find News with id=" + id, HttpStatus.NOT_FOUND);
		  }
	  }
	  //delete post
	  @DeleteMapping("/post/{id}")
	  public ResponseEntity<String> deletePost(@PathVariable("id") long id){
		  try {
			  int result = newsRepository.deleteById(id);
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
	      newsRepository.savecmt(new Comment(comment.getIdpost(),comment.getIdcmt(),comment.getNameuser(),comment.getContent(),java.time.LocalDateTime.now(),comment.getEvaluate())); 
		  return new ResponseEntity<>("Comment was created successfully.", HttpStatus.CREATED); 
	  }
	  
	  

}
