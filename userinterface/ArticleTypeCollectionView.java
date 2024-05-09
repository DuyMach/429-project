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
import model.ArticleType;
import model.ArticleTypeCollection;


//==============================================================================
public class ArticleTypeCollectionView extends View
{
	protected TableView<ArticleTypeTableModel> tableOfArticleTypes;
	protected ArticleTypeCollection articleTypeCollection;
	protected Button cancelButton;
	protected Button submitButton;

	protected TextField description;
    protected TextField alphaCode;
    protected TextField status;

	protected MessageView statusLog;


	//--------------------------------------------------------------------------
	public ArticleTypeCollectionView(IModel wsc) {
		super(wsc, "ArticleTypeCollectionView");
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

		myModel.subscribe("Modify", this);
		myModel.subscribe("CancelDeleteArticleType", this);
	}

	//--------------------------------------------------------------------------
	protected void populateFields() {
		getEntryTableModelValues();
	}

	//--------------------------------------------------------------------------
	protected void getEntryTableModelValues() {
		ObservableList<ArticleTypeTableModel> tableData = FXCollections.observableArrayList();
		try {
			articleTypeCollection = (ArticleTypeCollection)myModel.getState("ArticleTypeCollection");

	 		Vector entryList = (Vector)articleTypeCollection.getState("ArticleTypes");

			//if nothing was found in search display error message
			if (entryList.isEmpty()){
				statusLog.displayErrorMessage("No matching records found");
			}

			Enumeration entries = entryList.elements();
			while (entries.hasMoreElements() == true) {
				ArticleType nextArticleType = (ArticleType)entries.nextElement();
				Vector<String> view = nextArticleType.getEntryListView();

				// add this list entry to the list
				ArticleTypeTableModel nextTableRowData = new ArticleTypeTableModel(view);
				tableData.add(nextTableRowData);
			}
			
			tableOfArticleTypes.setItems(tableData);
		}
		catch (Exception e) {//SQLException e) {
			// Need to handle this exception
		}
	}

	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text("Article Type Information");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
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
        
        Text prompt = new Text(" Select an Article Type ");
        prompt.setWrappingWidth(300);
		prompt.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 16);
    
        Text aplhaCodeLabel = new Text(" Alpha Code: ");
        aplhaCodeLabel.setFont(myFont);
        aplhaCodeLabel.setWrappingWidth(100);
        aplhaCodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(aplhaCodeLabel, 0, 1);

        alphaCode = new TextField();
        alphaCode.setEditable(true);
        grid.add(alphaCode, 1, 1);

        Text descriptionLabel = new Text(" Description: ");
        descriptionLabel.setFont(myFont);
        descriptionLabel.setWrappingWidth(100);
        descriptionLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(descriptionLabel, 0, 2);

        description = new TextField();
        description.setEditable(true);
        grid.add(description, 1, 2);


		tableOfArticleTypes = new TableView<ArticleTypeTableModel>();
		tableOfArticleTypes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn descriptionColumn = new TableColumn("Description") ;
		descriptionColumn.setMinWidth(100);
		descriptionColumn.setCellValueFactory(
	                new PropertyValueFactory<ArticleTypeTableModel, String>("description"));

		TableColumn barcodePrefixColumn = new TableColumn("Barcode Prefix") ;
		barcodePrefixColumn.setMinWidth(150);
		barcodePrefixColumn.setCellValueFactory(
					new PropertyValueFactory<ArticleTypeTableModel, String>("barcodePrefix"));
	
		TableColumn alphaCodeColumn = new TableColumn("Alpha Code") ;
		alphaCodeColumn.setMinWidth(100);
		alphaCodeColumn.setCellValueFactory(
	                new PropertyValueFactory<ArticleTypeTableModel, String>("alphaCode"));

		tableOfArticleTypes.getColumns().addAll(descriptionColumn, barcodePrefixColumn, alphaCodeColumn);

		tableOfArticleTypes.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ) {
					processArticleTypeSelected();
				}
			}
		});
		tableOfArticleTypes.setPrefSize(370, 150);

		HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        cancelButton = new Button("Back");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
			/**
			 * Process the Cancel button.
			 * The ultimate result of this action is that the transaction will tell the teller to
			 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
			 * It simply tells its model (controller) that the transaction was canceled, and leaves it
			 * to the model to decide to tell the teller to do the switch back.
			*/
			//----------------------------------------------------------
			clearErrorMessage();
			myModel.stateChangeRequest("CancelTransaction", null); 
			}
		});
		buttons.getChildren().add(cancelButton);

		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage(); 
				Properties props = getInput();

				myModel.stateChangeRequest("ArticleTypeSearch", props);
				getEntryTableModelValues();
			}
		});
		buttons.getChildren().add(submitButton);
		
		vbox.getChildren().add(grid);
		vbox.getChildren().add(tableOfArticleTypes);
		vbox.getChildren().add(buttons);
		vbox.setAlignment(Pos.CENTER);
	
		return vbox;
	}

	

	//--------------------------------------------------------------------------
	public void updateState(String key, Object value) {
        switch (key) {
            case "Modify":
				getEntryTableModelValues();
				break;
			case "CancelDeleteArticleType":
				getEntryTableModelValues();
				break;
		}
	}

	//--------------------------------------------------------------------------
	protected void processArticleTypeSelected() {
		ArticleTypeTableModel selectedItem = tableOfArticleTypes.getSelectionModel().getSelectedItem();
		if(selectedItem != null) {
			Properties props = new Properties();
			props.setProperty("id", selectedItem.getId());
			props.setProperty("description", selectedItem.getDescription());
			props.setProperty("barcodePrefix", selectedItem.getBarcodePrefix());
			props.setProperty("alphaCode", selectedItem.getAlphaCode());

			//Calls to DeleteArticleTypeTransaction
			myModel.stateChangeRequest("ArticleTypeSelected", props);
		}
	}

	//--------------------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage) {
		statusLog = new MessageView(initialMessage);

		return statusLog;
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
	
	/*getInput---------------------------------------------
	 * takes the input of textfields and makes a props object
	 */
	public Properties getInput() {
		Properties props = new Properties();
		
		try {
			String descriptionString = description.getText();
			String alphaCodeString = alphaCode.getText();

			if (alphaCodeString.isEmpty() && descriptionString.isEmpty()){
				return props;
			}

			if (!alphaCodeString.isEmpty()){
				props.setProperty("alphaCode", alphaCodeString);
			}

			if (!descriptionString.isEmpty()){
				props.setProperty("description", descriptionString);
			}
		} catch (Exception e) {

		}

		//props.setProperty("description", description.getText());
		//props.setProperty("alphaCode", alphaCode.getText());
		
		return props;
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
