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

/** The class containing the DeleteColorTransaction for the ATM application */
//==============================================================
public class DeleteColorTransaction extends Transaction {
	private String transactionStatusMessage = "";

	private ColorCollection colorCollection;
    private Color selectedColor;

	public DeleteColorTransaction() throws Exception {
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
        dependencies.setProperty("DoDeleteColor", "TransactionStatus");
		dependencies.setProperty("CancelColorCollection", "CancelTransaction");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of creating the account,
	 * verifying ownership, crediting, etc. etc.
	 */
	//----------------------------------------------------------
	public void processTransaction() {
        selectedColor.delete();
        selectedColor.update();
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
                System.err.println("DeleteColorTransaction: invalid key for getState: "+key);
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
            case "DoDeleteColor":   // called from DeleteColorView on submit
                processTransaction();
                break;
            case "ColorSelected":
				selectedColor = new Color((Properties)value);
				createAndShowDeleteColorView();
				break;
            case "CancelDeleteColor":
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

    protected void createAndShowDeleteColorView() {
        View newView = ViewFactory.createView("DeleteColorView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("DeleteColorView", currentScene);
		swapToView(currentScene);
    }
}