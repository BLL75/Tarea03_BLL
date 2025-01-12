package luquelafuente.bernardo.tarea_03bll;

import com.squareup.moshi.Json;

public class PokemonResult {
    @Json(name = "name")
    private String name;

    @Json(name = "url")
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}