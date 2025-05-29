package org.jose.soundflow.model;

import org.jose.soundflow.DAO.RelacionListaAudioDAO;

import java.util.ArrayList;
import java.util.Objects;

public class Audio {
    private int idAudio;
    private String titulo;
    private String artista;
    private String descripcion;
    private int duracion;
    private Usuario usuario;
    private ArrayList<RelacionListaAudio> relaciones;

    public Audio() {}

    public Audio(int idAudio, String titulo, String artista, String descripcion, int duracion, Usuario usuario, ArrayList<RelacionListaAudio> relaciones) {
        this.idAudio = idAudio;
        this.titulo = titulo;
        this.artista = artista;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.usuario = usuario;
        this.relaciones = relaciones;
    }

    //Nuevo constructor
    public Audio(int idAudio, String titulo, String artista, String descripcion,
                 int duracion, Usuario usuario) {
        this.idAudio = idAudio;
        this.titulo = titulo;
        this.artista = artista;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.usuario = usuario;
        this.relaciones = new ArrayList<>(); // lo inicializa vacío
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ArrayList<RelacionListaAudio> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(ArrayList<RelacionListaAudio> relaciones) {
        this.relaciones = relaciones;
    }

    // Cambios 28/05
    public String getTipoComoTexto() {
        if (this instanceof Cancion) {
            return "Canción";
        } else if (this instanceof Podcast) {
            return "Podcast";
        } else if (this instanceof AudioLibro) {
            return "Audiolibro";
        } else {
            return "Desconocido";
        }
    }

    @Override
    public String toString() {
        return titulo + " - " + artista;
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
