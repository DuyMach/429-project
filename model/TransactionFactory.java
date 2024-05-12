// specify the package
package model;

// system imports
import userinterface.AddInventoryItemView;

import java.util.Vector;
import javax.swing.JFrame;

import model.ModifyInventoryTransaction;

/*Class constructor is called in clerk's doTransaction
 * - creates a returns corresponding transaction based on type
 */
public class TransactionFactory {
	public static Transaction createTransaction(String transType) throws Exception {
		Transaction retValue = null;
		switch (transType) {
            case "AddColor":
                 retValue = new AddColorTransaction();
                break;
            case "ModifyColor":
                retValue = new ModifyColorTransaction();
                break;
            case "DeleteColor":
                retValue = new DeleteColorTransaction();
                break;
            case "DeleteArticleType":
                retValue = new DeleteArticleTypeTransaction();
                break;
            case "AddArticleType":
                retValue = new AddArticleTypeTransaction();
                break;
            case "ModifyArticleType":
                retValue = new ModifyArticleTypeTransaction();
                break;
            case "AddInventory":
                retValue = new AddInventoryItemTransaction();
                break;
            case "ModifyInventory":
                retValue = new ModifyInventoryTransaction();
                break;
            case "DeleteInventory":
                retValue = new DeleteInventoryItemTransaction();
                break;
            case "CheckoutInventory":
                retValue = new CheckoutInventoryItemTransaction();
                break;
            case "ListAvailableInventory":
                retValue = new ListAvailableInventoryTransaction();
                break;
            default:
                System.err.println("Invalid transaction type");
        }
		return retValue;
	}
}