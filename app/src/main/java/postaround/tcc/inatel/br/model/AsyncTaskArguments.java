package postaround.tcc.inatel.br.model;

/**
 * Created by Carol on 06/11/2015.
 */
public class AsyncTaskArguments {

    private Post atPost;
    private String atImagePath;

    public AsyncTaskArguments(String imagePath, Post post) {
        this.atImagePath = imagePath;
        this.atPost = post;
    }

    public Post getAsyncTaskPost() {
        return atPost;
    }

    public void setAsyncTaskPost(Post post) {
        this.atPost = post;
    }

    public String getAsyncTaskImagePath() {
        return atImagePath;
    }

    public void setAsyncTaskImagePath(String imagePath) {
        this.atImagePath = imagePath;
    }
}
