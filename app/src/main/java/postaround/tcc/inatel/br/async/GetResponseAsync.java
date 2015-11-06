package postaround.tcc.inatel.br.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
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

    private Context context;

    private ProgressDialog mProgressDialog;
    int progress;

    public GetResponseAsync(Context c) {
        this.context = c;

        // mProgressDialog = new ProgressDialog(context);
        // mProgressDialog.setIndeterminate(true);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // mProgressDialog =ProgressDialog.show(context, "", "Criando o post...");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected String doInBackground(AsyncTaskArguments... params) {

        Post post = params[0].getAsyncTaskPost();
        String imagePath = params[0].getAsyncTaskImagePath();

        Map config = new HashMap();
        config.put("cloud_name", "dfnoff8kl");
        config.put("api_key", "423312899336841");
        config.put("api_secret", "PB7_3xXCf5Q-WURKEBpSf_IoiTM");
        //config.put("transformation", new Transformation().width(200).height(200).crop("limit"));

        Cloudinary cloudinary = new Cloudinary(config);

        try {
            Map result = cloudinary.uploader().upload(imagePath, config);
            String urlResult = result.get("url").toString();
            Boolean error = false;

            RestAdapter retrofit = new RestAdapter.Builder()
                        .setEndpoint("http://api-tccpostaround.rhcloud.com/api")
                        .build();

                RestAPI restAPI = retrofit.create(RestAPI.class);

                restAPI.postPost(post, new Callback<PostPostRes>() {
                    @Override
                    public void success(PostPostRes post, Response response) {
                        if (post != null) {
                            Log.v("res:", "" + post.getMessage());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.v("Erro : ", error.getMessage());
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

        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        // mProgressDialog.dismiss();
    }
}
