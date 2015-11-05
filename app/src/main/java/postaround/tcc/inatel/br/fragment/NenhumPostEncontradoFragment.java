package postaround.tcc.inatel.br.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import postaround.tcc.inatel.br.postaround.CriarPostActivity;
import postaround.tcc.inatel.br.postaround.R;

public class NenhumPostEncontradoFragment extends Fragment {

    private FloatingActionButton btnAddNewPost;

    public static NenhumPostEncontradoFragment newInstance() {
        NenhumPostEncontradoFragment fragment = new NenhumPostEncontradoFragment();
        return fragment;
    }

    public NenhumPostEncontradoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
