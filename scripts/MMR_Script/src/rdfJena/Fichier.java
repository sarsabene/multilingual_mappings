package rdfJena;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Fichier {
	
	 String retourAlaLigne="\r\n";

//****************************************************************************	
	public static void ecrire (String chemin, String text){
	try
			{
		        PrintWriter ecrir ;
				ecrir = new PrintWriter(new FileWriter(chemin));
				ecrir.print(text);
				ecrir.flush();
				ecrir.close();
			}//try
			catch (NullPointerException a)
			{
				System.out.println("Erreur : pointeur null");
			}
			catch (IOException a)
			{
				System.out.println("Problème d'IO");
			}
		}//ecrire
//****************************************************************************
	public void parseFile(String path) throws FileNotFoundException, IOException { 
		File csvFile = new File(path); 
		if (!csvFile.exists()) 
		throw new FileNotFoundException("Le fichier n'existe pas"); 
		if (csvFile.isDirectory()) 
		throw new FileNotFoundException("Le chemin désigne un répertoire et non un fichier"); 
		if (!csvFile.getAbsolutePath().endsWith(".csv")) 
		throw new FileNotFoundException("Le fichier n'est pas du type CSV (Comma Separated Value)"); 
		StringTokenizer lineParser; 
		BufferedReader reader = new BufferedReader(new FileReader(csvFile)); 
		ArrayList dataTable = new ArrayList<ArrayList>(); 
		ArrayList<String> dataRow; 
		String line = null; 
		String value = null; 
		while ((line = reader.readLine()) != null) { 
		dataRow = new ArrayList<String>(); 
		lineParser = new StringTokenizer(line, ";"); 
		while (lineParser.hasMoreElements()) { 
		value = (String) lineParser.nextElement(); 
		dataRow.add(value); 
		System.out.println(value);
		} 
		dataTable.add(dataRow); 
		} 
		} 
		public ArrayList<ArrayList> getDataList(ArrayList dataTable) { 
		return dataTable; 
		} 
	
	public static void main (String[] args) {
		
		ecrire("F:/AminaJava.txt","aminaAmine");
	}
	}


