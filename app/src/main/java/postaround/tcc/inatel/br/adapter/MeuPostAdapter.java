package postaround.tcc.inatel.br.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import postaround.tcc.inatel.br.Utils.UserInformation;
import postaround.tcc.inatel.br.Utils.CircleImage;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.postaround.ComentarioPostActivity;
import postaround.tcc.inatel.br.postaround.R;



/**
 * Created by Daivid on 01/09/2015.
 */
public class MeuPostAdapter extends RecyclerView.Adapter<MeuPostAdapter.ViewHolder>{

    public Context context;
    public List<Post> meuPostArrayList;

    private Post mPost;

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
        public TextView hiddenTextview;


        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_meu_post);
            mTitulo = (TextView) cv.findViewById(R.id.meu_post_titulo);
            mImagemPost = (ImageView) cv.findViewById(R.id.imageView_post_picture_meu_post);
            mDescricao = (TextView) cv.findViewById(R.id.meu_post_descricao);
            mUserName = (TextView) cv.findViewById(R.id.meu_post_nomeUsuario);
            fotoProfile = (ImageView) cv.findViewById(R.id.imagemview_profile_picture_meu_post);
            hiddenTextview = (TextView) cv.findViewById(R.id.hiddentext_meu_post);
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
    public void onBindViewHolder(final MeuPostAdapter.ViewHolder holder, int position) {

        mPost = meuPostArrayList.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.rows_meu_post, null);

            if (!mPost.getImage_url().trim().equals("")) {
                Picasso.with(context).load(mPost.getImage_url()).fit().centerCrop().into(holder.mImagemPost);
            }else {
                holder.mImagemPost.setVisibility(View.GONE);
            }
            holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //criar esse textview escondido
                //pra armazenar o id do post
                Intent intent = createIntent(holder.hiddenTextview.getText().toString());
                context.startActivity(intent);
            }
        });
            Picasso.with(context).load(("https://graph.facebook.com/" + mPost.getUser_id() + "/picture?type=large")).transform(new CircleImage()).into(holder.fotoProfile);

            holder.hiddenTextview.setText(mPost.get_id());
            holder.hiddenTextview.setVisibility(View.INVISIBLE);
            holder.mDescricao.setText(mPost.getDescription());
            holder.mUserName.setText(mPost.getUser_name());
    }

    private Intent createIntent(String hiddenText) {
        Intent intent = new Intent(context, ComentarioPostActivity.class);
        intent.putExtra("post_id",hiddenText);
        intent.putExtra("image_url", mPost.getImage_url());
        intent.putExtra("description", mPost.getDescription());

        return intent;
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
