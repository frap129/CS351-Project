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
import java.util.function.Predicate;

import static kettering.cs351.project.Constants.*;

public class PostListActivity extends AppCompatActivity
        implements NewPostFragment.OnPostCallback, PostListAdapter.PostClickCallback {
    private String TAG = "PostListActivity";
    private ArrayList<Post> mPosts;
    private RecyclerView mList;
    private PostListAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        // Get the list of posts from launch activity
        Intent launcher = getIntent();
        mPosts = (ArrayList<Post>) launcher.getSerializableExtra(listExtra);

        // Build the recyclerview to display posts
        mList = (RecyclerView) findViewById(R.id.postList);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PostListAdapter(this, mPosts, this);
        Log.i(TAG, "List size: " + mPosts.size());
        mList.setAdapter(mAdapter);

        final String name = getSharedPreferences(getString(R.string.author), Context.MODE_PRIVATE)
                .getString(getString(R.string.author), defPoster);

        final NewPostFragment.OnPostCallback callback = this;
        // Setup new post FAB
        FloatingActionButton newPost = findViewById(R.id.newPost);
        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                NewPostFragment postWindow = new NewPostFragment(name, callback);
                postWindow.show(transaction, postTransact);
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

    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(postExtra, mPosts.get(position));
        startActivityForResult(intent, postReturn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 420 && resultCode == RESULT_OK && data != null) {
            final Post updated = (Post) data.getSerializableExtra(postExtra);

            // Remove post that got updated
            mPosts.removeIf(new Predicate<Post>() {
                @Override
                public boolean test(Post post) {
                    return post.time == updated.time && post.authorID.equals(updated.authorID);
                }
            });

            // Add updated post
            mPosts.add(updated);

            // Sort list to maintain the same order
            mPosts.sort(new Comparator<Post>() {
                @Override
                public int compare(Post o1, Post o2) {
                    return Long.compare(o2.time, o1.time);
                }
            });
            mAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
