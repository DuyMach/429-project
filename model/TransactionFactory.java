// specify the package
package model;

// system imports
import java.util.Vector;
import javax.swing.JFrame;

public class TransactionFactory {
	public static Transaction createTransaction(String transType) throws Exception {
		Transaction retValue = null;
		switch (transType) {
            case "AddColor":
                // retValue = new AddColorTransaction();
                break;
            case "ModifyColor":
                retValue = new ModifyColorTransaction();
                break;
            case "DeleteColor":
                // retValue = new DeleteColorTransaction();
                break;
            default:
                System.err.println("Invalid transaction type");
        }
		return retValue;
	}
}