// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


// project imports
import exception.InvalidPrimaryKeyException;



/** The class containing the Account for the ATM application */
//==============================================================
public class Color extends EntityBase {
    private static final String myTableName = "Color";

    protected Properties dependencies;

    // GUI Components

    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public Color(String id)
            throws InvalidPrimaryKeyException {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (id = " + id + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one Color. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple ids matching : "
                        + id + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedColorData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedColorData.propertyNames();
                while (allKeys.hasMoreElements() == true) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedColorData.getProperty(nextKey);
                    // accountNumber = Integer.parseInt(retrievedAccountData.getProperty("accountNumber"));

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        else {
            throw new InvalidPrimaryKeyException("No color matching id : "
                    + id + " found.");
        }
    }

    // Can also be used to create a NEW Account (if the system it is part of
    // allows for a new account to be set up)
    //----------------------------------------------------------

    public Color(Properties props) {
        super(myTableName);
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }

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

    public void delete() {
        persistentState.setProperty("status", "Inactive");
    }

    private void setDependencies() {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage"))
            return updateStatusMessage;

        return persistentState.getProperty(key);
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);
    }


    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }



    public static int compare(Color a,  Color b) {
        String aNum = (String) a.getState("description");
        String bNum = (String) b.getState("description");

        return aNum.compareTo(bNum);
    }

    //-----------------------------------------------------------------------------------
    public void update() // save()
    {
        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("id") != null) {
                // update
                Properties whereClause = new Properties();
                whereClause.setProperty("id",
                        persistentState.getProperty("id"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Color data for id : " + persistentState.getProperty("id") + " updated successfully in database!";
            } else {
                // insert
                Integer id =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("id", "" + id);
                updateStatusMessage = "Color data for new Color : " + persistentState.getProperty("id")
                        + "installed successfully in database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing account data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }


    /**
     * This method is needed solely to enable the Account information to be displayable in a table
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("id"));
        v.addElement(persistentState.getProperty("description"));
        v.addElement(persistentState.getProperty("barcodePrefix"));
        v.addElement(persistentState.getProperty("alphaCode"));
        v.addElement(persistentState.getProperty("status"));

        return v;
    }

    public String toString() {
        return "Description: " + persistentState.getProperty("description ") +
                "; Barcode: " + persistentState.getProperty("barcodePrefix") +
                "; Alpha Code:" + persistentState.getProperty("alphaCode") +
                ";status" + persistentState.getProperty("status");
    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
}

