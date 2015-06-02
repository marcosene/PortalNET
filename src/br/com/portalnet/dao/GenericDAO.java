/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import java.io.Serializable;
import java.util.List;


public interface GenericDAO<T extends Serializable, PK extends Serializable> {
	
	Class<T> getEntityClass();
	
	T save(final T object);
	
	void saveOrUpdate(final T object);
	
	T merge(final T object);
	
	void update(final T object);
	
	void delete(final T object);
	
	void evict(final T object);
	
	T getByPk(final PK pk);
	
	List<T> listAll();

}
