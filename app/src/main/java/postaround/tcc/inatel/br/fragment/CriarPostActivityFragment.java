package postaround.tcc.inatel.br.fragment;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import postaround.tcc.inatel.br.Util.UserInformation;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.Loc;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.model.PostPostRes;
import postaround.tcc.inatel.br.postaround.CriarPostActivity;
import postaround.tcc.inatel.br.postaround.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class CriarPostActivityFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private EditText titulo;
    private EditText descricao;
    private ImageButton botao;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;


    public CriarPostActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_criar_post, container, false);

        titulo = (EditText) v.findViewById(R.id.editText_criar_descricao_post);
        descricao = (EditText) v.findViewById(R.id.editText_criar_comentario_post);
        botao = (ImageButton) v.findViewById(R.id.button_add_post);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titulo.getText().toString();
                String description = descricao.getText().toString();
                HashMap<String, Double> location = getLocation();
                Double longitude;
                Double latitude;
                List<Double> list = new ArrayList<Double>();
                if (location != null) {
                    list.add(location.get("longitude"));
                    list.add(location.get("latitude"));
                }

                Post post = new Post();
                Loc loc = new Loc();

                post.setTitle(title);
                post.setDescription(description);
                post.setUser_id(UserInformation.user_id);
                loc.setCoordinates(list);
                loc.setType("Point");
                post.setLoc(loc);

                RestAdapter retrofit = new RestAdapter.Builder()
                        .setEndpoint("http://api-tccpostaround.rhcloud.com/api")
                        .build();

                RestAPI restAPI = retrofit.create(RestAPI.class);

                restAPI.postPost(post, new Callback<PostPostRes>() {
                    @Override
                    public void success(PostPostRes post, Response response) {
                        Log.v("Resposta : ", response.getBody().toString());
                        if (post != null) {
                            Log.v("res:", "" + post.getMessage());
                            closeActivity();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.v("Erro : ", error.getMessage());
                    }
                });


            }
        });


        return v;

    }

    private void closeActivity() {

        ((CriarPostActivity)getActivity()).closeActivity();


    }


    private HashMap<String, Double> getLocation() {
        HashMap<String, Double> location = new HashMap<String, Double>();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            location.put("latitude", latitude);
            location.put("longitude", longitude);
            return location;
        } else {
            Log.w("FAIL: ", "(Couldn't get the location. Make sure location is enabled on the device)");
            return null;
        }
    }

    /**      * Creating google api client object      * */
    /**      * Method to verify google play services on the device      * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Falha ao receber localização.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }
    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    /**      * Google api callback methods      */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.w("FAIL: ", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        //getLocation();

    }
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }
}

