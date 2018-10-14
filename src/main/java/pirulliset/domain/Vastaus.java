
package pirulliset.domain;

public class Vastaus extends AbstractNamedObject {
    private String vastausteksti;
    private int kysymysId;
    private Boolean oikein;

    public Vastaus(int id, String vastausteksti, Boolean oikein) {
        super(id);
        this.vastausteksti = vastausteksti;
        this.oikein = oikein;
    }

    public Vastaus(int id, int kysymysId, String vastausteksti, Boolean oikein) {
        super(id);
        this.vastausteksti = vastausteksti;
        this.kysymysId = kysymysId;
        this.oikein = oikein;
    }

    public Boolean getOikein() {
        return oikein;
    }

    public String getVastausteksti() {
        return vastausteksti;
    }

    public int getKysymysId() {
        return kysymysId;
    }

}
