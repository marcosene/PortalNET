/**
 * @since 12/02/2009
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import br.com.portalnet.util.DataUtil;
import br.com.portalnet.util.FacesUtils;


@ManagedBean(name="utilBean")
@SessionScoped
public class UtilBean {
	
	@ManagedProperty(value="#{sessionBean}")
	private SessionBean sessionBean;
	
	private int mesSelecionado;

	private int anoSelecionado;
	
	private Calendar dataInicio;
	
	private Calendar dataFim;
	

	public Calendar getDataInicio() {
		if (dataInicio == null) {
			dataInicio = DataUtil.getDataInicioContabil(sessionBean.getDataTrabalho(), sessionBean.getParametros().getPrimeiroDiaContabil());
		}
		return dataInicio;
	}

	public void setDataInicio(Calendar dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Calendar getDataFim() {
		if (dataFim == null) {
			Calendar dataTrabalho = sessionBean.getDataTrabalho();
			dataFim = DataUtil.getDataFimContabil(dataTrabalho, sessionBean.getParametros().getUltimoDiaContabil(dataTrabalho));
		}
		return dataFim;
	}

	public void setDataFim(Calendar dataFim) {
		this.dataFim = dataFim;
	}
	
	public String getDataInicioFormatada() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(this.getDataInicio().getTime());
	}
	
	public String getDataFimFormatada() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(this.getDataFim().getTime());
	}

	public int getMesSelecionado() {
		if (mesSelecionado == 0) {
			Calendar dataHoje = Calendar.getInstance();
			mesSelecionado = dataHoje.get(Calendar.MONTH)+1;
		}
		return mesSelecionado;
	}

	public void setMesSelecionado(int mesSelecionado) {
		this.mesSelecionado = mesSelecionado;
	}

	public int getAnoSelecionado() {
		if (anoSelecionado == 0) {
			Calendar dataHoje = Calendar.getInstance();
			anoSelecionado = dataHoje.get(Calendar.YEAR);
		}
		return anoSelecionado;
	}

	public void setAnoSelecionado(int anoSelecionado) {
		this.anoSelecionado = anoSelecionado;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	/**
	 * Retorna uma lista com os meses para impressao do relatorio
	 * 
	 * @return lista de meses
	 */
	public List<SelectItem> getListaMes() {
		int i=0;
		List<SelectItem> listaMes = new ArrayList<SelectItem>();
		String[] meses = {"Janeiro", "Fevereiro", "Março",
						  "Abril", "Maio", "Junho",
						  "Julho", "Agosto", "Setembro",
						  "Outubro", "Novembro", "Dezembro"};
		
		for( i = 1 ; i <= 12 ; i++ ){
			listaMes.add(new SelectItem(i, meses[i-1]));
		}
				
		return listaMes;
	}
	
	/**
	 * Retorna uma lista com os anos para visualizacao em comboBoxs
	 * 
	 * @return lista de anos
	 */
	public List<SelectItem> getListaAnosCombo() {
		int i=0;
		List<SelectItem> listaAno = new ArrayList<SelectItem>();
		
		for( i = 2009 ; i <= Calendar.getInstance().get(Calendar.YEAR) + 10 ; i++ ){
			listaAno.add(new SelectItem(i, String.valueOf(i)));
		}
				
		return listaAno;
	}
	
	/**
	 * Retorna uma lista com os anos para impressao do relatorio
	 * 
	 * @return lista de anos
	 */
	public List<SelectItem> getListaAno() {
		int i=0;
		List<SelectItem> listaAno = new ArrayList<SelectItem>();
		
		for( i = 2009 ; i <= Calendar.getInstance().get(Calendar.YEAR) ; i++ ){
			listaAno.add(new SelectItem(i, String.valueOf(i)));
		}
				
		return listaAno;
	}
	
	/**
	 * Retorna uma lista com as possiveis horas iniciais.
	 * Cada índice equivale a meia hora, por isso os 48 índices.
	 * 
	 * @return lista de horas
	 */
	public List<SelectItem> getListaHoras() {
		List<SelectItem> listaHoras = new ArrayList<SelectItem>();
		int i, j = 0;
		String hora;
		
		for (i = 0; i <= 48; i++){
			if (i % 2 == 0) {
				hora = String.format("%02d:00", j);
			}
			else {
				hora = String.format("%02d:30", j);
				j++;
			}

			listaHoras.add(new SelectItem(hora, hora));
		}

		return listaHoras;
	}

	/**
	 * Retorna uma lista com as possiveis horas finais.
	 * Cada índice equivale a meia hora, por isso os 48 índices.
	 * 
	 * @return lista de horas
	 */
	public List<SelectItem> getListaHorasFinais(){
		ColaboradorBean colaboradorBean = (ColaboradorBean) FacesUtils.getManagedBean("colaboradorBean");
		String horaInicial = colaboradorBean.getHoraInicial();
		List<SelectItem> listaHorasFinais = new ArrayList<SelectItem>();
		
		if (horaInicial != null && !horaInicial.isEmpty()) {
			int i, j = 0;
			String hora;
			
			float inicio = Integer.parseInt(horaInicial.substring(0,2));
			
			if (Integer.parseInt(horaInicial.substring(3, 4)) != 0) {
				inicio += 0.5f;
			}
		
			for (i = 0; i <= 48; i++) {
				if (i % 2 == 0) {						
					hora = String.format("%02d:00 (" + (j-inicio) + " h)", j);
				}
				else {
					hora = String.format("%02d:30 (" + (j-inicio+0.5)+ " h)", j);
					j++;
				}

				if (hora.substring(0, 5).compareTo(horaInicial) > 0)
					listaHorasFinais.add(new SelectItem(hora, hora));
			}			
		}
		return listaHorasFinais;
	}
		
	/**
	 * Retorna uma lista com todos os meses do ano.
	 * 
	 * @return lista de meses
	 */
	public List<String> getListaMeses(){
		List<String> listaMeses = new ArrayList<String>();
		
		listaMeses.add(FacesUtils.getMessage("mes_janeiro"));
		listaMeses.add(FacesUtils.getMessage("mes_fevereiro"));
		listaMeses.add(FacesUtils.getMessage("mes_marco"));
		listaMeses.add(FacesUtils.getMessage("mes_abril"));
		listaMeses.add(FacesUtils.getMessage("mes_maio"));
		listaMeses.add(FacesUtils.getMessage("mes_junho"));
		listaMeses.add(FacesUtils.getMessage("mes_julho"));
		listaMeses.add(FacesUtils.getMessage("mes_agosto"));
		listaMeses.add(FacesUtils.getMessage("mes_setembro"));
		listaMeses.add(FacesUtils.getMessage("mes_outubro"));
		listaMeses.add(FacesUtils.getMessage("mes_novembro"));
		listaMeses.add(FacesUtils.getMessage("mes_dezembro"));
	
		return listaMeses;
	}
	
	public void valueChangeListener(ValueChangeEvent event) {
		Object object = event.getNewValue(); 
		
		if (object instanceof GregorianCalendar) {
			Calendar data = (Calendar)object;
			
			this.setDataInicio(data);
			return;
		}
	}
}

