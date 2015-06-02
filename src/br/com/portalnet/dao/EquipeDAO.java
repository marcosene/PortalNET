/**
 * @since 08/12/2008
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

import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Equipe;


@Repository("equipeDAO")
public class EquipeDAO extends GenericDAOImpl<Equipe, Integer> {

	public EquipeDAO() {
		super(Equipe.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Equipe> getListaEquipes(final boolean flagSoAtivas, final Colaborador coordenador) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<Equipe> doInHibernate(Session session)
				throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				boolean flagCondicao = false;

				sb.append("from Equipe equipe where ");
				
				if (flagSoAtivas) {
					if (flagCondicao)
						sb.append(" and ");
					sb.append("equipe.flagAtiva = 1");
					flagCondicao = true;
				}

				if (coordenador != null) {
					if (flagCondicao)
						sb.append(" and ");
					sb.append("equipe.coordenador.id = " + coordenador.getId());
					flagCondicao = true;
				}

				if (!flagCondicao) {
					sb.delete(0, sb.length());
					sb.append("from Equipe equipe ");
				}
					
				sb.append(" order by equipe.flagAtiva asc, equipe.nome asc");
				
				Query query = session.createQuery(sb.toString());
				query.setCacheable(true);
				return query.list();
			}
		});
	}

}
