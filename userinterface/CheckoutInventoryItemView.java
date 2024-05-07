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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Properties;
import java.time.LocalDate;

// project imports
import impresario.IModel;
import model.Color;
import model.ColorCollection;

//==============================================================================
public class CheckoutInventoryItemView extends View {
    protected Text barcode;
    protected Text gender;
    protected Text articleType;
    protected Text color1;
    protected Text color2;
    protected Text size;
    protected Text brand;
    protected Text notes;
	protected Text donorLastName;
    protected Text donorFirstName;
    protected Text donorPhone;
    protected Text donorEmail;

	protected TextField receiverNetId;
	protected TextField receiverLastName;
	protected TextField receiverFirstName;

	protected Button cancelButton;
	protected Button submitButton;

	protected MessageView statusLog;


	//--------------------------------------------------------------------------
	public CheckoutInventoryItemView(IModel wsc)
	{
		super(wsc, "CheckoutInventoryItemView");
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

        myModel.subscribe("TransactionStatus", this);
	}

	//--------------------------------------------------------------------------
	protected void populateFields() {
		getInventoryItemData();
	}

	//--------------------------------------------------------------------------
	protected void getInventoryItemData() {
		barcode.setText((String)(myModel.getState("barcode")));
        gender.setText((String)(myModel.getState("gender")));
        articleType.setText((String)(myModel.getState("articleTypeDescription")));
        color1.setText((String)(myModel.getState("color1Description")));

		String color2DescString = (String) myModel.getState("color2Description");
		if (color2DescString.equals("Inactive color")) {
			color2.setText("You don't got no color 2, you colorless hoe"); // Subject to change
		} else {
			color2.setText((String)(myModel.getState("color2Description")));
		}

        size.setText((String)(myModel.getState("size")));
        brand.setText((String)(myModel.getState("brand")));
        notes.setText((String)(myModel.getState("notes")));
        donorLastName.setText((String)(myModel.getState("donorLastName")));
        donorFirstName.setText((String)(myModel.getState("donorFirstName")));
        donorPhone.setText((String)(myModel.getState("donorPhone")));
        donorEmail.setText((String)(myModel.getState("donorEmail")));
	}

	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text(" Checkout Inventory Item ");
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
        
        Text prompt = new Text("Item to checkout:");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(javafx.scene.paint.Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

		Text barcodeLabel = new Text("Barcode:");
        barcode = new Text();
        Text genderLabel = new Text("Gender:");
        gender = new Text();
        Text articleTypeLabel = new Text("Article Type:");
        articleType = new Text();
        Text color1Label = new Text("Color 1:");
        color1 = new Text();
        Text color2Label = new Text("Color 2:");
        color2 = new Text();
        Text sizeLabel = new Text("Size:");
        size = new Text();
        Text brandLabel = new Text("Brand:");
        brand = new Text();
        Text notesLabel = new Text("Notes:");
        notes = new Text();
        Text donorLastNameLabel = new Text("Donor Last Name:");
        donorLastName = new Text();
        Text donorFirstNameLabel = new Text("Donor First Name:");
        donorFirstName = new Text();
        Text donorPhoneLabel = new Text("Donor Phone:");
        donorPhone = new Text();
        Text donorEmailLabel = new Text("Donor Email:");
        donorEmail = new Text();
        
        grid.add(barcodeLabel, 0, 1);
        grid.add(barcode, 1, 1);
        grid.add(genderLabel, 0, 2);
        grid.add(gender, 1, 2);
        grid.add(articleTypeLabel, 0, 3);
        grid.add(articleType, 1, 3);
        grid.add(color1Label, 0, 4);
        grid.add(color1, 1, 4);
        grid.add(color2Label, 0, 5);
        grid.add(color2, 1, 5);
        grid.add(sizeLabel, 0, 6);
        grid.add(size, 1, 6);
        grid.add(brandLabel, 0, 7);
        grid.add(brand, 1, 7);
        grid.add(notesLabel, 0, 8);
        grid.add(notes, 1, 8);
        grid.add(donorLastNameLabel, 0, 9);
        grid.add(donorLastName, 1, 9);
        grid.add(donorFirstNameLabel, 0, 10);
        grid.add(donorFirstName, 1, 10);
        grid.add(donorPhoneLabel, 0, 11);
        grid.add(donorPhone, 1, 11);
        grid.add(donorEmailLabel, 0, 12);
        grid.add(donorEmail, 1, 12);

		Text receiverInfoPrompt = new Text("Enter receiver Info:");
        receiverInfoPrompt.setWrappingWidth(350);
        receiverInfoPrompt.setTextAlignment(TextAlignment.CENTER);
        receiverInfoPrompt.setFill(javafx.scene.paint.Color.BLACK);
        grid.add(receiverInfoPrompt, 0, 13, 2, 1);

		Text receiverNetIdLabel = new Text("receiver Net Id:");
        receiverNetId = new TextField();
		Text receiverLastNameLabel = new Text("receiver Last Name:");
        receiverLastName= new TextField();
		Text receiverFirstNameLabel = new Text("receiver First Name:");
        receiverFirstName = new TextField();

		grid.add(receiverNetIdLabel, 0, 14);
        grid.add(receiverNetId, 1, 14);
        grid.add(receiverLastNameLabel, 0, 15);
        grid.add(receiverLastName, 1, 15);
		grid.add(receiverFirstNameLabel, 0, 16);
        grid.add(receiverFirstName, 1, 16);

		HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        cancelButton = new Button("Back");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
       		     @Override
       		     public void handle(ActionEvent e) {
       		     	clearErrorMessage();
       		     	myModel.stateChangeRequest("CancelCheckoutInventoryItem", null); 
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
        if (validateInputs()) {
			Properties props = new Properties();
			props.setProperty("receiverNetId", receiverNetId.getText());
			props.setProperty("receiverLastName", receiverLastName.getText());
			props.setProperty("receiverFirstName", receiverFirstName.getText());
			props.setProperty("dateTaken", LocalDate.now().toString());
			props.setProperty("status", "Received");
			myModel.stateChangeRequest("DoCheckoutInventoryItem", props);
		}
    }

	private boolean validateInputs() {
		if (receiverNetId.getText().isEmpty()) {
			displayErrorMessage("Enter a receiver net id");
			receiverNetId.requestFocus();
			return false;
		}
		else if (receiverLastName.getText().isEmpty()) {
			displayErrorMessage("Enter a receiver last name");
			receiverLastName.requestFocus();
			return false;
		}
		else if (receiverFirstName.getText().isEmpty()) {
			displayErrorMessage("Enter a receiver first name");
			receiverFirstName.requestFocus();
			return false;
		}
		return true;
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