package postaround.tcc.inatel.br.postaround;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import postaround.tcc.inatel.br.Utils.PostAiTextView;
import postaround.tcc.inatel.br.adapter.CommentsAdapter;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.Comment;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComentarioPostActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String mPostId;
    private Activity mActivity;
    private ImageView mPostImage;
    private PostAiTextView mDescription;
    private String mPostImageUrl;
    private String mPostDescription;
    private ImageButton mImageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comentarios_layout);

        mActivity = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_comments);

        mPostImage = (ImageView) findViewById(R.id.postImage);
        mDescription = (PostAiTextView) findViewById(R.id.tv_description);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPostId = bundle.getString("post_id");
        mPostImageUrl = bundle.getString("image_url");
        mPostDescription = bundle.getString("description");

        mImageButtonBack = (ImageButton) findViewById(R.id.back_button);

        mImageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(!mPostImageUrl.trim().equals("")) {
            Picasso.with(this).load(mPostImageUrl).into(mPostImage);
        }
        mDescription.setText(mPostDescription);

        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint("http://api-tccpostaround.rhcloud.com/api")
                .build();

        RestAPI restAPI = retrofit.create(RestAPI.class);
        if(mPostId != null) {
            restAPI.getComments(mPostId, new Callback<List<Comment>>() {
                @Override
                public void success(List<Comment> comments, Response response) {
                    Log.e("tag", response.toString());
                    mRecyclerView.setAdapter(new CommentsAdapter(mActivity, comments));
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("erro", "erro");
                }
            });
        }
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
