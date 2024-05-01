// specify the package
package model;

// system imports
import javafx.stage.Stage;
import model.ArticleType;
import model.Transaction;
import javafx.scene.Scene;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;

public class ModifyInventoryTransaction extends Transaction{
    private String transactionErrorMessage = "";
    private String transactionStatusMessage = "";

    private String barcode;
    private InventoryItem item = new InventoryItem();
    private ArticleType articleType = new ArticleType();
    private Color color1;
    private Color color2;


    public ModifyInventoryTransaction() throws Exception {
        super();
    }

    @Override
    protected void setDependencies() {
        dependencies = new Properties();
		dependencies.setProperty("DoModifyInventory", "TransactionError");
		dependencies.setProperty("CancelModifyInventory", "CancelTransaction");
        dependencies.setProperty("DoScanBarcode", "TransactionError");
        dependencies.setProperty("CancelScanBarcode", "CancelTransaction");
		myRegistry.setDependencies(dependencies);
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("ScanBarcodeView");

		if (currentScene == null) {
			// create our new view
			View newView = ViewFactory.createView("ScanBarcodeView", this);
			currentScene = new Scene(newView);
			myViews.put("ScanBarcodeView", currentScene);

			return currentScene;
		}
		else {
			return currentScene;
		}
    }

    @Override
    public Object getState(String key) {
        switch (key) {
            case "TransactionStatus":
                return transactionStatusMessage;
            case "TransactionError":
                return transactionErrorMessage;
            case "articleTypeDesc":
                return articleType.getState("description");
            case "color1Desc":
                return color1.getState("description");
            case "color2Desc":
                if (color2 != null) {
                    return color2.getState("description");
                }
            case "barcode":
            case "size":
            case "gender":
            case "brand":
            case "notes":
            case "status":
            case "donorLastName":
            case "donorFirstName":
            case "donorPhone":
            case "donorEmail":
            case "receiverNetid":
            case "receiverLastName":
            case "receiverFirstName":
            case "dateDonated":
            case "dateTaken":
                return item.getState(key);
            default:
                System.err.println("ModifyInventoryTransaction: invalid key for getState: "+key);
                break;
		}
		return null;
	}

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "CancelModifyInventory":
                swapToView(createView());
                break;
            case "DoScanBarcode":
                boolean isFound = false;
                isFound = scanBarcode((Properties) value);
                if (isFound) {
                    transactionErrorMessage = "";
                    createAndShowModifyInventoryView();
                } else {
                    transactionErrorMessage = "No Inventory Item found";
                }
                break;
            case "Modify":
                modify((Properties) value);
                Properties barcodeProp = new Properties();
                barcodeProp.setProperty("barcode", barcode);
                scanBarcode(barcodeProp);
                createAndShowModifyInventoryView();
                transactionStatusMessage = "Successfully Modified";
                break;
            case "CancelModifyInventoryItem":
                swapToView(createView());
                break;
            case "CancelScanBarcode":
                
                break;
            default:
                System.err.println("ModifyInventoryTransaction: invalid key for stateChangeRequest " + key);
        }
		myRegistry.updateSubscribers(key, this);
    }

    public boolean scanBarcode(Properties prop){
        barcode = prop.getProperty("barcode");

        try {
            item = new InventoryItem(barcode);
            item.update();

            articleType = articleType.findArticleTypeById((String)item.getState("articleTypeId"));

            color1 = new Color((String)item.getState("color1Id"));

            if ( !(((String) item.getState("color2Id")) == null) ) {
                color2 = new Color((String)item.getState("color2Id"));
            }
            transactionStatusMessage = (String)item.getState("UpdateStatusMessage");
            return true;
        } catch (InvalidPrimaryKeyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public void createAndShowModifyInventoryView() {
		View newView = ViewFactory.createView("ModifyInventoryView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("ModifyInventoryView", currentScene);
		swapToView(currentScene);
	}

    public void createAndShowClerkView() {
		View newView = ViewFactory.createView("ClerkView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("ClerkView", currentScene);
		swapToView(currentScene);
	}
    
    protected void modify(Properties prop) {
        String barcode = (String) item.getState("barcode");
        String articleTypeId = (String) item.getState("articleTypeId");
        String color1 = (String) item.getState("color1Id");
        String color2 = (String) item.getState("color2Id");

        String receiverFirstName = (String) item.getState("receiverFirstName");
        String receiverLastName = (String) item.getState("receiverLastName");
        String receiverNetId = (String) item.getState("receiverNetId");
        String dateDonated = (String) item.getState("dateDonated");
        String dateTaken = (String) item.getState("dateTaken");
        String statusString = (String) item.getState("status");
        String barcodeString = (String) item.getState("barcode");

        if (!(receiverFirstName == null)) {
            if (!receiverFirstName.isEmpty())
                prop.setProperty("receiverFirstName", receiverFirstName);
        }

        if (!(receiverLastName == null)) {
            if (!receiverLastName.isEmpty())
                prop.setProperty("receiverLastName", receiverLastName);
        }

        if (receiverNetId != null) {
            if (!receiverNetId.isEmpty()) {
                prop.setProperty("receiverNetId", receiverNetId);
            }
        }

        prop.setProperty("status", statusString);
        prop.setProperty("barcode", barcodeString);
        prop.setProperty("dateDonated", dateDonated);

        prop.setProperty("barcode", barcode);
        prop.setProperty("color1Id", color1);
        prop.setProperty("color2Id", color2);
        prop.setProperty("articleTypeId", articleTypeId);

        item.removeItem();

        InventoryItem tempItem = new InventoryItem(prop);
        tempItem.save();
        item = tempItem;
        barcode = (String) item.getState("barcode");
	}
}
