/**
 * Arquivo: FacesUtils.java
 * @since Jul 18, 2007
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.faces.util.MessageFactory;


/**
 * Utility class for JavaServer Faces.
 */
public class FacesUtils {
	
	/**
	 * Get servlet context.
	 * 
	 * @return the servlet context
	 */
	public static ServletContext getServletContext() {
		return (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
	}
	
	/**
	 * Get managed bean based on the bean name.
	 * 
	 * @param beanName the bean name
	 * @return the managed bean associated with the bean name
	 */
	public static Object getManagedBean(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext elContext = context.getELContext();
		ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
		
		ValueExpression valueExpression = expressionFactory.createValueExpression(elContext, "#{"+beanName+"}", Object.class);
		
		return valueExpression.getValue(elContext);
	}
	
	/**
	 * Store the managed bean inside the session scope.
	 * 
	 * @param beanName the name of the managed bean to be stored
	 * @param managedBean the managed bean to be stored
	 */
	public static void setManagedBeanInSession(String beanName, Object managedBean) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
	}
	
	/**
	 * Get the session scope.
	 * 
	 * @return the session scope
	 */
	public static HttpSession getSession() {
		HttpServletRequest request = getRequest();
		return request.getSession();
	}
	
	/**
	 * Get the request scope.
	 * 
	 * @return the request scope
	 */
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	/**
	 * Get the response scope.
	 * 
	 * @return the response scope
	 */
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}
	
	/**
	 * Get property value from request scope.
	 * 
	 * @param name the name of the property
	 * @return the property value
	 */
	public static String getRequestProperty(String name) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	}
	
	/**
	 * Get parameter value from request scope.
	 * 
	 * @param name the name of the parameter
	 * @return the parameter value
	 */
	public static Object getRequestParameter(String name) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(name);
	}
	
	/**
	 * @param property
	 * @param params
	 * @return
	 */
	public static String getMessage(String property, Object...params) {
		return MessageFactory.getMessage(property, params).getSummary();
	}
	
	/**
	 * Add information message.
	 * 
	 * @param msg the information message
	 */
	public static void addInfoMessage(String msg) {
		addInfoMessage(null, msg);
	}
	
	/**
	 * Add information message to a sepcific client.
	 * 
	 * @param clientId the client id 
	 * @param msg the information message
	 */
	public static void addInfoMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
	}
	
	/**
	 * Add warning message.
	 * 
	 * @param msg the warning message
	 */
	public static void addWarningMessage(String msg) {
		addWarningMessage(null, msg);
	}
	
	/**
	 * Add error message to a specific client.
	 * 
	 * @param clientId the client id 
	 * @param msg the error message
	 */	
	public static void addWarningMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
	}
	
	/**
	 * Add error message.
	 * 
	 * @param msg the error message
	 */
	public static void addErrorMessage(String msg) {
		addErrorMessage(null, msg);
	}
	
	/**
	 * Add error message to a specific client.
	 * 
	 * @param clientId the client id 
	 * @param msg the error message
	 */	
	public static void addErrorMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
	}
	
}
