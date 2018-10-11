package pirulliset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pirulliset.database.Database;

public class KysymysDao implements Dao {

    Database db;

    public KysymysDao(Database db) {
        this.db = db;
    }
    
    @Override
    public Object findOne(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findAll() throws SQLException {
        List kysymykset = new ArrayList<>();
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT kysymysteksti FROM Kysymys");
        ResultSet tulos = stmt.executeQuery();

        while (tulos.next()) {
            String nimi = tulos.getString("kysymysteksti");
            kysymykset.add(nimi);
        }
        conn.close();
        return kysymykset;
    }

    @Override
    public Object saveOrUpdate(Object object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
