package edu.escuelaing.arsw.model;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.escuelaing.arsw.persistence.AdventureMapNotFoundException;
import edu.escuelaing.arsw.persistence.AdventureMapPersistenceException;
import edu.escuelaing.arsw.services.persistence.AdventureMapServicesPersistenceException;

/**
 * Clase que simula un personaje en el juego
 */
public abstract class Personaje extends Thread{

    protected Tuple coordenadas;
    protected int vida;
    protected int dano;
    protected boolean vivo = true;
    protected Tablero tablero;
    protected String nombre;
    public boolean ataca = false;
    public static int VIDA = 100;
    public static int DANO = 10;
    private long time = System.currentTimeMillis();

    public Personaje(){
        this.dano = DANO;
        this.vida = VIDA;
    }

    abstract public Map<String,Object> getJSON();


    public Personaje(Tuple coordenada, Tablero tablero) throws AdventureMapPersistenceException{
        this.coordenadas = coordenada;
        this.tablero = tablero;
        try{
            tablero.ingresarPersonaje(coordenada,this);
        }catch(AdventureMapPersistenceException e){
            throw e;
        }
        this.dano = DANO;
        this.vida = VIDA;
    }

    @Override
    public void run(){
        try{
            while(vida >0){   
                long actual = System.currentTimeMillis();
                if(actual - time > 5000){
                        sleep(1000);
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
    

    @Override
    public String toString(){
        return "Nombre: "+this.nombre+", Posicion:"+this.coordenadas+" vida:"+this.vida+" dano: "+this.dano;
    }

    /**
    * Retorna lavida del personaje
    */
    public int consultarEstado(){
        return this.vida;
    }
  
    /**
     * Accion que simula el ataque de un personaje a otro
     * @param enemigo Posicion en el mapa del enemigo a atacar.
     */
    public void atacar(Tuple enemigo) throws AdventureMapNotFoundException,AdventureMapPersistenceException{
        Personaje p = tablero.getPersonaje(enemigo);
        try{
            setAtaca(true);
            p.sufrirAtaque(dano);
        }catch(AdventureMapPersistenceException  ea){
            if(ea.getMessage() == AdventureMapPersistenceException.EXCEPCTION_MUERTEJUGADOR){
                setAtaca(false);
            }
            throw ea;
        }
        catch(Exception e){
            throw e;
        }

    }
    //Atacar

    /**
     * Accion que simula recibir un ataque
     * @param dano Daño sufrido al personaje
     */
    public void sufrirAtaque(int dano) throws AdventureMapPersistenceException{
        this.vida -= dano;
        setAtaca(true);
        if(this.vida<=0){
            this.vivo = false;
            throw new AdventureMapPersistenceException(AdventureMapPersistenceException.EXCEPCTION_MUERTEJUGADOR);
        }
    }

    /**
     * Accion que simula el movimiento de un personaje en el tablero de juego
     */
    public void mover(Tuple destino) throws AdventureMapPersistenceException{
        tablero.moverPersonaje(coordenadas, destino);
    }
    //Moverse

    public Tuple getCoordenadas() {
        return coordenadas;
    }

    public int getVida() {
        return vida;
    }

    public int getDano() {
        return dano;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }
    
    public void setCoordenadas(Tuple coordenadas) {
        this.coordenadas = coordenadas;
    }

    public boolean getAtaca() {
        return this.ataca;
    }
    
    public void setAtaca(boolean atacaN) {
        this.ataca = atacaN;
    }

    public boolean getVivo(){
        return this.vivo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }





}
