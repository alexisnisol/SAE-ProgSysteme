package bdd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Requete {
    
    private ConnexionBD connexionBD;

    public Requete() throws ClassNotFoundException {
        this.connexionBD = new ConnexionBD();
        try {
            this.connexionBD.connecter();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public String showTables() {
        try {
            PreparedStatement ps = this.connexionBD.prepareStatement("SHOW TABLES");
            ResultSet rs = ps.executeQuery();
            String tables = "";
            while (rs.next()) {
                tables += rs.getString(1) + "\n";
            }
            return tables;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return null;
        }
    }

    public boolean playerExists(String name) {
        try {
            PreparedStatement ps = this.connexionBD.prepareStatement("SELECT * FROM JOUEURS WHERE nom = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
    }

    public void addPlayer(String name) {
        try {
            PreparedStatement ps = this.connexionBD.prepareStatement("INSERT INTO JOUEURS (nom) VALUES (?)");
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void insertPartie(String joueur1, String joueur2, String joueurGagnant) {
        try {
            PreparedStatement ps = this.connexionBD.prepareStatement("INSERT INTO PARTIES (nomJoueur1, nomJoueur2, nomGagnant) VALUES (?, ?, ?)");
            ps.setString(1, joueur1);
            ps.setString(2, joueur2);
            ps.setString(3, joueurGagnant);
            ps.executeUpdate();
            if (joueurGagnant == null) {
                this.addNul(joueur1);
                this.addNul(joueur2);
            } else if (joueurGagnant.equals(joueur1)) {
                this.addVictoire(joueur1);
                this.addDefaite(joueur2);
            } else {
                this.addVictoire(joueur2);
                this.addDefaite(joueur1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private void addVictoire(String name) {
        try {
            PreparedStatement ps = this.connexionBD.prepareStatement("UPDATE JOUEURS SET nbVictoires = nbVictoires + 1, nbParties = nbParties + 1 WHERE nom = ?");
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private void addDefaite(String name) {
        try {
            PreparedStatement ps = this.connexionBD.prepareStatement("UPDATE JOUEURS SET nbDefaites = nbDefaites + 1, nbParties = nbParties + 1 WHERE nom = ?");
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private void addNul(String name) {
        try {
            PreparedStatement ps = this.connexionBD.prepareStatement("UPDATE JOUEURS SET nbNuls = nbNuls + 1, nbParties = nbParties + 1 WHERE nom = ?");
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public String getInfoPlayer(String name) {
        try {
            PreparedStatement ps = this.connexionBD.prepareStatement("SELECT * FROM JOUEURS WHERE nom = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return "Nom: " + rs.getString("nom") + " - " +
                        "Nombre de parties: " + rs.getInt("nbParties") + "(" +
                        rs.getInt("nbVictoires") + "V/" +
                        rs.getInt("nbDefaites") + "D/" +
                        rs.getInt("nbNuls") + "N)";
            } else {
                return "Le joueur " + name + " n'existe pas";
            }
        } catch (Exception e) {
            return "Erreur lors de la récupération des informations du joueur " + name;
        }
    }

    public String getHistoriquePlayer(String name) {
        try {
            PreparedStatement ps = this.connexionBD.prepareStatement("SELECT * FROM PARTIES WHERE nomJoueur1 = ? OR nomJoueur2 = ?");
            ps.setString(1, name);
            ps.setString(2, name);
            ResultSet rs = ps.executeQuery();
            String parties = "";
            while (rs.next()) {
                parties += rs.getString("nomJoueur1") + " VS " + rs.getString("nomJoueur2") + " - Gagnant: " + rs.getString("nomGagnant") + " Le " + rs.getString("datePartie") + " | ";
            }
            if (parties.isEmpty()) {
                return "Aucune partie pour le joueur " + name;
            }
            return parties;
        } catch (Exception e) {
            return "Erreur lors de la récupération des parties du joueur " + name;
        }
    }
}
