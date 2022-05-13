package edu.escuelaing.arsw.model;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import edu.escuelaing.arsw.persistence.AdventureMapPersistenceException;
//import javafx.scene.image.Image;

public class Monstruo extends Personaje{


    public Monstruo(Tuple coordenada, Tablero tablero, String nombre) throws AdventureMapPersistenceException{
        super(coordenada, tablero);
        this.nombre = nombre;
    }

    public Monstruo(){

    }

    public Map<String,Object> getJSON() {
        Map<String,Object> monstruo = new HashMap<String,Object>();
        //Introduciendo atributos
        monstruo.put("posicion", this.coordenadas);
        monstruo.put("vida",this.vida);
        monstruo.put("dano",this.dano);
        monstruo.put("nombre",this.nombre);
        monstruo.put("ataca",this.ataca);
        monstruo.put("Tipo","Monstruo");
        return monstruo;
    }

    @Override
    public String toString(){
        return "Monstruo{"+super.toString()+"}";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

