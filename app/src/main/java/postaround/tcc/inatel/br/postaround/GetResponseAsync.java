package postaround.tcc.inatel.br.postaround;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
//import com.cloudinary.utils.ObjectUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import postaround.tcc.inatel.br.fragment.CriarPostActivityFragment;

/**
 * Created by Carol on 13/09/2015.
 */
public class GetResponseAsync extends AsyncTask<String, Void, String> {

    private Context context;

    private ProgressDialog mProgressDialog;
    int progress;

    public GetResponseAsync(Context c) {
        this.context = c;

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog =ProgressDialog.show(context, "", "Criando o post...");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected String doInBackground(String... params) {

        Map config = new HashMap();
        config.put("cloud_name", "dfnoff8kl"); //
        config.put("api_key", "423312899336841"); //
        config.put("api_secret", "PB7_3xXCf5Q-WURKEBpSf_IoiTM"); //
        config.put("transformation", new Transformation().width(200).height(200).crop("limit"));

        Cloudinary cloudinary = new Cloudinary(config);

        try {
            Map result = cloudinary.uploader().upload(params[0], config);
            Log.w("UPLOAD: ", params[0]);
            String urlResult = result.get("url").toString();

            return urlResult;
        }
        catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        mProgressDialog.dismiss();
    }
}
