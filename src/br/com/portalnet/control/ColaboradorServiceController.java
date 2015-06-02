/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.portalnet.model.AlocacaoAtividade;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Compromisso;
import br.com.portalnet.model.Feriados;
import br.com.portalnet.model.FraseDia;
import br.com.portalnet.model.MuralRecados;
import br.com.portalnet.model.Ocorrencia;
import br.com.portalnet.model.ProdutoInventario;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.RegistroAtividade;
import br.com.portalnet.model.Tipos;
import br.com.portalnet.util.StringUtil;


@Service("colaboradorService")
@Transactional(readOnly = true)
public class ColaboradorServiceController extends GenericServiceController {
	
	@Transactional(readOnly = false)
	public void cadastrarAlocacaoAtividade(AlocacaoAtividade alocacao) {
		if (alocacao.getId() == null) {
			alocacaoAtividadeDAO.save(alocacao);
		} else {
			alocacaoAtividadeDAO.merge(alocacao);
		}
	}
	
	@Transactional(readOnly = false)
	public void cadastrarMuralRecados(MuralRecados recado) {
		muralRecadosDAO.save(recado);
	}
	
	@Transactional(readOnly = false)
	public void cadastrarOcorrencia(Ocorrencia ocorrencia) throws DataIntegrityViolationException {
		if (ocorrencia.getId() == null) {
			ocorrenciaDAO.save(ocorrencia);
		} else {
			ocorrenciaDAO.merge(ocorrencia);
		}
	}

	@Transactional(readOnly = false)
	public void cadastrarFraseDia(FraseDia fraseDia) {
		fraseDiaDAO.save(fraseDia);
	}
	
	@Transactional(readOnly = false)
	public void excluirAlocacaoAtividade(AlocacaoAtividade alocacao) {
		alocacaoAtividadeDAO.delete(alocacao);
	}
	
	@Transactional(readOnly = false)
	public void excluirMuralRecados(MuralRecados recado) {
		muralRecadosDAO.delete(recado);
	}
	
	@Transactional(readOnly = false)
	public void excluirOcorrencia(Ocorrencia ocorrencia) {
		ocorrenciaDAO.delete(ocorrencia);
	}
	
	@Transactional(readOnly = false)
	public void excluirFraseDia(FraseDia fraseDia) {
		fraseDiaDAO.delete(fraseDia);
	}
	
	@Transactional(readOnly = false)
	public void atualizarColaborador(Colaborador colaborador) {
		if (colaborador.getId() != null) {
			colaboradorDAO.merge(colaborador);
		}
	}

	@Transactional(readOnly = false)
	public void atualizarCompromisso(Compromisso compromisso){
		if (compromisso.getId() != null) {
			compromissoDAO.merge(compromisso);
		}
	}
	
	@Transactional(readOnly = false)
	public void atualizarProdutoInventario(ProdutoInventario produto) {
		produtoInventarioDAO.merge(produto);
	}

	public Colaborador autenticaUsuario(String usuario, String senha) {
		Colaborador colaboradorAux = colaboradorDAO.getColaborador(usuario);
		
		if (colaboradorAux == null || !colaboradorAux.getSenha().equals(senha)) {
			return null;
		}
		
		return colaboradorAux;
	}
	
	public List<Colaborador> getListaColaboradores(boolean ativos) {
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradores(null, ativos);
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}
	
	public List<Colaborador> getListaColaboradoresParticipativos() {
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradoresInternos(false);
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}
	
	public List<Colaborador> getListaTodosColaboradoresParticipativos() {
		// flag TRUE por parametro para receber TODOS os colaboradores, mesmo os inativos
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradoresInternos(true);
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}
		
	public List<Colaborador> getListaAniversariantes() {
		return colaboradorDAO.getAniversariantes();
	}
	
	public List<Feriados> getListaFeriados() {
		return feriadoDAO.getFeriados();
	}
	
	public int getQtdeRecadosTrabalho() {
		return muralRecadosDAO.getRecados(true).size();
	}
	
	public List<MuralRecados> getListaRecadosTrabalho() {
		return muralRecadosDAO.getRecados(true);
	}
	
	public int getQtdeRecadosCasuais() {
		return muralRecadosDAO.getRecados(false).size();
	}
	
	public List<MuralRecados> getListaRecadosCasuais() {
		return muralRecadosDAO.getRecados(false);
	}
	
	public int getQtdeRecadosParticulares(Colaborador colaborador) {
		return muralRecadosDAO.getRecadosParticulares(colaborador).size();
	}
	
	public List<MuralRecados> getListaRecadosParticulares(Colaborador colaborador) {
		return muralRecadosDAO.getRecadosParticulares(colaborador);
	}
	
	public List<Projeto> getListaTodosProjetosAlocados(Colaborador colaborador) {
		return projetoDAO.getTodosProjetosAlocados(colaborador.getUsuario());
	}
	
	public List<Projeto> getListaProjetosAlocadosAtivos(Colaborador colaborador) {
		return projetoDAO.getProjetosAlocadosAtivos(colaborador.getUsuario());
	}
	
	public List<RegistroAtividade> getListaRegistrosPorData(Colaborador colaborador, Calendar dataTrabalho) {
		return colaboradorDAO.getRegistrosPorPeriodo(colaborador.getUsuario(), dataTrabalho, dataTrabalho);
	}
	
	public List<RegistroAtividade> getListaRegistrosPorPeriodo(Colaborador colaborador, Calendar dataInicial, Calendar dataFinal) {
		return colaboradorDAO.getRegistrosPorPeriodo(colaborador.getUsuario(), dataInicial, dataFinal);
	}
	
	public MuralRecados getMuralPK(long recado_id) {
		return muralRecadosDAO.getByPk(recado_id);
	}
	
	public List<Compromisso> getListaCompromissosRecentes(Colaborador colaborador) {
		return compromissoDAO.getListaCompromissosRecentes(colaborador);
	}

	public List<Projeto> getListaMeusProjetos(Colaborador colaborador) {
		if (colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_COORDENADOR) {
			List<Projeto> listaProjetos = projetoDAO.getProjetos(null, true, true, colaborador);
			List<Projeto> listaProjetosAlocados = projetoDAO.getProjetosAlocadosAtivos(colaborador.getUsuario());
			
			for (Projeto projetoAlocado : listaProjetosAlocados) {
				if (!projetoAlocado.isGenerico() && !listaProjetos.contains(projetoAlocado))
					listaProjetos.add(projetoAlocado);
			}
			
			return listaProjetos;
		} else {
			return projetoDAO.getProjetos(null, true, true, colaborador);
		}
		
	}

	public Projeto getProjetoPK(long codProjeto) {
		return projetoDAO.getByPk(codProjeto);
	}
	
	public void initializeObject(Object object) {
		colaboradorDAO.initialize(object);
	}
	
	public Compromisso getCompromissoPK(long codCompromisso) {
		return compromissoDAO.getByPk(codCompromisso);
	}
	
	public Ocorrencia getOcorrenciaPK(long idOcorrencia) {
		return ocorrenciaDAO.getByPk(idOcorrencia);
	}
	
	/**
	 * Retorna as ocorrencias, dadas determinadas palavras chaves no campo de busca
	 * @param palavrasChave
	 * @return
	 */
	public List<Ocorrencia> getPesquisaOcorrencias(String[] palavrasChave) {
		String[] palavrasChaveHtml;
		palavrasChaveHtml = palavrasChave.clone();
		
		//Troca as acentuacoes para o padrao html para que a busca possa ser feita nos campos que utilizam esse padrao
		StringUtil.substituirAcentuacaoHtml(palavrasChaveHtml);
		
		List<Ocorrencia> Ids = ocorrenciaDAO.getPesquisaOcorrencias(palavrasChave, palavrasChaveHtml);
		List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
		
		for(Ocorrencia ocorrencia : Ids){
			ocorrencia = ocorrenciaDAO.getByPk(ocorrencia.getId());
			if(!ocorrencias.contains(ocorrencia))
				ocorrencias.add(ocorrencia);
		}
		return ocorrencias; 
	}
	
	public List<Ocorrencia> getListaOcorrenciasAtivas(Projeto projeto) {
		List<Ocorrencia> listaOcorrencias;
		listaOcorrencias = ocorrenciaDAO.getOcorrencias(projeto, Tipos.TIPO_STATUS_OCORRENCIA_ABERTA);
		listaOcorrencias.addAll(ocorrenciaDAO.getOcorrencias(projeto, Tipos.TIPO_STATUS_OCORRENCIA_ANALISADA));
		listaOcorrencias.addAll(ocorrenciaDAO.getOcorrencias(projeto, Tipos.TIPO_STATUS_OCORRENCIA_EM_ANALISE));
		listaOcorrencias.addAll(ocorrenciaDAO.getOcorrencias(projeto, Tipos.TIPO_STATUS_OCORRENCIA_PENDENTE));
		
		Collections.sort(listaOcorrencias);
		return listaOcorrencias;
	}
	
	public List<Ocorrencia> getListaMinhasOcorrencias(Colaborador colaborador) {
		return ocorrenciaDAO.getMinhasOcorrencias(colaborador);
	}
	
	public List<ProdutoInventario> getTodosProdutosInventario(int categoria) {
		return produtoInventarioDAO.getTodosProdutosInventario(categoria);
	}
	
	public List<ProdutoInventario> getProdutosInventarioEmprestaveis(int categoria) {
		return produtoInventarioDAO.getProdutosInventarioEmprestaveis(categoria);
	}
	
	public List<Colaborador> getListaColaboradoresParticipativosNenhum() {
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradoresInternos(false);
		Collections.sort(listaColaboradores);
		listaColaboradores.add(null);
		return listaColaboradores;
	}

	public FraseDia getFraseDiaPK(long idFraseDia) {
		return fraseDiaDAO.getByPk(idFraseDia);
	}
	
	public FraseDia getFraseDiaAleatoria() {
		return fraseDiaDAO.getFraseDiaAleatoria();
	}

	public List<Compromisso> getTodosCompromissosColaborador(Colaborador colaborador) {
		List<Compromisso> listaCompromissos = compromissoDAO.getTodosCompromissosColaborador(colaborador);
		Collections.sort(listaCompromissos);
		return listaCompromissos;
	}

}
