/**
 * @since 19/01/2010
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;

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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ocorrencia implements Serializable, Comparable<Ocorrencia> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(unique = true)
	@Length(max=15)
	private String numOcorrencia;
	
	@Length(max=15)
	private String numBugCliente;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Calendar dataAbertura;
	
	@NotEmpty
	@Length(max=128)
	private String titulo;
	
	private String descricaoResumida;
	
	private String comportamentoEsperado;
	
	private String textoAnalise;
	
	private String textoPropostaSolucao;
	
	@Length(max=50)
	private String baseline;
	
	@Length(max=15)
	private String versaoModulo;
	
	private int tipoSeveridade;
	
	private int statusOcorrencia;
	
	@ManyToOne
  	@JoinColumn(name="Projeto_id", referencedColumnName="id")
  	private Projeto projeto;
	
	@ManyToOne
  	@JoinColumn(name="Modulo_id", referencedColumnName="id")
  	private Modulo modulo;
	
	@ManyToOne
  	@JoinColumn(name="ColaboradorGPJ_id", referencedColumnName="id")
  	private Colaborador gpj;
	
	@ManyToOne
  	@JoinColumn(name="ColaboradorResp_id", referencedColumnName="id")
  	private Colaborador responsavel;
	
	private String historicoOcorrencia;	
	
	
	public String getHistoricoOcorrencia() {
		return historicoOcorrencia;
	}

	public void setHistoricoOcorrencia(String historicoOcorrencia) {
		this.historicoOcorrencia = historicoOcorrencia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumOcorrencia() {
		return numOcorrencia;
	}

	public void setNumOcorrencia(String numOcorrencia) {
		this.numOcorrencia = numOcorrencia.trim();
	}

	public String getNumBugCliente() {
		return numBugCliente;
	}

	public void setNumBugCliente(String numBugCliente) {
		this.numBugCliente = numBugCliente.trim();
	}

	public Calendar getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Calendar dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricaoResumida() {
		return descricaoResumida;
	}

	public void setDescricaoResumida(String descricaoResumida) {
		this.descricaoResumida = descricaoResumida;
	}

	public String getComportamentoEsperado() {
		return comportamentoEsperado;
	}

	public void setComportamentoEsperado(String comportamentoEsperado) {
		this.comportamentoEsperado = comportamentoEsperado;
	}

	public String getTextoAnalise() {
		return textoAnalise;
	}

	public void setTextoAnalise(String textoAnalise) {
		this.textoAnalise = textoAnalise;
	}

	public String getTextoPropostaSolucao() {
		return textoPropostaSolucao;
	}

	public void setTextoPropostaSolucao(String textoPropostaSolucao) {
		this.textoPropostaSolucao = textoPropostaSolucao;
	}

	public String getBaseline() {
		return baseline;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}

	public String getVersaoModulo() {
		return versaoModulo;
	}

	public void setVersaoModulo(String versaoModulo) {
		this.versaoModulo = versaoModulo;
	}

	public int getTipoSeveridade() {
		return tipoSeveridade;
	}

	public void setTipoSeveridade(int tipoSeveridade) {
		this.tipoSeveridade = tipoSeveridade;
	}

	public int getStatusOcorrencia() {
		return statusOcorrencia;
	}

	public void setStatusOcorrencia(int statusOcorrencia) {
		this.statusOcorrencia = statusOcorrencia;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public Colaborador getGpj() {
		return gpj;
	}

	public void setGpj(Colaborador gpj) {
		this.gpj = gpj;
	}

	public Colaborador getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Colaborador responsavel) {
		this.responsavel = responsavel;
	}

	public String getDescTipoSeveridade() {
		return Tipos.getTipoSeveridadeOcorrencia(tipoSeveridade);
	}

	public String getDescTipoStatus() {
		return Tipos.getTipoStatusOcorrencia(statusOcorrencia);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numOcorrencia == null) ? 0 : numOcorrencia.hashCode());
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
		Ocorrencia other = (Ocorrencia) obj;
		if (numOcorrencia == null) {
			if (other.numOcorrencia != null)
				return false;
		} else if (!numOcorrencia.equals(other.numOcorrencia))
			return false;
		if (projeto == null) {
			if (other.projeto != null)
				return false;
		} else if (!projeto.equals(other.projeto))
			return false;
		return true;
	}
	
	public int compareTo(Ocorrencia o) {
		if (this.getNumBugCliente().equals(o.getNumBugCliente())){
			return this.getNumOcorrencia().compareToIgnoreCase(o.getNumOcorrencia());
		}else{
			return this.getNumBugCliente().compareToIgnoreCase(o.getNumBugCliente());
		}
	}	
}
