/**
 * @since 02/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

import br.com.portalnet.util.DataUtil;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AlocacaoAtividade implements Serializable, Comparable<AlocacaoAtividade> {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="Atividade_id", referencedColumnName="id")
  	private Atividade atividade;
	
	@ManyToOne
	@JoinColumn(name="Colaborador_id", referencedColumnName="id")
	private Colaborador colaborador;
	
	private int qtdeHorasAlocadas;
	
	private float qtdeHorasRestantes;
	
	private float qtdeHorasDiariasAlocadas;
	
	/*
	 * 0 = liberado
	 * 1 = bloqueado pelo coordenador
	 * 2 = bloqueado pelo processo de zerar as horas restantes
	 * */
	private short flagBloqueadoRegistro;
	
 	@Type(type="encryptedFloatAsString")
	private Float custoHoraAlocacao;

 	@NotNull
	@Temporal(TemporalType.DATE)
	private Calendar dataInicioPrevista;

	@OneToMany(mappedBy="alocacao", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<RegistroAtividade> registros;

	
	public AlocacaoAtividade() {
		qtdeHorasDiariasAlocadas = 8;
		dataInicioPrevista = Calendar.getInstance();
		registros = new ArrayList<RegistroAtividade>();
		flagBloqueadoRegistro = 0;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public int getQtdeHorasAlocadas() {
		return qtdeHorasAlocadas;
	}

	public void setQtdeHorasAlocadas(int qtdeHorasAlocadas) {
		this.qtdeHorasAlocadas = qtdeHorasAlocadas;
	}

	public float getQtdeHorasRestantes() {
		return Math.round(qtdeHorasRestantes*100)/100.0f;
	}

	public void setQtdeHorasRestantes(float qtdeHorasRestantes) {
		this.qtdeHorasRestantes = qtdeHorasRestantes;
	}
	
	public float getQtdeHorasDiariasAlocadas() {
		return qtdeHorasDiariasAlocadas;
	}

	public void setQtdeHorasDiariasAlocadas(float qtdeHorasDiariasAlocadas) {
		this.qtdeHorasDiariasAlocadas = qtdeHorasDiariasAlocadas;
	}

	public Calendar getDataInicioPrevista() {
		return dataInicioPrevista;
	}

	public void setDataInicioPrevista(Calendar dataInicioPrevista) {
		this.dataInicioPrevista = dataInicioPrevista;
	}

	public short getFlagBloqueadoRegistro() {
		return flagBloqueadoRegistro;
	}
	
	public void setFlagBloqueadoRegistro(int flagBloqueadoRegistro) {
		this.flagBloqueadoRegistro = (short) flagBloqueadoRegistro;
	}

	public float getCustoHoraAlocacao() {
		if(custoHoraAlocacao == null)
			return 0;
		
		return custoHoraAlocacao;
	}

	public void setCustoHoraAlocacao(float custoHoraAlocacao) {
		this.custoHoraAlocacao = custoHoraAlocacao;
	}

	public float getValorCustoAlocacao() {
		return this.getCustoHoraAlocacao() * qtdeHorasAlocadas;
	}
	
	public float getValorCustoLancamento() {
		float custoTotalLancamento = 0;
		
		for(RegistroAtividade registro : registros) {
			custoTotalLancamento += registro.getCustoHoraLancamento() * registro.getQtdeHorasTrabalho();
		}
		
		return custoTotalLancamento;
	} 

	public List<RegistroAtividade> getRegistros() {
		return registros;
	}

	public void setRegistros(List<RegistroAtividade> registros) {
		this.registros = null;
		this.registros = registros;
	}
	
	public void adicionarRegistro(RegistroAtividade registro) {
		this.removerRegistro(registro);
		this.getRegistros().add(registro);
	}
	
	public void removerRegistro(RegistroAtividade registro) {
		if (this.getRegistros().contains(registro)) {
			this.getRegistros().remove(registro);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((atividade == null) ? 0 : atividade.hashCode());
		result = prime * result
				+ ((colaborador == null) ? 0 : colaborador.hashCode());
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
		AlocacaoAtividade other = (AlocacaoAtividade) obj;
		if (atividade == null) {
			if (other.atividade != null)
				return false;
		} else if (!atividade.equals(other.atividade))
			return false;
		if (colaborador == null) {
			if (other.colaborador != null)
				return false;
		} else if (!colaborador.equals(other.colaborador))
			return false;
		return true;
	}

	public int compareTo(AlocacaoAtividade o) {
		if (this.getAtividade().getGrupoAtividade().getProjeto().isGenerico() ||
			this.getDataInicioPrevista().compareTo(o.getDataInicioPrevista()) == 0) {
			
			if (this.getColaborador().getNomeCompleto().compareTo(o.getColaborador().getNomeCompleto()) == 0)
				return this.getId().compareTo(o.getId());

			return this.getColaborador().getNomeCompleto().compareTo(o.getColaborador().getNomeCompleto());
		}
			
		return this.getDataInicioPrevista().compareTo(o.getDataInicioPrevista());
	}
	
	public Calendar getDataFimPrevista() {
		return DataUtil.avancarDataTrabalho(dataInicioPrevista, qtdeHorasAlocadas, this);
	}
	
	private Calendar getPrimeiroDiaRegistro() {
		Calendar primeiroDiaRegistro = null;
		
		for (RegistroAtividade registro : registros) {
			if (primeiroDiaRegistro == null ||
				primeiroDiaRegistro.after(registro.getDataTrabalho())) {
				primeiroDiaRegistro = registro.getDataTrabalho();
			}
		}
		
		return primeiroDiaRegistro;
	}
	
	private Calendar getUltimoDiaRegistro() {
		Calendar ultimoDiaRegistro = null;
		
		for (RegistroAtividade registro : registros) {
			if (ultimoDiaRegistro == null ||
				ultimoDiaRegistro.before(registro.getDataTrabalho())) {
				ultimoDiaRegistro = registro.getDataTrabalho();
			}
		}
		
		return ultimoDiaRegistro;
	}
	
	public Calendar getDataInicioReal() {
		if (this.getRegistros() == null ||
			this.getRegistros().isEmpty()) {
			return getDataInicioPrevista();
		}
		return getPrimeiroDiaRegistro();
	}
	
	public Calendar getDataFimReal() {
		Calendar dataFimReal = DataUtil.avancarDataTrabalho(getUltimoDiaRegistro(), qtdeHorasRestantes, this);
		
		if (dataFimReal == null)
			return getDataFimPrevista();
		
		return dataFimReal;
	}
	
	public float getQtdeHorasTrabalho() {
		float qtdeHorasTrabalho = 0;
		
		for(RegistroAtividade registro : registros) {
			qtdeHorasTrabalho += registro.getQtdeHorasTrabalho();
		}
		
		return qtdeHorasTrabalho;
	}
	
	public int getPorcentagemConclusaoPrev() {
		return (int) (100 * getQtdeHorasTrabalho() / qtdeHorasAlocadas);
	}
	
	public int getPorcentagemConclusaoReal() {
		float qtdeHorasTrabalho = getQtdeHorasTrabalho();
		float qtdeHorasRestantes = getQtdeHorasRestantes();
		
		if (qtdeHorasRestantes == 0)
			return 100;
		
		return (int) (100 * qtdeHorasTrabalho / (qtdeHorasTrabalho + qtdeHorasRestantes));
	}
	
	public float getQtdeHorasExcedidas() {
		return getQtdeHorasTrabalho() + getQtdeHorasRestantes() - getQtdeHorasAlocadas();
	}
	
	public int getQtdeDiasAtraso() {
		Calendar dataFimReal = getDataFimReal();
		Calendar dataFimPrevista = getDataFimPrevista();
		
		if (!atividade.isFlagContabilizarProgresso() ||
			dataFimPrevista.after(dataFimReal)) {
			return 0;
		}
		
		return DataUtil.getQtdeDiasUteis(dataFimPrevista, dataFimReal, this.getColaborador()) - 1;
	}

}
