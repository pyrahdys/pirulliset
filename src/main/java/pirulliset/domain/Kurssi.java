
package pirulliset.domain;

import java.util.ArrayList;
import java.util.List;

public class Kurssi extends AbstractNamedObject {
    private int id;
    private String nimi;
    private List aiheet;

    public Kurssi(int id, String nimi) {
        super(id);
        this.nimi = nimi;
        this.aiheet = new ArrayList<>();
    }

    public String getNimi() {
        return nimi;
    }

    public void lisaaAihe(Aihe aihe) {
        this.aiheet.add(aihe);
    }

    public void setAiheet(List aiheet) {
        this.aiheet = aiheet;
    }

    public List getAiheet() {
        return aiheet;
    }
    
    
}
