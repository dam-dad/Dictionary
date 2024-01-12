package dad.dictionary.api;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dad.dictionary.api.model.Dictionary;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DictionaryService {
	
	private static final String BASE_URL = "https://api.dictionaryapi.dev/api/v2/";
	
	private DictionaryInterface service;
	
	public DictionaryService() {
		
		ConnectionPool pool = new ConnectionPool(1, 5, TimeUnit.SECONDS);
		
//		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//		interceptor.setLevel(Level.BODY);
		
		OkHttpClient client = new OkHttpClient.Builder()
                .connectionPool(pool)
//                .addInterceptor(interceptor)
                .build();
		
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.client(client)
				.build();
		
		service = retrofit.create(DictionaryInterface.class);
	}
	
	public List<Dictionary> getDefinition(String word) throws Exception {
		
		Call<List<Dictionary>> call = service.getDefinition(word);
		
		Response<List<Dictionary>> response = call.execute();
		
		return response.body();
		
	}

}
