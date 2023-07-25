package com.hubbard.inventorysystem.controller;

import com.hubbard.inventorysystem.model.InHouse;
import com.hubbard.inventorysystem.model.Inventory;
import com.hubbard.inventorysystem.model.Outsourced;
import com.hubbard.inventorysystem.model.Part;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * <p>Handles functionality of the Add Part view</p>
 *
 * @author Lincoln Hubbard
 */
public class AddPartController {


    /**
     * <p>Label changes depending on radio button selected</p>
     */
    @FXML
    private Label variablePartLabel;

    /**
     * @see #cancelAddPart <p>Calls this method to cancel pending changes</p>
     */
    @FXML
    private Button cancelPartButton;

    /**
     * @see #savePart <p>Value is read when this method is called</p>
     */
    @FXML
    private RadioButton OutsourcedButton;
    /**
     * Text field for the Part name
     */
    @FXML
    private TextField partName;
    /**
     * Text field for the Part inventory amount
     */
    @FXML
    private TextField partInv;
    /**
     * Text field for the Part cost
     */
    @FXML
    private TextField partCost;
    /**
     * Text field for the Part max amount
     */
    @FXML
    private TextField partMax;
    /**
     * Text field for Machine ID/Company name
     */
    @FXML
    private TextField partVariableField;
    /**
     * Text field for the Part min amount
     */
    @FXML
    private TextField partMin;
    /**
     * @see #savePart <p>Calls this method to save new Part to inventory</p>
     */
    @FXML
    private Button savePartButton;

    /**
     * <p>Cancels pending changes and returns user to main view</p>
     * Triggered by the cancel button
     */
    public void cancelAddPart(){
        Stage stage = (Stage) cancelPartButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Called when either radio button is selected
     */
    public void radioButtonChanged(){
        if (OutsourcedButton.isSelected()) {
            variablePartLabel.setText( "Company Name" );
        } else {
            variablePartLabel.setText( "Machine ID" );
        }
    }

    /**
     * <p>Contains mix of try/catch and conditional statements to validate user input</p>
     * <p>Saves either InHouse or Outsourced Part to inventory depending on radio button selected</p>
     * Triggered when save button is pushed
     */
    public void savePart(){
        boolean bIsOutsourced = OutsourcedButton.isSelected();
        int    id = Inventory.generateAutoPartId();
        String name;
        int    stock;
        double price;
        int    min;
        int    max;
        Integer machineId = null; //So Intellij doesn't yell at me for not initializing
        String companyName = null;
        Part   partBuffer;

        try {
            if (partName.getText().isBlank()) {
                Inventory.inputError( "Name is empty/invalid!" );
                return;
            } else {
                name = partName.getText();
            }
        } catch (Exception e) {
            Inventory.inputError( "Name is empty/invalid!" );
            return;
        }

        try {
            stock = Integer.parseInt( partInv.getText() );
        } catch (Exception e) {
            Inventory.inputError( "Inv is empty/invalid!" );
            return;
        }

        try {
            price = Double.parseDouble( partCost.getText() );
        } catch (Exception e) {
            Inventory.inputError( "Price is empty/invalid!" );
            return;
        }

        try {
            min = Integer.parseInt( partMin.getText() );
        } catch (Exception e) {
            Inventory.inputError( "Min is empty/invalid!" );
            return;
        }

        try {
            max = Integer.parseInt( partMax.getText() );
        } catch (Exception e) {
            Inventory.inputError( "Max is empty/invalid!" );
            return;
        }

        if (max < min) {
            Inventory.inputError( "Max cannot be less than Min!" );
            return;
        } else if (stock < min || stock > max) {
            Inventory.inputError( "Inv must be between Min and Max!" );
            return;
        }

        if (!bIsOutsourced){
            try {
                machineId = Integer.parseInt( partVariableField.getText());
            }catch (Exception e){
                Inventory.inputError( variablePartLabel.getText() + " is empty/invalid!" );
                return;
            }
        }else {
            try {
                if (partVariableField.getText().isBlank()) {
                    Inventory.inputError( variablePartLabel.getText() + " is empty/invalid!" );
                    return;
                } else {
                    companyName = partVariableField.getText();
                }
            } catch (Exception e) {
                Inventory.inputError( variablePartLabel.getText() + "Inv is empty/invalid!" );
                return;
            }
        }


        if (bIsOutsourced) {
            partBuffer = new Outsourced( id, name, price, stock, min, max, companyName);
        } else {
            partBuffer = new InHouse( id, name, price, stock, min, max, machineId );
        }

        Inventory.addPart( partBuffer );

        Stage stage = (Stage) savePartButton.getScene().getWindow();
        stage.close();
    }

}
