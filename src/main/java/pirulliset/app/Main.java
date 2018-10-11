package pirulliset.app;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import pirulliset.dao.KysymysDao;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import pirulliset.database.Database;
import spark.Spark;

public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        File tiedosto = new File("db", "pirulliset.db");
        Database db = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());
        KysymysDao kysymys = new KysymysDao(db);
        
        Spark.get("*", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kysymykset", kysymys.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
/*
        Spark.post("*", (req, res) -> {
            tehtavat.saveOrUpdate(new Tehtava(req.queryParams("nimi")));
            res.redirect("*");
            return "";
        });
*/
    }

}
