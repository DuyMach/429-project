package userinterface;

// system imports
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Properties;

// project imports
import impresario.IModel;

//==============================================================================
public class ModifyArticleTypeView extends View {
    protected TextField description;
    protected TextField barcodePrefix;
    protected TextField alphaCode;
	protected Button cancelButton;
	protected Button submitButton;

	protected MessageView statusLog;


	//--------------------------------------------------------------------------
	public ModifyArticleTypeView(IModel wsc)
	{
		super(wsc, "ModifyArticleTypeView");
		String css = getClass().getResource("Styles.css").toExternalForm();
        getStylesheets().add(css);

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));
		container.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

		// create our GUI components, add them to this panel
		container.getChildren().add(createTitle());
		container.getChildren().add(createFormContent());

		// Error message area
		container.getChildren().add(createStatusLog("                                            "));

		getChildren().add(container);
		
		populateFields();

        myModel.subscribe("TransactionStatus", this);
	}

	//--------------------------------------------------------------------------
	protected void populateFields()
	{
		getArticleTypeData();
	}

	//--------------------------------------------------------------------------
	protected void getArticleTypeData() {
		description.setText((String)(myModel.getState("description")));
        barcodePrefix.setText((String)myModel.getState("barcodePrefix"));
        alphaCode.setText((String)myModel.getState("alphaCode"));
	}

	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	
		container.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

		Text titleText = new Text(" Modify Article Type ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(javafx.scene.paint.Color.DARKGREEN);
		container.getChildren().add(titleText);
		
		return container;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent() {
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
       	grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        
        Text prompt = new Text("Modify Color details:");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(javafx.scene.paint.Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

		Text descriptionLabel = new Text(" Description: ");
		descriptionLabel.setFont(myFont);
        descriptionLabel.setWrappingWidth(150);
        descriptionLabel.setTextAlignment(TextAlignment.RIGHT);

		Text barcodePrefixLabel = new Text(" Barcode Prefix : ");
        barcodePrefixLabel.setFont(myFont);
        barcodePrefixLabel.setWrappingWidth(150);
        barcodePrefixLabel.setTextAlignment(TextAlignment.RIGHT);

		Text aplhaCodeLabel = new Text(" Alpha Code Label : ");
        aplhaCodeLabel.setFont(myFont);
        aplhaCodeLabel.setWrappingWidth(150);
        aplhaCodeLabel.setTextAlignment(TextAlignment.RIGHT);

		grid.add(barcodePrefixLabel, 0, 1);
		grid.add(aplhaCodeLabel, 0, 2);
		grid.add(descriptionLabel, 0, 3);

        description = new TextField();
        barcodePrefix = new TextField();
        alphaCode = new TextField();

        grid.add(barcodePrefix, 1, 1);
        grid.add(alphaCode, 1, 2);
        grid.add(description, 1, 3);

		HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        Button cancelButton = new Button("Back");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage();
				myModel.stateChangeRequest("CancelModifyArticleType", null); 
			}
		});
		buttons.getChildren().add(cancelButton);

        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage();
				processSubmit();
			}
		});
        buttons.getChildren().add(submitButton);
		
		vbox.getChildren().add(grid);
		vbox.getChildren().add(buttons);
	
		return vbox;
	}

	

	//--------------------------------------------------------------------------
	public void updateState(String key, Object value) {
        clearErrorMessage();

        if (key.equals("TransactionStatus")) {
            String val = (String) value;
            if ((val.startsWith("ERR")) || (val.startsWith("Err"))) {
                displayErrorMessage(val);
            } else {
                displayMessage(val);
            }
        }
    }

	//--------------------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

    protected void processSubmit() {
        Properties props = new Properties();
        props.setProperty("description", description.getText());
        props.setProperty("barcodePrefix", barcodePrefix.getText());
        props.setProperty("alphaCode", alphaCode.getText());

        myModel.stateChangeRequest("DoModifyArticleType", props);
    }


	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}
	
	public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}
	/*
	//--------------------------------------------------------------------------
	public void mouseClicked(MouseEvent click)
	{
		if(click.getClickCount() >= 2)
		{
			processColorSelected();
		}
	}
   */
	
}