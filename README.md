[![CircleCI](https://circleci.com/gh/2022-1-AdventureMap-ARSW/Adventure-Map/tree/main.svg?style=svg)](https://circleci.com/gh/2022-1-AdventureMap-ARSW/Adventure-Map/tree/main)
# Adventure-Map
Proyecto final ARSW 2022-1 Adventure Map

# Adventure-Map
Proyecto final ARSW 2022-1 Adventure Map

# Integrantes
Diego Alejandro González
Eduardo Ospina Mejia
Cristian Andrés Castellanos

[Despliegue](https://adventuremap.herokuapp.com/AdventureMap/Index.html)

### Diagrama de actividades  
![DiagramaActividades](https://github.com/2022-1-AdventureMap-ARSW/Adventure-Map/blob/main/Img/Diagrama%20de%20actividades.png)  

Se penso hacer el manejo de actividades a traves de distintos topicos en donde se tiene uno general para el pintado del mapa, y uno especifico para cada pelea que se esta dando. el manejo del mapa se realiza desde el backend, como un concurrent hashmap, que permite un manejo de hilos de manera segura, y a partir de este manejamos el desarrollo del front-end.

 ## Ingreso Jugador  
 Para el ingreso del jugador se penso en mandar un mensaje al servidor en donde este ingresara en el back al usuario y se generara un log en donde se informa del ingreso del mismo.  

 ## Movimiento personaje  

 Enviamos una señal al servidor para que por dentro haga el movimiento del personaje y valide en caso de entrar en conflicto con quien se entra en conflicto, y asi de esta manera enviar al topico de pelea la informacion de los dos para actualizar la interfaz de estadisticas de los jugadores. Para el movimiento del personaje se penso en hacer uso de un topico general en donde se avisa a todos los subscritos que actualicen el tablero, para ver los movimientos en tiempo real, cuando se dan 3 movimientos en el tablero culesquiera que sea, los monstruos se mueven.

 ## Pelear jugadores  

 Enviamos una señal al servidor en donde se indica quienes son los jugadores que estan peleando, se genera la accion de atacar y se envia una señal al topico en donde se busca actualizar las estadisticas.



### Diagrama de Clases
![DiagramaActividades](https://github.com/2022-1-AdventureMap-ARSW/Adventure-Map/blob/main/Img/Clases.png)  
Para estos casos se generan 5 entidades que ayudarán a definir el funcionamiento en cuestión de backend del tablero y aquellos que estén dentro de el.
 - Tablero 
 - Monstruo
 - Personaje
 - Tuple
 - Jugador

Vamos a definir un servicio general llamado "AdventureMapServices". Este se encarga de conectar todas las funciones definidas en las entidades tales como "MoverJugador" o "GetPersonaje". Esto con el fin de conectar este con los otros servicios que se usan para la conexión hacia el frontend tales como STOMP y REST.

Se definen dos clases que son las que hacen la conexión directa de peticiones del frontend hacia el backend.
 - StompMessageHandler -> Esta función se genera de acuerdo con las funcionalidades de STOMP. Esto debido a que en el frontend se están usando tópicos para funcionalidades tales como "Mover el jugador", "Estadisticas de los personajes en cuestión de daño y vida", entre otras funcionalidades que se pueden hacer. Lo que hace esta función es que cada que se le envíe al tópico específico un SEND, este proceda a hacer las instrucciones que se le dicen en la función del backend para ese tópico específico.

 - appAPIController -> Esta función se genera de acuerdo con las funcionalidades de REST. Esta función ayuda a meter los jugadores dentro de backend así como los monstruos. Esto nos sirve para tener persistencia en los datos que se encuentran en el frontend con los datos del backend. Funciones de ejemplo tales como las de "Traer Personajes" y "Traer Monstruos"

Se definen tres clases de excepción para el proyecto:
 - AdventureMapPersistenceException
 - AdventureMapNotFoundException
 - AdventureMapServicesPersistenceException.

UN ejemplo de la importancia de las excepciones es que la excepción "ATACAR_EXCCEPTION" sirve para ver si un jugador tiene que entrar a combate de acuerdo a su posición, ya sea contra un monstruo o contra un jugador



### Diagrama de Casos de Uso
![DiagramaActividades](https://github.com/2022-1-AdventureMap-ARSW/Adventure-Map/blob/main/Img/casosUso.png)

1. Como usuario
Quiero decidir el punto de acción
Para poder revisar si se pelea o se huye de algún monstruo o usuario

2. Como usuario
Quiero crear personaje
Para poder entrar a la interfaz de juego

3. Como usuario
Quiero acceder a la interfaz de juego
Para poder interactuar con los demás usuarios y monstruos

4. Como usuario
Quiero moverme en el mapa
Para poder graficar en tiempo real mi nueva posición


### Diagrama de Despliegue
![DiagramaActividades](https://github.com/2022-1-AdventureMap-ARSW/Adventure-Map/blob/main/Img/Deployment.png)
El diagrama de despliegue tiene un nodo llamado "User's Computer", este se encarga de la conexión por parte del usuario hacia el ambiente de despliegue (en este caso Heroku), a través de peticiones HTTP, donde se le pide elementos tales como "index.html" para el ingreso del jugador y "Mapa.html", la cual es la interfaz del juego.

Revisando el nodo Heroku Deployment, este contiene los siguientes componentes:
 - App: Web Application -> Este componente es la conexión del frontend (Lenguaje JavaScript) hacia el usuario. Esto con el fin de conectar el ambiente de etiquetas de HTML con el ambiente de desarrollo de JavaScript. 
 
 - StompMessageHandler -> Este es un componente que se rige a partir de STOMP. Este se crea debido a que funciones tales como "Mover Personaje", "Pelear", entre otros, se manejan a través de suscripciones y envío de mensajes. Este componente se encarga de recibir los mensajes que se le envíen desde el frontend, y, a través de la etiqueta @MessageMapping, revisa a cuál tópico fue enviado el mensaje para empezar a correr las instrucciones de acuerdo a lo definido en el backend.
 
 - appAPIController -> Este es un componente que se rige a partir de REST. Esto significa que a este componente le van a llegar solicitudes HTTP como "GET" y "PUT".


### Diagrama de Componentes
![DiagramaActividades](https://github.com/2022-1-AdventureMap-ARSW/Adventure-Map/blob/main/Img/Components.png)
Para el nodo de Web Application, este requiere los servicios de WebSockets y de REST para tener todas las funcionalidades y elementos que requiere la interfaz de mapa.
El servicio de webSockets le ofrece al nodo Web las funcionalidades necesarias para el envío, suscripción y demás funcionalidades que nos puede ofrecer STOMP. Sin embargo, el componente de WebSockets requiere de funcionalidades en el backend para poder hacer las funcionalidades que le piden actualice en el frontend. Para eso le ofrecemos los servicios de AdventureMapServices, el cuál lo conecta con todas las funciones que tienen definidas las entidades, tales como "MoverJugador" en la entidad de tablero.
El otro componente que nos ofrece los servicios es SimpMessageTemplate, el cuál nos ayuda a enviar desde el backend un mensaje a un tópico definido.


### Diagramas de Secuencia 
![DiagramaActividades](https://github.com/2022-1-AdventureMap-ARSW/Adventure-Map/blob/main/Img/DieagramaSecuenciaCrear.png)
![DiagramaActividades](https://github.com/2022-1-AdventureMap-ARSW/Adventure-Map/blob/main/Img/DiagramaSecuenciaMover.png)


### Explicación Frontend
Para el frontend llegamos a usar una combinación de HTML y JavaScript para manejar las peticiones hacia el backend y poder tener todas las interfaces correctamente creadas y con sus utilidades necesarias para el juego. Con este modelo buscamos facilitar la conexión al backend, para el manejo de datos, manejo de cambios y el control de peleas. Lo separamos en diferentes clases con sus propios propositos. Separadas en diferentes funcionalidades, como el dibujo de personajes y monstruos por separado, la creacion de la conexión que los diferentes tópicos y el envío de mensajes a estos mismos. Gracias a esta conexión al backend, es que logramos manejar el cambio de posiciones y de daño a los diferentes jugadores que entren al juego.  

## Atributos de calidad  
### Seguridad  
Cuando el usuario acceda al juego bajo condiciones normales de funcionamiento no debe serle posible ingresar sin antes iniciar sesion, para asi evitar accesos no autorizado a la aplicacion.  
 - Origen: Usuario.  
 - Estimulo: Ingresar a la aplicacion.  
 - Entorno: En condiciones normales.
 - Artefacto: Pantalla de inicio del juego
 - Respuesta: Se debe notificar al usuario que no se puede ingresar sin antes haberse registrado.
 - Medida de respuesta: ...
## Disponibilidad 
Cuando por problemas en el sistema ocasionen fallos al hacer uso de la aplicacion por caida de un servidor, el sistema debe restablecer su operacion con normalidad y recuperar los datos afectados en un tiempo no superior a 10 seg.
- Origen: Problemas en el sistema.
- Estimulo: Hacer uso de la aplicacion.
- Entorno: Caido un servidor.  
- Artefacto: El sistema.  
- Respuesta: Restablecer su operacion con normalidad y recuperar los datos afectados.  
- Medida de respuesta: En un tiempo no superior a 10 seg.  
## Mantenibilidad
Cuando un desarrollador genere cambios en el codigo, bajo todos los casos, cuando el programa SonarQube analice el codigo, este debe tener una valoracion mínimo de B.
- Origen: Desarrollador.
- Estimulo: Cambios en el codigo.
- Entorno: En todos los casos.
- Artefacto: SonarQube.
- Respuesta: Debe retornar el analisis de codigo pertinente.
- Medidad de respuesta: Valoracion mínimo de B.





[Despliegue](https://adventuremap.herokuapp.com/AdventureMap/Index.html)
