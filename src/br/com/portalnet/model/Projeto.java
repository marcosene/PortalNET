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
import javax.persistence.Column;
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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Projeto implements Serializable, Comparable<Projeto> {
	
	private static final long serialVersionUID = 1L;
	
	public static final int ESTADO_HABILITADO = 0;
	public static final int ESTADO_DESABILITADO = 1;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Length(max=15)
	@Column(unique = true)
	private String codigo;

	@NotEmpty
	@Length(max=15)
	private String codigoCliente;

	@Length(max=15)
	private String codigoPacote;

	@NotEmpty
	@Length(max=64)
	private String nome;
	
	@Length(max=512)
	private String descricao;

	private int tipoFaseProjeto;
	
	private int estado;
	
	private boolean generico;
	
	@Temporal(TemporalType.DATE)
	@NotNull
	private Calendar dataCadastro;

	@Temporal(TemporalType.DATE)
	private Calendar dataInicio;

	@Temporal(TemporalType.DATE)
	private Calendar dataEncerramento;
	
	@ManyToOne
  	@JoinColumn(name="ColaboradorGPJ_id", referencedColumnName="id")
  	private Colaborador gpj;
	
	@ManyToOne
  	@JoinColumn(name="ColaboradorResponsavel_id", referencedColumnName="id")
  	private Colaborador responsavel;

	@ManyToOne
  	@JoinColumn(name="Produto_id", referencedColumnName="id")
  	private Produto produto;

	@OneToMany(mappedBy="projeto", cascade=CascadeType.ALL)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<GrupoAtividade> gruposAtividades;
	
	@OneToMany(mappedBy="projeto", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<Compromisso> compromissos;
	
	@OneToMany(mappedBy="projeto", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<ObservacoesProjeto> observacoes;
	
	@OneToMany(mappedBy="projeto", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<CustoRealProjeto> custosReaisProjeto;
	
	@OneToMany(mappedBy="projeto", fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.DELETE})
	private List<Ocorrencia> ocorrencias;
	
 	@Type(type="encryptedFloatAsString")
	private Float custoAcordado;
 	

	public Projeto() {
		this.estado = ESTADO_HABILITADO;
		this.generico = false;
		this.dataCadastro = Calendar.getInstance();
		this.dataInicio = Calendar.getInstance();
		gruposAtividades = new ArrayList<GrupoAtividade>();
		compromissos = new ArrayList<Compromisso>();
		observacoes = new ArrayList<ObservacoesProjeto>();
		ocorrencias = new ArrayList<Ocorrencia>();
		custosReaisProjeto = new ArrayList<CustoRealProjeto>();
	}

	public Colaborador getResponsavel() {
		return responsavel;
	}


	public void setResponsavel(Colaborador responsavel) {
		this.responsavel = responsavel;
	}
	
	public String getDescTipoFaseProjeto() {
		return Tipos.getTipoFasesProjeto(this.tipoFaseProjeto);
	}
	
	public int getTipoFaseProjeto() {
		return tipoFaseProjeto;
	}

	public void setTipoFaseProjeto(int tipoFaseProjeto) {
		this.tipoFaseProjeto = tipoFaseProjeto;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo.trim();
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente.trim();
	}


	public String getCodigoCliente() {
		return codigoCliente;
	}


	public void setCodigoPacote(String codigoPacote) {
		this.codigoPacote = codigoPacote;
	}


	public String getCodigoPacote() {
		return codigoPacote;
	}	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.trim();
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.trim();
	}

	public String getDescricao() {
		return descricao;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public boolean isGenerico() {
		return generico;
	}

	public void setGenerico(boolean generico) {
		this.generico = generico;
	}

	public Calendar getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Calendar dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Colaborador getGpj() {
		return gpj;
	}

	public void setGpj(Colaborador gpj) {
		this.gpj = gpj;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<GrupoAtividade> getGruposAtividades() {
		Collections.sort(gruposAtividades);
		return gruposAtividades;
	}

	public void setGruposAtividades(List<GrupoAtividade> gruposAtividades) {
		this.gruposAtividades = null;
		this.gruposAtividades = gruposAtividades;
	}

	public List<Compromisso> getCompromissos() {
		Collections.sort(compromissos);
		return compromissos;
	}

	public void setCompromissos(List<Compromisso> compromissos) {
		this.compromissos = null;
		this.compromissos = compromissos;
	}
	
	public List<ObservacoesProjeto> getObservacoes() {
		Collections.sort(observacoes);
		return observacoes;
	}

	public void setObservacoes(List<ObservacoesProjeto> observacoes) {
		this.observacoes = null;
		this.observacoes = observacoes;
	}
	
	public List<CustoRealProjeto> getCustosReaisProjeto() {
		Collections.sort(custosReaisProjeto);
		return custosReaisProjeto;
	}
	
	public void setCustosReaisProjeto(List<CustoRealProjeto> custosReaisProjeto) {
		this.custosReaisProjeto = null;
		this.custosReaisProjeto = custosReaisProjeto;
	}
	
	public List<Ocorrencia> getOcorrencias() {
		Collections.sort(ocorrencias);
		return ocorrencias;
	}

	public void setOcorrencias(List<Ocorrencia> ocorrencias) {
		this.ocorrencias = null;
		this.ocorrencias = ocorrencias;
	}

	public void adicionarGrupoAtividade(GrupoAtividade grupoAtividade) {
		this.removerGrupoAtividade(grupoAtividade);
		this.getGruposAtividades().add(grupoAtividade);
	}
	
	public void removerGrupoAtividade(GrupoAtividade grupoAtividade) {
		if (this.getGruposAtividades().contains(grupoAtividade)) {
			this.getGruposAtividades().remove(grupoAtividade);
		}
	}
	
	public void adicionarCompromisso(Compromisso compromisso) {
		this.removerCompromisso(compromisso);
		this.getCompromissos().add(compromisso);
	}
	
	public void removerCompromisso(Compromisso compromisso) {
		if (this.getCompromissos().contains(compromisso)) {
			this.getCompromissos().remove(compromisso);
		}
	}
	
	public void adicionarObservacao(ObservacoesProjeto observacao) {
		this.removerObservacao(observacao);
		this.getObservacoes().add(observacao);
	}
	
	public void removerObservacao(ObservacoesProjeto observacao) {
		if (this.getObservacoes().contains(observacao)) {
			this.getObservacoes().remove(observacao);
		}
	}
	
	public void adicionarCustoRealProjeto(CustoRealProjeto custoRealProjeto){
		this.removerCustoRealProjeto(custoRealProjeto);
		this.getCustosReaisProjeto().add(custoRealProjeto);
	}
	
	public void removerCustoRealProjeto(CustoRealProjeto custoRealProjeto){
		if(this.getCustosReaisProjeto().contains(custoRealProjeto)){
			this.getCustosReaisProjeto().remove(custoRealProjeto);
		}
	}
	
	public String getCodigoClienteCompleto() {
		if (codigoPacote != null && !codigoPacote.isEmpty())
			return codigoCliente + "-" + codigoPacote;
		else
			return codigoCliente;
	}
	
	public String getNomeCompleto() {
		return "[" + getCodigoClienteCompleto() + "] " + getNome();
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
		Projeto other = (Projeto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int getQtdeHorasPrevistas() {
		int qtdeHorasPrevistas = 0;
		
		for(GrupoAtividade grupoAtividade : gruposAtividades) {
			qtdeHorasPrevistas += grupoAtividade.getQtdeHorasPrevistas();
		}
		
		return qtdeHorasPrevistas;
	}
	
	public float getQtdeHorasTrabalho() {
		float qtdeHorasTrabalho = 0;
		
		for(GrupoAtividade grupoAtividade : gruposAtividades) {
			qtdeHorasTrabalho += grupoAtividade.getQtdeHorasTrabalho();
		}
		
		return qtdeHorasTrabalho;
	}
	
	public float getQtdeHorasRestantes() {
		float qtdeHorasRestantes = 0;
		
		for(GrupoAtividade grupoAtividade : gruposAtividades) {
			qtdeHorasRestantes += grupoAtividade.getQtdeHorasRestantes();
		}
		
		return qtdeHorasRestantes;
	}
	
	public int getPorcentagemConclusaoReal() {
		float qtdeHorasTrabalho = 0;
		float qtdeHorasRestantes = 0;

		for(GrupoAtividade grupoAtividade : gruposAtividades) {
			for(Atividade atividade : grupoAtividade.getAtividades()) {
				if (atividade.isFlagContabilizarProgresso())
				{
					qtdeHorasTrabalho += atividade.getQtdeHorasTrabalho();
					qtdeHorasRestantes += atividade.getQtdeHorasRestantes();
				}
			}
		}
		
		if (qtdeHorasRestantes == 0)
			return 100;
		
		return (int) (100 * qtdeHorasTrabalho / (qtdeHorasTrabalho + qtdeHorasRestantes));
	}

	
	public int getPorcentagemEsforcoUtilizado() {
		float qtdeHorasTrabalho = getQtdeHorasTrabalho();
		float qtdeHorasPrevistas = getQtdeHorasPrevistas();
		
		return (int) (100 * qtdeHorasTrabalho / qtdeHorasPrevistas);
	}

	public int getPorcentagemEsforcoNaoUtilizado() {
		return (int) (100 * getQtdeHorasRestantes() / getQtdeHorasPrevistas());
	}
	
	public float getQtdeHorasExcedidas() {
		return getQtdeHorasTrabalho() + getQtdeHorasRestantes() - getQtdeHorasPrevistas();
	}
	
	public int getQtdeDiasAtraso() {
		int qtdeDiasAtraso = 0;
		
		// Busca a alocacao que esta mais atrasada
		for(GrupoAtividade grupoAtividade : gruposAtividades) {
			int qtdeDiasAtrasoGrupo = grupoAtividade.getQtdeDiasAtraso();
				
			if (qtdeDiasAtrasoGrupo > qtdeDiasAtraso) {
				qtdeDiasAtraso = qtdeDiasAtrasoGrupo;
			}

		}
		
		return qtdeDiasAtraso;
	}
	
	public int compareTo(Projeto o) {
		if (this.getEstado() == o.getEstado()) {
			if (this.getProduto().equals(o.getProduto())) {
				if (this.getCodigoCliente().equalsIgnoreCase(o.getCodigoCliente()) &&
					this.getCodigoPacote() != null && o.getCodigoPacote() != null)
					return this.getCodigoPacote().compareToIgnoreCase(o.getCodigoPacote());
				else
					return this.getCodigo().compareToIgnoreCase(o.getCodigo());
			} else {
				return this.getProduto().compareTo(o.getProduto());
			}
			
		} else{
			return Integer.valueOf(this.getEstado()).compareTo(o.getEstado());
		}
	}


	public void setDataInicio(Calendar dataInicio) {
		this.dataInicio = dataInicio;
	}


	public Calendar getDataInicio() {
		return dataInicio;
	}


	public void setDataEncerramento(Calendar dataEncerramento) {
		this.dataEncerramento = dataEncerramento;
	}


	public Calendar getDataEncerramento() {
		return dataEncerramento;
	}
	
	public float getCustoTotalAlocado(){
		float custoTotalAlocado = 0;
		
		for(GrupoAtividade grupoAtividade : gruposAtividades) {
			custoTotalAlocado += grupoAtividade.getCustoTotalAlocado();
		}
		
		return custoTotalAlocado;
	}
	
	public float getCustoTotalLancado(){
		float custoTotalLancado = 0;
		
		for(GrupoAtividade grupoAtividade : gruposAtividades) {
			custoTotalLancado += grupoAtividade.getCustoTotalLancado();
		}
		
		return custoTotalLancado;
	}
	
	public float getTotalCustoRealProjeto() {
		
		float totalCustoRealProjeto = 0f;
		
		for (CustoRealProjeto custo : this.getCustosReaisProjeto()) {
			totalCustoRealProjeto += custo.getValor();
		}
		
		return totalCustoRealProjeto;
	}

	public Float getCustoAcordado() {
		return custoAcordado;
	}

	public void setCustoAcordado(Float custoAcordado) {
		this.custoAcordado = custoAcordado;
	}

}
