/**
 * @since 19/01/2010
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Ocorrencia;
import br.com.portalnet.model.Produto;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.Tipos;


@Repository("ocorrenciaDAO")
public class OcorrenciaDAO extends GenericDAOImpl<Ocorrencia, Long> {

	public OcorrenciaDAO() {
		super(Ocorrencia.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Ocorrencia> getOcorrencias(final Projeto projeto, final int statusSelecionado) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Ocorrencia> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from Ocorrencia ocorrencia ");
				sb.append("where ocorrencia.projeto.id = " + projeto.getId());
				if (statusSelecionado >= 0) {
					sb.append(" and ocorrencia.statusOcorrencia = " + statusSelecionado);
				}
				sb.append(" order by ocorrencia.dataAbertura desc");
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Ocorrencia> getOcorrencias(final Produto produto, final int statusSelecionado) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Ocorrencia> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from Ocorrencia ocorrencia ");
				sb.append("where ocorrencia.projeto.produto.id = " + produto.getId());
				if (statusSelecionado >= 0) {
					sb.append(" and ocorrencia.statusOcorrencia = " + statusSelecionado);
				}
				sb.append(" order by ocorrencia.dataAbertura desc");
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Ocorrencia> getPesquisaOcorrencias(final String[] listaChaves, final String[] listaChavesHtml) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Ocorrencia> doInHibernate(Session session)
				throws HibernateException, SQLException {
		
				StringBuffer sb = new StringBuffer();
				
				if(listaChaves.length <= 1){
					sb.append("select id from Ocorrencia ocorrencia ");
					sb.append("where (");
					
					for(int i = 0; i < listaChaves.length; i++) {
						sb.append("ocorrencia.numOcorrencia like '%" + listaChaves[i] + "%' or ");
						sb.append("ocorrencia.numBugCliente like '%" + listaChaves[i] + "%' or ");
						sb.append("(ocorrencia.titulo like '%" + listaChaves[i] + "%' or ocorrencia.titulo like '%" + listaChavesHtml[i] + "%') or ");
						sb.append("(ocorrencia.descricaoResumida like '%" + listaChaves[i] + "%' or ocorrencia.descricaoResumida like '%" + listaChavesHtml[i] + "%') or ");
						sb.append("(ocorrencia.textoPropostaSolucao like '%" + listaChaves[i] + "%' or ocorrencia.textoPropostaSolucao like '%" + listaChavesHtml[i] + "%') or ");
						sb.append("(ocorrencia.textoAnalise like '%" + listaChaves[i] + "%' or ocorrencia.textoAnalise like '%" + listaChavesHtml[i] + "%')");
						
						if (i == listaChaves.length - 1)
							sb.append(") order by ocorrencia.numOcorrencia");
						else
							sb.append(") and (");
					}
						
				}
				else{
					sb.append("select id from Ocorrencia ocorrencia ");
					sb.append("where (");
					
					for(int i = 0; i < listaChaves.length; i++) {
						sb.append("ocorrencia.numOcorrencia like '%" + listaChaves[i] + "%' or ");
						sb.append("ocorrencia.numBugCliente like '%" + listaChaves[i] + "%' or ");
						sb.append("(ocorrencia.titulo like '%" + listaChaves[i] + "%' or ocorrencia.titulo like '%" + listaChavesHtml[i] + "%') or ");
						sb.append("(ocorrencia.descricaoResumida like '%" + listaChaves[i] + "%' or ocorrencia.descricaoResumida like '%" + listaChavesHtml[i] + "%') or ");
						sb.append("(ocorrencia.textoPropostaSolucao like '%" + listaChaves[i] + "%' or ocorrencia.textoPropostaSolucao like '%" + listaChavesHtml[i] + "%') or ");
						sb.append("(ocorrencia.textoAnalise like '%" + listaChaves[i] + "%' or ocorrencia.textoAnalise like '%" + listaChavesHtml[i] + "%')");
						
						if (i == listaChaves.length - 1)
							sb.append(") ");
						else
							sb.append(") and (");
					}
					
					
					sb.append(" union all select id from Ocorrencia ocorrencia ");
					sb.append("where (");
					
					for(int i = 0; i < listaChaves.length; i++) {
						sb.append("ocorrencia.numOcorrencia like '%" + listaChaves[i] + "%' or ");
						sb.append("ocorrencia.numBugCliente like '%" + listaChaves[i] + "%' or ");
						sb.append("(ocorrencia.titulo like '%" + listaChaves[i] + "%' or ocorrencia.titulo like '%" + listaChavesHtml[i] + "%') or ");
						sb.append("(ocorrencia.descricaoResumida like '%" + listaChaves[i] + "%' or ocorrencia.descricaoResumida like '%" + listaChavesHtml[i] + "%') or ");
						sb.append("(ocorrencia.textoPropostaSolucao like '%" + listaChaves[i] + "%' or ocorrencia.textoPropostaSolucao like '%" + listaChavesHtml[i] + "%') or ");
						sb.append("(ocorrencia.textoAnalise like '%" + listaChaves[i] + "%' or ocorrencia.textoAnalise like '%" + listaChavesHtml[i] + "%')");
						
						if (i == listaChaves.length - 1)
							sb.append(") ");
						else
							sb.append(") or (");
					}
	
				}							

				SQLQuery query = session.createSQLQuery(sb.toString());
				query.addScalar("id", Hibernate.LONG);
				query.setResultTransformer(Transformers.aliasToBean(Ocorrencia.class));
				
				return query.list();
				
			}
		});
	}	
	
	@SuppressWarnings("unchecked")
	public List<Ocorrencia> getMinhasOcorrencias(final Colaborador colaborador) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Ocorrencia> doInHibernate(Session session)
				throws HibernateException, SQLException {

				StringBuffer sb = new StringBuffer();
				sb.append("from Ocorrencia ocorrencia ");
				sb.append("where (ocorrencia.statusOcorrencia = '" + Tipos.TIPO_STATUS_OCORRENCIA_ABERTA + "' or ocorrencia.statusOcorrencia = '" + Tipos.TIPO_STATUS_OCORRENCIA_EM_ANALISE + "' or ocorrencia.statusOcorrencia = '" + Tipos.TIPO_STATUS_OCORRENCIA_PENDENTE + "')");
				
				switch (colaborador.getTipoUsuario()) {
					case Tipos.TIPO_USUARIO_COORDENADOR:
						sb.append(" and (ocorrencia.projeto.produto.coordenador is not null and ocorrencia.projeto.produto.coordenador.usuario = :usuario)");
						break;

					case Tipos.TIPO_USUARIO_GPJ:
						sb.append(" and (ocorrencia.projeto.gpj is not null and ocorrencia.projeto.gpj.usuario = :usuario)");
						break;
					
					case Tipos.TIPO_USUARIO_GPD:
						sb.append(" and (ocorrencia.projeto.produto.gerenteProduto is not null and ocorrencia.projeto.produto.gerenteProduto.usuario = :usuario)");
						break;

					case Tipos.TIPO_USUARIO_GF:
						sb.append(" and (ocorrencia.projeto.produto.area.gerenteFuncional is not null and ocorrencia.projeto.produto.area.gerenteFuncional.usuario = :usuario)");
						break;

					case Tipos.TIPO_USUARIO_GG:
						sb.append(" and (ocorrencia.projeto.produto.area.cliente.gerenteGeral is not null and ocorrencia.projeto.produto.area.cliente.gerenteGeral.usuario = :usuario)");
						break;
					
					default:
						sb.append(" and ocorrencia.responsavel.usuario = :usuario");
				}
				
				sb.append(" order by ocorrencia.projeto.codigoCliente, ocorrencia.numOcorrencia");
				
				Query query = session.createQuery(sb.toString());
				query.setString("usuario", colaborador.getUsuario());
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
}

