package model;

import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

public class ArticleTypeCollection extends EntityBase {
    private static final String myTableName = "Color";
    private Vector<ArticleType> articleTypeList;
    public ArticleTypeCollection() {
        super(myTableName);
        articleTypeList = new Vector<ArticleType>();
    }

    public void getArticleTypes() throws SQLException {
        String query = "SELECT * FROM " + myTableName;
        Vector<Properties> result = getSelectQueryResult(query);
        if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Properties nextArticleTypeData = result.elementAt(i);
				ArticleType articleType = new ArticleType(nextArticleTypeData);
				if (articleType != null) {
					articleTypeList.add(articleType);
				}
			}
		}
    }

    public void display() {
        if (articleTypeList.size() == 0) {
            System.out.println("No ArticleType in collection");
        }
        else {
            for (int i = 0; i < articleTypeList.size(); i++) {
                System.out.println(articleTypeList.elementAt(i).toString());
            }
        }
    }

    public Object getState(String key) {
		if (key.equals("ArticleTypes"))
			return articleTypeList;
		else if (key.equals("ArticleTypeCollection"))
			return this;
		return null;
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