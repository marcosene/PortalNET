/**
 * @since 29/05/2009
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.dao;

import org.springframework.stereotype.Repository;

import br.com.portalnet.model.Cliente;


@Repository("clienteDAO")
public class ClienteDAO extends GenericDAOImpl<Cliente, Integer> {

	public ClienteDAO() {
		super(Cliente.class);
	}
	

}
