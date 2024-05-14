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

/** The class containing the ModifyArticleTypeTransaction for the ATM application */
//==============================================================
public class ModifyArticleTypeTransaction extends Transaction {
	private String transactionStatusMessage = "";

	private ArticleTypeCollection articleTypeCollection;
	private ArticleType selectedArticleType;
	private String description;
    private String alphaCode;    


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
		dependencies.setProperty("DoModifyArticleType", "TransactionStatus");
		dependencies.setProperty("CancelModifyArticleType", "ArticleTypeCollection");
		dependencies.setProperty("CancelArticleTypeCollection", "CancelTransaction");

		myRegistry.setDependencies(dependencies);
	}

	//----------------------------------------------------------
	protected void processTransaction(Properties props) {
        selectedArticleType.modify(props);
		selectedArticleType.save();
		transactionStatusMessage = (String)selectedArticleType.getState("UpdateStatusMessage");
	}

	protected void processArticleTypeSearch(Properties props) {
        description = props.getProperty("description");
        alphaCode = props.getProperty("alphaCode");
		articleTypeCollection = new ArticleTypeCollection();

		if (alphaCode != null && description == null) {
			articleTypeCollection.findArticleTypeAlphaCode(alphaCode);
		} else if (description != null && alphaCode == null){
			articleTypeCollection.findArticleTypeDesc(description);
		} else if (description != null && alphaCode != null){
            articleTypeCollection.findArticleTypeBoth(alphaCode, description);
        } else {
			articleTypeCollection.findArticleTypeDesc("");
		}
	}

	//-----------------------------------------------------------
	public Object getState(String key) {
        switch (key) {
            case "TransactionStatus":
                return transactionStatusMessage;
            case "ArticleTypeCollection":
                return articleTypeCollection;
			case "id":
			case "description":
			case "barcodePrefix":
			case "alphaCode":
				return selectedArticleType.getState(key);
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
            case "ArticleTypeSearch":   // gets called from ModifyColorTransactionView
                processArticleTypeSearch((Properties)value);
                break;
			case "ArticleTypeSelected":
				selectedArticleType = new ArticleType((Properties)value);
				createAndShowModifyArticleTypeView();
				break;
			case "CancelModifyArticleType":
				swapToView(createView());
				Properties props = new Properties();
				if (description != null)
					props.setProperty("description", description);
				if (alphaCode != null)
					props.setProperty("alphaCode", alphaCode);
				processArticleTypeSearch(props);
                break;
			case "DoModifyArticleType":
				processTransaction((Properties)value);
				break;
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

	protected void createAndShowModifyArticleTypeView() {
        View newView = ViewFactory.createView("ModifyArticleTypeView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("ModifyArticleTypeView", currentScene);
		swapToView(currentScene);
    }
}