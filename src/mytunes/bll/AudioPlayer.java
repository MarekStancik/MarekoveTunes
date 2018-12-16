/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author Marek
 */
public class AudioPlayer implements IPlayerModel
{
    private class ShuffleCollection //Class that creates collection for random play order without repeating
    {
        private List<Integer> collection; //List that stores the indexes of the songs
        private Iterator<Integer> iterator; //Iterator over the list
        
        public ShuffleCollection(List source) //Fills collection with shuffled indexes
        {
            if(source != null)
            {
                collection = new ArrayList();
                for (int i = 0; i < source.size(); i++) //add every number
                    collection.add(i);
                Collections.shuffle(collection); //shuffle them
                iterator = collection.iterator(); //set iterator at the beginig of colleciton
            }
        }
        
        public int next()
        {
            if(!iterator.hasNext()) //if reached end than shuffle collection again and set iterator on start of collection
            {
                Collections.shuffle(collection);
                iterator = collection.iterator();
            }
            return iterator.next();
        }
              
        public int size() //returns size of collection
        {
            return collection.size();
        }
    }
    
    private ShuffleCollection shuffleCollection; //Stores shuffled indexes of current playlist
    private MediaPlayer mediaPlayer; //Media player class for current media
    private List<Song> currentPlaylist; //Reference to chosen playlist
    private boolean shuffle; //indicates whether shuffle is on
    private boolean repeat; //indicates whether repeat is on
    private int currentIndex; //indicates index of currently playing song
    private double volumePercentage; 
    
    public AudioPlayer()
    {
        volumePercentage = 100;
    }
     
    public int getCurrentIndex()
    {
        return currentIndex;
    }
    
    public void setShuffle(boolean shuffle)
    {
        this.shuffle = shuffle;
    }
        
    private void play(Song s)
    {
        if(mediaPlayer != null) //is is not null than stop old song
            mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(new Media(new File(s.getFilePath()).toURI().toString())); //Create instance of media player with song from argument
        mediaPlayer.onEndOfMediaProperty().setValue(new Runnable() //Sets event when the song ends to play next one
        {
            @Override
            public void run()
            {
                playNext();
            }
        });
        mediaPlayer.setVolume(volumePercentage);
        mediaPlayer.play();
        currentIndex = currentPlaylist.indexOf(s);
    }
    
    //Plays song from playlist on the given index, and stores the provided playlist as current playlist
    public void playPlaylist(List<Song> playlist,int index)
    {
        if(playlist != null && playlist.size() > 0)
        {
            currentPlaylist = playlist;
            shuffleCollection = new ShuffleCollection(playlist);
            play(currentPlaylist.get(index));
        }
    }
      
    public void setVolume(double percentage)
    {
        mediaPlayer.setVolume(percentage / 100);
        volumePercentage = percentage / 100;
    }
    
    public void setTime(double percentage)
    {
        if(mediaPlayer != null)
        {
            Duration dur = mediaPlayer.getMedia().getDuration();
            mediaPlayer.seek(dur.multiply(percentage/100));
        }
    }
    
    //returns current time of song in percentage
    public double getTimePercentage()
    {
        Duration dur = mediaPlayer.getMedia().getDuration();
        Duration currentTime = mediaPlayer.getCurrentTime();
        return currentTime.toMillis() / (dur.toMillis() / 100.0);
    }
    
    public String getFormatedCurrentTime()
    {
        return formatTime(mediaPlayer.getCurrentTime(),mediaPlayer.getMedia().getDuration());
    }
    
    //Formats time string from current duration
    private String formatTime(Duration elapsed, Duration all)
    {
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) 
            intElapsed -= elapsedHours * 60 * 60;
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 
                           - elapsedMinutes * 60;
 
        if (all.greaterThan(Duration.ZERO)) 
        {
            int intDuration = (int)Math.floor(all.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) 
               intDuration -= durationHours * 60 * 60;
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - 
                durationMinutes * 60;
            if (durationHours > 0) 
                return String.format("%d:%02d:%02d/%d:%02d:%02d", 
                   elapsedHours, elapsedMinutes, elapsedSeconds,
                   durationHours, durationMinutes, durationSeconds);
            else 
                return String.format("%02d:%02d/%02d:%02d",
                  elapsedMinutes, elapsedSeconds,durationMinutes, 
                      durationSeconds);
        } else 
        {
            if (elapsedHours > 0)
                return String.format("%d:%02d:%02d", elapsedHours, 
                       elapsedMinutes, elapsedSeconds);
            else
                return String.format("%02d:%02d",elapsedMinutes, 
                    elapsedSeconds);
        }
    }
    
    private ShuffleCollection getShuffleCollection()
    {
        if(currentPlaylist.size() != shuffleCollection.size())
            shuffleCollection = new ShuffleCollection(currentPlaylist);
        return shuffleCollection;
    }
    
    //indicates whether the playlist has reached an end
    private boolean reachedEnd()
    {
        return shuffle ? !getShuffleCollection().iterator.hasNext() : !(currentIndex < currentPlaylist.size() - 1);
    }
    
    //Plays next song in the palylist
    public Song playNext()
    {
        Song s = null;
        if(!reachedEnd()) //if hasn't reached and than just plays next song
            s = currentPlaylist.get(shuffle ? currentIndex = getShuffleCollection().next() : ++currentIndex);
        else if(repeat) //if has bud is on repeat than plays next song
            s = currentPlaylist.get(shuffle ? currentIndex = getShuffleCollection().next() : 0);
        else //if has reached end thanjust stop and return null
        { 
            if(isPlaying())
                mediaPlayer.stop();
            return null;
        }
        play(s);
        return s;
    }
    
    //Plays previous song
    public Song playPrev()
    {
        if (currentPlaylist != null && currentPlaylist.size() > 0)
        {
            if (currentIndex > 0)
            {
                Song s = currentPlaylist.get(--currentIndex);
                play(s);
                return s;
            }
            if (isPlaying())
            {
                mediaPlayer.stop();
            }
        }
        return null;
    }
    
    public void setRepeat(boolean repeat)
    {
        this.repeat = repeat;
    }
    
    public void play()
    {
        if(mediaPlayer != null)
            mediaPlayer.play();
    }
    
    public void stop()
    {
        if(mediaPlayer != null)
            mediaPlayer.stop();
    }
    
    public void pause()
    {
        if(mediaPlayer != null)
            mediaPlayer.pause();
    }
    
    public boolean isPlaying()
    {
        return mediaPlayer != null ? mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING : false;
    }
    
}
