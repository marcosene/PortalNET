/**
 * @since 26/10/2011
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FraseDia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String frase;
	
	private String autor;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataRegistro;
	
	@ManyToOne
  	@JoinColumn(name="ColaboradorEnvio_id", referencedColumnName="id")
  	private Colaborador colaboradorEnvio;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFrase() {
		return frase;
	}

	public void setFrase(String frase) {
		this.frase = frase;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Calendar getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Calendar dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Colaborador getColaboradorEnvio() {
		return colaboradorEnvio;
	}

	public void setColaboradorEnvio(Colaborador colaboradorEnvio) {
		this.colaboradorEnvio = colaboradorEnvio;
	}

	
}
