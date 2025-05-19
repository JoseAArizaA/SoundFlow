package org.jose.soundflow.model;

import java.util.ArrayList;
import java.util.Objects;

public class ListaReproduccion {
    private int idLista;
    private String nombreLista;
    private Usuario usuario;
    private ArrayList<Audio> audios;

    public ListaReproduccion() {}

    public ListaReproduccion(int idLista, String nombreLista, Usuario usuario) {
        this.idLista = idLista;
        this.nombreLista = nombreLista;
        this.usuario = usuario;
        this.audios = new ArrayList<>();
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ArrayList<Audio> getAudios() {
        return audios;
    }

    public void setAudios(ArrayList<Audio> audios) {
        this.audios = audios;
    }

    @Override
    public String toString() {
        return "ListaReproduccion{" +
                "idLista=" + idLista +
                ", nombreLista='" + nombreLista + '\'' +
                ", usuario=" + usuario +
                ", audios=" + audios +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ListaReproduccion that = (ListaReproduccion) o;
        return idLista == that.idLista && Objects.equals(nombreLista, that.nombreLista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLista, nombreLista);
    }
}
