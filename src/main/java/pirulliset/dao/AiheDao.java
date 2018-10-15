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
import pirulliset.domain.Vastaus;

public class AiheDao implements Dao {

    Database db;

    public AiheDao(Database db) {
        this.db = db;
    }

    @Override
    public Object findOne(Object key) throws SQLException {
        Aihe etsittavaAihe = (Aihe) key;
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Aihe WHERE (LOWER(nimi) = LOWER(?) AND kurssi_id = ?) OR id = ?");
        stmt.setString(1, etsittavaAihe.getNimi());
        stmt.setInt(2, etsittavaAihe.getKurssiId());
        stmt.setInt(3, etsittavaAihe.getId());

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        String nimi = rs.getString("nimi");
        int id = rs.getInt("id");
        Aihe lisattavaAihe = new Aihe(id, nimi);
        lisattavaAihe.setKysymykset(new KysymysDao(db).findAllByAiheId(id));

        stmt.close();
        rs.close();
        conn.close();

        return lisattavaAihe;
    }

    @Override
    public List findAll() throws SQLException {
        List aiheet = new ArrayList<>();
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT Aihe.id AS id, Aihe.nimi AS aihe FROM Aihe");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String nimi = rs.getString("nimi");
            int id = rs.getInt("id");
            Aihe lisattavaAihe = new Aihe(id, nimi);
            lisattavaAihe.setKysymykset(new KysymysDao(db).findAllByAiheId(id));
            aiheet.add(lisattavaAihe);
        }

        conn.close();
        return aiheet;
    }

    public List findAllByKurssiId(int kurssiId) throws SQLException {
        List aiheet = new ArrayList<>();
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, nimi FROM Aihe WHERE kurssi_id = ?");
        stmt.setInt(1, kurssiId);

        ResultSet tulos = stmt.executeQuery();

        while (tulos.next()) {
            String nimi = tulos.getString("nimi");
            int id = tulos.getInt("id");
            Aihe lisattavaAihe = new Aihe(id, nimi);
            lisattavaAihe.setKysymykset(new KysymysDao(db).findAllByAiheId(id));
            aiheet.add(lisattavaAihe);
        }

        conn.close();
        return aiheet;
    }

    @Override
    public Object saveOrUpdate(Object object) throws SQLException {
        Aihe aihe = (Aihe) object;

        Aihe verrattava = (Aihe) findOne(aihe); // Tarkistetaan onko aihe tietokannassa
        if (verrattava != null) {
            return aihe;
        }

        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Aihe (kurssi_id, nimi) VALUES (?, ?)");
            stmt.setInt(1, aihe.getKurssiId());
            stmt.setString(2, aihe.getNimi());
            stmt.executeUpdate();
        }

        return findOne(aihe);
    }

    @Override
    public void delete(Object key) throws SQLException {
        if (findOne(key) == null) {
            return;
        }

        KysymysDao kysymys = new KysymysDao(db);
        kysymys.deleteByAiheId(key);

        Aihe aihe = (Aihe) findOne(key);

        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Aihe WHERE id = ?");

        stmt.setInt(1, aihe.getId());
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public void deleteByKurssiId(Object key) throws SQLException {
        Kurssi kurssi = (Kurssi) key;

        KysymysDao kysymys = new KysymysDao(db); // Poistetaan aiheeseen liittyvät kysymykset
        for (Object aihe : findAllByKurssiId(kurssi.getId())) {
            kysymys.deleteByAiheId(aihe);
        }

        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Aihe WHERE kurssi_id = ?");

        stmt.setInt(1, kurssi.getId());
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

}
