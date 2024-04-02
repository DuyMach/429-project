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
		ObservableList<ArticleTypeTableModel> tableData = FXCollections.observableArrayList();
		try {
			articleTypeCollection = (ArticleTypeCollection)myModel.getState("ArticleTypeCollection");

	 		Vector entryList = (Vector)articleTypeCollection.getState("ArticleTypes");
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
        
        Text prompt = new Text("Article Type INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
    
        Text aplhaCodeLabel = new Text(" Alpha Code Label : ");
        aplhaCodeLabel.setFont(myFont);
        aplhaCodeLabel.setWrappingWidth(150);
        aplhaCodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(aplhaCodeLabel, 0, 1);

        alphaCode = new TextField();
        alphaCode.setEditable(true);
        grid.add(alphaCode, 1, 1);

        Text descriptionLabel = new Text(" Description: ");
        descriptionLabel.setFont(myFont);
        descriptionLabel.setWrappingWidth(150);
        descriptionLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(descriptionLabel, 0, 2);

        description = new TextField();
        description.setEditable(true);
        grid.add(description, 1, 2);


		tableOfArticleTypes = new TableView<ArticleTypeTableModel>();
		tableOfArticleTypes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn idColumn = new TableColumn("ID") ;
		idColumn.setMinWidth(100);
		idColumn.setCellValueFactory(
	                new PropertyValueFactory<ArticleTypeTableModel, String>("id"));

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

		tableOfArticleTypes.getColumns().addAll(idColumn, descriptionColumn, barcodePrefixColumn, alphaCodeColumn);

		tableOfArticleTypes.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ) {
					processColorSelected();
				}
			}
		});
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(115, 150);
		scrollPane.setContent(tableOfArticleTypes);

		submitButton = new Button("Submit");
 		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage(); 
				Properties props = getInput();

				myModel.stateChangeRequest("DoModifyArticleType", props);
				getEntryTableModelValues();
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
		ArticleTypeTableModel selectedItem = tableOfArticleTypes.getSelectionModel().getSelectedItem();
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

	public Properties getInput() {
		Properties props = new Properties();

		try {
            String descriptionString = description.getText();
            String alphaCodeString = alphaCode.getText();

            if (alphaCodeString.isEmpty() && descriptionString.isEmpty()) {
				return props;
			}

			if (!alphaCodeString.isEmpty()) {
				props.setProperty("alphaCode", alphaCodeString);
			} 

			if (!descriptionString.isEmpty()) {
				props.setProperty("description", descriptionString);
			}
            
        }
        catch(NullPointerException e ){

        }
        catch (Exception e) {

        }

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