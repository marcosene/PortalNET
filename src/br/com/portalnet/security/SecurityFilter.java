/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.model.Tipos;
import br.com.portalnet.view.SessionBean;


public class SecurityFilter implements Filter {

	private static final String LOGIN_URI = "/login.jsf";
	
	/**
	 * Initializes the Filter.
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	/**
	 * Standard doFilter object.
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String requestUri = ((HttpServletRequest) req).getRequestURI();
		Integer tipoUsuario = this.getTipoUsuario((HttpServletRequest)req);
		Projeto projeto = this.getProjeto((HttpServletRequest)req);
		Colaborador colaborador = this.getColaborador((HttpServletRequest)req);
		boolean bloqueiaAcesso = false;
		
		if ((req instanceof HttpServletRequest) && (res instanceof HttpServletResponse)) {  
			HttpServletRequest httpServletRequest = (HttpServletRequest) req;  
			HttpServletResponse httpServletResponse = (HttpServletResponse) res;  

			// is session invalid?  
			if (isSessionInvalid(httpServletRequest)) {
				String timeoutUrl = httpServletRequest.getContextPath() + LOGIN_URI;
				httpServletResponse.sendRedirect(timeoutUrl);
				return;  
			}  
		}  
		
		if (requestUri.endsWith("visualizarAlocacoes.jsf") || requestUri.endsWith("cadastrarAlocacao.jsf")) {
			if (tipoUsuario == null ||
				(tipoUsuario.equals(Tipos.TIPO_USUARIO_COMUM) && projeto.getResponsavel().getId() != colaborador.getId())) {
				bloqueiaAcesso = true;
			}
			
		}else{
			
			if (!requestUri.contains("/javax.faces.resource/") &&
				!requestUri.contains("/rfRes/")) {
				
				if (requestUri.contains("/admin/")) {
					if (tipoUsuario == null ||
						!tipoUsuario.equals(Tipos.TIPO_USUARIO_ADMIN) && 
						!tipoUsuario.equals(Tipos.TIPO_USUARIO_ASSISTENTE)) {
						bloqueiaAcesso = true;
					}
				}
				
				if (requestUri.contains("/gestor/")) {
					if (tipoUsuario == null ||
						(!tipoUsuario.equals(Tipos.TIPO_USUARIO_ADMIN) &&
						 !tipoUsuario.equals(Tipos.TIPO_USUARIO_GG) &&
						 !tipoUsuario.equals(Tipos.TIPO_USUARIO_ASSISTENTE) &&
						 !tipoUsuario.equals(Tipos.TIPO_USUARIO_COORDENADOR))) {
						bloqueiaAcesso = true;
					}
				}
				
				if (!requestUri.endsWith(LOGIN_URI) && tipoUsuario == null) {
					bloqueiaAcesso = true;
				}	
			}
			
		}
		if (bloqueiaAcesso) {
			req.getRequestDispatcher(LOGIN_URI).forward(req, res);
		} else {
			chain.doFilter(req, res);
		}
	}
	
	/**
	 * @param req
	 * @return
	 */
	private Colaborador getColaborador(HttpServletRequest req) {
		SessionBean sessionBean = (SessionBean) req.getSession().getAttribute("sessionBean");
		
		if (sessionBean == null ||
				!sessionBean.isLoggedIn() ||
				sessionBean.getColaboradorLogado() == null) {
			return null;
		}
		
		return sessionBean.getColaboradorLogado();
	}
	
	/**
	 * @param req
	 * @return
	 */
	private Projeto getProjeto(HttpServletRequest req) {
		SessionBean sessionBean = (SessionBean) req.getSession().getAttribute("sessionBean");
		
		if (sessionBean == null ||
				!sessionBean.isLoggedIn() ||
				sessionBean.getColaboradorLogado() == null) {
			return null;
		}
		
		return sessionBean.getProjeto();
	}
	
	/**
	 * @param req
	 * @return
	 */
	private Integer getTipoUsuario(HttpServletRequest req) {
		SessionBean sessionBean = (SessionBean) req.getSession().getAttribute("sessionBean");
		
		if (sessionBean == null ||
				!sessionBean.isLoggedIn() ||
				sessionBean.getColaboradorLogado() == null) {
			return null;
		}
		
		return sessionBean.getColaboradorLogado().getTipoUsuario();
	}
	
	private boolean isSessionInvalid(HttpServletRequest httpServletRequest) {  
		return (httpServletRequest.getRequestedSessionId() != null) &&
			!httpServletRequest.isRequestedSessionIdValid();
	} 
	
	public void destroy() {
	}
	
}
