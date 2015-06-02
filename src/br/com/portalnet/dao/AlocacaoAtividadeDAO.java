/**
 * @since 15/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import org.springframework.stereotype.Repository;

import br.com.portalnet.model.AlocacaoAtividade;


@Repository("alocacaoAtividadeDAO")
public class AlocacaoAtividadeDAO extends GenericDAOImpl<AlocacaoAtividade, Long> {

	public AlocacaoAtividadeDAO() {
		super(AlocacaoAtividade.class);
	}
	
}
