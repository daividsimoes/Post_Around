package postaround.tcc.inatel.br.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import java.util.ArrayList;

import postaround.tcc.inatel.br.adapter.PostAoRedorAdapter;
import postaround.tcc.inatel.br.model.LoginModel;
import postaround.tcc.inatel.br.model.PostAoRedor;
import postaround.tcc.inatel.br.postaround.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostAoRedorFragment extends Fragment {

    private ListView listView;
    private ArrayList<PostAoRedor> postList;
    private PostAoRedor post;


    public PostAoRedorFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_ao_redor, container, false);
        listView = (ListView) view.findViewById(R.id.listView_post_redor);

        postList = populaLista();

        listView.setAdapter(new PostAoRedorAdapter(this.getActivity(), postList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"Nome "+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }


    public ArrayList<PostAoRedor> populaLista(){
        ArrayList<PostAoRedor> lista = new ArrayList<PostAoRedor>();

        post = new PostAoRedor();
        post.setIdUsuario("875726655854018");
        post.setNomeUsuario("Daivid Simões");
        post.setTituloDescricao("Desenvolvimento Android");
        post.setComentarioDescricao("Como fazer um ListView personalizado?");
        lista.add(post);

        post = new PostAoRedor();
        post.setIdUsuario("865987463482201");
        post.setNomeUsuario("Paula Souza");
        post.setTituloDescricao("Provas");
        post.setComentarioDescricao("Resolução provas antigas de Sistema de Controles Dinâmicos.");
        lista.add(post);


        post = new PostAoRedor();
        post.setIdUsuario("110091962676326");
        post.setNomeUsuario("TCC Profile");
        post.setTituloDescricao("Como fazer o TCC");
        post.setComentarioDescricao("Retire todas suas dúvidas sobre o TCC aqui.");
        lista.add(post);


        return  lista;
    }

}
