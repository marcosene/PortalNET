/**
 * @since 08/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.control;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.portalnet.model.Cliente;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.CustoRealProjeto;
import br.com.portalnet.model.Equipe;
import br.com.portalnet.model.Ferias;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.Tipos;


@Service("gestorService")
@Transactional(readOnly = true)
public class GestorServiceController extends ColaboradorServiceController {
	
	@Transactional(readOnly = false)
	public void cadastrarProjeto(Projeto projeto) throws DataIntegrityViolationException {
		if (projeto.getId() == null) {
			projetoDAO.save(projeto);
		} else {
			projetoDAO.merge(projeto);
		}
	}
	
	@Transactional(readOnly = false)
	public void cadastrarFerias(Ferias ferias) throws DataIntegrityViolationException {
		if (ferias.getId() == null) {
			feriasDAO.save(ferias);
		} else {
			feriasDAO.merge(ferias);
		}
	}
	
	@Transactional(readOnly = false)
	public void cadastrarEquipe(Equipe equipe) throws DataIntegrityViolationException {
		if (equipe.getId() == null) {
			equipeDAO.save(equipe);
		} else {
			equipeDAO.merge(equipe);
		}
	}

	@Transactional(readOnly = false)
	public void cadastrarCustoRealProjeto(CustoRealProjeto custoRealProjeto) throws DataIntegrityViolationException {
		if (custoRealProjeto.getId() == 0) {
			custoRealProjetoDAO.save(custoRealProjeto);
		} else {
			custoRealProjetoDAO.merge(custoRealProjeto);
		}
	}
	
	@Transactional(readOnly = false)
	public void excluirProjeto(Projeto projeto) {
		projetoDAO.delete(projeto);
	}
	
	@Transactional(readOnly = false)
	public void excluirFerias(Ferias ferias){
		feriasDAO.delete(ferias);
	}
	
	@Transactional(readOnly = false)
	public void excluirEquipe(Equipe equipe) {
		equipeDAO.delete(equipe);
	}
	
	@Transactional(readOnly = false)
	public void excluirCustoRealProjeto(CustoRealProjeto custoRealProjeto) {
		custoRealProjetoDAO.delete(custoRealProjeto);
	}

	public List<Colaborador> getListaColaboradoresCoordenadores() {
		int[] listaTipoUsuarios = new int[] {
			Tipos.TIPO_USUARIO_COORDENADOR,
			Tipos.TIPO_USUARIO_GG
		};
		
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradores(listaTipoUsuarios, false);
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}
	
	public List<Colaborador> getListaColaboradoresInternos() {			
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradoresInternos(false); 
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}
	
	public List<Colaborador> getListaColaboradoresGGs() {
		int[] listaTipoUsuarios = new int[] {
			Tipos.TIPO_USUARIO_GG
		};
		
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradores(listaTipoUsuarios, false);
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}
	
	public List<Colaborador> getListaColaboradoresGFs() {
		int[] listaTipoUsuarios = new int[] {
			Tipos.TIPO_USUARIO_GG,
			Tipos.TIPO_USUARIO_GF
		};
			
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradores(listaTipoUsuarios, false);
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}
	
	public List<Colaborador> getListaColaboradoresGPDs() {
		int[] listaTipoUsuarios = new int[] {
			Tipos.TIPO_USUARIO_GG,
			Tipos.TIPO_USUARIO_GF,
			Tipos.TIPO_USUARIO_GPD
		};
			
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradores(listaTipoUsuarios, false);
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}
	
	public List<Colaborador> getListaColaboradoresGPJs() {
		int[] listaTipoUsuarios = new int[] {
			Tipos.TIPO_USUARIO_GPJ
		};
		
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradores(listaTipoUsuarios, false);
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}
	
	public List<Colaborador> getListaTodosColaboradoresGPJs() {
		int[] listaTipoUsuarios = new int[] {
			Tipos.TIPO_USUARIO_GPJ
		};
		
		// flag TRUE por parametro para receber TODOS os colaboradores, mesmo os inativos
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradores(listaTipoUsuarios, true);
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}
	
	public List<Colaborador> getListaColaboradoresComCompromissos() {
		int[] listaTipoUsuarios = new int[] {
			Tipos.TIPO_USUARIO_COMUM,
			Tipos.TIPO_USUARIO_COORDENADOR,
			Tipos.TIPO_USUARIO_GPJ
		};
		
		List<Colaborador> listaColaboradores = colaboradorDAO.getColaboradores(listaTipoUsuarios, false);
		Collections.sort(listaColaboradores);
		return listaColaboradores;
	}

	public List<Projeto> getListaProjetosComLancamentos(Cliente cliente, Calendar dataInicio, Calendar dataFim) {
		return projetoDAO.getProjetosComLancamentos(cliente, dataInicio, dataFim);
	}
	
	public Equipe getEquipePK(int id) {
		return equipeDAO.getByPk(id);
	}
	
	public Ferias getFeriasPK(int id) {
		return feriasDAO.getByPk(id);
	}
	
	public CustoRealProjeto getCustoRealProjetoPK(int id){
		return custoRealProjetoDAO.getByPk(id);
	}
	
	public List<Projeto> getListaMeusProjetosGenericos(Colaborador colaborador) {
		return projetoDAO.getProjetosGenericos(colaborador);
	}

}