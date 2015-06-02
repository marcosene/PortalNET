/**
 * @since 02/12/2008
 * @author Marco Aurelio Sene Santos
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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.portalnet.util.DataUtil;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RegistroAtividade implements Serializable, Comparable<RegistroAtividade> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@NotNull
	private Calendar dataTrabalho;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar horaInicial;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar horaFinal;
	
	private float qtdeHorasTrabalho;

	@NotEmpty
	private String descricao;
	
	@ManyToOne
	@JoinColumn(name="AlocacaoAtividade_id", referencedColumnName="id")
  	private AlocacaoAtividade alocacao;

 	@Type(type="encryptedFloatAsString")
	private Float custoHoraLancamento;

 	@ManyToOne
	@JoinColumn(name="Ocorrencia_id", referencedColumnName="id")
  	private Ocorrencia ocorrencia;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getDataTrabalho() {
		return dataTrabalho;
	}

	public void setDataTrabalho(Calendar dataTrabalho) {
		this.dataTrabalho = dataTrabalho;
	}

	public Calendar getHoraInicial() {
		return horaInicial;
	}

	public void setHoraInicial(Calendar horaInicial) {
		this.horaInicial = horaInicial;
	}

	public Calendar getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Calendar horaFinal) {
		this.horaFinal = horaFinal;
	}

	public float getQtdeHorasTrabalho() {
		return Math.round(qtdeHorasTrabalho*100)/100.0f;
	}

	public void setQtdeHorasTrabalho(float qtdeHorasTrabalho) {
		this.qtdeHorasTrabalho = qtdeHorasTrabalho;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public String getDescricaoCompleta() {
		if (this.getOcorrencia() != null)
			return this.getOcorrencia().getNumBugCliente() + ": " + descricao;
		
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.trim();
	}

	public AlocacaoAtividade getAlocacao() {
		return alocacao;
	}

	public void setAlocacao(AlocacaoAtividade alocacao) {
		this.alocacao = alocacao;
	}
	
	public void setCustoHoraLancamento(Float custoHoraLancamento) {
		this.custoHoraLancamento = custoHoraLancamento;
	}

	public Float getCustoHoraLancamento() {
		if(custoHoraLancamento == null)
			return Float.valueOf(0);
		
		return custoHoraLancamento;
		
	}

	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
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
		RegistroAtividade other = (RegistroAtividade) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			} else {
				if (dataTrabalho == null) {
					if (other.getDataTrabalho() != null)
						return false;
				} else if (!dataTrabalho.equals(other.dataTrabalho))
					return false;
				if (horaInicial == null) {
					if (other.getHoraInicial() != null)
						return false;
				} else if (!horaInicial.equals(other.getHoraInicial()))
					return false;
			}
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(RegistroAtividade o) {
		if (this.getDataTrabalho().equals(o.getDataTrabalho()))
			return this.getHoraInicial().compareTo(o.getHoraInicial());
		
		return this.getDataTrabalho().compareTo(o.getDataTrabalho());
	}
	
	public String getHoraInicialFormatada() {
		return DataUtil.getHoraFormatada(horaInicial);
	}

	public String getHoraFinalFormatada() {
		return DataUtil.getHoraFormatada(horaFinal);
	}

}
