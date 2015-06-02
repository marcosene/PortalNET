/**
 * @since 08/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import br.com.portalnet.control.ColaboradorServiceController;
import br.com.portalnet.model.AlocacaoAtividade;
import br.com.portalnet.model.Area;
import br.com.portalnet.model.Atividade;
import br.com.portalnet.model.Cliente;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Compromisso;
import br.com.portalnet.model.CustoRealProjeto;
import br.com.portalnet.model.Equipe;
import br.com.portalnet.model.Feriados;
import br.com.portalnet.model.Ferias;
import br.com.portalnet.model.FraseDia;
import br.com.portalnet.model.GrupoAtividade;
import br.com.portalnet.model.ItemInventario;
import br.com.portalnet.model.Modulo;
import br.com.portalnet.model.ObservacoesProjeto;
import br.com.portalnet.model.Ocorrencia;
import br.com.portalnet.model.Parametros;
import br.com.portalnet.model.Produto;
import br.com.portalnet.model.ProdutoInventario;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.RegistroAtividade;
import br.com.portalnet.model.TipoAtividade;
import br.com.portalnet.model.Tipos;
import br.com.portalnet.util.DataUtil;
import br.com.portalnet.util.FacesUtils;


@ManagedBean(name="sessionBean")
@SessionScoped
public class SessionBean {
	
	@ManagedProperty(value="#{colaboradorService}")
	private ColaboradorServiceController colaboradorService;
	
	private Colaborador colaborador;
	
	private Cliente cliente;
	
	private Area area;

	private Produto produto;
	
	private Modulo modulo;
	
	private Equipe equipe;
	
	private TipoAtividade tipoAtividade;
	
	private Feriados feriado;
	
	private Projeto projeto;
	
	private Atividade atividade;
	
	private GrupoAtividade grupoAtividade;
	
	private Compromisso compromisso;
	
	private ObservacoesProjeto observacao;
	
	private AlocacaoAtividade alocacao;
	
	private Ocorrencia ocorrencia;
	
	private RegistroAtividade registroAtividade;
	
	private Parametros parametros;
	
	private String nomeXML;
	
	private boolean loggedIn;

	private Colaborador colaboradorLogado;

	private Calendar dataTrabalho;
	
	private Calendar dataUltLogin;
	
	private float qtdeHorasDia;
	
	private float qtdeHorasMes;
	
	private boolean flagAlteracao;
	
	private	List<Feriados> listaFeriados;
	
	private String chavePesquisaOcorrencia; 

	private Ferias ferias;
	
	private CustoRealProjeto custoRealProjeto;
	
	private ProdutoInventario produtoInventario;
	
	private ItemInventario itemInventario;

	private FraseDia fraseDia;
	
	
	public void init() {
		colaborador = null;
		cliente = null;
		area = null;
		produto = null;
		modulo = null;
		equipe = null;
		tipoAtividade = null;
		projeto = null;
		atividade = null;
		compromisso = null;
		observacao = null;
		alocacao = null;
		ocorrencia = null;
		registroAtividade = null;
		fraseDia = null;
		nomeXML = null;
		chavePesquisaOcorrencia = null;
		dataTrabalho = Calendar.getInstance();
		colaboradorLogado.setFotoTemp(null);	
		produtoInventario = null;
	}
	
	public void initAction(ActionEvent event)
    {
        if (flagAlteracao) {
        	/*HtmlCommandJSCookMenu menu = (HtmlCommandJSCookMenu) event.getComponent();

        	Class<?>[] parms = new Class[]{ActionEvent.class};
			ExpressionFactory ef = FacesContext.getCurrentInstance().getApplication().getExpressionFactory();
			MethodExpression mb = ef.createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "failure", null, parms);
			menu.setActionExpression(mb);
			*/
			String msg = FacesUtils.getMessage("cadastro_erroSalvar");
			FacesUtils.addInfoMessage(msg);
			
			flagAlteracao = false;
        } else {
			init();
        }
    }
	
	public ColaboradorServiceController getColaboradorService() {
		return colaboradorService;
	}

	public void setColaboradorService(ColaboradorServiceController colaboradorService) {
		this.colaboradorService = colaboradorService;
	}

	public void setChavePesquisaOcorrencia(String chavePesquisaOcorrencia) {
		this.chavePesquisaOcorrencia = chavePesquisaOcorrencia;
	}
	
	public String getChavePesquisaOcorrencia() {
		return chavePesquisaOcorrencia;
	}

	public List<Feriados> getListaFeriados() {
		if (listaFeriados == null) {
			int ano = Calendar.getInstance().get(Calendar.YEAR);
			listaFeriados = colaboradorService.getListaFeriadosDoAno(ano);
		}
		return listaFeriados;
	}

	public void setListaFeriados(List<Feriados> listaFeriados) {
		this.listaFeriados = listaFeriados;
	}
		
	public Feriados getFeriado() {
		return feriado;
	}

	public void setFeriado(Feriados feriado) {
		this.feriado = feriado;
	}
			
	public void setFerias(Ferias ferias) {
		this.ferias = ferias;
	}

	public Ferias getFerias() {
		return ferias;
	}

	public Colaborador getColaborador() {
		if (colaborador == null) {
			colaborador = getColaboradorLogado();
		}
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = null;
		this.colaborador = colaborador;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = null;
		this.cliente = cliente;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.projeto = null;
		this.produto = null;
		this.area = null;
		this.area = area;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.projeto = null;
		this.produto = null;
		this.produto = produto;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = null;
		this.modulo = modulo;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = null;
		this.equipe = equipe;
	}
	
	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = null;
		this.tipoAtividade = tipoAtividade;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = null;
		this.projeto = projeto;
	}

	public GrupoAtividade getGrupoAtividade() {
		return grupoAtividade;
	}

	public void setGrupoAtividade(GrupoAtividade grupoAtividade) {
		this.grupoAtividade = grupoAtividade;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = null;
		this.atividade = atividade;
	}

	public Compromisso getCompromisso() {
		return compromisso;
	}

	public void setCompromisso(Compromisso compromisso) {
		this.compromisso = null;
		this.compromisso = compromisso;
	}

	public ObservacoesProjeto getObservacao() {
		return observacao;
	}

	public void setObservacao(ObservacoesProjeto observacao) {
		this.observacao = null;
		this.observacao = observacao;
	}
	
	public AlocacaoAtividade getAlocacao() {
		return alocacao;
	}

	public void setAlocacao(AlocacaoAtividade alocacao) {
		this.alocacao = null;
		this.alocacao = alocacao;
	}
	
	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = null;
		this.ocorrencia = ocorrencia;
	}

	public RegistroAtividade getRegistroAtividade() {
		return registroAtividade;
	}

	public void setRegistroAtividade(RegistroAtividade registroAtividade) {
		this.registroAtividade = null;
		this.registroAtividade = registroAtividade;
	}

	public Parametros getParametros() {
		if (this.parametros == null) {
			this.parametros = colaboradorService.getParametros();
		}
		return this.parametros;
	}

	public void setParametros(Parametros parametros) {
		this.parametros = null;
		this.parametros = parametros;
	}

	public String getNomeXML() {
		return nomeXML;
	}

	public void setNomeXML(String nomeXML) {
		this.nomeXML = nomeXML;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public Colaborador getColaboradorLogado() {
		if (colaboradorLogado == null) {
			colaboradorLogado = new Colaborador();
		}
		return colaboradorLogado;
	}

	public void setColaboradorLogado(Colaborador colaboradorLogado) {
		this.colaboradorLogado = colaboradorLogado;
	}

	public Calendar getDataTrabalho() {
		if (dataTrabalho == null) {
			dataTrabalho = Calendar.getInstance();
			dataTrabalho.set(Calendar.DST_OFFSET, 0);
		}
		
		dataTrabalho.set(Calendar.HOUR_OF_DAY, 0);
		dataTrabalho.set(Calendar.MINUTE, 0);
		dataTrabalho.set(Calendar.SECOND, 0);
		dataTrabalho.set(Calendar.MILLISECOND, 0);
		
		return dataTrabalho;
	}
	
	public int getAnoTrabalho(){
		return getDataTrabalho().get(Calendar.YEAR);
	}
	
	public String getDataTrabalhoFormatada() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy (EEEE");
		StringBuilder dataFormatada = new StringBuilder();
		
		dataFormatada.append(formato.format(dataTrabalho.getTime()));
		
		for(Feriados feriado : listaFeriados) {
			if (dataTrabalho.equals(feriado.getDataFeriado())) {
				dataFormatada.append(", feriado");
				break;
			}
		}
		
		dataFormatada.append(")");
		return dataFormatada.toString();
	}

	public void setDataTrabalho(Calendar dataTrabalho) {
		dataTrabalho.set(Calendar.HOUR_OF_DAY, 0);
		dataTrabalho.set(Calendar.MINUTE, 0);
		dataTrabalho.set(Calendar.SECOND, 0);
		dataTrabalho.set(Calendar.MILLISECOND, 0);
		
		this.dataTrabalho = dataTrabalho;
	}

	public Calendar getDataUltLogin() {
		return dataUltLogin;
	}

	public void setDataUltLogin(Calendar dataUltLogin) {
		this.dataUltLogin = dataUltLogin;
	}

	public float getQtdeHorasDia() {
		return qtdeHorasDia;
	}

	public void setQtdeHorasDia(float qtdeHorasDia) {
		this.qtdeHorasDia = qtdeHorasDia;
	}

	public float getQtdeHorasMes() {
		return qtdeHorasMes;
	}

	public void setQtdeHorasMes(float qtdeHorasMes) {
		this.qtdeHorasMes = qtdeHorasMes;
	}

	public boolean isFlagAlteracao() {
		return flagAlteracao;
	}

	public void setFlagAlteracao(boolean flagAlteracao) {
		this.flagAlteracao = flagAlteracao;
	}
	
	public CustoRealProjeto getCustoRealProjeto() {
		if (custoRealProjeto == null)
			custoRealProjeto = new CustoRealProjeto();
		
		return custoRealProjeto;
	}

	public void setCustoRealProjeto(CustoRealProjeto custoRealProjeto) {
		this.custoRealProjeto = custoRealProjeto;
	}

	public ProdutoInventario getProdutoInventario() {
		return produtoInventario;
	}

	public void setProdutoInventario(ProdutoInventario produtoInventario) {
		this.produtoInventario = produtoInventario;
	}
	
	public ItemInventario getItemInventario() {
		return itemInventario;
	}

	public void setItemInventario(ItemInventario itemInventario) {
		this.itemInventario = itemInventario;
	}

	public FraseDia getFraseDia() {
		return fraseDia;
	}

	public void setFraseDia(FraseDia fraseDia) {
		this.fraseDia = fraseDia;
	}

	public void valueChangeListener(AjaxBehaviorEvent event) {
		HtmlSelectOneMenu selectOneMenu = (HtmlSelectOneMenu) event.getSource();
		Object object = selectOneMenu.getLocalValue();
		
		if (object == null) {
			object = selectOneMenu.getSubmittedValue();
			
			if (object == null)
				object = selectOneMenu.getValue();
		}
		
		if (object instanceof String) {
			ConverterBean converterBean = new ConverterBean();
			converterBean.setColaboradorService(colaboradorService);
			
			object = converterBean.getAsObject(null, null, (String)object);
		}
		
		if (object instanceof Ferias) {
			this.setFerias((Ferias)object);
			return;
		}
		if (object instanceof Colaborador) {
			this.setColaborador((Colaborador)object);
			return;
		}

		if (object instanceof Cliente) {
			this.setCliente((Cliente)object);
			return;
		}

		if (object instanceof Area) {
			this.setArea((Area)object);
			return;
		}

		if (object instanceof Produto) {
			this.setProduto((Produto)object);
			return;
		}

		if (object instanceof Projeto) {
			this.setAlocacao(null);
			this.setProjeto((Projeto)object);
			return;
		}
		
		if (object instanceof AlocacaoAtividade) {
			this.setOcorrencia(null);
			this.setAlocacao((AlocacaoAtividade)object);
			return;
		}
		
		if (object instanceof Calendar) {
			Calendar data = (Calendar)object;
			String msg;
			
			this.setDataTrabalho(data);
			
			if(this.colaboradorLogado.getTipoUsuario() != Tipos.TIPO_USUARIO_ADMIN
				&& data.after(Calendar.getInstance())) {
				msg = FacesUtils.getMessage("registrarAtividade_erroDiasFuturos");
				FacesUtils.addWarningMessage(msg);
			}
			return;
		}
	}
	
	public void colaboradorFeriasChangeListener(AjaxBehaviorEvent event) {
		HtmlSelectOneMenu select = (HtmlSelectOneMenu) event.getSource();
		Colaborador colaborador = (Colaborador)select.getValue();
		this.getFerias().setColaborador(colaborador);
		
		if (colaborador.getDataContratacao() != null) {
			Calendar dataInicioRef = (Calendar)this.getFerias().getColaborador().getDataContratacao().clone();
			
			List<Ferias> listaFerias = this.getFerias().getColaborador().getFerias();
			for (Ferias ferias : listaFerias) {
				if(dataInicioRef.before(ferias.getDataFimRef())){
					dataInicioRef.add(Calendar.YEAR, 1);
				}
			}
			
			Calendar dataFim = (Calendar)dataInicioRef.clone();
			dataFim.add(Calendar.YEAR, 1);
			dataFim.add(Calendar.DAY_OF_MONTH, -1);
	
			this.getFerias().setDataInicioRef(dataInicioRef);
			this.getFerias().setDataFimRef(dataFim);
		}
	}
	
	public void dataInicioRefFeriasChangeListener(AjaxBehaviorEvent event) {
		Object object = event.getSource();
		
		if (object instanceof Calendar) {
			Calendar dataFim = (Calendar) ((Calendar) object).clone();
			dataFim.add(Calendar.YEAR, 1);
			dataFim.add(Calendar.DAY_OF_MONTH, -1);
			this.getFerias().setDataFimRef(dataFim);
		}
	}
	
	public boolean isPreenchimentoPermitido() {
		int diaSelecionado = this.dataTrabalho.get(Calendar.DAY_OF_MONTH);
		int mesSelecionado = this.dataTrabalho.get(Calendar.MONTH);
		int anoSelecionado = this.dataTrabalho.get(Calendar.YEAR);
		int diaAtual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int mesAtual = Calendar.getInstance().get(Calendar.MONTH);
		int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
		
		int ultDiaContabil = parametros.getUltimoDiaContabil(Calendar.getInstance());
		
		if ((this.colaboradorLogado.getTipoUsuario() == Tipos.TIPO_USUARIO_ADMIN) ||
			(anoSelecionado == anoAtual && mesSelecionado == mesAtual && diaAtual <= ultDiaContabil && diaSelecionado <= ultDiaContabil) ||
			(anoSelecionado == anoAtual && mesSelecionado == mesAtual && diaAtual > ultDiaContabil && diaSelecionado > ultDiaContabil) ||
			(anoSelecionado == anoAtual && mesSelecionado == mesAtual-1 && diaAtual <= ultDiaContabil && diaSelecionado > ultDiaContabil) ||
			(anoSelecionado == anoAtual-1 && mesSelecionado == Calendar.DECEMBER && mesAtual == Calendar.JANUARY && diaAtual <= ultDiaContabil && diaSelecionado > ultDiaContabil)) {
			return true;
		}
			
		return false;
	}
	
	public float getQtdeHorasPrevistasMes() {
		Calendar dataAux = this.getDataTrabalho();
		Calendar dataInicial = DataUtil.getDataInicioContabil(dataAux, parametros.getPrimeiroDiaContabil());
		Calendar dataFinal = DataUtil.getDataFimContabil(dataAux, parametros.getUltimoDiaContabil(dataAux));
		float qtdeHorasPrevistasMes = 0;
		DateFormat formatoData = new SimpleDateFormat("yyyyMMdd");
		
		while(formatoData.format(dataInicial.getTime()).compareTo(formatoData.format(dataFinal.getTime())) <=0) {
			if (DataUtil.colaboradorTrabalhaNoDia(colaborador, dataInicial,true))
				qtdeHorasPrevistasMes += 8;
			
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return qtdeHorasPrevistasMes;
	}

	public float getMediaDiariaPreenchimento() {
		Calendar dataHoje = Calendar.getInstance();
		Calendar dataInicial = DataUtil.getDataInicioContabil(dataTrabalho, parametros.getPrimeiroDiaContabil());
		float media;
		
		// Se for em um periodo futuro, nao calcula
		if (dataInicial.after(dataHoje))
			return 0;
		
		// Se for em um periodo passado, calcula pela quantidade de horas preenchidas
		// sobre a quantidade de horas previstas para o mes
		if (!isPreenchimentoPermitido()) {
			media = getQtdeHorasMes()*8/getQtdeHorasPrevistasMes();
		} else {
			
			float qtdeHorasPrevistasAteHoje = 0;
			
			while(!dataInicial.after(dataHoje)) {
				if (DataUtil.colaboradorTrabalhaNoDia(colaborador, dataInicial, true))
					qtdeHorasPrevistasAteHoje += 8;
				
				dataInicial.add(Calendar.DAY_OF_MONTH, 1);
			}
			
			media = getQtdeHorasMes()*8/qtdeHorasPrevistasAteHoje;
		}
		
		return Math.round(media*100)/100.0f;
	}
	

	public List<ObservacoesProjeto> getListaObservacoes(){
		List<ObservacoesProjeto> listaObservacoes = projeto.getObservacoes();
		List<ObservacoesProjeto> listaObservacoesRetorno = new ArrayList<ObservacoesProjeto>();
		for (ObservacoesProjeto observacoesProjeto : listaObservacoes) {
			if((!observacoesProjeto.isFlagVisibilidadeRestrita())||((colaboradorLogado.getTipoUsuario() != 2) && (colaboradorLogado.getTipoAcesso() == 0))){
				listaObservacoesRetorno.add(observacoesProjeto);
			}
		}
		return listaObservacoesRetorno;
	}

}
