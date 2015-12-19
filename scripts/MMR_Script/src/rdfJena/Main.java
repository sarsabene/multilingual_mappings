package rdfJena;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.lang3.time.StopWatch;

import BDD.BDD;
import JenaClasses.OntologieJena;

import com.hp.hpl.jena.mem.ObjectIterator;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;


public class Main {
	

			static final String skos="http://www.w3.org/2004/02/skos/core#";
			static final String umls="http://bioportal.bioontology.org/ontologies/umls/"; 
			static final String icd="http://who.int/icd#";
			static final String owl="http://www.w3.org/2002/07/owl#";
			static Date actuelle = new Date();

	        /* *************************************************************************************************************** */
			
			static final String MTHMSTFRE_PATH="C:/Users/amina.annane/Desktop/Clement_mini_projet/MTHMSTFRE/";

        	/* *************************************************************************************************************** */
            static final String EN_MeSH_PATH= "C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\MeSH\\EN_MESH.ttl";
            static final String MSHFRE_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\MeSH\\";
        	/* *************************************************************************************************************** */
            static final String EN_STY_PATH= "C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\STY\\";
            static final String FR_STY_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\STY\\";
        	/* *************************************************************************************************************** */
			static final String FR_MDRFRE_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\MDRFRE\\";
			static final String EN_MEDDRA_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\MDRFRE\\MEDDRA.ttl";
			/* **************************************************************************************************************** */
			static final String EN_MEDLINEPLUS_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\MEDLINEPLUS\\MEDLINEPLUSEN.ttl";
			static final String MEDLINEPLUS_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\MEDLINEPLUS\\";
			/* ***************************************************************************************************************************** */
			static final String EN_ICF_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\CIF\\ICF.owl";
			static final String FR_CIF_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\CIF\\CIF.owl";
			/* ****************************************************************************************************************************** */
			static final String FR_who_artfre_Path="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\WHO_ART\\";
			/* ***************************************************************************************************************************** */
			static final String CISP2_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\CISP2\\";
			/* ***************************************************************************************************************************** */
			static final String CIM_10_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\CIM_10\\";
			/* ***************************************************************************************************************************** */
			static final String SNMIFRE_PATH="C:\\Users\\amina.annane\\Desktop\\Clement_mini_projet\\SNMIFRE\\";


            
	
			
			
		public static void main (String args[]) throws IOException { 
		
			
			
			
			
	  
	  java.sql.Connection conn=BDD.ConnexionDB();
	  Path input = Paths.get(CISP2_PATH, "cisp2.owl");    
	  String ontologieFR="cisp2";
	  String lang="FR";
	  String tableFR =lang+"_"+ontologieFR;
	  BDD.CreateTableForOntologie(conn,tableFR, lang);
	  OntologieJena.DataFromOntologyToTable(ontologieFR,input.toUri().toString(), skos+"altLabel",tableFR);
       

	   String ontologieEN="ICPC2P";
	   lang="EN";
	   String tableEN =lang+"_"+ontologieEN;
	   input = Paths.get(CISP2_PATH, "icpc2p.ttl");
	   BDD.CreateTableForOntologie(conn,tableEN, lang);  
           OntologieJena.DataFromOntologyToTable(ontologieEN,input.toUri().toString(),"http://purl.bioontology.org/ontology/ICPC2P/ICPCCODE",tableEN);
     
	    
	   StopWatch time = new StopWatch( );
	 //Premier traitement pris en compte
	   time.start( );
	   String query1="SELECT distinct en.uri, fr.uri FROM en_"+ontologieEN+" en, FR_"+ontologieFR+" fr WHERE en.code=fr.code";
	   String query2="SELECT en.uri, fr.uri FROM "+tableEn+" en, "+tableFR+" fr WHERE en.uri=fr.uri  ";
	   System.out.println(query1);
	   BDD.fichierJson(query1, "C:/Users/amina.annane/Desktop/Clement_mini_projet/sty/"+ontologieFR+" Mapping par code 12 08 2015.txt","ncbo:STY", "STY");
	   System.out.println("Run time : " + time.getTime()+" milisecondes");
		
		}}



