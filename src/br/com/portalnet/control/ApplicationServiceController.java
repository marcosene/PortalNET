package br.com.portalnet.control;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.com.portalnet.model.Colaborador;
import br.com.portalnet.model.Compromisso;
import br.com.portalnet.model.Projeto;
import br.com.portalnet.util.EmailUtil;

import org.springframework.beans.factory.annotation.Autowired;


public class ApplicationServiceController extends Thread {
	
	// 3 em 3 minutos
	private final int CHECKTIME = 3 * 60 * 1000;
	
	private EmailUtil emailUtil;
	
	private boolean threadRunning;

	@Autowired
	private GestorServiceController gestorService;
	
	@Autowired
	private HibernateTransactionManager txManager;
		

	public void setGestorService(GestorServiceController gestorService) {
		this.gestorService = gestorService;
	}
	
	public void setTxManager(HibernateTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	@PostConstruct    
	public void init() {
		String assinaturaEmail;
		Locale.setDefault(new Locale("pt", "BR", ""));
		assinaturaEmail = "\n\nEste é um email automático, não há necessidade de respondê-lo.\nAtenciosamente,\nCentral do Colaborador";
		emailUtil = new EmailUtil("smtp.facti-rp.com.br", true, "automanager@facti-rp.com.br", "manager2009", assinaturaEmail);
		
		//inicia a thread com escopo de aplicacao
		this.start();
		
		threadRunning = true;
	} 
	
	@Transactional
	@Override
	public void run () {
		try {
			Calendar dtHoraUltProcesso = Calendar.getInstance();

			while (txManager.getSessionFactory().isClosed()) { 
				Thread.sleep(CHECKTIME);
			}
	
			//a thread fica ativa enquando a aplicacao esta no ar
			while(threadRunning) {
				
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				
				// Se a conexao nao esta fechada e ja chegou no periodo de verificar o processamento
				if (!txManager.getSessionFactory().isClosed()) {
					if (Calendar.getInstance().getTimeInMillis() - dtHoraUltProcesso.getTimeInMillis() >= CHECKTIME) {
						TransactionStatus status = txManager.getTransaction(def);
						enviaEmailCompromisso();
						txManager.commit(status);
						
						dtHoraUltProcesso = Calendar.getInstance();
					}

				} else {
					threadRunning = false;
				}
				
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			
		}
	}

	protected void enviaEmailCompromisso() {
		List<Compromisso> listaCompromissos;
		Calendar dataAtual;
		
		// recuperando a data atual
		dataAtual = Calendar.getInstance();
		
		// zerando a hora para que a comparacao seja efetuada apenas pela data
		dataAtual.set(Calendar.HOUR, 0);
		dataAtual.set(Calendar.HOUR_OF_DAY, 0);
		dataAtual.set(Calendar.MINUTE, 0);
		dataAtual.set(Calendar.SECOND, 0);
		dataAtual.set(Calendar.MILLISECOND, 0);
		
		// recebendo a lista de compromissos que ainda nao enviou email
		listaCompromissos = gestorService.getListaCompromissosEmail();
		
		for(Compromisso compromisso: listaCompromissos) {
			Projeto projeto = compromisso.getProjeto();
			
			//Indica se há email para ser enviado						
			if (isEmail(compromisso)) {

				// envio de email ao coordenador caso nao seja ele mesmo o responsavel
				if(projeto.getProduto().getCoordenador() != null 
					&& !compromisso.getColaboradores().contains(projeto.getProduto().getCoordenador())) {
					// envio de email aos coordenadores do projeto sobre o compromisso do dia atual
					emailUtil.sendEmail(projeto.getProduto().getCoordenador().getEmail(), "[FACTI_NET] Lembrete de Compromisso ao Coordenador",
							"Olá "+projeto.getProduto().getCoordenador().getApelido()+",\n\nO compromisso \""+ compromisso.getNome()+ "\", do projeto \"" + projeto.getNome()+
							"\", \"" + projeto.getCodigo()+ "\", pelo qual o(s) colaborador(es) "+compromisso.getNomeResponsaveis()+" é(são) responsável(is), está agendado para hoje.\n");
					compromisso.setDataUltimoEnvio(Calendar.getInstance());
					
				}
		
				for (Colaborador responsavel : compromisso.getColaboradores()) {
					// envio de email ao responsavel pelo compromisso
					emailUtil.sendEmail(responsavel.getEmail(), "[FACTI_NET] Lembrete de Compromisso ao Colaborador", 
								"Olá "+responsavel.getApelido()+",\n\nO compromisso \""+ compromisso.getNome()+ "\", do projeto \"" + projeto.getNome()+
									"\", \"" + projeto.getCodigo()+ "\", está agendado para hoje.\n");
					compromisso.setDataUltimoEnvio(Calendar.getInstance());
					
				}
				
				if(compromisso.getTipoRepeticao()==0){
					//Sem repetição
					compromisso.setFlagEnvioEmail(true);
				}						
				gestorService.atualizarCompromisso(compromisso);
			}
		}
	}
	
	private boolean isEmail(Compromisso compromisso){
		boolean enviaEmail = false;
		Calendar dataAtual;
		
		// recuperando a data atual
		dataAtual = Calendar.getInstance();
		
		// zerando a hora para que a comparacao seja efetuada apenas pela data
		dataAtual.set(Calendar.HOUR, 0);
		dataAtual.set(Calendar.HOUR_OF_DAY, 0);
		dataAtual.set(Calendar.MINUTE, 0);
		dataAtual.set(Calendar.SECOND, 0);
		dataAtual.set(Calendar.MILLISECOND, 0);
		
		if ((compromisso.getDataUltimoEnvio() == null)
				|| (!compromisso.getDataUltimoEnvio().equals(dataAtual))) {

			// Verificando se é necessário enviar email, tendo como base o tipo
			// de repeticao
			switch (compromisso.getTipoRepeticao()) {
			case 0: // Sem repeticao

				if (compromisso.getDataEvento().equals(dataAtual)) {
					enviaEmail = true;
				}
				break;

			case 1: // Semanal
				int diaSemana = dataAtual.get(Calendar.DAY_OF_WEEK);

				if (diaSemana == 1) {
					enviaEmail = compromisso.isFlagRepeticaoDomingo();
				} else if (diaSemana == 2) {
					enviaEmail = compromisso.isFlagRepeticaoSegunda();
				} else if (diaSemana == 3) {
					enviaEmail = compromisso.isFlagRepeticaoTerca();
				} else if (diaSemana == 4) {
					enviaEmail = compromisso.isFlagRepeticaoQuarta();
				} else if (diaSemana == 5) {
					enviaEmail = compromisso.isFlagRepeticaoQuinta();
				} else if (diaSemana == 6) {
					enviaEmail = compromisso.isFlagRepeticaoSexta();
				} else if (diaSemana == 7) {
					enviaEmail = compromisso.isFlagRepeticaoSabado();
				}

				break;

			case 2: // Mensal

				// verifica se o dia do mês para envio de email é o dia atual 
				if (compromisso.getDataEvento().get(Calendar.DAY_OF_MONTH) == dataAtual.get(Calendar.DAY_OF_MONTH)) {
					enviaEmail = true;
				}else if (dataAtual.getActualMaximum(Calendar.DAY_OF_MONTH) < compromisso.getDataEvento().get(Calendar.DAY_OF_MONTH)){
					if(dataAtual.getActualMaximum(Calendar.DAY_OF_MONTH) == dataAtual.get(Calendar.DAY_OF_MONTH)){
						enviaEmail = true;
					}
				}

				break;

			case 3: // Anual
				
				// verifica se o dia/mês para envio de email é o dia atual
				if (compromisso.getDataEvento().get(Calendar.DAY_OF_MONTH) == dataAtual.get(Calendar.DAY_OF_MONTH)
					&& compromisso.getDataEvento().get(Calendar.MONTH) == dataAtual.get(Calendar.MONTH)) {
					enviaEmail = true;
				}else if (compromisso.getDataEvento().get(Calendar.MONTH) == dataAtual.get(Calendar.MONTH)){
					if (compromisso.getDataEvento().get(Calendar.DAY_OF_MONTH) == dataAtual.get(Calendar.DAY_OF_MONTH)){
						enviaEmail = true;
					}
				}
				
				break;

			}
		}
		
		return enviaEmail;
	}
	
}
