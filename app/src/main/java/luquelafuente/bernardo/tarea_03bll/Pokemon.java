package luquelafuente.bernardo.tarea_03bll;
public class Pokemon {
    private String nombre;
    private String indice;
    private String tipos;
    private String peso;
    private String altura;
    private String foto;


    public Pokemon(){}
    public Pokemon(String nombre, String indice, String tipos, String peso, String altura, String foto) {
        this.nombre = nombre;
        this.indice = indice;
        this.tipos = tipos;
        this.peso = peso;
        this.altura = altura;
        this.foto = foto;
    }

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
}
