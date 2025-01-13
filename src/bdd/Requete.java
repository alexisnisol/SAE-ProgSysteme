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
}