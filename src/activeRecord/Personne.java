package activeRecord;
import java.sql.*;
import java.util.ArrayList;

public class Personne {
    private int id;
    private String nom;
    private String prenom;

    /**
     * Constructeur de Personne
     * @param nom nom de la personne
     * @param prenom prenom de la personne
     */
    public Personne(String nom, String prenom){
        //Initialisation de l'id
        this.id = -1;
        this.prenom=prenom;
        this.nom=nom;
    }

    /**
     * Methode permettant de retourner tous les tuples de la table
     * @return liste de Personnes
     * @throws SQLException
     */
    public static ArrayList<Personne> findAll() throws SQLException {
        String SQLPrep = "SELECT * FROM Personne;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();
        ArrayList<Personne> listePersonne = new ArrayList<Personne>();
        // s'il y a un resultat
        while (rs.next()) {
            Personne p = new Personne(rs.getString("nom"),rs.getString("prenom"));
            p.id = rs.getInt("id");
            listePersonne.add(p);
        }
        return listePersonne;
    }

    /**
     * Methode permettant de rechercher une Personne par son id
     * @param id id de la personne que l'on recherche
     * @return object Personne
     * @throws SQLException
     */
    public static Personne findById(int id) throws SQLException {
        String SQLPrep = "SELECT * FROM Personne WHERE id=?;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        //On recherche dans la table selon l'id en paramètre
        prep1.setInt(1, id);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();
        // s'il y a un resultat
        Personne p = null;
        if (rs.next()) {
            p = new Personne(rs.getString("nom"),rs.getString("prenom"));
            p.id = rs.getInt("id");
        }
        return p;
    }

    /**
     * Methode permettant de rechercher une ou plusieurs personnes par leurs noms
     * @param nom nom de la personne
     * @return Liste de Personne
     * @throws SQLException
     */
    public static ArrayList<Personne> findByName(String nom) throws SQLException {
        String SQLPrep = "SELECT * FROM Personne WHERE nom=?;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);

        //on effectue la recherche selon le nom en paramètre
        prep1.setString(1, nom);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        ArrayList<Personne> listePersonne = new ArrayList<Personne>();
        // s'il y a un resultat
        while (rs.next()) {
            Personne p = new Personne(rs.getString("nom"),rs.getString("prenom"));
            p.id = rs.getInt("id");
            listePersonne.add(p);
        }
        return listePersonne;
    }

    /**
     * Methode permettant de crée la table personne dans la base de données
     * @throws SQLException
     */
    public static void createTable() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String createString = "CREATE TABLE Personne ( " + "ID INTEGER  AUTO_INCREMENT, "
                + "NOM varchar(40) NOT NULL, " + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(createString);
    }

    /**
     * Methode permettant de supprimer la table personne de la base de données
     * @throws SQLException
     */
    public static void deleteTable() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String drop = "DROP TABLE Personne";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(drop);
    }

    /**
     * Methode permettant de sauvegarder la personne dans la base
     * @throws SQLException
     * @throws RealisateurAbsentException
     */
    public void save() throws SQLException {
        if (this.id==-1){
            //Cas ou la personne n'exite pas --> enregistrement d'une nouvelle personne
            this.saveNew();
        }else {
            //cas ou la personne existe deja --> mise a jour de la personne
            this.update();
        }
    }

    /**
     * Methode permettant de sauvegarder une nouvelle personne dans la base de données
     * @throws SQLException
     */
    private void saveNew() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String SQLPrep = "INSERT INTO Personne (nom, prenom) VALUES (?,?);";
        PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
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
     * methode permettant d'actualisé la personne en question dans la base de données
     * @throws SQLException
     */
    private void update() throws SQLException {
        Connection connect = DBConnection.getConnect();
        String SQLprep = "update Personne set nom=?, prenom=? where id=?;";
        PreparedStatement prep = connect.prepareStatement(SQLprep);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
        prep.setInt(3, this.id);
        prep.execute();
    }

    /**
     * Methode permettant de supprimer la personne de la base de données
     * @throws SQLException
     */
    public void delete() throws SQLException {
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep = connect.prepareStatement("DELETE FROM Personne WHERE id=?");
        prep.setInt(1, this.id);
        prep.execute();
        //On redéfinis l'id par -1 signifiant qu'elle est absente de la talble
        this.id = -1;
    }

    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getId() {
        return id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}
