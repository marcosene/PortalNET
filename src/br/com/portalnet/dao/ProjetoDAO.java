/**
 * @since 08/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import br.com.portalnet.model.Cliente;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Produto;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.Tipos;


@Repository("projetoDAO")
public class ProjetoDAO extends GenericDAOImpl<Projeto, Long> {

	public ProjetoDAO() {
		super(Projeto.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Projeto> getProjetos(final Produto produto, final boolean flagSoNaoGenericos,
								final boolean flagSoAtivos, final Colaborador colaborador) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Projeto> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				List<Projeto> listaProjetos;
				boolean flagCondicao = false;
				
				sb.append("select distinct projeto from Projeto projeto ");
				
				if (colaborador != null && colaborador.getTipoUsuario() != Tipos.TIPO_USUARIO_ADMIN) {
					
					if (colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_COORDENADOR) {
						sb.append("where (projeto.produto.coordenador.usuario = :usuario or projeto.produto.coordenador is null)");
					}
	
					if (colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_GPJ) {
						sb.append("where (projeto.gpj is not null and projeto.gpj.usuario = :usuario)");
					}
						
					if (colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_GPD) {
						sb.append("where (projeto.produto.gerenteProduto is not null and projeto.produto.gerenteProduto.usuario = :usuario)");
					}
					
					if (colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_GF) {
						sb.append("where (projeto.produto.area.gerenteFuncional is not null and projeto.produto.area.gerenteFuncional.usuario = :usuario)");
					}
					
					if (colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_GG && colaborador.getTipoAcesso() == Tipos.TIPO_ACESSO_EXTERNO) {
						sb.append("where (projeto.produto.area.cliente.gerenteGeral is not null and projeto.produto.area.cliente.gerenteGeral.usuario = :usuario)");
					}
					
					if (colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_GG && colaborador.getTipoAcesso() == Tipos.TIPO_ACESSO_INTERNO){
						sb.append("where projeto.produto.area.cliente.gerenteGeral is not null");
					}
										
					if (colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_COMUM) {
						sb.append("join projeto.gruposAtividades grupoAtividades ");
						sb.append("join grupoAtividades.atividades atividade ");
						sb.append("join atividade.alocacoes alocacao ");
						sb.append("where (alocacao.colaborador.usuario = :usuario ");
						sb.append("or projeto.responsavel.id = :colaborador_id) ");
					}
					
					flagCondicao = true;
				}
				
				if (produto != null) {
					sb.append((flagCondicao) ? " and " : " where ");
					sb.append("projeto.produto.id = " + produto.getId());
					flagCondicao = true;
				}

				if (flagSoNaoGenericos) {
					sb.append((flagCondicao) ? " and " : " where ");
					sb.append("projeto.generico = 0");
					flagCondicao = true;
				}

				if (flagSoAtivos) {
					sb.append((flagCondicao) ? " and " : " where ");
					sb.append("projeto.estado = " + Projeto.ESTADO_HABILITADO);
					flagCondicao = true;
				}

				sb.append(" order by projeto.estado asc, projeto.codigoCliente asc, projeto.codigoPacote asc");
				
				Query query = session.createQuery(sb.toString());
				
				if (
						(colaborador != null && colaborador.getTipoUsuario() != Tipos.TIPO_USUARIO_ADMIN) && 
						(colaborador.getTipoAcesso() == Tipos.TIPO_ACESSO_EXTERNO || colaborador.getTipoUsuario() != Tipos.TIPO_USUARIO_GG)
					)
				{					
					query.setString("usuario", colaborador.getUsuario());	
				}
				
				if (colaborador != null && colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_COMUM) {
					query.setInteger("colaborador_id", colaborador.getId());
				}
				
				query.setCacheable(true);
				listaProjetos = query.list();
				
				Collections.sort(listaProjetos);
				return listaProjetos;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Projeto> getProjetosGenericos(final Colaborador colaborador) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			List<Projeto> unionProjetos = new ArrayList<Projeto>();
			public List<Projeto> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb1 = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				
				if (colaborador != null){
					sb1.append("from Projeto projeto ");
					sb1.append("where projeto.generico = 1");
					sb1.append(" and (projeto.produto.coordenador.usuario = :usuario)");
					sb1.append(" and projeto.estado = " + Projeto.ESTADO_HABILITADO);
					sb1.append(" order by projeto.codigoCliente");
					Query query1 = session.createQuery(sb1.toString());
					query1.setString("usuario", colaborador.getUsuario());	
					query1.setCacheable(true);
					unionProjetos.addAll(query1.list());
					
					/* Ha um bug no hibernate que nao permite "
					 * portanto foi necessario efetuar 2 querys e somar as listas no retorno
					 * */ 
					sb2.append("from Projeto projeto ");
					sb2.append("where projeto.generico = 1");
					sb2.append(" and (projeto.produto.coordenador is null)");
					sb2.append(" and projeto.estado = " + Projeto.ESTADO_HABILITADO);
					sb2.append(" order by projeto.codigoCliente");
					
					Query query2 = session.createQuery(sb2.toString());
					query2.setCacheable(true);
					unionProjetos.addAll(query2.list());
					
					return unionProjetos;
				} else {
					//caso a funcao seja acessada pelo Admin
					StringBuffer sb = new StringBuffer();
					sb.append("from Projeto projeto ");
					sb.append("where projeto.generico = 1");
					sb.append(" and projeto.estado = " + Projeto.ESTADO_HABILITADO);
					sb.append(" order by projeto.codigoCliente");
					
					Query query = session.createQuery(sb.toString());
					query.setCacheable(true);
					return query.list();
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Projeto> getProjetosComLancamentos(final Cliente cliente, final Calendar dataInicio, final Calendar dataFim) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Projeto> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				
				sb.append("select distinct projeto from Projeto projeto ");
				sb.append("join projeto.gruposAtividades grupoAtividades ");
				sb.append("join grupoAtividades.atividades atividade ");
				sb.append("join atividade.alocacoes alocacao ");
				sb.append("join alocacao.registros registro ");
				sb.append("where projeto.produto.area.cliente.id = " + cliente.getId());
				sb.append(" AND dataTrabalho >= :dataInicio AND dataTrabalho <= :dataFim"); 
				sb.append(" order by projeto.codigoCliente asc, projeto.codigoPacote asc");
				
				Query query = session.createQuery(sb.toString());
				query.setCalendar("dataInicio", dataInicio);
				query.setCalendar("dataFim", dataFim);
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Projeto> getTodosProjetosAlocados(final String usuario) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Projeto> doInHibernate(Session session)
				throws HibernateException, SQLException {

				StringBuffer sb = new StringBuffer();
				sb.append("select distinct projeto from Projeto projeto ");
				sb.append("join projeto.gruposAtividades grupoAtividades ");
				sb.append("join grupoAtividades.atividades atividade ");
				sb.append("join atividade.alocacoes alocacao ");
				sb.append("where alocacao.colaborador.usuario = :usuario");
				sb.append(" order by projeto.codigoCliente asc, projeto.codigoPacote asc");
				
				Query query = session.createQuery(sb.toString());
				query.setString("usuario", usuario);
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Projeto> getProjetosAlocadosAtivos(final String usuario) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Projeto> doInHibernate(Session session)
				throws HibernateException, SQLException {

				StringBuffer sb = new StringBuffer();
				sb.append("select distinct projeto from Projeto projeto ");
				sb.append("join projeto.gruposAtividades grupoAtividades ");
				sb.append("join grupoAtividades.atividades atividade ");
				sb.append("join atividade.alocacoes alocacao ");
				sb.append("where projeto.estado = " + Projeto.ESTADO_HABILITADO);
				sb.append(" and alocacao.colaborador.usuario = :usuario");
				sb.append(" and alocacao.flagBloqueadoRegistro != 1");
				sb.append(" and (projeto.generico = 1 or alocacao.qtdeHorasRestantes > 0)");
				sb.append(" order by projeto.codigoCliente asc, projeto.codigoPacote asc");
				
				Query query = session.createQuery(sb.toString());
				query.setString("usuario", usuario);
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
}
