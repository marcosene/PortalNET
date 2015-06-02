/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.RegistroAtividade;
import br.com.portalnet.model.Tipos;


@Repository("colaboradorDAO")
public class ColaboradorDAO extends GenericDAOImpl<Colaborador, Integer> {

	public ColaboradorDAO() {
		super(Colaborador.class);
	}

	public Colaborador getColaborador(final String usuario) {
		return (Colaborador) this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException {

				StringBuffer sb = new StringBuffer();
				sb.append("from Colaborador colaborador ");
				sb.append("where colaborador.usuario = :usuario");

				Query query = session.createQuery(sb.toString());
				query.setString("usuario", usuario);
				return query.uniqueResult();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Colaborador> getColaboradores(final int[] listaTipoUsuarios, final boolean todos) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Colaborador> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from Colaborador colaborador ");
				if (!todos){
					sb.append("where colaborador.flagAtivo = 1");
				}
				if (listaTipoUsuarios != null && listaTipoUsuarios.length != 0) {
					if (!todos){
						sb.append(" and (colaborador.tipoUsuario = " + listaTipoUsuarios[0]);
					}
					else{
						sb.append("where (colaborador.tipoUsuario = " + listaTipoUsuarios[0]);
					}
					for(int i = 1; i < listaTipoUsuarios.length; i++) {
						sb.append(" or colaborador.tipoUsuario = " + listaTipoUsuarios[i]);
					}
					
					sb.append(")");
				}
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Colaborador> getColaboradoresInternos(final boolean todos) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Colaborador> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from Colaborador colaborador");
				if (!todos) {
					sb.append(" where colaborador.flagAtivo = 1");
					sb.append(" and colaborador.tipoAcesso = " + Tipos.TIPO_ACESSO_INTERNO);
				}
				else {
					sb.append(" where colaborador.tipoAcesso = " + Tipos.TIPO_ACESSO_INTERNO);
				}
				sb.append(" and colaborador.tipoUsuario != " + Tipos.TIPO_USUARIO_ADMIN);
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				return query.list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<Colaborador> getAniversariantes() {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Colaborador> doInHibernate(Session session)
				throws HibernateException, SQLException {
				List<Colaborador> listaAniversariantes;
				int mesAtual = Calendar.getInstance().get(Calendar.MONTH)+1;
				int proximoMes = Calendar.getInstance().get(Calendar.MONTH)+2;
				
				if (mesAtual == 12) {
					proximoMes = 1;
				}

				StringBuffer sb = new StringBuffer();
				sb.append("from Colaborador colaborador ");
				sb.append("where colaborador.flagAtivo = 1");
				sb.append(" and colaborador.tipoUsuario != " + Tipos.TIPO_USUARIO_ADMIN);
				sb.append(" and colaborador.tipoAcesso = " + Tipos.TIPO_ACESSO_INTERNO);
				sb.append(" and (colaborador.mesAniversario = " + mesAtual);
				sb.append(" or colaborador.mesAniversario = " + proximoMes);
				sb.append(" )");
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				listaAniversariantes = query.list();
				
				Collections.sort(listaAniversariantes, new Comparator<Colaborador>() {
					public int compare(Colaborador c1, Colaborador c2) {
						int ano1, ano2;
						ano1 = ano2 = Calendar.getInstance().get(Calendar.YEAR);
						
						if (c1.getMesAniversario() == 1 && c2.getMesAniversario() == 12)
							ano1++;
						
						if (c1.getMesAniversario() == 12 && c2.getMesAniversario() == 1)
							ano2++;
						
						String aniversario1 = ano1 + String.format("%02d", c1.getMesAniversario()) + String.format("%02d", c1.getDiaAniversario());
						String aniversario2 = ano2 + String.format("%02d", c2.getMesAniversario()) + String.format("%02d", c2.getDiaAniversario());
						
						return aniversario1.compareTo(aniversario2);
					}
				});
				
				return listaAniversariantes;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<RegistroAtividade> getRegistrosPorPeriodo(final String usuario, final Calendar dataInicial, final Calendar dataFinal) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<RegistroAtividade> doInHibernate(Session session)
				throws HibernateException, SQLException {

				StringBuffer sb = new StringBuffer();
				sb.append("from RegistroAtividade registro ");
				sb.append("where registro.dataTrabalho >= :dataInicial ");
				sb.append("and registro.dataTrabalho <= :dataFinal ");
				sb.append("and registro.alocacao.colaborador.usuario = :usuario ");
				sb.append("order by horaInicial");
				
				Query query = session.createQuery(sb.toString());
				query.setString("usuario", usuario);
				query.setCalendar("dataInicial", dataInicial);
				query.setCalendar("dataFinal", dataFinal);
				query.setCacheable(true);
				return query.list();
			}
		});
	}

}

