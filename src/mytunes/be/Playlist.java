/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tothko
 */
public class Playlist {

    private int ID;
    private List<Integer> songIds;
    private String name;
    
    public Playlist()
    {
        songIds = getSongIds();
    }
    
    public Playlist(int ID, String name) {
        this.ID = ID;
        this.name = name;
        songIds = getSongIds();
    }

    public int getID() 
    {
        return ID;
    }
    
    public void setID(int id) 
    {
        ID = id;
    }
    
    public List<Integer> getSongIds()
    {
        return songIds == null ? songIds = new ArrayList() : songIds;
    }

    public void setSongIds(List<Integer> songIds) 
    {
        this.songIds = songIds;
    }
    
    public void addSongId(int id)
    {
        if(!songIds.contains(id))
            songIds.add(id);
    }
    
    public void removeSongId(int id)
    {
        if(songIds.contains(id))
            songIds.remove((Integer)id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ID + ". " + name;
    }
    

}
