package com.hubbard.inventorysystem.model;
/**
 * <p>Contains Company Name variable instead of Machine ID</p>
 * @see Part <p>Superclass</p>
 * @author Lincoln Hubbard
 */
public class Outsourced extends Part {

    /**
     * The name of the manufacturing company - Unique to this class
     */
    private String companyName;

    /**
     * @param id The ID to set
     * @param name The name to set
     * @param price The price to set
     * @param stock The number of this part in inventory
     * @param min The minimum The number of this part in inventory
     * @param max The maximum number of this part in inventory
     * @param companyName The name of company who manufactured part
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return The name of company who manufactured part
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName The ID of the machine this part is used for
     * <p>Not used, should still be included for further additions to
     * functionality</p>
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
