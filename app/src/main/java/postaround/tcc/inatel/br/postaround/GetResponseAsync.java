package postaround.tcc.inatel.br.postaround;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

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

/**
 * Created by Carol on 13/09/2015.
 */
public class GetResponseAsync extends AsyncTask<String, Void, Boolean> {

    Context context;

    public GetResponseAsync(Context c) {
        this.context = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected Boolean doInBackground(String... params) {

        Map config = new HashMap();
        config.put("cloud_name", "dfnoff8kl"); //
        config.put("api_key", "423312899336841"); //
        config.put("api_secret", "PB7_3xXCf5Q-WURKEBpSf_IoiTM"); //
        config.put("transformation", new Transformation().width(200).height(200).crop("limit"));

        Cloudinary cloudinary = new Cloudinary(config);

        try {
            cloudinary.uploader().upload(params[0], config);
            Log.w("UPLOAD: ", params[0]);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }
}
