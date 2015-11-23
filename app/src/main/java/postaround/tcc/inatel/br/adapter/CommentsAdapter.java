package postaround.tcc.inatel.br.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import postaround.tcc.inatel.br.Utils.CircleImage;
import postaround.tcc.inatel.br.Utils.UserInformation;
import postaround.tcc.inatel.br.model.Comment;
import postaround.tcc.inatel.br.postaround.R;

/**
 * Created by Paulo on 13/11/2015.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Activity mActivity;
    private List<Comment> mComments;


    public CommentsAdapter(Activity activity, List<Comment> comments){
        mActivity = activity;
        mComments = comments;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private TextView comment;
        private TextView nome;

    public ViewHolder(View itemView) {
        super(itemView);
        view = itemView;

        comment = (TextView) view.findViewById(R.id.textView_novo_comentario_post_comentado);
        nome = (TextView) view.findViewById(R.id.textView_novo_nome_post_comentado);

    }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rows_comentario_post, parent, false);


        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = mComments.get(position);

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.rows_comentario_post, null);

        holder.comment.setText(comment.getText());
      //  Picasso.with(mActivity).load(("https://graph.facebook.com/" + comment.getUser_id() + "/picture?type=large")).transform(new CircleImage()).into(holder.userImage);



    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
