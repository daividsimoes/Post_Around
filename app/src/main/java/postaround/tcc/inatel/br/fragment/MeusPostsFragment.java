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
import android.widget.Toast;

import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.List;

import postaround.tcc.inatel.br.Utils.LocationManager;
import postaround.tcc.inatel.br.adapter.MeuPostAdapter;
import postaround.tcc.inatel.br.adapter.PostAoRedorAdapter;
import postaround.tcc.inatel.br.interfaces.LocationObserver;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.postaround.R;
import postaround.tcc.inatel.br.postaround.ScrollingActivity;
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

    public static SharedPreferences prefs;
    public static ArrayList<Post> meuPostList;
    public static String userID;

    public MeusPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        prefs = activity.getSharedPreferences("loginpreferences", activity.MODE_PRIVATE);
        userID = prefs.getString("userid", "");

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
        meuPostList = new ArrayList();
        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint("http://api-tccpostaround.rhcloud.com/api")
                .build();

        RestAPI restAPI = retrofit.create(RestAPI.class);

        restAPI.getPosts(userID, new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                for (int i = 0; i < posts.size(); i++) {
                    if (posts.get(i).getUser_id().equals(userID)) {
                        meuPostList.add(posts.get(i));
                    }
                }
                if (meuPostList.size() > 0) {
                    recyclerView.setAdapter(new MeuPostAdapter(activity, meuPostList));
                    progressBar.setVisibility(View.GONE);
                    swipeView.setRefreshing(false);
                    locationManager.getmGoogleApiClient().disconnect();
                } else {
                    if(getFragmentManager() != null){
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new NenhumPostEncontradoFragment())
                                .commit();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.getMessage());
            }
        });


    }

    @Override
    public void onRefresh() {
        swipeView.setRefreshing(true);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if (locationManager.getmGoogleApiClient() != null) {
                    locationManager.getmGoogleApiClient().connect();
                }*/
                populaLista();
            }
        }, 1000);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Intent intent = new Intent(getActivity(), ComentarioPostActivity.class);
        Intent intent = new Intent(getActivity(), ScrollingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        populaLista();

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
