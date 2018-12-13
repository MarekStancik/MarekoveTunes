/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.util.List;
import javafx.collections.ObservableList;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.dal.PlaylistDAO;
import mytunes.dal.SongDAO;

/**
 *
 * @author Marek
 */
public class Model implements IModel
{
    private SongDAO songDao;
    private PlaylistDAO playlistDao;
    
    public Model()
    {
        songDao = new SongDAO();
        playlistDao = new PlaylistDAO();
    }
    
    @Override
    public List<Song> getAllSongs()
    {
        return songDao.getAllSongs(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Playlist> getAllPlaylists()
    {
        return playlistDao.getAllPlaylists(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addSong(Song s)
    {
        songDao.addSong(s); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addPlaylist(Playlist p)
    {
        playlistDao.addPlaylist(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteSong(Song s)
    {
        songDao.removeSong(s); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletePlaylist(Playlist p)
    {
        playlistDao.deletePlaylist(p); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void updatePlaylist(Playlist p)
    {
        playlistDao.updatePlaylist(p);
    }

    @Override
    public void addSongToPlaylist(Playlist p, Song s) {
        playlistDao.addSongToPlaylist(p, s);
    }

    @Override
    public void deleteSongFromPlaylist(Playlist p, Song s)
    {
        playlistDao.deleteSongFromPlaylist(p,s);
    }

    @Override
    public List<Playlist> getFilteredPlaylists(String filter)
    {
        return playlistDao.getFilteredPlaylists(filter);
    }

    @Override
    public List<Song> getFilteredSongs(String filter)
    {
        return songDao.getFilteredSongs(filter);
    }

    @Override
    public List<Song> getFilteredPlaylistSongs(Playlist p,String filter)
    {
        return playlistDao.getFilteredPlaylistSongs(p,filter);
    }  
    
}
