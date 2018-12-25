import java.io.IOException;
import java.io.RandomAccessFile;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

class AddressBookPane extends GridPane {
	//Singleton
	private static final int MAX_INSTANCES = 3;
	private static int num_of_instances = 0;
	
	private RandomAccessFile raf;
	// Text fields
	private TextField jtfName = new TextField();
	private TextField jtfStreet = new TextField();
	private TextField jtfCity = new TextField();
	private TextField jtfState = new TextField();
	private TextField jtfZip = new TextField();
	// Buttons
	private FlowPane jpButton;
	private FirstButton jbtFirst;
	private NextButton jbtNext;
	private PreviousButton jbtPrevious;
	private LastButton jbtLast;
	public EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent arg0) {
			((Command) arg0.getSource()).Execute();
		}
	};

	private AddressBookPane() { // Open or create a random access file
		try {
			raf = new RandomAccessFile("address.dat", "rw");
		} catch (IOException ex) {
			System.out.print("Error: " + ex);
			System.exit(0);
		}
		jtfState.setAlignment(Pos.CENTER_LEFT);
		jtfState.setPrefWidth(90);
		jtfZip.setPrefWidth(70);
		jbtFirst = new FirstButton(this, raf);
		jbtNext = new NextButton(this, raf);
		jbtPrevious = new PreviousButton(this, raf);
		jbtLast = new LastButton(this, raf);
		Label state = new Label("State");
		Label zp = new Label("Zip");
		Label name = new Label("Name");
		Label street = new Label("Street");
		Label city = new Label("City");
		// Panel p1 for holding labels Name, Street, and City
		GridPane p1 = new GridPane();
		p1.add(name, 0, 0);
		p1.add(street, 0, 1);
		p1.add(city, 0, 2);
		p1.setAlignment(Pos.CENTER_LEFT);
		p1.setVgap(8);
		p1.setPadding(new Insets(0, 2, 0, 2));
		GridPane.setVgrow(name, Priority.ALWAYS);
		GridPane.setVgrow(street, Priority.ALWAYS);
		GridPane.setVgrow(city, Priority.ALWAYS);
		// City Row
		GridPane adP = new GridPane();
		adP.add(jtfCity, 0, 0);
		adP.add(state, 1, 0);
		adP.add(jtfState, 2, 0);
		adP.add(zp, 3, 0);
		adP.add(jtfZip, 4, 0);
		adP.setAlignment(Pos.CENTER_LEFT);
		GridPane.setHgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfState, Priority.ALWAYS);
		GridPane.setVgrow(jtfZip, Priority.ALWAYS);
		GridPane.setVgrow(state, Priority.ALWAYS);
		GridPane.setVgrow(zp, Priority.ALWAYS);
		// Panel p4 for holding jtfName, jtfStreet, and p3
		GridPane p4 = new GridPane();
		p4.add(jtfName, 0, 0);
		p4.add(jtfStreet, 0, 1);
		p4.add(adP, 0, 2);
		p4.setVgap(1);
		GridPane.setHgrow(jtfName, Priority.ALWAYS);
		GridPane.setHgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setHgrow(adP, Priority.ALWAYS);
		GridPane.setVgrow(jtfName, Priority.ALWAYS);
		GridPane.setVgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setVgrow(adP, Priority.ALWAYS);
		// Place p1 and p4 into jpAddress
		GridPane jpAddress = new GridPane();
		jpAddress.add(p1, 0, 0);
		jpAddress.add(p4, 1, 0);
		GridPane.setHgrow(p1, Priority.NEVER);
		GridPane.setHgrow(p4, Priority.ALWAYS);
		GridPane.setVgrow(p1, Priority.ALWAYS);
		GridPane.setVgrow(p4, Priority.ALWAYS);
		// Set the panel with line border
		jpAddress.setStyle("-fx-border-color: grey;" + " -fx-border-width: 1;" + " -fx-border-style: solid outside ;");
		// Add buttons to a panel
		jpButton = new FlowPane();
		jpButton.setHgap(5);
		jpButton.getChildren().addAll(jbtFirst, jbtNext, jbtPrevious, jbtLast);
		jpButton.setAlignment(Pos.CENTER);
		GridPane.setVgrow(jpButton, Priority.NEVER);
		GridPane.setVgrow(jpAddress, Priority.ALWAYS);
		GridPane.setHgrow(jpButton, Priority.ALWAYS);
		GridPane.setHgrow(jpAddress, Priority.ALWAYS);
		// Add jpAddress and jpButton to the stage
		this.setVgap(5);
		this.add(jpAddress, 0, 0);
		this.add(jpButton, 0, 1);
		jbtFirst.setOnAction(ae);
		jbtNext.setOnAction(ae);
		jbtPrevious.setOnAction(ae);
		jbtLast.setOnAction(ae);
		jbtFirst.Execute();
	}

	public static AddressBookPane getInstance()
	{ if (num_of_instances >= MAX_INSTANCES)
		return null;
	  else
	  { num_of_instances++;
	    AddressBookPane p = new AddressBookPane();
	    if(num_of_instances == 1)
	    	new AddressBookDecorator(p);
	    return p;
	  }
	}
	
	public static void reduceNumberOfObjects()
	{ num_of_instances--;
	}
	public static int getNumberOfObjects()
	{ return num_of_instances;
	}
	public static void resetNumberOfObjects()
	{  num_of_instances = 0;
	}
	
	public FlowPane getButtonsPane() {
		return jpButton;
	}
	
	public RandomAccessFile getRaf() {
		return this.raf;
	}

	public void actionHandled(ActionEvent e) {
		((Command) e.getSource()).Execute();
	}

	public void SetName(String text) {
		jtfName.setText(text);
	}

	public void SetStreet(String text) {
		jtfStreet.setText(text);
	}

	public void SetCity(String text) {
		jtfCity.setText(text);
	}

	public void SetState(String text) {
		jtfState.setText(text);
	}

	public void SetZip(String text) {
		jtfZip.setText(text);
	}

	public void clearTextFields()
	{ jtfName.setText("");
	  jtfStreet.setText("");
	  jtfCity.setText("");
	  jtfState.setText("");
	  jtfZip.setText("");
	}
	
	public String GetName() {
		return jtfName.getText();
	}

	public String GetStreet() {
		return jtfStreet.getText();
	}

	public String GetCity() {
		return jtfCity.getText();
	}

	public String GetState() {
		return jtfState.getText();
	}

	public String GetZip() {
		return jtfZip.getText();
	}

	class NextButton extends CommandButton
	{ public NextButton(AddressBookPane pane, RandomAccessFile r)
	  {	super(pane, r);
		this.setText("Next");
	  }
	  @Override
	  public void Execute()
	  {	try
	    { long currentPosition = raf.getFilePointer();
		  if (currentPosition < raf.length())
			readAddressFile(currentPosition);
		} 
	    catch (IOException ex)
	    { ex.printStackTrace();
		}
	  }
	}
	class PreviousButton extends CommandButton
	{ public PreviousButton(AddressBookPane pane, RandomAccessFile r)
	  {	super(pane, r);
		this.setText("Previous");
	  }
	  @Override
	  public void Execute()
	  {	try
	    { long currentPosition = raf.getFilePointer();
		  if (currentPosition - 2 * 2 * RECORD_SIZE >= 0)
		 	    readAddressFile(currentPosition - 2 * 2 * RECORD_SIZE);
		  else;
		  } 
	      catch (IOException ex)
	      {	ex.printStackTrace();
		  }
		}
	  }
	class LastButton extends CommandButton
	{ public LastButton(AddressBookPane pane, RandomAccessFile r)
	  {	super(pane, r);
		this.setText("Last");
	  }
	  @Override
	  public void Execute()
	  {	try
	    { long lastPosition = raf.length();
		  if (lastPosition > 0)
			readAddressFile(lastPosition - 2 * RECORD_SIZE);
		} 
	    catch (IOException ex)
	    { ex.printStackTrace();
		}
	   }
	}
	class FirstButton extends CommandButton
	{ public FirstButton(AddressBookPane pane, RandomAccessFile r)
	  {	super(pane, r);
		this.setText("First");
	  }
	  @Override
	  public void Execute()
	  {	try
	    { if (raf.length() > 0) readAddressFile(0);
		} 
	    catch (IOException ex)
	    { ex.printStackTrace();
		}
	  }
	}

	
}