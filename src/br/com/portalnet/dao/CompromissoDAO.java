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

import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Compromisso;
import br.com.portalnet.model.Tipos;


@Repository("compromissoDAO")
public class CompromissoDAO extends GenericDAOImpl<Compromisso, Long> {

	public CompromissoDAO() {
		super(Compromisso.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Compromisso> getCompromissosEmail() {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Compromisso> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from Compromisso compromisso ");
				sb.append("where compromisso.flagEnvioEmail = 0 " );
				sb.append("and compromisso.flagConcluido = 0 " );
				
				Query query = session.createQuery(sb.toString());
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Compromisso> getListaCompromissosRecentes(Colaborador colaborador) {
		final String usuario;
		usuario = colaborador.getUsuario();
		final Calendar dataRef1 = Calendar.getInstance();
		
		dataRef1.add(Calendar.DAY_OF_YEAR, +7);
		if(colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_COMUM) {
			return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
				public List<Compromisso> doInHibernate(Session session)
					throws HibernateException, SQLException {
					StringBuffer sb = new StringBuffer();
					
					sb.append("select distinct compromisso from Compromisso compromisso  ");
					sb.append("join compromisso.projeto projeto ");
					sb.append("join compromisso.colaboradores responsavel ");
					sb.append("join projeto.gruposAtividades grupoAtividades ");
					sb.append("join grupoAtividades.atividades atividade ");
					sb.append("join atividade.alocacoes alocacao ");
					sb.append("where ((alocacao.colaborador.usuario = :usuario AND responsavel.tipoAcesso = " + Tipos.TIPO_ACESSO_EXTERNO + ")");
					sb.append(" OR responsavel.usuario = :usuario) AND compromisso.flagConcluido = FALSE AND (dataEvento < :dataRef1)"); 
					sb.append(" order by compromisso.dataEvento asc");
					
					Query query = session.createQuery(sb.toString());
					query.setString("usuario", usuario);
					query.setCalendar("dataRef1", dataRef1);
					query.setCacheable(true);
					return query.list();
				}
			});
		} else {
			return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
				public List<Compromisso> doInHibernate(Session session)
					throws HibernateException, SQLException {
					List<Compromisso> listaCompromissos = new ArrayList<Compromisso>();
					
					StringBuffer sb = new StringBuffer();
					sb.append("select compromisso from Compromisso compromisso  ");
					sb.append("where (compromisso.projeto.produto.coordenador is not null and compromisso.projeto.produto.coordenador.usuario = :usuario)");
					sb.append(" and (compromisso.flagConcluido = FALSE) AND (compromisso.dataEvento < :dataRef1)" );
					sb.append(" order by compromisso.dataEvento asc, compromisso.projeto.codigoCliente asc");
					
					Query query = session.createQuery(sb.toString());
					query.setString("usuario", usuario);
					query.setCalendar("dataRef1", dataRef1);
					query.setCacheable(true);
					
					for (Compromisso compromisso : (List<Compromisso>) query.list()) {
						if (!listaCompromissos.contains(compromisso))
							listaCompromissos.add(compromisso);
					}
					
					StringBuffer sb2 = new StringBuffer();
					sb2.append("select compromisso from Compromisso compromisso  ");
					sb2.append("join compromisso.colaboradores responsavel  ");
					sb2.append("where (responsavel.usuario = :usuario)");
					sb2.append(" and (compromisso.flagConcluido = FALSE) and (compromisso.dataEvento < :dataRef1)");
					sb2.append(" order by compromisso.dataEvento asc, compromisso.projeto.codigoCliente asc");
					
					Query query2 = session.createQuery(sb2.toString());
					query2.setString("usuario", usuario);
					query2.setCalendar("dataRef1", dataRef1);
					query2.setCacheable(true);
					
					for (Compromisso compromisso : (List<Compromisso>) query2.list()) {
						if (!listaCompromissos.contains(compromisso))
							listaCompromissos.add(compromisso);
					}
					
					Collections.sort(listaCompromissos);
					return listaCompromissos;
				}
			});
		}		
	}
	
	@SuppressWarnings("unchecked")
	public List<Compromisso> getTodosCompromissosColaborador(Colaborador colaborador) {
		final String usuario;
		usuario = colaborador.getUsuario();
		
		if(colaborador.getTipoUsuario() == Tipos.TIPO_USUARIO_COMUM) {
			return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
				public List<Compromisso> doInHibernate(Session session)
					throws HibernateException, SQLException {
					StringBuffer sb = new StringBuffer();
					
					sb.append("select distinct compromisso from Compromisso compromisso  ");
					sb.append("join compromisso.projeto projeto ");
					sb.append("join compromisso.colaboradores responsavel ");
					sb.append("join projeto.gruposAtividades grupoAtividades ");
					sb.append("join grupoAtividades.atividades atividade ");
					sb.append("join atividade.alocacoes alocacao ");
					sb.append("where ((alocacao.colaborador.usuario = :usuario AND responsavel.tipoAcesso = " + Tipos.TIPO_ACESSO_EXTERNO + ")");
					sb.append(" OR responsavel.usuario = :usuario) AND compromisso.flagConcluido = FALSE"); 
					sb.append(" order by compromisso.dataEvento asc");
					
					Query query = session.createQuery(sb.toString());
					query.setString("usuario", usuario);
					query.setCacheable(true);
					return query.list();
				}
			});
		} else {
			return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
				public List<Compromisso> doInHibernate(Session session)
					throws HibernateException, SQLException {
					List<Compromisso> listaCompromissos = new ArrayList<Compromisso>();
					
					StringBuffer sb = new StringBuffer();
					sb.append("select compromisso from Compromisso compromisso  ");
					sb.append("where (compromisso.projeto.produto.coordenador is not null and compromisso.projeto.produto.coordenador.usuario = :usuario)");
					sb.append(" and (compromisso.flagConcluido = FALSE)" );
					sb.append(" order by compromisso.dataEvento asc, compromisso.projeto.codigoCliente asc");
					
					Query query = session.createQuery(sb.toString());
					query.setString("usuario", usuario);
					query.setCacheable(true);
					
					for (Compromisso compromisso : (List<Compromisso>) query.list()) {
						if (!listaCompromissos.contains(compromisso))
							listaCompromissos.add(compromisso);
					}
					
					StringBuffer sb2 = new StringBuffer();
					sb2.append("select compromisso from Compromisso compromisso  ");
					sb2.append("join compromisso.colaboradores responsavel  ");
					sb2.append("where (responsavel.usuario = :usuario)");
					sb2.append(" and (compromisso.flagConcluido = FALSE)");
					sb2.append(" order by compromisso.dataEvento asc, compromisso.projeto.codigoCliente asc");
					
					Query query2 = session.createQuery(sb2.toString());
					query2.setString("usuario", usuario);
					query2.setCacheable(true);
					
					for (Compromisso compromisso : (List<Compromisso>) query2.list()) {
						if (!listaCompromissos.contains(compromisso))
							listaCompromissos.add(compromisso);
					}
					
					Collections.sort(listaCompromissos);
					return listaCompromissos;
				}
			});
		}
	}
	
}
