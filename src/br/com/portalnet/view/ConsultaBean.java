/**
 * @since 27/05/2009
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import br.com.portalnet.control.ColaboradorServiceController;
import br.com.portalnet.model.Area;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Compromisso;
import br.com.portalnet.model.Equipe;
import br.com.portalnet.model.Modulo;
import br.com.portalnet.model.Ocorrencia;
import br.com.portalnet.model.Produto;
import br.com.portalnet.model.ProdutoInventario;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.Tipos;
import br.com.portalnet.util.FacesUtils;
import br.com.portalnet.util.StringUtil;


@ManagedBean(name="consultaBean")
@ViewScoped
public class ConsultaBean {

	@ManagedProperty(value="#{colaboradorService}")
	private ColaboradorServiceController colaboradorService;
	
	@ManagedProperty(value="#{sessionBean}")
	private SessionBean sessionBean;
	
	private int statusSelecionado = -1;
	
	private int categoriaSelecionada = -1;
	
	
	public ColaboradorServiceController getColaboradorService() {
		return colaboradorService;
	}

	public void setColaboradorService(ColaboradorServiceController colaboradorService) {
		this.colaboradorService = colaboradorService;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	
	public int getStatusSelecionado() {
		return statusSelecionado;
	}
	
	public void setStatusSelecionado(int statusSelecionado) {
		this.statusSelecionado = statusSelecionado;
	}

	public int getCategoriaSelecionada() {
		if (categoriaSelecionada == -1) {
			categoriaSelecionada = Tipos.TIPO_CATEG_INVENT_CD_DVD;
		}
		return categoriaSelecionada;
	}

	public void setCategoriaSelecionada(int categoriaSelecionada) {
		this.categoriaSelecionada = categoriaSelecionada;
	}

	public String getUrlPaginaPrincipal() {
		return FacesUtils.getServletContext().getContextPath() + "/principal.xhtml";
	}
	
	public List<Area> getListaAreas() {
		return colaboradorService.getListaAreas(sessionBean.getCliente());
	}
	
	public List<Produto> getListaProdutos() {
		return colaboradorService.getListaProdutos(sessionBean.getArea());
	}

	public List<Projeto> getListaProjetos() {
		Colaborador colaborador = sessionBean.getColaboradorLogado();
		Produto produto = sessionBean.getProduto();
		
		if (produto == null)
			return null;
		
		return colaboradorService.getListaProjetos(produto, false, false, colaborador);
	}
	
	public List<Projeto> getListaProjetosAtivos() {
		Colaborador colaborador = sessionBean.getColaboradorLogado();
		Produto produto = sessionBean.getProduto();
		
		if (produto == null)
			return null;
		
		return colaboradorService.getListaProjetos(produto, false, true, colaborador);
	}
	
	public List<Projeto> getListaProjetosNaoGenericos() {
		Colaborador colaborador = sessionBean.getColaboradorLogado();
		Produto produto = sessionBean.getProduto();
		
		if (produto == null)
			return null;
		
		return colaboradorService.getListaProjetos(produto, true, true, colaborador);
	}
	
	public List<Equipe> getListaEquipes() {
		return colaboradorService.getListaEquipes(false, null);
	}
	
	public List<Modulo> getListaModulos() {
		return colaboradorService.getListaModulos(sessionBean.getProduto());
	}
	
	public List<Ocorrencia> getListaOcorrencias() {
		String[] listaChavesBusca;
		if (sessionBean.getChavePesquisaOcorrencia() != null) {
			listaChavesBusca = StringUtil.organizarChavesPesquisa(sessionBean.getChavePesquisaOcorrencia().trim());
			return colaboradorService.getPesquisaOcorrencias(listaChavesBusca);
		}
		sessionBean.setChavePesquisaOcorrencia(null);
		
		if (sessionBean.getProjeto() != null)
			return colaboradorService.getListaOcorrencias(sessionBean.getProjeto(), statusSelecionado);
		else
			return colaboradorService.getListaOcorrencias(sessionBean.getProduto(), statusSelecionado);
	}
	
	public List<SelectItem> getListaTiposSeveridade() {
		List<SelectItem> listaTiposSeveridade = new ArrayList<SelectItem>();
		
		for(Entry<Integer, String> entrada : Tipos.getTiposSeveridadesOcorrencia().entrySet()) {
			listaTiposSeveridade.add(new SelectItem(entrada.getKey().intValue(), entrada.getValue()));
		}
		
		return listaTiposSeveridade;
	}
	
	public List<SelectItem> getListaTiposStatusOcorrencia() {
		List<SelectItem> listaTiposStatusOcorrencia = new ArrayList<SelectItem>();
		
		for(Entry<Integer, String> entrada : Tipos.getTiposStatusOcorrencia().entrySet()) {
			listaTiposStatusOcorrencia.add(new SelectItem(entrada.getKey().intValue(), entrada.getValue()));
		}
		
		return listaTiposStatusOcorrencia;
	}

	public List<SelectItem> getListaTiposObservacoes() {
		List<SelectItem> listaTiposObservacoes = new ArrayList<SelectItem>();
		
		for(Entry<Integer, String> entrada : Tipos.getTiposObservacoes().entrySet()) {
			listaTiposObservacoes.add(new SelectItem(entrada.getKey().intValue(), entrada.getValue()));
		}
		
		return listaTiposObservacoes;
	}

	public List<SelectItem> getListaTiposRepeticoes() {
		List<SelectItem> listaTiposRepeticoes = new ArrayList<SelectItem>();
		
		for(Entry<Integer, String> entrada : Tipos.getTiposRepeticoes().entrySet()) {
			listaTiposRepeticoes.add(new SelectItem(entrada.getKey().intValue(), entrada.getValue()));
		}
		
		return listaTiposRepeticoes;
	}
	
	public List<SelectItem> getListaTiposAssociacoes() {
		List<SelectItem> listaTiposAssociacoes = new ArrayList<SelectItem>();
		
		for(Entry<Integer, String> entrada : Tipos.getTiposAssociacoes().entrySet()) {
			listaTiposAssociacoes.add(new SelectItem(entrada.getKey().intValue(), entrada.getValue()));
		}
		
		return listaTiposAssociacoes;
	}
	
	public List<SelectItem> getListaTiposCategoriasInventario() {
		List<SelectItem> listaTiposCategorias = new ArrayList<SelectItem>();
		
		for(Entry<Integer, String> entrada : Tipos.getTiposInventario().entrySet()) {
			listaTiposCategorias.add(new SelectItem(entrada.getKey().intValue(), entrada.getValue()));
		}		
		return listaTiposCategorias;
	}
	
	public List<ProdutoInventario> getTodosProdutosInventario() {
		List<ProdutoInventario> listaProdutosInventario;
		
		if (sessionBean.getColaboradorLogado().getTipoUsuario() == Tipos.TIPO_USUARIO_ADMIN ||
				sessionBean.getColaboradorLogado().getTipoUsuario() == Tipos.TIPO_USUARIO_ASSISTENTE) {
			listaProdutosInventario = colaboradorService.getTodosProdutosInventario(categoriaSelecionada);	
		} else {
			listaProdutosInventario = colaboradorService.getProdutosInventarioEmprestaveis(categoriaSelecionada);
		}
		
		Collections.sort(listaProdutosInventario);
		return listaProdutosInventario;
	}	
	
	public List<Compromisso> getTodosCompromissosColaborador() {
		return colaboradorService.getTodosCompromissosColaborador(sessionBean.getColaboradorLogado());
	}
	
}
