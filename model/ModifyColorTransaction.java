// specify the package
package model;

// system imports
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the ModifyColorTransaction for the ATM application */
//==============================================================
public class ModifyColorTransaction extends Transaction {
	private String transactionStatusMessage = "";

	private ColorCollection colorCollection;
    private Color selectedColor;

	public ModifyColorTransaction() throws Exception {
		super();

        try {
            ColorCollection colors = new ColorCollection();
            colors.getColors();
            colorCollection = (ColorCollection)colors.getState("ColorCollection");
        }
        catch (Exception exc) {
            System.err.println(exc);
        }
	}

	//----------------------------------------------------------
	protected void setDependencies() {
		dependencies = new Properties();
        dependencies.setProperty("DoModifyColor", "TransactionStatus");
		dependencies.setProperty("CancelColorCollection", "CancelTransaction");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of creating the account,
	 * verifying ownership, crediting, etc. etc.
	 */
	//----------------------------------------------------------
	public void processTransaction(Properties props) {
        selectedColor.modify(props);
        transactionStatusMessage = (String)selectedColor.getState("UpdateStatusMessage");
        try {
            colorCollection.getColors();
        }
        catch (Exception exc) {
            System.err.println(exc);
        }
	}

	//-----------------------------------------------------------
	public Object getState(String key) {
        switch (key) {
            case "TransactionStatus":
                return transactionStatusMessage;
            case "ColorCollection":
                return colorCollection;
            case "SelectedColor":
                return selectedColor;
            case "id":
            case "description":
            case "barcodePrefix":
            case "alphaCode":
                return selectedColor.getState(key);
            default:
                System.err.println("ModifyColorTransaction: invalid key for getState: "+key);
                break;
		}
		return null;
	}

	//-----------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
		switch(key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "DoModifyColor":   // called from ModifyColorView on submit
                processTransaction((Properties)value);
                break;
            case "ColorSelected":
				selectedColor = new Color((Properties)value);
				createAndShowModifyColorView();
				break;
            case "CancelModifyColor":
                swapToView(createView());
                break;
        }
		myRegistry.updateSubscribers(key, this);
	}

	protected Scene createView() {
        View newView = ViewFactory.createView("ColorCollectionView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("ColorCollectionView", currentScene);

        return currentScene;
	}

    protected void createAndShowModifyColorView() {
        View newView = ViewFactory.createView("ModifyColorView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("ModifyColorView", currentScene);
		swapToView(currentScene);
    }

	//------------------------------------------------------
	protected  void createAndShowReceiptView() {
		// create our new view
		View newView = ViewFactory.createView("ModifyColorReceipt", this);
		Scene newScene = new Scene(newView);

		myViews.put("ModifyColorReceiptView", newScene);

		// make the view visible by installing it into the frame
		swapToView(newScene);
	}
}