package postaround.tcc.inatel.br.interfaces;

import java.util.List;

import postaround.tcc.inatel.br.model.Post;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Paulo on 07/09/2015.
 */
public interface RestAPI {

    @GET("/posts/{longitude}/{latitude}/{maxDis}")
    public void getPosts(@Path("longitude") String longitude, @Path("latitude") String latitude,
                         @Path("maxDis") String maxDis, Callback<List<Post>> response);

}