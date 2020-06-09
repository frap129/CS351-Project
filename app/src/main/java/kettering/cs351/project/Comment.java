package kettering.cs351.project;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    public String author = "Anonymous Commenter";
    public long time = 0;
    public String comment = "";

    public Comment(String author, long time, String comment) {
        this.author = author;
        this.time = time;
        this.comment = comment;
    }

    public Comment(String json) throws JSONException {
        JSONObject jsonComment = new JSONObject(json);
        author = jsonComment.getString("author");
        time = jsonComment.getLong("time");
        comment = jsonComment.getString("comment");
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        try {
            json.put("author", author);
            json.put("time", time);
            json.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  json.toString();
    }
}
