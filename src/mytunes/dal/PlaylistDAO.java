/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author Marek
 */
public class PlaylistDAO
{
    private IConnectionProvider conProvider;
    
    public PlaylistDAO()
    {
        conProvider = new ConnectionProvider();
    }
    
    public void deletePlaylist(Playlist p)
    {
        try (Connection con = conProvider.getConnection()){
            String sql = "DELETE FROM PlaylistsSongs WHERE PlaylistID = ?; DELETE FROM Playlists WHERE ID=?;";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, p.getID());
            stmt.setInt(2, p.getID());
            stmt.execute();
        }
        catch(SQLServerException ex){
            Logger.getLogger(PlaylistDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SQLException ex){
            Logger.getLogger(PlaylistDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   public void addPlaylist(Playlist p)
    {
        try (Connection con = conProvider.getConnection()){
            String sql = "INSERT INTO Playlists (Name) VALUES (?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, p.getName());
            stmt.execute();
            ResultSet rs = con.createStatement().executeQuery("SELECT MAX(ID) as ID FROM Playlists");
            rs.next();
            p.setID(rs.getInt("ID"));
        }
        catch(SQLServerException ex){
            Logger.getLogger(PlaylistDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SQLException ex){
            Logger.getLogger(PlaylistDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    public Playlist getPlaylist(String name)
    {
        return null;
    }
    
    public List<Playlist> getAllPlaylists()
    {
        return getFilteredPlaylists("");
    }

    public void addSongToPlaylist(Playlist p, Song s)
    {
        try(Connection con = conProvider.getConnection())
        {
            String sql =    "INSERT INTO PlaylistsSongs (PlaylistID, SongID)\n" +
                            "VALUES (?,?)";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setInt(1,p.getID());
            psmt.setInt(2, s.getID());
            psmt.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SongDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(SongDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Song> getFilteredPlaylistSongs(Playlist p,String filter)
    {
        List<Song> retval = new ArrayList<>();
        try(Connection con = conProvider.getConnection())
        {
            String sqlStatement = "SELECT *,PlaylistsSongs.PlaylistID "
                    + "FROM Songs "
                    + "LEFT JOIN PlaylistsSongs ON PlaylistsSongs.SongID = Songs.ID "
                    + "WHERE PlaylistsSongs.PlaylistID = " + p.getID() + " AND Songs.Title LIKE ? ORDER BY ID;";
            PreparedStatement statement = con.prepareStatement(sqlStatement);
            statement.setString(1, filter + "%");
            ResultSet rs = statement.executeQuery();
            Playlist current = null;
            while(rs.next())
            {
                retval.add(SongDAO.songFromRs(rs));
            }
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SongDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(SongDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retval; 
    }
    
    public List<Playlist> getFilteredPlaylists(String filter)
    {
        List<Playlist> retval = new ArrayList<>();
        try(Connection con = conProvider.getConnection())
        {
            String sqlStatement = "SELECT Playlists.ID as ID ,Playlists.Name as Name,PlaylistsSongs.SongID as SongID FROM Playlists "
                    + "LEFT JOIN PlaylistsSongs ON PlaylistsSongs.PlaylistID = Playlists.ID WHERE Playlists.Name LIKE ? ORDER BY ID";
            PreparedStatement statement = con.prepareStatement(sqlStatement);
            statement.setString(1, filter + "%");
            ResultSet rs = statement.executeQuery();
            Playlist current = null;
            while(rs.next())
            {
                if(current != null && current.getID() == rs.getInt("ID"))
                {
                    current.addSongId(rs.getInt("SongID"));
                }
                else
                {
                    if(current != null)
                        retval.add(current);
                    current = new Playlist(rs.getInt("ID"),rs.getString("Name"));
                    current.addSongId(rs.getInt("SongID"));
                }
            }
            if(current != null)
                retval.add(current);
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SongDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(SongDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retval;
    }
    
    public void deleteSongFromPlaylist(Playlist p, Song s)
    {
        try(Connection con = conProvider.getConnection())
        {
            String sql = "DELETE FROM PlaylistsSongs WHERE PlaylistID = ? AND SongID = ?;";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setInt(1,p.getID());
            psmt.setInt(2, s.getID());
            psmt.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SongDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(SongDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
