package edu.escuelaing.arsw.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.escuelaing.arsw.model.Jugador;
import edu.escuelaing.arsw.model.Personaje;
import edu.escuelaing.arsw.model.Tuple;
import edu.escuelaing.arsw.persistence.AdventureMapNotFoundException;
import edu.escuelaing.arsw.persistence.AdventureMapPersistenceException;
import edu.escuelaing.arsw.services.persistence.AdventureMapServicesPersistenceException;

public class AdventureMapMain {
    
    public static Personaje j1,j2,j3;

    public static void main(String[] args) throws JsonProcessingException {
        AdventureMapServices ams = new AdventureMapServices();
        try {
            j1 = new Jugador(new Tuple(12,78), "Jugador1", ams.getTablero());
            j2 = new Jugador(new Tuple(13,78),"Jugador2",ams.getTablero());
            j3 = new Jugador(new Tuple(14,78),"Jugador3",ams.getTablero());
            // System.out.println(ams.getPersonaje("Jugador1"));
            
            // j1.start();
            // j2.start();
            // j3.start();
            // ams.atacar(j1, j2.getCoordenadas());
            // j1.atacar(j2.getCoordenadas());
            // j3.atacar(j2.getCoordenadas());
            //ams.moverPersonaje(j1, j2.getCoordenadas());
            // ams.moverPersonaje(j3, j2.getCoordenadas());
            // ams.moverPersonaje(j2, j3.getCoordenadas());
            // ams.moverPersonaje(j3, j1.getCoordenadas());
            // ams.atacar(j2, j3.getCoordenadas());
            // ams.atacar(j1, j3.getCoordenadas());
            // ams.atacar(j1,j2.getCoordenadas());
            // ams.atacar(j2,j1.getCoordenadas());
            // ams.atacar(j3,j1.getCoordenadas());
            ObjectMapper mapper = new ObjectMapper();
            String jugador = mapper.writeValueAsString(j2);
            System.out.println(jugador);

        } catch (AdventureMapPersistenceException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(ams.getMonstruos());

    }
}
