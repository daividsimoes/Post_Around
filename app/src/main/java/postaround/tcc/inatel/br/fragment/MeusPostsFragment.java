package postaround.tcc.inatel.br.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.facebook.FacebookSdk;

import java.util.HashMap;
import java.util.List;

import postaround.tcc.inatel.br.Utils.LocationManager;
import postaround.tcc.inatel.br.adapter.MeuPostAdapter;
import postaround.tcc.inatel.br.adapter.PostAoRedorAdapter;
import postaround.tcc.inatel.br.interfaces.LocationObserver;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.Post;
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
public class MeusPostsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener, LocationObserver {

    private RecyclerView recyclerView;
    private View view;
    private Activity activity;
    private SwipeRefreshLayout swipeView;
    private RelativeLayout progressBar;

    private LocationManager locationManager;

    private RecyclerView.LayoutManager layoutManager;



    public MeusPostsFragment() {
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

        view = inflater.inflate(R.layout.fragment_meus_posts, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_meu_post);
        recyclerView.setHasFixedSize(true);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_meu_post);
        progressBar = (RelativeLayout) view.findViewById(R.id.loadingPanel_meu_post);

        view.setFitsSystemWindows(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        swipeView.setOnRefreshListener(this);

        return view;
    }

    private void populaLista() {
        HashMap<String, Double> location = locationManager.getLocation();

        if(location != null) {
            String longitude = String.valueOf(location.get("longitude"));
            String latitude = String.valueOf(location.get("latitude"));
            String maxDis = "100";

            RestAdapter retrofit = new RestAdapter.Builder()
                    .setEndpoint("http://api-tccpostaround.rhcloud.com/api")
                    .build();

            RestAPI restAPI = retrofit.create(RestAPI.class);

            restAPI.getPosts(longitude, latitude, maxDis, new Callback<List<Post>>() {
                @Override
                public void success(List<Post> posts, Response response) {
                    recyclerView.setAdapter(new MeuPostAdapter(activity, posts));
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
                //populaLista();
            }
        }, 1000);
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


/*
*         post = new MeuPost();
        post.setIdUsuario("875726655854018");
        post.setNomeUsuario("Daivid Simões");
        post.setTituloDescricao("Vaga Apto");
        post.setComentarioDescricao("Vaga para quarto individual em Campinas-SP");
        lista.add(post);*/
