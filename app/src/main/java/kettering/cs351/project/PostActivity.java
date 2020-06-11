package kettering.cs351.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
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

        // Handle new comments
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


        final String uid = FirebaseAuth.getInstance().getUid();
        final int enabledColor = getColor(R.color.colorAccent);
        final int disabledColor = Color.BLACK;

        // Setup Like button and resources
        final TextView likeCount = findViewById(R.id.likeCount);
        likeCount.setText(String.valueOf(mPost.likes.size()));
        final ImageButton likeButton = findViewById(R.id.likeButton);
        final Drawable like = getDrawable(R.drawable.like);
        likeButton.setImageDrawable(like);
        if (!mPost.likes.isEmpty() && mPost.likes.contains(uid))
            like.setTint(enabledColor);

        // Setup dislikes and resources
        final TextView dislikeCount = findViewById(R.id.dislikeCount);
        dislikeCount.setText(String.valueOf(mPost.dislikes.size()));
        final ImageButton dislikeButton = findViewById(R.id.dislikeButton);
        final Drawable dislike = getDrawable(R.drawable.dislike);
        dislikeButton.setImageDrawable(dislike);
        if (!mPost.dislikes.isEmpty() && mPost.dislikes.contains(uid))
            dislike.setTint(enabledColor);

        // Handle button clicks
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPost.likes.contains(uid)) {
                    mPost.likes.remove(uid);
                    like.setTint(disabledColor);
                } else {
                    if (mPost.dislikes.contains(uid)) {
                        mPost.dislikes.remove(uid);
                        dislike.setTint(disabledColor);
                    }
                    mPost.likes.add(uid);
                    like.setTint(enabledColor);
                }
                likeCount.setText(String.valueOf(mPost.likes.size()));
                dislikeCount.setText(String.valueOf(mPost.dislikes.size()));
            }
        });
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPost.dislikes.contains(uid)) {
                    mPost.dislikes.remove(uid);
                    dislike.setTint(disabledColor);
                } else {
                    if (mPost.likes.contains(uid)) {
                        mPost.likes.remove(uid);
                        like.setTint(disabledColor);
                    }
                    mPost.dislikes.add(uid);
                    dislike.setTint(enabledColor);
                }
                likeCount.setText(String.valueOf(mPost.likes.size()));
                dislikeCount.setText(String.valueOf(mPost.dislikes.size()));
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
