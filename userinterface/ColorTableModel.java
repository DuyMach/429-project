package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class ColorTableModel {
    private String id;
    private final SimpleStringProperty description;
    private final SimpleStringProperty barcodePrefix;
	private final SimpleStringProperty alphaCode;

	//----------------------------------------------------------------------------
	public ColorTableModel(Vector<String> colorData) {
        id = colorData.elementAt(0);
		description =  new SimpleStringProperty(colorData.elementAt(1));
        barcodePrefix = new SimpleStringProperty(colorData.elementAt(2));
		alphaCode =  new SimpleStringProperty(colorData.elementAt(3));
	}

    //----------------------------------------------------------------------------
	public String getId() {
        return id;
    }

	//----------------------------------------------------------------------------
    public void setId(String id) {
        this.id = id;
    }

	//----------------------------------------------------------------------------
	public String getDescription() {
        return description.get();
    }

	//----------------------------------------------------------------------------
    public void setDescription(String id) {
        description.set(id);
    }

    //----------------------------------------------------------------------------
	public String getBarcodePrefix() {
        return barcodePrefix.get();
    }

	//----------------------------------------------------------------------------
    public void setBarcodePrefix(String prefix) {
        barcodePrefix.set(prefix);
    }

    //----------------------------------------------------------------------------
    public String getAlphaCode() {
        return alphaCode.get();
    }

    //----------------------------------------------------------------------------
    public void setAlphaCode(String athr) {
        alphaCode.set(athr);
    }
}
