/**
 * @since 15/12/2010
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GrupoAtividade implements Serializable, Comparable<GrupoAtividade> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Length(max=64)
	private String nome;
	
	@ManyToOne
  	@JoinColumn(name="Projeto_id", referencedColumnName="id")
  	private Projeto projeto;

	@OneToMany(mappedBy="grupoAtividade", cascade=CascadeType.ALL)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<Atividade> atividades;
	
	
	public GrupoAtividade() {
		atividades = new ArrayList<Atividade>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.trim();
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public List<Atividade> getAtividades() {
		Collections.sort(atividades);
		return atividades;
	}

	public void setAtividades(List<Atividade> atividades) {
		this.atividades = null;
		this.atividades = atividades;
	}
	
	public void adicionarAtividade(Atividade atividade) {
		this.removerAtividade(atividade);
		this.getAtividades().add(atividade);
	}
	
	public void removerAtividade(Atividade atividade) {
		if (this.getAtividades().contains(atividade)) {
			this.getAtividades().remove(atividade);
		}
	}
	
	public Calendar getDataInicioPrevista() {
		Calendar dataInicioPrevista = null;
		
		for (Atividade atividade : atividades) {
			if (dataInicioPrevista == null ||
				dataInicioPrevista.after(atividade.getDataInicioPrevista())) {
				dataInicioPrevista = atividade.getDataInicioPrevista();
			}
		}
		
		return dataInicioPrevista;
	}
	
	public Calendar getDataFimPrevista() {
		Calendar dataFimPrevista = null;
		
		for (Atividade atividade : atividades) {
			if (dataFimPrevista == null ||
				dataFimPrevista.before(atividade.getDataFimPrevista())) {
				dataFimPrevista = atividade.getDataFimPrevista();
			}
		}
		
		return dataFimPrevista;
	}
	
	public int getQtdeHorasPrevistas() {
		int qtdeHorasPrevistas = 0;
		
		for(Atividade atividade : atividades) {
			qtdeHorasPrevistas += atividade.getQtdeHorasPrevistas();
		}
		
		return qtdeHorasPrevistas;
	}
	
	public float getQtdeHorasTrabalho() {
		float qtdeHorasTrabalho = 0;
		
		for(Atividade atividade : atividades) {
			qtdeHorasTrabalho += atividade.getQtdeHorasTrabalho();
		}
		
		return qtdeHorasTrabalho;
	}
	
	public float getQtdeHorasRestantes() {
		float qtdeHorasRestantes = 0;
		
		for(Atividade atividade : atividades) {
			qtdeHorasRestantes += atividade.getQtdeHorasRestantes();
		}
		
		return qtdeHorasRestantes;
	}
	
	public int getPorcentagemConclusaoReal() {
		float qtdeHorasTrabalho = 0;
		float qtdeHorasRestantes = 0;

		for(Atividade atividade : atividades) {
			if (atividade.isFlagContabilizarProgresso())
			{
				qtdeHorasTrabalho += atividade.getQtdeHorasTrabalho();
				qtdeHorasRestantes += atividade.getQtdeHorasRestantes();
			}
		}
		
		if (qtdeHorasRestantes == 0)
			return 100;
		
		return (int) (100 * qtdeHorasTrabalho / (qtdeHorasTrabalho + qtdeHorasRestantes));
	}
	
	public float getQtdeHorasExcedidas() {
		return getQtdeHorasTrabalho() + getQtdeHorasRestantes() - getQtdeHorasPrevistas();
	}
	
	public int getQtdeDiasAtraso() {
		int qtdeDiasAtraso = 0;
		
		// Busca a alocacao que esta mais atrasada
		for(Atividade atividade : atividades) {
			for (AlocacaoAtividade alocacao : atividade.getAlocacoes()) {
				int qtdeDiasAtrasoAlocacao = alocacao.getQtdeDiasAtraso();
				
				if (qtdeDiasAtrasoAlocacao > qtdeDiasAtraso) {
					qtdeDiasAtraso = qtdeDiasAtrasoAlocacao;
				}
			}
		}
		
		return qtdeDiasAtraso;
	}
	
	public float getCustoTotalAlocado(){
		float custoTotalAlocado = 0;
		
		for(Atividade atividade : atividades) {
			custoTotalAlocado += atividade.getCustoAlocacoes();
		}
		
		return custoTotalAlocado;
	}
	
	public float getCustoTotalLancado(){
		float custoTotalLancado = 0;
		
		for(Atividade atividade : atividades) {
			custoTotalLancado += atividade.getCustoLancamentos();
		}
		
		return custoTotalLancado;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((projeto == null) ? 0 : projeto.hashCode());
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
		GrupoAtividade other = (GrupoAtividade) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (projeto == null) {
			if (other.projeto != null)
				return false;
		} else if (!projeto.equals(other.projeto))
			return false;
		return true;
	}

	public int compareTo(GrupoAtividade arg0) {
		return this.nome.compareTo(arg0.getNome());
	}
	
}
