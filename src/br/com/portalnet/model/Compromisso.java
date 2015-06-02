/**
 * @since 13/05/2009
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.portalnet.util.FacesUtils;
import br.com.portalnet.view.SessionBean;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Compromisso implements Serializable, Comparable<Compromisso> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Length(max=512)
	private String nome;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Calendar dataEvento;
	
	private boolean flagConcluido;
	
	@Temporal(TemporalType.DATE)
	private Calendar dataConclusao;
	
	private boolean flagEnvioEmail;
	
	private int tipoRepeticao;
		
	private boolean flagRepeticaoSegunda;
	
	private boolean flagRepeticaoTerca;
	
	private boolean flagRepeticaoQuarta;
	
	private boolean flagRepeticaoQuinta;
	
	private boolean flagRepeticaoSexta;
	
	private boolean flagRepeticaoSabado;
	
	private boolean flagRepeticaoDomingo;
	
	@Temporal(TemporalType.DATE)
	private Calendar dataUltimoEnvio;

	@ManyToOne
  	@JoinColumn(name="Projeto_id", referencedColumnName="id")
  	private Projeto projeto;
	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name="CompromissoColaborador",
			joinColumns=@JoinColumn(name="Compromisso_id"),
			inverseJoinColumns=@JoinColumn(name="Colaborador_id"))
	private List<Colaborador> colaboradores;
	

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
	
	public Calendar getDataEvento() {
		return dataEvento;
	}

	public String getPrazo() {
		
		switch(this.tipoRepeticao){
		
		case 0: //sem repetição
			DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
			return formatador.format(dataEvento.getTime());
			
		case 1: //semanal
			StringBuffer sbDiaSemana = new StringBuffer("(" + FacesUtils.getMessage("tipoRepeticao_semanal") + ") ");
			
			if (isFlagRepeticaoDomingo()){
				sbDiaSemana.append(FacesUtils.getMessage("cadastrarColaborador_domingo") + "/");
			}
			if (isFlagRepeticaoSegunda()){
				sbDiaSemana.append(FacesUtils.getMessage("cadastrarColaborador_segunda") + "/");
			}
			if (isFlagRepeticaoTerca()){
				sbDiaSemana.append(FacesUtils.getMessage("cadastrarColaborador_terca") + "/");
			}
			if (isFlagRepeticaoQuarta()){
				sbDiaSemana.append(FacesUtils.getMessage("cadastrarColaborador_quarta") + "/");
			}
			if (isFlagRepeticaoQuinta()){
				sbDiaSemana.append(FacesUtils.getMessage("cadastrarColaborador_quinta") + "/");
			}
			if (isFlagRepeticaoSexta()){
				sbDiaSemana.append(FacesUtils.getMessage("cadastrarColaborador_sexta") + "/");
			}
			if (isFlagRepeticaoSabado()){
				sbDiaSemana.append(FacesUtils.getMessage("cadastrarColaborador_sabado") + "/");
			}
			
			if (sbDiaSemana.length() > 0) {
				return sbDiaSemana.substring(0, sbDiaSemana.length()-1);
			}
			
			return sbDiaSemana.toString();
			
		case 2: //mensal
			
			return "(" + FacesUtils.getMessage("tipoRepeticao_mensal") + ") " + FacesUtils.getMessage("cadastrarCompromisso_dia") + ": " + this.getDataEvento().get(Calendar.DAY_OF_MONTH);

		case 3: //anual
			return "(" + FacesUtils.getMessage("tipoRepeticao_anual") + ") " +  this.getDataEvento().get(Calendar.DAY_OF_MONTH) + "/" + (this.getDataEvento().get(Calendar.MONTH) + 1);
		}
		
		return "";

	}

	public void setDataEvento(Calendar dataEvento) {
		this.dataEvento = dataEvento;
	}

	public boolean isFlagConcluido() {
		return flagConcluido;
	}

	public void setFlagConcluido(boolean flagConcluido) {
		this.flagConcluido = flagConcluido;
	}

	public Calendar getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Calendar dataConclusao) {
		this.dataConclusao = dataConclusao;
	}
	
	public boolean isFlagEnvioEmail() {
		return flagEnvioEmail;
	}

	public void setFlagEnvioEmail(boolean flagEnvioEmail) {
		this.flagEnvioEmail = flagEnvioEmail;
	}

	public int getTipoRepeticao() {
		return tipoRepeticao;
	}

	public void setTipoRepeticao(int tipoRepeticao) {
		this.tipoRepeticao = tipoRepeticao;
	}
	
	public boolean isFlagRepeticaoSegunda() {
		return flagRepeticaoSegunda;
	}

	public void setFlagRepeticaoSegunda(boolean flagRepeticaoSegunda) {
		this.flagRepeticaoSegunda = flagRepeticaoSegunda;
	}

	public boolean isFlagRepeticaoTerca() {
		return flagRepeticaoTerca;
	}

	public void setFlagRepeticaoTerca(boolean flagRepeticaoTerca) {
		this.flagRepeticaoTerca = flagRepeticaoTerca;
	}

	public boolean isFlagRepeticaoQuarta() {
		return flagRepeticaoQuarta;
	}

	public void setFlagRepeticaoQuarta(boolean flagRepeticaoQuarta) {
		this.flagRepeticaoQuarta = flagRepeticaoQuarta;
	}

	public boolean isFlagRepeticaoQuinta() {
		return flagRepeticaoQuinta;
	}

	public void setFlagRepeticaoQuinta(boolean flagRepeticaoQuinta) {
		this.flagRepeticaoQuinta = flagRepeticaoQuinta;
	}

	public boolean isFlagRepeticaoSexta() {
		return flagRepeticaoSexta;
	}

	public void setFlagRepeticaoSexta(boolean flagRepeticaoSexta) {
		this.flagRepeticaoSexta = flagRepeticaoSexta;
	}

	public boolean isFlagRepeticaoSabado() {
		return flagRepeticaoSabado;
	}

	public void setFlagRepeticaoSabado(boolean flagRepeticaoSabado) {
		this.flagRepeticaoSabado = flagRepeticaoSabado;
	}

	public boolean isFlagRepeticaoDomingo() {
		return flagRepeticaoDomingo;
	}

	public void setFlagRepeticaoDomingo(boolean flagRepeticaoDomingo) {
		this.flagRepeticaoDomingo = flagRepeticaoDomingo;
	}
	
	public void setDataUltimoEnvio(Calendar dataUltimoEnvio) {
		this.dataUltimoEnvio = dataUltimoEnvio;
	}

	public Calendar getDataUltimoEnvio() {
		return dataUltimoEnvio;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}
	
	public List<Colaborador> getColaboradores() {
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
	
	public boolean isToday() {
		Calendar dataHoje = Calendar.getInstance();
		
		switch(this.tipoRepeticao){
		case 0: //sem repetição 
			if(this.dataEvento.get(Calendar.YEAR) == dataHoje.get(Calendar.YEAR) &&
					this.dataEvento.get(Calendar.DAY_OF_YEAR) == dataHoje.get(Calendar.DAY_OF_YEAR)){
					return true;
				}
			break;
                    
		case 1: //semanal
			int diaSemana = dataHoje.get(Calendar.DAY_OF_WEEK);
			if (diaSemana == 1 && this.isFlagRepeticaoDomingo()) {
				return true;
			} else if (diaSemana == 2 && this.isFlagRepeticaoSegunda()) {
				return true;
			} else if (diaSemana == 3 && this.isFlagRepeticaoTerca()) {
				return true;
			} else if (diaSemana == 4 && this.isFlagRepeticaoQuarta()) {
				return true;
			} else if (diaSemana == 5 && this.isFlagRepeticaoQuinta()) {
				return true;
			} else if (diaSemana == 6 && this.isFlagRepeticaoSexta()) {
				return true;
			} else if (diaSemana == 7 && this.isFlagRepeticaoSabado()) {
				return true;
			}
			break;
                    
		case 2: //mensal
			// verifica se o dia do mês para envio de email é o dia atual 
			if (this.getDataEvento().get(Calendar.DAY_OF_MONTH) == dataHoje.get(Calendar.DAY_OF_MONTH)) {
				return true;
			}else if (dataHoje.getActualMaximum(Calendar.DAY_OF_MONTH) < this.getDataEvento().get(Calendar.DAY_OF_MONTH)){
				if(dataHoje.getActualMaximum(Calendar.DAY_OF_MONTH) == dataHoje.get(Calendar.DAY_OF_MONTH)){
					return true;
				}
			}
			break;
                    
		case 3: //anual
			// verifica se o dia/mês para envio de email é o dia atual
			if (this.getDataEvento().get(Calendar.DAY_OF_MONTH) == dataHoje.get(Calendar.DAY_OF_MONTH)
				&& this.getDataEvento().get(Calendar.MONTH) == dataHoje.get(Calendar.MONTH)) {
				return true;
			}else if (this.getDataEvento().get(Calendar.MONTH) == dataHoje.get(Calendar.MONTH)){
				if (this.getDataEvento().get(Calendar.DAY_OF_MONTH) == dataHoje.get(Calendar.DAY_OF_MONTH)){
					return true;
				}
			}
			break;
		}
		
		return false;
	}

	public boolean isOld() {
		Calendar dataHoje = Calendar.getInstance();
		
		if(this.dataEvento.get(Calendar.YEAR) < dataHoje.get(Calendar.YEAR) ||
			(this.dataEvento.get(Calendar.YEAR) == dataHoje.get(Calendar.YEAR) &&
			 this.dataEvento.get(Calendar.DAY_OF_YEAR) < dataHoje.get(Calendar.DAY_OF_YEAR))){
			return true;
		}
		else{
			return false;
		}
	}
	
	public String getNomeResponsaveis() {
		StringBuilder responsaveis = new StringBuilder();
		
		if (this.getColaboradores()!=null){
			for(Colaborador responsavel : this.getColaboradores()) {
				if (responsaveis.length() != 0) {
					responsaveis.append(", ");
				}
				responsaveis.append(responsavel.getApelido());
			}
		}

		return responsaveis.toString();
	}
	
	public boolean isResponsavelLogado() {
		SessionBean sessionBean = (SessionBean) FacesUtils.getManagedBean("sessionBean");
		return this.colaboradores.contains(sessionBean.getColaboradorLogado());
	}
	
	public int compareTo(Compromisso o) {
		if (this.getDataEvento().equals(o.getDataEvento()))
			return this.getProjeto().compareTo(o.getProjeto());
		else
			return this.getDataEvento().compareTo(o.getDataEvento());
	}
	
}
