package jdbc;

import java.time.LocalDateTime;
import java.util.List;

public class Post {

    private long idPost;
    private String title;
    private String content;
    private LocalDateTime timeline;
    private String status;
    private String thumbnailUrl;
    private long idAccount;

    public Post() {
    }

    public Post(long idPost, String title, String content, LocalDateTime timeline, String status, String thumbnailUrl,
            long idAccount) {
        super();
        this.idPost = idPost;
        this.title = title;
        this.content = content;
        this.timeline = timeline;
        this.status = status;
        this.thumbnailUrl = thumbnailUrl;
        this.idAccount = idAccount;
    }

    public Post(String title, String content, LocalDateTime timeline, String status, String thumbnailUrl, long idAccount) {
        super();
        this.title = title;
        this.content = content;
        this.timeline = timeline;
        this.status = status;
        this.thumbnailUrl = thumbnailUrl;
        this.idAccount = idAccount;
    }

    public long getIdPost() {
        return idPost;
    }

    public void setIdPost(long idPost) {
        this.idPost = idPost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(long idAccount) {
        this.idAccount = idAccount;
    }

    @Override
    public String toString() {
        return "Post [idPost=" + idPost + ", title=" + title + ", content=" + content + ", timeline=" + timeline
                + ", status=" + status + ", thumbnailUrl=" + thumbnailUrl + ", idAccount=" + idAccount + "]";
    }

}
