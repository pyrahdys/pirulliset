
package pirulliset.domain;

import java.util.ArrayList;
import java.util.List;

public class Kysymys extends AbstractNamedObject {
    
    private int aiheId;
    private String kysymysteksti;
    private List vastaukset;
    private Boolean sisaltaaOikeanVastauksen;

    public Kysymys(int id, String kysymysteksti) {
        super(id);
        this.kysymysteksti = kysymysteksti;
        this.vastaukset = new ArrayList<>();
    }

    public Kysymys(int id, int aiheId, String kysymysteksti) {
        super(id);
        this.aiheId = aiheId;
        this.kysymysteksti = kysymysteksti;
        this.vastaukset = new ArrayList<>();
    }

    public String getKysymysteksti() {
        return kysymysteksti;
    }

    public void setVastaukset(List vastaukset) {
        this.vastaukset = vastaukset;
    }

    public List getVastaukset() {
        return vastaukset;
    }

    public void lisaaVastaus(Vastaus vastaus) {
        this.vastaukset.add(vastaukset);
    }

    public int getAiheId() {
        return aiheId;
    }
    
    public Boolean getSisaltaaOikeanVastauksen() {
        for (Object v : this.vastaukset) {
            Vastaus vastaus = (Vastaus) v;
            return vastaus.getOikein();
        }
        return false;
    }
}
