package edu.escuelaing.arsw.Controllers;

import edu.escuelaing.arsw.model.Jugador;
import edu.escuelaing.arsw.model.Monstruo;
import edu.escuelaing.arsw.model.Personaje;
import edu.escuelaing.arsw.model.Tuple;
import edu.escuelaing.arsw.persistence.AdventureMapPersistenceException;
import edu.escuelaing.arsw.services.AdventureMapServices;
import edu.escuelaing.arsw.services.persistence.AdventureMapServicesPersistenceException;

import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;


@Service
@RestController
public class appAPIController {

    @Autowired
    AdventureMapServices services;

    private SimpMessagingTemplate smt;
    @Autowired
        public appAPIController(SimpMessagingTemplate smt){
            this.smt = smt;
        }

    /**
     * Funcion generada para retornar los monstruos que se han creado o que se tienen
     * en el backend a partir de una lista. Esto sirve para volverlos a pintar en caso de 
     * haberlos cambiado de posicion o de pintarlos por primera vez
     * @return List[Monstruo]
     */
    @RequestMapping(value = "/AdventureMap/monstruos", method = RequestMethod.GET)
    public ResponseEntity<?> manejadorgetMonstruos() {
        try{
            ArrayList<Map<String,Object>> monstruos = null;
            ResponseEntity<?> mensaje = null;
            monstruos = services.getPersonajesJson("Monstruo");
            mensaje = new ResponseEntity<>(monstruos, HttpStatus.ACCEPTED);
            return mensaje;
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Funcion para modificar el jugador indicado
     * @param jugador Nombre del jugador a modificar
     * @param rp Informacion a cambiar en el jugador
     * @return
     * @throws AdventureMapPersistenceException
     */
    @RequestMapping(path = "/AdventureMap/jugadores/{jugador}",method = RequestMethod.PUT)
    public ResponseEntity<?> putJugador(@PathVariable(name = "jugador") String jugador,@RequestBody Map<String,Object> rp) throws AdventureMapPersistenceException {
        try {
            Jugador j = (Jugador)services.getPersonaje(jugador);
            Map<String,Object> c = (Map<String,Object>)rp.get("posicion");
            Tuple coordenadas = new Tuple((int)c.get("x"),(int)c.get("y"));
            j.mover(coordenadas);
            return new ResponseEntity<>(j.getJSON(),HttpStatus.ACCEPTED);
        }catch(AdventureMapServicesPersistenceException ae){
            if(ae.getMessage().equals(AdventureMapPersistenceException.ATACAR_EXCEPTION)){
                return new ResponseEntity<>(false, HttpStatus.CONTINUE);
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }
    /**
     * Funcion para modificar el monstruo indicado
     * @param monstruo Nombre del monstruo a modificar
     * @param rp Informacion a cambiar en el monstruo
     * @return
     */
    @RequestMapping(path = "/AdventureMap/monstruos/{monstruo}",method = RequestMethod.PUT)
    public ResponseEntity<?> putMonstruo(@PathVariable(name = "monstruo") String monstruo,@RequestBody Map<String,Object> rp) {
        try {
            Monstruo j = (Monstruo)services.getPersonaje(monstruo);
            Map<String,Object> c = (Map<String,Object>)rp.get("posicion");
            Tuple coordenadas = new Tuple((int)c.get("x"),(int)c.get("y"));
            if(coordenadas.equals(j.getCoordenadas())){
                j.mover(coordenadas);
            }else if((boolean)rp.get("ataca") != j.getAtaca()){
                j.setAtaca(false);
            }
            return new ResponseEntity<>(j.getJSON(),HttpStatus.ACCEPTED);
        }catch(AdventureMapServicesPersistenceException ae){
            if(ae.getMessage().equals(AdventureMapPersistenceException.ATACAR_EXCEPTION)){
                return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
            }
            else{
                return new ResponseEntity<>(true, HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/AdventureMap/jugadores",method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> postJugador(@RequestBody Map<String,Object> rp) {
        try {
            Map<String,Object> c = (Map<String,Object>)rp.get("posicion");
            Tuple coordenadas = new Tuple((int)c.get("x"),(int)c.get("y"));
            String nombre = (String) rp.get("nombre");
            Jugador j = new Jugador(coordenadas, nombre, services.getTablero());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Funcion generada para retornar la lista de jugadores que se han inscrito al juego.
     * Esta función sirve para saber sus posiciones y posteriormente dibujarlos.
     * @return -> List[Jugador]
     */
        @RequestMapping(value = "/AdventureMap/jugadores", method = RequestMethod.GET)
        public ResponseEntity<?> manejadorgetJugadores() {
            try{
                ArrayList jugadores = services.getPersonajesJson("Jugador");
                return new ResponseEntity<>(jugadores, HttpStatus.ACCEPTED);
            }catch(Exception e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
        }
    
    /**
     * Funcion generada para retornar el jugador indicado
     * @param jugador Nombre del jugador a retornar
     * @return
     */
    @RequestMapping(value = "/AdventureMap/jugadores/{nombre}", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String,Object>> manejadorgetJugador(@PathVariable String nombre){
        ResponseEntity<?> mensaje = null;
        try{
            Personaje player = services.getJugador(nombre);
            Jugador jugador = (Jugador)player;
            return ResponseEntity.ok(jugador.getJSON());
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
