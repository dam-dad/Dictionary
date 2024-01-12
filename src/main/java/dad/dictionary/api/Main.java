package dad.dictionary.api;

public class Main {

	public static void main(String[] args) throws Exception {

		DictionaryService service = new DictionaryService();
				
		System.out.println(service.getDefinition("table").get(0).getMeanings().get(0).getDefinitions().get(0).getDefinition());

		System.out.println(service.getDefinition("chair").get(0).getMeanings().get(0).getDefinitions().get(0).getDefinition());

		System.out.println(service.getDefinition("mouse").get(0).getMeanings().get(1).getDefinitions().get(0).getDefinition());
		
	}

}
