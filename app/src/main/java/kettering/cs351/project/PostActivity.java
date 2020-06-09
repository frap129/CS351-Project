package kettering.cs351.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    }
}
