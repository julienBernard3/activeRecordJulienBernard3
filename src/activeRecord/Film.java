package activeRecord;
import java.sql.*;
import java.util.ArrayList;

public class Film {
    private int id;
    private String titre;
    private int id_real;

    public Film(String titre, Personne p){
        this.id = -1;
        this.id_real= p.getId();
        this.titre =titre;
    }

    private Film(int id, int id_real, String titre){
        this.id = id;
        this.id_real = id_real;
        this.titre = titre;
    }

    public static ArrayList<Film> findAll() throws SQLException {
        String SQLPrep = "SELECT * FROM Film;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();
        ArrayList<Film> listeFilms = new ArrayList<Film>();
        // s'il y a un resultat
        while (rs.next()) {
            Film f = new Film(rs.getInt("id"),rs.getInt("id_real"),rs.getString("titre"));
            listeFilms.add(f);
        }
        return listeFilms;
    }

    public static Film findById(int id) throws SQLException {
        String SQLPrep = "SELECT * FROM Film WHERE id=?;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.setInt(1, id);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();
        // s'il y a un resultat
        Film f = null;
        if (rs.next()) {
            f = new Film(rs.getInt("id"),rs.getInt("id_real"),rs.getString("titre"));
        }
        return f;
    }

    public static ArrayList<Film> findByTitre(String titre) throws SQLException {
        String SQLPrep = "SELECT * FROM Film WHERE titre=?;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.setString(1, titre);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        ArrayList<Film> listeFilm = new ArrayList<Film>();
        // s'il y a un resultat
        while (rs.next()) {
            Film f = new Film(rs.getInt("id"),rs.getInt("id_real"),rs.getString("titre"));
            listeFilm.add(f);
        }
        return listeFilm;
    }

    public static void createTable() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String createString = "CREATE TABLE Film ( " + "ID INTEGER  AUTO_INCREMENT, "
                + "TITRE varchar(40) NOT NULL, " + "ID_REAL INTEGER NOT NULL, " + "PRIMARY KEY (ID))";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(createString);
    }

    public static void deleteTable() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String drop = "DROP TABLE Film";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(drop);
    }

    public void save() throws SQLException {
        if (this.id==-1){
            this.saveNew();
        }else {
            this.update();
        }
    }

    private void saveNew() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String SQLPrep = "INSERT INTO Film (titre, id_real) VALUES (?,?);";
        PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, this.titre);
        prep.setInt(2, this.id_real);
        prep.executeUpdate();

        // recuperation de la derniere ligne ajoutee (auto increment)
        // recupere le nouvel id
        int autoInc = -1;
        ResultSet rs = prep.getGeneratedKeys();
        if (rs.next()) {
            autoInc = rs.getInt(1);
        }
        this.id=autoInc;
    }

    private void update() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String SQLprep = "update Film set titre=?, id_real=? where id=?;";
        PreparedStatement prep = connect.prepareStatement(SQLprep);
        prep.setString(1, this.titre);
        prep.setInt(2, this.id_real);
        prep.setInt(3, this.id);
        prep.execute();
    }
    
    public void delete() throws SQLException {
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep = connect.prepareStatement("DELETE FROM Film WHERE id=?");
        prep.setInt(1, this.id);
        prep.execute();
        this.id = -1;
    }

    public String getTitre() {
        return this.titre;
    }

    public int getId_real() {
        return id_real;
    }

    public int getId() {
        return id;
    }

    public Personne getRealisateur() throws SQLException {
        return Personne.findById(this.id_real);
    }
}
