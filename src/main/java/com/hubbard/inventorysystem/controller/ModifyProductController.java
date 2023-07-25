package com.hubbard.inventorysystem.controller;

import com.hubbard.inventorysystem.model.Inventory;
import com.hubbard.inventorysystem.model.Part;
import com.hubbard.inventorysystem.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>Handles functionality of the Modify Product view</p>
 *
 * @author Lincoln Hubbard
 */

public class ModifyProductController implements Initializable {
    /**
     * Text field for the Product minimum amount
     */
    @FXML
    private TextField productMin;
    /**
     * Text field for the Product maximum amount
     */
    @FXML
    private TextField productMax;
    /**
     * Text field for the Product cost amount
     */
    @FXML
    private TextField productCost;
    /**
     * Text field for the Product name
     */
    @FXML
    private TextField productName;
    /**
     * Text field for the Product inventory amount
     */
    @FXML
    private TextField productInv;
    /**
     * Text field for the Product ID - User cannot edit
     */
    @FXML
    private TextField productId;

    /**
     * <p>Populates based on Parts added to Product</p>
     */
    @FXML
    private TableView<Part> associatedPartTableView;

    /**
     * <p>Populates with all parts in inventory</p>
     */
    @FXML
    private TableView<Part> partTableView;
    /**
     * Column for Part IDs
     */
    @FXML
    private TableColumn<Part, String> partIdColumn;
    /**
     * Column for Part inventory amounts
     */
    @FXML
    private TableColumn<Part, String> partInvColumn;
    /**
     * Column for Part names
     */
    @FXML
    private TableColumn<Part, String> partNameColumn;
    /**
     * Column for Part prices
     */
    @FXML
    private TableColumn<Part, String> partPriceColumn;

    /**
     * <p>Field searches as user types</p>
     */
    @FXML
    private TextField partSearchField;
    /**
     * Column for associated Part IDs
     */
    @FXML
    private TableColumn<Part, String> asPartIdColumn;
    /**
     * Column for associated Part names
     */
    @FXML
    private TableColumn<Part, String> asPartNameColumn;
    /**
     * Column for associated Part inventory amounts
     */
    @FXML
    private TableColumn<Part, String> asPartInvColumn;
    /**
     * Column for associated Part price amounts
     */
    @FXML
    private TableColumn<Part, String> asPartPriceColumn;

    /**
     * <p>Contains associated parts to be passed to Inventory object when product is saved</p>
     */
    private ObservableList<Part> partsToAdd = FXCollections.observableArrayList();

    /**
     * @see #onCancelProductButtonPressed <p>Calls this method to cancel pending changes</p>
     */
    @FXML
    private Button cancelProductButton;

    /**
     * @see #saveProduct <p>Calls this method to save Product changes to inventory</p>
     */
    @FXML
    private Button saveProductButton;

    /**
     * <p>Updates Part table view as user changes text in field</p>
     */
    public void onUserInput() {
        if (partSearchField.getText().isBlank()){
            updatePartTableView(Inventory.getAllParts());
            partTableView.getSelectionModel().clearSelection();
            return;
        }
        ObservableList<Part> bufferList = Inventory.lookUpPart( partSearchField.getText() );

        Integer id;

        if (bufferList.size() == 0){
            try{
                id = Integer.parseInt( partSearchField.getText() );
            }catch (Exception e){
                id = null;
            }
            if (id != null){
                Part partBuffer = Inventory.lookUpPart( id );
                if (partBuffer == null) {
                    bufferList.clear();
                    updatePartTableView( bufferList );
                    return;
                }
                bufferList.add( partBuffer );
                updatePartTableView( bufferList );
                return;
            }
        }

        updatePartTableView( bufferList );
    }

    /**
     * @see #onUserInput() <p>Called by this event</p>
     * @param listToDisplay Updated list of parts
     */
    public void updatePartTableView(ObservableList<Part> listToDisplay){
        partTableView.setItems(listToDisplay);

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTableView.setPlaceholder(new Label("No parts found"));

        if (partTableView.getItems().size() == 1){
            partTableView.getSelectionModel().select(0, partIdColumn );
        }
    }

    /**
     * <p>Updates based on user adding Parts to Product</p>
     * @param listToDisplay Updated list of associated parts
     */
    public void updateAsPartTableView(ObservableList<Part> listToDisplay){
        associatedPartTableView.setItems(listToDisplay);

        asPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        asPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        asPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        asPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartTableView.setPlaceholder(new Label("No parts selected"));
    }


    /**
     * <p>Cancels pending changes and returns user to main view</p>
     * Triggered by the cancel button
     */
    public void onCancelProductButtonPressed() {
        Stage stage = (Stage) cancelProductButton.getScene().getWindow();
        stage.close();
    }

    /**
     * <p>Populates text fields with values from selected product to Modify</p>
     * <p>Also populates associated Part table view</p>
     * @param productBuffer The product to be loaded from inventory
     */
    public void loadProductValues(Product productBuffer){
        assert productBuffer != null;

        productId.setText( String.valueOf( productBuffer.getId()));
        productName.setText(productBuffer.getName());
        productInv.setText(String.valueOf(productBuffer.getStock()));
        productCost.setText(Double.toString(productBuffer.getPrice()));
        productMax.setText(String.valueOf(productBuffer.getMax()));
        productMin.setText(String.valueOf(productBuffer.getMin()));
        partsToAdd = productBuffer.getAllAssociatedParts();

        updateAsPartTableView(partsToAdd);
    }

    /**
     * <p>Contains mix of try/catch and conditional statements to validate user input</p>
     * <p>List of associated parts is saved regardless of number of elements</p>
     * Triggered when save button is pushed
     */
    public void saveProduct() {
        int id = Integer.parseInt(productId.getText());
        String name;
        double price;
        int stock;
        int min;
        int max;
        int index = Inventory.getAllProducts().indexOf( Inventory.lookUpProduct( id));

        try{
            if (productName.getText().isBlank()){
                Inventory.inputError("Name is empty/invalid!");
                return;
            }else {
                name = productName.getText();
            }
        } catch (Exception e)
        {
            Inventory.inputError("Name is empty/invalid!");
            return;
        }

        try{
            stock = Integer.parseInt(productInv.getText());
        }catch (Exception e)
        {
            Inventory.inputError("Inv is empty/invalid!");
            return;
        }

        try{
            price = Double.parseDouble(productCost.getText());
        }catch (Exception e){
            Inventory.inputError("Price is empty/invalid!");
            return;
        }

        try{
            min = Integer.parseInt(productMin.getText());
        }catch (Exception e)
        {
            Inventory.inputError("Min is empty/invalid!");
            return;
        }

        try{
            max = Integer.parseInt(productMax.getText());
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

        Inventory.updateProduct( index, Inventory.initNewProduct( id, name, stock, price, min, max, partsToAdd));

        Stage stage = (Stage) saveProductButton.getScene().getWindow();
        stage.close();
    }

    /**
     * <p>Loading of product values and updating table views occurs in this method</p>
     * @param url Inherited from super
     * @param resourceBundle Inherited from super
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProductValues(Inventory.getAllProducts().get( Inventory.getInventoryIndex() ));
        updatePartTableView( Inventory.getAllParts());
        updateAsPartTableView(partsToAdd);
        associatedPartTableView.setPlaceholder(new Label("No parts selected"));
    }

    /**
     * <p>Adds selected part to the associated Parts list and table view</p>
     * Triggered by the Add button
     */
    public void addPartToTable() {
        if (partTableView.getSelectionModel().getSelectedItem() == null)
        {
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No part selected", ButtonType.CLOSE);
            noSelection.showAndWait();
            return;
        }
        Part partToMove = partTableView.getSelectionModel().getSelectedItem();
        ObservableList<Part> movePartBuffer = FXCollections.observableArrayList();
        movePartBuffer.add(partToMove);
        associatedPartTableView.setItems(movePartBuffer);
        partsToAdd.add(partToMove);
        updateAsPartTableView(partsToAdd);
    }

    /**
     * <p>Error window pops up if no part is selected. Confirmation window pops up to confirm removal of selected
     * part from Product</p>
     * Triggered by the Remove Associated Part button
     */
    public void removeAsPart() {
        if (associatedPartTableView.getSelectionModel().getSelectedItem() == null)
        {
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No part selected", ButtonType.CLOSE);
            noSelection.showAndWait();
            return;
        }

        if(confirmRemove()){
            Part partToRemove = associatedPartTableView.getSelectionModel().getSelectedItem();
            partsToAdd.remove(partToRemove);
            updateAsPartTableView(partsToAdd);
        }

    }

    /**
     * <p>Generates dialog window to confirm removal of Part</p>
     * @return True if user confirms removal false if dialog window is closed
     */
    public boolean confirmRemove(){
        Alert confirmRemoval = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part?");
        Optional<ButtonType> result = confirmRemoval.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
