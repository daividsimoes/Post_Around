package postaround.tcc.inatel.br.postaround;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

public class ScrollingActivity extends AppCompatActivity {

    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private View mInclude;
    private ImageView mPostPicture;
    private PostAiTextView mDescription;
    private ImageView mUserPicture;
    private PostAiTextView mUserName;
    private PostAiEditText mComment;
    private FloatingActionButton mSendComment;
    private CommentAsync asyncTask;
    private String commentUserName;
    private String commentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);;

        mActivity = this;
        getUserInfo();
        mUserPicture = (ImageView) findViewById(R.id.imagemview_profile_picture_post_redor);
        mPostPicture = (ImageView) findViewById(R.id.imagemview_post_picture_post_redor);
        mSendComment = (FloatingActionButton) findViewById(R.id.send_comment);
        mComment = (PostAiEditText) findViewById(R.id.commentEditText);
        mUserName = (PostAiTextView) findViewById(R.id.userNameTextView);
        mInclude = findViewById(R.id.includeView);
        mDescription = (PostAiTextView) mInclude.findViewById(R.id.textview_conteudo_post_detalhado);

        Bundle extras = getIntent().getExtras();

        final String postId = extras.getString("post_id");
        String imageUrl = extras.getString("image_url");
        String description = extras.getString("description");
        String userName = extras.getString("user_name");
        String userId = extras.getString("user_id");

        mUserName.setText(userName);
        mDescription.setText(description);
        Picasso.with(this).load(("https://graph.facebook.com/" + userId + "/picture?type=large")).transform(new CircleImage()).into(mUserPicture);
        Picasso.with(this).load(imageUrl).into(mPostPicture);


        mRecyclerView = (RecyclerView) mInclude.findViewById(R.id.my_recycler_view_comments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

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

        mSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = mComment.getText().toString();
                if (description.equals(null) || description.equals("")) {
                    Toast.makeText(getApplication(), "Post deve possuir um comentário", Toast.LENGTH_LONG).show();
                } else {
                    Comment comment = new Comment();
                    comment.setPostid(postId);
                    comment.setComment(mComment.getText().toString());
                    comment.setUserid(commentUserId);
                    comment.setUsername(commentUserName);
                    asyncTask.execute(comment);
                }
            }
        });
    }
    private void getUserInfo() {
        SharedPreferences prefs = this.getSharedPreferences("loginpreferences", this.MODE_PRIVATE);
        commentUserId = prefs.getString("userid","");
        commentUserName = prefs.getString("username","");
    }
}
