package edu.escuelaing.arsw.entities;

public class Jugador extends Personaje{

    private String nombre;
    private int bajaJugadores;
    private int bajaMonstruos;


    public Jugador(Tuple coordenada, String nombre) {
        super(coordenada);
        this.nombre = nombre;
    }

}
