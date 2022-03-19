package edu.eci.arsw.model;

/**
 * Clase que simula una tupla conformado por una coordenada x e y
 */
public class Tuple {

    int x;
    int y;

    public Tuple(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return "Tuple{x: "+x+",y: "+y+"}";
    }
}
