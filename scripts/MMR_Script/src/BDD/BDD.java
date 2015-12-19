package BDD;

import gov.nih.nlm.uts.webservice.content.AtomDTO;
import gov.nih.nlm.uts.webservice.content.Psf;
import gov.nih.nlm.uts.webservice.content.UtsWsContentController;
import gov.nih.nlm.uts.webservice.content.UtsWsContentControllerImplService;
import gov.nih.nlm.uts.webservice.security.UtsFault_Exception;
import gov.nih.nlm.uts.webservice.security.UtsWsSecurityController;
import gov.nih.nlm.uts.webservice.security.UtsWsSecurityControllerImplService;

import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;



import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.sdb.layout2.ValueType;

import rdfJena.Fichier;




public class BDD {
	
	/*----------------------------------------------------------------*/
	

	// Runtime properties
	static String username = "aminaannane";
	static String passwordUmls = "P@ssw0rd";
	static String umlsRelease = "2015AA";
	static String serviceName = "http://umlsks.nlm.nih.gov";
	   private static String GlobalTicket ;
	
	/* ---------------------------------------------------------------*/
	private static String user="root";
	private static String userLirmm="select_umls";
	private static String  password="";
	private static String  passwordLirmm="thisisagoodpw";
private static String BDD="mapping";
	//private static String BDD="mapping";
	private static String BDDLirmm="umls2015aa";
	private static String host="localhost";
	private static String hostLirmm="tubo.lirmm.fr";
	private static Connection conn= null;
	private static Statement st=null;

	public static void main(String[] args) {
		
	    java.sql.Connection conn=ConnexionDB();
	    AfficherStatistiques(conn, "fr_snmifre", "en_snmi");
		// TODO Auto-generated method stub
		String regex = "C[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
		String data = "C2334345";
		//System.out.println(data.matches(regex));

	}
	

/* ######################################## Connexion à la base de données ######################################" */
	
	public static Connection ConnexionDB(){
		try{
			String chaineDeConnexion="jdbc:mysql://"+host+"/"+BDD+"?" + "user="+user+"&password="+password;
		//	System.out.println(chaineDeConnexion);
		return DriverManager.getConnection(chaineDeConnexion);
		}
		catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    return null;
		}
		

	}
/* ####################################### Créer une table pour une ontologie ####################################################" */

public static void CreateTableForOntologie(Connection connexion,String tablefR, String language){
	
	java.sql.Statement st=null;
	try{
		String queryDrop="Drop table IF EXISTS "+tablefR+";";
		
		String query="Create table IF NOT EXISTS "+tablefR+" (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, code varchar(50), uri varchar(100), cui varchar(8) );";
		conn=connexion;
		st= conn.createStatement();
		st.execute(queryDrop);
		st.execute(query);
		
	}
	catch(SQLException e){
		System.out.println(e.getMessage());
	}	
}

/* ######################################### Rajouter une colonne dans une ontologie ###########################################" */

public static void addColumn(Connection connexion,String tableFR, String Column, String type){
	java.sql.Statement st=null;
	try{
		
		String query="ALTER TABLE "+tableFR+" ADD COLUMN "+Column+" "+type+";";
	    System.out.println(query);
		conn=connexion;
		st= conn.createStatement();
		st.execute(query);
		
	}
	catch(SQLException e){
		System.out.println(e.getMessage());
	}
    finally {

        if (st != null) {
            try {
                st.close();
            } catch (SQLException sqlEx) { } // ignore

            st = null;
        }

    }
	
	
}

	
/* ########################################## Ajouter un enregistrement dans une table ########################################### */	
	public static void Ajouter( String table,String uri, String code, String cui){
		java.sql.Statement st=null;
		try{
		String query="insert into "+table+" (code, uri,cui)values ('"+code+"','"+uri+"','"+cui+"')";	
		System.out.println(query);
	    st= conn.createStatement();
		st.executeUpdate(query);
		System.out.println("L'enregistrement a été rajouté");
		}
		catch(SQLException e){
		System.out.println(e.getMessage());	
		}
	    finally {
	        // it is a good idea to release
	        // resources in a finally{} block
	        // in reverse-order of their creation
	        // if they are no-longer needed

	        if (st != null) {
	            try {
	                st.close();
	            } catch (SQLException sqlEx) { } // ignore

	            st = null;
	        }

	    }
	    
	}
	

	/* ################ Mettre à  jour un enregistrement ############################################################## */	
	public static void UpdateColumn(Connection connexion, String table,String column, String value, String id, String valueId)
	{	
		java.sql.Statement st=null;
		try{
		value=value.substring(value.indexOf("_-_")+3, value.length());
		value=value.replace("_", " ");
		value=value.replace("", " ");

		String query="update "+table+" set "+column+" = '"+value+"' where "+id+"='"+valueId+"';";	
		System.out.println(query);
		conn=connexion;
	    st= conn.createStatement();
		st.executeUpdate(query);

		}
		catch(SQLException e)
		{
		System.out.println(e.getMessage());	
		System.out.println("L'enregistrement n'a pas été mis à jour");
		}
	    
	}
	
/* ############################## Supprimer un eregistrement #################################### */
	
	public static void Supprimer(String nom){
		Connection conn= ConnexionDB();
		java.sql.Statement st=null;
		try{
			
			String query="DELETE FROM personne WHERE nom='"+nom+"';";
		    st= conn.createStatement();
			st.executeUpdate(query);
			System.out.println("L'enregistrement a été supprimé");
			
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}

	}

/* ####################################################################################### */
	public static void fichierJson(String query, String path, String ontologieEN, String ontologieFR){
		Connection conn= ConnexionDB();
		java.sql.Statement st=null;
		ResultSet rst=null;
		String Texte="{\"classes\":[";
		int cpt=0;
		try{
			
		    st= conn.createStatement();
		    rst = st.executeQuery(query);
		    String value1;
		    String value2;
            while (rst.next())
            {
            	cpt++;
            	System.out.println(rst.getString("EN.URI"));
            value1 =rst.getString("EN.URI") ;
			value2 = rst.getString("FR.URI"); 
		    String couple="{\""+value1+"\" : \""+ontologieEN+"\" , \""+value2+"\":\""+ontologieFR+"\"}, ";
			
			Texte=Texte+couple;
            }
            
			String JsonTexte=Texte.substring(0, Texte.length()-1)+"]}";
			System.out.println(JsonTexte);
			Fichier.ecrire(path,JsonTexte);
			System.out.println("nbre de concept mappés "+cpt);
			
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
	
		}

	}
	
/*############################################# UMLS to table ########################################################## */
	
	public static void umlsToTable(String table, String ontologie, String lang)
	{
	
		String NewTable=lang+"_"+ontologie;
		try {
			
			//String GlobalTicket = securityService.getProxyGrantTicket(username, passwordUmls);
			
			String query="select * from  "+table+" limit 30046 offset 36459"; 
			
			
			Connection conn= ConnexionDB();
			
			
			
			//CreateTableForOntologie(conn, ontologie, lang);
			//addColumn(conn, ontologie, lang, "cui", "varchar(8)");
			//addColumn(conn, ontologie, lang, "name", "varchar(200)");
			
			
			java.sql.Statement st=null;
			
			ResultSet rst=null;
			
			try{
				
			    st= conn.createStatement();
			    rst = st.executeQuery(query);
			    String cui;
			    
	            while (rst.next())
	            {
	          
	           
	            cui =rst.getString("cui") ;
	            
	            try {
					//String ticket = securityService.getProxyTicket(GlobalTicket, "http://umlsks.nlm.nih.gov");
			        
					List<AtomDTO> myAtoms = new ArrayList<AtomDTO>();

			        Psf  myPsf=new Psf();
			        myPsf.getIncludedSources().add("MDR");
			         
			        try {
					//	myAtoms = utsContentService.getConceptAtoms(ticket,"2015AA",cui,myPsf);
					    myPsf.getIncludedSources().add("MDR");
						
					     if (myAtoms.size()>0) {
					    	 
					      //  AtomDTO myAtomDTO = myAtoms.get(i);
					      //  String name = myAtomDTO.getTermString().getName();
							String uri="http://purl.bioontology.org/ontology/MEDDRA/"+rst.getString("code");
							
							
							String queryInsert="insert into "+NewTable+" (code, uri,cui)values ('"+rst.getString("code")+"','"+uri+"','"+cui+"')";	
							System.out.println(queryInsert);
						    st= conn.createStatement();
							st.executeUpdate(queryInsert);
							System.out.println("L'enregistrement a été rajouté");
							
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            }
				
			}
			catch(SQLException e){
				System.out.println(e.getMessage());
		
			}
			
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		


	}
/* ######################################## Statistique #########################################*/	
	public static void AfficherStatistiques(Connection conn,String tableFR, String tableEN)
	{
		int nbreConceptFr=0;
		int nbreConceptEn=0;
		/* créer the view */

		java.sql.Statement st=null;
		try{
			st= conn.createStatement();

			String requeteNbreConcept="SELECT DISTINCT uri FROM "+tableFR+"";
			ResultSet rst=st.executeQuery(requeteNbreConcept);
			//on place le curseur sur le dernier tuple 
			rst.last(); 
			//on récupère le numéro de la ligne 
			nbreConceptFr = rst.getRow(); 
			//on replace le curseur avant la première ligne 
			rst.beforeFirst(); 
			System.out.println("nbre cpts fr="+nbreConceptFr);

			
			requeteNbreConcept="SELECT DISTINCT uri FROM "+tableEN+"";
			rst=st.executeQuery(requeteNbreConcept);
			//on place le curseur sur le dernier tuple 
			rst.last(); 
			//on récupère le numéro de la ligne 
			nbreConceptEn = rst.getRow(); 
			//on replace le curseur avant la première ligne 
			rst.beforeFirst(); 
			System.out.println("nbre cpts En="+nbreConceptEn);
			
			//Suprimer la vue si elle existe
			String mapQuery="DROP VIEW IF EXISTS map_"+tableFR+"_"+tableEN+";"; 
			st.executeUpdate(mapQuery);
			//créer la vue de mapping seon le code
			String requeteCreateView="CREATE VIEW  map_"+tableFR+"_"+tableEN+" AS SELECT DISTINCT a.uri fruri, b.uri enuri FROM "+tableFR+" a,"+tableEN+" b WHERE a.code=b.code;";
			System.out.println(requeteCreateView);
		    st= conn.createStatement();
			st.executeUpdate(requeteCreateView);
			
			
			
			//le pourcentage de mapping fr
			String requeteNbreConceptMappes="SELECT DISTINCT uri FROM "+tableFR+" WHERE uri not IN (SELECT fruri FROM map_"+tableFR+"_"+tableEN+")";
			System.out.println(requeteNbreConceptMappes);
			rst=st.executeQuery(requeteNbreConceptMappes);
			//on place le curseur sur le dernier tuple 
			rst.last(); 
			//on récupère le numéro de la ligne 
			int cNmapFr = rst.getRow(); 
			int nbreCmapFr=nbreConceptFr-cNmapFr;
			//on replace le curseur avant la première ligne 
			rst.beforeFirst(); 
			int pFR=100*nbreCmapFr/nbreConceptFr ;
			System.out.println("nbre cpts fr mappés="+nbreCmapFr);
			System.out.println("l'ontologie française est mappée à "+pFR+"% ");
			System.out.println("Nombre de concepts fr non mappés "+(nbreConceptFr-nbreCmapFr) );
			
			
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
	}
	

/* ####################################### Destructeur ######################################### */
    public void finalize()
    {
    	  if (st != null) {
	            try {
	                st.close();
	            } catch (SQLException sqlEx) { } // ignore

	            st = null;
	        }

	        if (conn != null) {
	            try {
	                conn.close();
	            } catch (SQLException sqlEx) { } // ignore

	            conn= null;
	        } 
    }

/* ####################################################################################### */
/*
 * 		
				while ((line = reader.readLine()) != null) { 
				dataRow = new ArrayList<String>(); 
				lineParser = new StringTokenizer(line, ";"); 
				
				while (lineParser.hasMoreElements()) {
					String couple="{\"ontology\" : \"MTHMSTFRE\",\"term\" : [ ";
							value = (String) lineParser.nextElement(); 
							value = (String) lineParser.nextElement(); 
						couple=couple+"\""+value+"\""+" ]},";
						value = (String) lineParser.nextElement(); 
						couple= couple+"\r\n{\"ontology\" : \"MSTDE\",\"term\" :     [ ";
						couple=couple+"\""+value+"\""+" ]},";
						
						Texte=Texte+"\r\n"+couple;
			
				
			
				} 
			

	      } 
			

 * 	
 */
	



}
