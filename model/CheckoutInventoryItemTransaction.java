// specify the package
package model;

// system imports
import javafx.stage.Stage;
import model.ArticleType;
import model.Transaction;
import javafx.scene.Scene;
// import javafx.scene.paint.Color;

import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;

public class CheckoutInventoryItemTransaction extends Transaction{
    private String transactionStatusMessage = "";

    private String barcode;
    private InventoryItem item;
    private ArticleType articleType;
    private Color color1;
    private Color color2;

    public CheckoutInventoryItemTransaction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("DoScanBarcode", "TransactionStatus");
        dependencies.setProperty("DoCheckoutInventoryItem", "TransactionStatus");
        dependencies.setProperty("CancelCheckoutInventoryItem", "TransactionStatus");
        dependencies.setProperty("CancelScanBarcode", "CancelTransaction");

		myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("ScanBarcodeView");

		if (currentScene == null) {
			// create our new view
			View newView = ViewFactory.createView("ScanBarcodeView", this);
			currentScene = new Scene(newView);
			myViews.put("ScanBarcodeView", currentScene);
        }

        return currentScene;
    }

    public Object getState(String key) {
        try {
            if (key.startsWith("color1")) {
                String colorKey = key.substring(6,7).toLowerCase()+key.substring(7);
                return color1.getState(colorKey);
            }
            else if (key.startsWith("color2")) {
                String colorKey = key.substring(6,7).toLowerCase()+key.substring(7);
                return color2.getState(colorKey);
            }
            else if (key.startsWith("articleType")) {
                String articleTypeKey = key.substring(11,12).toLowerCase()+key.substring(12);
                return articleType.getState(articleTypeKey);
            }
            else if (key.equals("TransactionStatus")) {
                return transactionStatusMessage;
            }
            else if (key.equals("barcode")) {
                return barcode;
            }
            else {
                return item.getState(key);
            }
        }
        catch (Exception exc) {
            System.err.println("CheckoutInventoryItemTransaction: invalid key for getState: "+key);
            return null;
        }  
	}

    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "CancelCheckoutInventoryItem":
                transactionStatusMessage = "";
                swapToView(createView());
                break;
            case "DoScanBarcode":
                processBarcodeScanned((Properties)value);
                break;
            case "DoCheckoutInventoryItem":
                processTransaction((Properties)value);
                break;
            default:
                System.err.println("CheckoutInventoryItemTransaction: invalid key for stateChangeRequest " + key);
        }
		myRegistry.updateSubscribers(key, this);
    }
    
    private void processBarcodeScanned(Properties props) {
        barcode = props.getProperty("barcode");

		try {
            item = new InventoryItem();
            item.getDonatedInventoryItemByBarcode(barcode);
            articleType = new ArticleType();
            articleType.getArticleTypeById((String)item.getState("articleTypeId"));
            color1 = new Color((String)item.getState("color1Id"));

            String color2IdString = (String)item.getState("color2Id");
            if (color2IdString != null) {
                color2 = new Color((String)item.getState("color2Id"));
            } else {
                // If color2Id is null (empty) then set to inactive color
                color2 = new Color("22");
            }
            transactionStatusMessage = "";
            createAndShowCheckoutInventoryItemView();
        }
        catch (Exception exc) {
            transactionStatusMessage = exc.getMessage();
        }
	}

    private void processTransaction(Properties props) {
        try {
            item.modify(props);
            item.update();
            transactionStatusMessage = "Item successfully checked out!";
        }
        catch (Exception exc) {
            transactionStatusMessage = exc.getMessage();
        }   
    }

    private void createAndShowCheckoutInventoryItemView() {
        View newView = ViewFactory.createView("CheckoutInventoryItemView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("ClerkView", currentScene);
				
		swapToView(currentScene);
	}
}