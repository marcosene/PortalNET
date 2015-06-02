/**
 * @since 26/10/2011
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.FraseDia;
import br.com.portalnet.model.Tipos;


@Repository("fraseDiaDAO")
public class FraseDiaDAO extends GenericDAOImpl<FraseDia, Long> {

	public FraseDiaDAO() {
		super(FraseDia.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<FraseDia> getFrasesDia(final Colaborador colaborador) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<FraseDia> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append("from FraseDia fraseDia ");
				
				if (colaborador.getTipoUsuario() != Tipos.TIPO_USUARIO_ADMIN)
					sb.append("where colaboradorEnvio.id = " + colaborador.getId());

				sb.append(" order by dataRegistro desc");
				
				Query query = session.createQuery(sb.toString());
				return query.list();
			}
		});
	}
	
	public FraseDia getFraseDiaAleatoria() {
		return (FraseDia) this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(FraseDia.class);
				criteria.add(Restrictions.sqlRestriction("1=1 order by newid()")); 
				criteria.setMaxResults(1); 
				return criteria.uniqueResult();
			}
		});
	}

}
