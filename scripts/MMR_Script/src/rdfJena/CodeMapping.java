package rdfJena;

public class CodeMapping {
	
	/* extraction de tte les ressources avec preflabel*/ 
	public static String SNMIFRE(String uri){
		
		String code = null;
		
		if (uri.contains("#")){
			code =uri.substring(uri.indexOf("#")+1);
			if (code.contains("_"))
			{code=code.substring(0,code.indexOf("_"));}
		}
	
		
		return code;
	} 
	
	
/* ******************************************************************* */	
	public static String SNMI(String object){
		
		String code = null;
		
		if (object.contains("^^")){
			code =object.substring(0,object.indexOf("^^"));
			return code;
		}
		else return object;
		
		
	}
/* **************************************************************** */
	public static String getCUI(String object){
		
		String code = null;
		String regex = "C[0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
	
		
		if (object.contains("^^")){
			code =object.substring(0,object.indexOf("^^"));
		
		}
		if (object.contains("@")){
			code =object.substring(0,object.indexOf("@"));
		
		}
		if (code.matches(regex)) return code;
		else return null;
		
		
		
	}
/* **************************************************************** */
	public static String whoart(String subject){
		
		String code = null;
	
	
		
		if (subject.contains("#") && subject.contains("_") ){
			code =subject.substring(subject.indexOf("#")+1, subject.indexOf("_"));
		
		}

        return code;
		
		
		
	}
	/* **************************************************************** */
	public static String who(String object){
		
		String code = null;
		
		if (object.contains("^^")){
			code =object.substring(0,object.indexOf("^^"));
			return code;
		}
		else return object;
		
		
		
	}
/* **************************************************************** */

public static String CISP2(String subject){
		
		String code = null;
	
		
		if (subject.contains("#")){
			code =subject.substring(subject.indexOf("#")+1, subject.length());
		
		}

        return code;
		
		
		
	}
/* **************************************************************** */

public static String ICPC2P(String object){
	
	String code = null;
	
	if (object.contains("^^")){
		object =object.substring(0,object.indexOf("^^"));
		if (object.contains("-")){
			
			object=object.substring(object.indexOf("-")+1,object.length());
		
										return object;}
		return object;
	}

	else return object;
	
	
	
}
	
/* **************************************************************** */
	public static String getLabel(String label){
		
		label=label.trim();
		label=label.toLowerCase();
		if (label.contains("@")){
			label=label.substring(0,label.indexOf("@"));
		}
		if (label.contains("'")) label=label.replace("'", "¤");
		if (label.contains("é")) label=label.replace("é", "e");
		if (label.contains("è")) label=label.replace("è", "e");
		if (label.contains("ê")) label=label.replace("ê", "e");
		if (label.contains("  ")) label=label.replace("  ", " ");
		
	return label;	
	}
/* ******************************************************* */
	public static String SNMI2(String object){
		
		String code = null;
		
		if (object.contains("#")){
			code =object.substring(object.indexOf("#")+1,object.length());
			return code;
		}
		else return object;
		
		
	}
/* ********************************************************************* */
	
	
	public static String CIM10(String uri){
		
		String code = null;
		
		if (uri.contains("#")){
			code =uri.substring(uri.indexOf("#")+1);
			if (code.contains("(")){
				
				code =uri.substring(uri.indexOf("(")+1,uri.indexOf(")"));
			}
		}
	
		
		return code;
	} 
/* ********************************************************************* */
	
	public static String MeSH(String object){
		
		String code = null;
		
		if (object.contains("^^")){
			code =object.substring(0,object.indexOf("^^"));
			return code;
		}
		else return object;
		
		
		
	} 
/* ********************************************************************** */
	public static String ICD10(String object){
		
		String code = null;
		
		if (object.contains("^^")){
			code =object.substring(0,object.indexOf("^^"));
			return code;
		}
		else return object;
		
		
		
	}
/* ********************************************************************** */
	public static String MEDLINEPLUS_FR(String object){
		
		String code = null;
		if (object.length()>=8){
			object=object.substring(0,8);
			String regex = "C[0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
			if (object.matches(regex)){
				code=object;
			}
	
	}
		
		return code;
		}
	
	
}
/*
 			String regex = "C[0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
					if (code.matches(regex)){
 */
