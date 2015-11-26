package postaround.tcc.inatel.br.postaround;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import postaround.tcc.inatel.br.Utils.CircleImage;
import postaround.tcc.inatel.br.Utils.PostAiEditText;
import postaround.tcc.inatel.br.Utils.UserInformation;
import postaround.tcc.inatel.br.async.CommentAsync;
import postaround.tcc.inatel.br.model.Comment;

public class ScrollingActivity extends AppCompatActivity {

    private CommentAsync asyncTask;
    private FloatingActionButton send_comment;
    private PostAiEditText mComment;
    private ImageView fotoProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //toolBarLayout.setTitle("Texto Aqui!");

        fotoProfile = (ImageView) findViewById(R.id.imagemview_profile_picture_post_redor);
        send_comment = (FloatingActionButton) findViewById(R.id.send_comment);
        mComment = (PostAiEditText) findViewById(R.id.commentEditText);

        Bundle extras = getIntent().getExtras();

        final String postId = extras.getString("post_id");
        String image_url = extras.getString("image_url");
        String description = extras.getString("post_id");
        String user_name = extras.getString("user_name");
        String user_id = extras.getString("user_id");

        ImageView fotoProfile = (ImageView) findViewById(R.id.imagemview_profile_picture_post_redor);
        Picasso.with(this).load(("https://graph.facebook.com/" + user_id + "/picture?type=large")).transform(new CircleImage()).into(fotoProfile);

        asyncTask = new CommentAsync(this);

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setPostid(postId);
                comment.setComment(mComment.getText().toString());
                comment.setUserid(UserInformation.user_id);
                asyncTask.execute(comment);
            }
        });


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
