package jdbc;

import java.util.List;

public class PagePost {

    private List<Post> content;
    private int total;

    public PagePost() {
        super();
    }

    public List<Post> getContent() {
        return content;
    }

    public void setContent(List<Post> content) {
        this.content = content;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
