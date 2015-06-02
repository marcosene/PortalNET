/**
 * @since 13/05/2009
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
import org.hibernate.validator.constraints.Length;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ObservacoesProjeto implements Serializable, Comparable<ObservacoesProjeto> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Calendar dataInclusao;
	
	@NotNull
	@Length(max=512)
	private String observacoes;
	
	private Integer tipo;
	
	private boolean flagVisibilidadeRestrita;
	
	@ManyToOne
  	@JoinColumn(name="Projeto_id", referencedColumnName="id")
  	private Projeto projeto;

	
	public ObservacoesProjeto() {
		tipo = Tipos.TIPO_OBSERVACAO_NORMAL;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Calendar dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	
	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String obervacoes) {
		this.observacoes = obervacoes;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public int compareTo(ObservacoesProjeto o) {
		if (this.getDataInclusao().equals(o.getDataInclusao()))
			return this.getProjeto().compareTo(o.getProjeto());
		else
			return this.getDataInclusao().compareTo(o.getDataInclusao());
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Integer getTipo() {
		return tipo;
	}
	
	public void setFlagVisibilidadeRestrita(boolean flagVisibilidadeRestrita) {
		this.flagVisibilidadeRestrita = flagVisibilidadeRestrita;
	}
	
	public boolean isFlagVisibilidadeRestrita() {
		return flagVisibilidadeRestrita;
	}
	
}
