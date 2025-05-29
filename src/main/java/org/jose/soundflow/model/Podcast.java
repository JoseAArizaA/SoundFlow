package org.jose.soundflow.model;

public class Podcast extends Audio{
    private String tematica;

    public Podcast() {}

    public Podcast(int idAudio, String titulo, String artista, String descripcion, int duracion, Usuario usuario, String tematica) {
        super(idAudio, titulo, artista, descripcion, duracion, usuario);
        this.tematica = tematica;
    }

    public String getTematica() {
        return tematica;
    }

    public void setTematica(String tematica) {
        this.tematica = tematica;
    }

    @Override
    public String toString() {
        return super.toString() + "Tematica: " + tematica;
    }
}
