package luquelafuente.bernardo.tarea_03bll;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://pokeapi.co/api/v2/"; // Dirección base de la API
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {  // Si aún no se ha creado una instancia
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Establece la URL base de las solicitudes
                    .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON a objetos Java
                    .build(); // Crea la instancia de Retrofit
        }
        return retrofit; // Devuelve la instancia configurada
    }
}

