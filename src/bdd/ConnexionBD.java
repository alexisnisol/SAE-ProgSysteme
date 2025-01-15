package bdd;

import java.sql.*;
import bdd.Requete;

/**
 * ConnexionBD
 */
public class ConnexionBD {

    

    private Connection mysql;
	private boolean connecte=false;



	public ConnexionBD() throws ClassNotFoundException{
		this.mysql=null;
		this.connecte=false;
		Class.forName("org.mariadb.jdbc.Driver");
	}

	public void connecter() throws SQLException {
		try{
			System.out.println("Connexion à la base de donnée");
			this.mysql=null;
			this.connecte=false;
            this.mysql = DriverManager.getConnection("jdbc:mysql://servinfo-maria:3306/DBnagarajah", "nagarajah", "nagarajah");
            this.connecte=true;
			System.out.println("Connexion reussi !");
		}
		catch(SQLException e){
			System.out.println("Erreur de connexion à la base de donnée");
			System.out.println(e);
		}
		
	}

	public void close() throws SQLException {
		this.mysql.close();
		this.connecte=false;
	}

    public boolean isConnecte() { 
        return this.connecte;
    }

    public Blob createBlob()throws SQLException{
		return this.mysql.createBlob();
	}

	public Statement createStatement() throws SQLException {
		return this.mysql.createStatement();
	}

	public PreparedStatement prepareStatement(String requete) throws SQLException{
		return this.mysql.prepareStatement(requete);
	}

    public static void main(String[] args) {
        ConnexionBD connexionBD;
        try {
            Requete requete = new Requete();
			System.out.println(requete.showTables());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}