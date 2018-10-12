
package pirulliset.domain;

public class Kysymys {
    
    String kurssi;
    String aihe;
    String kysymysteksti;

    public Kysymys(String kurssi, String aihe, String kysymysteksti) {
        this.kurssi = kurssi;
        this.aihe = aihe;
        this.kysymysteksti = kysymysteksti;
    }

    public String getAihe() {
        return aihe;
    }

    public String getKurssi() {
        return kurssi;
    }

    public String getKysymysteksti() {
        return kysymysteksti;
    }
}
