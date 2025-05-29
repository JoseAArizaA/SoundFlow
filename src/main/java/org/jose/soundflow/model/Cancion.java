package org.jose.soundflow.model;

public class Cancion extends Audio {
    private String genero;

    public Cancion() {}

    public Cancion(int idAudio, String titulo, String artista, String descripcion, int duracion, Usuario usuario, String genero) {
        super(idAudio, titulo, artista, descripcion, duracion, usuario);
        this.genero = genero;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return super.toString() + "genero:" + genero;
    }
}
