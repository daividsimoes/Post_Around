package postaround.tcc.inatel.br.fragment;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import postaround.tcc.inatel.br.postaround.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostComentadoFragment extends Fragment {


    private TextView comentarioPost;
    private TextView nomeUsuario;
    private ImageView fotoPost;
    public PostComentadoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_post_comentado, container, false);

        comentarioPost = (TextView)view.findViewById(R.id.textView_novo_comentario_post_comentado);
        nomeUsuario = (TextView) view.findViewById(R.id.textView_novo_nome_post_comentado);
        fotoPost = (ImageView) view.findViewById(R.id.imageView_post_comentado_picture);


        return view;
    }


}
