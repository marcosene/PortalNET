/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.com.portalnet.model.AlocacaoAtividade;
import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Feriados;
import br.com.portalnet.model.Ferias;
import br.com.portalnet.model.RegistroAtividade;
import br.com.portalnet.view.SessionBean;


public abstract class DataUtil {

	public static String getHoraFormatada(Calendar hora) {
		DateFormat formatoHora = new SimpleDateFormat("HH:mm");
		return formatoHora.format(hora.getTime());
	}
	
	public static String getDataFormatada(Calendar data) {
		DateFormat formatoHora = new SimpleDateFormat("dd/MM/yy");
		return formatoHora.format(data.getTime());
	}
	
	public static boolean colaboradorTrabalhaNoDia(Colaborador colaborador, Calendar data, boolean usaFerias) {
		SessionBean sessionBean = (SessionBean)FacesUtils.getManagedBean("sessionBean");
		List<Feriados> listaFeriados = sessionBean.getListaFeriados();
		List<Ferias> listaFerias = colaborador.getFerias();

		// Se não for utilizar as férias entra nessa condição
		if (usaFerias==false)
		{
			// Verifica férias
			for(Ferias ferias : listaFerias){
				if(data.equals(ferias.getDataInicioFerias()) ||
					data.equals(ferias.getDataFimFerias()) ||
					(data.before(ferias.getDataFimFerias()) && data.after(ferias.getDataInicioFerias()))) {
					return false;
				}
			}	
		}
				
		// Verifica feriados
		for(Feriados feriado : listaFeriados){
			if(data.equals(feriado.getDataFeriado())) {
				return false;
			}
		}

		// Verifica flags
		switch(data.get(Calendar.DAY_OF_WEEK)){
			case(Calendar.SUNDAY):
				if(!colaborador.isFlagTrabDomingo())
					return false;
				break;
				
			case(Calendar.MONDAY):
				if(!colaborador.isFlagTrabSegunda())
					return false;
				break;
			
			case(Calendar.TUESDAY):
				if(!colaborador.isFlagTrabTerca())
					return false;
				break;
				
			case(Calendar.WEDNESDAY):
				if(!colaborador.isFlagTrabQuarta())
					return false;
				break;
				
			case(Calendar.THURSDAY):
				if(!colaborador.isFlagTrabQuinta())
					return false;
				break;
					
			case(Calendar.FRIDAY):
				if(!colaborador.isFlagTrabSexta())
					return false;
				break;
					
			case(Calendar.SATURDAY):
				if(!colaborador.isFlagTrabSabado())
					return false;
				break;
			
			default:
				return false;
		}
		return true;
	}
	
	public static Calendar avancarDataTrabalho(Calendar dataInicio, float qtdeHorasTrabalho, AlocacaoAtividade alocacao) {
		Calendar dataFim;
		int qtdeDiasUteis;
		
		if(dataInicio == null) {
			return null;
		}
		
		dataFim = (Calendar) dataInicio.clone();
		qtdeDiasUteis = (int) (qtdeHorasTrabalho / alocacao.getQtdeHorasDiariasAlocadas()) +
								(qtdeHorasTrabalho % alocacao.getQtdeHorasDiariasAlocadas() == 0 ? 0 : 1);
		
		while (qtdeDiasUteis > 1) {
			dataFim.add(Calendar.DAY_OF_YEAR, 1);
			
			if(!colaboradorTrabalhaNoDia(alocacao.getColaborador(), dataFim, false))
				continue;
			
			qtdeDiasUteis--;
			
		}
		
		return dataFim;
	}
	
	public static int getQtdeDiasUteis(Calendar dataInicio, Calendar dataFim, Colaborador colaborador) {
		int qtdeDiasUteis = 0;
		Calendar dataInicioAux, dataFimAux;
		
		if(dataInicio == null || dataFim == null || dataFim.before(dataInicio)) {
			return 0;
		}
		
		dataInicioAux = (Calendar) dataInicio.clone();
		dataInicioAux.set(Calendar.HOUR_OF_DAY, 0);
		dataInicioAux.set(Calendar.MINUTE, 0);
		dataInicioAux.set(Calendar.SECOND, 0);
		dataInicioAux.set(Calendar.MILLISECOND, 0);
		
		dataFimAux = (Calendar) dataFim.clone();
		dataFimAux.set(Calendar.HOUR_OF_DAY, 23);
		dataFimAux.set(Calendar.MINUTE, 59);
		dataFimAux.set(Calendar.SECOND, 59);
		
		do{
			dataInicioAux.add(Calendar.DAY_OF_YEAR, 1);
			
			if(!colaboradorTrabalhaNoDia(colaborador, dataInicioAux, false))
				continue;
			
			qtdeDiasUteis++;
			
		}while(dataInicioAux.before(dataFimAux));
		
		return qtdeDiasUteis;
	}
	
	public static boolean validaIntervaloHoraRegistro(Calendar horaInicial, Calendar horaFinal,
			List<RegistroAtividade> listaRegistros) {
		
		for(RegistroAtividade registro : listaRegistros) {
			if ( (horaInicial.equals(registro.getHoraInicial())) ||
				 (horaFinal.equals(registro.getHoraFinal())) ||
				 (horaInicial.after(registro.getHoraInicial()) && horaInicial.before(registro.getHoraFinal())) ||
				 (horaFinal.after(registro.getHoraInicial()) && horaFinal.before(registro.getHoraFinal())) ||
				 (horaInicial.before(registro.getHoraInicial()) && horaFinal.after(registro.getHoraFinal()) && !registro.getHoraFinal().toString().equals("00:00:00"))) {
				return false;
			}
		}
		
		return true;
	}
	
	public static Calendar getDataInicioContabil(Calendar dataTrabalho, int primeiroDiaContabil) {
		Calendar dataInicio = (Calendar)dataTrabalho.clone();
		dataInicio.set(Calendar.HOUR_OF_DAY, 0);
		dataInicio.set(Calendar.MINUTE, 0);
		dataInicio.set(Calendar.SECOND, 0);
		dataInicio.set(Calendar.MILLISECOND, 0);
			
		if (dataInicio.get(Calendar.DAY_OF_MONTH) < primeiroDiaContabil)
			dataInicio.add(Calendar.MONTH, -1);
		
		dataInicio.set(dataInicio.get(Calendar.YEAR), dataInicio.get(Calendar.MONTH), primeiroDiaContabil);

		return dataInicio;
	}
	
	public static Calendar getDataFimContabil(Calendar dataTrabalho, int ultimoDiaContabil) {
		Calendar dataFim = (Calendar)dataTrabalho.clone();
		dataFim.set(Calendar.HOUR_OF_DAY, 23);
		dataFim.set(Calendar.MINUTE, 59);
		dataFim.set(Calendar.SECOND, 59);
		
		if (dataFim.get(Calendar.DAY_OF_MONTH) > ultimoDiaContabil)
			dataFim.add(Calendar.MONTH, 1);
		
		dataFim.set(dataFim.get(Calendar.YEAR), dataFim.get(Calendar.MONTH), ultimoDiaContabil);

		return dataFim;
	}
	
}
