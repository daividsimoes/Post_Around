package postaround.tcc.inatel.br.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.List;

import postaround.tcc.inatel.br.adapter.PostAoRedorAdapter;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.LoginModel;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.model.PostAoRedor;
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
        AdapterView.OnItemClickListener{

    private ListView listView;
    private List<Post> postList;
    private View view;
    private Activity activity;
    private SwipeRefreshLayout swipeView;
    private ImageButton button;


    public PostAoRedorFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_post_ao_redor, container, false);
        listView = (ListView) view.findViewById(R.id.listView_post_redor);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        button = (ImageButton) view.findViewById(R.id.button_add_post);

        populaLista();

        swipeView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CriarPostActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void populaLista() {

        String longitude ="-45.70410132408142";
        String latitude = "-22.252638996602688";
        String maxDis = "3000";

        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint("http://api-tccpostaround.rhcloud.com/api")
                .build();

        RestAPI restAPI = retrofit.create(RestAPI.class);

        restAPI.getPosts(longitude, latitude, maxDis, new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {

                listView.setAdapter(new PostAoRedorAdapter(activity, posts));
                swipeView.setRefreshing(false);
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
                populaLista();
            }
        }, 3000);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(), "Nome " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
    }
}
