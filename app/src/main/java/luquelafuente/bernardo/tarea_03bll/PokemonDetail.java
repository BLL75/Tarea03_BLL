package luquelafuente.bernardo.tarea_03bll;

import java.util.List;

public class PokemonDetail {
    private int id;  // ID del Pokémon
    private String name;  // Nombre del Pokémon
    private int height;  // Altura del Pokémon
    private int weight;  // Peso del Pokémon
    private List<TypeWrapper> types; // Tipos del Pokémon
    private Sprites sprites; // Imágenes del Pokémon

    public static class TypeWrapper {
        private Type type;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }
    }

    public static class Type {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Sprites {
        private String front_default; // Imagen del Pokémon

        public String getFrontDefault() {
            return front_default;
        }

        public void setFrontDefault(String front_default) {
            this.front_default = front_default;
        }
    }

    // Getters y Setters...
}
