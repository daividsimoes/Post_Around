package postaround.tcc.inatel.br.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import postaround.tcc.inatel.br.adapter.MeuPostAdapter;
import postaround.tcc.inatel.br.adapter.PostAoRedorAdapter;
import postaround.tcc.inatel.br.model.MeuPost;
import postaround.tcc.inatel.br.model.PostAoRedor;
import postaround.tcc.inatel.br.postaround.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeusPostsFragment extends Fragment {

   private ListView listView;
   private ArrayList<MeuPost> postList;
   private MeuPost post;

    public MeusPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meus_posts, container, false);

        listView = (ListView) view.findViewById(R.id.listView_meu_post);

        postList = populaLista();

        listView.setAdapter(new MeuPostAdapter(this.getActivity(), postList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Nome " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }


    public ArrayList<MeuPost> populaLista(){
        ArrayList<MeuPost> lista = new ArrayList<MeuPost>();

        post = new MeuPost();
        post.setIdUsuario("875726655854018");
        post.setNomeUsuario("Daivid Simões");
        post.setTituloDescricao("Desenvolvimento Android");
        post.setComentarioDescricao("Como fazer um ListView personalizado?");
        lista.add(post);

        post = new MeuPost();
        post.setIdUsuario("875726655854018");
        post.setNomeUsuario("Daivid Simões");
        post.setTituloDescricao("Provas");
        post.setComentarioDescricao("Resolução provas antigas de Sistema de Controles Dinâmicos.");
        lista.add(post);


        post = new MeuPost();
        post.setIdUsuario("875726655854018");
        post.setNomeUsuario("Daivid Simões");
        post.setTituloDescricao("Como fazer o TCC");
        post.setComentarioDescricao("Retire todas suas dúvidas sobre o TCC aqui.");
        lista.add(post);


        post = new MeuPost();
        post.setIdUsuario("875726655854018");
        post.setNomeUsuario("Daivid Simões");
        post.setTituloDescricao("Estágio");
        post.setComentarioDescricao("Está procurando vaga de estágio? Envie seu CV para post_around@hotmail.com");
        lista.add(post);


        post = new MeuPost();
        post.setIdUsuario("875726655854018");
        post.setNomeUsuario("Daivid Simões");
        post.setTituloDescricao("Vaga Apto");
        post.setComentarioDescricao("Vaga para quarto individual em Campinas-SP");
        lista.add(post);


        return  lista;
    }

}
