/**
 * @since 02/12/2008
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
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Atividade implements Serializable, Comparable<Atividade> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Long idSequencia;	// somente para poder ordenar as atividades (opcional)
	
	@NotEmpty
	@Length(max=128)
	private String nome;
	
	@Length(max=512)
	private String descricao;
	
	private int qtdeHorasPrevistas;
	
	private boolean flagContabilizarProgresso;
	
	private int tipoAssocOcorrencia;
	
	@ManyToOne
  	@JoinColumn(name="TipoAtividade_id", referencedColumnName="id")
  	private TipoAtividade tipoAtividade;
	
	@ManyToOne
  	@JoinColumn(name="GrupoAtividade_id", referencedColumnName="id")
  	private GrupoAtividade grupoAtividade;
	
	@OneToMany(mappedBy="atividade", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<AlocacaoAtividade> alocacoes;
	
	
	public Atividade() {
		qtdeHorasPrevistas = 0;
		idSequencia = 0L;
		flagContabilizarProgresso = true;
		tipoAssocOcorrencia = Tipos.TIPO_ASSOC_NAO_HA;
		alocacoes = new ArrayList<AlocacaoAtividade>();
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.trim();
	}

	public int getQtdeHorasPrevistas() {
		return qtdeHorasPrevistas;
	}

	public void setQtdeHorasPrevistas(int qtdeHorasPrevistas) {
		this.qtdeHorasPrevistas = qtdeHorasPrevistas;
	}

	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public int getTipoAssocOcorrencia() {
		return tipoAssocOcorrencia;
	}

	public void setTipoAssocOcorrencia(int tipoAssocOcorrencia) {
		this.tipoAssocOcorrencia = tipoAssocOcorrencia;
	}

	public GrupoAtividade getGrupoAtividade() {
		return grupoAtividade;
	}

	public void setGrupoAtividade(GrupoAtividade grupoAtividade) {
		this.grupoAtividade = grupoAtividade;
	}

	public void setFlagContabilizarProgresso(boolean flagContabilizarProgresso) {
		this.flagContabilizarProgresso = flagContabilizarProgresso;
	}

	public boolean isFlagContabilizarProgresso() {
		return flagContabilizarProgresso;
	}

	public void setIdSequencia(Long idSequencia) {
		this.idSequencia = idSequencia;
	}

	public Long getIdSequencia() {
		return idSequencia;
	}
	
	public List<AlocacaoAtividade> getAlocacoes() {
		Collections.sort(alocacoes);
		return alocacoes;
	}
	
	public List<AlocacaoAtividade> getAlocacoesColaboradoresAtivos() {
		List<AlocacaoAtividade> listaAlocacoesAtivas = new ArrayList<AlocacaoAtividade>();
		
		if (!this.grupoAtividade.getProjeto().isGenerico()) {
			return getAlocacoes();
		}
		
		for (AlocacaoAtividade alocacao : alocacoes) {
			if (alocacao.getColaborador().isFlagAtivo())
				listaAlocacoesAtivas.add(alocacao);
		}
		
		Collections.sort(listaAlocacoesAtivas);
		
		return listaAlocacoesAtivas;
	}

	public void setAlocacoes(List<AlocacaoAtividade> alocacoes) {
		this.alocacoes = alocacoes;
	}
	
	public void adicionarAlocacao(AlocacaoAtividade alocacao) {
		this.removerAlocacao(alocacao);
		this.getAlocacoes().add(alocacao);
	}
	
	public void removerAlocacao(AlocacaoAtividade alocacao) {
		if (this.getAlocacoes().contains(alocacao)) {
			this.getAlocacoes().remove(alocacao);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((grupoAtividade == null) ? 0 : grupoAtividade.hashCode());
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
		Atividade other = (Atividade) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (grupoAtividade == null) {
			if (other.grupoAtividade != null)
				return false;
		} else if (!grupoAtividade.equals(other.grupoAtividade))
			return false;
		return true;
	}

	public int compareTo(Atividade o) {
		
		if (this.getDataInicioPrevista() == null &&
			o.getDataInicioPrevista() == null) {
			return this.getNome().compareTo(o.getNome());
		}
		
		if (this.getDataInicioPrevista() == null) {
			Calendar dataInicioAux = Calendar.getInstance();
			dataInicioAux.add(Calendar.YEAR, 100);
			return dataInicioAux.compareTo(o.getDataInicioPrevista());
		}
		
		if (o.getDataInicioPrevista() == null) {
			Calendar dataInicioAux = Calendar.getInstance();
			dataInicioAux.add(Calendar.YEAR, 100);
			return this.getDataInicioPrevista().compareTo(dataInicioAux);
		}
		
		if (this.getDataInicioPrevista().compareTo(o.getDataInicioPrevista()) == 0) {
			if (!this.flagContabilizarProgresso || !o.flagContabilizarProgresso)
				return Boolean.valueOf(this.flagContabilizarProgresso).compareTo(o.flagContabilizarProgresso);
			else
				return this.getId().compareTo(o.getId());
		}
			
		return this.getDataInicioPrevista().compareTo(o.getDataInicioPrevista());
	}

	public Calendar getDataInicioPrevista() {
		Calendar dataInicioPrevista = null;
		
		for (AlocacaoAtividade alocacao : alocacoes) {
			if (dataInicioPrevista == null ||
				dataInicioPrevista.after(alocacao.getDataInicioPrevista())) {
				dataInicioPrevista = alocacao.getDataInicioPrevista();
			}
		}
		
		return dataInicioPrevista;
	}
	
	public Calendar getDataFimPrevista() {
		Calendar dataFimPrevista = null;
		
		for (AlocacaoAtividade alocacao : alocacoes) {
			if (dataFimPrevista == null ||
				dataFimPrevista.before(alocacao.getDataFimPrevista())) {
				dataFimPrevista = alocacao.getDataFimPrevista();
			}
		}
		
		return dataFimPrevista;
	}
	
	public Calendar getDataInicioReal() {
		Calendar dataInicioReal = null;
		
		for (AlocacaoAtividade alocacao : alocacoes) {
			if (dataInicioReal == null ||
				dataInicioReal.after(alocacao.getDataInicioReal())) {
				dataInicioReal = alocacao.getDataInicioReal();
			}
		}
		
		return dataInicioReal;
	}
	
	public Calendar getDataFimReal() {
		Calendar dataFimReal = null;
		
		for (AlocacaoAtividade alocacao : alocacoes) {
			if (dataFimReal == null ||
				dataFimReal.before(alocacao.getDataFimReal())) {
				dataFimReal = alocacao.getDataFimReal();
			}
		}
		
		return dataFimReal;
	}
	
	public int getQtdeHorasDisponiveisAlocacao() {
		int qtdeHorasDisponiveisAlocacao = qtdeHorasPrevistas;
		
		for (AlocacaoAtividade alocacao : alocacoes) {
			qtdeHorasDisponiveisAlocacao -= alocacao.getQtdeHorasAlocadas();
		}
		
		if (qtdeHorasDisponiveisAlocacao < 0) {
			qtdeHorasDisponiveisAlocacao = 0;
		}
		
		return qtdeHorasDisponiveisAlocacao;
	}
	
	public float getQtdeHorasTrabalho() {
		float qtdeHorasTrabalho = 0;
		
		for (AlocacaoAtividade alocacao : alocacoes) {
			qtdeHorasTrabalho += alocacao.getQtdeHorasTrabalho();
		}
		
		return qtdeHorasTrabalho;
	}

	public float getQtdeHorasRestantes() {
		float qtdeHorasRestantes = 0;
		
		// Se ainda não tem nenhuma alocação, assume as horas previstas como restantes
		if (alocacoes.isEmpty()) {
			return qtdeHorasPrevistas;
		}
		
		for(AlocacaoAtividade alocacao : alocacoes) {
			qtdeHorasRestantes += alocacao.getQtdeHorasRestantes();
		}
		
		return qtdeHorasRestantes;
	}
	
	public int getPorcentagemConclusaoReal() {
		float qtdeHorasTrabalho = getQtdeHorasTrabalho();
		float qtdeHorasRestantes = getQtdeHorasRestantes();
		
		if (qtdeHorasRestantes == 0)
			return 100;
		
		return (int) (100 * qtdeHorasTrabalho / (qtdeHorasTrabalho + qtdeHorasRestantes));
	}
	
	public float getQtdeHorasExcedidas() {
		return getQtdeHorasTrabalho() + getQtdeHorasRestantes() - getQtdeHorasPrevistas();
	}
	
	public float getCustoAlocacoes() {
		float custoAlocacoes = 0;
		
		for(AlocacaoAtividade alocacao : alocacoes) {
			custoAlocacoes += alocacao.getValorCustoAlocacao();
		}
		
		return custoAlocacoes;
	}
	
	public float getCustoLancamentos() {
		float custoLancamento = 0;
		
		for(AlocacaoAtividade alocacao : alocacoes) {
			custoLancamento += alocacao.getValorCustoLancamento();
		}
		
		return custoLancamento;
	}
	
}
