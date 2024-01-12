package dad.dictionary.ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import dad.dictionary.api.DictionaryService;
import dad.dictionary.api.model.Dictionary;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {
	
	// logic
	
	private DictionaryService dictionary = new DictionaryService();
	private Random random = new Random();
	
	// model
	
	private StringProperty word = new SimpleStringProperty();
	private StringProperty definition = new SimpleStringProperty();
	
	// view
	
    @FXML
    private Label definitionLabel;

    @FXML
    private Button searchButton;

    @FXML
    private VBox view;

    @FXML
    private TextField wordText;
	
	public MainController() {
		try { 
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// bindings
		
		word.bind(wordText.textProperty());
		definitionLabel.textProperty().bind(definition);
		
		// deshabilita el botón si no hay palabra
		
		searchButton.disableProperty().bind(word.isEmpty());

	}
	
	public VBox getView() {
		return view;
	}

    @FXML
    void onSearch(ActionEvent event) {

    	try {
			List<Dictionary> wordDefinition = dictionary.getDefinition(word.get());
			
			// elige un meaning aleatorio
			int mean = random.nextInt(0, wordDefinition.get(0).getMeanings().size());
			// elige una definición aleatoria dentro del meaning
			int def = random.nextInt(0, wordDefinition.get(0).getMeanings().get(mean).getDefinitions().size());
			
			String firstDefinition = wordDefinition.get(0).getMeanings().get(mean).getDefinitions().get(def).getDefinition();
			definition.set(firstDefinition);
			
		} catch (Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.initOwner(DictionaryApp.primaryStage);
			error.setTitle("Error");
			error.setHeaderText("Error searching in dictionary");
			error.setContentText(e.getMessage());
			error.showAndWait();
		}
    	
    }
	
}
