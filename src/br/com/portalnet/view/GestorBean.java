/**
 * @since 08/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.TabView;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.portalnet.control.GestorServiceController;
import br.com.portalnet.model.AlocacaoAtividade;
import br.com.portalnet.model.Atividade;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Compromisso;
import br.com.portalnet.model.CustoRealProjeto;
import br.com.portalnet.model.Equipe;
import br.com.portalnet.model.Ferias;
import br.com.portalnet.model.GrupoAtividade;
import br.com.portalnet.model.ObservacoesProjeto;
import br.com.portalnet.model.Ocorrencia;
import br.com.portalnet.model.Produto;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.RegistroAtividade;
import br.com.portalnet.model.Tipos;
import br.com.portalnet.util.DataUtil;
import br.com.portalnet.util.EmailUtil;
import br.com.portalnet.util.FacesUtils;


@ManagedBean(name="gestorBean")
@ViewScoped
public class GestorBean {

	@ManagedProperty(value="#{gestorService}")
	private GestorServiceController gestorService;
	
	@ManagedProperty(value="#{sessionBean}")
	private SessionBean sessionBean;
	
	@ManagedProperty(value="#{utilBean}")
	private UtilBean utilBean;
	
	private String nomeTab;
	
	private List<AlocacaoAtividade> listaAlocacoes;	
	
	private List<CustoRealProjeto> listaCustosReaisTodosProjeto;
	
	public GestorServiceController getGestorService() {
		return gestorService;
	}

	public void setGestorService(GestorServiceController gestorService) {
		this.gestorService = gestorService;
	}
	
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	
	public void setUtilBean(UtilBean utilBean) {
		this.utilBean = utilBean;
	}
	
	public UtilBean getUtilBean() {
		return utilBean;
	}

	public List<AlocacaoAtividade> getListaAlocacoes() {
		if (listaAlocacoes == null) {
			listaAlocacoes = new ArrayList<AlocacaoAtividade>();
			
			if (sessionBean.getAtividade() != null)
				listaAlocacoes.addAll(sessionBean.getAtividade().getAlocacoes());
		}
		return listaAlocacoes;
	}

	public void setListaAlocacoes(List<AlocacaoAtividade> listaAlocacoes) {
		this.listaAlocacoes = listaAlocacoes;
	}

	
	public String cancelAction() {

		if(sessionBean.isFlagAlteracao()){
			String msg = FacesUtils.getMessage("cadastro_erroSalvar");
			FacesUtils.addInfoMessage(msg);
			sessionBean.setFlagAlteracao(false);
			return NavigationResults.FAILURE;
		}
		return NavigationResults.SUCCESS;
	}
	
	public void setNomeTab(String nomeTab) {
		this.nomeTab = nomeTab;
	}

	public String cadastrarNovoProjetoAction() {
		Projeto projeto = new Projeto();
		projeto.setProduto(sessionBean.getProduto());
		sessionBean.setProjeto(projeto);
		return NavigationResults.NEW;
	}
	
	public String cadastrarNovaFeriasAction() {
		Ferias ferias = new Ferias();
		sessionBean.setFerias(ferias);
		return NavigationResults.NEW;
	}
	
	public String cadastrarNovoGrupoAtividadeAction() {
		GrupoAtividade grupoAtividade = new GrupoAtividade();
		grupoAtividade.setProjeto(sessionBean.getProjeto());
		sessionBean.setGrupoAtividade(grupoAtividade);
		return NavigationResults.NEW3;
	}
	
	public String cadastrarNovaAtividadeAction() {
		Atividade atividade = new Atividade();
		atividade.setGrupoAtividade(sessionBean.getGrupoAtividade());
		sessionBean.setAtividade(atividade);
		return NavigationResults.NEW;
	}
	
	public String cadastrarNovoCompromissoAction() {
		Compromisso compromisso = new Compromisso();
		compromisso.setProjeto(sessionBean.getProjeto());
		sessionBean.setCompromisso(compromisso);
		return NavigationResults.NEW2;
	}
	
	public void cadastrarNovaAlocacaoAction(ActionEvent event) {
		DataTable tabelaAtividades = (DataTable) event.getComponent().getParent().getParent().getParent();
		Atividade atividade = (Atividade) tabelaAtividades.getRowData();
		
		AlocacaoAtividade alocacao = new AlocacaoAtividade();
		alocacao.setAtividade(atividade);
		alocacao.setQtdeHorasAlocadas(atividade.getQtdeHorasDisponiveisAlocacao());
		alocacao.setQtdeHorasRestantes(atividade.getQtdeHorasDisponiveisAlocacao());
		
		sessionBean.setAtividade(atividade);
		sessionBean.setAlocacao(alocacao);
	}
	
	public String cadastrarProjetoAction() {
		Projeto projeto = sessionBean.getProjeto();
		
		try {
			//verifica se foi cadastrada nova observação e envia e-mail
			for (ObservacoesProjeto observacao : projeto.getObservacoes()) {
				if (observacao.getId() == null && !observacao.isFlagVisibilidadeRestrita()) {
					
					StringBuilder msg = new StringBuilder();
					List<String> listaMailsColaboradores = new ArrayList<String>();
					String assinaturaEmail;
					assinaturaEmail = "\n\nEste é um email automático, não há necessidade de respondê-lo.\nAtenciosamente,\nAutoManager/FACTI_NET";
					EmailUtil emailUtil = new EmailUtil("mail.facti-rp.com.br", true, "automanager@facti-rp.com.br", "manager2009", assinaturaEmail);
					
					msg.append(FacesUtils.getMessage("cadastrarObservacoes_ola"));
					msg.append("\n\n" + FacesUtils.getMessage("cadastrarObservacoes_informeNova") + " " + observacao.getProjeto().getNome());
					msg.append("\n" + FacesUtils.getMessage("cadastrarObservacoes_observacao") + ": " + observacao.getObservacoes());
					
					for (GrupoAtividade grupoAtividade : projeto.getGruposAtividades()) {
						for (Atividade atividade: grupoAtividade.getAtividades()) {
							for (AlocacaoAtividade alocacao : atividade.getAlocacoesColaboradoresAtivos()) {
								if (!listaMailsColaboradores.contains(alocacao.getColaborador().getEmail()))
									listaMailsColaboradores.add(alocacao.getColaborador().getEmail());						
							} 
						}
					}
					if (listaMailsColaboradores.size() > 0) {
						emailUtil.sendEmail(listaMailsColaboradores, "[FACTI_NET] Observação Adicionada ao Projeto", msg.toString());
					}
				}
			}
			
			gestorService.cadastrarProjeto(projeto);
			
		} catch (DataIntegrityViolationException sqle) {
			String msg = FacesUtils.getMessage("cadastrarProjeto_projetoJaCadastrado");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		sessionBean.setFlagAlteracao(false);
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarGrupoAtividadeAction() {
		Projeto projeto = sessionBean.getProjeto();
		GrupoAtividade grupoAtividade = sessionBean.getGrupoAtividade();
		
		projeto.adicionarGrupoAtividade(grupoAtividade);
		nomeTab = "atividades";
		sessionBean.setFlagAlteracao(true);
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarAtividadeAction() {
		int qtdeHorasAlocadas = 0;
		GrupoAtividade grupoAtividade = sessionBean.getGrupoAtividade();
		Atividade atividade = sessionBean.getAtividade();
		
		if (atividade.getId() == null && atividade.getIdSequencia() != -1) {
			for (Atividade atv : grupoAtividade.getAtividades()){
				if(atv.getNome().equals(atividade.getNome())){
					String msg = FacesUtils.getMessage("cadastrarAtividade_mesmoNome");
					FacesUtils.addErrorMessage(msg);
					return NavigationResults.FAILURE;
				}
			}
		}
		
		for (AlocacaoAtividade alocacao : atividade.getAlocacoes()) {
			qtdeHorasAlocadas += alocacao.getQtdeHorasAlocadas();
		}
		
		if (atividade.getQtdeHorasPrevistas() < qtdeHorasAlocadas) {
			atividade.setQtdeHorasPrevistas(qtdeHorasAlocadas);
			
			String msg = FacesUtils.getMessage("cadastrarAtividade_erroHorasPrevistasMenor");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		atividade.getGrupoAtividade().removerAtividade(atividade);
		grupoAtividade.adicionarAtividade(atividade);
		atividade.setGrupoAtividade(grupoAtividade);
		nomeTab = "atividades";
		sessionBean.setFlagAlteracao(true);
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarCompromissoAction() {
		
		Projeto projeto = sessionBean.getProjeto();
		Compromisso compromisso = sessionBean.getCompromisso();
		
		if (compromisso.getNomeResponsaveis().equals("")) {
			String msg = FacesUtils.getMessage("cadastrarCompromisso_selecionarResponsavel");
			FacesUtils.addInfoMessage(msg);
			sessionBean.setFlagAlteracao(false);
			return NavigationResults.FAILURE;
		}
		
		if (compromisso.getId() != null) {
			compromisso.setFlagEnvioEmail(false);
		}
		
		if (compromisso.getDataEvento() == null){
			compromisso.setDataEvento(Calendar.getInstance());
		}
		
		projeto.adicionarCompromisso(compromisso);
		nomeTab = "compromissos";
		sessionBean.setFlagAlteracao(true);
		return NavigationResults.SUCCESS;
	}
	
	/**
	 * 
	 * Realiza o cadastro de custo real por meio da tela de cadastro de projetos
	 * 
	 */
	public void cadastrarCustoRealProjetoAction() {
		Projeto projeto = sessionBean.getProjeto();
		CustoRealProjeto custoRealProjeto = sessionBean.getCustoRealProjeto();
		int anoSelecionado = utilBean.getAnoSelecionado();
		
		custoRealProjeto.setProjeto(projeto);	
		custoRealProjeto.setAno(anoSelecionado);
		
		projeto.adicionarCustoRealProjeto(custoRealProjeto);
		
		if (custoRealProjeto.getValor() == 0) {
			projeto.removerCustoRealProjeto(custoRealProjeto);
		}
		
		sessionBean.setFlagAlteracao(true);
	}
	
	/**
	 * 
	 * Realiza o cadastro de custo real por meio da tela de cadastro de custo real
	 * 
	 */
	public String cadastrarNovoCustoRealProjetoAction(){
		int anoSelecionado;
		int mesSelecionado;
		
		for(CustoRealProjeto custoRealProjeto: listaCustosReaisTodosProjeto){
			
			anoSelecionado = utilBean.getAnoSelecionado();
			mesSelecionado = utilBean.getMesSelecionado();
			custoRealProjeto.setAno(anoSelecionado);
			custoRealProjeto.setMes(mesSelecionado);
			
			if ( custoRealProjeto.getValor() == 0 && custoRealProjeto.getId() != 0 ) {
				custoRealProjeto.getProjeto().getCustosReaisProjeto().remove(custoRealProjeto);
				gestorService.excluirCustoRealProjeto(custoRealProjeto);
			}else if ( custoRealProjeto.getValor() != 0 ) {
				gestorService.cadastrarCustoRealProjeto(custoRealProjeto);
			}
		}
		
		listaCustosReaisTodosProjeto = null;
		sessionBean.setFlagAlteracao(false);
		return NavigationResults.SUCCESS;				
	}
	
	public String getNomeTab() {
		return nomeTab;
	}

	public String cadastrarAlocacaoAction() {
		AlocacaoAtividade alocacao = sessionBean.getAlocacao();
		Atividade atividade = alocacao.getAtividade();
		
		// Se eh uma nova alocacao entao verifica se 
		// ja nao existe um colaborador cadastrado para esta atividade
		if (alocacao.getId() == null &&
			(!atividade.getGrupoAtividade().getProjeto().isGenerico())) {
			
			if(atividade.getAlocacoes().contains(alocacao)) {
				String msg = FacesUtils.getMessage("cadastrarAlocacao_alocacaoJaCadastrada");
				FacesUtils.addErrorMessage(msg);
				return NavigationResults.FAILURE;
			}
		}
		
		// Se nao for atividade de um projeto generico
		// entao consiste se a quantidade de horas previstas nao estourou
		if (!atividade.getGrupoAtividade().getProjeto().isGenerico()) {
			int qtdeHorasAlocadas = 0;
			
			//se a quantidade de horas previstas e restantes para um projeto nao generico for zero, retorna erro
			if (alocacao.getQtdeHorasAlocadas() == 0 && alocacao.getQtdeHorasRestantes() == 0
					&& alocacao.getQtdeHorasTrabalho() == 0) {
				String msg = FacesUtils.getMessage("cadastrarAlocacao_erroPrevistasRestantes");
				FacesUtils.addErrorMessage(msg);
				return NavigationResults.FAILURE;
			}
			
			if (!atividade.getAlocacoes().contains(alocacao)) {
				qtdeHorasAlocadas += alocacao.getQtdeHorasAlocadas();
			}
			
			for (AlocacaoAtividade alocacaoAux : atividade.getAlocacoes()) {
				qtdeHorasAlocadas += alocacaoAux.getQtdeHorasAlocadas();
			}
			
			if (qtdeHorasAlocadas > atividade.getQtdeHorasPrevistas()) {
				alocacao.setQtdeHorasAlocadas(atividade.getQtdeHorasPrevistas() -
						(qtdeHorasAlocadas - alocacao.getQtdeHorasAlocadas()));
				alocacao.setQtdeHorasRestantes(alocacao.getQtdeHorasAlocadas() - alocacao.getQtdeHorasTrabalho());
				
				String msg = FacesUtils.getMessage("cadastrarAlocacao_erroExcedeAlocacao", alocacao.getQtdeHorasAlocadas(), atividade.getQtdeHorasPrevistas());
				FacesUtils.addErrorMessage(msg);
				return NavigationResults.FAILURE;
			}
			
			if(alocacao.getQtdeHorasRestantes() <= 0 && alocacao.getFlagBloqueadoRegistro() != 1)
				alocacao.setFlagBloqueadoRegistro(2);
			else
				if(alocacao.getQtdeHorasRestantes() > 0 && alocacao.getFlagBloqueadoRegistro() == 2)
					alocacao.setFlagBloqueadoRegistro(0);

			alocacao.setCustoHoraAlocacao(alocacao.getColaborador().getValorCustoHora());
			atividade.adicionarAlocacao(alocacao);
			
			gestorService.cadastrarAlocacaoAtividade(alocacao);
			
		} else {
			List<AlocacaoAtividade> listaAlocacoesAntes = new ArrayList<AlocacaoAtividade>();
			listaAlocacoesAntes.addAll(atividade.getAlocacoesColaboradoresAtivos());
			
			// Se for projeto generico
			// Remove as alocacoes excluidas na selecao 
			for(AlocacaoAtividade alocacaoAux : listaAlocacoesAntes) { 
				if (listaAlocacoes == null || !listaAlocacoes.contains(alocacaoAux)) {
					if (!alocacaoAux.getRegistros().isEmpty()) {
						String msg = FacesUtils.getMessage("cadastrarAlocacao_erroExcluirAlocacao");
						FacesUtils.addErrorMessage(msg);
						return NavigationResults.FAILURE;
					}
					
					atividade.removerAlocacao(alocacaoAux);
					gestorService.excluirAlocacaoAtividade(alocacaoAux);
				}
			}

			// E adiciona as alocacoes ainda nao cadastradas 
			for(AlocacaoAtividade alocacaoAux : listaAlocacoes) {
				if (!atividade.getAlocacoes().contains(alocacaoAux)) {
					alocacaoAux.setFlagBloqueadoRegistro(alocacao.getFlagBloqueadoRegistro());
					atividade.adicionarAlocacao(alocacaoAux);
					
					gestorService.cadastrarAlocacaoAtividade(alocacaoAux);
				}
			}
		}
		
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarFeriasAction(){
		Ferias ferias = sessionBean.getFerias();
		Calendar dataFimFerias = (Calendar)ferias.getDataInicioFerias().clone();
		String qtdeDias = FacesUtils.getRequestProperty("formGeral:qtdeDiasCorridos");
		dataFimFerias.add(Calendar.DAY_OF_MONTH, (Integer.parseInt(qtdeDias)-1));
		
		ferias.setDataFimFerias(dataFimFerias);

		if (ferias.getDataInicioRef().after(ferias.getDataFimRef()) ||
			ferias.getDataInicioFerias().before(ferias.getDataFimRef())) {
			String msg = FacesUtils.getMessage("cadastrarFerias_datasIncompativeis");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		gestorService.cadastrarFerias(ferias);
		return NavigationResults.SUCCESS;
	}
	
	public String selecionarFeriasAction() {
		int idFerias = Integer.parseInt(FacesUtils.getRequestProperty("idFerias"));
		sessionBean.setFerias(gestorService.getFeriasPK(idFerias));
		return NavigationResults.EDIT;
	}
	
	public void excluirFeriasAction(ActionEvent event) {
		selecionarFeriasAction();
		Ferias ferias = sessionBean.getFerias();
		gestorService.excluirFerias(ferias);
	}
	
	public String selecionarProjetoAction() {
		int idProjeto = Integer.parseInt(FacesUtils.getRequestProperty("idProjeto"));
		sessionBean.setProjeto(gestorService.getProjetoPK(idProjeto));
		return NavigationResults.EDIT;
	}
	
	public void selecionarGrupoAtividadeAction(ActionEvent event) {
		TabView tabelaGrupoAtividades = (TabView) event.getComponent().getParent().getParent().getParent();
		GrupoAtividade grupoAtividade = (GrupoAtividade) tabelaGrupoAtividades.getRowData();
		sessionBean.setGrupoAtividade(grupoAtividade);
	}
	
	public void selecionarAtividadeAction(ActionEvent event) {
		TabView tabelaGrupoAtividades = (TabView) event.getComponent().getParent().getParent().getParent().getParent().getParent();
		GrupoAtividade grupoAtividade = (GrupoAtividade) tabelaGrupoAtividades.getRowData();
		sessionBean.setGrupoAtividade(grupoAtividade);
		
		DataTable tabelaAtividades = (DataTable) event.getComponent().getParent().getParent().getParent();
		Atividade atividade = (Atividade) tabelaAtividades.getRowData();
		
		atividade.setIdSequencia(-1L);
		sessionBean.setAtividade(atividade);
	}
	
	public void selecionarCompromissoAction(ActionEvent event) {
		DataTable tabelaCompromissos = (DataTable) event.getComponent().getParent().getParent().getParent();
		Compromisso compromisso = (Compromisso) tabelaCompromissos.getRowData();
		
		sessionBean.setCompromisso(compromisso);
	}
	
	public void selecionarObservacaoAction(ActionEvent event) {
		DataTable tabelaObservacoes = (DataTable) event.getComponent().getParent().getParent().getParent();
		ObservacoesProjeto observacao = (ObservacoesProjeto) tabelaObservacoes.getRowData();
		
		sessionBean.setObservacao(observacao);
	}
	
	public void selecionarAlocacaoAction(ActionEvent event) {
		DataTable tabelaAlocacoes = (DataTable) event.getComponent().getParent().getParent().getParent();
		AlocacaoAtividade alocacao = (AlocacaoAtividade) tabelaAlocacoes.getRowData();
		
		sessionBean.setAlocacao(alocacao);
	}
	
	public void selecionarCustoRealProjetoAction(ActionEvent event) {
		int idCustoRealProjeto = Integer.parseInt(FacesUtils.getRequestProperty("idCustoRealProjeto"));
		sessionBean.setCustoRealProjeto(gestorService.getCustoRealProjetoPK(idCustoRealProjeto));
	}
	
	public void excluirProjetoAction(ActionEvent event) {
		selecionarProjetoAction();
		Projeto projeto = sessionBean.getProjeto();
		
		if (!projeto.getGruposAtividades().isEmpty()) {
			String msg = FacesUtils.getMessage("cadastrarProjeto_erroExcluirProjeto");
			FacesUtils.addErrorMessage(msg);
			return;
		}
		
		gestorService.excluirProjeto(projeto);
	}
	
	public void excluirGrupoAtividadeAction(ActionEvent event) {
		selecionarGrupoAtividadeAction(event);
		GrupoAtividade grupoAtividade = sessionBean.getGrupoAtividade();
		
		for (Atividade atividade : grupoAtividade.getAtividades()) {
			if (!atividade.getAlocacoes().isEmpty()) {
				String msg = FacesUtils.getMessage("cadastrarAtividade_erroExcluirAtividade");
				FacesUtils.addErrorMessage(msg);
				return;
			}
		}
		
		grupoAtividade.getProjeto().removerGrupoAtividade(grupoAtividade);
		sessionBean.setFlagAlteracao(true);
	}
	
	public void excluirAtividadeAction(ActionEvent event) {
		selecionarAtividadeAction(event);
		Atividade atividade = sessionBean.getAtividade();
		
		if (!atividade.getAlocacoes().isEmpty()) {
			String msg = FacesUtils.getMessage("cadastrarAtividade_erroExcluirAtividade");
			FacesUtils.addErrorMessage(msg);
			return;
		}
		
		atividade.getGrupoAtividade().removerAtividade(atividade);
		sessionBean.setFlagAlteracao(true);
	}
	
	public void excluirCompromissoAction(ActionEvent event) {
		selecionarCompromissoAction(event);
		Compromisso compromisso = sessionBean.getCompromisso();
		
		compromisso.getProjeto().removerCompromisso(compromisso);
		sessionBean.setFlagAlteracao(true);
	}
	
	public void excluirObservacaoAction(ActionEvent event) {
		selecionarObservacaoAction(event);
		ObservacoesProjeto observacao = sessionBean.getObservacao();
		
		observacao.getProjeto().removerObservacao(observacao);
		sessionBean.setFlagAlteracao(true);
	}
	
	public void excluirAlocacaoAction(ActionEvent event) {
		selecionarAlocacaoAction(event);
		AlocacaoAtividade alocacao = sessionBean.getAlocacao();
		Atividade atividade = alocacao.getAtividade();
		
		if (!alocacao.getRegistros().isEmpty()) {
			String msg = FacesUtils.getMessage("cadastrarAlocacao_erroExcluirAlocacao");
			FacesUtils.addErrorMessage(msg);
			return;
		}
		
		atividade.removerAlocacao(alocacao);
		gestorService.excluirAlocacaoAtividade(alocacao);
	}
	
	public void excluirTodasAlocacoesAction(ActionEvent event) {
		DataTable tabelaAtividades = (DataTable) event.getComponent().getParent().getParent().getParent();
		Atividade atividade = (Atividade) tabelaAtividades.getRowData();
		List<AlocacaoAtividade> listaAlocacoesAExcluir = new ArrayList<AlocacaoAtividade>();
		boolean haMensagemErro = false;
		
		for (AlocacaoAtividade alocacao : atividade.getAlocacoes()) {
			// Exclui somente as alocacoes que nao possuem lancamentos registrados
			if (!alocacao.getRegistros().isEmpty()) {
				if (!haMensagemErro) {
					String msg = FacesUtils.getMessage("cadastrarAlocacao_erroExcluirAlocacao");
					FacesUtils.addErrorMessage(msg);
					haMensagemErro = true;
				}
				
				continue;
			}
			
			listaAlocacoesAExcluir.add(alocacao);
		}
		
		for (AlocacaoAtividade alocacao : listaAlocacoesAExcluir) {
			atividade.removerAlocacao(alocacao);
		}
		
		gestorService.cadastrarProjeto(atividade.getGrupoAtividade().getProjeto());
	}
	
	public void excluirCustoRealProjetoAction(ActionEvent event) {
		selecionarCustoRealProjetoAction(event);
		CustoRealProjeto custo = sessionBean.getCustoRealProjeto();
		
		custo.getProjeto().removerCustoRealProjeto(custo);
		sessionBean.setFlagAlteracao(true);
	}

	public void alterarEstadoProjetoAction(ActionEvent event) {
		selecionarProjetoAction();
		Projeto projeto = sessionBean.getProjeto();
		
		switch (projeto.getEstado()) {
			case Projeto.ESTADO_HABILITADO:
				projeto.setEstado(Projeto.ESTADO_DESABILITADO);
				break;
			case Projeto.ESTADO_DESABILITADO:
				projeto.setEstado(Projeto.ESTADO_HABILITADO);
				break;
		}
		
		gestorService.cadastrarProjeto(projeto);
	}
	
	public String alterarFlagCompromisso() {
		Compromisso compromisso = null;
		Projeto projeto;
		long id_compromisso;
		
		projeto = sessionBean.getProjeto();
		id_compromisso = Long.parseLong(FacesUtils.getRequestProperty("idCompromisso"));
		
		for(Compromisso compromissoAux : projeto.getCompromissos()) {
			if (compromissoAux.getId() != null && compromissoAux.getId() == id_compromisso) {
				compromisso = compromissoAux;
				break;
			}
		}
		
		if(compromisso.isFlagConcluido()) {
			compromisso.setFlagConcluido(false);
			compromisso.setDataConclusao(null);
		} else {
			compromisso.setFlagConcluido(true);
			compromisso.setDataConclusao(Calendar.getInstance());
		}
		
		sessionBean.setFlagAlteracao(true);
		return NavigationResults.SUCCESS_ALTERAR_FLAG;
	}

	public List<Projeto> getListaProjetos() {
		Produto produto = sessionBean.getProduto();
		Colaborador colaborador = sessionBean.getColaboradorLogado();
		if (produto== null)
			return gestorService.getListaProjetos(null, false, true, colaborador);
		else
			return gestorService.getListaProjetos(produto, false, false, colaborador);
	}
	
	public String acessarCadastroProjetoAction() {
		long id_projeto = Long.parseLong(FacesUtils.getRequestProperty("idProjeto"));
		Projeto projeto = gestorService.getProjetoPK(id_projeto);
		
		sessionBean.setProjeto(projeto);
				
		return NavigationResults.SUCCESS_CADASTRO_PROJETO;
	}

	public String acessarAlocacaoProjetoAction() {
		long id_projeto = Long.parseLong(FacesUtils.getRequestProperty("idProjeto"));
		Projeto projeto = gestorService.getProjetoPK(id_projeto);
		
		sessionBean.setCliente(projeto.getProduto().getArea().getCliente());
		sessionBean.setArea(projeto.getProduto().getArea());
		sessionBean.setProduto(projeto.getProduto());
		sessionBean.setProjeto(projeto);
				
		return NavigationResults.SUCCESS_ALOCACAO_PROJETO;
	}
	
	public String cadastrarObservacaoAction() {
		String observacao;
		Integer tipoObsVal = 0;
		String flagVisibilidadeRestrita;
		Calendar dataInclusao = Calendar.getInstance();
		
		try {			
			observacao = FacesUtils.getRequestProperty("formGeral:cadastroObservacao:observacao");
			tipoObsVal = Integer.parseInt(FacesUtils.getRequestProperty("formGeral:cadastroObservacao:flagTipoObs"));		
			flagVisibilidadeRestrita = FacesUtils.getRequestProperty("formGeral:cadastroObservacao:flagVisibilidadeRestrita");
			
		} catch (Exception pe) {
			String msg = FacesUtils.getMessage("erro_erroInterno");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}

		ObservacoesProjeto observacaoProjeto = new ObservacoesProjeto();
		Projeto projeto = sessionBean.getProjeto();
		observacaoProjeto.setProjeto(projeto);
		observacaoProjeto.setObservacoes(observacao);
		observacaoProjeto.setDataInclusao(dataInclusao);
		observacaoProjeto.setTipo(tipoObsVal);
		
		if(flagVisibilidadeRestrita==null){
			observacaoProjeto.setFlagVisibilidadeRestrita(false);
		}else{
			observacaoProjeto.setFlagVisibilidadeRestrita(true);
		}
	
		sessionBean.setFlagAlteracao(true);
		projeto.adicionarObservacao(observacaoProjeto);
		
		return NavigationResults.SUCCESS_ALTERAR_FLAG;
	}
	
	public List<SelectItem> getListaTiposFasesProjeto() {
		List<SelectItem> listaTiposFasesProjeto = new ArrayList<SelectItem>();
	
		for(Entry<Integer, String> entrada : Tipos.getTiposFasesProjeto().entrySet()) {
			listaTiposFasesProjeto.add(new SelectItem(entrada.getKey().intValue(), entrada.getValue()));
		}
		
		return listaTiposFasesProjeto;
	}

	public String selecionarEquipeAction() {
		int idEquipe = Integer.parseInt(FacesUtils.getRequestProperty("idEquipe"));
		sessionBean.setEquipe(gestorService.getEquipePK(idEquipe));
		return NavigationResults.EDIT;
	}
	
	public String cadastrarNovaEquipeAction() {
		if (gestorService.getListaColaboradoresCoordenadores().isEmpty()) {
			String msg = FacesUtils.getMessage("cadastrarEquipe_naoHaCoordenador");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		sessionBean.setEquipe(new Equipe());
		return NavigationResults.NEW;
	}
	
	public String cadastrarEquipeAction() {
		try {
			Equipe equipe = sessionBean.getEquipe();
			if ( (equipe.getColaboradores() == null) || equipe.getColaboradores().isEmpty()) {
				String msg = FacesUtils.getMessage("cadastrarEquipe_naoHaMembroSelecionado");
				FacesUtils.addErrorMessage(msg);
				return NavigationResults.FAILURE;
			}
			
			gestorService.cadastrarEquipe(equipe);
			
		} catch (DataIntegrityViolationException sqle) {
			String msg = FacesUtils.getMessage("cadastrarEquipe_equipeJaCadastrada");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		return NavigationResults.SUCCESS;
	}

	
	public void excluirEquipeAction(ActionEvent event) {
		selecionarEquipeAction();
		Equipe equipe = sessionBean.getEquipe();

		gestorService.excluirEquipe(equipe);
	}
	
	public void alterarFlagRegistroAtividadeAction(ActionEvent event) {
		selecionarAlocacaoAction(event);
		AlocacaoAtividade alocacao = sessionBean.getAlocacao();
		
		if(alocacao.getFlagBloqueadoRegistro() == 1){
			if(alocacao.getQtdeHorasRestantes() == 0 && !alocacao.getAtividade().getGrupoAtividade().getProjeto().isGenerico())
				alocacao.setFlagBloqueadoRegistro(2);
			else 
				alocacao.setFlagBloqueadoRegistro(0);
		} else {
			alocacao.setFlagBloqueadoRegistro(1);
		}
		
		gestorService.cadastrarAlocacaoAtividade(alocacao);
	}
	
	public List<SelectItem> getListaProgressoEquipes() {
		List<SelectItem> listaSelectEquipes = new ArrayList<SelectItem>();
		List<Equipe> listaMinhasEquipes = gestorService.getListaEquipes(true, sessionBean.getColaboradorLogado());
		
		
		for (Equipe equipe : listaMinhasEquipes) {
			List<SelectItem> listaProgressoRegistro = new ArrayList<SelectItem>();
			
			// Para cada colaborador da lista, calcula a quantidade de horas
			// trabalhadas desde o inicio do periodo contabil ate o dia de hoje
			for(Colaborador colaborador : equipe.getColaboradores()) {
				if (!colaborador.isFlagAtivo())
					continue;
				
				listaProgressoRegistro.add(getProgressoColaborador(colaborador));
			}
			
			Collections.sort(listaProgressoRegistro, new Comparator<SelectItem>() {
				public int compare(SelectItem m1, SelectItem m2) {
					return ((String)m1.getLabel()).compareTo((String)m2.getLabel());
				}
			});
			
			listaSelectEquipes.add(new SelectItem(listaProgressoRegistro, "Equipe " + equipe.getNome()));
		}
		
		return listaSelectEquipes;
	}

	public SelectItem getProgressoColaborador() {
		Colaborador colaborador = sessionBean.getColaboradorLogado();
		return getProgressoColaborador(colaborador);
	}

	public SelectItem getProgressoColaborador(Colaborador colaborador) {
		List<RegistroAtividade> listaRegistrosPeriodo;
		SelectItem progressoColab = new SelectItem();
		Calendar dataHoje = Calendar.getInstance();
		
		if (!colaborador.isFlagAtivo())
			return progressoColab;
				
		Calendar dataInicial = DataUtil.getDataInicioContabil(dataHoje, sessionBean.getParametros().getPrimeiroDiaContabil());
		
		listaRegistrosPeriodo = gestorService.getListaRegistrosPorPeriodo(colaborador, dataInicial, dataHoje);
		
		float qtdeHorasPeriodo = 0;
		int qtdeHorasPrevistas = 0;
		float mediaDiaria = 0;
		DateFormat formatoData = new SimpleDateFormat("yyyyMMdd");
		
		// Calcula a quant de horas trabalhadas ate o dia de hoje
		for (RegistroAtividade registro : listaRegistrosPeriodo) {
			qtdeHorasPeriodo += registro.getQtdeHorasTrabalho();
		}
		
		
		// Calcula a quant de horas previstas ate o dia de hoje
		while(formatoData.format(dataInicial.getTime()).compareTo(formatoData.format(dataHoje.getTime())) <=0) {
			if (DataUtil.colaboradorTrabalhaNoDia(colaborador, dataInicial, true))
				qtdeHorasPrevistas += 8;
			
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		// Calcula a media diaria ate o dia de hoje
		mediaDiaria = qtdeHorasPeriodo * 8 / qtdeHorasPrevistas;
		mediaDiaria = Math.round(mediaDiaria*100)/100.0f;
		
		progressoColab = new SelectItem(
				String.format("%.1f / %dh (Média: %.2fh)",
				qtdeHorasPeriodo, qtdeHorasPrevistas, mediaDiaria).replace(',', '.'),
				colaborador.getNomeCompleto());
		
		return progressoColab;
	}
	
	public List<AlocacaoAtividade> getSelectAlocacoes() {
		List<AlocacaoAtividade> listaAlocacoes = new ArrayList<AlocacaoAtividade>();

		for(Colaborador colaborador : gestorService.getListaColaboradoresInternos()) {
			AlocacaoAtividade alocacaoAux = new AlocacaoAtividade();
			alocacaoAux.setColaborador(colaborador);
			alocacaoAux.setAtividade(sessionBean.getAtividade());
			alocacaoAux.setCustoHoraAlocacao(colaborador.getValorCustoHora());
			
			listaAlocacoes.add(alocacaoAux);
		}
		
		return listaAlocacoes;
	}
	
	public List<Ferias> getListaFerias(){
		int anoSelecionado = utilBean.getAnoSelecionado();
		return gestorService.getListaFerias(anoSelecionado); 
	}
		
	/**
	 * *
	 * Método que busca os custos reais do projeto selecionado organizada por mês
	 * 
	 * @return Lista de custos reais do projeto
	 */
	public List<CustoRealProjeto> getListaCustosReaisProjeto() {
		int anoSelecionado = utilBean.getAnoSelecionado();
		
		List<CustoRealProjeto> listaCustos = sessionBean.getProjeto().getCustosReaisProjeto();
		List<CustoRealProjeto> listaCustosReaisProjeto = new ArrayList<CustoRealProjeto>();	
		
		//Filtra os custos reais de acordo com o ano selecionado
		for (CustoRealProjeto custo : listaCustos) {
			if (custo.getAno() == anoSelecionado) {
				listaCustosReaisProjeto.add(custo);
			}
		}

		//Adiciona custos reais com valor zero para os meses em que não existe registro
		for (int i = 0; i < 12; i++) {
			
			//Verificação necessária devido a possível nullPointerException em listaCustosReaisProjeto.get(i)
			if (listaCustosReaisProjeto.size() > i) {
				
				//intervalo de i: 0 até 11 - intervalo mes: 1 até 12; Por isso a comparação mês != i+1
				if (listaCustosReaisProjeto.get(i).getMes() != i + 1) {
					//mês sem custoReal cadastrado. Set valor zero
					
					CustoRealProjeto CustoRealProjeto = new CustoRealProjeto();
					CustoRealProjeto.setMes(i + 1);
					CustoRealProjeto.setValor(0);
					listaCustosReaisProjeto.add(i, CustoRealProjeto);
				}
			} else {
				CustoRealProjeto CustoRealProjeto = new CustoRealProjeto();
				CustoRealProjeto.setMes(i + 1);
				CustoRealProjeto.setValor(0);
				listaCustosReaisProjeto.add(i, CustoRealProjeto);
				i--; //necessário decrementar i para que comparacao (listaCustosReaisProjeto.size() > i) não retorne sempre false.
			}
		}
		return listaCustosReaisProjeto;
	}
	
	/**
	 * *
	 * Método que busca os custos reais de todos os projetos
	 * 
	 * @return Lista de custos reais
	 */
	public List<CustoRealProjeto> getListaCustosReaisTodosProjetos() {
		
		int anoSelecionado = utilBean.getAnoSelecionado();
		int mesSelecionado = utilBean.getMesSelecionado();
		
		listaCustosReaisTodosProjeto = new ArrayList<CustoRealProjeto>();
				
		for (Projeto projeto : getListaProjetos()) {
			
				//variavel que verifica se existe custo real para o prj no mes/ano selecionados
				boolean existeCusto = false;
				for (CustoRealProjeto custo: projeto.getCustosReaisProjeto()){
					if (custo.getAno() == anoSelecionado
							&& custo.getMes() == mesSelecionado) {
						listaCustosReaisTodosProjeto.add(custo);
						existeCusto = true;
					}
				}
				if(!existeCusto){
					CustoRealProjeto CustoRealProjeto = new CustoRealProjeto();
					CustoRealProjeto.setAno(anoSelecionado);
					CustoRealProjeto.setMes(mesSelecionado);
					CustoRealProjeto.setValor(0);
					CustoRealProjeto.setProjeto(projeto);

					listaCustosReaisTodosProjeto.add(CustoRealProjeto);
				}
		}

		return listaCustosReaisTodosProjeto;
	}
	
	public void setListaCustosReaisTodosProjeto(List<CustoRealProjeto> listaCustosReaisTodosProjeto) {
		this.listaCustosReaisTodosProjeto = listaCustosReaisTodosProjeto;
	}	
	
	public List<Ocorrencia> getListaOcorrenciasProjeto(){
		Projeto projeto = sessionBean.getProjeto();
		List<Ocorrencia> listaOcorrencias = gestorService.getListaOcorrencias(projeto, -1);
		return listaOcorrencias;
	}
	
	public void valueChangeListener(ValueChangeEvent event) {
		Object object = event.getNewValue(); 
		
		if(object instanceof Integer){
			this.getUtilBean().setAnoSelecionado((Integer)object);
		}
	}
	
	public List<Projeto> getListaMeusProjetosGenericos(){
		List<Projeto> listaProjGen = gestorService.getListaMeusProjetosGenericos(sessionBean.getColaboradorLogado());
		return listaProjGen;
	}
	
}