package hu.petrik.filmdb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDb {
    Connection conn;

    public FilmDb() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/filmdb", "root", "");
    }

    public List<Film> getFilmek() throws SQLException {
        List<Film> filmek = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM filmek;";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String cim = result.getString("cim");
            String kategoria = result.getString("kategoria");
            int hossz = result.getInt("hossz");
            int ertekeles = result.getInt("ertekeles");
            Film film = new Film(id, cim, kategoria, hossz, ertekeles);
            filmek.add(film);
        }
        return filmek;
    }

    public boolean filmHozzaadasa(String cim, String kategoria, int hossz, int ertekeles) throws SQLException {
        String sql = "INSERT INTO filmek (cim, kategoria, hossz, ertekeles) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, cim);
        stmt.setString(2, kategoria);
        stmt.setInt(3, hossz);
        stmt.setInt(4, ertekeles);
        return stmt.execute();
    }
}
