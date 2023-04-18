// # ProjetP4 - 10.04.2017
// # Damien MONTEAU

// Déclaration des variables principales du programme
var joueur1, joueur2;
var joueurCourant = null;
var game = {};
var grille = [];

// Initialisation du jeu
function initialiserJeu() {
	// Permet la creation de la grille
	grille = creerGrille();
}

// Permet de lancer le jeu
function lancerJeu(mode) {
	// On change le status du jeu à new
	game.status = "new";

	// On ajoute le mode choisi
	game.mode = mode;	

	// On récupère les noms saisies dans le formulaire
	var nomJoueur1 = document.getElementById("namePerso1").value;
	var nomJoueur2 = document.getElementById("namePerso2").value;

	// Si un nom a été saisie dans le joueur 1 on lui attribu
	// Sinon on lui ajoute un nom par defaut
	if (nomJoueur1.length > 0) {
		joueur1 = new Joueur(nomJoueur1, "red");
	} else {
		nomJoueur1 = "Joueur 1";
		joueur1 = new Joueur(nomJoueur1, "red");
	}

	// Si un nom a été saisie dans le joueur 2 et que l'on est pas en joueur contre joueur on lui attribu
	// Sinon on lui ajoute un nom par defaut
	if (nomJoueur2.length > 0) {
		joueur2 = new Joueur(nomJoueur2, "yellow");
	} else {
		nomJoueur2 = "Joueur 2";
		joueur2 = new Joueur(nomJoueur2, "yellow");
	}

	// Affichage des informations perso
	document.getElementById("infoPerso").style.display = "block";

	// Ajout des informations perso du joueur 1 sur l'HTML
	document.getElementById("infoPerso1").innerHTML = nomJoueur1;
	document.getElementById("infoPerso1").style.color = "red";

	// Ajout des informations perso du joueur 2 sur l'HTML
	document.getElementById("infoPerso2").innerHTML = nomJoueur2;
	document.getElementById("infoPerso2").style.color = "yellow";

	// On fait apparaitre la grille du jeu après le choix du mode
	var grille = document.getElementById("grille");
	grille.style.display = "block";

	// On fait disparaitre le mode après le choix
	var mode = document.getElementById("mode");
	mode.style.display = "none";

	// Le joueur qui commencera sera le joueur 1
	joueurCourant = joueur1;
}

// Creation de la grille visuelle
function creerGrille() {
	var tableau = []; // Permet l'initialisation du tableau sans definir de taille

	// On boucle sur toutes les colonnes
	for (var colonne = 0; colonne < 7; colonne++) {
		tableau[colonne] = []; // Permet l'initialisation du tableau sans definir de taille
	}

	// On boucle sur toutes les clonnes puis sur toutes les lignes
	for (var colonne = 0; colonne < 7; colonne++) {
		for (var ligne = 0; ligne < 6; ligne++) {
			var position = {};
			position.x = colonne;
			position.y = ligne;

			tableau[colonne][ligne] = new Cellule(undefined, position);
		}
	}

	return tableau;
}

// Recuperer la couleur d'une cellule
function getCellule(colonne, ligne) {
	var cellule = {};
	cellule.position = {};

	// Verifie si la cellule n'est pas hors de la grille
	if (colonne < grille.length && colonne >= 0 && ligne < grille[colonne].length && ligne >= 0) {
		cellule = grille[colonne][ligne];
		console.log("[GetCellule] La cellule en cours de verification : " + cellule.position.x + " - " + cellule.position.y);
	} else {
		console.log("[GetCellule] La cellule en cours de verification : n'existe pas ou est hors de la grille");
	}

	return cellule;
}

// Demander a un joueur la colonne a laquelle il souhaite ajouter un pion
// Le choix correspond à la colonne choisie - 1 (C'est un tableau :))
function getPlayerChoice(choix) {
	var position = {};
	position.x = 0;
	position.y = 0;

	// Si la partie a déjà été gagnée
	if (game.status === "end") {
		console.log("[GetPlayerChoice] Le jeu est déjà terminé");
		sendError("Le jeu est déjà terminé !");
		return;
	}

	// Si la partie est déjà terminée et nulle
	if (verifierPartieNulle()) {
		game.status = "null";
		console.log("[GetPlayerChoice] Le jeu est déjà terminé - Match null");
		sendError("Le jeu est déjà terminé MATCH NULL !");
		return;
	}

	// Si on est en mode joueur contre ordinateur, on empeche la possibilité de jouer au joueurCourant
	if (game.mode !== 3 && joueurCourant !== joueur1) {
		console.log("[GetPlayerChoice] Vous ne pouvez pas jouer en meme temps que l'ordinateur");
		sendError("Ce n'est pas à vous de jouer");
		return;
	}

	// Change le status du jeu
	game.status = "start";

	// Si la colonne ne sont pas de la grille
	if (getHauteurColonne(choix) < 7) {
		// Ajout du pion sur la colonne désirée et a la ligne la plus haute		
		position.x = choix;
		position.y = getHauteurColonne(choix);

		// On créé la cellule avec la bonne couleur et à la bonne position
		grille[position.x][position.y] = new Cellule(joueurCourant.couleur, position);

		// Verification de si la partie est gagnée
		if (verifierCombinaisonsGangnantes(position)) {
			// Callback, permet d'effectuer une action après un temps donné
			setTimeout(function () {
				finDuJeu();
			}, 800);
			return;
		}

		// Si nous ne sommes pas dans le mode joueur contre joueur
		if (game.mode !== 3) {
			// On fait jouer l'ordinateur
			jouerOrdinateur(position);
		} else {
			// On met à jour le joueurCourant afin de changer le joueur
			if (joueurCourant !== joueur1) {
				joueurCourant = joueur1;
			} else {
				joueurCourant = joueur2;
			}
		}
	} else {
		//alert("Veuillez choisir une autre colonne afin de jouer");
		sendError("Veuillez choisir une autre colonne afin de jouer");
		return;
	}

	hideError();
}

// Permet de récuperer la hauteur de la ligne
function getHauteurColonne(colonne) {
	var ligne = grille[colonne];

	// length : Fonction native a javascript
	// On boucle sur toutes les valeurs de la ligne
	if (ligne !== undefined && ligne !== null) {
		for (var i = 0; i < ligne.length; i++) {
			if (ligne[i].couleur == undefined) {
				return i;
			}
		}
	}	

	return null;
}

/* FONCTIONS DE VERIFICATIONS */

// Permet la verification des combinaisons gagnantes
function verifierCombinaisonsGangnantes(position) {
	var result = false;
	var celluleChoisie = getCellule(position.x, position.y);

	// Check des combinaisons

	// Verifier lignes

	// On verifie pour toutes les ositions passées en parametres de la fonction getCellule ne sont pas undefined
	// Si celui-ci l'est, cela veut dire que la cellule ciblée est hors de la grille

	if (verifierCombinaisonsGangnantesLignes(celluleChoisie).valeur) {
		result = true;
	}

	if (verifierCombinaisonsGangnantesColonnes(celluleChoisie).valeur) {
		result = true;
	}

	if (verifierCombinaisonsGangnantesDiagonales(celluleChoisie).valeur) {
		result = true;
	}

	console.log("[VerfifierCombinaisonGagnantes] Result : " + result);
	return result;

}

// Permet la verification des combinaisons gagnantes sur les lignes
function verifierCombinaisonsGangnantesLignes(celluleChoisie) {
	var result = {};

	result.cellule1 = false;
	result.cellule2 = false;
	result.cellule3 = false;
	result.valeur = false;

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x + 1, celluleChoisie.position.y);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x + 2, celluleChoisie.position.y);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x - 1, celluleChoisie.position.y);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x + 1, celluleChoisie.position.y);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x - 1, celluleChoisie.position.y);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x - 2, celluleChoisie.position.y);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x + 1, celluleChoisie.position.y);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x + 2, celluleChoisie.position.y);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x + 3, celluleChoisie.position.y);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x - 1, celluleChoisie.position.y);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x - 2, celluleChoisie.position.y);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x - 3, celluleChoisie.position.y);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}


	// Retourne resultat
	return result;
}

// Permet la verification des combinaisons gagnantes sur les colonnes
function verifierCombinaisonsGangnantesColonnes(celluleChoisie) {
	var result = {};

	result.cellule1 = false;
	result.cellule2 = false;
	result.cellule3 = false;
	result.valeur = false;

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y + 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y + 2);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y - 1);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y + 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y - 1);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y - 2);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y + 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y + 2);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y + 3);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y - 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y - 2);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x, celluleChoisie.position.y - 3);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}


	// Retourne resultat
	return result;
}

// Permet la verification des combinaisons gagnantes sur les diagonales
function verifierCombinaisonsGangnantesDiagonales(celluleChoisie) {
	var result = {};

	result.cellule1 = false;
	result.cellule2 = false;
	result.cellule3 = false;
	result.valeur = false;

	// Diagonale droite

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x + 1, celluleChoisie.position.y + 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x + 2, celluleChoisie.position.y + 2);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x - 1, celluleChoisie.position.y - 1);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x + 1, celluleChoisie.position.y + 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x - 1, celluleChoisie.position.y - 1);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x - 2, celluleChoisie.position.y - 2);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x + 1, celluleChoisie.position.y + 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x + 2, celluleChoisie.position.y + 2);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x + 3, celluleChoisie.position.y + 3);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x - 1, celluleChoisie.position.y - 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x - 2, celluleChoisie.position.y - 2);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x - 3, celluleChoisie.position.y - 3);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	// Diagonale gauche

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x + 1, celluleChoisie.position.y - 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x + 2, celluleChoisie.position.y - 2);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x - 1, celluleChoisie.position.y + 1);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x + 1, celluleChoisie.position.y - 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x - 1, celluleChoisie.position.y + 1);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x - 2, celluleChoisie.position.y + 2);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x + 1, celluleChoisie.position.y - 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x + 2, celluleChoisie.position.y - 2);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x + 3, celluleChoisie.position.y - 3);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	result.cellule1 = getCelluleResultColor(celluleChoisie.position.x - 1, celluleChoisie.position.y + 1);
	result.cellule2 = getCelluleResultColor(celluleChoisie.position.x - 2, celluleChoisie.position.y + 2);
	result.cellule3 = getCelluleResultColor(celluleChoisie.position.x - 3, celluleChoisie.position.y + 3);

	if (result.cellule1 && result.cellule2 && result.cellule3) {
		result.valeur = true;
	}

	// Retourne resultat
	return result;
}

// Fonction permetttant la verification d'une couleur à une certaine position
function getCelluleResultColor(x, y) {
	var result = false;

	// Si X et Y sont définis (non egal à undefined et non egal a null)
	if (x !== undefined && x !== null && y !== undefined && y !== null) {
		var cellule = getCellule(x, y);

		// Si la couleur de la cellule correspond à la couleur du joueurCourant	
		if (cellule.couleur === joueurCourant.couleur) {
			result = true;
		}
	}

	return result;
}

// Permet de verifier si la partie est nulle
function verifierPartieNulle() {

	// On boucle sur les colonnes et les lignes
	for (var i = 0; i < 7; i++) {
		for (var j = 0; j < 6; j++) {
			// Si tu as une case vide
			if (!grille[i][j].couleur) {
				return false;
			}
		}
	}

	return true;
}

// Permet de faire jouer l'ordinateur à la place d'un autre joueur
function jouerOrdinateur(positionJoueur1) {
	var position = {};
	position.x = 0;
	position.y = 0;
	joueurCourant = joueur2;

	// Callback permettant d'effectuer une action après 800 millesondes
	setTimeout(function () {
		// Si on est en joueur contre ordinateur niveau 1
		// Sinon si on est en joueur contre ordinateur niveau 2
		if (game.mode === 0) {
			// Ajout du pion sur la colonne désirée et a la ligne la plus haute	
			position.x = Math.floor(Math.random() * (7 - 0) + 0);

			// Si la cellule ne sort pas de la grille
			if (getHauteurColonne(position.x) !== null) {
				position.y = getHauteurColonne(position.x);
				grille[position.x][position.y] = new Cellule(joueur2.couleur, position);

				// Verification de si la partie est gagnée
				if (verifierCombinaisonsGangnantes(position)) {
					setTimeout(function () {
						finDuJeu();
					}, 800);

					return;
				}
			} else {
				// On demande à l'ordinateur de rejouer
				jouerOrdinateur(positionJoueur1);
				return;
			}
		} else if (game.mode === 1) {

			// Ajout du pion sur la colonne désirée et a la ligne la plus haute	
			if (positionJoueur1.x + 1 < grille.length) {
				position.x = positionJoueur1.x + 1;
			} else {
				position.x = positionJoueur1.x - 1;
			}

			if (getHauteurColonne(position.x) !== null) {
				position.y = getHauteurColonne(position.x);
				grille[position.x][position.y] = new Cellule(joueur2.couleur, position);

				// Verification de si la partie est gagnée
				if (verifierCombinaisonsGangnantes(position)) {
					setTimeout(function () {
						finDuJeu();
					}, 800);

					return;
				}
			} else {
				// On demande à l'ordinateur de rejouer
				jouerOrdinateur(positionJoueur1);
				return;
			}

		} else if (game.mode === 2) {

			// Permet de generer un nombre entre 1 et 10
			var random = Math.floor((Math.random() * 10) + 1);

			// On verifie si la cellule choisie ne sort pas de la grille et que la valeur de random est comprise entre 0 et 3
			// Sinon si on verifie que la valeur du random est comprise entre 4 et 7
			// Sinon c'est que c'est superieur à 7
			if (positionJoueur1.x + 1 < grille.length && random >= 0 && random < 4) {
				position.x = positionJoueur1.x + 1;
			} else if (random >= 4 && random < 8) {
				position.x = positionJoueur1.x - 1;
			} else {
				position.x = positionJoueur1.x;
			}

			// Si on ne sort pas de la grille en hauteur
			if (getHauteurColonne(position.x) !== null) {
				position.y = getHauteurColonne(position.x);
				grille[position.x][position.y] = new Cellule(joueur2.couleur, position);

				// Verification de si la partie est gagnée
				if (verifierCombinaisonsGangnantes(position)) {
					setTimeout(function () {
						finDuJeu();
					}, 800);

					return;
				}
			} else {
				// On demande à l'ordinateur de rejouer
				jouerOrdinateur(positionJoueur1);
				return;
			}
		} else {
			console.log("[JouerOrdinateur] L'ordinateur ne peut pas jouer dans ce mode");
		}

		joueurCourant = joueur1;

		return position;
	}, 800);

	return position;
}

// Fonction definissant les actions effectuées à la fin du jeu
function finDuJeu() {
	game.status = "end";
	sendMessage("BRAVO " + joueurCourant.nom + " vous avez gagné !!!");
}

function sendError(message) {
	var messageDiv = document.getElementById("message");
	messageDiv.innerHTML = message;
	messageDiv.style.opacity = 1;
	messageDiv.style.background = "red";
}

function sendMessage(message) {
	var messageDiv = document.getElementById("message");
	messageDiv.innerHTML = message;
	messageDiv.style.opacity = 1;
	messageDiv.style.background = "green";
}

function hideError() {
	var messageDiv = document.getElementById("message");
	messageDiv.innerHTML = "";
	messageDiv.style.opacity = 0;
}

/* OBJECTS */

// Objet joueur
function Joueur(nom, couleur) {
	var joueur = {};

	joueur.nom = nom;
	joueur.couleur = couleur;

	return joueur;
}

// Objet celulle
function Cellule(couleur, position) {
	var cellule = {};

	if (couleur) {
		var cellule = document.getElementById("cellule" + position.y + "-" + position.x);
		cellule.couleur = couleur;
		cellule.style.backgroundColor = couleur;
		cellule.style.boxShadow = "0px 0px 5px #000";
		cellule.style.animation = "anim 0.8s linear alternate";
	}

	if (position) {
		cellule.position = position;
	}

	return cellule;
}

initialiserJeu();