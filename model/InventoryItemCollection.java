package model;

import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

public class InventoryItemCollection extends EntityBase {
    private static final String myTableName = "Inventory";
    private Vector<InventoryItem> inventoryItemList;
    public InventoryItemCollection() {
        super(myTableName);
    }

    public void getInventoryItems() throws SQLException {
        inventoryItemList = new Vector<InventoryItem>();
        String query = "SELECT * FROM " + myTableName;
        Vector<Properties> result = getSelectQueryResult(query);
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                Properties nextInventoryItemData = result.elementAt(i);
                InventoryItem inventoryItem = new InventoryItem(nextInventoryItemData);
                if (inventoryItem != null) {
                    inventoryItemList.add(inventoryItem);
                }
            }
        }
    }

    public void getAvailableInventoryItems() throws SQLException {
        inventoryItemList = new Vector<InventoryItem>();
        String query = "SELECT * FROM " + myTableName + " WHERE status = \"Donated\"";
        Vector<Properties> result = getSelectQueryResult(query);
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                Properties nextInventoryItemData = result.elementAt(i);
                InventoryItem inventoryItem = new InventoryItem(nextInventoryItemData);
                if (inventoryItem != null) {
                    inventoryItemList.add(inventoryItem);
                }
            }
        }
    }

    public void display() {
        if (inventoryItemList.size() == 0) {
            System.out.println("No inventory items in collection");
        }
        else {
            for (int i = 0; i < inventoryItemList.size(); i++) {
                System.out.println(inventoryItemList.elementAt(i).toString());
            }
        }
    }

    public Object getState(String key) {
        if (key.equals("InventoryItems"))
            return inventoryItemList;
        else if (key.equals("InventoryItemCollection"))
            return this;
        return null;
    }

    public InventoryItem getInventoryItemWithBarcode(String barcode) {
        InventoryItem retValue = null;
        for (int i = 0; i < inventoryItemList.size(); i++) {
            InventoryItem nextInventoryItem = inventoryItemList.elementAt(i);
            String nextBarcode = (String)nextInventoryItem.getState("barcode");
            if (nextBarcode.equals(barcode) == true) {
                retValue = nextInventoryItem;
                return retValue;
            }
        }
        return retValue;
    }

    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
}