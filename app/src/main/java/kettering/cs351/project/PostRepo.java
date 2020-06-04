package kettering.cs351.project;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PostRepo {
    private final String TAG = "PostRepo";
    public final String POST_COLLECTION = "posts";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String uid = FirebaseAuth.getInstance().getUid();
    public ObservableArrayList<Post> ownPosts = new ObservableArrayList<>();
    public ObservableArrayList<Post> posts = new ObservableArrayList<>();

    public PostRepo() {
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
                            }
                        }
                    }
                });
    }

    private Post documentToPost(QueryDocumentSnapshot doc) {
        return new Post((String) doc.get("author"), (String) doc.get("authorID"),
                doc.getLong("dislikes").intValue(), doc.getLong("likes").intValue(), (String) doc.get("post"),
                doc.getDouble("time"));
    }
}
