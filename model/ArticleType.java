// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the Account for the ATM application */
//==============================================================
public class ArticleType extends EntityBase implements IView
{
	private static final String myTableName = "ArticleType";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";

	public ArticleType(){
		super(myTableName);
		setDependencies();
	}


	// constructor for this class
	//----------------------------------------------------------
	public ArticleType(String barcodePrefix)
			throws InvalidPrimaryKeyException
	{
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (barcodePrefix = " + barcodePrefix + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one book at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one book. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple ArticleType matching barcode : "
						+ barcodePrefix + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedAccountData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedAccountData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedAccountData.getProperty(nextKey);
					// bookId = Integer.parseInt(retrievedAccountData.getProperty("bookId"));

					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
					}
				}

			}
		}
		// If no book found for this id, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("No account matching id : "
					+ barcodePrefix + " found.");
		}
	}

	// Can also be used to create a NEW Book (if the system it is part of
	// allows for a new account to be set up)
	//----------------------------------------------------------
	public ArticleType(Properties props)
	{
		super(myTableName);

		setDependencies();
		persistentState = new Properties();
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);


			if (nextValue != null)
			{
				persistentState.setProperty(nextKey, nextValue);
			}
		}
	}

	public String toString()
	{
		setDependencies();
		return "Title: " + persistentState.getProperty("bookTitle") + "; Author: " +
			persistentState.getProperty("author") + "; Year: " +
			persistentState.getProperty("pubYear") ;
	}

	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();

		dependencies.setProperty("BookCollection", "BookCollectionMessage");
		myRegistry.setDependencies(dependencies);
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;

		return persistentState.getProperty(key);
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{

		myRegistry.updateSubscribers(key, this);
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
	}

	//-----------------------------------------------------------------------------------
	public void save() // save()
	{
		updateStateInDatabase();
	}

	//-----------------------------------------------------------------------------------
	private void updateStateInDatabase()
	{
		try
		{
			if (persistentState.getProperty("id") != null)
			{
				// update
				Properties whereClause = new Properties();
				whereClause.setProperty("id",
						persistentState.getProperty("id"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "ArticleType data for id : " + persistentState.getProperty("id") + " updated successfully in database!";
			}
			else
			{
				// insert
				Integer id =
						insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("id", "" + id.intValue());
				updateStatusMessage = "ArticleType data for new ArticleType : " +  persistentState.getProperty("id")
						+ "installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing book data in database!";
		}
		//DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
	}


	//Displays book information
	//--------------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("id"));
		v.addElement(persistentState.getProperty("description"));
		v.addElement(persistentState.getProperty("barcodePrefix"));
		v.addElement(persistentState.getProperty("alphaCode"));
		v.addElement(persistentState.getProperty("status"));

		return v;
	}

	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}

	//-----------------------------------------------------------------------------------
	public static int compare(ArticleType a, ArticleType b)
	{
		String aNum = (String)a.getState("barcodePrefix");
		String bNum = (String)b.getState("barcodePrefix");

		return aNum.compareTo(bNum);
	}

	public void modify(Properties props) {
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }
}