package jdbc;

import java.time.LocalDateTime;

public class Comment {

    private long idCmt;
    private long idPost;
    private String nameuser;
    private String content;
    private LocalDateTime timeline;
    private Long idCmtParent = null;

    public Comment() {
        super();
    }

    public Comment(long idPost, String nameuser, String content, LocalDateTime timeline ) {
        super();
        this.idPost = idPost;
        this.nameuser = nameuser;
        this.content = content;
        this.timeline = timeline;
    }

    public Comment(long idPost, String nameuser, String content, LocalDateTime timeline, 
            Long idCmtParent) {
        super();
        this.idPost = idPost;
        this.nameuser = nameuser;
        this.content = content;
        this.timeline = timeline;
        this.idCmtParent = idCmtParent;
    }

    public long getIdCmt() {
        return idCmt;
    }

    public void setidCmt(long idCmt) {
        this.idCmt = idCmt;
    }

    public long getIdPost() {
        return idPost;
    }

    public void setIdPost(long idPost) {
        this.idPost = idPost;
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


    public Long getIdCmtParent() {
        return idCmtParent;
    }

    public void setIdCmtParent(Long idCmtParent) {
        this.idCmtParent = idCmtParent;
    }

	@Override
	public String toString() {
		return "Comment [idCmt=" + idCmt + ", idPost=" + idPost + ", nameuser=" + nameuser + ", content=" + content
				+ ", timeline=" + timeline + ", idCmtParent=" + idCmtParent + "]";
	}


}