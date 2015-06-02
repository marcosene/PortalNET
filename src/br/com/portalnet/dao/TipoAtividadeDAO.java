/**
 * @since 11/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import org.springframework.stereotype.Repository;

import br.com.portalnet.model.TipoAtividade;


@Repository("tipoAtividadeDAO")
public class TipoAtividadeDAO extends GenericDAOImpl<TipoAtividade, Integer> {

	public TipoAtividadeDAO() {
		super(TipoAtividade.class);
	}

}
