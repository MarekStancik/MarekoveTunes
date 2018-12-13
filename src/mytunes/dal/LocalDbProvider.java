/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Marek
 */
public class LocalDbProvider implements IConnectionProvider
{
    public static Connection getLocalConnection(String fileName) throws IOException 
    {
 
        Path path = Paths.get("localdb.sql"); 
        Connection retval = null;
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "\\data\\" + fileName;
        String []sqls = new String(Files.readAllBytes(path)).split(";");
        System.out.println(url);
        try
        {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(LocalDbProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        try 
        {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) 
            {
                retval = conn;
                DatabaseMetaData meta = conn.getMetaData();
                {
                    for (String sql : sqls)
                    {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sql);  
                    }
                }                
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retval;
    }
    
    public Connection getConnection()
    {
        try
        {
            return getLocalConnection("mytunes.db");
        } catch (IOException ex)
        {
            Logger.getLogger(LocalDbProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
 
}
