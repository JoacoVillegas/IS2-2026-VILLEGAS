/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jpaprueba.logica;

import java.io.Serializable;
import java.util.LinkedList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author joavi
 */
@Entity
public class Carrera implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;
    private String nombre;

    @OneToMany (mappedBy="carre")
    private LinkedList<Materia> listaMaterias;
    
    public Carrera() {
    }

    public Carrera(int id, String nombre, LinkedList<Materia> listaMaterias) {
        this.id = id;
        this.nombre = nombre;
        this.listaMaterias = listaMaterias;
    }

    public LinkedList<Materia> getListaMaterias() {
        return listaMaterias;
    }

    public void setListaMaterias(LinkedList<Materia> listaMaterias) {
        this.listaMaterias = listaMaterias;
    }

    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
