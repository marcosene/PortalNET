/**
 * @since 17/09/2010
 * @author Felipe Marques de Souza Falcão Gondim
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

import br.com.portalnet.model.Ferias;


@Repository("feriasDAO")
public class FeriasDAO  extends GenericDAOImpl<Ferias, Integer> {

	public FeriasDAO() {
		super(Ferias.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Ferias> getListaFerias(final int anoFerias) {
		final Calendar dataRef1 = new GregorianCalendar(anoFerias, 0, 1, 0, 0, 0);
		final Calendar dataRef2 = new GregorianCalendar(anoFerias, 11, 31, 23, 59, 59);
		
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Ferias> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from Ferias ferias ");
				sb.append("where ferias.dataInicioFerias >= :dataRef1 and ferias.dataInicioFerias <= :dataRef2");
				
				Query query = session.createQuery(sb.toString());
				query.setDate("dataRef1", dataRef1.getTime());
				query.setDate("dataRef2", dataRef2.getTime()); 
				query.setCacheable(true);
				return query.list();
			}
		});
	}

}
