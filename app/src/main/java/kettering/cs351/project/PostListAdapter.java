package kettering.cs351.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static kettering.cs351.project.Constants.oldShortFormat;
import static kettering.cs351.project.Constants.postListSizeLimit;
import static kettering.cs351.project.Constants.todayFormat;

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "PostListAdapter";
    private LayoutInflater mInflater;
    private ArrayList<Post> mPosts;
    private PostClickCallback mCallback;

    public PostListAdapter(Context context, ArrayList<Post> posts, PostClickCallback callback) {
        mInflater = LayoutInflater.from(context);
        mPosts = posts;
        mCallback = callback;
        Log.d(TAG, "Created PostListAdapter");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Designate that all posts use the item_post layout
        View view = mInflater.inflate(R.layout.item_post, parent, false);

        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        // Build Post UI for a given list item
        Post post = mPosts.get(position);

        // Set Post text
        TextView postText = holder.itemView.findViewById(R.id.postText);
        String trimmedPost = post.post.substring(0, Math.min(post.post.length(), postListSizeLimit));
        if (post.post.length() > postListSizeLimit)
            trimmedPost += "...";
        postText.setText(trimmedPost);

        // Set post author
        TextView postAuthor = holder.itemView.findViewById(R.id.postAuthor);
        postAuthor.setText(post.author);

        // Set post time
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(post.time);
        SimpleDateFormat format;
        if (cal.getTime().getDate() == Calendar.getInstance().getTime().getDate())
            format = new SimpleDateFormat(todayFormat, Locale.getDefault());
        else
            format = new SimpleDateFormat(oldShortFormat, Locale.getDefault());
        TextView postTime = holder.itemView.findViewById(R.id.postTime);
        postTime.setText(format.format(cal.getTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPostClick(position);
            }
        });

        Log.d(TAG, "Bound ViewHolder for item " + position);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    interface PostClickCallback {
        void onPostClick(int position);
    }
}