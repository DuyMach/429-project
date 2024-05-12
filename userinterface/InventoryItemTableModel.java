package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class InventoryItemTableModel {
    private final SimpleStringProperty barcode;
    private final SimpleStringProperty articleTypeDescription;
    private final SimpleStringProperty colorDescription;
    private final SimpleStringProperty gender;
	private final SimpleStringProperty dateDonated;

	//----------------------------------------------------------------------------
	public InventoryItemTableModel(Vector<String> inventoryItemData) {
        barcode = new SimpleStringProperty(inventoryItemData.elementAt(0));
        articleTypeDescription = new SimpleStringProperty(inventoryItemData.elementAt(1));
        colorDescription = new SimpleStringProperty(inventoryItemData.elementAt(2));
        gender = new SimpleStringProperty(inventoryItemData.elementAt(3));
        dateDonated = new SimpleStringProperty(inventoryItemData.elementAt(4));
	}

    //----------------------------------------------------------------------------
	public String getBarcode() {
        return barcode.get();
    }

	//----------------------------------------------------------------------------
    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

	//----------------------------------------------------------------------------
	public String getArticleTypeDescription() {
        return articleTypeDescription.get();
    }

	//----------------------------------------------------------------------------
    public void setArticleTypeDescription(String articleTypeDescription) {
        this.articleTypeDescription.set(articleTypeDescription);
    }

    //----------------------------------------------------------------------------
	public String getColorDescription() {
        return colorDescription.get();
    }

	//----------------------------------------------------------------------------
    public void setColorDescription(String colorDescription) {
        this.colorDescription.set(colorDescription);
    }

    //----------------------------------------------------------------------------
    public String getGender() {
        return gender.get();
    }

	//----------------------------------------------------------------------------
    public void setGender(String gender) {
        this.gender.set(gender);
    }

    //----------------------------------------------------------------------------
    public String getDateDonated() {
        return dateDonated.get();
    }

	//----------------------------------------------------------------------------
    public void setDateDonated(String dateDonated) {
        this.dateDonated.set(dateDonated);
    }
}
