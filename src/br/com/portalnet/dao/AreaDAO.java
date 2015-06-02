/**
 * @since 11/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import br.com.portalnet.model.Area;
import br.com.portalnet.model.Cliente;


@Repository("areaDAO")
public class AreaDAO extends GenericDAOImpl<Area, Integer> {

	public AreaDAO() {
		super(Area.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Area> getAreas(final Cliente cliente) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Area> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from Area area ");
				sb.append("where area.cliente.id = " + cliente.getId());
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
}
