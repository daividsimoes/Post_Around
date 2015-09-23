package postaround.tcc.inatel.br.interfaces;

/**
 * Created by Paulo on 21/09/2015.
 */
public interface LocationObservable {

    public void addObserver(LocationObserver o);
    public void removeObserver(LocationObserver o);
    public void notifyObserver();

}
