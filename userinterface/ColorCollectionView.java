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
import java.util.Enumeration;
import java.util.Properties;

// project imports
import impresario.IModel;
import model.Color;
import model.ColorCollection;

//==============================================================================
public class ColorCollectionView extends View
{
	protected TableView<ColorTableModel> tableOfColors;
	protected ColorCollection colorCollection;
	protected Button cancelButton;
	protected Button submitButton;

	protected MessageView statusLog;


	//--------------------------------------------------------------------------
	public ColorCollectionView(IModel wsc) {
		super(wsc, "ColorCollectionView");

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
	}

	//--------------------------------------------------------------------------
	protected void populateFields() {
		getEntryTableModelValues();
	}

	//--------------------------------------------------------------------------
	protected void getEntryTableModelValues() {
		ObservableList<ColorTableModel> tableData = FXCollections.observableArrayList();
		try {
			colorCollection = (ColorCollection)myModel.getState("ColorCollection");

	 		Vector entryList = (Vector)colorCollection.getState("Colors");
			Enumeration entries = entryList.elements();
			while (entries.hasMoreElements() == true) {
				Color nextColor = (Color)entries.nextElement();
				Vector<String> view = nextColor.getEntryListView();

				// add this list entry to the list
				ColorTableModel nextTableRowData = new ColorTableModel(view);
				tableData.add(nextTableRowData);
				
			}
			
			tableOfColors.setItems(tableData);
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

		Text titleText = new Text(" Clothes Closet ");
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
        
        Text prompt = new Text("Select a Color to modify:");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(javafx.scene.paint.Color.BLACK);
        grid.add(prompt, 0, 0);

		tableOfColors = new TableView<ColorTableModel>();
		tableOfColors.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn descriptionColumn = new TableColumn("Description") ;
		descriptionColumn.setMinWidth(100);
		descriptionColumn.setCellValueFactory(
	                new PropertyValueFactory<ColorTableModel, String>("description"));

		TableColumn barcodePrefixColumn = new TableColumn("Barcode Prefix") ;
		barcodePrefixColumn.setMinWidth(150);
		barcodePrefixColumn.setCellValueFactory(
					new PropertyValueFactory<ColorTableModel, String>("barcodePrefix"));
	
		TableColumn alphaCodeColumn = new TableColumn("Alpha Code") ;
		alphaCodeColumn.setMinWidth(100);
		alphaCodeColumn.setCellValueFactory(
	                new PropertyValueFactory<ColorTableModel, String>("alphaCode"));

		tableOfColors.getColumns().addAll(descriptionColumn, barcodePrefixColumn, alphaCodeColumn);

		tableOfColors.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ) {
					processColorSelected();
				}
			}
		});
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setMaxSize(372, 150);
		scrollPane.setContent(tableOfColors);

		submitButton = new Button("Submit");
 		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage(); 
				processColorSelected();
			}
		});

		cancelButton = new Button("Back");
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
       		     	myModel.stateChangeRequest("CancelColorCollection", null); 
            	  }
        	});

		HBox btnContainer = new HBox(100);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().add(submitButton);
		btnContainer.getChildren().add(cancelButton);
		
		vbox.getChildren().add(grid);
		vbox.getChildren().add(scrollPane);
		vbox.getChildren().add(btnContainer);
	
		return vbox;
	}

	

	//--------------------------------------------------------------------------
	public void updateState(String key, Object value) {
	}

	//--------------------------------------------------------------------------
	protected void processColorSelected() {
		ColorTableModel selectedItem = tableOfColors.getSelectionModel().getSelectedItem();
		if(selectedItem != null) {
			Properties props = new Properties();
			props.setProperty("id", selectedItem.getId());
			props.setProperty("description", selectedItem.getDescription());
			props.setProperty("barcodePrefix", selectedItem.getBarcodePrefix());
			props.setProperty("alphaCode", selectedItem.getAlphaCode());

			myModel.stateChangeRequest("ColorSelected", props);
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
