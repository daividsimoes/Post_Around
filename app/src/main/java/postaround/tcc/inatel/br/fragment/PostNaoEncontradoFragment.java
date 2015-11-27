package postaround.tcc.inatel.br.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.List;

import postaround.tcc.inatel.br.interfaces.LocationObserver;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.postaround.R;
import postaround.tcc.inatel.br.postaround.ScrollingActivity;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostNaoEncontradoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    private SwipeRefreshLayout swipeView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public static SharedPreferences prefs;
    public static ArrayList<Post> meuPostList;
    public static String userID;
    private Activity activity;
    public PostNaoEncontradoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        prefs = activity.getSharedPreferences("loginpreferences", activity.MODE_PRIVATE);
        userID = prefs.getString("userid", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_nao_encontrado, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view__meu_post_nao_encontrado);
        recyclerView.setHasFixedSize(true);
        view.setFitsSystemWindows(true);
        swipeView = (SwipeRefreshLayout)view.findViewById(R.id.swipe_meu_post_nao_encontrado);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(true);
                        populaLista();
                    }
                }, 10);
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
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new MeusPostsFragment())
                            .commit();
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
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
                populaLista();
            }
        }, 10);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}