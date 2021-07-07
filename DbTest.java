import java.sql.*;

public class DbTest {

    public static void main(String[] args) {
        
            System.out.println("Hello World!!");
            System.out.println("Hello World!22!");
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        String dbFileUrl = "jdbc:sqlite:test.db";
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(dbFileUrl);
            System.out.println("Sqlite conn!!");

            stmt = con.createStatement();

            rs = stmt.executeQuery("select * from temp1;");

            System.out.println(rs);
;            
        }
        catch(Exception e) {
            System.out.println(e);
        }
        }

}
