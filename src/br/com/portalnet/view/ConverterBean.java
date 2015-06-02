/**
 * @since 24/12/2013
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.view;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.Id;

import br.com.portalnet.control.ColaboradorServiceController;

/**
 * @author marco
 * 
 */
@ManagedBean(name = "converterBean")
@ApplicationScoped
public class ConverterBean implements Converter {

	@ManagedProperty(value = "#{colaboradorService}")
	private ColaboradorServiceController colaboradorService;


	public ColaboradorServiceController getColaboradorService() {
		return colaboradorService;
	}

	public void setColaboradorService(ColaboradorServiceController colaboradorService) {
		this.colaboradorService = colaboradorService;
	}

	public Object getAsObject(FacesContext fc, UIComponent component,
			String string) {
		try {
			if (string == null)
				return null;
			
			String[] split = string.split(":");
			Class<?> clazz = Class.forName(split[0]);
			for (Field f : clazz.getDeclaredFields()) {
				if (f.isAnnotationPresent(Id.class)) {
					Method valueOfMethod = f.getType().getMethod("valueOf", String.class);
					Object object = colaboradorService.getGenericObject(clazz,
									(Serializable) valueOfMethod.invoke(null, split[1]));
					
					return object;
				}
			}
		} catch (ClassNotFoundException cnfe) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
		
		return null;
	}

	public String getAsString(FacesContext fc, UIComponent component,
			Object object) {
		try {
			if (object == null)
				return "";
			
			Class<?> clazz = object.getClass();
			for (Field f : clazz.getDeclaredFields()) {
				if (f.isAnnotationPresent(Id.class)) {
					f.setAccessible(true);
					String retorno = clazz.getCanonicalName() + ":" + f.get(object);

					return retorno;
				}
			}
		} catch (IllegalAccessException e) {
		}
		
		return null;
	}
	
}
