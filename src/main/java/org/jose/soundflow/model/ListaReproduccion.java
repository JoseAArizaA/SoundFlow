package org.jose.soundflow.model;

import java.util.ArrayList;
import java.util.Objects;

public class ListaReproduccion {
    private int idLista;
    private String nombreLista;
    private Usuario usuario;
    private ArrayList<RelacionListaAudio> relaciones;

    public ListaReproduccion() {}

    public ListaReproduccion(int idLista, String nombreLista, Usuario usuario, ArrayList<RelacionListaAudio> relaciones) {
        this.idLista = idLista;
        this.nombreLista = nombreLista;
        this.usuario = usuario;
        this.relaciones = relaciones;
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

    public ArrayList<RelacionListaAudio> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(ArrayList<RelacionListaAudio> relaciones) {
        this.relaciones = relaciones;
    }

    @Override
    public String toString() {
        return "ListaReproduccion{" +
                "idLista=" + idLista +
                ", nombreLista='" + nombreLista + '\'' +
                ", usuario=" + usuario +
                ", relaciones=" + relaciones +
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
