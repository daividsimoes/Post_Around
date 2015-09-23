package postaround.tcc.inatel.br.fragment;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import postaround.tcc.inatel.br.Util.LocationManager;
import postaround.tcc.inatel.br.adapter.PostAoRedorAdapter;
import postaround.tcc.inatel.br.interfaces.LocationObserver;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.LoginModel;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.model.PostAoRedor;

import postaround.tcc.inatel.br.postaround.ComentarioPostActivity;
import postaround.tcc.inatel.br.postaround.CriarPostActivity;
import postaround.tcc.inatel.br.postaround.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostAoRedorFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener, LocationObserver{

    private ListView listView;
    private View view;
    private Activity activity;
    private SwipeRefreshLayout swipeView;
    private ImageButton button;
    private RelativeLayout progressBar;

    private LocationManager locationManager;



    public PostAoRedorFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        locationManager = new LocationManager(getActivity());
        locationManager.addObserver(this);
        locationManager.buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_post_ao_redor, container, false);
        listView = (ListView) view.findViewById(R.id.listView_post_redor);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        button = (ImageButton) view.findViewById(R.id.button_add_post);
        progressBar = (RelativeLayout) view.findViewById(R.id.loadingPanel);



        swipeView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CriarPostActivity.class);
                startActivityForResult(intent, 0);
            }
        });


        return view;
    }

    private void populaLista() {
        HashMap<String, Double> location = locationManager.getLocation();

        if(location != null) {
            String longitude = String.valueOf(location.get("longitude"));
            String latitude = String.valueOf(location.get("latitude"));
            String maxDis = "200";

            RestAdapter retrofit = new RestAdapter.Builder()
                    .setEndpoint("http://api-tccpostaround.rhcloud.com/api")
                    .build();

            RestAPI restAPI = retrofit.create(RestAPI.class);

    restAPI.getPosts(longitude, latitude, maxDis, new Callback<List<Post>>() {
        @Override
        public void success(List<Post> posts, Response response) {

            listView.setAdapter(new PostAoRedorAdapter(activity, posts));
            progressBar.setVisibility(View.GONE);
            swipeView.setRefreshing(false);
            locationManager.getmGoogleApiClient().disconnect();
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e("error", error.getMessage());
        }
        });

        }
    }

    @Override
    public void onRefresh() {
        swipeView.setRefreshing(true);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (locationManager.getmGoogleApiClient() != null) {
                    locationManager.getmGoogleApiClient().connect();
                }
                populaLista();
            }
        }, 3000);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), ComentarioPostActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (locationManager.getmGoogleApiClient() != null) {
            locationManager.getmGoogleApiClient().connect();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        locationManager.checkPlayServices();
        progressBar.setVisibility(View.VISIBLE);
        if(locationManager.getLocation() != null){
            populaLista();
        }
    }
    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void update() {
        populaLista();

    }
}

