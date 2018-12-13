/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import mytunes.be.SimpleAudioPlayer;
import mytunes.be.Song;

/**
 *
 * @author Tothko
 */
public class PlayerModel implements IPlayerModel{
    String path;
    SimpleAudioPlayer SAP;

    public PlayerModel(String path) {
        this.path = path;
        SAP = new SimpleAudioPlayer();
        
    }
    
    public PlayerModel(){
        SAP = new SimpleAudioPlayer();
    }
    
    @Override
    public void play(Song s){
        SAP.play(s.getFilePath());
    }
    @Override
    public void stop(){
        SAP.stop();
    }
            
    @Override
    public void playNext(){
        SAP.playNext(path);
    }
    @Override
    public void playPrevious(){
        SAP.playPrevious(path);
    }
    
    public void pause(){
        SAP.pause();
    }

    
    
    
}
