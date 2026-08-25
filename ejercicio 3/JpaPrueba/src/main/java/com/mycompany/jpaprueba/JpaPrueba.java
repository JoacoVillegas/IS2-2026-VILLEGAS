/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.jpaprueba;

import com.mycompany.jpaprueba.logica.Alumno;
import com.mycompany.jpaprueba.logica.Controladora;
import com.mycompany.jpaprueba.persistencia.ControladoraPersistencia;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author joavi
 */
public class JpaPrueba {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        Controladora control = new Controladora();
        
       // Date fechaNac = new Date();
        Alumno alumno1 = new Alumno(13, "Grimes", "Rick", new Date());
        control.crearAlumno(alumno1);
        
        //control.eliminarAlumno(10);
        //control.editarAlumno(alumno1);
        
        Alumno alu1 = control.traerAlumno(11);
        System.out.println(alu1.toString());
        
        ArrayList<Alumno> listaAlumnos = control.traerListaAlumnos();
        for (Alumno al : listaAlumnos){
            System.out.println("El alumno es: " + al.toString());
        }
    }
}
