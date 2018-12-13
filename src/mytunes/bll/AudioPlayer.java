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
public class AudioPlayer
{
    private class ShuffleCollection
    {
        private List<Integer> collection;
        private Iterator<Integer> iterator;
        private boolean hasEnded;
        
        public ShuffleCollection(List source)
        {
            if(source != null)
            {
                collection = new ArrayList();
                for (int i = 0; i < source.size(); i++)
                    collection.add(i);
                Collections.shuffle(collection);
                iterator = collection.iterator();
            }
        }
        
        public int next()
        {
            if(!iterator.hasNext())
            {
                Collections.shuffle(collection);
                iterator = collection.iterator();
            }
            return iterator.next();
        }
        
        public boolean hasEnded()
        {
            return hasEnded;
        }
        
        public int size()
        {
            return collection.size();
        }
    }
    
    private ShuffleCollection shuffleCollection;
    private MediaPlayer mediaPlayer;
    private List<Song> currentPlaylist;
    private boolean shuffle;
    private boolean repeat;
    private int currentIndex;
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
        if(mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(new Media(new File(s.getFilePath()).toURI().toString()));
        mediaPlayer.onEndOfMediaProperty().setValue(new Runnable()
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
    
    private boolean reachedEnd()
    {
        return shuffle ? !getShuffleCollection().iterator.hasNext() : !(currentIndex < currentPlaylist.size() - 1);
    }
    
    public Song playNext()
    {
        Song s = null;
        if(!reachedEnd())
            s = currentPlaylist.get(shuffle ? currentIndex = getShuffleCollection().next() : ++currentIndex);
        else if(repeat)
            s = currentPlaylist.get(shuffle ? currentIndex = getShuffleCollection().next() : 0);
        else
        { 
            if(isPlaying())
                mediaPlayer.stop();
            return null;
        }
        play(s);
        return s;
    }
    
    public Song playPrev()
    {
        if(currentIndex > 0)
        {
            Song s = currentPlaylist.get(--currentIndex);
            play(s);
            return s;
        }
        if(isPlaying())
            mediaPlayer.stop();
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
