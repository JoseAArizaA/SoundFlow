package org.jose.soundflow.model;

import java.util.Objects;

public class Audio {
    private int idAudio;
    private String titulo;
    private String artista;
    private String descripcion;
    private int duracion;
    private TipoContenido tipoAudio;
    private Usuario usuario;

    public Audio() {}

    public Audio(int idAudio, String titulo, String artista, String descripcion, int duracion, TipoContenido tipoAudio, Usuario usuario) {
        this.idAudio = idAudio;
        this.titulo = titulo;
        this.artista = artista;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.tipoAudio = tipoAudio;
        this.usuario = usuario;
    }

    public int getIdAudio() {
        return idAudio;
    }

    public void setIdAudio(int idAudio) {
        this.idAudio = idAudio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public TipoContenido getTipoAudio() {
        return tipoAudio;
    }

    public void setTipoAudio(TipoContenido tipoAudio) {
        this.tipoAudio = tipoAudio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Audio->" +
                "idAudio:" + idAudio +
                ", titulo:'" + titulo + '\'' +
                ", artista:'" + artista + '\'' +
                ", descripcion:'" + descripcion + '\'' +
                ", duracion:" + duracion +
                ", tipoAudio:" + tipoAudio +
                ", usuario:" + usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Audio audio = (Audio) o;
        return idAudio == audio.idAudio && Objects.equals(titulo, audio.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAudio, titulo);
    }
}
