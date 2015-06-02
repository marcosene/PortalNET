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
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MuralRecados implements Serializable, Comparable<MuralRecados> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Length(max=64)
	private String assunto;
	
	@NotEmpty
	@Length(max=1024)
	private String descricao;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataRegistro;
	
	private boolean flagAssuntoTrabalho;
	
	@ManyToOne
  	@JoinColumn(name="ColaboradorAutor_id", referencedColumnName="id")
  	private Colaborador colaboradorAutor;
	
	@ManyToOne
  	@JoinColumn(name="ColaboradorDest_id", referencedColumnName="id")
  	private Colaborador colaboradorDest;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto.trim();
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.trim();
	}

	public Calendar getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Calendar dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public boolean isFlagAssuntoTrabalho() {
		return flagAssuntoTrabalho;
	}

	public void setFlagAssuntoTrabalho(boolean flagAssuntoTrabalho) {
		this.flagAssuntoTrabalho = flagAssuntoTrabalho;
	}

	public Colaborador getColaboradorAutor() {
		return colaboradorAutor;
	}

	public void setColaboradorAutor(Colaborador colaboradorAutor) {
		this.colaboradorAutor = colaboradorAutor;
	}

	public Colaborador getColaboradorDest() {
		return colaboradorDest;
	}

	public void setColaboradorDest(Colaborador colaboradorDest) {
		this.colaboradorDest = colaboradorDest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MuralRecados other = (MuralRecados) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(MuralRecados o) {
		return this.getDataRegistro().compareTo(o.getDataRegistro());
	}
	
}
