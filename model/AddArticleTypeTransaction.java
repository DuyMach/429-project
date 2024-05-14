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
public class AddArticleTypeTransaction extends Transaction {
	private String transactionStatusMessage = "";

	private ArticleType articleType;
    private String id;
    private String description;
    private String barcodePrefix;
    private String alphaCode;

	public AddArticleTypeTransaction() throws Exception {
		super();

        try {
            articleType = new ArticleType();
        }
        catch (Exception exc) {
            System.err.println(exc);
        }
	}

	//----------------------------------------------------------
	protected void setDependencies() {
		dependencies = new Properties();
		dependencies.setProperty("DoAddArticleType", "TransactionStatus");
		dependencies.setProperty("CancelAddArticleType", "CancelTransaction");
		dependencies.setProperty("OK", "CancelTransaction");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of creating the account,
	 * verifying ownership, crediting, etc. etc.
	 */
	//----------------------------------------------------------
	public void processTransaction(Properties props) {
		id = props.getProperty("id");
        description = props.getProperty("description");
        barcodePrefix = props.getProperty("barcodePrefix");
        alphaCode = props.getProperty("alphaCode");
		articleType = new ArticleType(props);
		articleType.save();
		transactionStatusMessage = (String)articleType.getState("UpdateStatusMessage");
	}

	//-----------------------------------------------------------
	public Object getState(String key) {
        switch (key) {
            case "TransactionStatus":
                return transactionStatusMessage;
            case "Id":
                return id;
            case "Description":
                return description;
            case "BarcodePrefix":
                return barcodePrefix;
            case "AlphaCode":
                return alphaCode;
            default:
                System.err.println("ModifyColorTransaction: invalid key for getState: "+ key);
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
            case "DoAddArticleType":
                processTransaction((Properties)value);
                break;
			case "CancelAddArticleType":
                swapToView(createView());
                break;
            default:
                System.err.println("ModifyColorTransaction: invalid key for stateChangeRequest");
        }
		myRegistry.updateSubscribers(key, this);
	}

	/**
	 * Create the view of this class. And then the super-class calls
	 * swapToView() to display the view in the stage
	 */
	//------------------------------------------------------
	protected Scene createView() {
		Scene currentScene = myViews.get("ArticleTypeView");

		if (currentScene == null) {
			// create our new view
			View newView = ViewFactory.createView("ArticleTypeView", this);
			currentScene = new Scene(newView);
			myViews.put("ArticleTypeView", currentScene);

			return currentScene;
		}
		else {
			return currentScene;
		}
	}
}