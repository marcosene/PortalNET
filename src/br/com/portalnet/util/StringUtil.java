/**
 * @since 30/11/2009
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.util;


public abstract class StringUtil {
	
	/**
	 * Substitui as acentuacoes de uma String
	 * para o respectivo caracter sem a acentuacao 
	 * @param text
	 * @return
	 */
	public static String substituirAcentuacao(String text) {
		text = text.replaceAll("[áàâãä]","a");
	    text = text.replaceAll("[éèêë]","e");
	    text = text.replaceAll("[íìîï]","i");
	    text = text.replaceAll("[óòôõö]","o");
	    text = text.replaceAll("[úùûü]","u");
	    text = text.replaceAll("[ç]","c");
	    
	    text = text.replaceAll("[ÁÀÂÃÄ]","A");
	    text = text.replaceAll("[ÉÈÊË]","E");
	    text = text.replaceAll("[ÍÌÎÏ]","I");
	    text = text.replaceAll("[ÓÒÔÕÖ]","O");
	    text = text.replaceAll("[ÚÙÛÜ]","U");
	    text = text.replaceAll("[Ç]","C");
	    
	    return text;
	}
	
	/**
	 * Substitui as acentuacoes de um vetor de Strings 
	 * para o formato padrao de HTML
	 * @param text
	 * @return
	 */
	public static String[] substituirAcentuacaoHtml(String[] text) {
		
		for (int i = 0; i < text.length; i++){
			//'a' e 'A'
			text[i] = text[i].replaceAll("[á]","&aacute;");
			text[i] = text[i].replaceAll("[à]","&agrave;");
			text[i] = text[i].replaceAll("[â]","&acirc;");
			text[i] = text[i].replaceAll("[ã]","&atilde;");
			text[i] = text[i].replaceAll("[ä]","&auml;");
			text[i] = text[i].replaceAll("[Á]","&Aacute;");
			text[i] = text[i].replaceAll("[À]","&Agrave;");
			text[i] = text[i].replaceAll("[Â]","&Acirc;");
			text[i] = text[i].replaceAll("[Ã]","&Atilde;");
			text[i] = text[i].replaceAll("[Ä]","&Auml;");
			
			//'e' e 'E'
		    text[i] = text[i].replaceAll("[é]","&eacute;");
		    text[i] = text[i].replaceAll("[è]","&egrave;");
		    text[i] = text[i].replaceAll("[ê]","&ecirc;");
		    text[i] = text[i].replaceAll("[ë]","&euml;");
		    text[i] = text[i].replaceAll("[É]","&Eacute;");
		    text[i] = text[i].replaceAll("[È]","&Egrave;");
		    text[i] = text[i].replaceAll("[Ê]","&Ecirc;");
		    text[i] = text[i].replaceAll("[Ë]","&Euml;");
		    
		    //'i' e 'I'
		    text[i] = text[i].replaceAll("[í]","&iacute;");
		    text[i] = text[i].replaceAll("[ì]","&igrave;");
		    text[i] = text[i].replaceAll("[î]","&icirc;");
		    text[i] = text[i].replaceAll("[ï]","&iuml;");
		    text[i] = text[i].replaceAll("[Í]","&Iacute;");
		    text[i] = text[i].replaceAll("[Ì]","&Igrave;");
		    text[i] = text[i].replaceAll("[Î]","&Icirc;");
		    text[i] = text[i].replaceAll("[Ï]","&Iuml;");
		    
		    //'o' e 'O'
		    text[i] = text[i].replaceAll("[ó]","&oacute;");
		    text[i] = text[i].replaceAll("[ò]","&ograve;");
		    text[i] = text[i].replaceAll("[ô]","&ocirc;");
		    text[i] = text[i].replaceAll("[õ]","&otilde;");
		    text[i] = text[i].replaceAll("[ö]","&ouml;");
		    text[i] = text[i].replaceAll("[Ó]","&Oacute;");
		    text[i] = text[i].replaceAll("[Ò]","&Ograve;");
		    text[i] = text[i].replaceAll("[Ô]","&Ocirc;");
		    text[i] = text[i].replaceAll("[Õ]","&Otilde;");
		    text[i] = text[i].replaceAll("[Ö]","&Ouml;");
		    
		    //'u' e 'U'
		    text[i] = text[i].replaceAll("[ú]","&uacute;");
		    text[i] = text[i].replaceAll("[ù]","&ugrave;");
		    text[i] = text[i].replaceAll("[û]","&ucirc;");
		    text[i] = text[i].replaceAll("[ü]","&uuml;");
		    text[i] = text[i].replaceAll("[Ú]","&Uacute;");
		    text[i] = text[i].replaceAll("[Ù]","&Ugrave;");
		    text[i] = text[i].replaceAll("[Û]","&Ucirc;");
		    text[i] = text[i].replaceAll("[Ü]","&Uuml;");
		    
		    //'ç' e 'Ç'
		    text[i] = text[i].replaceAll("[ç]","&ccedil;");
		    text[i] = text[i].replaceAll("[Ç]","&Ccedil;");
		}
	    
		return text;
	}
	
	/**
	 * Funcao que trata da separacao de uma String com palavras de busca em um vetor de Strings
	 * 
	 * ex1: busca normal com palavras chaves
	 * **************************************
	 * entrada - palavra1 palavra2 palavra3
	 * 
	 * saida - [0] - palavra1
	 * 		   [1] - palavra2
	 * 		   [2] - palavra3
	 * 
	 * ex2: utilizando aspas
	 * **************************************
	 * entrada - palavra1 "palavra2 palavra3"
	 * 
	 * saida -	[0] - palavra1
	 * 			[1] - palavra2 palavra3	
	 * 
	 * @param chaves
	 * @return
	 */
	public static String[] organizarChavesPesquisa(String chaves){
		String[] chavesPesquisa = chaves.trim().split(" ");
		String[] chavesPesquisaQuebrada = new String[chavesPesquisa.length];
		int j = 0;
		
		for(int i=0; i < chavesPesquisa.length; i++ ){
			if(chavesPesquisa[i].startsWith("\"") && !chavesPesquisa[i].endsWith("\"")){
				chavesPesquisaQuebrada[j] = "";
				do{
					
					chavesPesquisaQuebrada[j] += " "+chavesPesquisa[i];
					i++;
					if(chavesPesquisa[i] != null){
						if(chavesPesquisa[i].contains("\"")){
							chavesPesquisaQuebrada[j] += " "+chavesPesquisa[i];
							j++;
						}
					}
				}while(!chavesPesquisa[i].contains("\"") && i < chavesPesquisa.length - 1);				
			}
			else{
				chavesPesquisaQuebrada[j] = chavesPesquisa[i];
				j++;
			}
		}
		
		int i;
		for(i = 0; i < chavesPesquisaQuebrada.length; i++ ){
			if(chavesPesquisaQuebrada[i] == null){
				break;
			}
			chavesPesquisaQuebrada[i] = chavesPesquisaQuebrada[i].replace('"', ' ').trim();
			
		}
		
		String[] listaChaves = new String[i];
		for(j = 0; j < i ; j++ ){
			listaChaves[j] = chavesPesquisaQuebrada[j];
		}
			
		return listaChaves;
	}
}
