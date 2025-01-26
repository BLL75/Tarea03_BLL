package luquelafuente.bernardo.tarea_03bll;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interfaz para interactuar con la API de Pokémon.
 */
public interface PokemonApi {

    /**
     * Obtiene una lista básica de Pokémon.
     *
     * @param offset Punto de inicio para la lista (paginación).
     * @param limit  Número máximo de Pokémon a devolver.
     * @return Llamada que devuelve un {@link PokemonResponse}.
     */
    @GET("pokemon")
    Call<PokemonResponse> getPokemonList(
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    /**
     * Obtiene los detalles de un Pokémon específico.
     *
     * @param pokemonId ID del Pokémon.
     * @return Llamada que devuelve un {@link PokemonDetail}.
     */
    @GET("pokemon/{id}")
    Call<PokemonDetail> getPokemonDetails(@Path("id") String pokemonId);
}
