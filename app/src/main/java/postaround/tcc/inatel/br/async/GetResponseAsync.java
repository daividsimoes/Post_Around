package postaround.tcc.inatel.br.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cloudinary.Cloudinary;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.AsyncTaskArguments;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.model.PostPostRes;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Carol on 13/09/2015.
 */
public class GetResponseAsync extends AsyncTask<AsyncTaskArguments, Void, String> {

    String API = "http://api-tccpostaround.rhcloud.com/api";
    private Activity context;
    private ProgressDialog mProgressDialog;
    private Boolean postCreated = false;

    public GetResponseAsync(Activity c) {
        this.context = c;

        mProgressDialog = new ProgressDialog(c);
        mProgressDialog.setIndeterminate(true);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog =ProgressDialog.show(context, "", "Criando o post...\nPor favor aguarde!");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected String doInBackground(AsyncTaskArguments... params) {

        Post post = params[0].getAsyncTaskPost();
        final String imagePath = params[0].getAsyncTaskImagePath();

        Map config = new HashMap();
        config.put("cloud_name", "dfnoff8kl");
        config.put("api_key", "423312899336841");
        config.put("api_secret", "PB7_3xXCf5Q-WURKEBpSf_IoiTM");
        //config.put("transformation", new Transformation().width(200).height(200).crop("limit"));

        Cloudinary cloudinary = new Cloudinary(config);

        try {
            Map result = null;
            String urlResult = null;
            if(imagePath != null) {
                result = cloudinary.uploader().upload(imagePath, config);
                urlResult = result.get("url").toString();
            }else {
                urlResult = " ";
            }
            post.setImage_url(urlResult);

            RestAdapter retrofit = new RestAdapter.Builder()
                        .setEndpoint(API)
                        .build();

            RestAPI restAPI = retrofit.create(RestAPI.class);

            restAPI.postPost(post, new Callback<PostPostRes>() {
                @Override
                public void success(PostPostRes post, Response response) {
                    Log.v("ASYNC SUCCESS:", "" + response.toString());
                    postCreated = true;
                    //File file = new File(imagePath);
                    //file.delete();

                    if (post != null) {
                        Log.v("res:", "" + post.getMessage());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.v("ASYNC ERROR : ", error.getMessage());
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(postCreated){
            context.finish();
        }else {
            Toast.makeText(context, "Problema ao tentar criar um Post.", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }
        }
}
