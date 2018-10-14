
package pirulliset.app;

public class Palvelinviesti {
    String teksti;

    public Palvelinviesti(String teksti) {
        this.teksti = teksti;
    }

    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }

    public String getTeksti() {
        return teksti;
    }
}
