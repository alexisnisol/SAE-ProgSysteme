CREATE OR REPLACE TABLE JOUEURS (
    nom VARCHAR(50) PRIMARY KEY,
    nbParties INT NOT NULL DEFAULT 0,
    nbVictoires INT NOT NULL DEFAULT 0,
    nbDefaites INT NOT NULL DEFAULT 0,
    nbNuls INT NOT NULL DEFAULT 0
);

CREATE OR REPLACE TABLE PARTIES (
    idPartie INT PRIMARY KEY AUTO_INCREMENT,
    datePartie DATE DEFAULT CURRENT_DATE,
    nomJoueur1 VARCHAR(50) NOT NULL,
    nomJoueur2 VARCHAR(50) NOT NULL,
    nomGagnant VARCHAR(50),
    FOREIGN KEY (nomJoueur1) REFERENCES JOUEURS(nom),
    FOREIGN KEY (nomJoueur2) REFERENCES JOUEURS(nom),
    FOREIGN KEY (nomGagnant) REFERENCES JOUEURS(nom)
);

