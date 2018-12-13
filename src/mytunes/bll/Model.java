/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.io.File;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
   // private FileSearch FS;
    
    public Model()
    {
        songDao = new SongDAO();
        playlistDao = new PlaylistDAO();
        //FS = new FileSearch();
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
    public ObservableList<Song> getPlaylistSongs(Playlist p) {
       return null;// return playlistDao.getPlaylistSongs(p);
    }

    @Override
    public void addSongToPlaylist(Playlist p, Song s) {
        playlistDao.addSongToPlaylist(p, s);
    }

    @Override
    public List<Song> loadAllSongs() {
        
        return /*null;*/FS.getSongs();
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
