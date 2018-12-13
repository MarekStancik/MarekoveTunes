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

    //Returns all songs from databse
    List<Song> getAllSongs();

    //returns all playlists from database
    List<Playlist> getAllPlaylists();

    //adds song to database
    void addSong(Song s);
    
    //adds playlist to database
    void addPlaylist(Playlist p);

    void deleteSong(Song s);

    void deletePlaylist(Playlist p);

    void addSongToPlaylist(Playlist p, Song s);

    void deleteSongFromPlaylist(Playlist p, Song s);

    List<Playlist> getFilteredPlaylists(String filter);

    List<Song> getFilteredSongs(String filter);

    List<Song> getFilteredPlaylistSongs(Playlist p, String filter);
}
