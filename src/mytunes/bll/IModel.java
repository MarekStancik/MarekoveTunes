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

/**
 *
 * @author Marek
 */
public interface IModel
{
    List<Song> getAllSongs();
    List<Playlist> getAllPlaylists();
    void addSong(Song s);
    void addPlaylist(Playlist p);
    void deleteSong(Song s);
    void deletePlaylist(Playlist p);    
    ObservableList<Song> getPlaylistSongs(Playlist p);
    void addSongToPlaylist(Playlist p, Song s);
    void deleteSongFromPlaylist(Playlist p, Song s);
    List<Song> loadAllSongs();
    List<Playlist> getFilteredPlaylists(String filter);
    List<Song> getFilteredSongs(String filter);
    List<Song> getFilteredPlaylistSongs(Playlist p,String filter);
}
