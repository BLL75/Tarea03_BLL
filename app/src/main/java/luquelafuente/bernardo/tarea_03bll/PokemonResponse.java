package luquelafuente.bernardo.tarea_03bll;

import java.util.List;

/**
 * Clase que representa la respuesta de la API al obtener una lista de Pokémon.
 * Contiene una lista de resultados que incluye los nombres y las URLs de los Pokémon.
 */
public class PokemonResponse {

    private List<Result> results; // Lista de resultados que contiene información básica de los Pokémon

    /**
     * Obtiene la lista de resultados (Pokémon).
     *
     * @return Lista de resultados.
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * Establece la lista de resultados (Pokémon).
     *
     * @param results Lista de resultados.
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     * Clase interna que representa un resultado individual de la lista de Pokémon.
     */
    public static class Result {
        private String name; // Nombre del Pokémon
        private String url;  // URL para obtener más detalles del Pokémon

        /**
         * Obtiene el nombre del Pokémon.
         *
         * @return Nombre del Pokémon.
         */
        public String getName() {
            return name;
        }

        /**
         * Establece el nombre del Pokémon.
         *
         * @param name Nombre del Pokémon.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Obtiene la URL con más detalles del Pokémon.
         *
         * @return URL del Pokémon.
         */
        public String getUrl() {
            return url;
        }

        /**
         * Establece la URL con más detalles del Pokémon.
         *
         * @param url URL del Pokémon.
         */
        public void setUrl(String url) {
            this.url = url;
        }
    }
}
