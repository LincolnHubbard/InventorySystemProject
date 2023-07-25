package com.hubbard.inventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * <p>The product class includes an ObservableList of associated parts</p>
 * @author Lincoln Hubbard
 */

public class Product {

    /**
     * The list of all parts associated with this product
     */
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * The id of the product
     */
    private int id;
    /**
     * The name of the product
     */
    private String name;
    /**
     * The price of the product
     */
    private double price;
    /**
     * The stock in inventory of the product
     */
    private int stock;
    /**
     * The minimum stock in inventory of the product
     */
    private int min;
    /**
     * The maximum stock in inventory of the product
     */
    private int max;

    /**
     * @param id id to set
     * @param name name to set
     * @param price price of product
     * @param stock number of product in inventory
     * @param min minimum number of product in inventory
     * @param max maximum number of product in inventory
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return The product ID
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The product name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The price of product
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price The price of product to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return The number of product in inventory
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock The number of product in inventory to set
     * <p>Not used, should still be included for further additions to
     * functionality</p>
     */
    public void setStock(int stock) {
        this.stock = stock;
    }
    /**
     * @return The minimum number of product in inventory
     */
    public int getMin() {
        return min;
    }
    /**
     * @param min The minimum number of product in inventory to set
     */
    public void setMin(int min) {
        this.min = min;
    }
    /**
     * @return The max number of product in inventory
     */
    public int getMax() {
        return max;
    }
    /**
     * @param max The maximum number of product in inventory to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @param partToAdd Product to be added to list of associated parts
     */
    public void addAssociatedPart(Part partToAdd){
        associatedParts.add(partToAdd);
    }

    /**
     * @param selectedAssociatedPart The part to delete (from list of associated parts)
     * @return If deletion was successful
     * <p>Not used, should still be included for further additions to
     *    functionality</p>
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
       return associatedParts.remove(selectedAssociatedPart); //look at this later (object) instead of (part)
    }

    /**
     * @return The list of all associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
