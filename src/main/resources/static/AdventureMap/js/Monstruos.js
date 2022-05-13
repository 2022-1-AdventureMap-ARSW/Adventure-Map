const Monster_col = 'red';
const monster_border = 'black';
const board_borderm = 'black';
const board_backgroundm = "white";
const GameCanva = document.getElementById("gameCanvas");
const GameCanva_ctx = GameCanvas.getContext("2d");
const movement = [-10, 10]

let url2 = "https://adventuremap-app.azurewebsites.net/";
let url1 = "http://localhost:8080/";

let monstruos = [];


function getMonstruos(){
  $.get(url5+"AdventureMap/monstruos",function(data){
    monstruos = data;
    console.log("Lista de monstruos obtenida");
    console.log(monstruos);
  }).then(function(){
    drawMonsterPart(monstruos);
  },function(err){
    console.log("Monstruos no encontrados");
  });
  if(count%4===0){
    count+=1;
    monstruos.forEach(element => {
      move_monster(element,function(monster){
        $.ajax({
          url: url5+"AdventureMap/monstruos/"+monster.nombre,
          type: "PUT",
          data: JSON.stringify(monster),	
          contentType: "application/json"
      }).then(
          function(){
              console.log("Monstruo modificado");
              console.log(monster);
              // drawjugadoresPart(player.posicion);
              stompClient.send('/App/jugadores/map',{},JSON.stringify(monster));
          },
          function(err){
              console.log("No se pudo modificar el monstruo");
              move_monster(element);
          }
      )
      });
    });
  }
  //console.log("COUNT"+count)
}


function mainM(){
  //console.log("ENTRA A MAINM");
  drawMonster();
  
}

function random_monster(min, max){
   return Math.round((Math.random() * (max-min) + min) / 10) * 10;
}

function random_movement(){
   return movement[Math.floor(Math.random() * movement.length)];
}

function drawMonster(){
 // console.log("ENTRA AL PRIMER DRAWMONSTER");
  getMonstruos();
}

function clear_boardm() {
      GameCanva_ctx.fillStyle = board_backgroundm;
      GameCanva_ctx.strokestyle = board_borderm;
      GameCanva_ctx.fillRect(0, 0, GameCanva.width, GameCanva.height);
      GameCanva_ctx.strokeRect(0, 0, GameCanva.width, GameCanva.height);
}


function drawMonsterPart(MonsterPart) {
 // console.log("ENTRA A DRAWMONSTERPART")
    // maint();
    // main();
    var monstruos_ = MonsterPart.map(function(monstruo){
      var monster =  monstruo.posicion;
      return monster;
    })
    GameCanva_ctx.fillStyle = Monster_col;
    GameCanva_ctx.strokestyle = monster_border;
    monstruos_.forEach(element => {
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

function move_monster(monster,callback) {
      console.log("MUEVE EL MONSTRUO")
      let dx = 0;
      let dy = 0;
      console.log(monster);
      mons = monster;
      const xoy = Math.floor(Math.random() * 2)
      if (xoy == 0){
        dx = random_movement();
      }
      else{
        dy = random_movement();
      }
      if(monster.ataca == false){
        if (!(comprobar_bordesx(monster.posicion.x + dx))){
          if(!(comprobar_bordesy(monster.posicion.y + dy))){
            // En caso que la posicion a la que quiere ir hay otro monstruo
            //Se queda en la misma posicion
            console.log("Se mueve");
            console.log("Posicion inicial "+ JSON.stringify(monster.posicion));
            monster.posicion.x = monster.posicion.x + dx;
            monster.posicion.y = monster.posicion.y + dy
              //new_monster = {x: monster.x + dx, y: monster.y + dy};
              console.log("Supuesta posicion "+JSON.stringify({x:monster.posicion.x + dx,y:monster.posicion.y + dy}));
              console.log("POSICION DE MONSTRUO "+JSON.stringify(monster.posicion));
              //console.log("Mons movido: x"+mons.x+ ", y: "+mons.y);
              //console.log("Monster movido: x"+monster.x+ ", y: "+monster.y);
              var h = "(" + mons.x + ","+  mons.y + ")";
              //console.log("ESTE ES MONS: "+JSON.stringify(mons))
              //console.log("ESTE ES MONSTER" + JSON.stringify(monster));
              console.log(monster);
              callback(monster);
              //stompClient.send("/App/map/mover/monstruo/"+h,{},JSON.stringify(monster));
             // console.log(monster);
          }
        }
      }
}