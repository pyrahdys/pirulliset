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

public class KysymysDao implements Dao {

    Database db;

    public KysymysDao(Database db) {
        this.db = db;
    }

    @Override
    public Object findOne(Object key) throws SQLException { // Katsotaan, löytyykö kysymystä kysymystekstin ja aihe_id:n yhdistelmällä tai ID:llä
        Kysymys etsittavaKysymys = (Kysymys) key;

        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE (LOWER(kysymysteksti) = LOWER(?) AND aihe_id = ?) OR id = ?");
        stmt.setString(1, etsittavaKysymys.getKysymysteksti());
        stmt.setInt(2, etsittavaKysymys.getAiheId());
        stmt.setInt(3, etsittavaKysymys.getId());

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int id = rs.getInt("id");
        String kysymysteksti = rs.getString("kysymysteksti");
        Kysymys lisattavaKysymys = new Kysymys(id, kysymysteksti);
        lisattavaKysymys.setVastaukset(new VastausDao(db).findAllByKysymysId(id));

        stmt.close();
        rs.close();

        conn.close();
        return lisattavaKysymys;
    }

    @Override
    public List findAll() throws SQLException {
        List kysymykset = new ArrayList<>();
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, kysymysteksti FROM Kysymys");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String kysymysteksti = rs.getString("kysymysteksti");
            Kysymys lisattavaKysymys = new Kysymys(id, kysymysteksti);
            lisattavaKysymys.setVastaukset(new VastausDao(db).findAllByKysymysId(id));
            kysymykset.add(lisattavaKysymys);
        }

        conn.close();
        return kysymykset;
    }

    public List findAllByAiheId(int aiheId) throws SQLException { // Etsitään kaikki aiheen kysymykset aiheen ID:llä
        List kysymykset = new ArrayList<>();
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, kysymysteksti FROM Kysymys WHERE aihe_id = ?");
        stmt.setInt(1, aiheId);

        ResultSet tulos = stmt.executeQuery();

        while (tulos.next()) {
            String kysymysteksti = tulos.getString("kysymysteksti");
            int id = tulos.getInt("id");
            Kysymys lisattavaKysymys = new Kysymys(id, kysymysteksti);
            lisattavaKysymys.setVastaukset(new VastausDao(db).findAllByKysymysId(id));
            kysymykset.add(lisattavaKysymys); // Lisätään ehdon täyttävät kysymykset listalle
        }

        conn.close();
        return kysymykset;
    }

    @Override
    public Object saveOrUpdate(Object object) throws SQLException {
        Kysymys kysymys = (Kysymys) object;

        Kysymys verrattava = (Kysymys) findOne(kysymys); // Tarkistetaan onko kysymys tietokannassa
        if (verrattava != null) {
            return kysymys;
        }

        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (aihe_id, kysymysteksti) VALUES (?, ?)");
            stmt.setInt(1, kysymys.getAiheId());
            stmt.setString(2, kysymys.getKysymysteksti());
            stmt.executeUpdate();
        }

        return findOne(kysymys);
    }

    @Override
    public void delete(Object key) throws SQLException { // poistetaan kaikki aiheen kysymykset aiheen ID:llä
        if (findOne(key) == null) {
            return;
        }

        VastausDao vastaus = new VastausDao(db); // Poistetaan kysymykseen liittyvät vastaukset
        vastaus.deleteByKysymysId(key);

        Kysymys kysymys = (Kysymys) findOne(key);

        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");

        stmt.setInt(1, kysymys.getId());
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public void deleteByAiheId(Object key) throws SQLException {
        Aihe aihe = (Aihe) key;

        VastausDao vastaus = new VastausDao(db); // Poistetaan kysymykseen liittyvät vastaukset
        for (Object kysymys : findAllByAiheId(aihe.getId())) {
            vastaus.deleteByKysymysId(kysymys);
        }

        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE aihe_id = ?");

        stmt.setInt(1, aihe.getId());
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
