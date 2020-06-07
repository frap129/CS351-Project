package kettering.cs351.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LaunchActivity extends AppCompatActivity implements PostRepo.OnCompleteCallback {
    private final String TAG = "LaunchActivity";
    private FirebaseAuth mAuth;
    private PostRepo mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Log.d(TAG, "onStart: User not signed in");
            signInAnonymously();
        } else {
            Log.d(TAG, "onStart: User already signed in");
        }

        // Start the repo to fetch data, pass callback so we know when its complete
        mRepo = new PostRepo((PostRepo.OnCompleteCallback) this);
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    public void FirestoreGetComplete() {
        final Context context = this;

        // Check author name before going to posts
        final SharedPreferences prefs = getSharedPreferences(getString(R.string.author), Context.MODE_PRIVATE);
        if (prefs.getString(getString(R.string.author), "").isEmpty()) {
            findViewById(R.id.loading).setVisibility(View.GONE);
            findViewById(R.id.name).setVisibility(View.VISIBLE);
            final TextInputLayout nameInput = findViewById(R.id.nameInput);
            nameInput.setEndIconDrawable(R.drawable.send);
            nameInput.setEndIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = nameInput.getEditText().getText().toString();
                    if (!name.isEmpty()) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString(getString(R.string.author), name);
                        edit.apply();
                        // Now that repo has fetched all posts, start the post list activity
                        Intent intent = new Intent(context, PostListActivity.class);
                        intent.putExtra("posts", mRepo.posts);
                        startActivity(intent);
                    }
                }
            });
        } else {
            // Now that repo has fetched all posts, start the post list activity
            Intent intent = new Intent(context, PostListActivity.class);
            intent.putExtra("posts", mRepo.posts);
            startActivity(intent);
        }

    }
}
