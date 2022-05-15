package edu.escuelaing.arsw.Controllers;


import edu.escuelaing.arsw.services.AdventureMapServices;
import edu.escuelaing.arsw.services.persistence.AdventureMapServicesPersistenceException;

import org.jboss.logging.Message;
import org.springframework.beans.factory.annotation.Autowired;

import edu.escuelaing.arsw.model.Jugador;
import edu.escuelaing.arsw.model.Monstruo;
import edu.escuelaing.arsw.model.Personaje;
import edu.escuelaing.arsw.model.Tuple;
import edu.escuelaing.arsw.persistence.AdventureMapPersistenceException;
import edu.escuelaing.arsw.services.AdventureMapServices;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class StompMessageHandler {

    @Autowired
    SimpMessagingTemplate msgt;
    @Autowired
    AdventureMapServices ams;

    ArrayList<Jugador> jugadores = new ArrayList<>();
    ArrayList<Monstruo> monstruos = new ArrayList<>();  

    
    @MessageMapping("/map/{nombre}")
    public void handleIngresarJugador(@DestinationVariable String nombre, Tuple coordenada){
        System.out.println("INGRESO UN NUEVO JUGADOR  "+ nombre);
        try {
            Jugador j = new Jugador(coordenada, nombre, ams.getTablero());
            System.out.println("Jugador adicionado" + j);
        } catch (AdventureMapPersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // @MessageMapping("/map/mover/monstruos")
    // public void handleMoverMonstruo(@DestinationVariable String monstruo){

    // }

    // @MessageMapping("/jugador/atacar.{j}")
    // public void handleAtacarJugador(){
    // }

    @MessageMapping("/map/mover/monstruo/{origen}")
    public void handleMoverMonstruo(@DestinationVariable String origen, Tuple destino){
        Personaje p = null;
        try {
            p = ams.getPersonaje(new Tuple(origen));
            if(p.getAtaca()){
                System.out.println("NO SE PUEDE MOVER, ESTA EN COMBATE");
            }
            else{
                if(ams.getPersonaje(destino) != null){
                    System.out.println("YA HAY ALGUIEN AHI 1");
                }
                else{
                    ams.moverPersonaje(p, destino);
                    System.out.println("Monstruo: " + ams.getJugadores());
                    msgt.convertAndSend("/App/jugador/map",ams.getJugadores());
                    }
                }
            
            }
        catch(Exception ex){ex.printStackTrace();}

    }

    @MessageMapping("/map/mover/{origen}")
    public void handleMoverJugador(@DestinationVariable String origen, Tuple destino) throws AdventureMapServicesPersistenceException{
        Personaje p = ams.getPersonaje(origen);
        ArrayList<Map<String,Object>> participantes = new ArrayList();
        Personaje enemy = ams.getPersonaje(destino);
        try {
            ams.moverPersonaje(p, destino);
            System.out.println("Jugadores: " + ams.getJugadores());
            msgt.convertAndSend("/App/jugadores/map",true);
        } catch (AdventureMapServicesPersistenceException e) {
            if(e.getMessage().equals(AdventureMapPersistenceException.ATACAR_EXCEPTION)){
                participantes.add(p.getJSON());
                participantes.add(enemy.getJSON());
                msgt.convertAndSend("/App/pelea/",participantes);
                //Tuple con las ubicaciones del personaje a mover y el personaje a atacar
                //IDEA PARA ATAQUE DE MONSTRUOS
                // if(ams.getPersonaje(destino).getClass() == Monstruo.class){
                //     //En caso que sea un monstruo, cada 2 segundos se ataca al jugador
                //     //Se empieza manejando esto en el front, donde JS tiene funcion para
                //     //que cada cierto tiempo se ejecute la función.}
                //     System.out.println("PELEA CONTRA EL MONSTRUO");
                    
                //     msgt.convertAndSend("/App/pelea/jugaVSmons",participantes);
                // }
            }
            // else if(e.getMessage().equals(AdventureMapPersistenceException.MAS_DE_DOS)){
            //     System.out.println("YA ESTA EN PELEA EL OTRO 21");
            //     ams.moverPersonaje(p, new Tuple(origen));
            //     msgt.convertAndSend("/App/jugador/map",ams.getJugadores());
            //     msgt.convertAndSend("/App/atacando/masDos",((Jugador)ams.getPersonaje(new Tuple(origen))).getNombre());
            // }
            // else{
            //     e.printStackTrace();
            // }
            else{
                e.printStackTrace();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @MessageMapping("/map/pelea.{propio}")
    public void handlePelear(@DestinationVariable String propio, String enemigo) throws AdventureMapServicesPersistenceException{
        Personaje p = ams.getPersonaje(propio);
        if(enemigo.equals("HUIR")){
            p.setAtaca(false);
        }
        Personaje v = ams.getPersonaje(enemigo);
        ArrayList<Map<String,Object>> participantes = new ArrayList();
        try {
            System.out.println("Se entra en conflictoo");
            ams.atacar(p, v);
        } catch (AdventureMapServicesPersistenceException e) {
            System.out.println(e.getMessage());
            if(e.getMessage().equals(AdventureMapPersistenceException.EXCEPCTION_MUERTEJUGADOR)){
                System.out.println("JUGADOR HA MUERTO");
                v.setAtaca(false);
                p.setAtaca(false);
                quitarJugador();
            }
            e.printStackTrace();
        }
        finally{
            participantes.add(p.getJSON());
            participantes.add(v.getJSON());
            // msgt.convertAndSend("/App/jugador/map",ams.getJugadores());
            // msgt.convertAndSend("/App/monstruo/map",ams.getMonstruos());
            msgt.convertAndSend("/App/pelea/",participantes);//Envia el evento para actualizar las estadisticas
        }
    }

    @MessageMapping("/App/atacando.{propio}")
    public void handlePeleando(@DestinationVariable String propio, String jsonPropio){
        System.out.println("ENTRA A ATACANDO, NUEVO TOPICO ");
        msgt.convertAndSend("/App/peleando",jsonPropio);
    }

    /**
     * Funcion generada para quitar un personaje en caso que este muerto
     */
    public void quitarJugador(){
        for(Personaje p : ams.getPersonajes()){
            if(!p.getVivo()){
                System.out.println("Personaje" + p.getNombre() +"removido");
                ams.quitarPersonaje(p);
            }
        }
    }






}
