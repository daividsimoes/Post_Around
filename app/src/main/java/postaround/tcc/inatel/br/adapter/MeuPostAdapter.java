package postaround.tcc.inatel.br.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import postaround.tcc.inatel.br.postaround.R;



/**
 * Created by Daivid on 01/09/2015.
 */
public class MeuPostAdapter extends RecyclerView.Adapter<MeuPostAdapter.ViewHolder>{

    private Context context;
    private List<Post> meuPostArrayList;

    private String userID;

    public  MeuPostAdapter(Context context, List<Post> listaMeuPost){
        this.context = context;
        this.meuPostArrayList = listaMeuPost;
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

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_meu_post);
            mTitulo = (TextView) cv.findViewById(R.id.meu_post_titulo);
            mImagemPost = (ImageView) cv.findViewById(R.id.imageView_post_picture_meu_post);
            mDescricao = (TextView) cv.findViewById(R.id.meu_post_descricao);
            mUserName = (TextView) cv.findViewById(R.id.meu_post_nomeUsuario);
            fotoProfile = (ImageView) cv.findViewById(R.id.imagemview_profile_picture_meu_post);
        }
    }

    @Override
    public MeuPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rows_meu_post, parent, false);


        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(MeuPostAdapter.ViewHolder holder, int position) {

        Post post = meuPostArrayList.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.rows_meu_post, null);

            if(!post.getImage_url().trim().equals("")) {
                Picasso.with(context).load(post.getImage_url()).fit().centerCrop().into(holder.mImagemPost);
            }
            Picasso.with(context).load(("https://graph.facebook.com/" + post.getUser_id() + "/picture?type=large")).transform(new CircleImage()).into(holder.fotoProfile);
            holder.cv.setTag(post.get_id());
            holder.mDescricao.setText(post.getDescription());
            holder.mUserName.setText(post.getUser_name());


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return meuPostArrayList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
