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
}
