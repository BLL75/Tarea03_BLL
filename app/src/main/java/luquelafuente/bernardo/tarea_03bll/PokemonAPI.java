package luquelafuente.bernardo.tarea_03bll;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokemonAPI{
    @GET("pokemon")
    Call<PokemonResponse> getPokemons(@Query("limit") int limit, @Query("offset") int offset);
}
