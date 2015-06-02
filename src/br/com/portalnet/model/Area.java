/**
 * @since 27/05/2009
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Area implements Serializable, Comparable<Area> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	@Length(max=20)
	private String nome;
	
	@ManyToOne
  	@JoinColumn(name="Cliente_id", referencedColumnName="id")
  	private Cliente cliente;
	
	@ManyToOne
  	@JoinColumn(name="ColaboradorGF_id", referencedColumnName="id")
  	private Colaborador gerenteFuncional;
	
	@OneToMany(mappedBy="area", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<Produto> produtos;
	
	
	public Area() {
		produtos = new ArrayList<Produto>();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.trim();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Colaborador getGerenteFuncional() {
		return gerenteFuncional;
	}

	public void setGerenteFuncional(Colaborador gerenteFuncional) {
		this.gerenteFuncional = gerenteFuncional;
	}

	public List<Produto> getProdutos() {
		Collections.sort(produtos);
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = null;
		this.produtos = produtos;
	}

	public void adicionarProduto(Produto produto) {
		this.removerProduto(produto);
		this.getProdutos().add(produto);
	}
	
	public void removerProduto(Produto produto) {
		if (this.getProdutos().contains(produto)) {
			this.getProdutos().remove(produto);
		}
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
		Area other = (Area) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(Area o) {
		if (this.getNome().compareToIgnoreCase(o.getNome()) == 0) {
			return this.getCliente().compareTo(o.getCliente());
		}
		
		return this.getNome().compareToIgnoreCase(o.getNome());
	}
	
}
