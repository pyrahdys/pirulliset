package pirulliset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pirulliset.database.Database;
import pirulliset.domain.Kurssi;

public class KurssiDao implements Dao {

    Database db;

    public KurssiDao(Database db) {
        this.db = db;
    }

    @Override
    public Object findOne(Object key) throws SQLException {
        Kurssi etsittavaKurssi = (Kurssi) key;
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kurssi WHERE LOWER(nimi) = LOWER(?) OR id = ?");
        stmt.setString(1, etsittavaKurssi.getNimi());
        stmt.setInt(2, etsittavaKurssi.getId());

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        String nimi = rs.getString("nimi");
        int id = rs.getInt("id");
        Kurssi lisattavaKurssi = new Kurssi(id, nimi);
        lisattavaKurssi.setAiheet(new AiheDao(db).findAllByKurssiId(id));

        stmt.close();
        rs.close();
        conn.close();

        return lisattavaKurssi;

    }

    @Override
    public List findAll() throws SQLException {
        List kurssit = new ArrayList<>();
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, nimi FROM Kurssi");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String nimi = rs.getString("nimi");
            int id = rs.getInt("id");
            Kurssi lisattavaKurssi = new Kurssi(id, nimi);
            lisattavaKurssi.setAiheet(new AiheDao(db).findAllByKurssiId(id));
            kurssit.add(lisattavaKurssi);
        }

        conn.close();

        return kurssit;
    }

    @Override
    public Object saveOrUpdate(Object object) throws SQLException {
        Kurssi kurssi = (Kurssi) object;
        Kurssi verrattava = (Kurssi) findOne(kurssi); // Tarkistetaan onko kurssi tietokannassa
        if (verrattava != null) {
            return kurssi;
        }

        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kurssi (nimi) VALUES (?)");
            stmt.setString(1, kurssi.getNimi());
            stmt.executeUpdate();
        }

        return findOne(kurssi);
    }

    @Override
    public void delete(Object key) throws SQLException {
        Kurssi tutkittava = (Kurssi) findOne(key);

        if (findOne(key) == null) {
            return;
        }

        AiheDao aihe = new AiheDao(db);
        aihe.deleteByKurssiId(key);

        Kurssi kurssi = (Kurssi) findOne(key);

        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kurssi WHERE id = ?");

        stmt.setInt(1, kurssi.getId());
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

}
