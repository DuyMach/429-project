package userinterface;

import impresario.IModel;
import model.CheckoutInventoryItemTransaction;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model) {
		switch(viewName) {
			case "ClerkView":
				return new ClerkView(model);
			case "ColorCollectionView":
				return new ColorCollectionView(model);
			case "ModifyColorView":
				return new ModifyColorView(model);
			case "DeleteColorView":
				return new DeleteColorView(model);
			case "AddInventoryItemView":
				return new AddInventoryItemView(model);
			case "AddColorView":
				return new AddColorView(model);
			case "ArticleTypeCollectionView":
				return new ArticleTypeCollectionView(model);
			case "ConfirmDeleteArticleView":
				return new ConfirmDeleteArticleView(model);
			case "ArticleTypeView":
				return new ArticleTypeView(model);
			case "ModifyArticleTypeView":
				return new ModifyArticleTypeView(model);
			case "ScanBarcodeView":
				return new ScanBarcodeView(model);
			case "ModifyInventoryView":
				return new ModifyInventoryView(model);
			case "DeleteInventoryItemView":
				return new DeleteInventoryItemView(model);
			case "CheckoutInventoryItemView":
				return new CheckoutInventoryItemView(model);
			case "ListAvailableInventoryView":
				return new ListAvailableInventoryView(model);
			default:
				return null;
		}
	}


	/*
	public static Vector createVectorView(String viewName, IModel model)
	{
		if(viewName.equals("SOME VIEW NAME") == true)
		{
			//return [A NEW VECTOR VIEW OF THAT NAME TYPE]
		}
		else
			return null;
	}
	*/

}
