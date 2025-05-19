package org.jose.soundflow.model;

import org.jose.soundflow.utils.Utilidades;

import java.util.ArrayList;
import java.util.Objects;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String correo;
    private String password;
    private ArrayList<Audio> misAudios;
    private ArrayList<ListaReproduccion> misListasReproduccion;

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String correo){
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.misAudios = new ArrayList<>();
        this.misListasReproduccion = new ArrayList<>();
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if (Utilidades.validarCorreo(correo)) {
            this.correo = correo;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Audio> getMisAudios() {
        return misAudios;
    }

    public void setMisAudios(ArrayList<Audio> misAudios) {
        this.misAudios = misAudios;
    }

    public ArrayList<ListaReproduccion> getMisListasReproduccion() {
        return misListasReproduccion;
    }

    public void setMisListasReproduccion(ArrayList<ListaReproduccion> misListasReproduccion) {
        this.misListasReproduccion = misListasReproduccion;
    }

    //Agregar un aduio a la lista de audios
    public boolean agregarAudio(Audio audio){
        boolean result = false;
        if(audio != null && !this.misAudios.contains(audio)){
            this.misAudios.add(audio);
            result =true;
        }
        return result;
    }

    //Eliminar un audio de la lista de audios
    public boolean eliminarAudio(Audio audio){
        boolean result = false;
        if(misAudios.contains(audio)){
            this.misAudios.remove(audio);
            result =  true;
        }
        return result;
    }

    //Agregar una lista de reproduccion
    public boolean agregarListaReproduccion(ListaReproduccion lista){
        boolean result = false;
        if(lista != null && !this.misListasReproduccion.contains(lista)){
            this.misListasReproduccion.add(lista);
            result = true;
        }
        return result;
    }

    //Eliminar una lista de reproduccion
    public boolean eliminarListaReproduccion(ListaReproduccion lista){
        boolean result = false;
        if(misListasReproduccion.contains(lista)){
            this.misListasReproduccion.remove(lista);
            result = true;
        }
        return result;
    }

    //Metodo que devuelve el total de audios
    public int totalAudios(){
        return this.misAudios.size();
    }

    //Metodo que devuelve el total de listas de reproduccion
    public int totalListasReproduccion(){
        return this.misListasReproduccion.size();
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return idUsuario == usuario.idUsuario && Objects.equals(nombre, usuario.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, nombre);
    }
}
