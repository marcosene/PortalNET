/**
 * @since 17/09/2010
 * @author Felipe Marques de Souza Falcão Gondim
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Ferias implements Serializable, Comparable<Ferias>{

	private static final long serialVersionUID = 1L;

	public static final int FERIAS_EM_ANDAMENTO = 0;
	public static final int FERIAS_AGENDADAS = 1;
	public static final int FERIAS_CONCLUIDAS = 2;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	private Calendar dataInicioRef;
	
	@Temporal(TemporalType.DATE)
	private Calendar dataFimRef;
	
	@Temporal(TemporalType.DATE)
	private Calendar dataInicioFerias;
	
	@Temporal(TemporalType.DATE)
	private Calendar dataFimFerias;
	
	@ManyToOne
  	@JoinColumn(name="Colaborador_id", referencedColumnName="id")
  	private Colaborador colaborador;
	
	@Transient
	private int qtdeDiasCorridos;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getDataInicioRef() {
		return dataInicioRef;
	}

	public void setDataInicioRef(Calendar dataInicioRef) {
		this.dataInicioRef = dataInicioRef;
	}

	public Calendar getDataFimRef() {
		return dataFimRef;
	}

	public void setDataFimRef(Calendar dataFimRef) {
		this.dataFimRef = dataFimRef;
	}

	public Calendar getDataInicioFerias() {
		return dataInicioFerias;
	}

	public void setDataInicioFerias(Calendar dataInicioFerias) {
		this.dataInicioFerias = dataInicioFerias;
	}

	public Calendar getDataFimFerias() {
		return dataFimFerias;
	}

	public void setDataFimFerias(Calendar dataFimFerias) {
		this.dataFimFerias = dataFimFerias;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	
	public int compareTo(Ferias arg0) {
		if (this.getStatus() == arg0.getStatus()) {
			if (this.getDataInicioFerias().compareTo(arg0.getDataInicioFerias()) == 0) {
				return this.getDataFimFerias().compareTo(arg0.getDataFimFerias());
			}
			
			return this.getDataInicioFerias().compareTo(arg0.getDataInicioFerias());
		} else {
			return new Integer(this.getStatus()).compareTo(arg0.getStatus());
		}
	}
	
	public void setQtdeDiasCorridos(int qtdeDiasCorridos){
		this.qtdeDiasCorridos = qtdeDiasCorridos;
	}
		
	public int getQtdeDiasCorridos() {
		Calendar dataInicioAux, dataFimAux;
		int qtdeDias = 0;
		
		if (dataInicioFerias == null || dataFimFerias == null) {
			return 30;
		}
		
		dataInicioAux = (Calendar) this.dataInicioFerias.clone();
		dataInicioAux.set(Calendar.HOUR_OF_DAY, 0);
		dataInicioAux.set(Calendar.MINUTE, 0);
		dataInicioAux.set(Calendar.SECOND, 0);
		dataInicioAux.set(Calendar.MILLISECOND, 0);
		
		dataFimAux = (Calendar) this.dataFimFerias.clone();
		dataFimAux.set(Calendar.HOUR_OF_DAY, 23);
		dataFimAux.set(Calendar.MINUTE, 59);
		dataFimAux.set(Calendar.SECOND, 59);
		
		do {
			dataInicioAux.add(Calendar.DAY_OF_YEAR, 1);
			qtdeDias++;
			
		} while(dataInicioAux.before(dataFimAux));
		
		return qtdeDias;
	}
	
	public int getStatus() {
		Calendar dataHoje = Calendar.getInstance();
		dataHoje.set(Calendar.HOUR_OF_DAY, 0);
		dataHoje.set(Calendar.MINUTE, 0);
		dataHoje.set(Calendar.SECOND, 0);
		dataHoje.set(Calendar.MILLISECOND, 0);
		
		// Ferias concluidas
		if (dataFimFerias.before(dataHoje))
			return FERIAS_CONCLUIDAS;
		
		// Ferias agendadas
		if (dataInicioFerias.after(dataHoje))
			return FERIAS_AGENDADAS;
		
		// Em ferias 
		return FERIAS_EM_ANDAMENTO;
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
		Ferias other = (Ferias) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
