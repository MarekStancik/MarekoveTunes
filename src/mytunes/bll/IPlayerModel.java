/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import mytunes.be.Song;

/**
 *
 * @author Tothko
 */
public interface IPlayerModel {
    void play(Song s);
    void stop();
    void playNext();
    void playPrevious();
}
