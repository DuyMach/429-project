package userinterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
import javafx.stage.Stage;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Properties;

import impresario.IModel;
import model.Color;
import model.ColorCollection;
import model.InventoryItem;
import model.InventoryItemCollection;

public class ListAvailableInventoryView extends View
{
    protected TableView<InventoryItemTableModel> tableOfInventoryItems;
    protected InventoryItemCollection inventoryItemCollection;
    protected Button backButton;

    protected MessageView statusLog;

    public ListAvailableInventoryView(IModel wsc) {
        super(wsc, "ListAvailableInventoryView");
        String css = getClass().getResource("Styles.css").toExternalForm();
        getStylesheets().add(css);

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.LIGHTYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);

        populateFields();
    }

    protected void populateFields() {
        getEntryTableModelValues();
    }

    protected void getEntryTableModelValues() {
        ObservableList<InventoryItemTableModel> tableData = FXCollections.observableArrayList();
        try {
            inventoryItemCollection = (InventoryItemCollection)myModel.getState("InventoryItemCollection");

            Vector entryList = (Vector)inventoryItemCollection.getState("InventoryItems");
            Enumeration entries = entryList.elements();
            while (entries.hasMoreElements() == true) {
                InventoryItem nextInventoryItem = (InventoryItem)entries.nextElement();
                Vector<String> view = nextInventoryItem.getEntryListView();

                // add this list entry to the list
                InventoryItemTableModel nextTableRowData = new InventoryItemTableModel(view);
                tableData.add(nextTableRowData);

            }

            tableOfInventoryItems.setItems(tableData);
        }
        catch (Exception e) {
            // Need to handle this exception
        }
    }

    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Clothes Closet ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(javafx.scene.paint.Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    private VBox createFormContent() {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text("Available Inventory");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(javafx.scene.paint.Color.BLACK);
        grid.add(prompt, 0, 0);

        tableOfInventoryItems = new TableView<InventoryItemTableModel>();
        tableOfInventoryItems.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableOfInventoryItems.setMaxWidth(490);

        TableColumn barcodeColumn = new TableColumn("Barcode") ;
        barcodeColumn.setMinWidth(80);
        barcodeColumn.setCellValueFactory(
                    new PropertyValueFactory<InventoryItemTableModel, String>("barcode"));

        TableColumn articleTypeColumn = new TableColumn("Article Type") ;
        articleTypeColumn.setMinWidth(120);
        articleTypeColumn.setCellValueFactory(
                    new PropertyValueFactory<InventoryItemTableModel, String>("articleTypeDescription"));

        TableColumn colorColumn = new TableColumn("Color 1") ;
        colorColumn.setMinWidth(80);
        colorColumn.setCellValueFactory(
                    new PropertyValueFactory<InventoryItemTableModel, String>("colorDescription"));

        TableColumn genderColumn = new TableColumn("Gender") ;
        genderColumn.setMinWidth(70);
        genderColumn.setCellValueFactory(
                    new PropertyValueFactory<InventoryItemTableModel, String>("gender"));

        TableColumn dateDonatedColumn = new TableColumn("Date Donated") ;
        dateDonatedColumn.setMinWidth(133);
        dateDonatedColumn.setCellValueFactory(
                    new PropertyValueFactory<InventoryItemTableModel, String>("dateDonated"));

        tableOfInventoryItems.getColumns().addAll(barcodeColumn, articleTypeColumn, colorColumn, genderColumn, dateDonatedColumn);

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {

                clearErrorMessage();
                myModel.stateChangeRequest("CancelListAvailableInventory", null);
                }
        });
        buttons.getChildren().add(backButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(tableOfInventoryItems);
        vbox.getChildren().add(buttons);

        return vbox;
    }

    public void updateState(String key, Object value) {
    }

    protected MessageView createStatusLog(String initialMessage) {
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
