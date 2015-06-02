/**
 * @since 13/10/2011
 * @author Thales Almeida
 */
package br.com.portalnet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ProdutoInventario implements Serializable, Comparable<ProdutoInventario> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	@Length(max=200)
	private String nomeProduto;
	
	private int categoria;
	
	private String especificacao;
		
  	private boolean flagEmprestavel;
	
	@OneToMany(mappedBy="produtoInventario", cascade=CascadeType.ALL)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<ItemInventario> listaItens;
	
	
	public ProdutoInventario() {
		nomeProduto = null;
		categoria = Tipos.TIPO_CATEG_INVENT_CD_DVD;
		flagEmprestavel = true;
		listaItens = new ArrayList<ItemInventario>();
	}

	public List<ItemInventario> getListaItens() {
		Collections.sort(listaItens);
		return listaItens;
	}

	public void setListaItens(List<ItemInventario> listaItens) {
		this.listaItens = listaItens;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto.trim();
	}

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	public String getEspecificacao() {
		return especificacao;
	}

	public void setEspecificacao(String especificacao) {
		this.especificacao = especificacao.trim();
	}

	public boolean isFlagEmprestavel() {
		return flagEmprestavel;
	}

	public void setFlagEmprestavel(boolean flagEmprestavel) {
		this.flagEmprestavel = flagEmprestavel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescTipoCategoriaInventario() {
		return Tipos.getTipoCategoriaInventario(categoria);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoInventario other = (ProdutoInventario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(ProdutoInventario o) {
		return this.getNomeProduto().compareToIgnoreCase(o.getNomeProduto());
	}
	
}
