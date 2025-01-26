package luquelafuente.bernardo.tarea_03bll;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase ApiClient
 *
 * Proporciona una instancia singleton de Retrofit configurada para realizar
 * solicitudes a la API de Pokémon. Esta clase sigue el patrón Singleton para
 * asegurarse de que solo exista una instancia de Retrofit.
 */
public class ApiClient {

    // URL base para acceder a la API de Pokémon
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    // Instancia única de Retrofit
    private static Retrofit retrofit;

    /**
     * Devuelve la instancia de Retrofit. Si aún no se ha creado, la configura y crea.
     *
     * @return Retrofit - Instancia de Retrofit configurada con la URL base y un convertidor JSON.
     */
    public static Retrofit getClient() {
        if (retrofit == null) {  // Comprueba si la instancia ya está creada
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Configura la URL base para las solicitudes
                    .addConverterFactory(GsonConverterFactory.create()) // Convierte respuestas JSON a objetos Java
                    .build(); // Crea la instancia de Retrofit
        }
        return retrofit; // Devuelve la instancia configurada
    }
}
