package activeRecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestFilm {
    Personne p1;
    Film f2;

    @BeforeEach
    public void before() throws SQLException, RealisateurAbsentException {
        //On crée la table personne
        Personne.createTable();
        //On crée 2 personnes qu'on enregistre dans la table
        this.p1 = new Personne("Spielberg","Steven");
        this.p1.save();
        Personne p3 = new Personne("personne3Nom","Personne3Prenom");
        p3.save();
        //On crée la talbe film
        Film.createTable();
        //On crée des films dont 2 du meme réalisateur
        Film f1=new Film("seven", p1);
        Film f3=new Film("film3", p3);
        Film f4=new Film("film4", p1);
        //On enregistre les films
        f1.save();
        f3.save();
        f4.save();
        //On crée une personne sans l'enregistrer dans la table
        Personne p2 = new Personne("pNom","pPrenom");
        //On crée un fim a partir de cette personne sans l'enregistrer dans la table
        this.f2 = new Film("test2",p2);
    }

    @AfterEach
    public void after() throws SQLException {
        //on supprime les tables
        Personne.deleteTable();
        Film.deleteTable();
    }

    @Test
    public void test1_findAll() throws SQLException {
        ArrayList<Film> listeFilm = Film.findAll();
        //On vérifie le nombre de film retourné par la methode
        assertEquals(3,listeFilm.size(),"Il devrait y avoir 3 films");

    }

    @Test
    public void test2_findById() throws SQLException {
        Film film = Film.findById(1);
        //On vérifie que le film retourné est le bon
        assertEquals("seven",film.getTitre(),"Il devrait s'appeler seven");
        assertEquals(1,film.getId_real(),"L'id_Real devrait être 1");
        assertEquals(1, film.getRealisateur().getId(),"le realisateur devrait avoir pour id 1");
    }

    @Test
    public void test3_findByNom() throws SQLException {
        //On vérifie que le nombre de film retourné est correcte
        ArrayList<Film> listeFilm = Film.findByName("seven");
        assertEquals(1,listeFilm.size(),"Il devrait y avoir 1 Film");
    }

    @Test
    public void test4_saveFilmRealisateurAbsent() throws SQLException, RealisateurAbsentException {
        boolean exception = false;
        try{
            //On test de sauvegarder dans le film dans la table
            this.f2.save();
        }catch (RealisateurAbsentException e){
            exception = true;
        }
        assertTrue(exception,"l'exception RealisateurAbsentException devrait etre levé");
    }

    @Test
    public void test5_findByRealisateur() throws SQLException, RealisateurAbsentException {
        ArrayList<Film> listeFilm = Film.findByRealisateur(this.p1);
        //On recherche les films du realisateur et on vérifie leurs nombres
        assertEquals(2,listeFilm.size(),"Il devrait y avoir 2 films réalisé par ce réalisateur");
    }
}