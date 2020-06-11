package kettering.cs351.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static kettering.cs351.project.Constants.oldShortFormat;
import static kettering.cs351.project.Constants.todayFormat;

public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "PostListAdapter";
    private LayoutInflater mInflater;
    private ArrayList<String> mComments;

    public CommentListAdapter(Context context, ArrayList<String> comments) {
        mInflater = LayoutInflater.from(context);
        mComments = comments;
        Log.d(TAG, "Created CommentListAdapter");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Designate that all posts use the item_post layout
        View view = mInflater.inflate(R.layout.item_comment, parent, false);

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
        try {
            Comment comment = new Comment(mComments.get(position));
            // Set Post text
            TextView commentText = holder.itemView.findViewById(R.id.commentText);
            commentText.setText(comment.comment);

            // Set post author
            TextView commentAuthor = holder.itemView.findViewById(R.id.commentAuthor);
            commentAuthor.setText(comment.author);

            // Set post time
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(comment.time);
            SimpleDateFormat format;
            if (cal.getTime().getDate() == Calendar.getInstance().getTime().getDate())
                format = new SimpleDateFormat(todayFormat, Locale.getDefault());
            else
                format = new SimpleDateFormat(oldShortFormat, Locale.getDefault());
            TextView commentTime = holder.itemView.findViewById(R.id.commentTime);
            commentTime.setText(format.format(cal.getTime()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Bound ViewHolder for item " + position);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }
}