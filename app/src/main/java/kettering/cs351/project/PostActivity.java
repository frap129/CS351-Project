package kettering.cs351.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {
    private Post mPost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent launcher = getIntent();
        mPost = (Post) launcher.getSerializableExtra("post");

        // Set Post text
        TextView postText = findViewById(R.id.postText);
        postText.setText(mPost.post);

        // Set post author
        TextView postAuthor = findViewById(R.id.postAuthor);
        postAuthor.setText(mPost.author);

        // Set post time
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mPost.time);
        SimpleDateFormat format;
        if (cal.getTime().getDate() == Calendar.getInstance().getTime().getDate())
            format = new SimpleDateFormat("h:mm a", Locale.getDefault());
        else
            format = new SimpleDateFormat("M/d/yy, h:mm a", Locale.getDefault());
        TextView postTime = findViewById(R.id.postTime);
        postTime.setText(format.format(cal.getTime()));

        // Build list of comments
        RecyclerView list = findViewById(R.id.commentList);
        list.setLayoutManager(new LinearLayoutManager(this));
        final CommentListAdapter adapter = new CommentListAdapter(this, mPost.comments);
        list.setAdapter(adapter);

        final String name = getSharedPreferences(getString(R.string.author), Context.MODE_PRIVATE)
                .getString(getString(R.string.author), "Anonymous Commenter");

        final TextInputLayout input = findViewById(R.id.newCommentInput);
        input.setEndIconDrawable(R.drawable.send);
        input.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = input.getEditText().getText().toString();
                if (!comment.isEmpty()) {
                    Comment newComment = new Comment(name, Calendar.getInstance().getTimeInMillis(),
                            comment);
                    mPost.comments.add(newComment.toString());
                    adapter.notifyDataSetChanged();
                    input.getEditText().setText("");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Send updated post back to PostListActivity
        Intent output = new Intent();
        output.putExtra("post", mPost);
        setResult(RESULT_OK, output);

        // Post updated post to firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .document(mPost.authorID + "+" + mPost.time)
                .set(mPost, SetOptions.merge());

        // Exit the activity as requested
        finish();
    }
}
