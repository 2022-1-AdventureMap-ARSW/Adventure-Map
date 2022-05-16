package edu.escuelaing.arsw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLongArray;

import edu.escuelaing.arsw.persistence.AdventureMapNotFoundException;
import edu.escuelaing.arsw.persistence.AdventureMapPersistenceException;


/**
 * Clase que simula el tablero de juego, en este se guardan lo personajes ya sea monstruo o jugador de la partida.
 * Se maneja el tablero por medio de un hashMap.
 * @author CristianCastellanos ,DiegoGonzalez,EduardoOspina
 */
public class Tablero {

    //Tablero del juego
    private ConcurrentHashMap<String,Personaje> tablero;

    /**
     * Retorna un nuevo tablero de juego
     * @return
     */
    public final static Tablero getTableroJuego(){
        return new Tablero();
    }

    /**
     * Constructor de tablero. Este es un ConcurrentHashMap para ofrecer
     * concurrencia en los datos, así como persistencia en estos al 
     * momento de cambiar datos.
     */
    public Tablero(){
        tablero = new ConcurrentHashMap<>();
    }

    /**
     * Simula el movimiento de un personaje en el tablero
     * @param begin Punto de partida del personaje
     * @param end Punto de llegada del personaje
     */
    public void moverPersonaje(Tuple begin, Tuple end) throws AdventureMapPersistenceException{
        try{
            Personaje personaje = tablero.remove(begin.toString());
            if(getPersonaje(end) != null){
                tablero.put(begin.toString(),personaje);
                if(getPersonaje(end).getAtaca()){
                    throw new AdventureMapPersistenceException(AdventureMapPersistenceException.MAS_DE_DOS);
                }
                else{
                    // getPersonaje(end).setAtaca(true);
                    // getPersonaje(begin).setAtaca(true);
                    throw new AdventureMapPersistenceException(AdventureMapPersistenceException.ATACAR_EXCEPTION);

                }
            }
            personaje.setCoordenadas(end);
            tablero.put(end.toString(), personaje);
        }catch(Exception e){
            //e.printStackTrace();
            throw new AdventureMapPersistenceException(e.getMessage(), e.getCause());
        }

    }

    /**
     * Ingresa al tablero de juego un personaje
     * @param pos Posicion donde sera ingresado el personaje
     * @param p Personaje a ingresar
     * @return Indica si se ingreso el personaje
     * @throws AdventureMapPersistenceException
     */
    public boolean ingresarPersonaje(Tuple pos, Personaje p) throws AdventureMapPersistenceException{
        boolean isOk = false;//Informa si se inserto el personaje
            if(tablero.get(pos.toString()) == null){
                tablero.put(pos.toString(),p);
                isOk = true;
            }else{
                throw new AdventureMapPersistenceException("No es posible ingresar al personaje en la posicicion ("+pos.getX()+","+pos.getY()+")");    
            }
        return isOk;

    }

    /**
     * Retorna el personaje en la posicion indicada
     * @param pos Posicion del personaje a retornar
     * @return
     */
    public Personaje getPersonaje(Tuple pos) throws AdventureMapNotFoundException, AdventureMapPersistenceException{
        Personaje p;
        try{
            p = tablero.get(pos.toString());
            // En caso que no se encuentre un elemento en el tablero en la posicion.
            if(p == null){
                return null;
            }
        }catch(Exception e){
            throw new AdventureMapPersistenceException(e.getMessage(), e.getCause());
        }
        return p;
    }

        /** 
     * Retorna la posicion del jugador inidicado
     * @param nombre Nombre del jugador a buscar
     * @return
     */
    public Tuple getPersonaje(String nombre){
        Tuple jugador = null;
        Collection<Personaje> jugadores = tablero.values();
        for(Personaje p:jugadores){
            if(p instanceof Jugador){
                Jugador j = (Jugador)p;
                if(j.getNombre().equals(nombre)){
                    jugador = j.getCoordenadas();
                }
            }
        }
        return jugador;
    }

    /**
     * Indica si en la posicion se encuentra un Personaje
     * @param pos
     * @return
     */
    public boolean isPlayer(Tuple pos) throws AdventureMapPersistenceException{
        boolean isPlayer = false;
        try{
            Personaje p =tablero.get(pos.toString());    
            if(p != null){
                isPlayer = true;
            }
        }catch(Exception e){
            throw new AdventureMapPersistenceException(e.getMessage(), e.getCause()); 
        }
        return isPlayer;
    }

    public ConcurrentHashMap<String,Personaje> getTablero(){
        return this.tablero;
    }

    

    

}
