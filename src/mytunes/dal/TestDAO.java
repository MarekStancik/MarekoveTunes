/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import mytunes.be.Song;

/**
 *
 * @author Marek
 */
public class TestDAO {

    public static void main(String[] kokot) throws FileNotFoundException, IOException, SQLException {
        /*
        SongDAO dao = new SongDAO();
        List<Song> songs = dao.getAllSongs();
        for (Song song : songs)
        {
            System.out.println(song);
        }
        Song s = dao.getSong(1);
        System.out.println(s);
         *//*

        File file = new File("music\\A file.txt");
        String path = file.getPath();
        System.out.println(path);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            System.out.println(st);
        }*/
       //  LocalDbProvider.getLocalConnection("testdb.db").close();
        
    }

}
