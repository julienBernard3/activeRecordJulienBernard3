package activeRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Personne {
    private int id;
    private String nom;
    private String prenom;

    public Personne(String nom, String prenom){
        this.id = -1;
        this.prenom=prenom;
        this.nom=nom;
    }

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

    public static Personne findById(int id) throws SQLException {
        String SQLPrep = "SELECT * FROM Personne WHERE id=?;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
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

    public static ArrayList<Personne> findByNom(String nom) throws SQLException {
        String SQLPrep = "SELECT * FROM Personne WHERE nom=?;";
        Connection connect = DBConnection.getConnect();
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
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

}
