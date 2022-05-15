// Select DOM elements to work with
const Playerid = document.getElementById("playerName");


function showWelcomeMessage(username) {
// Select DOM elements to work with
    const profileButton = document.getElementById("entergame");
    const signInButton = document.getElementById("SignIn");
    // Reconfiguring DOM elements
    signInButton.setAttribute("onclick", "signOut();");
    signInButton.setAttribute('class', "btn btn-success")
    signInButton.innerHTML = "Sign Out";
    profileButton.removeAttribute("hidden")

}

function updateUI() {
    console.log('Game responded at: ' + new Date().toString());
}

