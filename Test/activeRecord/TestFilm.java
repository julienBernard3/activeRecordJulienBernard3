package activeRecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestFilm {
    Personne p1;

    @BeforeEach
    public void before() throws SQLException {
        Personne.createTable();
        this.p1 = new Personne("Spielberg","Steven");
        this.p1.save();
        Film.createTable();
        Film f1=new Film("seven", p1);
        f1.save();
        Film f2 = new Film("test2",p1);
        f2.save();
    }

    @AfterEach
    public void after() throws SQLException {
        Personne.deleteTable();
        Film.deleteTable();
    }

    @Test
    public void test1_findAll() throws SQLException {
        ArrayList<Film> listeFilm = Film.findAll();
        assertEquals(2,listeFilm.size(),"Il devrait y avoir 2 films");

    }

    @Test
    public void test2_findById() throws SQLException {
        Film film = Film.findById(1);

        assertEquals("seven",film.getTitre(),"Il devrait s'appeler seven");
        assertEquals(1,film.getId_real(),"L'id_Real devrait être 1");
        assertEquals(1, film.getRealisateur().getId(),"le realisateur devrait avoir pour id 1");
    }

    @Test
    public void test3_findByNom() throws SQLException {
        ArrayList<Film> listeFilm = Film.findByName("seven");
        assertEquals(1,listeFilm.size(),"Il devrait y avoir 1 Film");
    }
}