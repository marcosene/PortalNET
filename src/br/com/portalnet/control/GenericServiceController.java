/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.control;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.portalnet.dao.AlocacaoAtividadeDAO;
import br.com.portalnet.dao.AreaDAO;
import br.com.portalnet.dao.ClienteDAO;
import br.com.portalnet.dao.ColaboradorDAO;
import br.com.portalnet.dao.CompromissoDAO;
import br.com.portalnet.dao.CustoRealProjetoDAO;
import br.com.portalnet.dao.EquipeDAO;
import br.com.portalnet.dao.FeriadoDAO;
import br.com.portalnet.dao.FeriasDAO;
import br.com.portalnet.dao.FraseDiaDAO;
import br.com.portalnet.dao.MuralRecadosDAO;
import br.com.portalnet.dao.OcorrenciaDAO;
import br.com.portalnet.dao.ParametrosDAO;
import br.com.portalnet.dao.ProdutoDAO;
import br.com.portalnet.dao.ProdutoInventarioDAO;
import br.com.portalnet.dao.ProjetoDAO;
import br.com.portalnet.dao.TipoAtividadeDAO;
import br.com.portalnet.model.Area;
import br.com.portalnet.model.Cliente;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Compromisso;
import br.com.portalnet.model.CustoRealProjeto;
import br.com.portalnet.model.Equipe;
import br.com.portalnet.model.Feriados;
import br.com.portalnet.model.Ferias;
import br.com.portalnet.model.FraseDia;
import br.com.portalnet.model.Modulo;
import br.com.portalnet.model.Ocorrencia;
import br.com.portalnet.model.Parametros;
import br.com.portalnet.model.Produto;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.TipoAtividade;


@Transactional(readOnly = true)
public abstract class GenericServiceController {

	@Autowired
	protected ColaboradorDAO colaboradorDAO;
	
	@Autowired
	protected ClienteDAO clienteDAO;
	
	@Autowired
	protected AreaDAO areaDAO;
	
	@Autowired
	protected ProdutoDAO produtoDAO;
	
	@Autowired
	protected TipoAtividadeDAO tipoAtividadeDAO;
	
	@Autowired
	protected ProjetoDAO projetoDAO;
	
	@Autowired
	protected AlocacaoAtividadeDAO alocacaoAtividadeDAO;
	
	@Autowired
	protected OcorrenciaDAO ocorrenciaDAO;

	@Autowired
	protected MuralRecadosDAO muralRecadosDAO;
	
	@Autowired
	protected FeriadoDAO feriadoDAO;
	
	@Autowired
	protected CompromissoDAO compromissoDAO;
	
	@Autowired
	protected EquipeDAO equipeDAO;
	
	@Autowired
	protected ParametrosDAO parametrosDAO;
	
	@Autowired
	protected FeriasDAO feriasDAO;
	
	@Autowired
	protected CustoRealProjetoDAO custoRealProjetoDAO;
	
	@Autowired
	protected ProdutoInventarioDAO produtoInventarioDAO;

	@Autowired
	protected FraseDiaDAO fraseDiaDAO;
	
	
	public void setColaboradorDAO(ColaboradorDAO colaboradorDAO) {
		this.colaboradorDAO = colaboradorDAO;
	}
	
	public void setClienteDAO(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}

	public void setFeriadoDAO(FeriadoDAO feriadoDAO) {
		this.feriadoDAO = feriadoDAO;
	}

	public void setAreaDAO(AreaDAO areaDAO) {
		this.areaDAO = areaDAO;
	}

	public void setProdutoDAO(ProdutoDAO produtoDAO) {
		this.produtoDAO = produtoDAO;
	}

	public void setTipoAtividadeDAO(TipoAtividadeDAO tipoAtividadeDAO) {
		this.tipoAtividadeDAO = tipoAtividadeDAO;
	}

	public void setProjetoDAO(ProjetoDAO projetoDAO) {
		this.projetoDAO = projetoDAO;
	}
	
	public void setAlocacaoAtividadeDAO(AlocacaoAtividadeDAO alocacaoAtividadeDAO) {
		this.alocacaoAtividadeDAO = alocacaoAtividadeDAO;
	}
	
	public void setOcorrenciaDAO(OcorrenciaDAO ocorrenciaDAO) {
		this.ocorrenciaDAO = ocorrenciaDAO;
	}

	public void setMuralRecadosDAO(MuralRecadosDAO muralrecadosDAO) {
		this.muralRecadosDAO = muralrecadosDAO;
	}
	
	public void setCompromissoDAO(CompromissoDAO compromissoDAO) {
		this.compromissoDAO = compromissoDAO;
	}

	public void setEquipeDAO(EquipeDAO equipeDAO) {
		this.equipeDAO = equipeDAO;
	}

	public void setParametrosDAO(ParametrosDAO parametrosDAO) {
		this.parametrosDAO = parametrosDAO;
	}

	public void setFeriasDAO(FeriasDAO feriasDAO) {
		this.feriasDAO = feriasDAO;
	}
	
	public void setCustoRealProjetoDAO(CustoRealProjetoDAO custoRealProjetoDAO) {
		this.custoRealProjetoDAO = custoRealProjetoDAO;
	}
	
	public void setProdutoInventarioDAO(ProdutoInventarioDAO produtoInventarioDAO) {
		this.produtoInventarioDAO = produtoInventarioDAO;
	}

	public void setFraseDiaDAO(FraseDiaDAO fraseDiaDAO) {
		this.fraseDiaDAO = fraseDiaDAO;
	}
	
	public Object getGenericObject(Class<?> classe, Serializable id) {
		return this.colaboradorDAO.getGenericObject(classe, id);
	}
	
	public List<TipoAtividade> getListaTiposAtividade() {
		List<TipoAtividade> listaTiposAtividade = tipoAtividadeDAO.listAll();
		Collections.sort(listaTiposAtividade);
		return listaTiposAtividade;
	}
	
	public List<Cliente> getListaClientes() {
		List<Cliente> listaClientes = clienteDAO.listAll();
		Collections.sort(listaClientes);
		return listaClientes;
	}
	
	public List<Area> getListaAreas(Cliente cliente) {
		if (cliente == null)
			return null;
		
		List<Area> listaAreas = areaDAO.getAreas(cliente);
		Collections.sort(listaAreas);
		return listaAreas;
	}
	
	public List<Produto> getListaProdutos(Area area) {
		if (area == null)
			return null;
		
		List<Produto> listaProdutos = produtoDAO.getProdutos(area);
		Collections.sort(listaProdutos);
		return listaProdutos;
	}
	
	public List<Projeto> getListaProjetos(Produto produto, boolean flagSoNaoGenericos, 
										boolean flagSoAtivos, Colaborador colaborador) {
		return projetoDAO.getProjetos(produto, flagSoNaoGenericos, flagSoAtivos, colaborador);
	}
	
	public List<Feriados> getListaFeriadosDoAno(int anoSelecionado) {
		List<Feriados> listaFeriados;
		if(anoSelecionado == 0) {
			listaFeriados = feriadoDAO.listAll();	
		}
		else {
			listaFeriados = feriadoDAO.getListaFeriadosDoAno(anoSelecionado);
		}
		Collections.sort(listaFeriados);
		return listaFeriados;
	}

	public List<Equipe> getListaEquipes(boolean flagSoAtivas, Colaborador coordenador) {
		return equipeDAO.getListaEquipes(flagSoAtivas, coordenador);
	}
	
	public List<Modulo> getListaModulos(Produto produto) {
		if (produto == null)
			return null;
		
		List<Modulo> listaModulos = produtoDAO.getModulos(produto);
		Collections.sort(listaModulos);
		return listaModulos;
	}
	
	public List<Ocorrencia> getListaOcorrencias(Projeto projeto, int status) {
		if (projeto == null)
			return null;
	
		return ocorrenciaDAO.getOcorrencias(projeto, status);
	}
	
	public List<Ocorrencia> getListaOcorrencias(Produto produto, int status) {
		if (produto == null)
			return null;
	
		return ocorrenciaDAO.getOcorrencias(produto, status);
	}
		
	public List<Compromisso> getListaCompromissosEmail() {
		return compromissoDAO.getCompromissosEmail();
	}
	
	public List<Ferias> getListaFerias(final int anoSelecionado) {
		List<Ferias> listaFerias;
		if(anoSelecionado == 0){
			listaFerias = feriasDAO.listAll();
		}else{
			listaFerias = feriasDAO.getListaFerias(anoSelecionado);
		}
		Collections.sort(listaFerias);
		return listaFerias;
	}
	
	public Parametros getParametros() {
		return parametrosDAO.getByPk(1);
	}
	
	public List<CustoRealProjeto> getListaCustosReaisProjeto(final int mesSelecionado, final int anoSelecionado) {
		List<CustoRealProjeto> listaCustosReais;
		
		listaCustosReais = custoRealProjetoDAO.getlistaCustosReaisProjeto(mesSelecionado, anoSelecionado);
		Collections.sort(listaCustosReais);
		
		return listaCustosReais;
	}
	
	public List<FraseDia> getListaFrasesDia(Colaborador colaborador) {
		return fraseDiaDAO.getFrasesDia(colaborador);
	}

}