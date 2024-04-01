//Specify Package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;

/*BOOKSEARCHVIEW CLASS=================================================
 * Class contains view for Librarian application
 */

 public class SearchArticleTypeView extends View{
    //Declare GUI controls
    private Button subButton;
    private Button cancelButton;
    private TextField searchField;

    //Show error message
    private MessageView statusLog;

    //---------------------------------------------
    /*CONSTRUCTOR
     * Takes model object from ViewFactory
     */

     public SearchArticleTypeView(IModel clerk){

        super(clerk, "SearchArticleTypeView");

        //Create container
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        //Create GUI components, and add
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        //Error message area
        container.getChildren().add(createStatusLog(""));
        
        getChildren().add(container);

        populateFields();

        myModel.subscribe("TransactionStatus", this);
     }//END CONSTRUCTOR--------------------------------

     //-------------------------------------------------
     /*createTitle
      * Create title field for the view
      */
    private Node createTitle(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" SEARCH ARTICLE TYPE ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);
		
		return container;

    }//End createTitle-------------------------------------

    //---------------------------------------------------
    /*createFormContent
     * Method creates actual form for user input
     */
    private VBox createFormContent(){
        VBox vbox = new VBox(10);
        
        //Establish grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
       	grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //SearchLabel and Text Field------------
        Text searchLabel = new Text("Enter AlphaCode: ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        searchLabel.setFont(myFont);
        searchLabel.setWrappingWidth(150);
        searchLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(searchLabel, 0, 0);

        searchField = new TextField();
        searchField.setEditable(true);
        grid.add(searchField, 0, 1);

        //Setup separate hbox for submit and back buttons
		HBox subBack = new HBox(10);
		subBack.setAlignment(Pos.BOTTOM_CENTER);
		
		//Submit Button---------------------------------
		subButton = new Button("Submit");
		subButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		subButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent e) {
				System.out.println("Debug: submit button registered");
				clearErrorMessage();
				processSubAction(e);				
			}
		});
		subBack.getChildren().add(subButton);
		
		//Back Button-----------------------------------
		cancelButton = new Button("Back");
		cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		    	clearErrorMessage();
       		    	myModel.stateChangeRequest("BackToLibrarian", null);   
            	  }
        	});
		subBack.getChildren().add(cancelButton);
		
		//Add form and buttons
		vbox.getChildren().add(grid);
		vbox.getChildren().add(subBack);

		return vbox;
        
    }//End createFormContent----------------------------

    //----------------------------------------------------
    /*processSubACtion
     * On submit click method will set up BookCollection
     */
    public void processSubAction(Event evt){
        //Validate user input
        if (searchField == null){
            clearErrorMessage();
            displayErrorMessage("Invalid input");
        } else {

            //Convert textfield to string
            String searchString = searchField.getText();

            //Call Clerk method to create book collection
            //and search
            myModel.stateChangeRequest("searchBook", searchString);

        }

    }//End processSubAction------------------------------

    // Create the status log field
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void populateFields()
	{
		searchField.setText((String)myModel.getState("SearchBook"));
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		clearErrorMessage();
		String temp = ((String)value);

		if (key.equals("searchBookMessage") == true)
		{
			String val = (String)value;
			searchField.setText(val);
			displayMessage(val);
		}
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
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


 }//END CLASS===============================================

