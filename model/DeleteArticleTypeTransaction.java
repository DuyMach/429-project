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
public class DeleteArticleTypeTransaction extends Transaction {
	private String transactionStatusMessage = "";

	private ArticleTypeCollection articleTypeCollection;
    private ArticleType selectedArticleType;

	public DeleteArticleTypeTransaction() throws Exception {
		super();

        try {
            ArticleTypeCollection articleTypes = new ArticleTypeCollection();
            articleTypes.getColors();
            articleTypeCollection = (ArticleTypeCollection)articleTypes.getState("ColorCollection");
        }
        catch (Exception exc) {
            System.err.println(exc);
        }
	}

	//----------------------------------------------------------
	protected void setDependencies() {
		dependencies = new Properties();
        dependencies.setProperty("DoDeleteArticleType", "TransactionStatus");
		dependencies.setProperty("CancelArticleTypeCollection", "CancelTransaction");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of creating the account,
	 * verifying ownership, crediting, etc. etc.
	 */
	//----------------------------------------------------------
	public void processTransaction() {
        selectedArticleType.delete();
        selectedArticleType.update();
        transactionStatusMessage = (String)selectedArticleType.getState("UpdateStatusMessage");
        try {
            articleTypeCollection.getColors();
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
            case "ArticleTypeCollection":
                return articleTypeCollection;
            case "SelectedArticleType":
                return selectedArticleType;
            case "id":
            case "description":
            case "barcodePrefix":
            case "alphaCode":
                return selectedArticleType.getState(key);
            default:
                System.err.println("DeleteArticleTypeTransaction: invalid key for getState: "+key);
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
            case "DoDeleteArticleType":   // called from DeleteColorView on submit
                processTransaction();
                break;
            case "ArticleTypeSelected":
				selectedArticleType = new ArticleType((Properties)value);
				createAndShowDeleteArticleTypeView();
				break;
            case "CancelDeleteArticleType":
                swapToView(createView());
                break;
        }
		myRegistry.updateSubscribers(key, this);
	}

	protected Scene createView() {
        View newView = ViewFactory.createView("ArticleTypeCollectionView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("ArticleTypeCollectionView", currentScene);

        return currentScene;
	}

    protected void createAndShowDeleteArticleTypeView() {
        View newView = ViewFactory.createView("DeleteArticleTypeView", this);
        Scene currentScene = new Scene(newView);
        myViews.put("DeleteArticleTypeView", currentScene);
		swapToView(currentScene);
    }
}