package pirulliset.app;

import java.sql.*;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
//import spark.template.thymeleaf.ThymeleafTemplateEngine;
import pirulliset.database.Database;
import spark.Spark;

public class Main {

    public static void main(String[] args) {

        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        Spark.get("/hei", (req, res) -> {
            return "Hei maailma!";
        });

    }

}
