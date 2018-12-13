/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author Marek
 */
public class LoginDialogController implements Initializable
{

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    @FXML
    private void txtKeyPressed(KeyEvent event)
    {
        if(event.getCode() == KeyCode.ENTER)
            btnLoginAction(null);
    }

    @FXML
    private void btnLoginAction(ActionEvent event)
    {
        if(!txtUsername.getText().isEmpty() && !txtPassword.getText().isEmpty())
        {
            Properties prop = new Properties();
            try(FileInputStream input = new FileInputStream("DbConnection.prop")){
            prop.load(input);
            }catch(IOException ex){
               Logger.getLogger(ConnectionProvider.class.getName()).log(Level.SEVERE, null, ex); 
            }
            ds.setDatabaseName(prop.getProperty("DbName"));
            ds.setUser(prop.getProperty("UserName"));
            ds.setPassword(prop.getProperty("Pass"));
            ds.setPortNumber(Integer.parseInt(prop.getProperty("PortNum")));
            ds.setServerName(prop.getProperty("ServerName"));
        }
    }
    
}
