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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

// project imports
import impresario.IModel;

//==============================================================================
public class ModifyInventoryView extends View {
    protected Text barcodeText;
    protected TextField gender;
    protected TextField size;

    protected TextField articleTypeDesc;
    protected TextField color1Desc;
    protected TextField color2Desc;

    protected TextField brand;
    protected TextField notes;

    protected TextField donorLastname;
    protected TextField donorFirstname;
    protected TextField donorPhone;

    protected TextField donorEmail;

	protected Button cancelButton;
	protected Button submitButton;

	protected MessageView statusLog;


	//--------------------------------------------------------------------------
	public ModifyInventoryView(IModel wsc)
	{
		super(wsc, "ModifyInventoryView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

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
		getInventoryData();
	}

	//--------------------------------------------------------------------------
	protected void getInventoryData() {
		barcodeText.setText((String)(myModel.getState("barcode")));
        gender.setText((String)myModel.getState("gender"));
        size.setText((String)myModel.getState("size"));

        articleTypeDesc.setText((String)(myModel.getState("articleTypeDesc")));
        color1Desc.setText((String)myModel.getState("color1Desc"));
        color2Desc.setText((String)myModel.getState("color2Desc"));

        brand.setText((String)(myModel.getState("brand")));
        notes.setText((String)myModel.getState("notes"));

        donorLastname.setText((String)(myModel.getState("donorLastName")));
        donorFirstname.setText((String)myModel.getState("donorFirstName"));
        donorPhone.setText((String)myModel.getState("donorPhone"));

        donorEmail.setText((String)(myModel.getState("donorEmail")));
	}

	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

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
        
        Text prompt = new Text("Modify Inventory details:");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(javafx.scene.paint.Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

		Text barcodeLabel = styleTextLable("Barcode");
        Text genderLabel = styleTextLable("Gender");
		Text sizeLabel = styleTextLable("Size");

        Text articleTypeDescLabel = styleTextLable("Article Type");
        Text color1DescLabel = styleTextLable("Color 1");
		Text color2DescLabel = styleTextLable("Color 2");

        Text brandLabel = styleTextLable("Brand");
        Text notesLabel = styleTextLable("Notes");

        Text donorLastnameLabel = styleTextLable("Donor Last Name");
        Text donorFirstnameLabel = styleTextLable("Donot First Name");
		Text donorPhoneLabel = styleTextLable("Donor Phone Number");

        Text donorEmailLabel = styleTextLable("Donor Email");
        

		grid.add(barcodeLabel, 0, 1);
		grid.add(genderLabel, 0, 2);
		grid.add(sizeLabel, 0, 3);

        grid.add(articleTypeDescLabel, 0, 4);
		grid.add(color1DescLabel, 0, 5);
		grid.add(color2DescLabel, 0, 6);

        grid.add(brandLabel, 0, 7);
		grid.add(notesLabel, 0, 8);

        grid.add(donorFirstnameLabel, 0, 9);
		grid.add(donorLastnameLabel, 0, 10);
		grid.add(donorPhoneLabel, 0, 11);

        grid.add(donorEmailLabel, 0, 12);

        barcodeText = new Text("");
        gender = new TextField("");
        size = new TextField("");

        articleTypeDesc = new TextField("");
        color1Desc = new TextField("");
        color2Desc = new TextField("");

        brand = new TextField("");
        notes = new TextField("");

        donorFirstname = new TextField("");
        donorLastname = new TextField("");
        donorPhone = new TextField("");

        donorEmail = new TextField("");

        grid.add(barcodeText, 1, 1);
        grid.add(gender, 1, 2);
        grid.add(size, 1, 3);

        grid.add(articleTypeDesc, 1, 4);
        grid.add(color1Desc, 1, 5);
        grid.add(color2Desc, 1, 6);

        grid.add(brand, 1, 7);
        grid.add(notes, 1, 8);

        grid.add(donorFirstname, 1, 9);
        grid.add(donorLastname, 1, 10);
        grid.add(donorPhone, 1, 11);

        grid.add(donorEmail, 1, 12);


        HBox btnContainer = new HBox(100);
		btnContainer.setAlignment(Pos.CENTER);

		submitButton = new Button("Submit");
 		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage();
				processSubmit();
			}
		});
        btnContainer.getChildren().add(submitButton);

		cancelButton = new Button("Back");
 		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	clearErrorMessage();
       		     	myModel.stateChangeRequest("CancelModifyInventoryItem", null); 
            	  }
        	});
		btnContainer.getChildren().add(cancelButton);
		
        ScrollPane sp = new ScrollPane(grid);
        sp.setMaxHeight(500);
        sp.setMaxWidth(vbox.getMaxWidth());
        
        statusLog = new MessageView("");

		vbox.getChildren().add(sp);
		vbox.getChildren().add(btnContainer);
        vbox.getChildren().add(statusLog);
	
		return vbox;
	}

	

	//--------------------------------------------------------------------------
	public void updateState(String key, Object value) {
        switch (key) {
            case "TransactionStatus":
                displayMessage((String)value);
		}
	}

	//--------------------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

    protected boolean processSubmit() {
        Properties props = new Properties();
        String genderString = "";
        String sizeString = "";
        String brandString = "";
        String noteString = "";
        String donorFirstNameString = "";
        String donorLastNameString = "";
        String donorPhoneString = "";
        String donorEmailString = "";
        String receiverNetidString = "";
        String receiverFirstNameString = "";
        String receiverLastNameString = "";
        String dateDonatedString = "";
        String dateTakenString = "";



        if ((size.getText().isEmpty())) {
            statusLog.displayErrorMessage("Size cannot be empty");
            return false;
        } else {
            props.setProperty("size", size.getText());
        }

        if (!(gender.getText().isEmpty())) {
            genderString = gender.getText();
            if (genderString.toUpperCase().equals("M") || genderString.toUpperCase().equals("W")) {
                props.setProperty("gender", genderString);
            } else {
                statusLog.displayErrorMessage("Gender can only be M or W (One letter)");
                return false;
            }
        } else {
            statusLog.displayErrorMessage("Gender cannot be empty");
            return false;
        }

        donorPhoneString = donorPhone.getText();
        if (donorPhoneString != null) {
            if (!donorPhoneString.isEmpty()) {
                if (isValidPhoneNumber(donorPhoneString)) {
                    props.setProperty("donorPhone", donorPhoneString);
                } else {
                    statusLog.displayErrorMessage("Phone number must follow XXX-XXXX");
                    return false;
                }
            }
        }

        donorEmailString = donorEmail.getText();
        if (donorEmailString != null) {
            if (!donorEmailString.isEmpty()) {
                if (isValidEmail(donorEmailString)) {
                    props.setProperty("donorEmail", donorEmailString);
                } else {
                    statusLog.displayErrorMessage("Email must follow format: example@mail.com");
                    return false;
                }
            }
        }

        brandString = brand.getText();
        if (brandString != null) {
            if (!brandString.isEmpty()) {
                props.setProperty("brand", brandString);
            }
        }

        noteString = notes.getText();
        if (noteString != null) {
            if (!noteString.isEmpty()) {
                props.setProperty("notes", noteString);
            }
        }

        donorFirstNameString = donorFirstname.getText();
        if (donorFirstNameString != null) {
            if (!donorFirstNameString.isEmpty()) {
                props.setProperty("donorFirstName", donorFirstNameString);
            }
        }

        donorLastNameString = donorLastname.getText();
        if (donorLastNameString != null) {
            if (!donorLastNameString.isEmpty()) {
                props.setProperty("donorLastName", donorLastNameString);
            }
        }

        statusLog.displayMessage("Success");
        myModel.stateChangeRequest("Modify", props);

        return true;
    }

    private boolean isValidDate(String date){
        String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence)date);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String regex = "^[^@]+@[^@]+\\.[^@]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence)email);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phone){
        String regex = "[0-9]{3}-[0-9]{3}-[0-9]{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence)phone);
        return matcher.matches();
    }


	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
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

   public Text styleTextLable(String labelName){

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        Text label = new Text(labelName + ": ");
        label.setFont(myFont);
        label.setWrappingWidth(150);
        label.setTextAlignment(TextAlignment.RIGHT);

        return label;
   }
	
}