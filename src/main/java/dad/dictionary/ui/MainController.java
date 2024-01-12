package dad.dictionary.ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import dad.dictionary.api.DictionaryService;
import dad.dictionary.api.model.Definition;
import dad.dictionary.api.model.Dictionary;
import dad.dictionary.api.model.Meaning;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
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
	
	// logic (lógica de negocio)
	
	private DictionaryService dictionary = new DictionaryService();
	private Random random = new Random();
	
	// model (modelo de datos)
	
	private StringProperty word = new SimpleStringProperty();
	private StringProperty definition = new SimpleStringProperty();
	
	// view (vista)
	
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
			// carga la vista a partri del fichero FXML
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

    	// crea una tarea para hacer la búsqueda de la palabra en la API en segundo plano
    	Task<String> task = new Task<String>() {
			
			@Override
			protected String call() throws Exception {
				
				try { 
				
					// actualiza el mensaje de la tarea, para indicar que está buscando (feedback de la tarea en segundo plano)
					updateMessage("Searching ...");
	
					// usa el servicio de diccionario (API) para buscar la palabra
					List<Dictionary> wordDefinition = dictionary.getDefinition(word.get());
					
					// elige un significado aleatorio de entre todos los devueltos por el diccionario
					int mean = random.nextInt(0, wordDefinition.get(0).getMeanings().size());
					
					// elige una definición aleatoria dentro del significado
					int def = random.nextInt(0, wordDefinition.get(0).getMeanings().get(mean).getDefinitions().size());
					
					// guarda referencias al significado y a la definición
					Meaning foundMeaning = wordDefinition.get(0).getMeanings().get(mean);
					Definition foundDefinition = foundMeaning.getDefinitions().get(def);
					
					// actualiza el mensaje de la tarea, para indicar que terminó la búsqueda y se puede volver a buscar
					updateMessage("Search definition");

					// devuelve el resultado de la tarea
					return "[" + foundMeaning.getPartOfSpeech() + "] " + foundDefinition.getDefinition();
					
				} catch (Exception e) {
					
					// en caso de error, actualizamos el mensaje de la tarea y relanzamos la incidencia para que se dispare el evento "onFailed"
					updateMessage("Error ocurred ... search again!!");
					throw e;
					
				}
			}
			
		};

		// pone un listener al evento "onSchedule" que se dispara justo antes de iniciar la tarea  
		task.setOnScheduled(e -> {
			
			// bindea el texto del botón al mensaje de la tarea (usamos el botón para mostrar el estado de la búsqueda) 
			searchButton.textProperty().bind(task.messageProperty());
			
			// bindea la deshabilitación del botón para que se desactive si la tarea está "running" o la palabra está vacía  
			searchButton.disableProperty().bind(task.runningProperty().or(word.isEmpty()));
			
		});
		
		// pone un listener al evento "onSucceeded" que se dispara si la tarea termina sin errores
		task.setOnSucceeded(e -> {
			
			definition.set(task.getValue());
			
		});
		
		// pone un listener al evento "onFailed" que se dispara si la tarea lanza un excepción
		task.setOnFailed(e -> {			
			Alert error = new Alert(AlertType.ERROR);
			error.initOwner(DictionaryApp.primaryStage);
			error.setTitle("Error");
			error.setHeaderText("Error searching in dictionary");
			error.setContentText(task.getException().getMessage());
			error.showAndWait();			
		});
		
		// inicia la tarea dentro de su propio hilo (segundo plano)
		new Thread(task).start();
    	
    }
	
}
