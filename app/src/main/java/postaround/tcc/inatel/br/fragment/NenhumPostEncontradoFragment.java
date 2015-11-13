package postaround.tcc.inatel.br.fragment;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.postaround.CriarPostActivity;
import postaround.tcc.inatel.br.postaround.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NenhumPostEncontradoFragment extends Fragment
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
        {

    String API = "http://api-tccpostaround.rhcloud.com/api";

    /* Google Play Services */
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 segundos
    private static int FATEST_INTERVAL = 5000; // 5 segundos
    private static int DISPLACEMENT = 10; // 10 metros

    private FloatingActionButton btnAddNewPost;

    public NenhumPostEncontradoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nenhum_post_encontrado, container, false);

        btnAddNewPost = (FloatingActionButton) view.findViewById(R.id.add_new_post);
        btnAddNewPost.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CriarPostActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void populaLista() {

        if (mLastLocation != null) {
            String longitude = String.valueOf(mLastLocation.getLongitude());
            String latitude = String.valueOf(mLastLocation.getLatitude());
            String maxDis = "100"; // TODO Get from Config

            RestAdapter retrofit = new RestAdapter.Builder()
                    .setEndpoint(API)
                    .build();

            RestAPI restAPI = retrofit.create(RestAPI.class);

            restAPI.getPosts(longitude, latitude, maxDis, new Callback<List<Post>>() {
                @Override
                public void success(List<Post> posts, Response response) {

                    if (posts.size() > 0) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new PostAoRedorFragment())
                                .commit();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("error", error.getMessage());
                }
            });
        } else {
            Log.e("error", "location not found!");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPlayServices();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        startLocationUpdates();
        populaLista();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("PostAoRedor: ", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    // Creating google api client object/
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    // Creating location request object
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    // Method to verify google play services on the device
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    // Starting the location updates
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    // Stopping location updates
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getActivity().getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();
    }

}
