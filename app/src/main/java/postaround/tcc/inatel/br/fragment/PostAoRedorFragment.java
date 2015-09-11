package postaround.tcc.inatel.br.fragment;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import postaround.tcc.inatel.br.adapter.PostAoRedorAdapter;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.LoginModel;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.model.PostAoRedor;
import postaround.tcc.inatel.br.postaround.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostAoRedorFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private ListView listView;
    private List<Post> postList;
    private View view;
    private Activity activity;
    private SwipeRefreshLayout swipeView;

    public PostAoRedorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this.getActivity();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_post_ao_redor, container, false);
        listView = (ListView) view.findViewById(R.id.listView_post_redor);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        //populaLista(); movido para OnConnected()

        swipeView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);

        return view;
    }

    private void populaLista() {

        HashMap<String, Double> location = getLocation();

        if(location != null) {
            String longitude = location.get("latitude").toString();
            String latitude = location.get("longitude").toString();


            //String longitude = "-45.70410132408142";
            //String latitude =  "-22.252638996602688";
            String maxDis = "3000";

            RestAdapter retrofit = new RestAdapter.Builder()
                    .setEndpoint("http://api-tccpostaround.rhcloud.com/api")
                    .build();

            RestAPI restAPI = retrofit.create(RestAPI.class);

            restAPI.getPosts(longitude, latitude, maxDis, new Callback<List<Post>>() {
                @Override
                public void success(List<Post> posts, Response response) {
                    if(posts.size() <= 0) {
                        Toast.makeText(getActivity(), "Nenhum post na sua localização!", Toast.LENGTH_LONG);
                    }
                    else {
                        listView.setAdapter(new PostAoRedorAdapter(activity, posts));
                        swipeView.setRefreshing(false);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("error", error.getMessage());
                }
            });
        }
        else
            Toast.makeText(getActivity(), "Falha ao receber localização!", Toast.LENGTH_LONG);
    }

    @Override
    public void onRefresh() {
        swipeView.setRefreshing(true);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                populaLista();
            }
        }, 3000);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(), "Nome " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
    }

    //*************************************************************************************************
    //                    MÉTODOS NECESSÁRIOS PARA PEGAR LOCALIZAÇÃO DO USUARIO                      //
    // ************************************************************************************************
    private HashMap<String, Double> getLocation() {

        HashMap<String, Double> location = new HashMap<String, Double>();

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

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

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(getActivity().getApplicationContext(),
                        "Falha ao receber localização.", Toast.LENGTH_LONG)
                        .show();
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

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.w("FAIL: ", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        //getLocation();
        populaLista();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }
}
