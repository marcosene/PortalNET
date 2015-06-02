/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import br.com.portalnet.util.FacesUtils;


@ManagedBean(name="validatorBean")
@ViewScoped
public class ValidatorBean {

	public void validateSenha(FacesContext context,
			UIComponent toValidate, Object value) {
		String senha = (String) value;

		if (senha == null || senha.contains(" ")) {
			((UIInput) toValidate).setValid(false);
			String message = FacesUtils.getMessage("cadastrarColaborador_senhaInvalida");
			FacesUtils.addErrorMessage(toValidate.getClientId(context), message);
		}
	}

	public void validateSenhaConfirmada(FacesContext context,
			UIComponent toValidate, Object value) {
		String senhaConfirmada = (String) value;
		String senha = FacesUtils.getRequestProperty("formGeral:senha");

		if (senhaConfirmada == null || senha == null || !senhaConfirmada.equals(senha)) {
			((UIInput) toValidate).setValid(false);
			String message = FacesUtils.getMessage("cadastro_senhaConfirmadaIncorreta");
			FacesUtils.addErrorMessage(toValidate.getClientId(context), message);
		}
	}
	

	public void validateHorario(FacesContext context,
			UIComponent toValidate, Object value) {
		String hora = (String) value;
		DateFormat formatoHora = new SimpleDateFormat("HH:mm");
		
		try {
			formatoHora.parse(hora).getTime();
			
		} catch (ParseException pe) {
			((UIInput) toValidate).setValid(false);
			String message = FacesUtils.getMessage("registrarAtividade_horaInvalida") + " - "
							+ FacesUtils.getMessage("registrarAtividade_helpFormatoHora");
			FacesUtils.addErrorMessage(toValidate.getClientId(context), message);
		}
	}
	
	public void validateDataInicioFim(FacesContext context,
			UIComponent toValidate, Object value) {
		UtilBean utilBean = (UtilBean)FacesUtils.getManagedBean("utilBean");
		Calendar dataFinal = (Calendar) value;
		Calendar dataInicial = utilBean.getDataInicio();
		
		if (dataInicial.after(dataFinal)) {
			((org.primefaces.component.calendar.Calendar) toValidate).setValid(false);			
			String message = FacesUtils.getMessage("visualizarRelatorio_erroValidacaoDatas");
			FacesUtils.addErrorMessage(toValidate.getClientId(context), message);
		}
	}
}
