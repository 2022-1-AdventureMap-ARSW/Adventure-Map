// Select DOM elements to work with
const signInButton = document.getElementById("SignIn");
const Playerid = document.getElementById("playerName");
const profileButton = document.getElementById("entergame");
const cardDiv = document.getElementById("card-div");

function showWelcomeMessage(username) {
    // Reconfiguring DOM elements
    signInButton.setAttribute("onclick", "signOut();");
    signInButton.setAttribute('class', "btn btn-success")
    signInButton.innerHTML = "Sign Out";
}

function updateUI() {
    console.log('Game responded at: ' + new Date().toString());


}