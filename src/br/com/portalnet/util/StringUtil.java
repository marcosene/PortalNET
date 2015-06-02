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
		text = text.replaceAll("[�����]","a");
	    text = text.replaceAll("[����]","e");
	    text = text.replaceAll("[����]","i");
	    text = text.replaceAll("[�����]","o");
	    text = text.replaceAll("[����]","u");
	    text = text.replaceAll("[�]","c");
	    
	    text = text.replaceAll("[�����]","A");
	    text = text.replaceAll("[����]","E");
	    text = text.replaceAll("[����]","I");
	    text = text.replaceAll("[�����]","O");
	    text = text.replaceAll("[����]","U");
	    text = text.replaceAll("[�]","C");
	    
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
			text[i] = text[i].replaceAll("[�]","&aacute;");
			text[i] = text[i].replaceAll("[�]","&agrave;");
			text[i] = text[i].replaceAll("[�]","&acirc;");
			text[i] = text[i].replaceAll("[�]","&atilde;");
			text[i] = text[i].replaceAll("[�]","&auml;");
			text[i] = text[i].replaceAll("[�]","&Aacute;");
			text[i] = text[i].replaceAll("[�]","&Agrave;");
			text[i] = text[i].replaceAll("[�]","&Acirc;");
			text[i] = text[i].replaceAll("[�]","&Atilde;");
			text[i] = text[i].replaceAll("[�]","&Auml;");
			
			//'e' e 'E'
		    text[i] = text[i].replaceAll("[�]","&eacute;");
		    text[i] = text[i].replaceAll("[�]","&egrave;");
		    text[i] = text[i].replaceAll("[�]","&ecirc;");
		    text[i] = text[i].replaceAll("[�]","&euml;");
		    text[i] = text[i].replaceAll("[�]","&Eacute;");
		    text[i] = text[i].replaceAll("[�]","&Egrave;");
		    text[i] = text[i].replaceAll("[�]","&Ecirc;");
		    text[i] = text[i].replaceAll("[�]","&Euml;");
		    
		    //'i' e 'I'
		    text[i] = text[i].replaceAll("[�]","&iacute;");
		    text[i] = text[i].replaceAll("[�]","&igrave;");
		    text[i] = text[i].replaceAll("[�]","&icirc;");
		    text[i] = text[i].replaceAll("[�]","&iuml;");
		    text[i] = text[i].replaceAll("[�]","&Iacute;");
		    text[i] = text[i].replaceAll("[�]","&Igrave;");
		    text[i] = text[i].replaceAll("[�]","&Icirc;");
		    text[i] = text[i].replaceAll("[�]","&Iuml;");
		    
		    //'o' e 'O'
		    text[i] = text[i].replaceAll("[�]","&oacute;");
		    text[i] = text[i].replaceAll("[�]","&ograve;");
		    text[i] = text[i].replaceAll("[�]","&ocirc;");
		    text[i] = text[i].replaceAll("[�]","&otilde;");
		    text[i] = text[i].replaceAll("[�]","&ouml;");
		    text[i] = text[i].replaceAll("[�]","&Oacute;");
		    text[i] = text[i].replaceAll("[�]","&Ograve;");
		    text[i] = text[i].replaceAll("[�]","&Ocirc;");
		    text[i] = text[i].replaceAll("[�]","&Otilde;");
		    text[i] = text[i].replaceAll("[�]","&Ouml;");
		    
		    //'u' e 'U'
		    text[i] = text[i].replaceAll("[�]","&uacute;");
		    text[i] = text[i].replaceAll("[�]","&ugrave;");
		    text[i] = text[i].replaceAll("[�]","&ucirc;");
		    text[i] = text[i].replaceAll("[�]","&uuml;");
		    text[i] = text[i].replaceAll("[�]","&Uacute;");
		    text[i] = text[i].replaceAll("[�]","&Ugrave;");
		    text[i] = text[i].replaceAll("[�]","&Ucirc;");
		    text[i] = text[i].replaceAll("[�]","&Uuml;");
		    
		    //'�' e '�'
		    text[i] = text[i].replaceAll("[�]","&ccedil;");
		    text[i] = text[i].replaceAll("[�]","&Ccedil;");
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
