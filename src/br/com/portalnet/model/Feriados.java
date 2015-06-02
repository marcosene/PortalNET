package br.com.portalnet.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.portalnet.util.FacesUtils;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Feriados implements Serializable, Comparable<Feriados> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	@Length(max=64)
	private String nome;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataFeriado;
	
	
	public Feriados() {
	}

	public Calendar getDataFeriado() {
		return dataFeriado;
	}

	public void setDataFeriado(Calendar dataFeriado) {
		this.dataFeriado = dataFeriado;
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

	public String getDiaSemana() {
		String diaSemana;
                
		switch (this.dataFeriado.get(Calendar.DAY_OF_WEEK)) {
		    case 1: 
		    	diaSemana = FacesUtils.getMessage("diaSemana_domingo");
				break;
		    case 2: 
		    	diaSemana = FacesUtils.getMessage("diaSemana_segunda");
				break;
		    case 3: 
		    	diaSemana = FacesUtils.getMessage("diaSemana_terca");
				break;
		    case 4: 
		    	diaSemana = FacesUtils.getMessage("diaSemana_quarta");
				break;
		    case 5: 
		    	diaSemana = FacesUtils.getMessage("diaSemana_quinta");
				break;
		    case 6: 
		    	diaSemana = FacesUtils.getMessage("diaSemana_sexta");
				break;
		    case 7: 
		    	diaSemana = FacesUtils.getMessage("diaSemana_sabado");
				break;
		    default:
		    	diaSemana = "Erro";
		    	break;
		}
		return diaSemana;
	}
        
	public int compareTo(Feriados o) {
		return this.getDataFeriado().compareTo(o.getDataFeriado());
	}
}
