package JenaClasses;
import java.util.List;

import org.apache.jena.web.HttpSC.Code;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import rdfJena.CodeMapping;
import BDD.BDD;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class OntologieJena {
	
/* ##################################################################################################################### */	

	public static void DataFromOntologyToTable(String ontologie,String ontologyPath, String codeProperty,String table)
	{
		
	// creer un modele vide
		Model modelOntologie = ModelFactory.createDefaultModel();
		modelOntologie.read(ontologyPath) ;
 
		//list the statements in the model
		Property propriete= modelOntologie.getProperty(codeProperty);
		Selector requete= new SimpleSelector(null,propriete, (RDFNode)null);
		StmtIterator iter=modelOntologie.listStatements(requete);
	

		while (iter.hasNext())
		{
			
				Statement stmt=iter.nextStatement();//get next statement
				Resource subject =stmt.getSubject();//uri
				RDFNode object=stmt.getObject();//le code
				if(object instanceof Resource)
				{
					
					 ExtendedIterator<Resource> g = subject.as( OntResource.class ).listRDFTypes(true);
					 requete= new SimpleSelector(null,null, (RDFNode)subject);
					StmtIterator iter2=modelOntologie.listStatements(requete);

						
							Statement stmt2=iter2.nextStatement();//get next statement
								 subject =stmt2.getSubject();//uri
								System.out.println("its correct "+subject.toString());
					
				
					BDD.Ajouter(table,subject.toString(), object.toString().substring(object.toString().indexOf("#")+1,object.toString().length()),"");
				}
				else
				{	
					//object is a literal
	
					System.out.println(object.toString());
					String code;
					
				     if(ontologie=="SNMIFRE"){
				    	 
				    	 code=CodeMapping.SNMIFRE(subject.toString());
				     }
				     else if(ontologie=="cui_snmifre"){
				    	 
				    	 code=CodeMapping.getCUI(object.toString());
				     }
				     else if(ontologie=="WHOARTFRECUI"){
				    	 
				    	 code=CodeMapping.getCUI(object.toString());
				     }
				     else if(ontologie=="whoCode"){
				    	 
				    	 code=CodeMapping.who(object.toString());
				     }
				     else if(ontologie=="who"){
				    	 
				    	 code=CodeMapping.getCUI(object.toString());
				     }
				     else if(ontologie=="ICPC2P"){
				    	 
				    	 code=CodeMapping.ICPC2P(object.toString());
				     }
				     else if(ontologie=="CISP2"){
				    	 
				    	 code=CodeMapping.CISP2(subject.toString());
				     }
				     else if(ontologie=="ICPC"){
				    	 
				    	 code=CodeMapping.ICPC2P(object.toString());
				     }
				     else if(ontologie=="CIM_10"){
				    	 code=CodeMapping.CIM10(subject.toString());
				     }
				     else if(ontologie=="ICD10"){
				    	 code=CodeMapping.ICD10(object.toString());
				     }
				     else if(ontologie=="MEDLINEPLUS"){
				    	 code=CodeMapping.MEDLINEPLUS_FR(object.toString());
				     }
				     else if(ontologie=="MESH" || ontologie=="MSHFRE" || ontologie=="MTHMSTFRE" || ontologie=="MSTDE" || ontologie=="MEDLINEPLUSEN"){
				    	 code=CodeMapping.MeSH(object.toString());
				     }
				     
				     else{
				    	 code=null;
				     }
					
			 
					
					
					if(code!=null){
									BDD.Ajouter(table, subject.toString(), code,"");
									}
				}	
		}

  }
/* #################################################################################################################### */

	public static void UpdateColumn(String ontologyPath, String codeProperty, String toGetCode, String table, String Column)
	{
	//
		java.sql.Connection conn=BDD.ConnexionDB();
	// crÃ©er un modÃ¨le vide
		Model modelOntologie = ModelFactory.createDefaultModel();
		modelOntologie.read(ontologyPath) ;
 
		//list the statements in the model
		Property propriete= modelOntologie.getProperty(codeProperty);
		Selector requete= new SimpleSelector(null,propriete, (RDFNode)null);
		StmtIterator iter=modelOntologie.listStatements(requete);

		while (iter.hasNext())
		{
				Statement stmt=iter.nextStatement();//get next statement
				Resource subject =stmt.getSubject();//uri
				RDFNode object=stmt.getObject();//le code
				if(object instanceof Resource)
				{
					BDD.UpdateColumn(conn,table, Column, object.toString(),"uri", subject.toString());
				}
				else
				{
					//object is a literal
				String codeChaine =object.toString().substring(0, object.toString().indexOf(toGetCode));

				    String   code=CodeMapping.SNMIFRE(subject.toString());
					String regex = "C[0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
					if (codeChaine.matches(regex)){
					BDD.UpdateColumn(conn,table, Column, codeChaine,"uri", subject.toString());}
					
				}	
		}

  }
	/* #################################################################################################################### */

	public static void UpdateLabelColumn(String ontologyPath, String codeProperty,String table, String Column)
	{
	//
		java.sql.Connection conn=BDD.ConnexionDB();
	// créer un modèle vide
		Model modelOntologie = ModelFactory.createDefaultModel();
		modelOntologie.read(ontologyPath) ;
 
		//list the statements in the model
		Property propriete= modelOntologie.getProperty(codeProperty);
		Selector requete= new SimpleSelector(null,propriete, (RDFNode)null);
		StmtIterator iter=modelOntologie.listStatements(requete);

		while (iter.hasNext())
		{
				Statement stmt=iter.nextStatement();//get next statement
				Resource subject =stmt.getSubject();//uri
				RDFNode object=stmt.getObject();//le code
				if(object instanceof Resource)
				{
					BDD.UpdateColumn(conn,table, Column, object.toString(),"uri", subject.toString());
				}
				else
				{
					//object is a literal
				//	String codeChaine =object.toString().substring(0, object.toString().indexOf(toGetCode));

				    String   code=CodeMapping.getLabel(object.toString());
		
					BDD.UpdateColumn(conn,table, Column, code,"uri", subject.toString());
					
				}	
		}

  }
	
/* ######################################################################################################################### */
	
	
}
