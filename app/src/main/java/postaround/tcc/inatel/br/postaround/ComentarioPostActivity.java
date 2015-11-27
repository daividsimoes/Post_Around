package postaround.tcc.inatel.br.postaround;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import postaround.tcc.inatel.br.Utils.CircleImage;
import postaround.tcc.inatel.br.Utils.PostAiEditText;
import postaround.tcc.inatel.br.Utils.PostAiTextView;
import postaround.tcc.inatel.br.Utils.UserInformation;
import postaround.tcc.inatel.br.adapter.CommentsAdapter;
import postaround.tcc.inatel.br.async.CommentAsync;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.Comment;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComentarioPostActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Activity mActivity;
    private ImageButton mImageButtonBack;
    private FloatingActionButton sendButton;
    private CommentAsync asyncTask;
    private PostAiEditText mComment;
    private ImageView mUserPicture;
    private PostAiTextView mUserName;
    private PostAiTextView mDescription;
    private String postId;
    private String postDescription;
    private String userName;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comentarios_layout);

        mActivity = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_comments);
        mDescription = (PostAiTextView) findViewById(R.id.tv_description);
        mUserName = (PostAiTextView) findViewById(R.id.userNameTextView);
        mUserPicture = (ImageView) findViewById(R.id.imagemview_profile_picture_post_redor);

        mComment = (PostAiEditText) findViewById(R.id.set_comment);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        postId = bundle.getString("post_id");
        postDescription = bundle.getString("description");
        userName = bundle.getString("user_name");
        userId = bundle.getString("user_id");

        mImageButtonBack = (ImageButton) findViewById(R.id.back_button);

        sendButton = (FloatingActionButton) findViewById(R.id.send_comment);

        mDescription.setText(postDescription);
        mUserName.setText(userName);
        Picasso.with(this).load(("https://graph.facebook.com/" + userId + "/picture?type=large")).transform(new CircleImage()).into(mUserPicture);

        mImageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint("http://api-tccpostaround.rhcloud.com/api")
                .build();

        RestAPI restAPI = retrofit.create(RestAPI.class);
        if(postId != null) {
            restAPI.getComments(postId, new Callback<List<Comment>>() {
                @Override
                public void success(List<Comment> comments, Response response) {
                    Log.e("tag", response.getReason().toString());
                    mRecyclerView.setAdapter(new CommentsAdapter(mActivity, comments));
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("erro", "erro");
                }
            });
        }

        asyncTask = new CommentAsync(this);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setPostid(postId);
                comment.setComment(mComment.getText().toString());
                comment.setUserid(UserInformation.user_id);
                asyncTask.execute(comment);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comentario_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
