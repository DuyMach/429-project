package model;

//import packages
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

//===================================================================
public class InventoryItem extends EntityBase implements IView {
    
    //Declare string of table name to reference
    private static final String myTableName = "Inventory";

    //Properties Object declared
    protected Properties dependencies;

    //Guii Components
    private String updateStatusMessage = "";

    //------------------------------------------------------------
    /*CONSTRUCTOR
     * Constructor to pull existing record from table
     * Takes barcode value
     */
    public InventoryItem(String barcode) throws InvalidPrimaryKeyException{
        super(myTableName);

        setDependencies();

        String query = "SELECT * FROM " + myTableName + " WHERE (barcode = " + barcode + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        
        //If value from table isn't null...
        if (allDataRetrieved != null) //Should also have "&& (allDataRef...size() > 0))"
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one record. Throw error otherwise
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple items matching barcode : "
                        + barcode + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state.
                // This properties object holds data that is being passed to be used 
                // within this class locally.
                Properties retrievedInvenItemData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedInvenItemData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedInvenItemData.getProperty(nextKey);
                    // accountNumber = Integer.parseInt(retrievedAccountData.getProperty("accountNumber"));

                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
            System.out.println("--NOTICE: INVENTORY ITEM OBJECT CREATED FROM TABLE--"); //Debug
        }//Endif taking existing entry

        // If no inventory item exists, throw error
        else
        {
            throw new InvalidPrimaryKeyException("No inventory item with barcode: "
                    + barcode + " found.");
        }
    }//End constructor1-------------------------------------------------------------------------------------------


    //--------------------------------------------------------------
    /*setDependencies
     * Sets dependencies to Clerk
     */
    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }


    @Override
    public void updateState(String key, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateState'");
    }


    //Get property method
    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage") == true)
            return updateStatusMessage;

        return persistentState.getProperty(key);
    }


    @Override
    public void stateChangeRequest(String key, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stateChangeRequest'");
    }


    @Override
    protected void initializeSchema(String tableName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initializeSchema'");
    }

}//END CLASS==========================================================
