package model;

import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

public class ArticleTypeCollection extends EntityBase {
    private static final String myTableName = "ArticleType";
    private Vector<ArticleType> articleTypeList;
    public ArticleTypeCollection() {
        super(myTableName);
        articleTypeList = new Vector<ArticleType>();
    }

    // public void getArticleTypes() throws SQLException {
    //     String query = "SELECT * FROM " + myTableName;
    //     Vector<Properties> result = getSelectQueryResult(query);
    //     if (result != null) {
	// 		for (int i = 0; i < result.size(); i++) {
	// 			Properties nextArticleTypeData = result.elementAt(i);
	// 			ArticleType articleType = new ArticleType(nextArticleTypeData);
	// 			if (articleType != null) {
	// 				articleTypeList.add(articleType);
	// 			}
	// 		}
	// 	}
    // }

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

    public void findArticleTypeDesc(String description) {
        String query = "SELECT * FROM " + myTableName + " WHERE description LIKE '%" + description + "%';";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            articleTypeList = new Vector<ArticleType>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextArticleTypeData = (Properties)allDataRetrieved.elementAt(cnt);

                ArticleType articleType = new ArticleType(nextArticleTypeData);

                if (articleType != null)
                {
                    addArticleType(articleType);
                }
            }

        }
    }

    public void findArticleTypeBarCodePrefix(String barcodePrefix) {
        String query = "SELECT * FROM " + myTableName + " WHERE (barcodePrefix = " + barcodePrefix + ");";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            articleTypeList = new Vector<ArticleType>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextArticleTypeData = (Properties)allDataRetrieved.elementAt(cnt);

                ArticleType articleType = new ArticleType(nextArticleTypeData);

                if (articleType != null)
                {
                    addArticleType(articleType);
                }
            }

        }

    }

    public void findArticleTypeAlphaCode(String alphaCode) {
        String query = "SELECT * FROM " + myTableName + " WHERE (alphaCode = '" + alphaCode + "')";
        
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            articleTypeList = new Vector<ArticleType>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextArticleTypeData = (Properties)allDataRetrieved.elementAt(cnt);

                ArticleType articleType = new ArticleType(nextArticleTypeData);

                if (articleType != null)
                {
                    addArticleType(articleType);
                }
            }

        }

    }

    private void addArticleType(ArticleType a)
    {
        int index = findIndexToAdd(a);
        articleTypeList.insertElementAt(a,index); // To build up a collection sorted on some key
    }

    private int findIndexToAdd(ArticleType a)
    {
        //users.add(u);
        int low=0;
        int high = articleTypeList.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            ArticleType midSession = articleTypeList.elementAt(middle);

            int result = ArticleType.compare(a,midSession);

            if (result ==0)
            {
                return middle;
            }
            else if (result<0)
            {
                high=middle-1;
            }
            else
            {
                low=middle+1;
            }


        }
        return low;
    }

    public void printArticleType() {
		for (ArticleType at: articleTypeList) {
			System.out.println(at);
		}
	}
}