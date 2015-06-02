package br.com.portalnet.dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.MuralRecados;


@Repository("muralRecadosDAO")
public class MuralRecadosDAO extends GenericDAOImpl<MuralRecados, Long> {
	
	public static int PERIODO_EXIBICAO_RECADOS = 30;
	

	public MuralRecadosDAO() {
		super(MuralRecados.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<MuralRecados> getRecados(final boolean flagAssuntoTrabalho) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<MuralRecados> doInHibernate(Session session)
				throws HibernateException, SQLException {
				final Calendar dataRef = Calendar.getInstance();
				dataRef.add(Calendar.DAY_OF_YEAR, -PERIODO_EXIBICAO_RECADOS);
				
				StringBuffer sb = new StringBuffer();
				sb.append("from MuralRecados muralrecados ");
				sb.append("where muralrecados.flagAssuntoTrabalho = " + flagAssuntoTrabalho);
				sb.append(" and dataRegistro >= :dataRef");
				sb.append(" and muralrecados.colaboradorDest = null ");
				sb.append("order by dataRegistro desc");
				
				Query query = session.createQuery(sb.toString());
				query.setCalendar("dataRef", dataRef);
				return query.list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<MuralRecados> getRecadosParticulares(final Colaborador colaborador) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public List<MuralRecados> doInHibernate(Session session)
				throws HibernateException, SQLException {
				final Calendar dataRef = Calendar.getInstance();
				dataRef.add(Calendar.DAY_OF_YEAR, -PERIODO_EXIBICAO_RECADOS);

				StringBuffer sb = new StringBuffer();
				sb.append("from MuralRecados muralrecados ");
				sb.append("where dataRegistro >= :dataRef");
				sb.append(" and muralrecados.colaboradorDest.id = " + colaborador.getId());
				sb.append(" order by dataRegistro desc");
				
				Query query = session.createQuery(sb.toString());
				query.setCalendar("dataRef", dataRef);
				return query.list();
			}
		});
	}

}
