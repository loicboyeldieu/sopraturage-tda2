package sopraturage.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sopraturage.models.tables.Address;
import sopraturage.models.tables.PostCode;
import sopraturage.models.tables.User;



public class DatabaseManager {

	private static final boolean LOCAL=true;

	// Pour la base de donn�e en local
	private static final String url = "jdbc:mysql://localhost:3306/sopraturage";
	private static final String utilisateur = "java";
	private static final String motDePasse = "123";

	//Pour la base de donn�e sur le serveur
	private static String urlServ;
	private static final String utilisateurServ = "adminbkQsH15";
	private static final String motDePasseServ = "lGIJM9jHiC8l";

	private Connection connexion;
	private Statement statement;

	public DatabaseManager(){
		if (!LOCAL)
		{
			String dbhost=System.getenv("OPENSHIFT_MYSQL_DB_HOST");
			String dbport=System.getenv("OPENSHIFT_MYSQL_DB_PORT");
			urlServ="jdbc:mysql://"+dbhost+":"+dbport+"/tomcatsopra";
		}
	}
	
	private void connect() throws SQLException{
		if (LOCAL){
			connectoDatabase();
		} else {
			connectoDatabaseOnline();
		}
	}

	
	// Se connecte � la base de donn�e en local
	private void connectoDatabase() throws SQLException{
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
				statement = connexion.createStatement();
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) 
			{
				e.printStackTrace();
			}
		

	}

	private void connectoDatabaseOnline() throws SQLException{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connexion = DriverManager.getConnection( urlServ, utilisateurServ, motDePasseServ );
			statement = connexion.createStatement();

		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) 
		{
			e.printStackTrace();
		}

	}
	
	public void closeConnection(){
		try {
			if (connexion !=null){
				connexion.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}


	
	// execute un requete qui attend un retour : SELECT ,....
	public ResultSet query(String request){
		ResultSet resultat;
		try{
			connect();
			resultat=statement.executeQuery(request);
			return resultat;
		} catch (Exception e){
			e.printStackTrace();
		} 
		return null;

	}
	
	

	//renvoie vrai si le couple login pwd existe dans la BDD
	public boolean isPasswordOK(String login,String pwd){
		try{
			connect();
			ResultSet resultat=statement.executeQuery("SELECT email,password "
					+ "FROM users "
					+ "WHERE email='"+login+"' "
					+ "AND password='"+pwd+"'");

			String retour=new String();
			while (resultat.next())
			{
				retour=resultat.getString("email");
				retour+=resultat.getString("password");
			}

			if (retour.equals("")){
				return false;
			} else{
				return true;
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return false;

	}

	public int insert(PostCode pc){
		int statutpostCode=-1;

		String insertionPostCode="INSERT INTO Postcodes (postcode, city) "
				+ "VALUES ('"+pc.getPostcode()+"', '"+pc.getCity()+"')";

		try{
			connect();
			statutpostCode=statement.executeUpdate(insertionPostCode);
			return statutpostCode;
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return statutpostCode;
	}
	
	

	public int insert(Address a,int idPostCode,boolean home){
		int statut=-1;
		
		try{
			String insertionAdress="INSERT INTO addresses (num, way_type, way_name, id_postcode)"
					+ "VALUES ("+a.getNum()+",'"+a.getWaytype()+"','"+a.getWayName()+"','"+idPostCode+"');";
			
			connect();
			statut=statement.executeUpdate(insertionAdress);

			if (home){
				String insertionHome="INSERT INTO homes "
						+ "VALUES ("+getId(a, idPostCode)+");";
				statut=statement.executeUpdate(insertionHome);
			}


			return statut;
		} catch (Exception e){
			e.printStackTrace();
		} 
		return statut;
	}

	public int insert(User u,int idAdress){
		int statut=-1;
		try{
			String insertUser="INSERT INTO Users (surname, name, email, password, phone_number, workplace, home)"
					+ "VALUES ('"+u.getName()+"', '"+u.getSurname()+"', '"+u.getEmail()+"', '"+u.getPassword()+"', '"+u.getPhone()+"', 2,"+idAdress+" )";

			connect();
			statut=statement.executeUpdate(insertUser);
		} catch (Exception e){
			e.printStackTrace();
		} 



		return statut;
	}


	// renvoie l'id dans la base de donn�e du code postal en question
	public int getId(PostCode pc){
		int  id=-1;

		try{
			String sql="SELECT id FROM postcodes WHERE postcode='"+pc.getPostcode()+"';";
			connect();
			ResultSet resultat=statement.executeQuery(sql);
			while(resultat.next()){
				id=resultat.getInt("id");
			}

		}catch(Exception e){
			e.printStackTrace();
		} 

		return id;
	}

	// renvoie l'id d'une adresse donn�e
	public int getId(Address adress,int idPC){
		int id=-1;

		try {
			String sql="SELECT id FROM Addresses WHERE num ="+adress.getNum()+" AND"
					+ " way_type='"+adress.getWaytype()+"' AND way_name='"+adress.getWayName()+"' AND id_postcode ="+idPC+";";
			connect();
			ResultSet resultat=statement.executeQuery(sql);
			while (resultat.next())
			{
				id=resultat.getInt("id");
				return id;
			}
		}catch(Exception e){
			e.printStackTrace();
		} 
		return -1;
	}





}
