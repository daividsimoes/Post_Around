package postaround.tcc.inatel.br.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import postaround.tcc.inatel.br.Utils.UserInformation;
import postaround.tcc.inatel.br.Utils.CircleImage;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.postaround.ComentarioPostActivity;
import postaround.tcc.inatel.br.postaround.R;


/**
 * Created by Daivid on 30/08/2015.
 */
public class PostAoRedorAdapter extends RecyclerView.Adapter<PostAoRedorAdapter.ViewHolder>{

    private static Context context;
    private List<Post> postAoRedorArrayList;

    private TextView tituloDescricao;
    private TextView comentarioDescricao;
    private TextView nomeUsuario;
    private ImageView fotoProfile;
    private ImageView fotoPost;

    public  PostAoRedorAdapter(Context context, List<Post> listaPostAoRedor){
        this.context = context;
        this. postAoRedorArrayList = listaPostAoRedor;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitulo;
        public TextView mDescricao;
        public TextView mUserName;
        public CardView cv;
        public ImageView fotoProfile;
        public ImageView mImagemPost;
        public Integer mPostTag;
        public View view;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            cv = (CardView) itemView.findViewById(R.id.cv);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ComentarioPostActivity.class);
                    intent.putExtra("post_id",cv.getTag().toString());
                    context.startActivity(intent);
                }
            });
            mTitulo = (TextView) cv.findViewById(R.id.post_titulo);
            mImagemPost = (ImageView) cv.findViewById(R.id.imageView_post_picture_post_redor);
            mDescricao = (TextView) cv.findViewById(R.id.post_descricao);
            mUserName = (TextView) cv.findViewById(R.id.post_nomeUsuario);
            fotoProfile = (ImageView) cv.findViewById(R.id.imagemview_profile_picture_post_redor);

        }
    }

    @Override
    public PostAoRedorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rows_post_ao_redor, parent, false);


        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(PostAoRedorAdapter.ViewHolder holder, int position) {
        Post post = postAoRedorArrayList.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.rows_post_ao_redor, null);

        if(!post.getImage_url().trim().equals("")) {
            Picasso.with(context).load(post.getImage_url()).fit().centerCrop().into(holder.mImagemPost);
        }


        Picasso.with(context).load(("https://graph.facebook.com/" + UserInformation.user_id + "/picture?type=large")).transform(new CircleImage()).into(holder.fotoProfile);

        holder.cv.setTag(post.get_id());
        holder.mDescricao.setText(post.getDescription());
        holder.mUserName.setText(UserInformation.user_name);

    }






    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return postAoRedorArrayList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
