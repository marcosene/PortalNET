/**
 * @since 29/12/2009
 * @author Sérgio Assunção
 */
package br.com.portalnet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Equipe implements Serializable, Comparable<Equipe> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	@Column(unique=true)
	@Length(max=25)
	private String nome;
	
	@Length(max=128)
	private String descricao;
	
	private boolean flagAtiva;
	
	private int tipoEquipe;
	
	@ManyToOne
  	@JoinColumn(name="Coordenador_id", referencedColumnName="id")
  	private Colaborador coordenador;
	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name="EquipeColaborador",
			joinColumns=@JoinColumn(name="Equipe_id"),
			inverseJoinColumns=@JoinColumn(name="Colaborador_id"))
	private List<Colaborador> colaboradores;
	
	public Equipe() {
		colaboradores = new ArrayList<Colaborador>();
		flagAtiva = true;
		tipoEquipe = Tipos.TIPO_EQUIPE_PERMANENTE;
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


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setFlagAtiva(boolean flagAtiva) {
		this.flagAtiva = flagAtiva;
	}

	public boolean isFlagAtiva() {
		return flagAtiva;
	}

	public void setTipoEquipe(int tipoEquipe) {
		this.tipoEquipe = tipoEquipe;
	}

	public int getTipoEquipe() {
		return tipoEquipe;
	}

	public void setCoordenador(Colaborador coordenador) {
		this.coordenador = coordenador;
	}

	public Colaborador getCoordenador() {
		return coordenador;
	}

	public List<Colaborador> getColaboradores() {
		//Collections.sort(colaboradores);
		return colaboradores;
	}

	public void setColaboradores(List<Colaborador> colaboradores) {
		this.colaboradores = null;
		this.colaboradores = colaboradores;
	}
	
	public void adicionarColaborador(Colaborador colaborador) {
		this.removerColaborador(colaborador);
		this.getColaboradores().add(colaborador);
	}
	
	public void removerColaborador(Colaborador colaborador) {
		if (this.getColaboradores().contains(colaborador)) {
			this.getColaboradores().remove(colaborador);
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
		Equipe other = (Equipe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		if (nome==null) {
			if (other.nome!=null)
				return false;
		}
		else if(!nome.equals(other.nome))
				return false;
		
		return true;
	}

	public int compareTo(Equipe o) {
		return this.getNome().compareToIgnoreCase(o.getNome());
	}

}
