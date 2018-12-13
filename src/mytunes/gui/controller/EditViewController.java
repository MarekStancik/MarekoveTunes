/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mytunes.be.Song;
import mytunes.dal.SongDAO;

/**
 * FXML Controller class
 *
 * @author mads_
 */
public class EditViewController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField titleField;
    @FXML
    private TextField artistField;
    @FXML
    private TextField timeField;
    @FXML
    private TextField fileField;
    
    private ControllerModel controllerModel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }    

    @FXML
    private void pressCancel(ActionEvent event) 
    {
        Stage st = (Stage) cancelButton.getScene().getWindow();
        st.close();
    }

    @FXML
    private void pressSave(ActionEvent event) 
    {
        //TODO
        Song s = new Song(titleField.getText(),artistField.getText(),fileField.getText(), "METAL"/*genreBox.getSelected()*/,50 );
        controllerModel.addSong(s);
        Stage st = (Stage) saveButton.getScene().getWindow();
        st.close();
    }

    @FXML
    private void chooseFile(ActionEvent event) throws IOException 
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileField.setText(fileChooser.showOpenDialog(((Node)event.getTarget()).getScene().getWindow()).getPath());      
    }
    
    public void setControllerModel(ControllerModel model)
    {
        controllerModel = model;
    }
    
    
}
