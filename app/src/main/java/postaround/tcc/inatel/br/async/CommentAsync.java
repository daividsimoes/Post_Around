package postaround.tcc.inatel.br.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.Comment;
import postaround.tcc.inatel.br.model.PostCommentRes;
import postaround.tcc.inatel.br.model.PostPostRes;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paulo on 23/11/2015.
 */
public class CommentAsync extends AsyncTask<Comment,Void,Boolean> {

    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    String API = "http://api-tccpostaround.rhcloud.com/api";


    public CommentAsync(Activity activity) {
        this.mActivity = activity;

        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setIndeterminate(true);
    }

    @Override
    protected Boolean doInBackground(Comment... params) {

        Comment comment = params[0];

        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint(API)
                .build();

        RestAPI restAPI = retrofit.create(RestAPI.class);


        restAPI.postComment(comment, new Callback<PostCommentRes>() {
            @Override
            public void success(PostCommentRes post, Response response) {
                Log.v("ASYNC SUCCESS:", "" + response.toString());
                mActivity.finish();
                //File file = new File(imagePath);
                //file.delete();

                if (post != null) {
                    //    Log.v("res:", "" + post.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v("ASYNC ERROR : ", error.getMessage());
                Toast.makeText(mActivity, "Problema ao tentar enviar comentário.", Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();
            }
        });


        return false;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mActivity, "", "Enviando comentário...\nPor favor aguarde!");
    }
}
