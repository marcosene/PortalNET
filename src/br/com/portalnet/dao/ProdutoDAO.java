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
import br.com.portalnet.model.Modulo;
import br.com.portalnet.model.Produto;


@Repository("produtoDAO")
public class ProdutoDAO extends GenericDAOImpl<Produto, Integer> {

	public ProdutoDAO() {
		super(Produto.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Produto> getProdutos(final Area area) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Produto> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from Produto produto ");
				sb.append("where produto.area.id = " + area.getId());
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Modulo> getModulos(final Produto produto) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Modulo> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from Modulo modulo ");
				sb.append("where modulo.produto.id = " + produto.getId());
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
}
