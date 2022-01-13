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

    public int filmHozzaadasa(String cim, String kategoria, int hossz, int ertekeles) throws SQLException {
        String sql = "INSERT INTO filmek (cim, kategoria, hossz, ertekeles) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, cim);
        stmt.setString(2, kategoria);
        stmt.setInt(3, hossz);
        stmt.setInt(4, ertekeles);
        return stmt.executeUpdate();
    }

    public boolean filmTorles(int id) throws SQLException {
        String sql = "DELETE FROM filmek WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        int erintettSorok = stmt.executeUpdate();
        return erintettSorok == 1;
    }

    public boolean filmModositasa(Film modositando) throws SQLException {
        String sql = "UPDATE filmek SET cim = ?, kategoria = ?, hossz = ?, ertekeles = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, modositando.getCim());
        stmt.setString(2, modositando.getKategoria());
        stmt.setInt(3, modositando.getHossz());
        stmt.setInt(4, modositando.getErtekeles());
        stmt.setInt(5, modositando.getId());
        int erintettSorok = stmt.executeUpdate();
        return erintettSorok == 1;
    }
}
