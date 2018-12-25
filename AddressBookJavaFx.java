import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddressBookJavaFx extends Application {
	private static int NUMBER_OF_PANES = 4;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		if (NUMBER_OF_PANES <= 0) {
			System.out.println("no windows to create, closing...");
			System.exit(0);
		}
		Stage[] stages = new Stage[NUMBER_OF_PANES];
		Scene[] scenes = new Scene[NUMBER_OF_PANES];
		AddressBookPane[] panes = new AddressBookPane[NUMBER_OF_PANES];
		for (int i = 0; i < panes.length; i++) {
			panes[i] = AddressBookPane.getInstance();
			if (panes[i] != null) {
				scenes[i] = new Scene(panes[i],550,170);
				stages[i] = new Stage();
				stages[i].setTitle("AddressBook");
				stages[i].setScene(scenes[i]);
				stages[i].setResizable(true);
				stages[i].show();
				stages[i].setAlwaysOnTop(true);
				stages[i].setOnCloseRequest(event -> {
					AddressBookPane.reduceNumberOfObjects();
				});
			} else {
				System.out.println("Singelton violation. Only 3 panes were created.");
			}
		}
	}
}