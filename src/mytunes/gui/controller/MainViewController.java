/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author mads_
 */
public class MainViewController implements Initializable
{
    @FXML
    private ListView<Song> listViewSongs; //ListVIew with all songs
    
    @FXML
    private ListView<Playlist> listViewPlaylists; //ListView with all playlists
            
    @FXML
    private ListView<Song> listViewCurrentPlaylist; //listview that Shows songs in currently chosen playlist
    
    @FXML
    private Button buttonPrevious;
    @FXML
    private Button buttonPlay;
    @FXML
    private Button buttonNext;
    @FXML
    private Slider sliderChannels;
    @FXML
    private Slider sliderVolume;
    @FXML
    private Slider sliderSongtime;
    @FXML
    private Button buttonStop;
    @FXML
    private Button buttonRepeat;
    
    @FXML
    private TextField searchSongsField;
    @FXML
    private TextField songsPlaylistSearchField;
    @FXML
    private TextField playlistSearchField;
    
    @FXML
    private Rectangle rectCurrentlyPlaying;
    @FXML
    private Label labelCurrentlyPlaing;
    @FXML
    private ToggleButton btnRepeat;
    
    @FXML
    private Label labelCurrentTime;
    
    @FXML
    private ToggleButton btnShuffle;
    
    private ControllerModel controllerModel;  
    private ListView<Song> currentListview;
    private Timeline timer; //Timer that adapts slider
   
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        Stage stage = new Stage();          //Shows login window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/LoginDialog.fxml"));
        try
        {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException ex)
        {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setTitle("Login Dialog");
        stage.showAndWait();
        
        controllerModel = new ControllerModel(); 
        
        timer = new Timeline(new KeyFrame(Duration.seconds(0.3),ev ->  //Creating event on timer
        {
            if(controllerModel.isSongPlaying())
            {
                if(!sliderSongtime.isPressed() && !sliderSongtime.isValueChanging())
                    sliderSongtime.setValue(controllerModel.getTimePosition());
                labelCurrentTime.setText(controllerModel.getTimeString());
                if(currentListview.getSelectionModel().getSelectedIndex() != controllerModel.getCurrentIndex()) //if next song is playing than change the title and selection in listview
                {
                    currentListview.getSelectionModel().select(controllerModel.getCurrentIndex());
                    adaptSongLabel(currentListview.getSelectionModel().getSelectedItem());
                }
            }            
            else 
                labelCurrentTime.setText("00:00/00:00");
        }));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
        
        sliderVolume.setValue(100);
        listViewSongs.setItems(controllerModel.getSongList());
        listViewPlaylists.setItems(controllerModel.getPlaylists());
        listViewCurrentPlaylist.setItems(controllerModel.getSelectedPlaylistSongs());
    }

    private void editSong(ActionEvent event) throws IOException 
    {/*
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/EditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Edit Song");
        EditViewController songEdit = loader.<EditViewController>getController();
        stage.show();*/
    }

    @FXML
    private void pressPrevious(ActionEvent event) 
    {
        adaptSongLabel(controllerModel.playPrev());
    }
    
    private void adaptSongLabel(Song s)
    {
        if(s != null)
        {
            labelCurrentlyPlaing.setText(s.getTitle());
        }
     //   else 
       //     labelCurrentlyPlaing.setText("END");
    }

    @FXML
    private void pressPlay(ActionEvent event) 
    {
        controllerModel.pressPlay();
        
    }

    @FXML
    private void pressNext(ActionEvent event) 
    {
        adaptSongLabel(controllerModel.playNext());
    }

    //On Listveiw with Playlists click
    @FXML
    private void selectPlaylist(MouseEvent event) 
    {
        if(event.getButton().equals(MouseButton.PRIMARY))  //On double click
        {
            controllerModel.setSelectedPlaylistIndex(listViewPlaylists.getSelectionModel().getSelectedIndex());
            if(event.getClickCount() == 2 && listViewPlaylists.getSelectionModel().getSelectedIndex() > -1)
            {
                if(listViewCurrentPlaylist.getItems().size() > 0)   // if list is not empty - was throwing exceptions when it was getting song of index 0
                    adaptSongLabel(controllerModel.getSelectedPlaylistSongs().get(0));
                controllerModel.playPlaylist(0); //Play setted playlist from the beggining
                currentListview = listViewCurrentPlaylist;
            }
        }
    }

    //On listview with songs in current playlist click
    @FXML
    private void selectPlaylistSong(MouseEvent event) 
    {
        if(event.getButton().equals(MouseButton.PRIMARY)) //On double click
        {
            if(event.getClickCount() == 2 && listViewCurrentPlaylist.getSelectionModel().getSelectedIndex() > -1)
            {
                adaptSongLabel(listViewCurrentPlaylist.getSelectionModel().getSelectedItem()); //Change the currently playing label
                controllerModel.playPlaylist(listViewCurrentPlaylist.getSelectionModel().getSelectedIndex()); //Plays selected playlist from selected index
                currentListview = listViewCurrentPlaylist;
            }
        }
    }

    @FXML
    private void pressStop(ActionEvent event) 
    {
        controllerModel.stopSong();
    }

    //Sets the audioPlayer so it woould repeat the playlist if it comes to the end of playlist
    @FXML
    private void pressRepeat(ActionEvent event) 
    {
        controllerModel.setRepeat(btnRepeat.isSelected());
    }

    //On changing text in textBox - Searches by title of song in all songs list
    @FXML
    private void searchSongs(KeyEvent event) 
    {
        controllerModel.filterSongs(searchSongsField.getText());         
    }
    
    //On changing text in textBox - Searches by title of the song in selected playlist
    @FXML
    private void songsPlaylistSearch(KeyEvent event) 
    {
         controllerModel.filterPlaylistSongs(songsPlaylistSearchField.getText());      
    }

    //On changing text in textBox - Searches by name of the playlist in all playlists
    @FXML
    private void playlistSearch(KeyEvent event) 
    {
        controllerModel.filterPlaylists(playlistSearchField.getText());        
    }

    //On new playlist button click - Loads the fxml dialog for creating new playlist
    @FXML
    private void newPlaylistAction(ActionEvent event) throws IOException
    {
        Stage s = new Stage(); 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/PlaylistEditView.fxml"));
        s.setScene(new Scene(loader.load()));
        s.setTitle("New Playlist");
        loader.<PlaylistEditViewController>getController().setControllerModel(controllerModel);
        s.showAndWait();
    }

    //On delete playlist button click - Deletes selected playlist
    @FXML
    private void deletePlaylistAction(ActionEvent event)
    {
        if(listViewPlaylists.getSelectionModel().getSelectedIndex() > -1)
        {
            controllerModel.setSelectedPlaylistIndex(listViewPlaylists.getSelectionModel().getSelectedIndex());
            controllerModel.deletePlaylist();
        }
    }

    //On add to playlist button click - Adds song to currently selected playlist
    @FXML
    private void addToPlaylistAction(ActionEvent event)
    {
        if(listViewSongs.getSelectionModel().getSelectedIndex() > -1)
            controllerModel.addSongToPlaylist(listViewSongs.getSelectionModel().getSelectedIndex());
    }
    
    //Deletes song from currentlySelectedPlaylist
    @FXML
    private void deleteSongFromPlaylistAction(ActionEvent event)
    {
        if(listViewCurrentPlaylist.getSelectionModel().getSelectedIndex() > -1)
            controllerModel.deleteSongFromPlaylist(listViewCurrentPlaylist.getSelectionModel().getSelectedIndex());
    }

    //On double click plays song from all songs list
    @FXML
    private void allSongsMouseClick(MouseEvent event)
    {
        if(event.getButton().equals(MouseButton.PRIMARY))
        {
            if(event.getClickCount() == 2 && listViewSongs.getSelectionModel().getSelectedIndex() > -1)
            {
                controllerModel.playSong(listViewSongs.getSelectionModel().getSelectedItem());
                adaptSongLabel(listViewSongs.getSelectionModel().getSelectedItem());    
                currentListview = listViewSongs;
            }
        }
    }

    //On button new song click -- Opens dialog for new song creation
    @FXML
    private void newSongAction(ActionEvent event) throws IOException
    {
        Stage s = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/EditView.fxml"));
        s.setScene(new Scene(loader.load()));
        s.setTitle("New Song");
        loader.<EditViewController>getController().setControllerModel(controllerModel);
        s.showAndWait();
    }

    //On delete song button click - deletes song form list and database
    @FXML
    private void deleteSongAction(ActionEvent event)
    {
        if(listViewSongs.getSelectionModel().getSelectedIndex() > -1)
            controllerModel.deleteSong(listViewSongs.getSelectionModel().getSelectedItem() );
    }

    @FXML
    private void newSong(ActionEvent event)
    {
    }

    //On slider with volume action
    @FXML
    private void volumeDrag(MouseEvent event)
    {
        controllerModel.setVolume(sliderVolume.valueProperty().getValue());
    }
    
    //on slider with time action
    @FXML
    private void timeDrag(MouseEvent event)
    {
        controllerModel.setTime(sliderSongtime.valueProperty().getValue());
    }

    
    @FXML
    private void editPlaylistAction(ActionEvent event) throws IOException
    {
        if (listViewPlaylists.getSelectionModel().getSelectedIndex() > -1)
        {
            Stage s = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/PlaylistEditView.fxml"));
            s.setScene(new Scene(loader.load()));
            s.setTitle("Edit Playlist");
            loader.<PlaylistEditViewController>getController().setControllerModelWithPlaylist(controllerModel, listViewPlaylists.getSelectionModel().getSelectedItem());
            s.showAndWait();
            listViewPlaylists.refresh();
        }
    }

    @FXML
    private void editSongAction(ActionEvent event)
    {
    }

    //Sets whether playlist should play in random order
    @FXML
    private void btnShuffleAction(ActionEvent event)
    {
        controllerModel.setShuffle(btnShuffle.isSelected());
    }

}
