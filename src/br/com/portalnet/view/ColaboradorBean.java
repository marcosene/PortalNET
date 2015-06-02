/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.portalnet.control.ColaboradorServiceController;
import br.com.portalnet.model.AlocacaoAtividade;
import br.com.portalnet.model.Atividade;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Compromisso;
import br.com.portalnet.model.Feriados;
import br.com.portalnet.model.Ferias;
import br.com.portalnet.model.FraseDia;
import br.com.portalnet.model.GrupoAtividade;
import br.com.portalnet.model.ItemInventario;
import br.com.portalnet.model.MuralRecados;
import br.com.portalnet.model.Ocorrencia;
import br.com.portalnet.model.ProdutoInventario;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.RegistroAtividade;
import br.com.portalnet.model.Tipos;
import br.com.portalnet.util.CalendarDataModelImpl;
import br.com.portalnet.util.CalendarDataModelItemImpl;
import br.com.portalnet.util.DataUtil;
import br.com.portalnet.util.EmailUtil;
import br.com.portalnet.util.FacesUtils;


@ManagedBean(name="colaboradorBean")
@ViewScoped
public class ColaboradorBean {

	@ManagedProperty(value="#{colaboradorService}")
	private ColaboradorServiceController colaboradorService;

	@ManagedProperty(value="#{sessionBean}")
	private SessionBean sessionBean;

	private CalendarDataModelImpl agenda;
	
	private Colaborador colaborador;
	
	private String horaInicial;
	
	private String horaFinal;
	
	private FraseDia fraseDia;
	
		
	public ColaboradorServiceController getColaboradorService() {
		return colaboradorService;
	}

	public void setColaboradorService(ColaboradorServiceController colaboradorService) {
		this.colaboradorService = colaboradorService;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public CalendarDataModelImpl getAgenda() {
		if (agenda == null) {
			agenda = new CalendarDataModelImpl();
			agenda.setCurrentDate(Calendar.getInstance().getTime());
		}
		return agenda;
	}

	public void setAgenda(CalendarDataModelImpl agenda) {
		this.agenda = null;
		this.agenda = agenda;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public String getHoraInicial() {
		if (horaInicial == null || horaInicial.isEmpty())
			horaInicial = "08:30"; 
		return horaInicial;
	}

	public void setHoraInicial(String horaInicial) {
		this.horaInicial = horaInicial;
	}

	public String getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}
	
	public void horaInicialChangeListener(ValueChangeEvent event) {
		this.horaInicial = (String) event.getNewValue();
		this.horaFinal = null;
	}
	
	public void imprimeMensagens() {
		// Exibe mensagem dos aniversariantes do dia
		StringBuilder aniversariantes = new StringBuilder();
		for(Colaborador aniversariante : colaboradorService.getListaAniversariantes()) {
			if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == aniversariante.getDiaAniversario() &&
					Calendar.getInstance().get(Calendar.MONTH)+1 == aniversariante.getMesAniversario()) {
				if (aniversariantes.length() == 0) {
					aniversariantes.append("Aniversariante(s) de hoje: ");
				} else {
					aniversariantes.append(", ");
				}

				aniversariantes.append(aniversariante.getNomeCompleto());
			}	
		}
		
		if (aniversariantes.length() != 0) {
			FacesUtils.addInfoMessage(aniversariantes.toString());
		}
		
		// Exibe mensagem de ferias agendadas
		StringBuilder feriasAgendadas = new StringBuilder();
		for(Ferias ferias : sessionBean.getColaboradorLogado().getFerias()) {
			if (ferias.getStatus() == 1) {
				if (feriasAgendadas.length() == 0) {
					feriasAgendadas.append("Férias Agendadas: ");
				} else {
					feriasAgendadas.append(", ");
				}

				feriasAgendadas.append(DataUtil.getDataFormatada(ferias.getDataInicioFerias()) +
						" a " + DataUtil.getDataFormatada(ferias.getDataFimFerias()));
			}	
		}
		
		if (feriasAgendadas.length() != 0) {
			FacesUtils.addInfoMessage(feriasAgendadas.toString());
		}
	}

	public String loginAction() {
		try {
			Colaborador colaborador = colaboradorService.autenticaUsuario(
					sessionBean.getColaborador().getUsuario(), 
					sessionBean.getColaborador().getSenha());

			if (colaborador == null) {
				sessionBean.setLoggedIn(false);
				String msg = FacesUtils.getMessage("login_usuarioSenhaIncorreta");
				FacesUtils.addErrorMessage(msg);
				return NavigationResults.FAILURE;
			}

			if (!colaborador.isFlagAtivo()) {
				sessionBean.setLoggedIn(false);
				String msg = FacesUtils.getMessage("login_usuarioInativo");
				FacesUtils.addErrorMessage(msg);
				return NavigationResults.FAILURE;
			}
			
			// Armazena a data do ultimo login na sessao
			sessionBean.setDataUltLogin(colaborador.getDataUltLogin());
			colaborador.setDataUltLogin(Calendar.getInstance());
			colaboradorService.atualizarColaborador(colaborador);
			
			sessionBean.setColaboradorLogado(colaborador);
			sessionBean.setLoggedIn(true);
			
			imprimeMensagens();
			
		} catch (Exception e) {
			e.printStackTrace();
			String msg = FacesUtils.getMessage("erro_erroInterno");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		return NavigationResults.SUCCESS;
	}

	public String logoutAction() {
		sessionBean.init();
		sessionBean.setColaboradorLogado(null);
		sessionBean.setLoggedIn(false);
		return NavigationResults.LOGOUT;
	}
	
	public FraseDia getFraseDia() {
		if (fraseDia == null)
			fraseDia = colaboradorService.getFraseDiaAleatoria();
		
		return fraseDia;
	}
	
	public long getQtdeRecadosParticulares() {
		return colaboradorService.getQtdeRecadosParticulares(sessionBean.getColaboradorLogado());
	}

	public List<MuralRecados> getListaRecadosParticulares() {
		return colaboradorService.getListaRecadosParticulares(sessionBean.getColaboradorLogado());
	}
	
	public List<Projeto> getListaProjetosAlocados() {
		List<Projeto> listaProjetosAlocados = colaboradorService.getListaProjetosAlocadosAtivos(sessionBean.getColaborador());
		
		// Se for uma modificação em um lançamento ja registrado e a atividade já estava concluida,
		// entao adiciona o proprio projeto desta alocacao a lista
		if (sessionBean.getRegistroAtividade() != null) {
			Projeto projeto = sessionBean.getProjeto();
			
			if (!listaProjetosAlocados.contains(projeto))
				listaProjetosAlocados.add(projeto);
		}
		
		return listaProjetosAlocados; 
	}

	public List<AlocacaoAtividade> getListaAlocacoes() {
		List<AlocacaoAtividade> listaAlocacoes = new ArrayList<AlocacaoAtividade>();

		Projeto projeto = sessionBean.getProjeto();

		if (projeto != null) {
			for (GrupoAtividade grupoAtividade : projeto.getGruposAtividades()) {
				for (Atividade atividadeAux : grupoAtividade.getAtividades()) {
					for (AlocacaoAtividade alocacaoAux : atividadeAux.getAlocacoes()) {
						if (alocacaoAux.getColaborador().equals(sessionBean.getColaborador()) &&
								alocacaoAux.getFlagBloqueadoRegistro() != 1 &&
								(projeto.isGenerico() ||
									(alocacaoAux.equals(sessionBean.getAlocacao()) && sessionBean.getRegistroAtividade() != null) ||
									alocacaoAux.getQtdeHorasRestantes() > 0)) {
							listaAlocacoes.add(alocacaoAux);
						}
					}
				}
			}
		}
		
		Collections.sort(listaAlocacoes, new Comparator<AlocacaoAtividade>() {
			public int compare(AlocacaoAtividade aa1, AlocacaoAtividade aa2) {
				return (aa1.getAtividade().getGrupoAtividade().getNome() + 
						aa1.getAtividade().getNome()).compareToIgnoreCase(
								aa2.getAtividade().getGrupoAtividade().getNome() +
								aa2.getAtividade().getNome());
			}
		});

		return listaAlocacoes;
	}

	public String limpaRegistroAtividadeAction() {
		sessionBean.setProjeto(null);
		return NavigationResults.SUCCESS;
	}

	
	public List<RegistroAtividade> getListaRegistrosPorData() {
		List<RegistroAtividade> listaRegistrosDia = new ArrayList<RegistroAtividade>();
		List<RegistroAtividade> listaRegistrosMes;
		float qtdeHorasMes = 0, qtdeHorasDia = 0;
		
		Colaborador colaborador = sessionBean.getColaborador();
		Calendar dataTrabalho = sessionBean.getDataTrabalho();
		
		agenda = new CalendarDataModelImpl();
		agenda.setCurrentDate(dataTrabalho.getTime());
		
		listaRegistrosMes = colaboradorService.getListaRegistrosPorPeriodo(colaborador,
				DataUtil.getDataInicioContabil(dataTrabalho, sessionBean.getParametros().getPrimeiroDiaContabil()),
				DataUtil.getDataFimContabil(dataTrabalho, sessionBean.getParametros().getUltimoDiaContabil(dataTrabalho)));
		
		for (RegistroAtividade registro : listaRegistrosMes) {
			qtdeHorasMes += registro.getQtdeHorasTrabalho();
			
			if (registro.getDataTrabalho().equals(dataTrabalho)) {
				qtdeHorasDia += registro.getQtdeHorasTrabalho();
				listaRegistrosDia.add(registro);
			}
			
			CalendarDataModelItemImpl diaRegistros = new CalendarDataModelItemImpl();
			diaRegistros.setDay(registro.getDataTrabalho().get(Calendar.DAY_OF_MONTH));
	        diaRegistros.setDescricao("");
	        diaRegistros.setStyleClass("fundoEscuro");
			
			agenda.addItem(diaRegistros);
		}
		
		sessionBean.setQtdeHorasDia(qtdeHorasDia);
		sessionBean.setQtdeHorasMes(qtdeHorasMes);
		
		Collections.sort(listaRegistrosDia);
		return listaRegistrosDia;
	}

	public String cadastrarRegistroAtividadeAction() {
		DateFormat formatoHora = new SimpleDateFormat("HH:mm");
		Calendar horaInicial = Calendar.getInstance();
		Calendar horaFinal = Calendar.getInstance();
		String descricao;

		if(sessionBean.getColaboradorLogado().getTipoUsuario() != Tipos.TIPO_USUARIO_ADMIN
			&& sessionBean.getDataTrabalho().after(Calendar.getInstance())) {
			String msg = FacesUtils.getMessage("registrarAtividade_erroDiasFuturos");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		// Se a atividade requer a associacao de uma ocorrencia ao registrar
		// e o usuario nao selecionou nenhuma ocorrencia do projeto
		// emite uma mensagem de erro no componente e retorna falha
		if(sessionBean.getAlocacao() != null &&
			sessionBean.getAlocacao().getAtividade().getTipoAssocOcorrencia() == Tipos.TIPO_ASSOC_OBRIGATORIA &&
			sessionBean.getOcorrencia() == null) {
			String msg = FacesUtils.getMessage("registrarAtividade_erroOcorrenciaObrigatoria");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		try {
			horaInicial.setTime(formatoHora.parse(this.horaInicial));
			horaFinal.setTime(formatoHora.parse(this.horaFinal));
			descricao = FacesUtils.getRequestProperty("formGeral:descricao");
			
		} catch (ParseException pe) {
			String msg = FacesUtils.getMessage("erro_erroInterno");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		if (horaInicial.getTime().after(horaFinal.getTime()) ||
			horaInicial.getTime().equals(horaFinal.getTime())) {
			this.horaFinal = null;
			String msg = FacesUtils.getMessage("registrarAtividade_horaFinalInvalida");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		if (!DataUtil.validaIntervaloHoraRegistro(horaInicial, horaFinal,
				colaboradorService.getListaRegistrosPorData(sessionBean.getColaborador(), sessionBean.getDataTrabalho()))) {
			this.horaFinal = null;
			String msg = FacesUtils.getMessage("registrarAtividade_erroIntervaloHoras");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
			
		}

		float qtdeHoras = (float) (horaFinal.getTimeInMillis() / 1000 - horaInicial.getTimeInMillis() / 1000) / 3600;
		
		AlocacaoAtividade alocacao = sessionBean.getAlocacao();
		colaboradorService.initializeObject(alocacao);

		if (!alocacao.getAtividade().getGrupoAtividade().getProjeto().isGenerico()) {
			String concluido = FacesUtils.getRequestProperty("formGeral:flagConcluido");
			
			if (qtdeHoras > alocacao.getQtdeHorasRestantes()) {
				String msg = FacesUtils.getMessage("registrarAtividade_erroHorasUltrapassam");
				FacesUtils.addErrorMessage(msg);
				return NavigationResults.FAILURE;
			}	
			
			if (concluido != null)
				alocacao.setQtdeHorasRestantes(0);
			else
				alocacao.setQtdeHorasRestantes(alocacao.getQtdeHorasRestantes() - qtdeHoras);
			
			if(alocacao.getQtdeHorasRestantes() == 0){
				/* Se a qtde de horas restantes chegar ao fim, ou o checkbox de concluido tiver sido clicado,
				 * seta a flag de registro bloqueado para 2
				 * */ 
				alocacao.setFlagBloqueadoRegistro(2);
			}
		}

		RegistroAtividade registroAtividade = new RegistroAtividade();
		registroAtividade.setAlocacao(alocacao);
		registroAtividade.setDataTrabalho(sessionBean.getDataTrabalho());
		registroAtividade.setQtdeHorasTrabalho(qtdeHoras);
		registroAtividade.setHoraInicial(horaInicial);
		registroAtividade.setHoraFinal(horaFinal);
		registroAtividade.setDescricao(descricao);
		registroAtividade.setCustoHoraLancamento(alocacao.getColaborador().getValorCustoHora());
		registroAtividade.setOcorrencia(sessionBean.getOcorrencia());

		// Salva a quantidade de horas restantes na alocacao
		alocacao.adicionarRegistro(registroAtividade);
		colaboradorService.cadastrarAlocacaoAtividade(alocacao);
		
		sessionBean.setRegistroAtividade(null);
		
		
		this.horaInicial = this.horaFinal.substring(0, 5);
		this.horaFinal = null;
		return NavigationResults.SUCCESS;
	}
	
	public String atualizarRegistroAtividadeAction() {
		DateFormat formatoHora = new SimpleDateFormat("HH:mm");
		Calendar horaInicial = Calendar.getInstance();
		Calendar horaFinal = Calendar.getInstance();
		RegistroAtividade registroAtividade = sessionBean.getRegistroAtividade();
		AlocacaoAtividade alocacaoAnterior = registroAtividade.getAlocacao();
		AlocacaoAtividade alocacaoAtual = sessionBean.getAlocacao();
		List<RegistroAtividade> listaRegistrosDia = colaboradorService.getListaRegistrosPorData(sessionBean.getColaborador(), sessionBean.getDataTrabalho());
		String concluido = FacesUtils.getRequestProperty("formGeral:flagConcluido");
		
		colaboradorService.initializeObject(alocacaoAnterior);
		colaboradorService.initializeObject(alocacaoAtual);
		
		// Se a atividade requer a associacao de uma ocorrencia ao registrar
		// e o usuario nao selecionou nenhuma ocorrencia do projeto
		// emite uma mensagem de erro no componente e retorna falha
		if(alocacaoAtual != null &&
			alocacaoAtual.getAtividade().getTipoAssocOcorrencia() == Tipos.TIPO_ASSOC_OBRIGATORIA &&
			sessionBean.getOcorrencia() == null) {
			String msg = FacesUtils.getMessage("registrarAtividade_erroOcorrenciaObrigatoria");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		try {
			horaInicial.setTime(formatoHora.parse(this.horaInicial));
			horaFinal.setTime(formatoHora.parse(this.horaFinal));
			
		} catch (ParseException pe) {
			String msg = FacesUtils.getMessage("erro_erroInterno");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		if (horaInicial.getTime().after(horaFinal.getTime()) ||
			horaInicial.getTime().equals(horaFinal.getTime())) {
			String msg = FacesUtils.getMessage("registrarAtividade_horaFinalInvalida");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		listaRegistrosDia.remove(registroAtividade);
		
		if (!DataUtil.validaIntervaloHoraRegistro(horaInicial, horaFinal, listaRegistrosDia)) {
			String msg = FacesUtils.getMessage("registrarAtividade_erroIntervaloHoras");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}

		// Volta as horas anteriores para as horas restantes
		float qtdeHoras = (float) (horaFinal.getTimeInMillis() / 1000 - horaInicial.getTimeInMillis() / 1000) / 3600;
		float qtdeHorasAntes = (float) (registroAtividade.getHoraFinal().getTimeInMillis() / 1000 - registroAtividade.getHoraInicial().getTimeInMillis() / 1000) / 3600;

		if (!alocacaoAtual.getAtividade().getGrupoAtividade().getProjeto().isGenerico()) {
			
			if ((alocacaoAtual.equals(alocacaoAnterior) && (qtdeHoras > alocacaoAtual.getQtdeHorasRestantes() + qtdeHorasAntes)) ||
				(!alocacaoAtual.equals(alocacaoAnterior) && (qtdeHoras > alocacaoAtual.getQtdeHorasRestantes()))) {
				String msg = FacesUtils.getMessage("registrarAtividade_erroHorasUltrapassam");
				FacesUtils.addErrorMessage(msg);
				return NavigationResults.FAILURE;
			}	

			if (concluido != null)
				alocacaoAtual.setQtdeHorasRestantes(0);
			else
				alocacaoAtual.setQtdeHorasRestantes(alocacaoAtual.getQtdeHorasRestantes() - qtdeHoras);
			
			if(alocacaoAtual.getQtdeHorasRestantes() == 0) {
				/* Se a qtde de horas restantes chegar ao fim, ou o checkbox de concluido tiver sido clicado,
				 * seta a flag de registro bloqueado para 2
				 * */ 
				alocacaoAtual.setFlagBloqueadoRegistro(2);
			}
		}

		// Se nao eh generico, atualiza as horas restantes
		if (!alocacaoAnterior.getAtividade().getGrupoAtividade().getProjeto().isGenerico()) {
			if (!(alocacaoAnterior == alocacaoAtual && concluido != null)){
				alocacaoAnterior.setQtdeHorasRestantes(alocacaoAnterior.getQtdeHorasRestantes() + qtdeHorasAntes);
			}
			
			alocacaoAnterior.setFlagBloqueadoRegistro(0);
		}
		
		registroAtividade.setAlocacao(alocacaoAtual);
		registroAtividade.setQtdeHorasTrabalho(qtdeHoras);
		registroAtividade.setHoraInicial(horaInicial);
		registroAtividade.setHoraFinal(horaFinal);
		registroAtividade.setOcorrencia(sessionBean.getOcorrencia());

		// Atualiza a quantidade de horas restantes na alocacao
		alocacaoAnterior.removerRegistro(registroAtividade);
		alocacaoAtual.adicionarRegistro(registroAtividade);
		
		colaboradorService.cadastrarAlocacaoAtividade(alocacaoAnterior);
		colaboradorService.cadastrarAlocacaoAtividade(alocacaoAtual);
		
		sessionBean.setRegistroAtividade(null);
		this.horaInicial = this.horaFinal;
		this.horaFinal = null;
		return NavigationResults.SUCCESS;
	}

	public void selecionarRegistroAtividadeAction(ActionEvent event) {
		DateFormat formatoHora = new SimpleDateFormat("HH:mm");
		DataTable tabelaRegistros = (DataTable) event.getComponent().getParent().getParent().getParent();
		RegistroAtividade registro = (RegistroAtividade) tabelaRegistros.getRowData();
		
		sessionBean.setProjeto(registro.getAlocacao().getAtividade().getGrupoAtividade().getProjeto());
		sessionBean.setAlocacao(registro.getAlocacao());
		sessionBean.setOcorrencia(registro.getOcorrencia());
		sessionBean.setRegistroAtividade(registro);	
	
		int diferencaHoras = (registro.getHoraFinal().get(Calendar.HOUR_OF_DAY)-registro.getHoraInicial().get(Calendar.HOUR_OF_DAY));
		
		// representa a diferença entre os minutos, sendo esta representada em
		// horas.
		float diferencaMinutos = (registro.getHoraFinal().get(Calendar.MINUTE)-registro.getHoraInicial().get(Calendar.MINUTE));
		
		//A diferença é dividida por 60, pois uma hora possui 60 minutos.
		// Assim, um diferença de 30 minutos equivale a 30/60 (0,5 hr)
		diferencaMinutos /=60;
		
		String data = String.valueOf(diferencaHoras + diferencaMinutos);	
		
		this.horaInicial = formatoHora.format(registro.getHoraInicial().getTime());
		this.horaFinal = formatoHora.format(registro.getHoraFinal().getTime());
		this.horaFinal+=" ("+ data + " h)";
	}
	
	public String excluirRegistroAtividadeAction(ActionEvent event) {
		try {
			selecionarRegistroAtividadeAction(event);
			RegistroAtividade registroAtividade = sessionBean.getRegistroAtividade();
			AlocacaoAtividade alocacao = registroAtividade.getAlocacao();

			// Retorna a quantidade de horas de trabalho nas horas restantes da alocacao
			if (!alocacao.getAtividade().getGrupoAtividade().getProjeto().isGenerico()) {
				alocacao.setQtdeHorasRestantes(alocacao.getQtdeHorasRestantes() + registroAtividade.getQtdeHorasTrabalho());
			}
		
			/*
			 * Apenas reseta a flag se ela tiver ativada via registro de atividades,
			 * se ela tiver sido ativada pelo coordenador, continua ativa. 
			 * */ 
			if(alocacao.getFlagBloqueadoRegistro() == 2){
				alocacao.setFlagBloqueadoRegistro(0);
			}
		
			alocacao.removerRegistro(registroAtividade);

			// Exclui o registro de atividade e salva a qtde de horas restantes na alocacao
			colaboradorService.cadastrarAlocacaoAtividade(alocacao);
			sessionBean.setRegistroAtividade(null);
			sessionBean.setAlocacao(null);
			sessionBean.setProjeto(null);
			setHoraInicial(horaFinal);
			return NavigationResults.SUCCESS;
		} catch(Exception e) {
			String msg = FacesUtils.getMessage("registrarAtividade_erroGenericoExclusao");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
	}

	public String agendaColaboradorAction() {
		UtilBean utilBean = (UtilBean)FacesUtils.getManagedBean("utilBean");
		Colaborador colaborador = sessionBean.getColaborador();
		
		Calendar dataSelecionada = new GregorianCalendar(utilBean.getAnoSelecionado(),utilBean.getMesSelecionado()-1,1);

		agenda = new CalendarDataModelImpl();
		agenda.setCurrentDate(dataSelecionada.getTime());
		agenda.setTitulo(colaborador.getNomeCompleto() + " - " + utilBean.getMesSelecionado() + "/" + utilBean.getAnoSelecionado());
		
		// Adiciona os feriados na agenda
		for(Feriados feriado: colaboradorService.getListaFeriadosDoAno(utilBean.getAnoSelecionado())) {
			if ((feriado.getDataFeriado().get(Calendar.MONTH)+1) == utilBean.getMesSelecionado()) {
				CalendarDataModelItemImpl diaFeriado = new CalendarDataModelItemImpl();
				diaFeriado.setDay(feriado.getDataFeriado().get(Calendar.DAY_OF_MONTH));
		        diaFeriado.setDescricao("<b>Feriado: </b>" + feriado.getNome());
		        diaFeriado.setStyleClass("fundoEscuro");
				
				agenda.addItem(diaFeriado);
			}
		}
		

		// Adiciona as férias na agenda
		for(Ferias ferias: colaborador.getFerias())
		{
			if ((ferias.getDataInicioFerias().get(Calendar.YEAR) <= utilBean.getAnoSelecionado()) && 
				(ferias.getDataInicioFerias().get(Calendar.MONTH)+1 <= utilBean.getMesSelecionado()) &&
				(ferias.getDataFimFerias().get(Calendar.YEAR) >= utilBean.getAnoSelecionado()) &&
				(ferias.getDataFimFerias().get(Calendar.MONTH)+1 >= utilBean.getMesSelecionado()))
			{
				int inicio = (ferias.getDataInicioFerias().get(Calendar.MONTH)+1 < utilBean.getMesSelecionado()) ? 1 : ferias.getDataInicioFerias().get(Calendar.DAY_OF_MONTH); 
				int termino= (ferias.getDataFimFerias().get(Calendar.MONTH)+1 > utilBean.getMesSelecionado()) ? ferias.getDataFimFerias().getMaximum(Calendar.DAY_OF_MONTH) : ferias.getDataFimFerias().get(Calendar.DAY_OF_MONTH);
					
				while (inicio<=termino)
				{
					CalendarDataModelItemImpl diaFerias = new CalendarDataModelItemImpl();
					diaFerias.setDay(inicio);
					diaFerias.setDescricao("<b>Férias</b>");
					diaFerias.setStyleClass("fundoEscuro");
			        							
					inicio++;
						
					agenda.addItem(diaFerias);	
				}
			}
		}
		
		// Adiciona as atividades e os compromissos dos projetos alocados na agenda
		List<Projeto> listaProjetos = colaboradorService.getListaTodosProjetosAlocados(colaborador);
		
		for (Projeto projeto : listaProjetos) {
			if (!projeto.isGenerico()) {
				for (GrupoAtividade grupoAtividade : projeto.getGruposAtividades()) {
					for (Atividade atividadeAux : grupoAtividade.getAtividades()) {
						if (atividadeAux.isFlagContabilizarProgresso()) {
							for (AlocacaoAtividade alocacaoAux : atividadeAux.getAlocacoes()) {
								if ((alocacaoAux.getDataInicioPrevista().get(Calendar.YEAR) <= utilBean.getAnoSelecionado()) &&
									(alocacaoAux.getDataInicioPrevista().get(Calendar.MONTH)+1 <= utilBean.getMesSelecionado()) &&
									(alocacaoAux.getDataFimPrevista().get(Calendar.YEAR) >= utilBean.getAnoSelecionado()) &&
									(alocacaoAux.getDataFimPrevista().get(Calendar.MONTH)+1 >= utilBean.getMesSelecionado()))
								{
									Calendar dataInicioAlocacao, dataFimAlocacao;
									
									if (alocacaoAux.getDataInicioPrevista().get(Calendar.MONTH)+1 == utilBean.getMesSelecionado())
										dataInicioAlocacao = (Calendar) alocacaoAux.getDataInicioPrevista().clone();
									else
										dataInicioAlocacao = new GregorianCalendar(utilBean.getAnoSelecionado(), utilBean.getMesSelecionado()-1, 1); 
									
									if (alocacaoAux.getDataFimPrevista().get(Calendar.MONTH)+1 == utilBean.getMesSelecionado())
										dataFimAlocacao = (Calendar) alocacaoAux.getDataFimPrevista().clone();
									else
										dataFimAlocacao = new GregorianCalendar(utilBean.getAnoSelecionado(), utilBean.getMesSelecionado()-1, dataInicioAlocacao.getMaximum(Calendar.DAY_OF_MONTH));
									
									do {
										if (DataUtil.colaboradorTrabalhaNoDia(colaborador, dataInicioAlocacao, false)) {
											
											CalendarDataModelItemImpl diaAlocacao = new CalendarDataModelItemImpl();
											diaAlocacao.setDay(dataInicioAlocacao.get(Calendar.DAY_OF_MONTH));
									        diaAlocacao.setDescricao("<b>" + projeto.getCodigoClienteCompleto()+ ": </b>" + atividadeAux.getNome());
									        
											agenda.addItem(diaAlocacao);
										}
										
										dataInicioAlocacao.add(Calendar.DAY_OF_YEAR, 1);
										
									} while (!dataInicioAlocacao.after(dataFimAlocacao));
								}
							}
						}
					}
				}
				
				for (Compromisso compromisso : projeto.getCompromissos()) {
					if (compromisso.getColaboradores().contains(colaborador) &&
						(compromisso.getDataEvento().get(Calendar.MONTH)+1) == utilBean.getMesSelecionado() &&
						compromisso.getDataEvento().get(Calendar.YEAR) == utilBean.getAnoSelecionado()) {
						CalendarDataModelItemImpl compromissoAlocado = new CalendarDataModelItemImpl();
						compromissoAlocado.setDay(compromisso.getDataEvento().get(Calendar.DAY_OF_MONTH));
				        compromissoAlocado.setDescricao("<b>Compromisso (" + projeto.getCodigoClienteCompleto() + "): </b>" + compromisso.getNome());
				        compromissoAlocado.setStyleClass("fundoDestaque");
				        
						agenda.addItem(compromissoAlocado);
					}
				}
			}
		}

		return(NavigationResults.SUCCESS);
	}
	
	public String muralRecadosAction() {
		String descricao, assunto;
		Colaborador colaboradorAutor = sessionBean.getColaboradorLogado();
		Calendar dataRecadoCalendar = Calendar.getInstance();
		long flagAssuntoTrabalho;
		
		try {
			assunto = FacesUtils.getRequestProperty("formGeral:assunto");
			descricao = FacesUtils.getRequestProperty("formGeral:recado");
			flagAssuntoTrabalho = Integer.parseInt(FacesUtils.getRequestProperty("formGeral:flagAssuntoTrabalho"));

		} catch (Exception pe) {
			String msg = FacesUtils.getMessage("erro_erroInterno");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}

		MuralRecados muralRecados = new MuralRecados();
		muralRecados.setDescricao(descricao);
		muralRecados.setAssunto(assunto);
		muralRecados.setDataRegistro(dataRecadoCalendar);
		
		if(flagAssuntoTrabalho == 0)
			muralRecados.setFlagAssuntoTrabalho(true);
		else
			muralRecados.setFlagAssuntoTrabalho(false);
		
		muralRecados.setColaboradorAutor(colaboradorAutor);
		muralRecados.setColaboradorDest(this.colaborador);
		
		colaboradorService.cadastrarMuralRecados(muralRecados);
		
		String msg = FacesUtils.getMessage("principal_mensagem_enviada_sucesso");
		FacesUtils.addInfoMessage(msg);
		
		return NavigationResults.SUCCESS;
	}
	
	public String muralRecadosDeleteAction() {
		long recado_id = Long.parseLong(FacesUtils.getRequestProperty("recadoSelecionado"));
		colaboradorService.excluirMuralRecados(colaboradorService.getMuralPK(recado_id));
			
		return NavigationResults.SUCCESS;
	}

	public List<Compromisso> getListaCompromissosRecentes() {
		List<Compromisso> listaCompromissoBD = colaboradorService.getListaCompromissosRecentes(sessionBean.getColaboradorLogado());
		List<Compromisso> listaCompromissos = new ArrayList<Compromisso>();
		Calendar dataEvento;
		Calendar dataAtual = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
													Calendar.getInstance().get(Calendar.MONTH),
													Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		Calendar dataMaxima = Calendar.getInstance();
		dataMaxima.add(Calendar.DAY_OF_MONTH, +7);
		
		for (Compromisso compromisso : listaCompromissoBD) {
			
			if (compromisso.getTipoRepeticao() == Tipos.TIPO_REPETICAO_UNICA ||
				compromisso.getTipoRepeticao() == Tipos.TIPO_REPETICAO_SEMANAL) {
				listaCompromissos.add(compromisso);
			} else {
				
				dataEvento = (Calendar) compromisso.getDataEvento().clone();
				dataEvento.set(Calendar.YEAR, dataAtual.get(Calendar.YEAR));
				
				if (compromisso.getTipoRepeticao() == Tipos.TIPO_REPETICAO_MENSAL) {
					dataEvento.set(Calendar.MONTH, dataAtual.get(Calendar.MONTH));
				} 
				
				if (dataEvento.equals(dataAtual) ||
						(dataEvento.after(dataAtual) && dataEvento.before(dataMaxima))) { 
					listaCompromissos.add(compromisso);
				}
			}
		}
		return listaCompromissos;
	}
	
	public List<Projeto> getListaMeusProjetos() {
		try {
			List<Projeto> listProj = colaboradorService.getListaMeusProjetos(sessionBean.getColaboradorLogado());
			return listProj;
		} catch (Exception e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getApplication().getNavigationHandler().handleNavigation(fc,	"#{colaboradorBean.errorAction}", "toErrorPage");
			fc.responseComplete();
			return null;
		}		
	}
	
	public String acessarStatusProjetoAction() {
		long id_projeto = Long.parseLong(FacesUtils.getRequestProperty("idProjeto"));
		Projeto projeto = colaboradorService.getProjetoPK(id_projeto);
		
		sessionBean.setCliente(projeto.getProduto().getArea().getCliente());
		sessionBean.setArea(projeto.getProduto().getArea());
		sessionBean.setProduto(projeto.getProduto());
		sessionBean.setProjeto(projeto);
				
		return NavigationResults.SUCCESS_STATUS_PROJETO;
	}
	
	public String acessarStatusOcorrenciaAction() {
		int idOcorrencia = Integer.parseInt(FacesUtils.getRequestProperty("idOcorrencia"));
		Ocorrencia ocorrencia = colaboradorService.getOcorrenciaPK(idOcorrencia);
		
		sessionBean.setCliente(ocorrencia.getProjeto().getProduto().getArea().getCliente());
		sessionBean.setArea(ocorrencia.getProjeto().getProduto().getArea());
		sessionBean.setProduto(ocorrencia.getProjeto().getProduto());
		sessionBean.setProjeto(ocorrencia.getProjeto());
		sessionBean.setOcorrencia(ocorrencia);
		
		return NavigationResults.SUCCESS_STATUS_OCORRENCIA;
	}

	
	public String atualizarRegistroColaborador() {
		Colaborador colaborador = sessionBean.getColaboradorLogado();

		//atualiza o objeto da foto com a foto temporaria
		colaborador.setFoto(colaborador.getFotoTemp());
		
		colaboradorService.atualizarColaborador(colaborador);
		
		String msg = FacesUtils.getMessage("cadastro_atualizado_sucesso");
		FacesUtils.addInfoMessage(msg);
		return NavigationResults.SUCCESS;
	}
	
	public String alterarFlagCompromisso() {
		Compromisso compromisso;
		Projeto projeto;
		long id_compromisso;
		
		id_compromisso = Long.parseLong(FacesUtils.getRequestProperty("idCompromisso"));
		compromisso = colaboradorService.getCompromissoPK(id_compromisso);
		
		if(compromisso.isFlagConcluido()){
			compromisso.setFlagConcluido(false);
			compromisso.setDataConclusao(null);
		} else {
			compromisso.setFlagConcluido(true);
			compromisso.setDataConclusao(Calendar.getInstance());
		}
		colaboradorService.atualizarCompromisso(compromisso);
		
		projeto = sessionBean.getProjeto();
		
		if (projeto != null) {
			projeto = colaboradorService.getProjetoPK(projeto.getId());
			sessionBean.setProjeto(projeto);//reseta o projeto para que a sessao contenha o objeto ja com a flag alterada
		}
		
		return NavigationResults.SUCCESS;
	}
	
	public String acessarOcorrenciaProjetoAction() {
		long id_projeto = Long.parseLong(FacesUtils.getRequestProperty("idProjeto"));
		Projeto projeto = colaboradorService.getProjetoPK(id_projeto);
		
		sessionBean.setChavePesquisaOcorrencia(null);
		sessionBean.setCliente(projeto.getProduto().getArea().getCliente());
		sessionBean.setArea(projeto.getProduto().getArea());
		sessionBean.setProduto(projeto.getProduto());
		sessionBean.setProjeto(projeto);
				
		return NavigationResults.SUCCESS_OCORRENCIA_PROJETO;
	}
	
	public String cadastrarNovaOcorrenciaAction() {
		Ocorrencia ocorrencia = new Ocorrencia();
		ocorrencia.setProjeto(sessionBean.getProjeto());
		sessionBean.setOcorrencia(ocorrencia);
		return NavigationResults.NEW;
	}
	
	public void cadastrarOcorrenciaAction(ActionEvent event) {
		Ocorrencia ocorrencia = new Ocorrencia();
		Calendar dataAtual = Calendar.getInstance();
		StringBuilder stringBuffer = new StringBuilder();
		String assinaturaEmail;
		assinaturaEmail = "\n\nEste é um email automático, não há necessidade de respondê-lo.\nAtenciosamente,\nAutoManager/FacTI_NET";
		EmailUtil emailUtil = new EmailUtil("mail.facti-rp.com.br", true, "automanager@facti-rp.com.br", "manager2009", assinaturaEmail);
		
		try {
			
			if (sessionBean.getOcorrencia().getId() != null) {
				//salvando uma ocorrencia ja existente
				ocorrencia = colaboradorService.getOcorrenciaPK(sessionBean.getOcorrencia().getId());
				stringBuffer.append(ocorrencia.getHistoricoOcorrencia());
				
				//Caso o Responsavel tenha sido alterado, salva a mudanca no historico e manda email ao novo responsavel
				if (ocorrencia.getResponsavel().getId().compareTo(sessionBean.getOcorrencia().getResponsavel().getId()) != 0){
					stringBuffer.append("<br/>(" + DataUtil.getDataFormatada(dataAtual) + ")");
					stringBuffer.append("-->Responsável alterado para: " + sessionBean.getOcorrencia().getResponsavel().getNomeCompleto());
					sessionBean.getOcorrencia().setHistoricoOcorrencia(stringBuffer.toString());
					emailUtil.sendEmail(sessionBean.getOcorrencia().getResponsavel().getEmail(), "[FACTI_NET] Lembrete de Ocorrência ao Responsável", sessionBean.getOcorrencia().getResponsavel().getNome()+ ", você agora é responsável pela Ocorrência [" + sessionBean.getOcorrencia().getNumOcorrencia()+ "], [" + sessionBean.getOcorrencia().getTitulo() + "].");
				}
				
				//Caso o Status tenha sido alterado, salva a mudanca no historico
				if (ocorrencia.getStatusOcorrencia() != sessionBean.getOcorrencia().getStatusOcorrencia()){
					stringBuffer.append("<br/>(" + DataUtil.getDataFormatada(dataAtual) + ")");
					stringBuffer.append("-->Status alterado para: " + Tipos.getTipoStatusOcorrencia(sessionBean.getOcorrencia().getStatusOcorrencia()));
					sessionBean.getOcorrencia().setHistoricoOcorrencia(stringBuffer.toString());
					
					/*
					 * Se o status da ocorrencia mudar para Analisada, 
					 * envia email para o Responsavel interno do projeto, se existir, caso contrario
					 * envia ao Coordenador do Produto.
					 * */
					if(sessionBean.getOcorrencia().getStatusOcorrencia() == Tipos.TIPO_STATUS_OCORRENCIA_ANALISADA){
						if (sessionBean.getOcorrencia().getProjeto().getResponsavel() != null){
							//se existe responsavel interno, envia a ele 
							emailUtil.sendEmail(sessionBean.getOcorrencia().getProjeto().getResponsavel().getEmail(), "[FACTI_NET] Lembrete de Término de Análise ao Responsável Interno", "O status da ocorrência ["+sessionBean.getOcorrencia().getNumOcorrencia() + " - " + sessionBean.getOcorrencia().getNumBugCliente()+"] foi alterado para \"Analisada\".");	
						}
						else{
							//se nao existe, envia ao coordenador
							emailUtil.sendEmail(sessionBean.getOcorrencia().getProjeto().getProduto().getCoordenador().getEmail(), "[FACTI_NET] Lembrete de Término de Análise ao Coordenador", "O status da ocorrência ["+sessionBean.getOcorrencia().getNumOcorrencia() + " - " + sessionBean.getOcorrencia().getNumBugCliente()+"] foi alterado para \"Analisada\".");
						}
						
					}
				}
			}
			else {
				//criacao de uma nova ocorrencia
				ocorrencia = sessionBean.getOcorrencia();
				
				stringBuffer.append("<br/>(" + DataUtil.getDataFormatada(dataAtual) + ")");
				stringBuffer.append("-->Número Ocorrência: " + ocorrencia.getNumOcorrencia().toString());
				stringBuffer.append("<br/>(" + DataUtil.getDataFormatada(dataAtual) + ")");
				stringBuffer.append("-->Data de abertura: " + DataUtil.getDataFormatada(ocorrencia.getDataAbertura()));
				stringBuffer.append("<br/>(" + DataUtil.getDataFormatada(dataAtual) + ")");
				stringBuffer.append("-->Responsável: " + ocorrencia.getResponsavel().getNomeCompleto());
				stringBuffer.append("<br/>(" + DataUtil.getDataFormatada(dataAtual) + ")");
				stringBuffer.append("-->Status: " + Tipos.getTipoStatusOcorrencia(ocorrencia.getStatusOcorrencia()));
				sessionBean.getOcorrencia().setHistoricoOcorrencia(stringBuffer.toString());
				emailUtil.sendEmail(sessionBean.getOcorrencia().getResponsavel().getEmail(), "[FACTI_NET] Lembrete de Ocorrência ao Responsável", sessionBean.getOcorrencia().getResponsavel().getNome()+ ", você agora é responsável pela Ocorrência [" + sessionBean.getOcorrencia().getNumOcorrencia()+ "], [" + sessionBean.getOcorrencia().getTitulo() + "].");
			}
			colaboradorService.cadastrarOcorrencia(sessionBean.getOcorrencia());

		} catch (DataIntegrityViolationException sqle) {
			/*
			 * Este catch existe porem nao ha ainda consistencia de duplicidade do numero da ocorrencia
			 * na base de dados. Portanto nao ha ainda como cair aqui.
			 */
			String msg = FacesUtils.getMessage("cadastrarOcorrencia_ocorrenciaJaCadastrada");
			FacesUtils.addErrorMessage(msg);
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getApplication().getNavigationHandler().handleNavigation(fc,	"#{colaboradorBean.errorAction}", "failure");
			fc.responseComplete();
		}
	}
	
	public String selecionarOcorrenciaAction() {
		Projeto auxProj;
		int idOcorrencia = Integer.parseInt(FacesUtils.getRequestProperty("idOcorrencia"));
		sessionBean.setOcorrencia(colaboradorService.getOcorrenciaPK(idOcorrencia));
		// guarda o objeto do Projeto pois o setProduto seta o Projeto da sessao com null
		auxProj = sessionBean.getProjeto();
		sessionBean.setProduto(sessionBean.getOcorrencia().getProjeto().getProduto());
		// recupera o projeto da sessao, e se este for null vai continuar funcionando 
		sessionBean.setProjeto(auxProj);
		
		return NavigationResults.EDIT;
	}
	
	public String excluirOcorrenciaAction(ActionEvent event) {
		selecionarOcorrenciaAction();
		Ocorrencia ocorrencia = sessionBean.getOcorrencia();
		try{
			colaboradorService.excluirOcorrencia(ocorrencia);
		} catch (Exception e) {
			String msg = FacesUtils.getMessage("cadastrarOcorrencia_ocorrenciaJaRegistrada");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		return NavigationResults.SUCCESS;
	}
	
	public List<Ocorrencia> getListaOcorrenciasDoProjeto() {
		return colaboradorService.getListaOcorrenciasAtivas(sessionBean.getProjeto());
	}
	
	public List<Ocorrencia> getListaMinhasOcorrencias() {
		return colaboradorService.getListaMinhasOcorrencias(sessionBean.getColaboradorLogado());
	}
	
	public String errorAction() {
		return NavigationResults.FAILURE;
	}
	
	public void listenerFotoTemp(FileUploadEvent event) {
		UploadedFile item = event.getFile();
        sessionBean.getColaboradorLogado().setFotoTemp(item.getContents());
    }
	
	public long getTimeStamp() {  
        return System.currentTimeMillis();  
    }
	
	public String emprestarItemInventarioAction() {
		ProdutoInventario produto = sessionBean.getProdutoInventario(); 
		for (ItemInventario item : produto.getListaItens()) {
			if (item.equals(sessionBean.getItemInventario())){
				if (item.getResponsavel() != null){
					if (item.getResponsavel().equals(sessionBean.getColaboradorLogado())){
						//caso seja uma devolucao
						item.setDataEmprestimo(null);
						item.setResponsavel(null);
					}
				} else {
					//caso seja um emprestimo
					item.setDataEmprestimo(Calendar.getInstance());
					item.setResponsavel(sessionBean.getColaboradorLogado());	
				}
			} 
		}
		colaboradorService.atualizarProdutoInventario(sessionBean.getProdutoInventario());
		
		return NavigationResults.SUCCESS;
	}

	public String cadastrarFraseDiaAction() {
		String frase, autor;
		
		try {
			frase = FacesUtils.getRequestProperty("formGeral:frase");
			autor = FacesUtils.getRequestProperty("formGeral:autor");

		} catch (Exception pe) {
			String msg = FacesUtils.getMessage("erro_erroInterno");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}

		FraseDia fraseDia = new FraseDia();
		fraseDia.setFrase(frase);
		fraseDia.setAutor(autor);
		fraseDia.setColaboradorEnvio(sessionBean.getColaboradorLogado());
		fraseDia.setDataRegistro(Calendar.getInstance());
		
		colaboradorService.cadastrarFraseDia(fraseDia);
				
		return NavigationResults.SUCCESS;
	}
	
	public void excluirFraseDiaAction(ActionEvent event) {
		long idFraseDia = Long.parseLong(FacesUtils.getRequestProperty("idFraseDia"));
		colaboradorService.excluirFraseDia(colaboradorService.getFraseDiaPK(idFraseDia));
	}
	
	public List<FraseDia> getListaFrasesDia() {
		return colaboradorService.getListaFrasesDia(sessionBean.getColaboradorLogado());
	}

}
