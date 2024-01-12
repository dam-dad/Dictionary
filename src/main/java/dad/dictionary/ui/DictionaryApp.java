package dad.dictionary.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DictionaryApp extends Application {
	
	public static Stage primaryStage;
	
	private MainController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {

		DictionaryApp.primaryStage = primaryStage;
				
		controller = new MainController();
		
		primaryStage.setTitle("Dictionary");
		primaryStage.setScene(new Scene(controller.getView()));
		primaryStage.show();		
		
	}

}
