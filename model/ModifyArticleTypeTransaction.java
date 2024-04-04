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
public class ModifyArticleTypeTransaction extends Transaction {
	private String transactionErrorMessage = "";

	private ArticleTypeCollection articleTypeCollection;
    
	private ArticleType articleTypeSelected;

	public ModifyArticleTypeTransaction() throws Exception {
		super();

        try {
            ArticleTypeCollection articleTypes = new ArticleTypeCollection();
            articleTypeCollection = (ArticleTypeCollection)articleTypes.getState("ArticleTypeCollection");
        }
        catch (Exception exc) {
            System.err.println(exc);
        }
	}

	//----------------------------------------------------------
	protected void setDependencies() {
		dependencies = new Properties();
		dependencies.setProperty("DoModifyArticleType", "TransactionError");
		dependencies.setProperty("CancelModifyArticleType", "CancelTransaction");
		dependencies.setProperty("OK", "CancelTransaction");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of creating the account,
	 * verifying ownership, crediting, etc. etc.
	 */
	//----------------------------------------------------------
	public void processTransaction(Properties props) {
        String description = props.getProperty("description");
        String alphaCode = props.getProperty("alphaCode");
		articleTypeCollection = new ArticleTypeCollection();

		if (!(alphaCode == null)) {
			articleTypeCollection.findArticleTypeAlphaCode(alphaCode);
			alphaCode = null;
		} else if (!(description == null)){
			articleTypeCollection.findArticleTypeDesc(description);
			description = null;
		} else if (description == null && alphaCode == null){
			articleTypeCollection.findArticleTypeDesc("");
		} else {
			articleTypeCollection.findArticleTypeBoth(alphaCode, description);
		}
		
	}

	//-----------------------------------------------------------
	public Object getState(String key) {
        switch (key) {
            case "TransactionError":
                return transactionErrorMessage;
            case "ArticleTypeCollection":
                return articleTypeCollection;
            case "Id":
				if (articleTypeSelected != null)
                	return articleTypeSelected.getState("Id");
				else return null;
            case "description":
				if (articleTypeSelected != null)
					return articleTypeSelected.getState("description");
				else return null;
            case "barcodePrefix":
				if (articleTypeSelected != null)
					return articleTypeSelected.getState("barcodePrefix");
				else return null;
            case "alphaCode":
				if (articleTypeSelected != null)
					return articleTypeSelected.getState("alphaCode");
				else return null;
            default:
                System.err.println("ModifyArticleTypeTransaction: invalid key for getState: " + key);
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
            case "DoModifyArticleType":   // gets called from ModifyColorTransactionView
                processTransaction((Properties)value);
                break;
			case "ArticleTypeSelected":
				articleTypeSelected = new ArticleType((Properties)value);
				createAndShowModifyArticleTypeView();
				break;
			case "Modify":
				modify((Properties)value);
				stateChangeRequest("DoModifyArticleType", value); //Don't question my authoritah
				createAndShowArticleTypeCollectionView();
            default:
                System.err.println("ModifyArticleTypeTransaction: invalid key for stateChangeRequest " + key);
        }
		myRegistry.updateSubscribers(key, this);
	}

	/**
	 * Create the view of this class. And then the super-class calls
	 * swapToView() to display the view in the stage
	 */
	//------------------------------------------------------
	protected Scene createView() {
		Scene currentScene = myViews.get("ArticleTypeCollectionView");

		if (currentScene == null) {
			// create our new view
			View newView = ViewFactory.createView("ArticleTypeCollectionView", this);
			currentScene = new Scene(newView);
			myViews.put("ArticleTypeCollectionView", currentScene);

			return currentScene;
		}
		else {
			return currentScene;
		}
	}

	public void createAndShowArticleTypeCollectionView() {
		View newView = ViewFactory.createView("ArticleTypeCollectionView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("ArticleTypeCollectionView", currentScene);
		swapToView(currentScene);
	}

	//------------------------------------------------------
	protected void createAndShowReceiptView() {
		// create our new view
		View newView = ViewFactory.createView("ModifyColorReceipt", this);
		Scene newScene = new Scene(newView);

		myViews.put("ModifyColorReceiptView", newScene);

		// make the view visible by installing it into the frame
		swapToView(newScene);
	}

	protected void createAndShowModifyArticleTypeView() {
        View newView = ViewFactory.createView("ModifyArticleTypeView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("ModifyArticleTypeView", currentScene);
		swapToView(currentScene);
    }

	protected void modify(Properties prop) {
		articleTypeSelected.modify(prop);
		articleTypeSelected.save();
	}
}