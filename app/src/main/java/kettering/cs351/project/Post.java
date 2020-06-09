package kettering.cs351.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    public String author = "";
    public String authorID = "";
    public int dislikes = 0;
    public int likes = 0;
    public String post = "";
    public long time = 0;
    public ArrayList<String> comments;

    public Post(String author, String authorID, int dislikes, int likes, String post, long time,
            List<String> comments) {
        this.author = author;
        this.authorID = authorID;
        this.dislikes = dislikes;
        this.likes = likes;
        this.post = post;
        this.time = time;
        this.comments = (ArrayList<String>) comments;
    }
}
