package postaround.tcc.inatel.br.interfaces;

import java.util.List;

import postaround.tcc.inatel.br.model.Comment;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.model.PostCommentRes;
import postaround.tcc.inatel.br.model.PostPostRes;
import postaround.tcc.inatel.br.model.PostUserRes;
import postaround.tcc.inatel.br.model.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Paulo on 07/09/2015.
 */
public interface RestAPI {

    @GET("/posts/{longitude}/{latitude}/{maxDis}")
    public void getPosts(@Path("longitude") String longitude, @Path("latitude") String latitude,
                         @Path("maxDis") String maxDis, Callback<List<Post>> response);

    @GET("/posts/{userid}")
    public void getPosts(@Path("userid") String userid, Callback<List<Post>> response);

    @POST("/login/")
    public void postUser(@Body User user, Callback<PostUserRes> response);

    @POST("/posts/")
    public void postPost(@Body Post post, Callback<PostPostRes> response);

    @GET("/comments/{postid}")
    public void getComments(@Path("postid") String postid, Callback<List<Comment>> response);

    @POST("/comments/")
    public void postComment(@Body Comment comment, Callback<PostCommentRes> response);

}