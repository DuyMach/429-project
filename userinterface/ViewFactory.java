package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model) {
		switch(viewName) {
			case "ClerkView":
				return new ClerkView(model);
			case "ArticleTypeCollectionView":
				return new ArticleTypeCollectionView(model);
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
