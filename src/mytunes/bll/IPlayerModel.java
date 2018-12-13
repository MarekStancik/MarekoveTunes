/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.util.List;
import mytunes.be.Song;

/**
 *
 * @author Tothko
 */
public interface IPlayerModel
{

    public int getCurrentIndex();

    public void setShuffle(boolean shuffle);

    //Plays song from playlist on the given index, and stores the provided playlist as current playlist
    public void playPlaylist(List<Song> playlist, int index);

    public void setVolume(double percentage);

    public void setTime(double percentage);

    //returns current time of song in percentage
    public double getTimePercentage();

    public String getFormatedCurrentTime();

    //Plays next song in the palylist
    public Song playNext();

    //Plays previous song
    public Song playPrev();

    public void setRepeat(boolean repeat);

    public void play();

    public void stop();

    public void pause();

    public boolean isPlaying();
}
