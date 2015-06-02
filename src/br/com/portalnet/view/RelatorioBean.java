/**
 * @since 12/02/2009
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.view;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.jdbc.datasource.DataSourceUtils;

import br.com.portalnet.control.GestorServiceController;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Ocorrencia;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.util.FacesUtils;
import br.com.portalnet.util.FileUtil;
import br.com.portalnet.util.StringUtil;
import br.com.portalnet.util.ZipUtil;


@ManagedBean(name="relatorioBean")
@ViewScoped
public class RelatorioBean {
	
	private static Map<Integer, String> listaTemplates = new HashMap<Integer, String>();
	
	@ManagedProperty(value="#{gestorService}")
	private GestorServiceController gestorService;
	
	@ManagedProperty(value="#{sessionBean}")
	private SessionBean sessionBean;
	
	private int relatorioSelecionado;
	
	private int tipoRelatorioSelecionado;
	
	private Colaborador colaborador;
	
	private int tipoSelecaoColaborador;
	

	/*** Relatorios ***/
	public static final int RELATORIO_ATIV_DIA = 1;
	public static final int RELATORIO_ATIV_PROJETO = 2;
	public static final int RELATORIO_PROJETO = 3;
	public static final int RELATORIO_CONTROLE_GERAL_PROJETO = 4;
	public static final int RELATORIO_CONTROLE_GERAL_COLABORADOR = 5;
	public static final int RELATORIO_ANALISE_OCORRENCIA = 6;
	public static final int RELATORIO_OCORRENCIAS_PROJETO = 7;
	
	/*** Templates dos Relatorios ***/
	public static final String TEMPLATE_ATIV_DIA = "RegAtivPorDia.jasper";
	public static final String TEMPLATE_ATIV_PROJETO = "RegAtivPorProjeto.jasper";
	public static final String TEMPLATE_PROJETO = "Projeto.jasper";
	public static final String TEMPLATE_CONTROLE_GERAL_PROJETO = "ControleGeralPorProjeto.jasper";
	public static final String TEMPLATE_CONTROLE_GERAL_COLABORADOR = "ControleGeralPorColaborador.jasper";
	public static final String TEMPLATE_ANALISE_OCORRENCIA = "Ocorrencia.jasper";
	public static final String TEMPLATE_OCORRENCIAS_PROJETO = "OcorrenciasProjeto.jasper";
	
	
	/*** Tipos de Relatorios ***/
	public static final int TIPO_RELATORIO_HTML = 1;
	public static final int TIPO_RELATORIO_PDF = 2;
	public static final int TIPO_RELATORIO_RTF = 3;
	public static final int TIPO_RELATORIO_XLS = 4;
	
	
	static {
		listaTemplates.put(RELATORIO_ATIV_DIA, TEMPLATE_ATIV_DIA);
		listaTemplates.put(RELATORIO_ATIV_PROJETO, TEMPLATE_ATIV_PROJETO);
		listaTemplates.put(RELATORIO_PROJETO, TEMPLATE_PROJETO);
		listaTemplates.put(RELATORIO_CONTROLE_GERAL_PROJETO, TEMPLATE_CONTROLE_GERAL_PROJETO);
		listaTemplates.put(RELATORIO_CONTROLE_GERAL_COLABORADOR, TEMPLATE_CONTROLE_GERAL_COLABORADOR);
		listaTemplates.put(RELATORIO_ANALISE_OCORRENCIA, TEMPLATE_ANALISE_OCORRENCIA);
		listaTemplates.put(RELATORIO_OCORRENCIAS_PROJETO, TEMPLATE_OCORRENCIAS_PROJETO);
	}
	
	public void setGestorService(GestorServiceController gestorService) {
		this.gestorService = gestorService;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	/**
	 * @return the relatorioSelecionado
	 */
	public int getRelatorioSelecionado() {
		return relatorioSelecionado;
	}

	/**
	 * @param relatorioSelecionado the relatorioSelecionado to set
	 */
	public void setRelatorioSelecionado(int relatorioSelecionado) {
		this.relatorioSelecionado = relatorioSelecionado;
	}

	/**
	 * @return the tipoRelatorioSelecionado
	 */
	public int getTipoRelatorioSelecionado() {
		if (tipoRelatorioSelecionado == 0) {
			tipoRelatorioSelecionado = TIPO_RELATORIO_PDF;
		}
		return tipoRelatorioSelecionado;
	}

	/**
	 * @param tipoRelatorioSelecionado the tipoRelatorioSelecionado to set
	 */
	public void setTipoRelatorioSelecionado(int tipoRelatorioSelecionado) {
		this.tipoRelatorioSelecionado = tipoRelatorioSelecionado;
	}
	
	public Colaborador getColaborador() {
		if (colaborador == null) {
			colaborador = sessionBean.getColaboradorLogado();
		}
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	
	public int getTipoSelecaoColaborador() {
		return tipoSelecaoColaborador;
	}

	public void setTipoSelecaoColaborador(int tipoSelecaoColaborador) {
		this.tipoSelecaoColaborador = tipoSelecaoColaborador;
	}

	/**
	 * Retorna uma lista com os tipos de impressao de relatorio
	 * 
	 * @return lista de tipos de impressao de relatorio
	 */
	public List<SelectItem> getListaTipoRelatorios() {
		List<SelectItem> listaTipoRelatorios = new ArrayList<SelectItem>();
		
		listaTipoRelatorios.add(new SelectItem(TIPO_RELATORIO_HTML, "HTML"));
		listaTipoRelatorios.add(new SelectItem(TIPO_RELATORIO_PDF, "PDF"));
		listaTipoRelatorios.add(new SelectItem(TIPO_RELATORIO_RTF, "RTF"));
		listaTipoRelatorios.add(new SelectItem(TIPO_RELATORIO_XLS, "XLS"));
		
		return listaTipoRelatorios;
	}
	
	/**
	 * @param parameters
	 * @param relatorioSelecionado
	 */
	private void configurarParametros(Map<String,Object> parameters, int relatorioSelecionado) {
		UtilBean utilBean = (UtilBean)FacesUtils.getManagedBean("utilBean");

		parameters.put(JRParameter.REPORT_LOCALE, new Locale("pt","BR"));
		parameters.put(JRXPathQueryExecuterFactory.XML_LOCALE, new Locale("pt","BR"));
		
		switch(relatorioSelecionado) {
			case RELATORIO_ATIV_DIA:
			case RELATORIO_ATIV_PROJETO:
				parameters.put("dataInicio", utilBean.getDataInicio().getTime());
				parameters.put("dataFim", utilBean.getDataFim().getTime());
				parameters.put("usuario", this.getColaborador().getUsuario());
				break;
	
			case RELATORIO_PROJETO:
			case RELATORIO_OCORRENCIAS_PROJETO:
				parameters.put("dataInicio", utilBean.getDataInicio().getTime());
				parameters.put("dataFim", utilBean.getDataFim().getTime());
				parameters.put("codProjeto", sessionBean.getProjeto().getCodigo());
				break;
	
			case RELATORIO_CONTROLE_GERAL_PROJETO:
			case RELATORIO_CONTROLE_GERAL_COLABORADOR:
				parameters.put("dataInicio", utilBean.getDataInicio().getTime());
				parameters.put("dataFim", utilBean.getDataFim().getTime());
				parameters.put("idCliente", sessionBean.getCliente().getId());
				break;
				
			case RELATORIO_ANALISE_OCORRENCIA:
				parameters.put("idOcorrencia", Integer.parseInt(FacesUtils.getRequestProperty("idOcorrencia")));
				break;
		}
	}
	
	/**
	 * @param outputStream
	 * @param relatorioSelecionado
	 * @param tipoRelatorioSelecionado
	 */
	private void imprimirRelatorio(OutputStream outputStream,
			int relatorioSelecionado, int tipoRelatorioSelecionado) {
		Map<String,Object> parameters = new HashMap<String, Object>();
		DataSource dataSource = (DataSource) FacesUtils.getManagedBean("dataSource");
		Connection connection = DataSourceUtils.getConnection(dataSource);
		
		try {
			configurarParametros(parameters, relatorioSelecionado);
			
			String nomeTemplate = listaTemplates.get(relatorioSelecionado);
			File reportFile = new File(FacesUtils.getServletContext().getRealPath("/relatorios/" + nomeTemplate));
			
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
			JRExporter exporter = null;			
	
			switch (tipoRelatorioSelecionado) {
				case TIPO_RELATORIO_HTML:
					exporter = new JRHtmlExporter();
					exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap<String,String>());
					exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, FacesUtils.getRequest().getContextPath() + "/image?image=");
					break;
					
				case TIPO_RELATORIO_PDF:
					exporter = new JRPdfExporter();
					break;
					
				case TIPO_RELATORIO_RTF:
					exporter = new JRRtfExporter();
					break;
					
				case TIPO_RELATORIO_XLS:
					exporter = new JRXlsExporter();
					break;
			}
	
			exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
		    
		} catch (JRException je) {
			je.printStackTrace();
	    }
	    
	    finally {
	    	try {
	    		connection.close();
	    	} catch (SQLException e) {
			}
	    }
	}
	
	private String getNomeRelatorio() {
		UtilBean utilBean = (UtilBean)FacesUtils.getManagedBean("utilBean");
		String nomeRelatorio, nomeCompleto, nomeProjeto;
		DateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy");
		
		switch(relatorioSelecionado) {
			case RELATORIO_ATIV_DIA:
			case RELATORIO_ATIV_PROJETO:
				nomeRelatorio = this.getColaborador().getNomeCompleto()+"_"+formatoData.format(utilBean.getDataInicio().getTime())+ "_" + formatoData.format(utilBean.getDataFim().getTime());;
				break;
	
			case RELATORIO_PROJETO:
			case RELATORIO_OCORRENCIAS_PROJETO:
				if (sessionBean.getProjeto().isGenerico()) {
					nomeProjeto = sessionBean.getProjeto().getCodigo() + " - " + sessionBean.getProjeto().getNome();
				} else {
					nomeProjeto = sessionBean.getProjeto().getCodigoClienteCompleto();
				}
				nomeRelatorio = ((relatorioSelecionado == RELATORIO_OCORRENCIAS_PROJETO) ? "Bugs_" : "") + 
						nomeProjeto + "_" + formatoData.format(utilBean.getDataInicio().getTime())+ "_" + formatoData.format(utilBean.getDataFim().getTime());
				break;
	
			case RELATORIO_CONTROLE_GERAL_PROJETO:
				nomeRelatorio = "ControleGeralPorProjeto_" + formatoData.format(utilBean.getDataInicio().getTime())+ "_" + formatoData.format(utilBean.getDataFim().getTime());
				break;
				
			case RELATORIO_CONTROLE_GERAL_COLABORADOR:
				nomeRelatorio = "ControleGeralPorColaborador_" + formatoData.format(utilBean.getDataInicio().getTime())+ "_" + formatoData.format(utilBean.getDataFim().getTime());
				break;
				
			case RELATORIO_ANALISE_OCORRENCIA:
				Ocorrencia ocorrencia = gestorService.getOcorrenciaPK(Long.valueOf(FacesUtils.getRequestProperty("idOcorrencia")));
				if (ocorrencia.getNumBugCliente() != null)
					nomeRelatorio = ocorrencia.getNumBugCliente();
				else
					nomeRelatorio = ocorrencia.getNumOcorrencia();
				break;
				
			default:
				nomeRelatorio = "Relatorio";
				break;
		}
		
		switch (getTipoRelatorioSelecionado()) {
			case TIPO_RELATORIO_HTML:
				nomeCompleto = nomeRelatorio + ".html";
				break;
				
			case TIPO_RELATORIO_PDF:
				nomeCompleto = nomeRelatorio + ".pdf";
				break;
				
			case TIPO_RELATORIO_RTF:
				nomeCompleto = nomeRelatorio + ".rtf";
				break;
				
			case TIPO_RELATORIO_XLS:
				nomeCompleto = nomeRelatorio + ".xls";
				break;
				
			default:
				nomeCompleto = nomeRelatorio;
		}
		
		return nomeCompleto;
	}

	/**
	 * @return
	 */
	public String relatorioAction() {
		
		// Neste caso gera o relatorio de todos os colaboradores
		if (tipoSelecaoColaborador == 1) {
			return gerarRelatoriosColaboradoresAction();
		}
		
		try {
			HttpServletResponse resp = FacesUtils.getResponse();
			ServletOutputStream servletOutputStream = resp.getOutputStream();

			if(this.relatorioSelecionado == 0){
				this.relatorioSelecionado = (Integer) FacesUtils.getRequestParameter("relatorioSelecionado");
			}
			
			switch (getTipoRelatorioSelecionado()) {
				case TIPO_RELATORIO_HTML:
					resp.setContentType("text/html");
					break;
					
				case TIPO_RELATORIO_PDF:
					resp.setContentType("application/pdf");
					resp.setHeader("Content-disposition", "attachment; filename=" + getNomeRelatorio());
					break;
					
				case TIPO_RELATORIO_RTF:
					resp.setContentType("application/rtf");
					resp.setHeader("Content-Disposition", "inline; filename=\"" + getNomeRelatorio() + "\"");
					break;
					
				case TIPO_RELATORIO_XLS:
					resp.setContentType("application/xls");
					resp.setHeader("Content-Disposition", "inline; filename=\"" + getNomeRelatorio() + "\"");
					break;
			}
			
			imprimirRelatorio(servletOutputStream, getRelatorioSelecionado(), getTipoRelatorioSelecionado());
			
		    servletOutputStream.flush();
		    servletOutputStream.close();
		    FacesContext.getCurrentInstance().responseComplete();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return NavigationResults.SUCCESS;
	}
	
	/**
	 * @return
	 */
	public String gerarRelatoriosAction() {
		String diretorio = FacesUtils.getServletContext().getRealPath(File.separator) + "RelatoriosTemp\\";
		UtilBean utilBean = (UtilBean)FacesUtils.getManagedBean("utilBean");
		File dirRelatorios = new File(diretorio);
		DateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			dirRelatorios.mkdirs();
			
			File arqRelatorio = new File(diretorio + "ControleGeralPorColaborador.pdf");
			FileOutputStream fileOutputStream = new FileOutputStream(arqRelatorio);
			
			imprimirRelatorio(fileOutputStream, RELATORIO_CONTROLE_GERAL_COLABORADOR, TIPO_RELATORIO_PDF);
			
		    fileOutputStream.flush();
		    fileOutputStream.close();
		    
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		try {
			File arqRelatorio = new File(diretorio + "ControleGeralPorProjeto.pdf");
			FileOutputStream fileOutputStream = new FileOutputStream(arqRelatorio);
			
			imprimirRelatorio(fileOutputStream, RELATORIO_CONTROLE_GERAL_PROJETO, TIPO_RELATORIO_PDF);
			
		    fileOutputStream.flush();
		    fileOutputStream.close();
		    
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		for (Projeto projeto : gestorService.getListaProjetosComLancamentos(sessionBean.getCliente(),
				utilBean.getDataInicio(), utilBean.getDataFim())) {
			try {
				String diretorioProjeto;
				String nomeProjeto;
				
				if (projeto.isGenerico()) {
					nomeProjeto = projeto.getCodigo() + " - " + projeto.getNome();
				} else {
					nomeProjeto = projeto.getCodigoClienteCompleto();
				}
				
				diretorioProjeto = diretorio + projeto.getProduto().getArea().getNome() + "\\";

				File dirProjeto = new File(diretorioProjeto);
				dirProjeto.mkdirs();
				
				File arqRelatorio = new File(diretorioProjeto + nomeProjeto + ".pdf");
				FileOutputStream fileOutputStream = new FileOutputStream(arqRelatorio);
				
				sessionBean.setProjeto(projeto);
				imprimirRelatorio(fileOutputStream, RELATORIO_PROJETO, TIPO_RELATORIO_PDF);
				
				if(projeto.getOcorrencias().size()>0){
					diretorioProjeto += FacesUtils.getMessage("relatorios_geracaoRelatorioPorPeriodoBugs") + "\\";
					
					dirProjeto = new File(diretorioProjeto);
					dirProjeto.mkdirs();
					
					arqRelatorio = new File(diretorioProjeto + nomeProjeto + ".pdf");
					fileOutputStream = new FileOutputStream(arqRelatorio);
					
					imprimirRelatorio(fileOutputStream, RELATORIO_OCORRENCIAS_PROJETO, TIPO_RELATORIO_PDF);
				}
								
			    fileOutputStream.flush();
			    fileOutputStream.close();
			    
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		try {
			String nomeArqZip = "Relatorios_"+ formatoData.format(utilBean.getDataInicio().getTime())+ "_" + formatoData.format(utilBean.getDataFim().getTime())+".zip";
			
			File zipRelatorios = new File(FacesUtils.getServletContext().getRealPath(File.separator) + nomeArqZip);
			ZipUtil.zip(dirRelatorios.listFiles(), zipRelatorios);
			
	        int length = 0;
			HttpServletResponse resp = FacesUtils.getResponse();
			ServletOutputStream servletOutputStream = resp.getOutputStream();
	        ServletContext context = FacesUtils.getServletContext();
	        String mimetype = context.getMimeType( nomeArqZip );

	        resp.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
	        resp.setContentLength( (int)zipRelatorios.length() );
	        resp.setHeader( "Content-Disposition", "attachment; filename=\"" + nomeArqZip + "\"" );

	        byte[] bbuf = new byte[1024];
	        DataInputStream in = new DataInputStream(new FileInputStream(zipRelatorios));

	        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
	        	servletOutputStream.write(bbuf,0,length);
	        }

	        in.close();
	        servletOutputStream.flush();
	        servletOutputStream.close();
	        FacesContext.getCurrentInstance().responseComplete();
	        
			FileUtil.delete(dirRelatorios);
	        FileUtil.delete(zipRelatorios);
	        
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return NavigationResults.SUCCESS;
	}

	/**
	 * @return
	 */
	public String gerarRelatoriosColaboradoresAction() {
		String diretorio = FacesUtils.getServletContext().getRealPath(File.separator) + "RelatoriosTemp\\";
		UtilBean utilBean = (UtilBean)FacesUtils.getManagedBean("utilBean");
		File dirRelatorios = new File(diretorio);
		DateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			dirRelatorios.mkdirs();
			
		    for (Colaborador colaborador : gestorService.getListaColaboradoresParticipativos()) {
		    	this.setColaborador(colaborador);
		    	
		    	String nomeRelatorioSemAcento = StringUtil.substituirAcentuacao(getNomeRelatorio());
				File arqRelatorio = new File(diretorio + nomeRelatorioSemAcento);
				FileOutputStream fileOutputStream = new FileOutputStream(arqRelatorio);
				
				imprimirRelatorio(fileOutputStream, this.getRelatorioSelecionado(), this.getTipoRelatorioSelecionado());
				
			    fileOutputStream.flush();
			    fileOutputStream.close();
			}
		    
		    this.setColaborador(null);
			    
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		try {
			String nomeArqZip = "Colaboradores_" + formatoData.format(utilBean.getDataInicio().getTime())+ "_" + formatoData.format(utilBean.getDataFim().getTime())+".zip";
			
			File zipRelatorios = new File(FacesUtils.getServletContext().getRealPath(File.separator) + nomeArqZip);
			ZipUtil.zip(dirRelatorios.listFiles(), zipRelatorios);
			
	        int length = 0;
			HttpServletResponse resp = FacesUtils.getResponse();
			ServletOutputStream servletOutputStream = resp.getOutputStream();
	        ServletContext context = FacesUtils.getServletContext();
	        String mimetype = context.getMimeType( nomeArqZip );

	        resp.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
	        resp.setContentLength( (int)zipRelatorios.length() );
	        resp.setHeader( "Content-Disposition", "attachment; filename=\"" + nomeArqZip + "\"" );

	        byte[] bbuf = new byte[1024];
	        DataInputStream in = new DataInputStream(new FileInputStream(zipRelatorios));

	        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
	        	servletOutputStream.write(bbuf,0,length);
	        }

	        in.close();
	        servletOutputStream.flush();
	        servletOutputStream.close();
	        FacesContext.getCurrentInstance().responseComplete();
	        
			FileUtil.delete(dirRelatorios);
	        FileUtil.delete(zipRelatorios);
	        
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return NavigationResults.SUCCESS;
	}

}
