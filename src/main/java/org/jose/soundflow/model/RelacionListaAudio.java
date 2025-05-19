package org.jose.soundflow.model;

import java.time.LocalDate;
import java.util.Objects;

public class RelacionListaAudio {
    private ListaReproduccion lista;
    private Audio audio;
    private LocalDate fechaAgregada;

    public RelacionListaAudio(ListaReproduccion lista, Audio audio, LocalDate fechaAgregada) {
        this.lista = lista;
        this.audio = audio;
        this.fechaAgregada = fechaAgregada;
    }

    public ListaReproduccion getLista() {
        return lista;
    }

    public void setLista(ListaReproduccion lista) {
        this.lista = lista;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public LocalDate getFechaAgregada() {
        return fechaAgregada;
    }

    public void setFechaAgregada(LocalDate fechaAgregada) {
        this.fechaAgregada = fechaAgregada;
    }

    @Override
    public String toString() {
        return "RelacionListaAudio{" +
                "lista=" + lista +
                ", audio=" + audio +
                ", fechaAgregada=" + fechaAgregada +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RelacionListaAudio that = (RelacionListaAudio) o;
        return Objects.equals(lista, that.lista) && Objects.equals(audio, that.audio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lista, audio);
    }
}
