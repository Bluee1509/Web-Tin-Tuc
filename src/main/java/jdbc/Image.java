package jdbc;

public class Image {

    private long idImg;
    private long idPost;
    private String imageUrl;

    public Image() {
        super();
    }

    public Image(long idPost, String imageUrl) {
        super();
        this.idPost = idPost;
        this.imageUrl = imageUrl;
    }

    public long getIdImg() {
        return idImg;
    }

    public void setIdImg(long idImg) {
        this.idImg = idImg;
    }

    public long getIdPost() {
        return idPost;
    }

    public void setIdPost(long idPost) {
        this.idPost = idPost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Image [idImg=" + idImg + ", idPost=" + idPost + ", imageUrl=" + imageUrl + "]";
    }

}
