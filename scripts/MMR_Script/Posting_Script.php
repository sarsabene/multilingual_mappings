<!DOCTYPE html>
<html>
<body>

<?php
/*************************************/
/* Déclaration des variables*/
/*************************************/
$db="ontologies";
$user="AAmina";
$apiKey="myApiKey";
$creator="http://data.stageportal.lirmm.fr/users/AAmina";
$source_contact_info="a_annane@esi.dz"; 
$source="REST";
$source_name="Reconciliation of multilingual mapping";
$comment="Multilingual mapping between an English concept on NCBO BioPortal and its French translation on SIFR BioPortal";
$ontoFR="CIM-10";
$ontoEN="ncbo:ICD10";
$skosRelation="http://www.w3.org/2004/02/skos/core#exactMatch";
$goldRelation="http://purl.org/linguistics/gold/freeTranslation";
$cpt=0;
$requete="SELECT DISTINCT  fr.uri fr_uri, en.uri en_uri 
FROM    fr_cim_10 fr, en_icd10 en 
WHERE  (  (fr.code=en.code)
OR      (fr.code like '%-%'
AND      en.code like '%-%'
AND     fr.code=SUBSTRING(en.code,1,7))) and fr.uri not in (select fr.uri from fr_cim_10 fr where fr.uri like '%(%)%' and length(fr.code)=3) ;";

/* *** *** *** *** *** ***  Le programme à executer *** ** *** *** *** *** *** *** */
echo '[';
$cpt=posterRequeteMapping($urlVerifPart1,$urlVerifPart2, $user,$apiKey,$ontoFR,$ontoEN,$skosRelation,$goldRelation,$urlMapping,$requete,$db,$cpt);
echo ']';
echo $cpt;
/* *** *** *** *** *** ***  Déclaration des fonctions *** ** *** *** *** *** *** *** */

/*************************************/
/* fonction qui vérifie l'existence d'une chaine dans une autre*/
/*************************************/
function existMap($resultat, $uri){
if (strpos($resultat,$uri) !== false) {
    return true;
}
else {return false;}
}
/*************************************/
/* fonction qui poste un mapping*/
/*************************************/

function posterMapping($ch,$user,$apiKey,$ontoFR,$ontoEN,$uriFR,$uriEN,$skosRelation,$goldRelation){
echo '{"creator":"http://data.stageportal.lirmm.fr/users/AAmina" ,"source_contact_info":"a_annane@esi.dz","relation" : ["'.$skosRelation.'", "'.$goldRelation.'"],"Source":"REST","source_name":"Reconciliation of multilingual mapping","comment" : "Multilingual mapping between an English concept on NCBO BioPortal and its French translation on SIFR BioPortal","classes" : { "'.$uriFR.'" : "'.$ontoFR.'","'.$uriEN.'" : "'.$ontoEN.'"}},';
		curl_setopt( $ch, CURLOPT_POSTFIELDS,'{"creator" : "http://data.stageportal.lirmm.fr/users/AAmina","relation" : ["'.$skosRelation.'", "'.$goldRelation.'"],"comment" : "Commentaire de test","classes" : { "'.$uriFR.'" : "'.$ontoFR.'","'.$uriEN.'" : "'.$ontoEN.'"}}' );
		curl_setopt( $ch, CURLOPT_HTTPHEADER, array('Content-Type:application/json','Authorization: apikey token='.$apiKey.''));	
		# Return response instead of printing.
		curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
		# Send request.
		$result = curl_exec($ch);
		# Print response.
		echo "<pre></pre>";
		if (strstr($result,'point to a valid class')) echo '{"creator" : "http://data.stageportal.lirmm.fr/users/AAmina","relation" : ["'.$skosRelation.'", "'.$goldRelation.'"],"comment" : "Commentaire de test","classes" : { "'.$uriFR.'" : "'.$ontoFR.'","'.$uriEN.'" : "'.$ontoEN.'"}}';
		
}
/*************************************/
/* fonction qui cherche une chaine de caractère sur une page web*/
/*************************************/
function searchOnTheWeb($url,$str){
$ch = curl_init();
//echo $url.' \n';
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
$resultat = curl_exec ($ch);
curl_close($ch);
$str= str_replace (  '/' ,'%2F' , $str);
$str= str_replace (  ':' ,'%3A' , $str );
return existMap($resultat,$str);

}

/************************************************************/
/* fonction qui poste le résultat d'une requête de mapping*/
/************************************************************/
function posterRequeteMapping($urlVerifPart1,$urlVerifPart2, $user,$apiKey,$ontoFR,$ontoEN,$skosRelation,$goldRelation,$urlMapping,$requete,$db,$cpt){
$ch = curl_init( $urlMapping );
try
{
	$bdd = new PDO('mysql:host=localhost;dbname='.$db.';charset=utf8', 'root', '');
	$reponse = $bdd->query($requete);
		$time = microtime(true) - $_SERVER["REQUEST_TIME_FLOAT"];
     //   echo "la requete a pris $time sec  \n";
	while ($donnees = $reponse->fetch())
	{
	$urlFrWithoutSlash=$donnees['fr_uri'];
	$urlFrWithoutSlash= str_replace (  '/' ,'%2F' , $urlFrWithoutSlash);
	$urlFrWithoutSlash= str_replace (  '#' ,'%23' , $urlFrWithoutSlash);
    $urlFrWithoutSlash= str_replace (  ':' ,'%3A' , $urlFrWithoutSlash);
//	if(searchOnTheWeb($urlVerifPart1.$urlFrWithoutSlash.$urlVerifPart2,$donnees['en_uri'])<>1)
		{
        posterMapping($ch,$user,$apiKey,$ontoFR,$ontoEN,$donnees['fr_uri'],$donnees['en_uri'],$skosRelation,$goldRelation);
		$time = microtime(true) - $_SERVER["REQUEST_TIME_FLOAT"];
      //  echo " $time sec \n";
		}
	//echo $cpt.' ';
	$cpt++;
	}
	$reponse->closeCursor(); // Termine le traitement de la requête
    curl_close($ch);
	$time = microtime(true) - $_SERVER["REQUEST_TIME_FLOAT"];
   // echo "l'execution a pris $time secondes \n";
	$time_end = microtime(true);
}
catch (Exception $e)
{
        die('Erreur : ' . $e->getMessage());
		$time = microtime(true) - $_SERVER["REQUEST_TIME_FLOAT"];
		//echo " exception l'exécution a pris $time secondes\n";
}
return $cpt;
}

?>

</body>
</html>
