package com.hubbard.inventorysystem.controller;

import com.hubbard.inventorysystem.model.InHouse;
import com.hubbard.inventorysystem.model.Inventory;
import com.hubbard.inventorysystem.model.Outsourced;
import com.hubbard.inventorysystem.model.Part;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>Handles functionality of the Modify Part view</p>
 *
 * @author Lincoln Hubbard
 */

public class ModifyPartController implements Initializable{

    /**
     * <p>Label changes depending on radio button selected and value loaded when modifying part</p>
     */
    @FXML
    private Label variablePartLabel;

    /**
     * @see #cancelModifyPart <p>Calls this method to cancel pending changes</p>
     */
    @FXML
    private Button cancelPartButton;
    /**
     * Radio button to select if part is Outsourced
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
     * Text field for the Part maximum amount
     */
    @FXML
    private TextField partMax;

    /**
     * <p>Value in this field changes depending on if InHouse or Outsourced Part is loaded.
     * Will also reset when user changes Part types</p>
     */
    @FXML
    private TextField partVariableField;
    /**
     * Text field for the Part minimum amount
     */
    @FXML
    private TextField partMin;

    /**
     * @see #savePart <p>Calls this method to save new Part to inventory</p>
     */
    @FXML
    private Button savePartButton;

    /**
     * <p>Field is disabled and ID is loaded from Part data</p>
     */
    @FXML
    private TextField partId;


    /**
     * <p>Part values are loaded in this method</p>
     * @param url Inherited from super
     * @param resourceBundle Inherited from super
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPartValues( Inventory.getAllParts().get( Inventory.getInventoryIndex() ) );
    }

    /**
     * <p>Cancels pending changes and returns user to main view</p>
     * <p>Triggered by the cancel button</p>
     */
    public void cancelModifyPart() {
        Stage stage = (Stage) cancelPartButton.getScene().getWindow();
        stage.close();
    }

    /**
     * <p>Will reset variablePartLabel field when user changes Part type</p>
     * Called when either radio button is selected
     */
    public void radioButtonChanged() {
        if(OutsourcedButton.isSelected()){
            variablePartLabel.setText("Company Name");
        }
        else {
            variablePartLabel.setText("Machine ID");
        }

        partVariableField.setText("");
    }

    /**
     * <p>Populates text fields with values from selected part to Modify</p>
     * <p>Uses temporary variable partBuffer to correctly populate Machine ID or Company Name </p>
     * @param partBuffer The part to be loaded from inventory
     */
    public void loadPartValues(Part partBuffer){
        assert partBuffer != null;


        partId.setText(String.valueOf( partBuffer.getId()));
        partName.setText( partBuffer.getName());
        partInv.setText(String.valueOf( partBuffer.getStock()));
        partCost.setText(Double.toString( partBuffer.getPrice()));
        partMax.setText(String.valueOf( partBuffer.getMax()));
        partMin.setText(String.valueOf( partBuffer.getMin()));
        if (partBuffer.getClass() == Outsourced.class){
            partVariableField.setText( ((Outsourced) partBuffer).getCompanyName());
            variablePartLabel.setText( "Company Name" );
            OutsourcedButton.setSelected(true);
        }
        else{
            partVariableField.setText( String.valueOf( ((InHouse) partBuffer).getMachineId() ) );
        }
    }

    /**
     * <p>Contains mix of try/catch and conditional statements to validate user input</p>
     * <p>Saves either InHouse or Outsourced Part to inventory depending on radio button selected</p>
     * Triggered when save button is pushed
     */
    public void savePart()  {
        boolean bIsOutsourced = OutsourcedButton.isSelected();
        int id = Integer.parseInt(partId.getText());
        String name;
        int stock;
        double price;
        int min;
        int max;
        Integer machineId = null; //So Intellij doesn't yell at me for not initializing
        String companyName = null;
        int index = Inventory.getAllParts().indexOf( Inventory.lookUpPart( id));
        Part partBuffer;

        try{
            if (partName.getText().isBlank()){
                Inventory.inputError("Name is empty/invalid!");
                return;
            }else {
                name = partName.getText();
            }
        }
        catch (Exception e)
        {
            Inventory.inputError("Name is empty/invalid!");
            return;
        }

        try{
            stock = Integer.parseInt(partInv.getText());
        }catch (Exception e)
        {
            Inventory.inputError("Inv is empty/invalid!");
            return;
        }

        try{
            price = Double.parseDouble(partCost.getText());
        }catch (Exception e){
            Inventory.inputError("Price is empty/invalid!");
            return;
        }

        try{
            min = Integer.parseInt(partMin.getText());
        }catch (Exception e)
        {
            Inventory.inputError("Min is empty/invalid!");
            return;
        }

        try{
            max = Integer.parseInt(partMax.getText());
        }catch (Exception e)
        {
            Inventory.inputError("Max is empty/invalid!");
            return;
        }

        if(max < min) {
            Inventory.inputError("Max cannot be less than Min!");
            return;
        }else if (stock < min || stock > max){
            Inventory.inputError("Inv must be between Min and Max!");
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

        if(bIsOutsourced){
            partBuffer = new Outsourced( id, name, price, stock, min, max, companyName );

        }
        else {
            partBuffer = new InHouse( id, name, price, stock, min, max, machineId );
        }

        Inventory.updatePart( index, partBuffer );
        Stage stage = (Stage) savePartButton.getScene().getWindow();
        stage.close();
    }
}
