package com.hubbard.inventorysystem.model;

/**
 * <p>Contains Machine ID variable instead of Company Name</p>
 * @see Part <p>Superclass</p>
 * @author Lincoln Hubbard
 */
public class InHouse extends Part {

    /**
     * The machine id of this part, unique to this class
     */
    private int machineId;

    /**
     * @param id The ID to set
     * @param name The name to set
     * @param price The price to set
     * @param stock The number of this part in inventory
     * @param min The minimum The number of this part in inventory
     * @param max The maximum number of this part in inventory
     * @param machineId The ID of the machine this part is used for
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return The ID of the machine this part is used for
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId The ID of the machine this part is used for
     * <p>Not used, should still be included for further additions to
     * functionality</p>
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
