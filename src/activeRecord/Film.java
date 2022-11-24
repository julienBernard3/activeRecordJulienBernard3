package activeRecord;
import org.junit.jupiter.api.function.Executable;

import java.sql.*;
import java.util.ArrayList;

public class Film {
    private int id;
    private String titre;
    private int id_real;

    /**
     * Création d'un film
     * @param titre titre du film
     * @param p personne correspondant au réalisateur
     */
    public Film(String titre, Personne p){
        //Initialisationd de l'id
        this.id = -1;
        this.id_real= p.getId();
        this.titre =titre;
    }

    /**
     * Constructeur de film privée (servant pour le save d'un film innexistant dans la table
     * @param id id du film
     * @param id_real id du realisateur
     * @param titre titre du film
     */
    private Film(int id, int id_real, String titre){
        this.id = id;
        this.id_real = id_real;
        this.titre = titre;
    }

    /**
     * Methode permettant de retourner tous les tuples de la table
     * @return liste de film
     * @throws SQLException
     */
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

    /**
     * Methode permettant de rechercher un film par son id
     * @param id id du film que l'on recherche
     * @return object film
     * @throws SQLException
     */
    public static Film findById(int id) throws SQLException {
        String SQLPrep = "SELECT * FROM Film WHERE id=?;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        //On recherche dans la table film a partir de l'id du film
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

    /**
     * Methode permettant de rechercher ou plusieurs films par leurs nom
     * @param titre nom du film
     * @return Liste de film
     * @throws SQLException
     */
    public static ArrayList<Film> findByName(String titre) throws SQLException {
        String SQLPrep = "SELECT * FROM Film WHERE titre=?;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);

        //On recherche dans la table film a partir du titre du film
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

    /**
     * Methode permettant de rechercher un ou plusieurs Film par leurs réalisateur
     * @param p objet Personne corresponctant au réalisateur
     * @return Liste des films réalisé par ce réalisateur
     * @throws SQLException
     */
    public static ArrayList<Film> findByRealisateur(Personne p) throws SQLException {
        String SQLPrep = "SELECT * FROM Film WHERE id_real=?;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        //On recherche dans la table film a partir de l'id du realisateur
        prep1.setInt(1, p.getId());
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

    /**
     * Methode permettant de crée la table film dans la base de données
     * @throws SQLException
     */
    public static void createTable() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String createString = "CREATE TABLE Film ( " + "ID INTEGER  AUTO_INCREMENT, "
                + "TITRE varchar(40) NOT NULL, " + "ID_REAL INTEGER NOT NULL, " + "PRIMARY KEY (ID))";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(createString);
    }

    /**
     * Methode permettant de supprimer la table film de la base de données
     * @throws SQLException
     */
    public static void deleteTable() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String drop = "DROP TABLE Film";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(drop);
    }

    /**
     * Methode permettant de sauvegarder le film dans la base
     * @throws SQLException
     * @throws RealisateurAbsentException
     */
    public void save() throws SQLException, RealisateurAbsentException {
        //Vérification que le réaliateur est présent dans la base
        if(this.id_real == -1){
            throw new RealisateurAbsentException("Realisateur absent");
        }
        if (this.id==-1){
            //Cas ou le film n'exite pas --> enregistrement d'un nouveau film
            this.saveNew();
        }else {
            //cas ou le film existe deja --> mise a jour du film
            this.update();
        }
    }

    /**
     * Methode permettant de sauvegarder un nouveau film dans la base de données
     * @throws SQLException
     */
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

    /**
     * methode permettant d'actualisé le film en question dans la base de données
     * @throws SQLException
     */
    private void update() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String SQLprep = "update Film set titre=?, id_real=? where id=?;";
        PreparedStatement prep = connect.prepareStatement(SQLprep);
        prep.setString(1, this.titre);
        prep.setInt(2, this.id_real);
        prep.setInt(3, this.id);
        prep.execute();
    }

    /**
     * Methode permettant de supprimer le film de la base de données
     * @throws SQLException
     */
    public void delete() throws SQLException {
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep = connect.prepareStatement("DELETE FROM Film WHERE id=?");
        prep.setInt(1, this.id);
        prep.execute();
        //On redéfinis l'id par -1 signifiant qu'il est absent de la talble
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

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", id_real=" + id_real +
                '}';
    }
}
