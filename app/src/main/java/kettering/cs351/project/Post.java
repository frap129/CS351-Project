package kettering.cs351.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    public String author = "";
    public String authorID = "";
    public ArrayList<String> dislikes;
    public ArrayList<String> likes;
    public String post = "";
    public long time = 0;
    public ArrayList<String> comments;

    public Post(String author, String authorID, List<String> dislikes, List<String> likes, String post, long time,
            List<String> comments) {
        this.author = author;
        this.authorID = authorID;
        if (dislikes == null)
            this.dislikes = new ArrayList<String>();
        else
            this.dislikes = (ArrayList<String>) dislikes;
        if (likes == null)
            this.likes = new ArrayList<String>();
        else
            this.likes = (ArrayList<String>) likes;
        this.post = post;
        this.time = time;
        this.comments = (ArrayList<String>) comments;
    }
}
