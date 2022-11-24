package activeRecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestPersonne {

    @BeforeEach
    public void before() throws SQLException {
        //on crée la talble personne
        Personne.createTable();
        //on crée des personnes et on les sauvegardes
        Personne p1 = new Personne("Spielberg","Steven");
        Personne p2 = new Personne("Scott","Ridley");
        Personne p3 = new Personne("S_c_o_t_t","R_i_d_l_e_y");
        Personne p4 = new Personne("Spielberg","StevenJr");
        p1.save();
        p2.save();
        p3.save();
        p4.save();
    }

    @AfterEach
    public void after() throws SQLException {
        Personne.deleteTable();
    }

    @Test
    public void test1_findAll() throws SQLException {
        //On vérifie le nombre de personne retourné par la methode
        ArrayList<Personne> listePersonne = Personne.findAll();
        assertEquals(4,listePersonne.size(),"Il devrait y avoir 4 personnes");

    }

    @Test
    public void test2_findById() throws SQLException {
        Personne personne = Personne.findById(1);
        //On vérifie la personne retourné par la methode
        assertEquals("Spielberg",personne.getNom(),"Il devrait s'appeler Spielberg");
        assertEquals("Steven",personne.getPrenom(),"Il devrait s'appeler Steven");
    }

    @Test
    public void test3_findByNom() throws SQLException {
        ArrayList<Personne> listePersonne = Personne.findByName("Spielberg");
        //On vérifie le nombre de Personne portant ce nom
        assertEquals(2,listePersonne.size(),"Il devrait y avoir 2 personnes");
    }
}