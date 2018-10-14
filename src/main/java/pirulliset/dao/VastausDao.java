package pirulliset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pirulliset.database.Database;
import pirulliset.domain.Aihe;
import pirulliset.domain.Kurssi;
import pirulliset.domain.Kysymys;
import pirulliset.domain.Vastaus;

public class VastausDao implements Dao {

    Database db;

    public VastausDao(Database db) {
        this.db = db;
    }

    @Override
    public Object findOne(Object key) throws SQLException {
        try (Connection conn = db.getConnection()) {
            Vastaus etsittavaVastaus = (Vastaus) key;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE LOWER(vastausteksti) = LOWER(?) OR id = ?");
            stmt.setString(1, etsittavaVastaus.getVastausteksti());
            stmt.setInt(2, etsittavaVastaus.getId());

            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }

            Vastaus lisattavaVastaus = new Vastaus(rs.getInt("id"), rs.getString("vastausteksti"), rs.getBoolean("oikein"));
            stmt.close();
            rs.close();

            conn.close();

            return lisattavaVastaus;
        } catch (SQLException e) {
            System.err.println("SQL-kysely epäonnistui");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List findAll() throws SQLException { // Kesken, korjaa!!!
        try (Connection conn = db.getConnection()) {
            List vastaukset = new ArrayList<>();

            //"SELECT Kurssi.nimi AS kurssi, Aihe.nimi AS aihe, Kysymys.kysymysteksti AS kysymysteksti FROM Kysymys, Aihe, Kurssi WHERE Kysymys.aihe_id = Aihe.id AND Aihe.kurssi_id = Kurssi.id");
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, vastausteksti, oikein FROM Vastaus");
            ResultSet tulos = stmt.executeQuery();

            while (tulos.next()) {
                int id = tulos.getInt("id");
                String vastausteksti = tulos.getString("vastausteksti");
                boolean oikein = tulos.getBoolean("oikein");

                vastaukset.add(new Vastaus(id, vastausteksti, oikein));
            }

            conn.close();
            return vastaukset;
        } catch (SQLException e) {
            System.err.println("SQL-kysely epäonnistui");
            e.printStackTrace();
            return null;
        }

    }

    public List findAllByKysymysId(int kysymysId) throws SQLException {
        List vastaukset = new ArrayList<>();
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, vastausteksti, oikein FROM Vastaus WHERE kysymys_id = ?");
        stmt.setInt(1, kysymysId);

        ResultSet tulos = stmt.executeQuery();

        while (tulos.next()) {
            String vastausteksti = tulos.getString("vastausteksti");
            int id = tulos.getInt("id");
            boolean oikein = tulos.getBoolean("oikein");
            vastaukset.add(new Vastaus(id, vastausteksti, oikein)); // Lisätään ehdon täyttävät kysymykset listalle
        }

        conn.close();
        return vastaukset;
    }

    @Override
    public Object saveOrUpdate(Object object) throws SQLException {
        Vastaus vastaus = (Vastaus) object;
        
        Vastaus verrattava = (Vastaus) findOne(vastaus); // Tarkistetaan onko vastaus tietokannassa
        if (verrattava != null) {
            return vastaus;
        }

        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastaus (kysymys_id, vastausteksti, oikein) VALUES (?, ?, ?)");
            stmt.setInt(1, vastaus.getKysymysId());
            stmt.setString(2, vastaus.getVastausteksti());
            stmt.setBoolean(3, vastaus.getOikein());
            stmt.executeUpdate();
        }

        return findOne(vastaus);
    }

    @Override
    public void delete(Object key) throws SQLException {
        Vastaus vastaus = (Vastaus) findOne(key);

        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE id = ?");

        stmt.setInt(1, vastaus.getId());
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public void deleteByKysymysId(Object key) throws SQLException {
        Kysymys kysymys = (Kysymys) key;
        
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE kysymys_id = ?");

        stmt.setInt(1, kysymys.getId());
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

}
