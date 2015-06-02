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

import br.com.portalnet.model.ProdutoInventario;


@Repository("produtoInventarioDAO")
public class ProdutoInventarioDAO extends GenericDAOImpl<ProdutoInventario, Integer> {

	public ProdutoInventarioDAO() {
		super(ProdutoInventario.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProdutoInventario> getTodosProdutosInventario(final int categoria) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<ProdutoInventario> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from ProdutoInventario produto");
				sb.append(" where produto.categoria = " + categoria);
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<ProdutoInventario> getProdutosInventarioEmprestaveis(final int categoria) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<ProdutoInventario> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from ProdutoInventario produto ");
				sb.append("where produto.flagEmprestavel = True and produto.categoria = " + categoria);
				Query query = session.createQuery(sb.toString());
				return query.list();
			}
		});
	}
}
