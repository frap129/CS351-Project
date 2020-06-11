package kettering.cs351.project;

import java.util.Comparator;

public final class Constants {
    // Numbers
    public static final int postListSizeLimit = 280;
    public static final int postReturn = 420;

    // Strings
    public static final String collection = "posts";
    public static final String postExtra = "post";
    public static final String listExtra = "posts";
    public static final String postTransact = "New Post";
    public static final String defPoster = "Anonymous Poster";
    public static final String defCommenter = "Anonymous Commenter";
    public static final String defUID = "anonymous";
    public static final String todayFormat = "h:mm a";
    public static final String oldShortFormat = "M/d/yy";
    public static final String oldLongFormat = "h:mm a, M/d/yy";

    // Other
    public static final Comparator<Post> sortByNew = new Comparator<Post>() {
        @Override
        public int compare(Post o1, Post o2) {
            return Long.compare(o2.time, o1.time);
        }
    };
}
