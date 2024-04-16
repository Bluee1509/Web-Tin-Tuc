package jdbc;

import java.time.LocalDateTime;

public class Post {
	private long id;
	private String title;
	private String content;
	private String username;
	private LocalDateTime timeline;
	private String status;
	
	public Post() {
		
	}
	

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Post(long id, String title, String content, String username, LocalDateTime timeline,String status) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.username = username;
		this.timeline = timeline;
		this.status = status;
	}

	public Post(String title, String content, String username, LocalDateTime timeline,String status) {
		super();
		this.title = title;
		this.content = content;
		this.username = username;
		this.timeline = timeline;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String gettitle() {
		return title;
	}

	public void settitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getTimeline() {
		return timeline;
	}

	public void setTimeline(LocalDateTime timeline) {
		this.timeline = timeline;
	}
	
	@Override
		public String toString() {
			return "Post [id=" + id + ", title=" + title + ", content=" + content + ", username=" + username + ",timeline=" + timeline + "]";
	}
	
	
	
	
	
	
}
