package postaround.tcc.inatel.br.model;

/**
 * Created by Paulo on 08/09/2015.
 */
public class User {

    private String id;
    private String token;
    private String api_key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }
}
