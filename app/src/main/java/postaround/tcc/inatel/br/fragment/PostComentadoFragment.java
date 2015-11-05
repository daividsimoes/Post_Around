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

import postaround.tcc.inatel.br.Utils.CircleImage;
import postaround.tcc.inatel.br.postaround.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostComentadoFragment extends Fragment {

    private ImageView fotoPost;
    private ImageView fotoUsuario;
    private TextView nomeUsuario;
    private TextView comentarioPost;

    public PostComentadoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_comentado, container, false);

        fotoPost = (ImageView) view.findViewById(R.id.imageView_post_comentado_imagem);
        fotoUsuario = (ImageView) view.findViewById(R.id.imageView_post_comentado_foto);
        nomeUsuario = (TextView) view.findViewById(R.id.textView_post_comentado_nome);
        comentarioPost = (TextView)view.findViewById(R.id.textView_post_comentado_comentario);


        String id = "1586076827";
        String nome = "Carol Tenório";
        String comentario = "Como fazer um ListView personalizado?";

        fotoPost.setImageDrawable(getResources().getDrawable(R.drawable.ic_21x9_sample));

        Picasso.with(getActivity())
                .load("https://graph.facebook.com/" + id + "/picture?type=large")
                .transform(new CircleImage()).into(fotoUsuario);

        nomeUsuario.setText(nome);
        comentarioPost.setText(comentario);

        return view;
    }


}
