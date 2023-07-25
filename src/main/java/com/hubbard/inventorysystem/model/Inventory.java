package com.hubbard.inventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * <p>Contains all functions for managing company inventory</p>
 * @author Lincoln Hubbard
 */
public class Inventory {

     /**
     * Contains all parts int the inventory
     */
     private static final ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * Contains all products in the inventory
     */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Used to track which part/product to load when modifying
     */
     private static int inventoryIndex;

    /**
     * <p>Used to pass new Product object into methods when updating inventory</p>
     * @see Product#Product(int, String, double, int, int, int) <p>Params are inherited from constructor</p>
     * @param id The id to set
     * @param name The name to set
     * @param price The price of product to set
     * @param stock The number of product in inventory to set
     * @param min The minimum number of product in inventory to set
     * @param max The maximum number of product in inventory to set
     * @param partsToAdd Can be empty
     * @return New product to be added/updated
     */
    public static Product initNewProduct( int id, String name, int stock, double price, int min, int max,
                                   ObservableList<Part> partsToAdd ) {
        Product productBuffer = new Product( id, name, price, stock, min, max );
        if (!partsToAdd.isEmpty()) {
            for (Part indexPart : partsToAdd) {
                productBuffer.addAssociatedPart( indexPart );
            }
        }
        return productBuffer;
    }

    /**
     * <p>RUNTIME_ERROR Was getting errors when trying to (Cast) to the correct Part subclass, fixed by
     * implementing a <code>bIsOutsourced</code> boolean variable in the AddPartController and ModifyPartController
     * classes</p>
     *
     * @param newPart The part to be added into inventory
     */
    public static void addPart( Part newPart ) {
        allParts.add( newPart );
    }

    /**
     * @see #initNewProduct(int, String, int, double, int, int, ObservableList) <p>Use first to
     * return Product object to pass into *this* method</p>
     * @param newProduct The product to be added into inventory
     */
    public  static void addProduct( Product newProduct ) {
        allProducts.add( newProduct );
    }


    /**
     * @see #lookUpPart(int) <p>Use this method if Part object needs to be returned</p>
     * @param partQuery Part ID or name to search inventory for
     * @return An array of matching parts (ID or Name only)
     */
    public static ObservableList<Part> lookUpPart( String partQuery ) {

        ObservableList<Part> bufferList = FXCollections.observableArrayList();
        for (Part indexedPart : getAllParts()) {
            if (indexedPart.getName().toLowerCase().contains( partQuery.toLowerCase() )) {
                bufferList.add( indexedPart );
            }
        }
        return bufferList;
    }

    /**
     * @see #lookUpPart(String) <p>Use this method if populating tables with search bar</p>
     * @param idQuery Part ID for part to be returned
     * @return A part object matching ID
     */
    public static Part lookUpPart( int idQuery ) {

        for (Part indexedPart : getAllParts()) {
            if (indexedPart.getId() == idQuery) {
                return indexedPart;
            }
        }
        return null;
    }

    /**
     * @see #lookUpProduct(int) <p>Use this method if Product object needs to be returned</p>
     * @param productQuery Product ID or name to search inventory for
     * @return An array of matching products (ID or Name only)
     */
    public static ObservableList<Product> lookUpProduct( String productQuery ) {

        ObservableList<Product> bufferList = FXCollections.observableArrayList();

        for (Product indexedProduct : getAllProducts()) {
            if (indexedProduct.getName().toLowerCase().contains( productQuery.toLowerCase() ) ||
                Integer.toString( indexedProduct.getId() ).contains( productQuery )) {
                bufferList.add( indexedProduct );
            }
        }

        return bufferList;
    }

    /**
     * @see #lookUpProduct(String) <p>Use this method if populating tables with search bar</p>
     * @param idQuery Product ID for part to be returned
     * @return A product object matching ID
     */
    public static Product lookUpProduct( int idQuery ){

        for (Product indexedProduct : getAllProducts()) {
            if (indexedProduct.getId() == idQuery) {
                return indexedProduct;
            }
        }
        return null;
    }

    /**
     * <p>RUNTIME_ERROR Had issues updating part values until I found the <code>set()</code> method in the JavaFX 8 API
     * documentation</p>
     * @see ObservableList#set(int, Object) <p>Uses this method to update values</p>
     * @param index The index of the part to update
     * @param selectedPart Used to pass in values to update part with
     */
    public static void updatePart( int index, Part selectedPart ){
        allParts.set( index, selectedPart );
    }

    /**
     * @see ObservableList#set(int, Object) <p>Uses this method to update values</p>
     * @param index The index of the product to update
     * @param selectedProduct Used to pass in values to update product with
     */
    public static void updateProduct( int index, Product selectedProduct ){
        allProducts.set( index, selectedProduct );
    }

    /**
     * @param selectedPart The part to remove from inventory
     * @return If deletion was successful
     */
    public static boolean deletePart( Part selectedPart ){ return allParts.remove( selectedPart ) ;}

    /**
     * @param selectedProduct The product to remove from inventory
     * @return If deletion was successful
     */
    public static boolean deleteProduct(Product selectedProduct) { return allProducts.remove(selectedProduct); }

    /**
     * @return A list of all parts in inventory
     */
    public static ObservableList<Part> getAllParts() { return allParts; }

    /**
     * @return A list of all products in inventory
     */
    public static ObservableList<Product> getAllProducts() { return allProducts; }

    /**
     * <p>Generates default part values for testing</p>
     */
    public static void initializePartList() {
        allParts.add(new InHouse(1, "Screw", 15.00, 2, 1,15, 20));
        allParts.add(new InHouse(2, "Bolt", 10.00, 1, 1,15, 12));
        allParts.add(new Outsourced(3, "Wrench", 10.00, 1, 1,15, "Good Boy"));
    }

    /**
     * <p>Generates default product values for testing</p>
     */
    public  static void initializeProductList(){
         allProducts.add(new Product(1,"Bicycle", 140.00, 1, 0, 10));
         allProducts.add(new Product(2,"Xbox", 500.00, 1, 1, 15));
    }

    /**
     * @return An automatically generated Part ID
     */
    public static int generateAutoPartId(){
        return allParts.get(allParts.size() - 1).getId() + 1;
    }

    /**
     * @return An automatically generated Product ID
     */
    public static int generateAutoProductId(){
        return allProducts.get(allProducts.size() - 1).getId() + 1;
    }

    /**
     * @return The index of the part or product selected to be modified
     */
    public static int getInventoryIndex(){
        return inventoryIndex;
    }

    /**
     * <p>Used to set the index of the part/product to be modified</p>
     * @param inventoryIndex The index of the part or product selected to be modified
     */
    public static void setInventoryIndex( int inventoryIndex ){
        Inventory.inventoryIndex = inventoryIndex;
    }

    /**
     * <p>Displays when validating user input</p>
     * @param errorMsg Error message to display
     */
    public static void inputError(String errorMsg){
        Alert noSelection = new Alert( Alert.AlertType.ERROR, (errorMsg),
                                       ButtonType.CLOSE);
        noSelection.showAndWait();
    }

}
