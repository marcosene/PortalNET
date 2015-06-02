/**
 * @since 11/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import br.com.portalnet.model.Feriados;


@Repository("feriadoDAO")
public class FeriadoDAO extends GenericDAOImpl<Feriados, Integer> {

	public FeriadoDAO() {
		super(Feriados.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Feriados> getFeriados() {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Feriados> doInHibernate(Session session)
				throws HibernateException, SQLException {
				Calendar dataAtual = Calendar.getInstance();
				final Calendar anoSeguinte = Calendar.getInstance();
				anoSeguinte.add(Calendar.DAY_OF_YEAR, + 366);
				
				StringBuffer sb = new StringBuffer();
				sb.append("from Feriados feriado ");
				sb.append("where (feriado.dataFeriado > :dataAtual");
				sb.append(" and feriado.dataFeriado < :anoSeguinte");
				sb.append(") order by feriado.dataFeriado");
				
				Query query = session.createQuery(sb.toString());
				query.setCalendar("anoSeguinte", anoSeguinte);
				query.setCalendar("dataAtual", dataAtual);
				
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Feriados> getListaFeriadosDoAno(int anoFeriados) {
		final Calendar dataRef1 = new GregorianCalendar(anoFeriados, 0, 1, 0, 0, 0);
		final Calendar dataRef2 = new GregorianCalendar(anoFeriados, 11, 31, 23, 59, 59);
		
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Feriados> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from Feriados feriado ");
				sb.append("where feriado.dataFeriado >= :dataRef1 and feriado.dataFeriado <= :dataRef2");
				
				Query query = session.createQuery(sb.toString());
				query.setDate("dataRef1", dataRef1.getTime());
				query.setDate("dataRef2", dataRef2.getTime()); 
				return query.list();
			}
		});
	}

}
