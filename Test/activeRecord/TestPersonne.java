package activeRecord;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestPersonne {

    @Test
    public void test1_findAll() throws SQLException {
        ArrayList<Personne> listePersonne = Personne.findAll();
        assertEquals(4,listePersonne.size(),"Il devrait y avoir 2 personnes");

    }

    @Test
    public void test2_findById() throws SQLException {
        Personne personne = Personne.findById(1);

        assertEquals("Spielberg",personne.getNom(),"Il devrait s'appeler Spielberg");
        assertEquals("Steven",personne.getPrenom(),"Il devrait s'appeler Steven");
    }

    @Test
    public void test3_findByNom() throws SQLException {
        ArrayList<Personne> listePersonne = Personne.findByNom("Spielberg");
        assertEquals(1,listePersonne.size(),"Il devrait y avoir 1 personnes");
    }
}