package kettering.cs351.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;

public class PostListActivity extends AppCompatActivity implements NewPostFragment.OnPostCallback {
    private String TAG = "PostListActivity";
    private ArrayList<Post> mPosts;
    private RecyclerView mList;
    private PostListAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        // Get the list of posts from launch activity
        Intent launcher = getIntent();
        mPosts = (ArrayList<Post>) launcher.getSerializableExtra("posts");

        // Build the recyclerview to display posts
        mList = (RecyclerView) findViewById(R.id.postList);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PostListAdapter(this, mPosts);
        Log.i(TAG, "List size: " + mPosts.size());
        mList.setAdapter(mAdapter);

        final String name = getSharedPreferences(getString(R.string.author), Context.MODE_PRIVATE)
                .getString(getString(R.string.author), "Anonymous Poster");

        final NewPostFragment.OnPostCallback callback = this;
        // Setup new post FAB
        FloatingActionButton newPost = (FloatingActionButton) findViewById(R.id.newPost);
        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                NewPostFragment postWindow = new NewPostFragment(name, callback);
                postWindow.show(transaction, "New Post");
            }
        });
    }

    @Override
    public void PostComplete(Post newPost) {
        mPosts.add(newPost);
        mPosts.sort(new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return Long.compare(o2.time, o1.time);
            }
        });
        mAdapter.notifyDataSetChanged();
    }
}
