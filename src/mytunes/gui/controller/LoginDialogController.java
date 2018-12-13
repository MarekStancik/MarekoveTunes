/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

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
    private TextField txtDbName;
    @FXML
    private PasswordField txtPass;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("DbConnection.prop"))
        {
            prop.load(input);
            txtUsername.setText(prop.getProperty("UserName"));// getProperty("UserName"));
            txtPass.setText(prop.getProperty("Pass"));
            txtDbName.setText(prop.getProperty("DbName"));
        } catch (IOException ex)
        {
            Logger.getLogger(LoginDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void txtKeyPressed(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            btnLoginAction(null);
        }
    }

    @FXML
    private void btnLoginAction(ActionEvent event)
    {
        if (!txtUsername.getText().isEmpty() && !txtPass.getText().isEmpty() && !txtDbName.getText().isEmpty())
        {
            Properties prop = new Properties();
            try (FileInputStream input = new FileInputStream("DbConnection.prop"))
            {
                prop.load(input);
                input.close();
                prop.setProperty("UserName", txtUsername.getText());// getProperty("UserName"));
                prop.setProperty("Pass", txtPass.getText());
                prop.setProperty("DbName", txtDbName.getText());
                FileOutputStream output = new FileOutputStream("DbConnection.prop");
                prop.store(output, null);
            } catch (IOException ex)
            {
                Logger.getLogger(LoginDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Stage st = (Stage) txtUsername.getScene().getWindow();
            st.close();
        }
    }

}
