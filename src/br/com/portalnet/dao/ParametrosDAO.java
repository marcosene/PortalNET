package br.com.portalnet.dao;

import org.springframework.stereotype.Repository;

import br.com.portalnet.model.Parametros;


@Repository("parametrosDAO")
public class ParametrosDAO extends GenericDAOImpl<Parametros, Integer> {

	public ParametrosDAO() {
		super(Parametros.class);
	}
	
}
