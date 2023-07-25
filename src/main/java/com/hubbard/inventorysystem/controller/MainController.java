package com.hubbard.inventorysystem.controller;

import com.hubbard.inventorysystem.model.Inventory;
import com.hubbard.inventorysystem.model.Part;
import com.hubbard.inventorysystem.model.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>This class contains all functionality for the main screen, including creating an Inventory object
 * for use in the program</p>
 * @author Lincoln Hubbard
 */
public class MainController implements Initializable {

    /**
     * Column of Part IDs in the Part table view
     */
    @FXML
    private TableColumn<Part, String> partIdColumn;
    /**
     * Column of Part names in the Part table view
     */
    @FXML
    private TableColumn<Part, String> partNameColumn;
    /**
     * Column of Part inventory amounts in the Part table view
     */
    @FXML
    private TableColumn<Part, String> partInvColumn;
    /**
     * Column of Part prices in the Part table view
     */
    @FXML
    private TableColumn<Part, String> partPriceColumn;
    /**
     * Column of Product IDs in the Part table view
     */
    @FXML
    private TableColumn<Product, String> productIdColumn;
    /**
     * Column of Product names in the Part table view
     */
    @FXML
    private TableColumn<Product, String> productNameColumn;
    /**
     * Column of Part inventory amounts in the Part table view
     */
    @FXML
    private TableColumn<Product, String> productInvColumn;
    /**
     * Column of Product prices in the Part table view
     */
    @FXML
    private TableColumn<Product, String> productPriceColumn;

    /**
     * <p>Populates with all parts in inventory</p>
     */
    @FXML
    private TableView<Part> partTableView;

    /**
     * <p>Populates with all products in inventory</p>
     */
    @FXML
    private TableView<Product> productTableView;
    /**
     * <p>Field searches as user types</p>
     */
    @FXML
    private TextField partSearchField;

    /**
     * <p>Field searches as user types</p>
     */
    @FXML
    private TextField productSearchField;

    /**
     * <p>Can easily be extended in future to support new views or controllers </p>
     * @see #addNewView(ViewToAdd) <p>Used in this method for file path and passing Invetory object</p>
     */
    public enum ViewToAdd {
        ADD_PART, MODIFY_PART, ADD_PRODUCT, MODIFY_PRODUCT
    }

    /**
     * <p>Exits program</p>
     */
    public void onExitButtonPressed(){
        Alert confirmExit = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit");
        Optional<ButtonType> result = confirmExit.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            javafx.application.Platform.exit();
        }
    }

    /**
     * Triggered when the Add button is pressed under the Part table view
     * @throws IOException If file path is incorrect
     */
    public void onAddPartPressed() throws IOException {
        addNewView(ViewToAdd.ADD_PART);
    }

    /**
     * <p>Generates error dialog window if no part is selected</p>
     * <p>Triggered when the Modify button is pressed under the Part table view</p>
     * @throws IOException If file path is incorrect
     */
    public void onModifyPartPressed() throws IOException {
        if (partTableView.getSelectionModel().getSelectedItem() == null)
        {
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No part selected", ButtonType.CLOSE);
            noSelection.showAndWait();
            return;
        }
        addNewView(ViewToAdd.MODIFY_PART);
    }

    /**
     * <p>Triggered when the Add button is pressed under the Product table view</p>
     * @throws IOException If file path is incorrect
     */
    public void onAddProductPressed() throws IOException {
        addNewView(ViewToAdd.ADD_PRODUCT);
    }

    /**
     * <p>Generates error dialog window if no product is selected</p>
     * @throws IOException If file path is incorrect
     */
    public void onModifyProductPressed() throws IOException {
        if (productTableView.getSelectionModel().getSelectedItem() == null)
        {
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No product selected", ButtonType.CLOSE);
            noSelection.showAndWait();
            return;
        }
        addNewView(ViewToAdd.MODIFY_PRODUCT);
    }

    /**
     * <p>Generates error dialog if no part is selected, and confirms deletion before calling method to remove part</p>
     */
    public void onDeletePartPressed(){
        if (partTableView.getSelectionModel().getSelectedItem() == null)
        {
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No part selected", ButtonType.CLOSE);
            noSelection.showAndWait();
            return;
        }

        if (confirmDelete()){
            Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
            if(Inventory.deletePart(selectedPart)){
                updatePartTableView(Inventory.getAllParts());
            }
        }
    }


    /**
     * <p>Generates error dialog if no product is selected or if product has associated parts,
     * and confirms deletion before calling method to
     * remove product</p>
     */
    public void onDeleteProductPressed(){
        if (productTableView.getSelectionModel().getSelectedItem() == null)
        {
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No product selected", ButtonType.CLOSE);
            noSelection.showAndWait();
            return;
        }

        if (productTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().size() != 0) {
            Alert noSelection = new Alert(Alert.AlertType.ERROR,
                                          "Cannot delete: Product has associated parts!", ButtonType.CLOSE);
            noSelection.showAndWait();
            return;
        }

        if(confirmDelete()){
            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
            if(Inventory.deleteProduct((selectedProduct))){
                updateProductTableView(Inventory.getAllProducts());
            }
        }
    }

    /**
     * <p>Updates Part table view as user changes text in field</p>
     */
    public void onPartUserInput() {
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
     * <p>Updates Product table view as user changes text in field</p>
     */
    public void onProductUserInput() {
        if (productSearchField.getText().isBlank()){
            updateProductTableView(Inventory.getAllProducts());
            productTableView.getSelectionModel().clearSelection();
            return;
        }
        ObservableList<Product> bufferList = Inventory.lookUpProduct( productSearchField.getText() );
        Integer id;

        if (bufferList.size() == 0){
            try{
                id = Integer.parseInt( productSearchField.getText() );
            }catch (Exception e){
                id = null;
            }
            if (id != null){
                Product productBuffer = Inventory.lookUpProduct( id );
                if (productBuffer == null) {
                    bufferList.clear();
                    updateProductTableView( bufferList );
                    return;
                }
                bufferList.add( productBuffer );
                updateProductTableView( bufferList );
                return;
            }
        }

        updateProductTableView( bufferList );

    }

    /**
     * <p>Used to load in test data and populate table views on program start</p>
     * @param url Inherited from super
     * @param resourceBundle Inherited from super
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Inventory.initializePartList();
        Inventory.initializeProductList();
        updatePartTableView(Inventory.getAllParts());
        updateProductTableView(Inventory.getAllProducts());
    }

    /**
     * @see #onPartUserInput() <p>Called by this event</p>
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
     * @see #onProductUserInput() <p>Called by this event</p>
     * @param listToDisplay Updated list of products
     */
    public void updateProductTableView(ObservableList<Product> listToDisplay){
        productTableView.setItems(listToDisplay);

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setPlaceholder(new Label("No products found"));

        if (productTableView.getItems().size() == 1){
            productTableView.getSelectionModel().select(0, productIdColumn );
        }
    }

    /**
     * <p>RUNTIME_ERROR was having issue in this function loading different files paths for each view,
     * solved by using file path from source root and concatenating rest of path based on enum value</p>
     * @see ViewToAdd <p>Use this enum</p>
     * @param view Matching ViewToAdd Enum type needed to call function
     * @throws IOException If file path(s) are incorrect
     */
    public void addNewView(ViewToAdd view) throws IOException {
        String viewFilePath = "/com/hubbard/inventorysystem/";
        String viewFileName = null;
        String viewName = null;
        int selectionId;

        switch(view){
            case ADD_PART -> {
                viewFileName = "add-part.fxml";
                viewName = "Add Part";
            }
            case MODIFY_PART -> {
                viewFileName = "modify-part.fxml";
                viewName = "Modify Part";
                selectionId = partTableView.getSelectionModel().getSelectedItem().getId();
                Inventory.setInventoryIndex( Inventory.getAllParts().indexOf( Inventory.lookUpPart(selectionId) ) );
            }
            case ADD_PRODUCT -> {
                viewFileName = "add-product.fxml";
                viewName = "Add Product";
            }
            case MODIFY_PRODUCT -> {
                viewFileName = "modify-product.fxml";
                viewName = "Modify Product";
                selectionId = productTableView.getSelectionModel().getSelectedItem().getId();
                Inventory.setInventoryIndex( Inventory.getAllProducts().indexOf( Inventory.lookUpProduct(selectionId) ) );
            }
        }

        assert viewFileName != null;

        FXMLLoader fxmlLoader = new FXMLLoader( getClass().getResource(viewFilePath + viewFileName) );
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle(viewName);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }


    /**
     * <p>Generates dialog window to confirm deletion of Part/Product</p>
     * @return True if user confirms deletion - false if dialog window is closed
     */
    public boolean confirmDelete(){
        Alert confirmDeletion = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?");
        Optional<ButtonType> result = confirmDeletion.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }
}