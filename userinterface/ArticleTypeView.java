package userinterface;

import java.util.Properties;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ArticleTypeView extends View{
    private TextField description;
    private TextField barcodePrefix;
    private TextField alphaCode;
    protected ComboBox<String> status = new ComboBox<>();
    private String[] activity = {"Active", "Inactive"};
    private Button cancelButton;
    private Button doneButton;
    private MessageView statusLog;

    public ArticleTypeView(IModel model) {
        super(model, "ArticleTypeView");
        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        populateFields();
    }

    private void populateFields() {
        description.setText((String)myModel.getState("description"));
        barcodePrefix.setText((String)myModel.getState("barcodePrefix"));
        alphaCode.setText((String)myModel.getState("alphaCode"));
        // status.setText((String)myModel.getState("Status"));
    }

    private MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }

    private VBox createFormContent() {
        VBox vbox = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text("Article Type INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text barcodeLabel = new Text(" Barcode Prefix : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        barcodeLabel.setFont(myFont);
        barcodeLabel.setWrappingWidth(150);
        barcodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(barcodeLabel, 0, 1);

        barcodePrefix = new TextField();
        barcodePrefix.setEditable(true);
        grid.add(barcodePrefix, 1, 1);

        Text aplhaCodeLabel = new Text(" Alpha Code Label : ");
        aplhaCodeLabel.setFont(myFont);
        aplhaCodeLabel.setWrappingWidth(150);
        aplhaCodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(aplhaCodeLabel, 0, 2);

        alphaCode = new TextField();
        alphaCode.setEditable(true);
        grid.add(alphaCode, 1, 2);

        Text descriptionLabel = new Text(" Description: ");
        descriptionLabel.setFont(myFont);
        descriptionLabel.setWrappingWidth(150);
        descriptionLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(descriptionLabel, 0, 3);

        description = new TextField();
        description.setEditable(true);
        grid.add(description, 1, 3);

        Text statusLabel = new Text(" Status : ");
        statusLabel.setFont(myFont);
        statusLabel.setWrappingWidth(150);
        statusLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(statusLabel, 0, 4);

        status.getItems().addAll(activity);
        status.getSelectionModel().selectFirst();
        grid.add(status, 1, 4);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        cancelButton = new Button("Back");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearErrorMessage();
                myModel.stateChangeRequest("BookCancelled", null);
            }
        });
        doneCont.getChildren().add(cancelButton);

        doneButton = new Button("Done");
        doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        doneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearErrorMessage();
                if (validate()) {
                    Properties prop = getInput();
                    myModel.stateChangeRequest("DoAddArticleType", prop);
                    statusLog.displayMessage("Successfully added ArticleType!");
                }
            }
        });
        doneCont.getChildren().add(doneButton);

        statusLog = new MessageView("");

        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);
        vbox.getChildren().add(statusLog);

        return vbox;
    }

    private Properties getInput() {
        Properties prop = new Properties();
        String barcodePrefixString = barcodePrefix.getText();
        String descString = description.getText();
        String alphaCodeString = alphaCode.getText();
        String statusString = status.getValue();

        prop.setProperty("barcodePrefix", barcodePrefixString);
        prop.setProperty("description", descString);
        prop.setProperty("alphaCode", alphaCodeString);
        prop.setProperty("status", statusString);

        return prop;
    }

    private boolean validate(){
        try {
            String barcodePrefixString = barcodePrefix.getText();
            String descString = description.getText();
            String alphaCodeString = alphaCode.getText();

            if (barcodePrefixString == null || descString == null || alphaCodeString == null){
                statusLog.displayErrorMessage("Cannot have any empty fields!");
                return false;
            }

            if (barcodePrefixString.length() != 2 || alphaCodeString.length() != 2){
                statusLog.displayErrorMessage("BarcodePrefix and Alpha Code must be exactly 2 characters!");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }

    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Library System ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    @Override
    public void updateState(String key, Object value) {

    }
}

