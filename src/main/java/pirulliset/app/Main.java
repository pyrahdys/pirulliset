package pirulliset.app;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pirulliset.dao.AiheDao;
import pirulliset.dao.KurssiDao;
import pirulliset.dao.KysymysDao;
import pirulliset.dao.VastausDao;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import pirulliset.database.Database;
import pirulliset.domain.Aihe;
import pirulliset.domain.Kurssi;
import pirulliset.domain.Kysymys;
import pirulliset.domain.Vastaus;
import spark.Spark;

public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database db = new Database("jdbc:sqlite:pirulliset.db");
        KysymysDao kysymys = new KysymysDao(db);
        KurssiDao kurssi = new KurssiDao(db);
        AiheDao aihe = new AiheDao(db);
        VastausDao vastaus = new VastausDao(db);

        Spark.get("/", (req, res) -> { // Pääsivun lataaminen
            HashMap map = new HashMap<>();
            map.put("kurssit", kurssi.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/kysymys/:id", (req, res) -> { // Kysymyssivun lataaminen
            HashMap map = new HashMap<>();
            Integer id = Integer.parseInt(req.params(":id"));
            Kysymys k = (Kysymys) kysymys.findOne(new Kysymys(id, ""));
            map.put("kysymykset", k);
            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/kurssi/lisaa", (req, res) -> { // Kurssin lisääminen
            String nimi = req.queryParams("kurssinimi").trim();
            if (nimi == null || nimi.isEmpty()) {return "<p>Kurssin nimi ei voi olla tyhjä.</p>";} // Virheilmoitus uudelle sivulle, jos syötetty arvo on tyhjä
            Kurssi lisattavaKurssi = new Kurssi(-1, nimi);
            if (kurssi.findOne(lisattavaKurssi) != null) {return "<p>Kurssi '" + nimi + "' on jo luotu.</p>";} // Virheilmoitus uudelle sivulle, jos kurssi on jo olemassa
            kurssi.saveOrUpdate(lisattavaKurssi);
            res.redirect("/"); // Ladataan pääsivu
            return "";
        });

        Spark.post("/aihe/lisaa", (req, res) -> {  // Aiheen lisääminen
            String nimi = req.queryParams("aihenimi").trim();
            if (nimi == null || nimi.isEmpty()) {return "<p>Aiheen nimi ei voi olla tyhjä.</p>";} // Virheilmoitus uudelle sivulle, jos syötetty arvo on tyhjä
            int kurssiId = Integer.parseInt(req.queryParams("kurssiId"));
            Aihe lisattavaAihe = new Aihe(-1, kurssiId, nimi);
            if (aihe.findOne(lisattavaAihe) != null) {return "<p>Aihe '" + nimi + "' on jo luotu.</p>";} // Virheilmoitus uudelle sivulle, jos aihe on jo olemassa
            aihe.saveOrUpdate(lisattavaAihe);
            res.redirect("/"); // Ladataan pääsivu
            return "";
        });

        Spark.post("/kysymys/lisaa", (req, res) -> { // Kysymyksen lisääminen
            String nimi = req.queryParams("kysymysteksti").trim();
            if (nimi == null || nimi.isEmpty()) {return "<p>Kysymysteksti ei voi olla tyhjä.</p>";} // Virheilmoitus uudelle sivulle, jos syötetty arvo on tyhjä
            int aiheId = Integer.parseInt(req.queryParams("aiheId"));
            Kysymys lisattavaKysymys = new Kysymys(-1, aiheId, nimi);
            if (kysymys.findOne(lisattavaKysymys) != null) {return "<p>Kysymys '" + nimi + "' on jo luotu.</p>";} // Virheilmoitus uudelle sivulle, jos kysymys on jo olemassa
            kysymys.saveOrUpdate(lisattavaKysymys);
            res.redirect("/"); // Ladataan pääsivu
            return "";
        });

        Spark.post("/vastaus/lisaa", (req, res) -> { // Vastauksen lisääminen
            String nimi = req.queryParams("vastausteksti").trim();
            if (nimi == null || nimi.isEmpty()) {return "<p>Vastausteksti ei voi olla tyhjä.</p>";} // Virheilmoitus uudelle sivulle, jos syötetty arvo on tyhjä
            int kysymysId = Integer.parseInt(req.queryParams("kysymysId"));
            Boolean oikein = !((req.queryParams("vastausOikein")) == null);
            Vastaus lisattavaVastaus = new Vastaus(-1, kysymysId, nimi, oikein);
            if (vastaus.findOne(lisattavaVastaus) != null) {return "<p>Vastaus '" + nimi + "' on jo luotu.</p>";} // Virheilmoitus uudelle sivulle, jos vastaus on jo olemassa
            vastaus.saveOrUpdate(lisattavaVastaus);
            res.redirect("/kysymys/" + kysymysId); // Ladataan pääsivu
            return "";
        });

        Spark.post("/kurssi/poista", (req, res) -> { // Kurssin poistaminen
            int id = Integer.parseInt(req.queryParams("kurssiId"));
            Kurssi poistettavaKurssi = new Kurssi(id, "");
            kurssi.delete(poistettavaKurssi);
            res.redirect("/"); // Ladataan pääsivu
            return "";
        });

        Spark.post("/aihe/poista", (req, res) -> { // Aiheen poistaminen
            int id = Integer.parseInt(req.queryParams("aiheId"));
            Aihe poistettavaAihe = new Aihe(id, "");
            aihe.delete(poistettavaAihe);
            res.redirect("/"); // Ladataan pääsivu
            return "";
        });

        Spark.post("/kysymys/poista", (req, res) -> { // Kysymyksen poistaminen
            int id = Integer.parseInt(req.queryParams("kysymysId"));
            Kysymys poistettavaKysymys = new Kysymys(id, "");
            kysymys.delete(poistettavaKysymys);
            res.redirect("/"); // Ladataan pääsivu
            return "";
        });

        Spark.post("/vastaus/poista", (req, res) -> { // Vastauksen poistaminen
            int id = Integer.parseInt(req.queryParams("vastausId"));
            int kysymysId = Integer.parseInt(req.queryParams("kysymysId"));
            Vastaus poistettavaVastaus = new Vastaus(id, "", false);
            vastaus.delete(poistettavaVastaus);
            res.redirect("/kysymys/" + kysymysId); // Ladataan kysymyssivu
            return "";
        });
    }
}
