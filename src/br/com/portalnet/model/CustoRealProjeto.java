/**
 * @since 18/04/2011
 * @author Gustavo de Mello Mascarin
 */
package br.com.portalnet.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustoRealProjeto implements Serializable, Comparable<CustoRealProjeto>{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
  	@JoinColumn(name="Projeto_id", referencedColumnName="id")
	private Projeto projeto;
	
	private int ano;
	
	private int mes;
	
	@Type(type="encryptedFloatAsString")
	private float valor;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}
	
	public String getMesString(){
		DateFormat formatador = new SimpleDateFormat("MMMMM", Locale.getDefault());
		Calendar data = Calendar.getInstance();
		data.set(Calendar.MONTH, this.getMes()-1);
		return formatador.format(data.getTime());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CustoRealProjeto){
			CustoRealProjeto custo = (CustoRealProjeto)obj;
			if (custo.getAno() == getAno()
					&& custo.getMes() == getMes()
					&& custo.getProjeto().getCodigo().equalsIgnoreCase(getProjeto()
							.getCodigo())) {
				return true;
			}
		}
		return false;
	}

	public int compareTo(CustoRealProjeto custo) {
		int comparacao = this.getAno() - custo.getAno();
		if(comparacao !=0)
			return comparacao;
		return this.getMes() - custo.getMes();
	}
}
