package postaround.tcc.inatel.br.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paulo on 07/09/2015.
 */
public class Loc {
    private String type;
    private List<Double> coordinates = new ArrayList<Double>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
