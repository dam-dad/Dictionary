package dad.dictionary.api;

import java.util.List;

import dad.dictionary.api.model.Dictionary;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryInterface {
	
	@GET("entries/en/{word}")
	public Call<List<Dictionary>> getDefinition(@Path("word") String word);

}
