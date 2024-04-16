package jdbc;

import java.time.LocalDateTime;

public class Comment {
	private long id;
	private long idpost;
	private Long idcmt;
	private String nameuser;
	private String content;
	private LocalDateTime timeline;
	private long evaluate;
	
	
	public Comment(long idpost, Long idcmt, String nameuser, String content, LocalDateTime timeline, long evaluate) {
		super();
		this.idpost = idpost;
		this.idcmt = idcmt;
		this.nameuser = nameuser;
		this.content = content;
		this.timeline = timeline;
		this.evaluate = evaluate;
	}
	public Comment(long id, long idpost, Long idcmt, String nameuser, String content, LocalDateTime timeline,
			long evaluate) {
		super();
		this.id = id;
		this.idpost = idpost;
		this.idcmt = idcmt;
		this.nameuser = nameuser;
		this.content = content;
		this.timeline = timeline;
		this.evaluate = evaluate;
	}
	public Comment() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdpost() {
		return idpost;
	}
	public void setIdpost(long idpost) {
		this.idpost = idpost;
	}
	public Long getIdcmt() {
		return idcmt;
	}
	public void setIdcmt(Long idcmt) {
		this.idcmt = idcmt;
	}
	public String getNameuser() {
		return nameuser;
	}
	public void setNameuser(String nameuser) {
		this.nameuser = nameuser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getTimeline() {
		return timeline;
	}
	public void setTimeline(LocalDateTime timeline) {
		this.timeline = timeline;
	}
	public long getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(long evaluate) {
		this.evaluate = evaluate;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", idpost=" + idpost + ", idcmt=" + idcmt + ", nameuser=" + nameuser + ", content="
				+ content + ", timeline=" + timeline + ", evaluate=" + evaluate + "]";
	}
	
}