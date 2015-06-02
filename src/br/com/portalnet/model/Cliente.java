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
public class Cliente implements Serializable, Comparable<Cliente> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	@Length(max=64)
	private String nomeEmpresa;
	
	@NotEmpty
	@Length(max=20)
	private String cnpj;
	
	@Length(max=128)
	private String endereco;

	@Length(max=64)
	private String cidade;

	@Length(max=15)
	private String telefone;

	@ManyToOne
  	@JoinColumn(name="ColaboradorGG_id", referencedColumnName="id")
  	private Colaborador gerenteGeral;
	
	@OneToMany(mappedBy="cliente", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<Area> areas;
	
	
	public Cliente() {
		areas = new ArrayList<Area>();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa.trim();
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj.trim();
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco.trim();
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade.trim();
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone.trim();
	}

	public Colaborador getGerenteGeral() {
		return gerenteGeral;
	}

	public void setGerenteGeral(Colaborador gerenteGeral) {
		this.gerenteGeral = gerenteGeral;
	}

	public List<Area> getAreas() {
		Collections.sort(areas);
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public void adicionarArea(Area area) {
		this.removerArea(area);
		this.getAreas().add(area);
	}
	
	public void removerArea(Area area) {
		if (this.getAreas().contains(area)) {
			this.getAreas().remove(area);
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(Cliente o) {
		return this.getNomeEmpresa().compareToIgnoreCase(o.getNomeEmpresa());
	}
	
}
