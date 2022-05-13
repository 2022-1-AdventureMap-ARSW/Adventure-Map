package edu.escuelaing.arsw.Controllers;

import edu.escuelaing.arsw.model.Jugador;
import edu.escuelaing.arsw.model.Monstruo;
import edu.escuelaing.arsw.model.Personaje;
import edu.escuelaing.arsw.model.Tuple;
import edu.escuelaing.arsw.services.AdventureMapServices;
import edu.escuelaing.arsw.services.persistence.AdventureMapServicesPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Service
@RestController
public class appAPIController {
    @Autowired
    AdventureMapServices services;

    /**
     * Funcion generada para retornar los monstruos que se han creado o que se tienen
     * en el backend a partir de una lista. Esto sirve para volverlos a pintar en caso de 
     * haberlos cambiado de posicion o de pintarlos por primera vez
     * @return List[Monstruo]
     */
    @RequestMapping(value = "/AdventureMap/monstruos", method = RequestMethod.GET)
    public ResponseEntity<?> manejadorgetMonstruos() {
        ArrayList<Tuple> monstruos = null;
        ResponseEntity<?> mensaje = null;
        monstruos = services.getMonstruos();
        mensaje = new ResponseEntity<>(monstruos, HttpStatus.ACCEPTED);
        return mensaje;
        
    }

    /**
     * Funcion generada para retornar la lista de jugadores que se han inscrito al juego.
     * Esta función sirve para saber sus posiciones y posteriormente dibujarlos.
     * @return -> List[Jugador]
     */
        @RequestMapping(value = "/AdventureMap/jugadores", method = RequestMethod.GET)
        public ResponseEntity<?> manejadorgetJugadores() {
            ArrayList<Tuple> jugadores = null;
            ResponseEntity<?> mensaje = null;
            System.out.println("Se hace la solicitud de Jugadores");
            jugadores = services.getJugadores();
            System.out.println("Lista de jugadores: "+jugadores.toString());
            mensaje = new ResponseEntity<>(jugadores, HttpStatus.ACCEPTED);
            return mensaje;
        }

        /**
         * Función generada para retornar el mapa que se hizo en el backend.
         * Esta conexión sirve para tener control sobre los movimientos de 
         * jugadores y monstruos dentro del mapa pero en backend
         * @return
         */
    @RequestMapping(value = "/AdventureMap/initMap", method = RequestMethod.GET)
    public ResponseEntity<?> manejadorgetMapa(){
        ResponseEntity<?> mensaje = null;
        try{
            services.iniciarMapa();
        } catch (AdventureMapServicesPersistenceException e) {
            e.printStackTrace();
            mensaje = new ResponseEntity<>("No se pudo iniciar el mapa con los monstruos", HttpStatus.NOT_FOUND);
        }
        return mensaje;
    }

    /**
     * Funcion generada para retornar un jugador a partir de su nombre. En este caso
     * se dara la tupla de coordenadas donde este se encuentra
     * @param personaje
     * @return
     */
    @RequestMapping(value="/AdventureMap/personajes/{personaje}", method=RequestMethod.GET)
    public ResponseEntity<?> manejadorGetJugadorTupla(@PathVariable String personaje) {
        ResponseEntity<?> mensaje = null;
            Tuple p = services.getPersonaje(new Tuple(personaje),true);
            mensaje= new ResponseEntity<>(p,HttpStatus.ACCEPTED);
        return mensaje;
    }

    /**
     * 
     * @param jugador
     * @return
     */
    @RequestMapping(value = "/AdventureMap/jugadores/{jugador}", method=RequestMethod.GET)
    public ResponseEntity<?> manejadorGetJugadorNombre(@PathVariable String jugador){
        ResponseEntity<?> mensaje = null;
        Tuple p;
        try {
            p = services.getPersonaje(jugador);
            System.out.println(services.getPersonaje(jugador));
            mensaje = new ResponseEntity<>(p,HttpStatus.ACCEPTED);
        } catch (AdventureMapServicesPersistenceException e) {
            // TODO Auto-generated catch block
            mensaje = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return mensaje;
    }

    /**
     * Metodo creado para retornar las estadisticas del personaje indicado
     * @param personaje Personaje a retornar estadisticas
     * @return Estadisticas del jugador representadas como una tupla x: vida, y:dano
     */
    @RequestMapping(value="/AdventureMap/personajes/estadisticas/{personaje}", method=RequestMethod.GET)
    public ResponseEntity<?> manejadorGetEstadisticasJugador(@PathVariable String personaje) {
        ResponseEntity<?> mensaje = null;
            Personaje p;
            try {
                p = services.getPersonaje(new Tuple(personaje));
                //Creacion de la tupla por la cual se representan las estadisticas del jugador
                System.out.println("ESTE ES EL PERSONAJE "+personaje);
                //System.out.println(p.getVida());
                System.out.println(p.getDano());
                Tuple q = new Tuple(p.getVida(),p.getDano());
                System.out.println("SOY EL JUGADOOR "+personaje+" MI VIDA ES "+p.getVida()+" Y MI DAÑO ES "+p.getDano());
                mensaje= new ResponseEntity<>(q,HttpStatus.ACCEPTED);
            } catch (AdventureMapServicesPersistenceException e) {
                // TODO Auto-generated catch block
                mensaje = new ResponseEntity<>("Personaje no encontrado", HttpStatus.NOT_FOUND);
                e.printStackTrace();
            }
        return mensaje;
    }

}
