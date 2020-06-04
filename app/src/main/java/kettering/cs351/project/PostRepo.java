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
    public String uid = FirebaseAuth.getInstance().getUid();
    public ArrayList<Post> ownPosts = new ArrayList<>();
    public ArrayList<Post> posts = new ArrayList<>();

    public PostRepo(final OnCompleteCallback callback) {
        db.collection(POST_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("authorID").equals(uid))
                                    ownPosts.add(documentToPost(document));
                                else
                                    posts.add(documentToPost(document));

                                Log.i(TAG, "Added post to list");
                            }
                        }
                        posts.sort(new Comparator<Post>() {
                            @Override
                            public int compare(Post o1, Post o2) {
                                return Double.compare(o1.time, o2.time);
                            }
                        });
                        callback.FirestoreGetComplete();
                    }
                });
    }

    private Post documentToPost(QueryDocumentSnapshot doc) {
        return new Post((String) doc.get("author"), (String) doc.get("authorID"),
                doc.getLong("dislikes").intValue(), doc.getLong("likes").intValue(), (String) doc.get("post"),
                doc.getDouble("time"));
    }

    interface OnCompleteCallback {
        void FirestoreGetComplete();
    }
}
