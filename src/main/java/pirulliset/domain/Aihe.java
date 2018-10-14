package pirulliset.domain;

import java.util.ArrayList;
import java.util.List;

public class Aihe extends AbstractNamedObject {

    private int kurssiId;
    private String nimi;
    private List kysymykset;
    

    public Aihe(int id, String nimi) {
        super(id);
        this.nimi = nimi;
        this.kysymykset = new ArrayList<>();
    }

    public Aihe(int id, int kurssiId, String nimi) {
        super(id);
        this.kurssiId = kurssiId;
        this.nimi = nimi;
        this.kysymykset = new ArrayList<>();
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public List getKysymykset() {
        return kysymykset;
    }

    public void lisaaKysymys(Kysymys kysymys) {
        this.kysymykset.add(kysymys);
    }

    public void setKysymykset(List kysymykset) {
        this.kysymykset = kysymykset;
    }

    public int getKurssiId() {
        return kurssiId;
    }
    
    
}
