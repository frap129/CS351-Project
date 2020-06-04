package kettering.cs351.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "PostListAdapter";
    private LayoutInflater mInflater;
    private ArrayList<Post> mPosts;
    private Context mContext;

    public PostListAdapter(Context context, ArrayList<Post> posts) {
        mInflater = LayoutInflater.from(context);
        mPosts = posts;
        mContext = context;
        Log.d(TAG, "Created PostListAdapter");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_post, parent, false);

        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        TextView postText = holder.itemView.findViewById(R.id.postText);
        postText.setText(post.post);
        Log.d(TAG, "Bound ViewHolder for item " + position);

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}