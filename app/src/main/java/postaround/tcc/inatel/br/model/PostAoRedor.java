package postaround.tcc.inatel.br.model;

/**
 * Created by Daivid on 30/08/2015.
 */
public class PostAoRedor {
    private String tituloDescricao;
    private String nomeUsuario;
    private String comentarioDescricao;
    private String idUsuario;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }





    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getComentarioDescricao() {
        return comentarioDescricao;
    }

    public void setComentarioDescricao(String comentarioDescricao) {
        this.comentarioDescricao = comentarioDescricao;
    }

    public String getTituloDescricao() {
        return tituloDescricao;
    }

    public void setTituloDescricao(String tituloDescricao) {
        this.tituloDescricao = tituloDescricao;
    }
}
