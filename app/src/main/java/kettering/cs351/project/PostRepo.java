package kettering.cs351.project;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class PostRepo implements Serializable {
    private final String TAG = "PostRepo";
    public final String POST_COLLECTION = "posts";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ArrayList<Post> posts = new ArrayList<>();

    public PostRepo(final OnCompleteCallback callback) {
        // Get all posts from Firestore
        db.collection(POST_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            // For each posts, filter out the users own and format them into lists
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    posts.add(documentToPost(document));
                            }
                        }

                        // Sort posts by newest to oldest
                        posts.sort(new Comparator<Post>() {
                            @Override
                            public int compare(Post o1, Post o2) {
                                return Long.compare(o2.time, o1.time);
                            }
                        });

                        // Execute callback to inform of completion.
                        callback.FirestoreGetComplete();
                    }
                });
    }

    // Simple function to build a Post object from a Firebase Document Snapshot
    private Post documentToPost(QueryDocumentSnapshot doc) {
        return new Post((String) doc.get("author"), (String) doc.get("authorID"),
                doc.getLong("dislikes").intValue(), doc.getLong("likes").intValue(), (String) doc.get("post"),
                doc.getLong("time"));
    }

    // Callback interface for notifying when fetch is complete
    interface OnCompleteCallback {
        void FirestoreGetComplete();
    }
}
