package kettering.cs351.project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;

import static kettering.cs351.project.Constants.*;

public class NewPostFragment extends DialogFragment {
    private String mAuthorName;
    private String uid;
    private OnPostCallback mCallback;

    public NewPostFragment(String name, OnPostCallback callback) {
        mAuthorName = name;
        mCallback = callback;
        uid = FirebaseAuth.getInstance().getUid();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View fragment = LayoutInflater.from(getContext()).inflate(R.layout.fragment_newpost, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(fragment);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);

        // Setup posting related views
        final TextInputLayout input = fragment.findViewById(R.id.newPostInput);
        final Switch anonymous = fragment.findViewById(R.id.anonymousSwitch);
        input.setEndIconDrawable(R.drawable.send);
        input.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = input.getEditText().getText().toString();
                if (!post.isEmpty()) {
                    // Make post anonymous if requested
                    if (anonymous.isChecked()) {
                        uid = defUID;
                        mAuthorName = defPoster;
                    }

                    // Build post object
                    Post newPost = new Post(mAuthorName, uid, new ArrayList<String>(),
                            new ArrayList<String>(), post, Calendar.getInstance().getTimeInMillis(),
                            new ArrayList<String>());

                    // Post new post to firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(collection)
                            .document(newPost.authorID + "+" + newPost.time)
                            .set(newPost, SetOptions.merge());

                    // Go back to PostListActivity
                    dialog.dismiss();
                    mCallback.PostComplete(newPost);
                }
            }
        });
        return dialog;
    }

    // Callback interface for notifying when fetch is complete
    interface OnPostCallback {
        void PostComplete(Post newPost);
    }
}
