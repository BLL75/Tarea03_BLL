package luquelafuente.bernardo.tarea_03bll;

import java.util.List;

public class PokemonResponse {
    private List<Result> results; // Lista de resultados (Pokémon)

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static class Result {
        private String name; // Nombre del Pokémon
        private String url;  // URL para obtener más detalles

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
