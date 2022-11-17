import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static Connection connect;
    private static String userName = "root";
    private static String password = "";
    private static String serverName = "localhost";
    private static String portNumber = "3306";
    private static String tableName = "personne";
    private static String dbName = "testpersonne";

    private DBConnection(String userName, String password,String serverName, String portNumber, String dbName ) throws SQLException {
        // creation de la connection
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        String urlDB = "jdbc:mysql://" + serverName + ":";
        urlDB += portNumber + "/" + dbName;
        connect = DriverManager.getConnection(urlDB, connectionProps);
    }

    public static Connection getConnect() throws SQLException {
        if(connect == null){
            new DBConnection(userName, password, serverName, portNumber, dbName);
        }
        return connect;
    }

    public static void setDbName(String dbName) {
        DBConnection.dbName = dbName;
        connect = null;
    }
}
