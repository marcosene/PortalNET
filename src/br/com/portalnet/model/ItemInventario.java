/**
 * @since 14/10/2011
 * @author Thales Almeida
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemInventario implements Serializable, Comparable<ItemInventario> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	@Length(max=64)
	private String nroSerie;
	
	@Length(max=32)
	private String nroPatrimonio;
	
	@Length(max=32)
	private String nroNotaFiscal;
	
  	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataEmissaoNota;
  	
  	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataEmprestimo;

	@ManyToOne
  	@JoinColumn(name="ProdutoInventario_id", referencedColumnName="id")
  	private ProdutoInventario produtoInventario;

	@ManyToOne
  	@JoinColumn(name="Colaborador_id", referencedColumnName="id")
  	private Colaborador responsavel;
	
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNroSerie() {
		return nroSerie;
	}

	public void setNroSerie(String nroSerie) {
		this.nroSerie = nroSerie.trim();
	}

	public String getNroPatrimonio() {
		return nroPatrimonio;
	}

	public void setNroPatrimonio(String nroPatrimonio) {
		this.nroPatrimonio = nroPatrimonio.trim();
	}

	public String getNroNotaFiscal() {
		return nroNotaFiscal;
	}

	public void setNroNotaFiscal(String nroNotaFiscal) {
		this.nroNotaFiscal = nroNotaFiscal.trim();
	}

	public Calendar getDataEmissaoNota() {
		return dataEmissaoNota;
	}

	public void setDataEmissaoNota(Calendar dataEmissaoNota) {
		this.dataEmissaoNota = dataEmissaoNota;
	}

	public Calendar getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(Calendar dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public ProdutoInventario getProdutoInventario() {
		return produtoInventario;
	}

	public void setProdutoInventario(ProdutoInventario produtoInventario) {
		this.produtoInventario = produtoInventario;
	}

	public Colaborador getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Colaborador responsavel) {
		this.responsavel = responsavel;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemInventario other = (ItemInventario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(ItemInventario o) {
		if (this.getNroSerie().compareToIgnoreCase(o.getNroSerie()) == 0) {
			return this.getNroPatrimonio().compareTo(o.getNroPatrimonio());
		}
		return this.getNroSerie().compareToIgnoreCase(o.getNroSerie());
	}
}
