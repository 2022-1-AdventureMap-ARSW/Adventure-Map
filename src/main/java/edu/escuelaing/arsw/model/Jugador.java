package edu.escuelaing.arsw.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import edu.escuelaing.arsw.persistence.AdventureMapPersistenceException;

public class Jugador extends Personaje{

    private int bajaJugadores = 0;
    private int bajaMonstruos = 0;


    public Jugador(){
        super();
    }
    public Jugador(Tuple coordenada, String nombre, Tablero tablero) throws AdventureMapPersistenceException{
        super(coordenada, tablero);
        this.nombre = nombre;
    }

    @Override
    public String toString(){
        return "Jugador{"+super.toString()+"}";
    }


    public Map<String,Object> getJSON() {
        Map<String,Object> jugador = new HashMap<String,Object>();
        //Introduciendo atributos
        jugador.put("nombre", this.nombre);
        jugador.put("posicion", this.coordenadas);
        jugador.put("vida",this.vida);
        jugador.put("dano",this.dano);
        jugador.put("bajaJugadores",this.bajaJugadores);
        jugador.put("bajaMonstruos",this.bajaMonstruos);
        jugador.put("ataca",this.ataca);
        jugador.put("Tipo","Jugador");
        return jugador;

    }


    /**
     * Simula la accion segun la decision del personaje
     * Si en la accion se decide pelear, se procede a entrar a la pelea
     * Si en la accion se decide huir, se procede a moverse a una casilla vacia al rededor
     * @param accion
     */ 
    public void decidir(String accion) throws AdventureMapPersistenceException{
        switch (accion){
            case "Pelear":
                //Pelear
            case "Huir":
                //Huir
        }
    }

    public int getBajaMonstruos() {
        return this.bajaMonstruos;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setBajaJugadores(int bajaJugadores) {
        this.bajaJugadores = bajaJugadores;
    }

    public void setBajaMonstruos(int bajaMonstruos) {
        this.bajaMonstruos = bajaMonstruos;
    }
    

}
