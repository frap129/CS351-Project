package kettering.cs351.project;

public class Post {
    public String author = "";
    public String authorID = "";
    public int dislikes = 0;
    public int likes = 0;
    public String post = "";
    public double time = 0.0;

    public Post(String author, String authorID, int dislikes, int likes, String post, double time) {
        this.author = author;
        this.authorID = authorID;
        this.dislikes = dislikes;
        this.likes = likes;
        this.post = post;
        this.time = time;
    }

    public Post() {
    }
}
