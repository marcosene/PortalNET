/**
 * @since 02/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.model;

import java.io.ByteArrayInputStream;
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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.portalnet.util.StringUtil;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Colaborador implements Serializable, Comparable<Colaborador> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	@Length(max=25)
	private String nome;
	
	@NotNull
	@Length(max=35)
	private String sobrenome;
	
	@NotNull
	@Length(max=15)
	private String apelido;
	
	@NotEmpty
	@Email
	@Length(max=50)
	private String email;
	
	@Email
	@Length(max=50)
	private String emailAlternativo;
	
	@NotEmpty
	@Length(max=16)
	private String usuario;
	
	@NotEmpty
	@Type(type="encryptedString")
	@Length(min=6,max=128)
	private String senha;
	
	private int tipoUsuario;
	
	private int tipoAcesso;
	
	private boolean flagAtivo;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataUltLogin;
	
	@Min(value=1)
	@Max(value=31)
	private int diaAniversario;
	
	@Min(value=1)
	@Max(value=12)
	private int mesAniversario;
	
	@Length(max=15)
	private String ramal;
	
	@Length(max=15)
	private String telResid;
	
	@Length(max=15)
	private String telCelular;
	
	private boolean flagTrabSegunda;
	
	private boolean flagTrabTerca;
	
	private boolean flagTrabQuarta;
	
	private boolean flagTrabQuinta;
	
	private boolean flagTrabSexta;
	
	private boolean flagTrabSabado;
	
	private boolean flagTrabDomingo;
	
 	@Type(type="encryptedFloatAsString")
	private Float valorCustoHora;
	
	@Lob
	private byte[] foto;
	
	@Transient
	private byte[] fotoTemp;
	
	@Temporal(TemporalType.DATE)
	private Calendar dataContratacao;

	@OneToMany(mappedBy="colaborador", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<AlocacaoAtividade> alocacoes;

	@OneToMany(mappedBy="colaborador", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<Ferias> ferias;
	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name="EquipeColaborador",
			joinColumns=@JoinColumn(name="Colaborador_id"),
			inverseJoinColumns=@JoinColumn(name="Equipe_id"))
	private List<Equipe> equipes;
	
	public Colaborador() {
		alocacoes = new ArrayList<AlocacaoAtividade>();
		ferias = new ArrayList<Ferias>();
		equipes = new ArrayList<Equipe>();
		diaAniversario = 1;
		mesAniversario = 1;
		
		if (dataUltLogin == null) {
			dataUltLogin = Calendar.getInstance();
		}
		
		flagAtivo = true;
		tipoUsuario = Tipos.TIPO_USUARIO_COMUM;
		tipoAcesso = Tipos.TIPO_ACESSO_INTERNO;
		
		flagTrabSegunda = true;
		flagTrabTerca = true;
		flagTrabQuarta = true;
		flagTrabQuinta = true;
		flagTrabSexta = true;
		flagTrabSabado = false;
		flagTrabDomingo = false;
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

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}

	public String getEmailAlternativo() {
		return emailAlternativo;
	}

	public void setEmailAlternativo(String emailAlternativo) {
		this.emailAlternativo = emailAlternativo.trim();
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido.trim();
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario.trim();
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public int getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(int tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public int getTipoAcesso() {
		return tipoAcesso;
	}

	public void setTipoAcesso(int tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}

	public boolean isFlagAtivo() {
		return flagAtivo;
	}

	public void setFlagAtivo(boolean flagAtivo) {
		this.flagAtivo = flagAtivo;
	}

	public Calendar getDataUltLogin() {
		return dataUltLogin;
	}

	public void setDataUltLogin(Calendar dataUltLogin) {
		this.dataUltLogin = dataUltLogin;
	}

	public int getDiaAniversario() {
		return diaAniversario;
	}

	public void setDiaAniversario(int diaAniversario) {
		this.diaAniversario = diaAniversario;
	}

	public int getMesAniversario() {
		return mesAniversario;
	}

	public void setMesAniversario(int mesAniversario) {
		this.mesAniversario = mesAniversario;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal.trim();
	}

	public String getTelResid() {
		return telResid;
	}

	public void setTelResid(String telResid) {
		this.telResid = telResid.trim();
	}

	public String getTelCelular() {
		return telCelular;
	}

	public void setTelCelular(String telCelular) {
		this.telCelular = telCelular.trim();
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}	
	
	public byte[] getFotoTemp() {
		return fotoTemp;
	}
	public void setFotoTemp(byte[] fotoTemp) {
		this.fotoTemp = fotoTemp;
	}

	public Calendar getDataContratacao() {
		return dataContratacao;
	}

	public void setDataContratacao(Calendar dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	public Float getValorCustoHora() {
		if (valorCustoHora != null)
			return valorCustoHora;
		else 
			return new Float(0);
	}

	public void setValorCustoHora(Float valorCustoHora) {
		this.valorCustoHora = valorCustoHora;
	}
	
	public String getDescTipoUsuario() {
		return Tipos.getTipoUsuario(tipoUsuario);
	}

	public List<AlocacaoAtividade> getAlocacoes() {
		return alocacoes;
	}

	public void setAlocacoes(List<AlocacaoAtividade> alocacoes) {
		this.alocacoes = null;
		this.alocacoes = alocacoes;
	}
	
	public List<Ferias> getFerias() {
		return ferias;
	}
	
	public void setFerias(List<Ferias> ferias) {
		this.ferias = null;
		this.ferias = ferias;
	}

	public List<Equipe> getEquipes() {
		return equipes;
	}

	public void setEquipes(List<Equipe> equipes) {
		this.equipes = null;
		this.equipes = equipes;
	}

	public boolean isFlagTrabSegunda() {
		return flagTrabSegunda;
	}

	public void setFlagTrabSegunda(boolean flagTrabSegunda) {
		this.flagTrabSegunda = flagTrabSegunda;
	}

	public boolean isFlagTrabTerca() {
		return flagTrabTerca;
	}

	public void setFlagTrabTerca(boolean flagTrabTerca) {
		this.flagTrabTerca = flagTrabTerca;
	}

	public boolean isFlagTrabQuarta() {
		return flagTrabQuarta;
	}

	public void setFlagTrabQuarta(boolean flagTrabQuarta) {
		this.flagTrabQuarta = flagTrabQuarta;
	}

	public boolean isFlagTrabQuinta() {
		return flagTrabQuinta;
	}

	public void setFlagTrabQuinta(boolean flagTrabQuinta) {
		this.flagTrabQuinta = flagTrabQuinta;
	}

	public boolean isFlagTrabSexta() {
		return flagTrabSexta;
	}

	public void setFlagTrabSexta(boolean flagTrabSexta) {
		this.flagTrabSexta = flagTrabSexta;
	}

	public boolean isFlagTrabSabado() {
		return flagTrabSabado;
	}

	public void setFlagTrabSabado(boolean flagTrabSabado) {
		this.flagTrabSabado = flagTrabSabado;
	}

	public boolean isFlagTrabDomingo() {
		return flagTrabDomingo;
	}

	public void setFlagTrabDomingo(boolean flagTrabDomingo) {
		this.flagTrabDomingo = flagTrabDomingo;
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
		Colaborador other = (Colaborador) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(Colaborador o) {
		return StringUtil.substituirAcentuacao(this.getNomeCompleto())
					.compareToIgnoreCase(StringUtil.substituirAcentuacao(o.getNomeCompleto()));
	}
	
	public String getNomeCompleto() {
		if (nome == null || sobrenome == null)
			return "";
		
		return nome.trim() + " " + sobrenome.trim();
	}
	
	public boolean getEmFerias() {
		for(Ferias feriasAux : this.getFerias()) {
			if (feriasAux.getStatus() == Ferias.FERIAS_EM_ANDAMENTO) {
				return true;
			}	
		}
		return false;
	}

	public StreamedContent getGraphicFoto() {
		return new DefaultStreamedContent(new ByteArrayInputStream(foto), "image/jpeg");
	}
	
	public StreamedContent getGraphicFotoTemp() {
		if (fotoTemp == null)
			fotoTemp = foto;
		
		return new DefaultStreamedContent(new ByteArrayInputStream(fotoTemp), "image/jpeg");
	}
	
}
