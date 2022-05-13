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
    const url5 = 'http://adventuremap.herokuapp.com/AdventureMap';

    /**
     * Funcion generada para redireccionar desde la página inicial
     * a la página donde se encuentra el mapa. Se recibe el nombre
     * del jugador 
     * @param {String} data 
     */
    function redirect(data){
        name = data;
        console.log(name);
        console.log("SI REDEPLOY2");
        window.location = "/AdventureMap/Mapa.html"
    }

    /**
     * Funcion generada para guardar la dirección en la que va el 
     * jugador. Esto en vista que se maneja por botones su movimiento
     * @param {String} direccion 
     */
    function newDirection(direccion){
        console.log("ENTRA A NEW DIRECTION "+direccion)
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
        console.log(name)
        console.log("ESTE ES EL JUGADOR"+name);
        var player = {"nombre":this.name, "posicion":getJugador()};
        $.ajax({
            url: url5+"AdventureMap/jugadores/",
            type: "POST",
            data: JSON.stringify(player),	
            contentType: "application/json"
        }).then(
            function(){
                console.log("JugadorIngresado");
                console.log(JSON.stringify(player.posicion));
                // drawjugadoresPart(player.posicion);
                stompClient.send('/App/jugadores/map',{},JSON.stringify(player));
            },
            function(err){
                alert("No se pudo ingresar el jugador");
            }
        )
    }


    /**
     * Funcion generada para conectarse a STOMP, así como 
     * poder suscribirse a los topicos que se crearon
     * como canales de escucha a eventos o peticiones
     */
    function connectAndSuscribe(){
        console.log(name);
        //SE EMPIEZA CREANDO EL STOMPCLIENT PARA MANDAR PETICIONES
        console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        //SUSCRIBIRSE AL CANAL DE JUEGO
        stompClient.connect({},function(frame){
            console.log('Connected: ' + frame);
            stompClient.subscribe('/App/jugadores/map', function(eventbody){
                console.log("EVENTBODY DE /JUGADOR/MAP 1");
                console.log("Se supone que imprimer los elementos del mapa");
                drawPlayer();
            });

            //SUSCRIPCION AL CANAL DE PELEA
            // ESTE CANAL ACTUALIZA LAS ESTADISTICAS DE LOS JUGADORES
             subscribePelea = stompClient.subscribe("/App/pelea/", function(eventbody){
                var personaje = JSON.parse(eventbody.body);
                console.log("ESTE ES EL EVENTBODY1 " + eventbody);
                var local = personaje[0];
                var enemigo = personaje[1];
                document.getElementById("imagenJugador").src ="img/ATACANDO.jpg";
                console.log("Enemigo");
                console.log(enemigo);
                console.log("Local");
                console.log(local);
                console.log(enemigo.Tipo == "Monstruo");
                console.log(enemigo.ataca == false);
                console.log(enemigo.Tipo);
                console.log(enemigo.Tipo == "Monstruo" && enemigo.ataca == false);
                if(enemigo.Tipo == "Monstruo"){
                    console.log("Entra a pelea con monstruo");
                    monstruo1 = enemigo.nombre;
                    if(enemigo.ataca == false){
                        intervaloAtaqueMonstruo = setInterval('ataqueMonstruo()',2000);
                    }
                    actualizarEstadisticasJugadorMonstruo(local,enemigo);
                }else if(enemigo.ataca == true && local.ataca == false){
                    console.log("Pela más de dos");
                    alert("El enemigo esta en una pelea");
                    huirJugador();
                }
                else{
                    actualizarEstadisticasJugadorJugador(local, enemigo);
                }
                // SI SOY ATACANTE
             });
             
            // SUSCRIPCION PELEA DOS
            stompClient.subscribe("/App/atacando/masDos",function(eventbody){
                var nombreAtacante = eventbody.body;
                if(nombreAtacante == name){
                    setTimeout(function(){
                        alert("No se puede ingresar, porque el destino esta en combate");
                    }, 2000);
                }
            });

            //SUSCRIPCION MONSTRUO VS JUGADOR
            stompClient.subscribe("/App/pelea/jugaVSmons", function(eventbody){
                console.log("EVENTBODY PELEA ENTRE JUGADOR Y MONSTRUO "+eventbody.body);
                var contrincantes = JSON.parse(eventbody.body);
                monstruo1 = "(" + contrincantes[1].x + ","+  contrincantes[1].y + ")";
                jugador1 = "(" + contrincantes[0].x + ","+  contrincantes[0].y + ")";
                console.log(monstruo1)
                console.log(jugador1)
                setInterval('ataqueMonstruo()',2000);
            })
            getElementsTablero();
      });      
    };
    
    function actualizarEstadisticasJugadorJugador(local, enemigo){
        if(name == local.nombre){
            $("#vidaP").text("vidaP: "+local.vida);
            $("#ataqueP").text("ataqueP: "+" "+local.dano);
            $("#vidaE").text("vidaE: "+" "+enemigo.vida);
            $("#ataqueE").text("ataqueE: "+" "+enemigo.dano);
            $(".movement").prop('disabled', true);
            contrincante = enemigo;
            if(local.vida == 0){
                informarPerdida(function(){
                    window.location = "/AdventureMap/Index.html";
                });
            }else if(enemigo.vida == 0){
                alert("Ha ganado");
                huirJugador();
            }
        }//SI SOY ENEMIGO
        else if(name == enemigo.nombre){
            $("#vidaE").text("vidaE: "+" "+local.vida);
            $("#ataqueE").text("ataqueE: "+" "+local.dano);
            $("#vidaP").text("vidaP: "+enemigo.vida);
            $("#ataqueP").text("ataqueP: "+" "+enemigo.dano);
            $(".movement").prop('disabled', true);
            contrincante = local;
            if(local.vida == 0){
                alert("Ha Ganado");
                huirJugador();
            }
            }else if(enemigo.vida == 0){
                informarPerdida(function(){
                    window.location = "/AdventureMap/Index.html";
                });
            }
    }

    function actualizarEstadisticasJugadorMonstruo(local, enemigo){
        if(name == local.nombre){
            $("#vidaP").text("vidaP: "+local.vida);
            $("#ataqueP").text("ataqueP: "+" "+local.dano);
            $("#vidaE").text("vidaE: "+" "+enemigo.vida);
            $("#ataqueE").text("ataqueE: "+" "+enemigo.dano);
            $(".movement").prop('disabled', true);
            contrincante = enemigo;
            if(local.vida == 0){
                clearInterval(intervaloAtaqueMonstruo);
                informarPerdida(function(){
                    window.location = "/AdventureMap/Index.html";
                });
            }else if(enemigo.vida == 0){
                clearInterval(intervaloAtaqueMonstruo);
                alert("Ha ganado");
                huirJugador();
            }
        }
    }
    
    function informarPerdida(callback){
        alert("Ha perdido");
        setTimeout((callback, 2000));
    }

    /**
     * Funcion generada para que se envie mensaje donde el jugador recibe el ataque
     */
    function ataqueMonstruo(){
        console.log("ENTRA A ATAQUE MONSTRUO ")
        stompClient.send("/App/map/pelea."+monstruo1,{},name);
    }

    /**
     * Funcion generada para atacar al jugador y que se envie la solicitud de ataque al backend JAVA para que
     * los jugadores guardados peleeen y al que hayan atacado, reciba daño en sus estadisticas
     */
    function atacarJugador(){
        console.log("YO SOY EL "+name)
        console.log("EL ENEMIGO ES "+contrincante.nombre)
        stompClient.send("/App/map/pelea."+name,{},contrincante.nombre);
    }

    /**
     * Funcion generada para que el jugador huya de la pelea que tiene con otro jugador o monstruo
     * Lo primero que se hace es desuscribirse del topico de pelea que se genero al entrar en combate
     */
    function huirJugador(){
        drawPlayer();
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
