/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public abstract class GenericDAOImpl<T extends Serializable, PK extends Serializable>
		extends HibernateDaoSupport implements GenericDAO<T, PK> {

	private final Class<T> entityClass;

	public GenericDAOImpl(final Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public Class<T> getEntityClass() {
		return this.entityClass;
	}
	

	@SuppressWarnings("unchecked")
	public T save(T object) {
		T objReturn = (T) getHibernateTemplate().save(object);
		getHibernateTemplate().flush();
		return (T) objReturn;
	}

	public void saveOrUpdate(T object) {
		getHibernateTemplate().saveOrUpdate(object);
		getHibernateTemplate().flush();
	}

	public T merge(T object) {
		T objReturn =  getHibernateTemplate().merge(object);
		getHibernateTemplate().flush();
		return objReturn;
	}

	public void update(T object) {
		getHibernateTemplate().update(object);
		getHibernateTemplate().flush();
	}

	public void delete(T object) {
		getHibernateTemplate().delete(object);
		getHibernateTemplate().flush();
	}

	public void evict(T object) {
		getHibernateTemplate().evict(object);
		getHibernateTemplate().flush();
	}

	public void initialize(Object object) {
		getHibernateTemplate().refresh(object);
	}

	public T getByPk(PK primaryKey) {
		return (T) getHibernateTemplate().load(this.entityClass, primaryKey);
	}

	public List<T> listAll() {
		return getHibernateTemplate().loadAll(this.entityClass);
	}

	public Object getGenericObject(Class<?> classe, Serializable id) {
		return getHibernateTemplate().get(classe, id);
	}
	
}
