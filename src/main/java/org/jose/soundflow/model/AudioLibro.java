package org.jose.soundflow.model;

public class AudioLibro extends Audio{
    private String idioma;

    public AudioLibro() {}

    public AudioLibro(int idAudio, String titulo, String artista, String descripcion, int duracion, TipoContenido tipoAudio, Usuario usuario, String idioma) {
        super(idAudio, titulo, artista, descripcion, duracion, tipoAudio, usuario);
        this.idioma = idioma;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    @Override
    public String toString() {
        return super.toString() + "idioma: " + idioma;
    }


}
