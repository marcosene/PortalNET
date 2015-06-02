package br.com.portalnet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.StringUtils;
import org.primefaces.component.calendar.Calendar;


@FacesConverter("calendarConverter")
public class CalendarConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		java.util.Calendar calendar = null;
		Date convertedToDate;

		if (!StringUtils.isBlank(value)) {
			convertedToDate = convertToDate(context, (Calendar) component, value);
			calendar = java.util.Calendar.getInstance();
			calendar.setTime(convertedToDate);
		}

		return calendar;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String convertedValue = null;

		if (value != null) {
			convertedValue = convertToString(context, (Calendar) component, value);
		}

		return convertedValue;
	}

	protected Date convertToDate(FacesContext context, Calendar pfCalendarComponent, String submittedValue) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
		format.setTimeZone(pfCalendarComponent.calculateTimeZone());

		try {
			return format.parse(submittedValue);
		} catch (ParseException e) {
			throw new ConverterException(e);
		}
	}

	protected String convertToString(FacesContext context, Calendar pfCalendarComponent, Object value) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		dateFormat.setTimeZone(pfCalendarComponent.calculateTimeZone());

		return dateFormat.format(((java.util.Calendar) value).getTime());
	}
}
