package kettering.cs351.project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;

public class NewPostFragment extends DialogFragment {
    private String mAuthorName;
    public Post mPost;
    public NewPostFragment(String name) {
        mAuthorName = name;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View fragment = LayoutInflater.from(getContext()).inflate(R.layout.fragment_newpost, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(fragment);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);

        final TextInputLayout input = fragment.findViewById(R.id.newPostInput);
        input.setEndIconDrawable(R.drawable.send);
        input.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = input.getEditText().getText().toString();
                if (!post.isEmpty()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Post newPost = new Post(mAuthorName,
                            FirebaseAuth.getInstance().getUid(), 0, 0, post,
                            Calendar.getInstance().getTimeInMillis());
                    db.collection("posts").document().set(newPost, SetOptions.merge());
                    dialog.dismiss();
                }
            }
        });
        return dialog;
    }
}
