/**
 * @since 08/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.control;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.portalnet.model.Area;
import br.com.portalnet.model.Cliente;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Feriados;
import br.com.portalnet.model.Parametros;
import br.com.portalnet.model.Produto;
import br.com.portalnet.model.ProdutoInventario;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.TipoAtividade;


@Service("adminService")
@Transactional(readOnly = true)
public class AdminServiceController extends GestorServiceController {
	
	@Transactional(readOnly = false)
	public void cadastrarParametros(Parametros parametros) throws DataIntegrityViolationException {
		if (parametros.getId() == null) {
			parametrosDAO.save(parametros);
		} else {
			parametrosDAO.merge(parametros);
		}
	}
	
	@Transactional(readOnly = false)
	public void cadastrarColaborador(Colaborador colaborador) throws DataIntegrityViolationException {
		if (colaborador.getId() == null) {
			colaboradorDAO.save(colaborador);
		} else {
			colaboradorDAO.merge(colaborador);
		}
	}
	
	@Transactional(readOnly = false)
	public void cadastrarFeriado(Feriados feriado) throws DataIntegrityViolationException {
		if (feriado.getId() == null) {
			feriadoDAO.save(feriado);
		} else {
			feriadoDAO.merge(feriado);
		}
	}
	
	@Transactional(readOnly = false)
	public void cadastrarCliente(Cliente cliente) throws DataIntegrityViolationException {
		if (cliente.getId() == null) {
			clienteDAO.save(cliente);
		} else {
			clienteDAO.merge(cliente);
		}
	}

	@Transactional(readOnly = false)
	public void cadastrarArea(Area area) throws DataIntegrityViolationException {
		if (area.getId() == null) {
			areaDAO.save(area);
		} else {
			areaDAO.merge(area);
		}
	}

	@Transactional(readOnly = false)
	public void cadastrarProduto(Produto produto) throws DataIntegrityViolationException {
		if (produto.getId() == null) {
			produtoDAO.save(produto);
		} else {
			produtoDAO.merge(produto);
		}
	}
	
	@Transactional(readOnly = false)
	public void cadastrarTipoAtividade(TipoAtividade tipoAtividade) throws DataIntegrityViolationException {
		if (tipoAtividade.getId() == null) {
			tipoAtividadeDAO.save(tipoAtividade);
		} else {
			tipoAtividadeDAO.merge(tipoAtividade);
		}
	}
	
	@Transactional(readOnly = false)
	public void cadastrarProdutoInventario(ProdutoInventario produto) throws DataIntegrityViolationException {
		if (produto.getId() == null) {
			produtoInventarioDAO.save(produto);
		} else {
			produtoInventarioDAO.merge(produto);
		}
	}
	
	@Transactional(readOnly = false)
	public void excluirColaborador(Colaborador colaborador) {
		colaboradorDAO.delete(colaborador);
	}
	
	@Transactional(readOnly = false)
	public void excluirFeriado(Feriados feriado) {
		feriadoDAO.delete(feriado);
	}
	
	@Transactional(readOnly = false)
	public void excluirCliente(Cliente cliente) {
		clienteDAO.delete(cliente);
	}
	
	@Transactional(readOnly = false)
	public void excluirArea(Area area) {
		areaDAO.delete(area);
	}
	
	@Transactional(readOnly = false)
	public void excluirProduto(Produto produto) {
		produtoDAO.delete(produto);
	}
	
	@Transactional(readOnly = false)
	public void excluirTipoAtividade(TipoAtividade tipoAtividade) {
		tipoAtividadeDAO.delete(tipoAtividade);
	}
	
	@Transactional(readOnly = false)
	public void excluirProdutoInventario(ProdutoInventario produto) {
		produtoInventarioDAO.delete(produto);
	}
	
	public Colaborador getColaboradorPK(int id) {
		return colaboradorDAO.getByPk(id);
	}
	
	public Feriados getFeriadoPK(int id) {
		return feriadoDAO.getByPk(id);
	}
	
	public Cliente getClientePK(int id) {
		return clienteDAO.getByPk(id);
	}
	
	public Area getAreaPK(int id) {
		return areaDAO.getByPk(id);
	}
	
	public Produto getProdutoPK(int id) {
		return produtoDAO.getByPk(id);
	}
	
	public TipoAtividade getTipoAtividadePK(int id) {
		return tipoAtividadeDAO.getByPk(id);
	}
	
	public List<Projeto> getListaProjetosGenericos() {
		return projetoDAO.getProjetosGenericos(null);
	}
	
}
