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

import postaround.tcc.inatel.br.Utils.CircleImage;
import postaround.tcc.inatel.br.model.MeuPost;
import postaround.tcc.inatel.br.postaround.R;

/**
 * Created by Daivid on 18/09/2015.
 */
public class ComentarioPostAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MeuPost> meuPostArrayList;


    private ImageView fotoUsuario;

    public ComentarioPostAdapter(Context context, ArrayList<MeuPost> listaPostAoRedor){
        this.context = context;
        this. meuPostArrayList = listaPostAoRedor;
    }

    public ComentarioPostAdapter(){

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
        View view =  inflater.inflate(R.layout.rows_comentario_post, null);


        fotoUsuario = (ImageView) view.findViewById(R.id.imageView_post_picture_meu_post);

        Picasso.with(context).load("https://graph.facebook.com/" + post.getIdUsuario() + "/picture?type=large").transform(new CircleImage()).into(fotoUsuario);

        return view;
    }
}
