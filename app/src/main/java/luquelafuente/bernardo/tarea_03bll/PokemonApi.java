package luquelafuente.bernardo.tarea_03bll;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokemonApi {
    @GET("pokemon")
    Call<PokemonResponse> getPokemonList(
            @Query("offset") int offset,  // Parámetro para el punto de inicio
            @Query("limit") int limit    // Parámetro para la cantidad de Pokémon a cargar
    );
}
