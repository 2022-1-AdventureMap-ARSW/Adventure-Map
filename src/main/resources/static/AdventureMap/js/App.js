    //Variables generales
    var name;
    var monstruo1;
    var jugador1;
    var stompClient;
    var direction;
    var contrincante = {}
    var subscribePelea;
    var count = 1;
    var h1;
    var h2;
    var rol;
    let intervaloAtaqueMonstruo;
    const boton = document.querySelector("#botonAtaque");
    const url5 = 'https://adventuremap.herokuapp.com/';
    var contadorAtaque = 0;
    var local = {};
    var enemigo = {};

    /**
     * Funcion generada para redireccionar desde la página inicial
     * a la página donde se encuentra el mapa. Se recibe el nombre
     * del jugador 
     * @param {String} data 
     */
    function redirect(data){
        name = data;
        window.location = "/AdventureMap/Mapa.html"
    }

    /**
     * Funcion generada para guardar la dirección en la que va el 
     * jugador. Esto en vista que se maneja por botones su movimiento
     * @param {String} direccion 
     */
    function newDirection(direccion){
        direction = direccion
    }

    /**
     * Funcion generada para generar movimiento en el jugador de acuerdo
     * a la direccion que nos de el listener de botones.
     */
    function move(direction){
        switch (direction){
            case "ABA": movimiento.arriba();
            break;

            case "ARR": movimiento.abajo();
            break;

            case "IZQ": movimiento.izquierda();
            break;

            case "DER": movimiento.derecha();
            break;
        }
        var h = "(" + getJugadorVie().x + ","+  getJugadorVie().y + ")";
        // main();
        // mainM();
        // maint();
        stompClient.send("/App/map/mover/"+name,{},JSON.stringify(getJugador()));
        count +=1;

    }
    
    
    /**
     * Funcion generada para crear el tablero de juego, sus monstruos,
     * jugadores entre otros elementos.
     */
    function getElementsTablero(){
        clear_board()
        maint();
        getMonstruos();
        getPlayerInCanva();
    }

    /**
     * Funcion generada para iniciar el mapa. Esta función es la primera
     * en ejecutar apenas se entra en Mapa.html. Se hace las conexiones con 
     * los topicos y se pone en escucha cuando se oprima un boton
     */
    function init(){
        //eventButtonListener();
        connectAndSuscribe();
    }

    /**
     * Funcion generada para insertar el jugador en el mapa de acuerdo
     * con una peticion generada en STOMP donde se manda la ubicación
     * aleatoria
     */
    function getPlayerInCanva(){
        var player = {"nombre":this.name, "posicion":getJugador()};
        $.ajax({
            url: url4+"AdventureMap/jugadores/",
            type: "POST",
            data: JSON.stringify(player),	
            contentType: "application/json"
        }).then(
            function(){
                // drawjugadoresPart(player.posicion);
                stompClient.send('/App/jugadores/map',{},JSON.stringify(player));
            },
            function(err){
                alert("No se pudo ingresar el jugador");
            }
        )
    }

    function cancelarIntervalo(){
        if(intervaloAtaqueMonstruo!= null){
            clearInterval(intervaloAtaqueMonstruo);
            clearInterval(intervaloAtaqueMonstruo);
        }
        // window.location = "/AdventureMap/Index.html";
    }
    /**
     * Funcion generada para conectarse a STOMP, así como 
     * poder suscribirse a los topicos que se crearon
     * como canales de escucha a eventos o peticiones
     */
    function connectAndSuscribe(){
        //SE EMPIEZA CREANDO EL STOMPCLIENT PARA MANDAR PETICIONES
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        //SUSCRIBIRSE AL CANAL DE JUEGO
        stompClient.connect({},function(frame){
            stompClient.subscribe('/App/jugadores/map', function(eventbody){
                console.log("EVENTBODY DE /JUGADOR/MAP 1");
                console.log("Se supone que imprimer los elementos del mapa");
                drawPlayer();
            });

            //SUSCRIPCION AL CANAL DE PELEA
            // ESTE CANAL ACTUALIZA LAS ESTADISTICAS DE LOS JUGADORES
             subscribePelea = stompClient.subscribe("/App/pelea/", function(eventbody){
                var personaje = JSON.parse(eventbody.body);
                local = personaje[0];
                enemigo = personaje[1];
                if(enemigo.ataca == true && local.ataca == false){
                    if(local.nombre == name){
                        alert("El destino esta en una pelea");
                    }
                }else{
                    actualizarEstadisticasJugadorJugador(local,enemigo,informarPerdida);
                    if(enemigo.Tipo == "Monstruo" && enemigo.ataca == false){
                        monstruo1 = enemigo;
                        jugador1 = local;
                        ataqueMonstruo();
                    }
                }
             });
            getElementsTablero();
      });      
    };
    
    function actualizarEstadisticasJugadorJugador(local, enemigo,callback){
        //Si ataco
        if(name == local.nombre){
            document.getElementById("imagenJugador").src ="img/ATACANDO.jpg";
            $("#vidaP").text("vidaP: "+local.vida);
            $("#ataqueP").text("ataqueP: "+" "+local.dano);
            $("#vidaE").text("vidaE: "+" "+enemigo.vida);
            $("#ataqueE").text("ataqueE: "+" "+enemigo.dano);
            $(".movement").prop('disabled', true);
            contrincante = enemigo;
            jugador1 = local;
            if(local.vida == 0){
                alert("Ha perdido");
                cancelarIntervalo();
                callback();
            }else if(enemigo.vida == 0){
                alert("Ha ganado");
                cancelarIntervalo();
                huirJugador();
                
            }
        }//SI soy atacado
        else if(name == enemigo.nombre){
            document.getElementById("imagenJugador").src ="img/ATACANDO.jpg";
            $("#vidaE").text("vidaE: "+" "+local.vida);
            $("#ataqueE").text("ataqueE: "+" "+local.dano);
            $("#vidaP").text("vidaP: "+enemigo.vida);
            $("#ataqueP").text("ataqueP: "+" "+enemigo.dano);
            $(".movement").prop('disabled', true);
            contrincante = local;
            jugador1 = enemigo;
            if(local.vida == 0){
                alert("Ha Ganado");
                cancelarIntervalo();
                huirJugador();
            }else if(enemigo.vida == 0){
                alert("Ha perdido");
                cancelarIntervalo();
                callback();
            }
        }
    }
    
    function informarPerdida(){
        window.location = "/AdventureMap/Index.html";
    }

    /**
     * Funcion generada para que se envie mensaje donde el jugador recibe el ataque
     */
    function ataqueMonstruo(){
        stompClient.send("/App/map/pelea."+monstruo1.nombre,{},jugador1.nombre);
    }

    /**
     * Funcion generada para atacar al jugador y que se envie la solicitud de ataque al backend JAVA para que
     * los jugadores guardados peleeen y al que hayan atacado, reciba daño en sus estadisticas
     */
    function atacarJugador(){
        if(contrincante.Tipo == "Monstruo"){
            contadorAtaque +=1;        
            if(contadorAtaque %2 == 0){
                ataqueMonstruo();
            }
        }
        stompClient.send("/App/map/pelea."+name,{},contrincante.nombre);
    }

    function huirMonstruo(){
        $.ajax({
            url: url4+"AdventureMap/monstruos/"+monstruo1.nombre,
            type: "PUT",
            data: JSON.stringify(monstruo1),	
            contentType: "application/json"
        }).then(
            function(){
                console.log("Estado de monstruo cambiado");
            },
            function(err){
                alert("No se pudo cambiar el estado del monstruo");
            }
        )
    }

    /**
     * Funcion generada para que el jugador huya de la pelea que tiene con otro jugador o monstruo
     * Lo primero que se hace es desuscribirse del topico de pelea que se genero al entrar en combate
     */
    function huirJugador(){
        cancelarIntervalo();
        drawPlayer();
        if (monstruo1 != null){
            if(monstruo1.nombre == local.nombre){
                if(local.vida > 0){
                    huirMonstruo();
                }
            else if(monstruo1.nombre == enemigo.nombre){
                if(enemigo.vida > 0){
                    huirMonstruo();
                }
            }
            }
        }
        $.ajax({
            url: url4+"AdventureMap/jugadores/"+jugador1.nombre,
            type: "PUT",
            data: JSON.stringify(jugador1),	
            contentType: "application/json"
        }).then(
            function(){
                console.log("Estado de jugador cambiado");
            },
            function(err){
                alert("No se pudo cambiar el estado del jugador");
            }
        );
        document.getElementById("imagenJugador").src ="img/CAMINANDO.jpg";
        $(".movement").prop('disabled', false);
        $("#vidaP").text("vidaP: ");
        $("#ataqueP").text("ataqueP: ");
        $("#vidaE").text("vidaE: ");
        $("#ataqueE").text("ataqueE: ");
    } 

    $(document).ready(
        function(){
            $(".movement").prop('disabled', false);
        }
    );
