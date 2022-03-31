const Monster_col = 'red';
const monster_border = 'black';
const board_borderm = 'black';
const board_backgroundm = "white";
const GameCanva = document.getElementById("gameCanvas");
const GameCanva_ctx = GameCanvas.getContext("2d");
const movement = [-10, 10]

let url2 = "http://localhost:8080/";

var booleans = [];
var jugadores_M = [];
var list = [];


function getMonstruos(){
  $.get(url2+"AdventureMap/monstruos",function(data){
      console.log(data);
      var monstruos = data.map(function(jugador){
          return {x:jugador.x, y:jugador.y}
      });
      list = monstruos;
  });
  drawMonsterPart(list);
}

mainM();

function mainM(){
  drawMonster();
}
function prueba(){
  console.log("SI SE HACE LA CONEXION CON MONSTRUO")
}

function random_monster(min, max){
   return Math.round((Math.random() * (max-min) + min) / 10) * 10;
}

function random_movement(){
   return movement[Math.floor(Math.random() * movement.length)];
}

function drawMonster() {
  getMonstruos();
}

function clear_boardm() {
      GameCanva_ctx.fillStyle = board_backgroundm;
      GameCanva_ctx.strokestyle = board_borderm;
      GameCanva_ctx.fillRect(0, 0, GameCanva.width, GameCanva.height);
      GameCanva_ctx.strokeRect(0, 0, GameCanva.width, GameCanva.height);
}


function drawMonsterPart(MonsterPart) {
    // maint();
    // main();
    GameCanva_ctx.fillStyle = Monster_col;
    GameCanva_ctx.strokestyle = monster_border;
    MonsterPart.forEach(element => {
        console.log(element);
        GameCanva_ctx.fillRect(element.x, element.y, 10, 10);
        GameCanva_ctx.strokeRect(element.x, element.y, 10, 10);
    });
}

function comprobar_bordesx(dx, i) {
    const hitLeftWall = dx < 0;
    const hitRightWall = dx > GameCanva.width - 10;
    return hitLeftWall ||  hitRightWall;
}

function comprobar_bordesy(dy, i){
    const hitToptWall = dy < 0;
    const hitBottomWall = dy > GameCanva.height -10;

    return hitToptWall || hitBottomWall;
}

function comprobar_jugador(monstruo){
    var player0 = (jugadores.x === Monstruo[0].x && jugadores.y === Monstruo[0].y);
    return player0;
}

function comprobar_otro_monstruo(monstruo, i){

       var another_monster0 = false;
       var another_monster1 = false;
       var another_monster2 = false;
       var another_monster3 = false;
       var another_monster4 = false;

       if(i === 0){
               another_monster0 = (monstruo.x === Monstruo[0].x && monstruo.y === Monstruo[0].y);
       }
       if(i === 1){
              another_monster1 = (monstruo.x === Monstruo[1].x && monstruo.y === Monstruo[1].y);
       }
       if(i === 2){
              another_monster2 = (monstruo.x === Monstruo[2].x && monstruo.y === Monstruo[2].y);
       }
       if(i === 3){
              another_monster3 = (monstruo.x === Monstruo[3].x && monstruo.y === Monstruo[3].y);
       }
       if(i === 4){
              another_monster4 = (monstruo.x === Monstruo[4].x && monstruo.y === Monstruo[4].y);
       }


       return another_monster0 || another_monster1 || another_monster2 || another_monster3 || another_monster4;

}

function move_monster(i) {
      let dx = 0;
      let dy = 0;
      const xoy = Math.floor(Math.random() * 2)
      if (xoy == 0){
        dx = random_movement();
      }
      else{
        dy = random_movement();
      }
      if (!(comprobar_bordesx(Monstruo[i].x + dx))){
        if(!(comprobar_bordesy(Monstruo[i].y + dy))){
            if(!(comprobar_otro_monstruo({x: Monstruo[i].x + dx, y: Monstruo[i].y + dy}, i))){
                Monstruo[i] = {x: Monstruo[i].x + dx, y: Monstruo[i].y + dy};
                if(comprobar_jugador(Monstruo[i])){
                    console.info("encontro jugador el monstruo:")
                    console.info(i)
                }
            }
        }
      }

}