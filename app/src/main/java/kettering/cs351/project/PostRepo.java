package kettering.cs351.project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static kettering.cs351.project.Constants.collection;
import static kettering.cs351.project.Constants.sortByNew;

public class PostRepo implements Serializable {
    private final String TAG = "PostRepo";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ArrayList<Post> posts = new ArrayList<>();

    public PostRepo(final OnCompleteCallback callback) {
        // Get all posts from Firestore
        db.collection(collection)
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
                        posts.sort(sortByNew);
                        // Execute callback to inform of completion.
                        callback.FirestoreGetComplete();
                    }
                });
    }

    // Simple function to build a Post object from a Firebase Document Snapshot
    private Post documentToPost(QueryDocumentSnapshot doc) {
        return new Post((String) doc.get("author"), (String) doc.get("authorID"),
                (List<String>) doc.get("dislikes"), (List<String>) doc.get("likes"),
                (String) doc.get("post"), doc.getLong("time"),  (List<String>) doc.get("comments"));
    }

    // Callback interface for notifying when fetch is complete
    interface OnCompleteCallback {
        void FirestoreGetComplete();
    }
}
