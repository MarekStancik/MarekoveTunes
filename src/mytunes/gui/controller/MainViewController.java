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
    private ListView<Song> listViewSongs;
    
    @FXML
    private ListView<Playlist> listViewPlaylists;
            
    @FXML
    private ListView<Song> listViewCurrentPlaylist;
    
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
    
    private ControllerModel controllerModel; 
    private Label txtPlayingName;
    @FXML
    private Rectangle rectCurrentlyPlaying;
    @FXML
    private Label labelCurrentlyPlaing;
    @FXML
    private ToggleButton btnRepeat;
    
    private Timeline timer;
    @FXML
    private Label labelCurrentTime;
    
    private ListView<Song> currentListview;
    @FXML
    private ToggleButton btnShuffle;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        Stage stage = new Stage();
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
        timer = new Timeline(new KeyFrame(Duration.seconds(0.3),ev -> 
        {
            if(controllerModel.isSongPlaying())
            {
                if(!sliderSongtime.isPressed() && !sliderSongtime.isValueChanging())
                    sliderSongtime.setValue(controllerModel.getTimePosition());
                labelCurrentTime.setText(controllerModel.getTimeString());
                if(currentListview.getSelectionModel().getSelectedIndex() != controllerModel.getCurrentIndex())
                {
                    currentListview.getSelectionModel().select(controllerModel.getCurrentIndex());
                    labelCurrentlyPlaing.setText(currentListview.getSelectionModel().getSelectedItem().getTitle());
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

    private void editPlaylist(ActionEvent event) throws IOException 
    {
/*        Stage s = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/PlaylistEditView.fxml"));
        s.setScene(new Scene(loader.load()));
        s.setTitle("Edit Playlist");
        PlaylistEditViewController questions = loader.<PlaylistEditViewController>getController();
        s.show();*/
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
        else 
            labelCurrentlyPlaing.setText("END");
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

    @FXML
    private void selectPlaylist(MouseEvent event) 
    {
        controllerModel.setSelectedPlaylistIndex(listViewPlaylists.getSelectionModel().getSelectedIndex());
        if(event.getButton().equals(MouseButton.PRIMARY))
            if(event.getClickCount() == 2 && listViewPlaylists.getSelectionModel().getSelectedIndex() > -1)
            {
                adaptSongLabel(controllerModel.getSelectedPlaylistSongs().get(0));
                controllerModel.playPlaylist(0);
                currentListview = listViewCurrentPlaylist;
            }
    }

    @FXML
    private void selectPlaylistSong(MouseEvent event) 
    {
        if(event.getButton().equals(MouseButton.PRIMARY))
            if(event.getClickCount() == 2 && listViewCurrentPlaylist.getSelectionModel().getSelectedIndex() > -1)
            {
                adaptSongLabel(listViewCurrentPlaylist.getSelectionModel().getSelectedItem());
                controllerModel.playPlaylist(listViewCurrentPlaylist.getSelectionModel().getSelectedIndex());
                currentListview = listViewCurrentPlaylist;
            }
    }

    @FXML
    private void pressStop(ActionEvent event) 
    {
        controllerModel.stopSong();
    }


    @FXML
    private void pressRepeat(ActionEvent event) 
    {
        controllerModel.setRepeat(btnRepeat.isSelected());
    }



    @FXML
    private void searchSongs(KeyEvent event) 
    {
        controllerModel.filterSongs(searchSongsField.getText());         
    }

    @FXML
    private void songsPlaylistSearch(KeyEvent event) 
    {
         controllerModel.filterPlaylistSongs(songsPlaylistSearchField.getText());      
    }

    @FXML
    private void playlistSearch(KeyEvent event) 
    {
        controllerModel.filterPlaylists(playlistSearchField.getText());        
    }

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

    @FXML
    private void deletePlaylistAction(ActionEvent event)
    {
        if(listViewPlaylists.getSelectionModel().getSelectedIndex() > -1)
        {
            controllerModel.setSelectedPlaylistIndex(listViewPlaylists.getSelectionModel().getSelectedIndex());
            controllerModel.deletePlaylist();
        }
    }

    @FXML
    private void addToPlaylistAction(ActionEvent event)
    {
        if(listViewSongs.getSelectionModel().getSelectedIndex() > -1)
            controllerModel.addSongToPlaylist(listViewSongs.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void deleteSongFromPlaylistAction(ActionEvent event)
    {
        if(listViewCurrentPlaylist.getSelectionModel().getSelectedIndex() > -1)
            controllerModel.deleteSongFromPlaylist(listViewCurrentPlaylist.getSelectionModel().getSelectedIndex());
    }

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
    
    private void setCurrentSong(Song s)
    {
        if(s != null)
        {
            controllerModel.playSong(s);
            labelCurrentlyPlaing.setText(s.getTitle());
        }
    }

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


    @FXML
    private void volumeDrag(MouseEvent event)
    {
        controllerModel.setVolume(sliderVolume.valueProperty().getValue());
    }
    
    @FXML
    private void timeDrag(MouseEvent event)
    {
        controllerModel.setTime(sliderSongtime.valueProperty().getValue());
    }

    @FXML
    private void editPlaylistAction(ActionEvent event)
    {
    }

    @FXML
    private void editSongAction(ActionEvent event)
    {
    }

    @FXML
    private void btnShuffleAction(ActionEvent event)
    {
        controllerModel.setShuffle(btnShuffle.isSelected());
    }

}
