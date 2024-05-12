package model;

import javafx.scene.Scene;
import java.util.Properties;

import userinterface.View;
import userinterface.ViewFactory;

public class ListAvailableInventoryTransaction extends Transaction {
    private String transactionStatusMessage = "";
    private String transactionErrorMessage = "";

    private InventoryItemCollection inventoryItemCollection;

    public ListAvailableInventoryTransaction() throws Exception {
        super();

        try {
            inventoryItemCollection = new InventoryItemCollection();
            inventoryItemCollection.getAvailableInventoryItems();
        }
        catch (Exception exc) {
            System.err.println(exc);
        }
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelListAvailableInventory", "CancelTransaction");
        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {
        switch (key) {
            case "TransactionStatus":
                return transactionStatusMessage;
            case "TransactionError":
                return transactionErrorMessage;
            case "InventoryItemCollection":
                return inventoryItemCollection;
            default:
                System.err.println("ListAvailableInventoryTransaction: invalid key for getState: "+key);
                break;
        }
        return null;
    }

    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob":
                doYourJob();
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    protected Scene createView() {
        View newView = ViewFactory.createView("ListAvailableInventoryView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("ListAvailableInventoryView", currentScene);
        return currentScene;
    }
}