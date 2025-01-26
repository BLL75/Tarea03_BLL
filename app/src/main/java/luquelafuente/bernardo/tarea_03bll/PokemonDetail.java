package luquelafuente.bernardo.tarea_03bll;

import java.util.List;

/**
 * Clase que representa los detalles de un Pokémon obtenidos desde la API.
 */
public class PokemonDetail {

    private int id;  // ID único del Pokémon
    private String name;  // Nombre del Pokémon
    private int height;  // Altura del Pokémon en decímetros
    private int weight;  // Peso del Pokémon en hectogramos
    private List<TypeWrapper> types; // Lista de tipos asociados al Pokémon
    private Sprites sprites; // Representa las imágenes asociadas al Pokémon

    /**
     * Clase que envuelve la información del tipo del Pokémon.
     */
    public static class TypeWrapper {
        private Type type;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }
    }

    /**
     * Clase que representa un tipo individual de Pokémon.
     */
    public static class Type {
        private String name; // Nombre del tipo (e.g., "fire", "water")

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Clase que representa las imágenes del Pokémon.
     */
    public static class Sprites {
        private String front_default; // URL de la imagen principal del Pokémon

        public String getFrontDefault() {
            return front_default;
        }

        public void setFrontDefault(String front_default) {
            this.front_default = front_default;
        }
    }

    // Getters y Setters para los campos principales
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<TypeWrapper> getTypes() {
        return types;
    }

    public void setTypes(List<TypeWrapper> types) {
        this.types = types;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }
}
