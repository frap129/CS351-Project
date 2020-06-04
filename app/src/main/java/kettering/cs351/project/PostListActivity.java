package kettering.cs351.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostListActivity extends AppCompatActivity {
    private String TAG = "PostListActivity";
    private ArrayList<Post> mPosts;
    private RecyclerView mList;
    private PostListAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        Intent launcher = getIntent();
        mPosts = (ArrayList<Post>) launcher.getSerializableExtra("posts");
        mList = (RecyclerView) findViewById(R.id.postList);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PostListAdapter(this, mPosts);
        Log.i(TAG, "List size: " + mPosts.size());
        mList.setAdapter(mAdapter);
    }
}
