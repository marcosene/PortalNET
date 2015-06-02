/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.portalnet.control.AdminServiceController;
import br.com.portalnet.model.AlocacaoAtividade;
import br.com.portalnet.model.Area;
import br.com.portalnet.model.Atividade;
import br.com.portalnet.model.Cliente;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Feriados;
import br.com.portalnet.model.GrupoAtividade;
import br.com.portalnet.model.ItemInventario;
import br.com.portalnet.model.Modulo;
import br.com.portalnet.model.Parametros;
import br.com.portalnet.model.Produto;
import br.com.portalnet.model.ProdutoInventario;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.TipoAtividade;
import br.com.portalnet.model.Tipos;
import br.com.portalnet.util.FacesUtils;


@ManagedBean(name="adminBean")
@ViewScoped
public class AdminBean {
	
	@ManagedProperty(value="#{adminService}")
	private AdminServiceController adminService;
	
	@ManagedProperty(value="#{sessionBean}")
	private SessionBean sessionBean;	

	@ManagedProperty(value="#{utilBean}")
	private UtilBean utilBean;
	
	private List<Projeto> listaProjetosGenericosSelecionados;
	
	
	public AdminServiceController getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminServiceController adminService) {
		this.adminService = adminService;
	}
	
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	
	public void setUtilBean(UtilBean utilBean) {
		this.utilBean = utilBean;
	}

	public List<Projeto> getListaProjetosGenericosSelecionados() {
		Colaborador colaborador = sessionBean.getColaborador();
		
		if (colaborador != null) {
			listaProjetosGenericosSelecionados = new ArrayList<Projeto>();
			
			for (AlocacaoAtividade alocacao : colaborador.getAlocacoes()) {
				Projeto projeto = alocacao.getAtividade().getGrupoAtividade().getProjeto();
				
				if (projeto.isGenerico() && projeto.getEstado() == Projeto.ESTADO_HABILITADO) {
					if (!listaProjetosGenericosSelecionados.contains(projeto)) {
						listaProjetosGenericosSelecionados.add(projeto);
					}
				}
			}
		}
		
		return listaProjetosGenericosSelecionados;
	}

	public void setListaProjetosGenericosSelecionados(
			List<Projeto> listaProjetosGenericosSelecionados) {
		this.listaProjetosGenericosSelecionados = listaProjetosGenericosSelecionados;
	}

	public List<SelectItem> getListaTiposUsuario() {
		List<SelectItem> listaTiposUsuario = new ArrayList<SelectItem>();
		
		for(Entry<Integer, String> entrada : Tipos.getTiposUsuarios().entrySet()) {
			listaTiposUsuario.add(new SelectItem(entrada.getKey().intValue(), entrada.getValue()));
		}
		
		return listaTiposUsuario;
	}

	public List<SelectItem> getListaTiposAcesso() {
		List<SelectItem> listaTiposAcesso = new ArrayList<SelectItem>();
		
		for(Entry<Integer, String> entrada : Tipos.getTiposAcessos().entrySet()) {
			listaTiposAcesso.add(new SelectItem(entrada.getKey().intValue(), entrada.getValue()));
		}
		
		return listaTiposAcesso;
	}
	
	public List<SelectItem> getListaTiposLinguagem() {
		List<SelectItem> listaTiposLinguagem = new ArrayList<SelectItem>();
		
		for(Entry<Integer, String> entrada : Tipos.getTiposLinguagens().entrySet()) {
			listaTiposLinguagem.add(new SelectItem(entrada.getKey().intValue(), entrada.getValue()));
		}
		
		return listaTiposLinguagem;
	}

	public String cadastrarNovoColaboradorAction() {
		sessionBean.setColaborador(new Colaborador());
		return NavigationResults.NEW;
	}
	
	public String cadastrarNovoClienteAction() {
		if (adminService.getListaColaboradoresGGs().isEmpty()) {
			String msg = FacesUtils.getMessage("cadastrarCliente_naoHaGerenteGeral");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		sessionBean.setCliente(new Cliente());
		return NavigationResults.NEW;
	}
	
	public String cadastrarNovaAreaAction() {
		if (adminService.getListaColaboradoresGFs().isEmpty()) {
			String msg = FacesUtils.getMessage("cadastrarArea_naoHaGerenteFuncional");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		Area area = new Area();
		area.setCliente(sessionBean.getCliente());
		sessionBean.setArea(area);
		return NavigationResults.NEW;
	}
	
	public String cadastrarNovoProdutoAction() {
		if (adminService.getListaColaboradoresGPDs().isEmpty()) {
			String msg = FacesUtils.getMessage("cadastrarProduto_naoHaGerenteProduto");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		Produto produto = new Produto();
		produto.setArea(sessionBean.getArea());
		sessionBean.setProduto(produto);
		return NavigationResults.NEW;
	}
	
	public String cadastrarNovoModuloAction() {
		Modulo modulo = new Modulo();
		modulo.setProduto(sessionBean.getProduto());
		sessionBean.setModulo(modulo);
		return NavigationResults.NEW;
	}
	
	public String cadastrarNovoTipoAtividadeAction() {
		sessionBean.setTipoAtividade(new TipoAtividade());
		return NavigationResults.NEW;
	}
	
	public String cadastrarNovoFeriadoAction() {
		sessionBean.setFeriado(new Feriados());
		return NavigationResults.NEW;
	}
	
	public String cadastrarColaboradorAction() {
		Colaborador colaborador = sessionBean.getColaborador(); 
		
		if (colaborador.getId() == null) {
			try {
				File imagem = new File(FacesUtils.getServletContext().getRealPath(File.separator) + "imagens/sem_foto.jpg");
				FileInputStream fileInputStream = new FileInputStream(imagem);
				
				byte[] foto = new byte[(int)imagem.length()];
				fileInputStream.read(foto);
				fileInputStream.close();
				
				colaborador.setFoto(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Aloca automaticamente o colaborador em cada atividade
		// cadastrada dos projetos genericos selecionados para alocacao
		if (colaborador.getId() == null && listaProjetosGenericosSelecionados != null) {
			for(Projeto projeto : listaProjetosGenericosSelecionados) {
				projeto = adminService.getProjetoPK(projeto.getId());
				
				for(GrupoAtividade grupoAtividade : projeto.getGruposAtividades()) {
					for (Atividade atividade : grupoAtividade.getAtividades()) {
						AlocacaoAtividade alocacao = new AlocacaoAtividade();
						alocacao.setAtividade(atividade);
						alocacao.setColaborador(colaborador);
						
						atividade.adicionarAlocacao(alocacao);
						colaborador.adicionarAlocacao(alocacao);
					}
				}
			}
		}
		
		try {
			adminService.cadastrarColaborador(colaborador);
			
		} catch (DataIntegrityViolationException sqle) {
			String msg = FacesUtils.getMessage("cadastrarColaborador_usuarioJaCadastrado");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarClienteAction() {
		try {
			adminService.cadastrarCliente(sessionBean.getCliente());
			
		} catch (DataIntegrityViolationException sqle) {
			String msg = FacesUtils.getMessage("cadastrarCliente_clienteJaCadastrado");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarAreaAction() {
		try {
			adminService.cadastrarArea(sessionBean.getArea());
			
		} catch (DataIntegrityViolationException sqle) {
			String msg = FacesUtils.getMessage("cadastrarArea_areaJaCadastrada");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarProdutoAction() {
		try {
			adminService.cadastrarProduto(sessionBean.getProduto());
			
		} catch (DataIntegrityViolationException sqle) {
			String msg = FacesUtils.getMessage("cadastrarProduto_produtoJaCadastrado");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarModuloAction() {
		Produto produto = sessionBean.getProduto();
		Modulo modulo = sessionBean.getModulo();
		produto.adicionarModulo(modulo);
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarTipoAtividadeAction() {
		try {
			adminService.cadastrarTipoAtividade(sessionBean.getTipoAtividade());
			
		} catch (DataIntegrityViolationException sqle) {
			String msg = FacesUtils.getMessage("cadastrarTipoAtividade_tipoAtividadeJaCadastrado");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		return NavigationResults.SUCCESS;
	}
	
	public String selecionarColaboradorAction() {
		int idColaborador = Integer.parseInt(FacesUtils.getRequestProperty("idColaborador"));
		sessionBean.setColaborador(adminService.getColaboradorPK(idColaborador));
		return NavigationResults.EDIT;
	}
	
	public String selecionarClienteAction() {
		int idCliente = Integer.parseInt(FacesUtils.getRequestProperty("idCliente"));
		sessionBean.setCliente(adminService.getClientePK(idCliente));
		return NavigationResults.EDIT;
	}
	
	public String selecionarAreaAction() {
		int idArea = Integer.parseInt(FacesUtils.getRequestProperty("idArea"));
		sessionBean.setArea(adminService.getAreaPK(idArea));
		return NavigationResults.EDIT;
	}
	
	public String selecionarProdutoAction() {
		int idProduto = Integer.parseInt(FacesUtils.getRequestProperty("idProduto"));
		sessionBean.setProduto(adminService.getProdutoPK(idProduto));
		return NavigationResults.EDIT;
	}
	
	public void selecionarModuloAction(ActionEvent event) {
		DataTable tabelaModulos = (DataTable) event.getComponent().getParent().getParent().getParent();
		Modulo modulo = (Modulo) tabelaModulos.getRowData();
		
		sessionBean.setModulo(modulo);
	}
	
	public void exportarFeriadosAction(){
		int anoDestino = utilBean.getAnoSelecionado() + 1;
		List<Feriados> listaFeriados = getListaFeriadosNoAno();

		for (Feriados feriado : listaFeriados) {
				Feriados f = new Feriados();
				
				String nome = feriado.getNome();
				Calendar data = (Calendar)feriado.getDataFeriado().clone();
				data.set(Calendar.YEAR, anoDestino);
				
				f.setNome(nome);
				f.setDataFeriado(data);
				
				adminService.cadastrarFeriado(f);
		}
		
		String msg = FacesUtils.getMessage("cadastrarFeriados_sucessoExportacao");
		FacesUtils.addInfoMessage(msg);
	}
	
	public String selecionarTipoAtividadeAction() {
		int idTipoAtividade = Integer.parseInt(FacesUtils.getRequestProperty("idTipoAtividade"));
		sessionBean.setTipoAtividade(adminService.getTipoAtividadePK(idTipoAtividade));
		return NavigationResults.EDIT;
	}
	
	public String selecionarFeriadoAction() {
		int idFeriado = Integer.parseInt(FacesUtils.getRequestProperty("idFeriado"));
		sessionBean.setFeriado(adminService.getFeriadoPK(idFeriado));
		return NavigationResults.EDIT;
	}
	
	public void excluirColaboradorAction(ActionEvent event) {
		selecionarColaboradorAction();
		Colaborador colaborador = sessionBean.getColaborador();
		
		// Verifica se o colaborador possui algum registro de atividade
		// Caso possua, bloqueia a exclusao
		for(AlocacaoAtividade alocacao : colaborador.getAlocacoes()) {
			if (!alocacao.getRegistros().isEmpty()) {
				String msg = FacesUtils.getMessage("cadastrarColaborador_erroExcluir");
				FacesUtils.addErrorMessage(msg);
				return;
			}
		}
		
		for(AlocacaoAtividade alocacao : colaborador.getAlocacoes()) {
			alocacao.getAtividade().removerAlocacao(alocacao);
		}
		
		adminService.excluirColaborador(colaborador);
	}
	
	public void excluirClienteAction(ActionEvent event) {
		selecionarClienteAction();
		Cliente cliente = sessionBean.getCliente();
		
		if (!cliente.getAreas().isEmpty()) {
			String msg = FacesUtils.getMessage("cadastrarCliente_erroExcluir");
			FacesUtils.addErrorMessage(msg);
			return;
		}
		
		adminService.excluirCliente(cliente);
	}
	
	public void excluirAreaAction(ActionEvent event) {
		selecionarAreaAction();
		Area area = sessionBean.getArea();
		
		if (!area.getProdutos().isEmpty()) {
			String msg = FacesUtils.getMessage("cadastrarArea_erroExcluir");
			FacesUtils.addErrorMessage(msg);
			return;
		}
		
		adminService.excluirArea(area);
	}
	
	public void excluirProdutoAction(ActionEvent event) {
		selecionarProdutoAction();
		Produto produto = sessionBean.getProduto();
		
		if (!produto.getProjetos().isEmpty()) {
			String msg = FacesUtils.getMessage("cadastrarProduto_erroExcluir");
			FacesUtils.addErrorMessage(msg);
			return;
		}
		
		adminService.excluirProduto(produto);
	}
	
	public void excluirModuloAction(ActionEvent event) {
		selecionarModuloAction(event);
		Modulo modulo = sessionBean.getModulo();
		Produto produto = sessionBean.getProduto();
		produto.removerModulo(modulo);
	}
	
	public void excluirTipoAtividadeAction(ActionEvent event) {
		selecionarTipoAtividadeAction();
		TipoAtividade tipoAtividade = sessionBean.getTipoAtividade();
		adminService.excluirTipoAtividade(tipoAtividade);
	}
	
	public void excluirFeriadoAction(ActionEvent event) {
		selecionarFeriadoAction();
		Feriados feriado = sessionBean.getFeriado();
		adminService.excluirFeriado(feriado);
		
		int ano = Calendar.getInstance().get(Calendar.YEAR);
		sessionBean.setListaFeriados(adminService.getListaFeriadosDoAno(ano));
	}
	
	public void alterarEstadoColaboradorAction(ActionEvent event) {
		selecionarColaboradorAction();
		Colaborador colaborador = sessionBean.getColaborador();
		colaborador.setFlagAtivo(!colaborador.isFlagAtivo());
		adminService.cadastrarColaborador(colaborador);
	}
	
	public String cadastrarFeriadoAction() {
		try {
			Feriados feriado = sessionBean.getFeriado();
			adminService.cadastrarFeriado(feriado);
			
		} catch (DataIntegrityViolationException sqle) {
			String msg = FacesUtils.getMessage("cadastrarFeriados_feriadoJaCadastrado");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}
		
		int ano = Calendar.getInstance().get(Calendar.YEAR);
		sessionBean.setListaFeriados(adminService.getListaFeriadosDoAno(ano));
		
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarProdutoInventarioAction() {
		try {
			ProdutoInventario produto = sessionBean.getProdutoInventario();
			adminService.cadastrarProdutoInventario(produto);
		
		} catch (DataIntegrityViolationException sqle) {
			String msg = FacesUtils.getMessage("cadastrarInventario_produtoJaCadastrado");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}		
		return NavigationResults.SUCCESS;
	}
	
	public String cadastrarItemProdutoInventarioAction() {
		try {
			if (sessionBean.getProdutoInventario().getListaItens().contains(sessionBean.getItemInventario())) {
				if (sessionBean.getItemInventario().getResponsavel() == null) {
					sessionBean.getItemInventario().setDataEmprestimo(null);
				}
				adminService.cadastrarProdutoInventario(sessionBean.getProdutoInventario());
			} else {
				if (sessionBean.getItemInventario().getResponsavel() == null) {
					sessionBean.getItemInventario().setDataEmprestimo(null);
				}
				sessionBean.getProdutoInventario().getListaItens().add(sessionBean.getItemInventario());	
				//se eh apenas edicao, ja faz o merge
				adminService.cadastrarProdutoInventario(sessionBean.getProdutoInventario());
			}
		} catch (DataIntegrityViolationException sqle) {
			String msg = FacesUtils.getMessage("cadastrarInventario_itemJaCadastrado");
			FacesUtils.addErrorMessage(msg);
			return NavigationResults.FAILURE;
		}		
		return NavigationResults.SUCCESS;
	}

	public List<Feriados> getListaFeriadosNoAno() {
		int anoSelecionado = utilBean.getAnoSelecionado();
		List<Feriados> listaFeriados = adminService.getListaFeriadosDoAno(anoSelecionado);
		
		return listaFeriados;
	}

	public String selecionarParametrosAction() {
		sessionBean.setParametros(adminService.getParametros());
		return NavigationResults.EDIT;
	}

	public String cadastrarParametrosAction(){
		Parametros parametros = sessionBean.getParametros();
		adminService.cadastrarParametros(parametros);
		return NavigationResults.SUCCESS;
	}
	
	public void listenerFotoLogoTipo(FileUploadEvent event) {
		UploadedFile item = event.getFile();
        sessionBean.getParametros().setLogotipo(item.getContents());
    }
	
	public List<Projeto> getListaProjetosGenericos() {
		return adminService.getListaProjetosGenericos();
	}
	
	public List<Colaborador> getListaColaboradores() {
		if (FacesUtils.getRequestProperty("formGeral:todos") != null) {
			if(Integer.parseInt(FacesUtils.getRequestProperty("formGeral:todos")) == 1)
				//o valor retornado eh o do indice da combo (1 = Todos)
				return adminService.getListaColaboradores(true);
			else
				return adminService.getListaColaboradores(false);
		}else{
			return adminService.getListaColaboradores(false);
		}
	}

	public void selecionarProdutoInventarioAction(ActionEvent event) {
		DataTable tabelaProdutoInventario = (DataTable) event.getComponent().getParent().getParent().getParent();
		ProdutoInventario produtoInventario = (ProdutoInventario) tabelaProdutoInventario.getRowData();
		sessionBean.setProdutoInventario(produtoInventario);
	}
	
	public void selecionarItemInventarioAction(ActionEvent event) {
		DataTable tabelaProdutoInventario = (DataTable) event.getComponent().getParent().getParent().getParent().getParent().getParent();
		ProdutoInventario produtoInventario = (ProdutoInventario) tabelaProdutoInventario.getRowData();
		sessionBean.setProdutoInventario(produtoInventario);
		
		DataTable tabelaItemInventario = (DataTable) event.getComponent().getParent().getParent().getParent();
		ItemInventario itemInventario = (ItemInventario) tabelaItemInventario.getRowData();
		sessionBean.setItemInventario(itemInventario);
	}
	
	public String cadastrarNovoItemInventarioAction() {
		ItemInventario itemInventario = new ItemInventario();
		itemInventario.setProdutoInventario(sessionBean.getProdutoInventario());
		sessionBean.setItemInventario(itemInventario);
		return NavigationResults.NEW2;
	}
	
	public String cadastrarNovoProdutoInventarioAction() {
		ProdutoInventario produto = new ProdutoInventario();
		sessionBean.setProdutoInventario(produto);
		return NavigationResults.NEW;
	}	

	public void excluirProdutoInventarioAction(ActionEvent event) {
		selecionarProdutoInventarioAction(event);
		ProdutoInventario produto = sessionBean.getProdutoInventario();
		adminService.excluirProdutoInventario(produto);
	}
	
	public void excluirItemInventarioAction(ActionEvent event) {
		selecionarItemInventarioAction(event);
		sessionBean.getProdutoInventario().getListaItens().remove(sessionBean.getItemInventario());
		adminService.cadastrarProdutoInventario(sessionBean.getProdutoInventario());
	}
}
