/**
 * @since 02/12/2008
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
public class Produto implements Serializable, Comparable<Produto> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	@Length(max=20)
	private String nome;
	
	private int tempoLimiteRegistro;
	
	@ManyToOne
  	@JoinColumn(name="Area_id", referencedColumnName="id")
  	private Area area;

	@ManyToOne
  	@JoinColumn(name="ColaboradorGPD_id", referencedColumnName="id")
  	private Colaborador gerenteProduto;
	
	@ManyToOne
  	@JoinColumn(name="ColaboradorCoord_id", referencedColumnName="id")
  	private Colaborador coordenador;
	
	@OneToMany(mappedBy="produto", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	private List<Modulo> modulos;
	
	@OneToMany(mappedBy="produto", cascade=CascadeType.ALL)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<Projeto> projetos;

	public Produto() {
		modulos = new ArrayList<Modulo>();
		projetos = new ArrayList<Projeto>();
		tempoLimiteRegistro = 5;
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

	public int getTempoLimiteRegistro() {
		return tempoLimiteRegistro;
	}

	public void setTempoLimiteRegistro(int tempoLimiteRegistro) {
		this.tempoLimiteRegistro = tempoLimiteRegistro;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Colaborador getGerenteProduto() {
		return gerenteProduto;
	}

	public void setGerenteProduto(Colaborador gerenteProduto) {
		this.gerenteProduto = gerenteProduto;
	}

	public Colaborador getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(Colaborador coordenador) {
		this.coordenador = coordenador;
	}

	public List<Modulo> getModulos() {
		Collections.sort(modulos);
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = null;
		this.modulos = modulos;
	}

	public List<Projeto> getProjetos() {
		Collections.sort(projetos);
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = null;
		this.projetos = projetos;
	}

	public void adicionarModulo(Modulo modulo) {
		this.removerModulo(modulo);
		this.getModulos().add(modulo);
	}
	
	public void removerModulo(Modulo modulo) {
		if (this.getModulos().contains(modulo)) {
			this.getModulos().remove(modulo);
		}
	}
	
	public void adicionarProjeto(Projeto projeto) {
		this.removerProjeto(projeto);
		this.getProjetos().add(projeto);
	}
	
	public void removerProjeto(Projeto projeto) {
		if (this.getProjetos().contains(projeto)) {
			this.getProjetos().remove(projeto);
		}
	}
	
	public List<Projeto> getProjetosNaoGenericos() {
		List<Projeto> listaProjetosNaoGenericos = new ArrayList<Projeto>();
		
		for (Projeto projeto : getProjetos()) {
			if (!projeto.isGenerico()) {
				listaProjetosNaoGenericos.add(projeto);
			}
		}
		
		return listaProjetosNaoGenericos;
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(Produto o) {
		if (this.getNome().compareToIgnoreCase(o.getNome()) == 0) {
			return this.getArea().compareTo(o.getArea());
		}
		return this.getNome().compareToIgnoreCase(o.getNome());
	}

}
