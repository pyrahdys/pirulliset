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
        ////////////////////////////////////////////////
        Spark.get("*", (req, res) -> {
            return "Hei maailma!";
        });
        ////////////////////////////////////////////////
        /*
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database db = new Database("jdbc:sqlite:pirulliset.db");
        KysymysDao kysymys = new KysymysDao(db);
        KurssiDao kurssi = new KurssiDao(db);
        AiheDao aihe = new AiheDao(db);
        VastausDao vastaus = new VastausDao(db);
        Palvelinviesti viesti = new Palvelinviesti("");

        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kurssit", kurssi.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/kysymys/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer id = Integer.parseInt(req.params(":id"));
            Kysymys k = (Kysymys) kysymys.findOne(new Kysymys(id, ""));
            map.put("kysymykset", k);
            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/kurssi/lisaa", (req, res) -> {
            String nimi = req.queryParams("kurssinimi");
            if (nimi == null || nimi.isEmpty()) {
                return "<p>Kurssin nimi ei voi olla tyhjä.</p>";
            };
            Kurssi lisattavaKurssi = new Kurssi(-1, nimi);
            kurssi.saveOrUpdate(lisattavaKurssi);
            res.redirect("/");
            return "";
        });

        Spark.post("/aihe/lisaa", (req, res) -> {
            String nimi = req.queryParams("aihenimi");
            if (nimi == null || nimi.isEmpty()) {
                return "<p>Aiheen nimi ei voi olla tyhjä.</p>";
            };
            int kurssiId = Integer.parseInt(req.queryParams("kurssiId"));
            Aihe lisattavaAihe = new Aihe(-1, kurssiId, nimi);
            aihe.saveOrUpdate(lisattavaAihe);
            res.redirect("/");
            return "";
        });

        Spark.post("/kysymys/lisaa", (req, res) -> {
            String nimi = req.queryParams("kysymysteksti");
            if (nimi == null || nimi.isEmpty()) {
                return "<p>Kysymysteksti ei voi olla tyhjä.</p>";
            };
            int aiheId = Integer.parseInt(req.queryParams("aiheId"));
            Kysymys lisattavaKysmys = new Kysymys(-1, aiheId, nimi);
            kysymys.saveOrUpdate(lisattavaKysmys);
            res.redirect("/");
            return "";
        });

        Spark.post("/vastaus/lisaa", (req, res) -> {
            String nimi = req.queryParams("vastausteksti");
            if (nimi == null || nimi.isEmpty()) {
                return "<p>Vastausteksti ei voi olla tyhjä.</p>";
            };
            int kysymysId = Integer.parseInt(req.queryParams("kysymysId"));
            Boolean oikein = !((req.queryParams("vastausOikein")) == null);
            Vastaus lisattavaVastaus = new Vastaus(-1, kysymysId, nimi, oikein);
            vastaus.saveOrUpdate(lisattavaVastaus);
            res.redirect("/kysymys/" + kysymysId);
            return "";
        });

        Spark.post("/kurssi/poista", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("kurssiId"));
            Kurssi poistettavaKurssi = new Kurssi(id, "");
            kurssi.delete(poistettavaKurssi);
            res.redirect("/");
            return "";
        });

        Spark.post("/aihe/poista", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("aiheId"));
            Aihe poistettavaAihe = new Aihe(id, "");
            aihe.delete(poistettavaAihe);
            res.redirect("/");
            return "";
        });

        Spark.post("/kysymys/poista", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("kysymysId"));
            Kysymys poistettavaKysymys = new Kysymys(id, "");
            kysymys.delete(poistettavaKysymys);
            res.redirect("/");
            return "";
        });

        Spark.post("/vastaus/poista", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("vastausId"));
            int kysymysId = Integer.parseInt(req.queryParams("kysymysId"));
            Vastaus poistettavaVastaus = new Vastaus(id, "", false);
            vastaus.delete(poistettavaVastaus);
            res.redirect("/kysymys/" + kysymysId);
            return "";
        });
*/
    }

}
