package edu.escuelaing.arsw.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.escuelaing.arsw.model.Jugador;
import edu.escuelaing.arsw.model.Monstruo;
import edu.escuelaing.arsw.model.Personaje;
import edu.escuelaing.arsw.model.Tablero;
import edu.escuelaing.arsw.model.Tuple;
import edu.escuelaing.arsw.persistence.AdventureMapNotFoundException;
import edu.escuelaing.arsw.persistence.AdventureMapPersistenceException;
import edu.escuelaing.arsw.services.persistence.AdventureMapServicesPersistenceException;

public class AdventureMapServicesTest {
    
    AdventureMapServices ams;
    Tablero t;
    Jugador player1,player2,player11;

    @Before
    public void before(){
        ams = new AdventureMapServices();
        t = ams.getTablero();
        try{
            player1 = new Jugador(new Tuple(12,24), "Jugador1", t);
            player11 = new Jugador(new Tuple(12,25),"Victima1",t);
            player2 = new Jugador(new Tuple(15,78),"Jugador2",t);
        }catch(AdventureMapPersistenceException e){
            e.printStackTrace();
        }
    }

    @Test
    public void deberiaCrearMapaAleatorio(){
            //ams.iniciarMapa();
            ConcurrentHashMap<String,Personaje> tablero = t.getTablero();
            assertEquals(tablero.size(), 8);//Se valida que se hayan creado 5 Monstruos
            System.out.println(tablero.toString());
            Collection<Personaje> l = tablero.values();
            for(Personaje p:l){
                if(!p.getCoordenadas().equals(new Tuple(12,24)) && !p.getCoordenadas().equals(new Tuple(15, 78)) && !p.getCoordenadas().equals(new Tuple(12,25))){
                    System.out.println("Perosonaje   " + p.toString());
                    assertEquals(p.getClass(), Monstruo.class);
                }
            }
            
        
    }

    @Test
    public void deberiaRetornarListasPersonajes(){
        ArrayList<Tuple> jugadores = ams.getJugadores();
        ArrayList<Tuple> monstruos = ams.getMonstruos();
        assertEquals(jugadores.size(), 3);
        assertEquals(monstruos.size(), 5);
    }

    @Test
    public void deberiaIngresarJugadores(){
        try{
            assertEquals(t.getPersonaje(new Tuple(12,24)).getCoordenadas().toString(),new Tuple(12,24).toString());
            assertEquals(t.getPersonaje(new Tuple(15,78)).getCoordenadas().toString(), new Tuple(15,78).toString());
            new Jugador(new Tuple(12,24),"Jugador3",t);

        }catch(AdventureMapPersistenceException e){
            try{
                assertNotEquals(((Jugador)t.getPersonaje(new Tuple(12,24))).getNombre(),"Jugador3");//Se evalua que no permita ingresar jugadores en la misma posicion    
            }catch(AdventureMapNotFoundException exc){
                fail("No deberia lanzar error");
            }catch(AdventureMapPersistenceException excp){
                fail("No deberia lanzar error");
            }
        }catch(AdventureMapNotFoundException ex){
            fail("No deberia lanzar errores");
        }
    }

    @Test
    public void deberiaMoverPersonaje(){
        try{
            ams.moverPersonaje(player1,new Tuple(13,25));
            System.out.println(player1.toString());
            assertEquals(player1.getCoordenadas(), new Tuple(13,25));//Se prueba que el personaje se haya movido a la clase dicha
            ams.moverPersonaje(player1, new Tuple(15,78));
        }catch(AdventureMapServicesPersistenceException e){
            //Se comprueba que el personaje no se pueda mover a una posicion ya ocupada
            assertEquals(e.getMessage(), "Empieza el ataque");
        }
    }

    @Test
    public void deberiaAtacarPersonaje(){
        Tuple victima = new Tuple(12,25);
        try{
            ams.atacar(player1,victima);
            assertEquals(player11.getVida(),90);//Se valida que haya perdido la vida del dano del atacante
            for(int i=0;i<9;i++){
                ams.atacar(player1, victima);
            }
        }catch(AdventureMapServicesPersistenceException e){
            if(e.getMessage() == AdventureMapPersistenceException.EXCEPCTION_MUERTEJUGADOR){
                System.out.println("Se murio el personaje");
                assertTrue(true);
            }else{
                e.printStackTrace();
                fail("No deberia lanzar error");
            }
        }
    }

    @After
    public void after(){
        player1.stop();
        player2.stop();
        player11.stop();
    }

}
