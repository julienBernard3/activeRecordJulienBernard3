package activeRecord;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class DBConnectionTest {

    @Test
    public void test1_connectionUnique() throws SQLException {
        Connection connection1 = DBConnection.getConnect();
        Connection connection2 = DBConnection.getConnect();
        assertEquals(connection1,connection2, "les 2 connection devraient etre les memes");
    }

    @Test
    public void test2_changementNomBase() throws SQLException {
        Connection connection1 = DBConnection.getConnect();
        DBConnection.setDbName("test");
        boolean erreur = false;
        try {
            Connection connection = DBConnection.getConnect();
        }catch (SQLException e){
            erreur = true;
        }
        assertFalse(erreur, "la connection devrait pouvoir se faire");
    }

}