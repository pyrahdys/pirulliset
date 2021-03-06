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
    public Object findOne(Object key) throws SQLException { // Katsotaan, löytyykö vastausta vastaustekstin ja kysymys_id:n yhdistelmällä tai ID:llä
        Vastaus etsittavaVastaus = (Vastaus) key;
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE (LOWER(vastausteksti) = LOWER(?) AND kysymys_id = ?) OR id = ?");
        stmt.setString(1, etsittavaVastaus.getVastausteksti());
        stmt.setInt(2, etsittavaVastaus.getKysymysId());
        stmt.setInt(3, etsittavaVastaus.getId());

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
    }

    @Override
    public List findAll() throws SQLException {
        List vastaukset = new ArrayList<>();

        Connection conn = db.getConnection();
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

    }

    public List findAllByKysymysId(int kysymysId) throws SQLException { // Etsitään kaikki kysymyksen vastaukset kysymyksen ID:llä
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

        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastaus (kysymys_id, vastausteksti, oikein) VALUES (?, ?, ?)");
        stmt.setInt(1, vastaus.getKysymysId());
        stmt.setString(2, vastaus.getVastausteksti());
        stmt.setBoolean(3, vastaus.getOikein());
        stmt.executeUpdate();

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

    public void deleteByKysymysId(Object key) throws SQLException { // poistetaan kaikki kysymyksen vastaukset kysymyksen ID:llä
        Kysymys kysymys = (Kysymys) key;

        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE kysymys_id = ?");

        stmt.setInt(1, kysymys.getId());
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

}
