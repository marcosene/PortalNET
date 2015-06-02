/**
 * @since 26/07/2010
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.validator.constraints.Length;


@Entity
public class Parametros implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	
	@Length(max=128)
	private String nomeEmpresa;
	
	@Length(max=19)
	private String cnpj;
	
	@Length(max=128)
	private String endereco;
	
	@Lob
	private byte[] logotipo;
	
	@Length(max=64)
	private String cidade;
	
	@Length(max=15)
	private String telefone;
	
	private int primeiroDiaContabil;
	
	
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
		this.nomeEmpresa = nomeEmpresa;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public byte[] getLogotipo() {
		return logotipo;
	}

	public void setLogotipo(byte[] logotipo) {
		this.logotipo = logotipo;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public int getPrimeiroDiaContabil() {
		if (primeiroDiaContabil == 0)
			primeiroDiaContabil = 1;
		return primeiroDiaContabil;
	}

	public void setPrimeiroDiaContabil(int primeiroDiaContabil) {
		this.primeiroDiaContabil = primeiroDiaContabil;
	}
	
	public int getUltimoDiaContabil(Calendar dataRef) {
		int ultDiaContabil = this.getPrimeiroDiaContabil() - 1;
		
		if (ultDiaContabil == 0)
			ultDiaContabil = dataRef.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		return ultDiaContabil;
	}
	
	public void paintLogotipo(OutputStream stream, Object object) throws IOException {
		if (this.getLogotipo() != null) {
			stream.write(this.getLogotipo());
		}
    }

}
