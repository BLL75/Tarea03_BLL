package luquelafuente.bernardo.tarea_03bll;

/**
 * Clase que representa un Pokémon con sus atributos principales.
 */
public class Pokemon {

    // Atributos de la clase
    private String nombre; // Nombre del Pokémon
    private String indice; // Índice de la Pokédex
    private String tipos;  // Tipos del Pokémon
    private String peso;   // Peso del Pokémon
    private String altura; // Altura del Pokémon
    private String foto;   // URL de la imagen del Pokémon

    /**
     * Constructor vacío necesario para Firebase y otras librerías.
     */
    public Pokemon() {}

    /**
     * Constructor completo de la clase Pokemon.
     *
     * @param nombre Nombre del Pokémon.
     * @param indice Índice de la Pokédex.
     * @param tipos Tipos del Pokémon.
     * @param peso Peso del Pokémon.
     * @param altura Altura del Pokémon.
     * @param foto URL de la imagen del Pokémon.
     */
    public Pokemon(String nombre, String indice, String tipos, String peso, String altura, String foto) {
        this.nombre = nombre;
        this.indice = indice;
        this.tipos = tipos;
        this.peso = peso;
        this.altura = altura;
        this.foto = foto;
    }

    // Getters y setters para cada atributo

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getTipos() {
        return tipos;
    }

    public void setTipos(String tipos) {
        this.tipos = tipos;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    /**
     * Devuelve una representación en String del objeto.
     *
     * @return String con los datos principales del Pokémon.
     */
    @Override
    public String toString() {
        return "Pokemon{" +
                "nombre='" + nombre + '\'' +
                ", indice='" + indice + '\'' +
                ", tipos='" + tipos + '\'' +
                ", peso='" + peso + '\'' +
                ", altura='" + altura + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
