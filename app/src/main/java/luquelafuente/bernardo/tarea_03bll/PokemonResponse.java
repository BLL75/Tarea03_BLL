package luquelafuente.bernardo.tarea_03bll;

import com.squareup.moshi.Json;

import java.util.List;

public class PokemonResponse {

    @Json(name = "results")
    private List<PokemonResult> pokemonResults;

    public List<PokemonResult> getPokemonResults() {
        return pokemonResults;
    }
}
