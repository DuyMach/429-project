package userinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.ArticleType;
import model.InventoryItem;

import java.util.Enumeration;
import java.util.Properties;

import impresario.IModel;

public class DeleteInventoryItemView extends View {
    protected InventoryItem item;
    protected Properties itemState;

    protected Button cancelButton;
    protected Button submitButton;

    protected MessageView statusLog;

    public DeleteInventoryItemView(IModel wsc)
    {
        super(wsc, "DeleteInventoryItemView");
        String css = getClass().getResource("Styles.css").toExternalForm();
        getStylesheets().add(css);

        populateFields();

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.LIGHTYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog("                                            "));
        getChildren().add(container);

        myModel.subscribe("TransactionStatus", this);
        myModel.subscribe("TransactionError", this);
    }

    protected void populateFields()
    {
        item = (InventoryItem)(myModel.getState("InventoryItem"));
        itemState = (Properties)item.getState("PersistantState");
    }

    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Delete Inventory Item");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(javafx.scene.paint.Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text("Are you sure you'd like to remove the inventory item with the details below?");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(javafx.scene.paint.Color.DARKGREEN);
        grid.add(prompt, 0, 0);

        Enumeration<Object> enu = itemState.keys();

        int i = 1;
        while (enu.hasMoreElements())
        {
            String nextKey = (String)enu.nextElement();
            String nextValue = itemState.getProperty(nextKey);
            if (nextKey.equals("barcode"))
            {
                continue;
            }
            if (nextKey.equals("articleTypeId"))
            {
                try
                {
                    ArticleType temp = new ArticleType(nextValue);
                    nextKey = "articleType";
                    nextValue = (String)temp.getState("description");
                }
                catch (Exception ex)
                {
                    // This should never happen.
                }
            }
            if (nextKey.equals("color1Id"))
            {
                try
                {
                    model.Color temp = new model.Color(nextValue);
                    nextKey = "color 1";
                    nextValue = (String)temp.getState("description");
                }
                catch (Exception ex)
                {
                    // This should never happen.
                }
            }
            if (nextKey.equals("color2Id"))
            {
                try
                {
                    model.Color temp = new model.Color(nextValue);
                    nextKey = "color 2";
                    nextValue = (String)temp.getState("description");
                }
                catch (Exception ex)
                {
                    // This should never happen.
                }
            }

            nextKey = nextKey.replaceAll("([A-Z])", " $1");
            char[] temp = nextKey.toCharArray();
            temp[0] = Character.toUpperCase(temp[0]);
            nextKey = String.valueOf(temp);

            Text detail = new Text(nextKey + ": " + nextValue);
            detail.setWrappingWidth(350);
            detail.setTextAlignment(TextAlignment.CENTER);
            detail.setFill(javafx.scene.paint.Color.BLACK);
            grid.add(detail, 0, i);
            i++;
        }

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        Button cancelButton = new Button("No");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e)
            {
                clearErrorMessage();
                myModel.stateChangeRequest("CancelDeleteInventoryItem", null);
            }
        });
        buttons.getChildren().add(cancelButton);

        submitButton = new Button("Yes");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e)
            {
                clearErrorMessage();
                myModel.stateChangeRequest("DeleteInventoryItem", null);
            }
        });
        buttons.getChildren().add(submitButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(buttons);

        return vbox;
    }

    public void updateState(String key, Object value)
    {
        switch (key)
        {
            case "TransactionStatus":
                displayMessage((String)value);
        }
    }

    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }

    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }
}