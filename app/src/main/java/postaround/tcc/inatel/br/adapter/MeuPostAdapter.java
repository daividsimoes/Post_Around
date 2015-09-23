package postaround.tcc.inatel.br.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import postaround.tcc.inatel.br.model.MeuPost;
import postaround.tcc.inatel.br.postaround.R;
import postaround.tcc.inatel.br.Utils.CircleImage;

/**
 * Created by Daivid on 01/09/2015.
 */
public class MeuPostAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MeuPost> meuPostArrayList;

    private TextView tituloDescricao;
    private TextView comentarioDescricao;
    private TextView nomeUsuario;
    private ImageView fotoProfile;
    private ImageView fotoPost;

    public  MeuPostAdapter(Context context, ArrayList<MeuPost> listaPostAoRedor){
        this.context = context;
        this. meuPostArrayList = listaPostAoRedor;
    }

    @Override
    public int getCount() {
        return meuPostArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return meuPostArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MeuPost post = meuPostArrayList.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.rows_meu_post, null);

        tituloDescricao = (TextView) view.findViewById(R.id.textView_descricao_meu_post);
        comentarioDescricao = (TextView) view.findViewById(R.id.textView_comentario_meu_post);
        nomeUsuario = (TextView) view.findViewById(R.id.textView_nome_usuario_meu_post);
        fotoProfile = (ImageView) view.findViewById(R.id.imagemview_profile_picture_meu_post);
        fotoPost = (ImageView) view.findViewById(R.id.imageView_post_picture_meu_post);

        tituloDescricao.setText(post.getTituloDescricao());
        comentarioDescricao.setText(post.getComentarioDescricao());

        nomeUsuario.setText(post.getNomeUsuario());

        Picasso.with(context).load(R.drawable.tcc).into(fotoPost);
        Picasso.with(context).load("https://graph.facebook.com/" + post.getIdUsuario() + "/picture?type=large").transform(new CircleImage()).into(fotoProfile);
        return view;
    }
}
