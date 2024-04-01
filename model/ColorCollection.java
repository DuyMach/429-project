package model;

import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

public class ColorCollection extends EntityBase {
    private static final String myTableName = "Color";
    private Vector<Color> colorList;
    public ColorCollection() {
        super(myTableName);
    }

    public void getColors() throws SQLException {
        colorList = new Vector<Color>();
        String query = "SELECT * FROM " + myTableName + " WHERE status = \"Active\"";
        Vector<Properties> result = getSelectQueryResult(query);
        if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Properties nextColorData = result.elementAt(i);
				Color color = new Color(nextColorData);
				if (color != null) {
					colorList.add(color);
				}
			}
		}
    }

    public void display() {
        if (colorList.size() == 0) {
            System.out.println("No colors in collection");
        }
        else {
            for (int i = 0; i < colorList.size(); i++) {
                System.out.println(colorList.elementAt(i).toString());
            }
        }
    }

    public Object getState(String key) {
		if (key.equals("Colors"))
			return colorList;
		else if (key.equals("ColorCollection"))
			return this;
		return null;
	}

    public Color getColorWithId(String colorId) {
		Color retValue = null;
		for (int i = 0; i < colorList.size(); i++) {
			Color nextColor = colorList.elementAt(i);
			String nextId = (String)nextColor.getState("id");
			if (nextId.equals(colorId) == true) {
				retValue = nextColor;
				return retValue;
			}
		}
		return retValue;
	}

    public void stateChangeRequest(String key, Object value) {
		myRegistry.updateSubscribers(key, this);
	}

    protected void initializeSchema(String tableName) {
		if (mySchema == null) {
			mySchema = getSchemaInfo(tableName);
		}
	}
}