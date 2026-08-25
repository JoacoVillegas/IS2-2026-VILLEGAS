/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jpaprueba.logica;

import com.mycompany.jpaprueba.persistencia.ControladoraPersistencia;
import java.util.ArrayList;

/**
 *
 * @author joavi
 */
public class Controladora {
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();

    public void crearAlumno(Alumno alu){
        controlPersis.crear(alu);
    }
    
    public void eliminarAlumno(int id){
        controlPersis.eliminar(id);
    }
    
    public void editarAlumno(Alumno alu){
        controlPersis.editar(alu);
    }
    
    public Alumno traerAlumno(int id){
        return controlPersis.traer(id);
    }
    
    public ArrayList<Alumno> traerListaAlumnos(){
        return controlPersis.traerLista();
    }
    
    //metodos de carrera
    public void crearCarrera(Carrera cr){
        controlPersis.crearCarrera(cr);
    }
    
    public void eliminarCarrera(int id){
        controlPersis.eliminarCarrera(id);
    }
    
    public void editarCarrera(Carrera cr){
        controlPersis.editarCarrera(cr);
    }
    
    public Carrera traerCarrera(int id){
        return controlPersis.traerCarrera(id);
    }
    
    public ArrayList<Carrera> traerListaCarreras(){
        return controlPersis.traerListaCarrera();
    }
    
        //Materia
    public void crearMateria(Materia mate) {
        controlPersis.crearMateria(mate);
    }
    
    public void eliminarMateria(int id) {
        controlPersis.eliminarMateria(id);
    }
    
    public void editarMateria(Materia mate) {
        controlPersis.editarMateria(mate);
    }
    
    public Materia traerMateria(int id) {
        return controlPersis.traerMateria(id);
    }
    
    public ArrayList<Materia> traerListaMaterias() {
        return controlPersis.traerListaMateria();
    }
}
