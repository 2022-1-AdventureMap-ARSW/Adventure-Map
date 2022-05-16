var player_col = 'blue';
const player_border = 'black';
const board_borderr = 'black';
const board_backgroundr = "white";
const GameCanvasp = document.getElementById("gameCanvas");
const GameCanvasp_ctx = GameCanvasp.getContext("2d");

let url3 = "https://adventuremap-app.azurewebsites.net/";
// let url4 = "http://localhost:8080/";
// let url4 = 'https://adventuremap.herokuapp.com/';
let url4 = 'http://projectarsw.australiaeast.cloudapp.azure.com:8080/';


let jugador = {"x": random_player(0, GameCanvasp.width - 10), "y": random_player(0, GameCanvasp.height - 10)}
let jugadores_ = [];
let jugadoresViejos = jugador;


function getJugador(){
    return jugador;
}

function getJugadorVie(){
    return jugadoresViejos;
}

function getJugadores(){
    $.get(url4+"AdventureMap/jugadores",function(data){
        console.log("Jugadores obtenidos");
        console.log(data);
        jugadores_ = data;
        console.log(jugadores_);
    }).then(function(){
        drawjugadoresPart(jugadores_);
    },function(err){
        console.log("Jugadores no encontrados ");
    })
}

function main() {
    drawPlayer();
}

function drawPlayer() {
    getJugadores();
}

function random_player(min, max)
{
   return Math.round((Math.random() * (max-min) + min) / 10) * 10;
}

function drawjugadoresPart(MonsterPart) {
    clear_board();
    maint();
    mainM();
    GameCanvasp_ctx.fillStyle = player_col;
    GameCanvasp_ctx.strokestyle = player_border;
    console.log(MonsterPart);
    var players = MonsterPart.map(function(element){
        console.log(element);
        return element.posicion;
    })
    console.log("Posiciones Jugador");
    console.log(players);
    players.forEach(element => {
        GameCanvasp_ctx.fillRect(element.x, element.y, 10, 10);
        GameCanvasp_ctx.strokeRect(element.x, element.y, 10, 10);
    });
}


function clear_board(){
      GameCanvasp_ctx.fillStyle = board_backgroundr;
      GameCanvasp_ctx.strokestyle = board_borderr;
      GameCanvasp_ctx.fillRect(0, 0, GameCanvasp.width, GameCanvasp.height);
      GameCanvasp_ctx.strokeRect(0, 0, GameCanvasp.width, GameCanvasp.height);
}


var movimiento = (function(){

    function comprobar_bordesx(dx) {
        const hitLeftWallp = dx < 0;
        const hitRightWallp = dx > GameCanvasp.width - 10;

        return hitLeftWallp ||  hitRightWallp;
    }

    function comprobar_bordesy(dy){
        const hitToptWallp = dy < 0;
        const hitBottomWallp = dy > GameCanvasp.height -10;
        return hitToptWallp || hitBottomWallp;
    }

    function move_monsterderecha() {
          let dx = 10;
          let dy = 0;
          jugadoresViejos = {x:jugador.x,y:jugador.y};
          if (!(comprobar_bordesx(jugador.x + dx)  )){
               jugador = {x: jugador.x + dx, y: jugador.y + dy}
          }
    }

    function move_monsterizquierda() {
          let dx = -10;
          let dy = 0;
          jugadoresViejos = {x:jugador.x,y:jugador.y};
          if (!(comprobar_bordesx(jugador.x + dx)  )){
              jugador = {x: jugador.x + dx, y: jugador.y + dy}
          }

    }

    function move_monsterarriba() {
          let dx = 0;
          let dy = 10;
          jugadoresViejos = {x:jugador.x,y:jugador.y};
          if(!(comprobar_bordesy(jugador.y + dy))){
               jugador = {x: jugador.x + dx, y: jugador.y + dy}
          }
    }

    function move_monsterabajo() {
          let dx = 0;
          let dy = -10;
          jugadoresViejos = {x:jugador.x,y:jugador.y};
          if(!(comprobar_bordesy(jugador.y + dy))){
                  jugador = {x: jugador.x + dx, y: jugador.y + dy}
          }
    }

    return{
        derecha:move_monsterderecha,
        izquierda:move_monsterizquierda,
        arriba:move_monsterarriba,
        abajo:move_monsterabajo
        };

})();

