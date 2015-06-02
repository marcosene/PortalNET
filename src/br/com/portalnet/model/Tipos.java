/**
 * @since 08/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.model;

import java.util.LinkedHashMap;
import java.util.Map;

import br.com.portalnet.util.FacesUtils;


public abstract class Tipos {
	
	private static Map<Integer, String> tiposUsuario = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> tiposAcesso = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> tiposLinguagem = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> tiposFasesProjeto = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> tiposSeveridadeOcorrencia = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> tiposStatusOcorrencia = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> tiposObservacoes = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> tiposRepeticoes = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> tiposAssociacoes = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> tiposInventario = new LinkedHashMap<Integer, String>();
	
	public static final int TIPO_USUARIO_ADMIN = 0;
	public static final int TIPO_USUARIO_COORDENADOR = 1;
	public static final int TIPO_USUARIO_COMUM = 2;
	public static final int TIPO_USUARIO_GPJ = 3;
	public static final int TIPO_USUARIO_GPD = 4;
	public static final int TIPO_USUARIO_GF = 5;
	public static final int TIPO_USUARIO_GG = 6;
	public static final int TIPO_USUARIO_ASSISTENTE = 7;
		
	public static final int TIPO_ACESSO_INTERNO = 0;
	public static final int TIPO_ACESSO_EXTERNO = 1;
	
	public static final Integer TIPO_LINGUAGEM_C = 0;
	public static final Integer TIPO_LINGUAGEM_CPLUSPLUS = 1;
	public static final Integer TIPO_LINGUAGEM_C_CPLUSPLUS = 2;
	public static final Integer TIPO_LINGUAGEM_CSHARP = 3;
	public static final Integer TIPO_LINGUAGEM_JAVA = 4;
	public static final Integer TIPO_LINGUAGEM_VB = 5;
	public static final Integer TIPO_LINGUAGEM_VBNET = 6;
	public static final Integer TIPO_LINGUAGEM_DELPHI = 7;
	public static final Integer TIPO_LINGUAGEM_OUTRAS = 8;
	
	public static final Integer TIPO_FASE_PLANEJAMENTO = 0;
	public static final Integer TIPO_FASE_ANALISE = 1;
	public static final Integer TIPO_FASE_DESENVOLVIMENTO = 2;
	public static final Integer TIPO_FASE_TESTES = 3;
	public static final Integer TIPO_FASE_ENTREGUE = 4;
	public static final Integer TIPO_FASE_CANCELADO = 5;
	public static final Integer TIPO_FASE_HOMOLOGACAO = 6;
	public static final Integer TIPO_FASE_ESPECIFICACAO = 7;
	public static final Integer TIPO_FASE_CONGELADO = 8;
	public static final Integer TIPO_FASE_FINALIZADO = 9;
	
	public static final Integer TIPO_SEVERIDADE_OCORRENCIA_MELHORIA = 0;
	public static final Integer TIPO_SEVERIDADE_OCORRENCIA_TRIVIAL = 1;
	public static final Integer TIPO_SEVERIDADE_OCORRENCIA_MENOR = 2;
	public static final Integer TIPO_SEVERIDADE_OCORRENCIA_NORMAL = 3;
	public static final Integer TIPO_SEVERIDADE_OCORRENCIA_MAIOR = 4;
	public static final Integer TIPO_SEVERIDADE_OCORRENCIA_CRITICO = 5;
	public static final Integer TIPO_SEVERIDADE_OCORRENCIA_IMPACTANTE = 6;
	public static final Integer TIPO_SEVERIDADE_OCORRENCIA_DEFEITO = 7;
	
	public static final Integer TIPO_EQUIPAMENTO_HARDWARE = 0;
	public static final Integer TIPO_EQUIPAMENTO_SOFTWARE = 1;
	
	public static final Integer TIPO_STATUS_OCORRENCIA_ABERTA = 0;
	public static final Integer TIPO_STATUS_OCORRENCIA_CANCELADA = 1;
	public static final Integer TIPO_STATUS_OCORRENCIA_EM_ANALISE = 2;
	public static final Integer TIPO_STATUS_OCORRENCIA_ANALISADA = 3;
	public static final Integer TIPO_STATUS_OCORRENCIA_PENDENTE = 4;
	public static final Integer TIPO_STATUS_OCORRENCIA_ENCERRADA = 5;
	
	public static final Integer TIPO_EQUIPE_PERMANENTE = 0;
	public static final Integer TIPO_EQUIPE_TEMPORARIA = 1;

	public static final Integer TIPO_OBSERVACAO_NORMAL = 0;
	public static final Integer TIPO_OBSERVACAO_IMPORTANTE = 1;
	public static final Integer TIPO_OBSERVACAO_GRAVE = 2;

	public static final Integer TIPO_REPETICAO_UNICA = 0;
	public static final Integer TIPO_REPETICAO_SEMANAL = 1;
	public static final Integer TIPO_REPETICAO_MENSAL = 2;
	public static final Integer TIPO_REPETICAO_ANUAL = 3;
	
	public static final Integer TIPO_ASSOC_NAO_HA = 0;
	public static final Integer TIPO_ASSOC_OBRIGATORIA = 1;
	public static final Integer TIPO_ASSOC_FACULTATIVA = 2;

	public static final Integer TIPO_CATEG_INVENT_MONITOR = 0;
	public static final Integer TIPO_CATEG_INVENT_GABINETE = 1;
	public static final Integer TIPO_CATEG_INVENT_IMPRESSORA = 2;
	public static final Integer TIPO_CATEG_INVENT_NOTEBOOK = 3;
	public static final Integer TIPO_CATEG_INVENT_TECLADO = 4;
	public static final Integer TIPO_CATEG_INVENT_MOUSE = 5;
	public static final Integer TIPO_CATEG_INVENT_MESA = 6;
	public static final Integer TIPO_CATEG_INVENT_CADEIRA = 7;
	public static final Integer TIPO_CATEG_INVENT_TELEFONE = 8;
	public static final Integer TIPO_CATEG_INVENT_CD_DVD = 9;
	public static final Integer TIPO_CATEG_INVENT_LIVRO = 10;
	public static final Integer TIPO_CATEG_INVENT_REVISTA = 11;
	public static final Integer TIPO_CATEG_INVENT_SERVIDOR = 12;
	public static final Integer TIPO_CATEG_INVENT_ARCOND = 13;
	public static final Integer TIPO_CATEG_INVENT_COZINHA = 14;
	public static final Integer TIPO_CATEG_INVENT_OUTROS = 99;
	
	private static void init() {
		
		tiposUsuario.put(TIPO_USUARIO_ADMIN, FacesUtils.getMessage("tipoUsuario_administrador"));
		tiposUsuario.put(TIPO_USUARIO_COORDENADOR, FacesUtils.getMessage("tipoUsuario_coordenador"));	
		tiposUsuario.put(TIPO_USUARIO_COMUM, FacesUtils.getMessage("tipoUsuario_comum"));
		tiposUsuario.put(TIPO_USUARIO_GPJ, FacesUtils.getMessage("tipoUsuario_gpj"));
		tiposUsuario.put(TIPO_USUARIO_GPD, FacesUtils.getMessage("tipoUsuario_gpd"));
		tiposUsuario.put(TIPO_USUARIO_GF, FacesUtils.getMessage("tipoUsuario_gf"));
		tiposUsuario.put(TIPO_USUARIO_GG, FacesUtils.getMessage("tipoUsuario_gg"));
		tiposUsuario.put(TIPO_USUARIO_ASSISTENTE, FacesUtils.getMessage("tipoUsuario_assistente"));
		
		tiposAcesso.put(TIPO_ACESSO_INTERNO, FacesUtils.getMessage("tipoAcesso_interno"));
		tiposAcesso.put(TIPO_ACESSO_EXTERNO, FacesUtils.getMessage("tipoAcesso_externo"));
		
		tiposLinguagem.put(TIPO_LINGUAGEM_C, FacesUtils.getMessage("tipoLinguagem_C"));
		tiposLinguagem.put(TIPO_LINGUAGEM_CPLUSPLUS, FacesUtils.getMessage("tipoLinguagem_CPlusPlus"));
		tiposLinguagem.put(TIPO_LINGUAGEM_C_CPLUSPLUS, FacesUtils.getMessage("tipoLinguagem_C_CPLUSPLUS"));
		tiposLinguagem.put(TIPO_LINGUAGEM_CSHARP, FacesUtils.getMessage("tipoLinguagem_CSHARP"));
		tiposLinguagem.put(TIPO_LINGUAGEM_JAVA, FacesUtils.getMessage("tipoLinguagem_Java"));
		tiposLinguagem.put(TIPO_LINGUAGEM_VB, FacesUtils.getMessage("tipoLinguagem_VB"));
		tiposLinguagem.put(TIPO_LINGUAGEM_VBNET, FacesUtils.getMessage("tipoLinguagem_VBNET"));
		tiposLinguagem.put(TIPO_LINGUAGEM_DELPHI, FacesUtils.getMessage("tipoLinguagem_Delphi"));
		tiposLinguagem.put(TIPO_LINGUAGEM_OUTRAS, FacesUtils.getMessage("tipoLinguagem_Outras"));
		
		tiposFasesProjeto.put(TIPO_FASE_ANALISE, FacesUtils.getMessage("tipoFase_analise"));
		tiposFasesProjeto.put(TIPO_FASE_PLANEJAMENTO, FacesUtils.getMessage("tipoFase_planejamento"));
		tiposFasesProjeto.put(TIPO_FASE_ESPECIFICACAO, FacesUtils.getMessage("tipoFase_especificacao"));
		tiposFasesProjeto.put(TIPO_FASE_DESENVOLVIMENTO, FacesUtils.getMessage("tipoFase_desenvolvimento"));
		tiposFasesProjeto.put(TIPO_FASE_TESTES, FacesUtils.getMessage("tipoFase_testes"));
		tiposFasesProjeto.put(TIPO_FASE_HOMOLOGACAO, FacesUtils.getMessage("tipoFase_homologacao"));
		tiposFasesProjeto.put(TIPO_FASE_ENTREGUE, FacesUtils.getMessage("tipoFase_entregue"));
		tiposFasesProjeto.put(TIPO_FASE_CANCELADO, FacesUtils.getMessage("tipoFase_cancelado"));
		tiposFasesProjeto.put(TIPO_FASE_CONGELADO, FacesUtils.getMessage("tipoFase_congelado"));
		tiposFasesProjeto.put(TIPO_FASE_FINALIZADO, FacesUtils.getMessage("tipoFase_finalizado"));
		
		tiposSeveridadeOcorrencia.put(TIPO_SEVERIDADE_OCORRENCIA_MELHORIA, FacesUtils.getMessage("tipoSeveridade_melhoria"));
		tiposSeveridadeOcorrencia.put(TIPO_SEVERIDADE_OCORRENCIA_TRIVIAL, FacesUtils.getMessage("tipoSeveridade_trivial"));
		tiposSeveridadeOcorrencia.put(TIPO_SEVERIDADE_OCORRENCIA_MENOR, FacesUtils.getMessage("tipoSeveridade_menor"));
		tiposSeveridadeOcorrencia.put(TIPO_SEVERIDADE_OCORRENCIA_NORMAL, FacesUtils.getMessage("tipoSeveridade_normal"));
		tiposSeveridadeOcorrencia.put(TIPO_SEVERIDADE_OCORRENCIA_MAIOR, FacesUtils.getMessage("tipoSeveridade_maior"));
		tiposSeveridadeOcorrencia.put(TIPO_SEVERIDADE_OCORRENCIA_CRITICO, FacesUtils.getMessage("tipoSeveridade_critico"));
		tiposSeveridadeOcorrencia.put(TIPO_SEVERIDADE_OCORRENCIA_IMPACTANTE, FacesUtils.getMessage("tipoSeveridade_impactante"));
		tiposSeveridadeOcorrencia.put(TIPO_SEVERIDADE_OCORRENCIA_DEFEITO, FacesUtils.getMessage("tipoSeveridade_defeito"));
				
		tiposStatusOcorrencia.put(TIPO_STATUS_OCORRENCIA_ABERTA, FacesUtils.getMessage("tipoStatusOcorrencia_aberta"));
		tiposStatusOcorrencia.put(TIPO_STATUS_OCORRENCIA_CANCELADA, FacesUtils.getMessage("tipoStatusOcorrencia_cancelada"));
		tiposStatusOcorrencia.put(TIPO_STATUS_OCORRENCIA_EM_ANALISE, FacesUtils.getMessage("tipoStatusOcorrencia_emAnalise"));
		tiposStatusOcorrencia.put(TIPO_STATUS_OCORRENCIA_ANALISADA, FacesUtils.getMessage("tipoStatusOcorrencia_analisada"));
		tiposStatusOcorrencia.put(TIPO_STATUS_OCORRENCIA_PENDENTE, FacesUtils.getMessage("tipoStatusOcorrencia_pendente"));
		tiposStatusOcorrencia.put(TIPO_STATUS_OCORRENCIA_ENCERRADA, FacesUtils.getMessage("tipoStatusOcorrencia_encerrada"));

		tiposObservacoes.put(TIPO_OBSERVACAO_NORMAL, FacesUtils.getMessage("tipoObservacao_normal"));
		tiposObservacoes.put(TIPO_OBSERVACAO_IMPORTANTE, FacesUtils.getMessage("tipoObservacao_importante"));
		tiposObservacoes.put(TIPO_OBSERVACAO_GRAVE, FacesUtils.getMessage("tipoObservacao_grave"));

		tiposRepeticoes.put(TIPO_REPETICAO_UNICA, FacesUtils.getMessage("tipoRepeticao_unica"));
		tiposRepeticoes.put(TIPO_REPETICAO_SEMANAL, FacesUtils.getMessage("tipoRepeticao_semanal"));
		tiposRepeticoes.put(TIPO_REPETICAO_MENSAL, FacesUtils.getMessage("tipoRepeticao_mensal"));
		tiposRepeticoes.put(TIPO_REPETICAO_ANUAL, FacesUtils.getMessage("tipoRepeticao_anual"));
		
		tiposAssociacoes.put(TIPO_ASSOC_NAO_HA, FacesUtils.getMessage("tipoAssociacao_naoHa"));
		tiposAssociacoes.put(TIPO_ASSOC_OBRIGATORIA, FacesUtils.getMessage("tipoAssociacao_obrigatoria"));
		tiposAssociacoes.put(TIPO_ASSOC_FACULTATIVA, FacesUtils.getMessage("tipoAssociacao_facultativa"));
		

		tiposInventario.put(TIPO_CATEG_INVENT_ARCOND, FacesUtils.getMessage("tipoInventario_arCond"));
		tiposInventario.put(TIPO_CATEG_INVENT_CADEIRA, FacesUtils.getMessage("tipoInventario_cadeira"));
		tiposInventario.put(TIPO_CATEG_INVENT_CD_DVD, FacesUtils.getMessage("tipoInventario_cd_dvd"));
		tiposInventario.put(TIPO_CATEG_INVENT_COZINHA, FacesUtils.getMessage("tipoInventario_cozinha"));
		tiposInventario.put(TIPO_CATEG_INVENT_GABINETE, FacesUtils.getMessage("tipoInventario_gabinete"));
		tiposInventario.put(TIPO_CATEG_INVENT_IMPRESSORA, FacesUtils.getMessage("tipoInventario_impressora"));
		tiposInventario.put(TIPO_CATEG_INVENT_LIVRO, FacesUtils.getMessage("tipoInventario_livro"));
		tiposInventario.put(TIPO_CATEG_INVENT_MESA, FacesUtils.getMessage("tipoInventario_mesa"));
		tiposInventario.put(TIPO_CATEG_INVENT_MONITOR, FacesUtils.getMessage("tipoInventario_monitor"));
		tiposInventario.put(TIPO_CATEG_INVENT_MOUSE, FacesUtils.getMessage("tipoInventario_mouse"));
		tiposInventario.put(TIPO_CATEG_INVENT_NOTEBOOK, FacesUtils.getMessage("tipoInventario_notebook"));
		tiposInventario.put(TIPO_CATEG_INVENT_OUTROS, FacesUtils.getMessage("tipoInventario_outros"));
		tiposInventario.put(TIPO_CATEG_INVENT_REVISTA, FacesUtils.getMessage("tipoInventario_revista"));
		tiposInventario.put(TIPO_CATEG_INVENT_TECLADO, FacesUtils.getMessage("tipoInventario_teclado"));
		tiposInventario.put(TIPO_CATEG_INVENT_TELEFONE, FacesUtils.getMessage("tipoInventario_telefone"));
	}
	
	
	public static String getTipoUsuario(int id) {
		if (tiposUsuario.isEmpty()) {
			init();
		}
		return tiposUsuario.get(id);
	}

	public static Map<Integer, String> getTiposUsuarios() {
		if (tiposUsuario.isEmpty()) {
			init();
		}
		return tiposUsuario;
	}
	
	public static String getTipoAcesso(int id) {
		if (tiposAcesso.isEmpty()) {
			init();
		}
		return tiposAcesso.get(id);
	}

	public static Map<Integer, String> getTiposAcessos() {
		if (tiposAcesso.isEmpty()) {
			init();
		}
		return tiposAcesso;
	}
	
	public static String getTipoLinguagem(int id) {
		if (tiposLinguagem.isEmpty()) {
			init();
		}
		return tiposLinguagem.get(id);
	}
	
	public static Map<Integer, String> getTiposLinguagens() {
		if (tiposLinguagem.isEmpty()) {
			init();
		}
		return tiposLinguagem;
	}
	
	public static String getTipoFasesProjeto(int id) {
		if (tiposFasesProjeto.isEmpty()) {
			init();
		}
		return tiposFasesProjeto.get(id);
	}
	
	public static Map<Integer, String> getTiposFasesProjeto() {
		if (tiposFasesProjeto.isEmpty()) {
			init();
		}
		return tiposFasesProjeto;
	}
	
	public static String getTipoSeveridadeOcorrencia(int id) {
		if (tiposSeveridadeOcorrencia.isEmpty()) {
			init();
		}
		return tiposSeveridadeOcorrencia.get(id);
	}
	
	public static Map<Integer, String> getTiposSeveridadesOcorrencia() {
		if (tiposSeveridadeOcorrencia.isEmpty()) {
			init();
		}
		return tiposSeveridadeOcorrencia;
	}
		
	public static String getTipoStatusOcorrencia(int id) {
		if (tiposStatusOcorrencia.isEmpty()) {
			init();
		}
		return tiposStatusOcorrencia.get(id);
	}
	
	public static Map<Integer, String> getTiposStatusOcorrencia() {
		if (tiposStatusOcorrencia.isEmpty()) {
			init();
		}
		return tiposStatusOcorrencia;
	}

	public static String getTiposObservacoes(int id) {
		if (tiposObservacoes.isEmpty()) {
			init();
		}
		return tiposObservacoes.get(id);
	}

	public static Map<Integer, String> getTiposObservacoes() {
		if (tiposObservacoes.isEmpty()) {
			init();
		}
		return tiposObservacoes;
	}

	public static String getTiposRepeticoes(int id) {
		if (tiposRepeticoes.isEmpty()) {
			init();
		}
		return tiposRepeticoes.get(id);
	}

	public static Map<Integer, String> getTiposRepeticoes() {
		if (tiposRepeticoes.isEmpty()) {
			init();
		}
		return tiposRepeticoes;
	}
	
	public static String getTipoAssociacao(int id) {
		if (tiposAssociacoes.isEmpty()) {
			init();
		}
		return tiposAssociacoes.get(id);
	}

	public static Map<Integer, String> getTiposAssociacoes() {
		if (tiposAssociacoes.isEmpty()) {
			init();
		}
		return tiposAssociacoes;
	}	
	
	public static String getTipoInventario(int id) {
		if (tiposInventario.isEmpty()) {
			init();
		}
		return tiposInventario.get(id);
	}

	public static Map<Integer, String> getTiposInventario() {
		if (tiposInventario.isEmpty()) {
			init();
		}
		return tiposInventario;
	}
	
	public static String getTipoCategoriaInventario(int id) {
		if (tiposInventario.isEmpty()) {
			init();
		}
		return tiposInventario.get(id);
	}
}
