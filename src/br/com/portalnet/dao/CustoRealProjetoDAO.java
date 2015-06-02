/**
 * @since 11/12/2008
 * @author Gustavo de Mello Mascarin
 */
package br.com.portalnet.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import br.com.portalnet.model.CustoRealProjeto;


@Repository("custoRealProjetoDAO")
public class CustoRealProjetoDAO extends GenericDAOImpl<CustoRealProjeto, Integer> {

	public CustoRealProjetoDAO() {
		super(CustoRealProjeto.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<CustoRealProjeto> getlistaCustosReaisProjeto(final int mes, final int ano) {
		
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<CustoRealProjeto> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from CustoRealProjeto custo ");
				sb.append("where custo.ano = " + ano);
				sb.append(" and custo.mes = " + mes);
				sb.append(" order by custo.mes");
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				return query.list();
			}
		});
	}

}
