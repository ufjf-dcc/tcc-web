package br.ufjf.tcc.mail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.business.PrazoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Prazo;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.persistent.impl.TCCDAO;

@Startup
@Singleton
public class EmailListener {
	TCCBusiness tccBusiness;
	TCCDAO tccDAO;
	CalendarioSemestreBusiness calendarioBusiness;
	PrazoBusiness prazoBusiness;
	Calendar calendar;
	
	public EmailListener() {
		this.calendar = null;
		this.tccBusiness = new TCCBusiness();
		this.tccDAO = new TCCDAO();
		this.calendarioBusiness = new CalendarioSemestreBusiness();
		this.prazoBusiness = new PrazoBusiness();
	}
		
	private final AtomicBoolean alreadyRunning = new AtomicBoolean(false);
	
	@Schedule(hour="*", minute="*", second="*/10", persistent = true)
	@Lock(LockType.READ)
	public void listener() {
		if (alreadyRunning.getAndSet(true)) return;
		
		try
        {
			List<CalendarioSemestre> calendarios = (List<CalendarioSemestre>) this.calendarioBusiness.getCurrentCalendars();
			this.calendar = Calendar.getInstance();
			for(CalendarioSemestre calendario : calendarios) {
				
				List<Prazo> prazos = prazoBusiness.getPrazosByCalendario(calendario);
				this.verificarPrazos(calendario, 2);
			}
			
        }
        finally
        {
            alreadyRunning.set(false);
        }
	}
	
	public void verificarPrazos(CalendarioSemestre calendario, int diasParaAlerta) {
		List<Prazo> prazos = prazoBusiness.getPrazosByCalendario(calendario);
		Calendar dataOffset = (Calendar) this.calendar.clone();
		dataOffset.add(Calendar.DATE, diasParaAlerta);							// Adiciona dias ao dia de hoje
		
		this.verificarProjetoJaCriado(calendario, dataOffset, prazos.get(0));
		this.verificarDadosDeDefesa(calendario, dataOffset, prazos.get(1));
		this.verificarSubmissaoTCC(calendario, dataOffset, prazos.get(2));
		this.verificarSubmissaoTCCfinal(calendario, dataOffset, prazos.get(3));
	}
	
	/**
	 * Para cada calendário atual, notifica os alunos 
	 * que ainda não concluiram o projeto, x dias antes da data limite (por parâmetro) 
	 */
	@Lock(LockType.READ)
	public void verificarProjetoJaCriado(CalendarioSemestre calendario, Calendar dataOffset,  Prazo prazo) {
		Calendar dataFinalPrazo = (dateToCalendar(prazo.getDataFinal()));	// Transforma a data final do calendario do semestre (Date) em Calendar
		boolean datasIguais = this.compareCalendars(dataOffset, dataFinalPrazo, false);
		if(!datasIguais)
			return;
		
		List<TCC> projetos = this.tccBusiness.getNotFinishedProjectsByCalendar(calendario);
		if(projetos == null)
			return;
		
		for(TCC projeto : projetos) {
			if(!projeto.isEmailAlertaPrazoProjetoSubmetidoEnviado()) {
				System.out.println(projeto.getNomeTCC());
				System.out.println(projeto.getIdTCC());
//					EnviadorEmailChain email = new EnviadorEmailAlertaSubmissaoProjeto();
//					email.enviarEmail(projeto, null);
//					projeto.setEmailAlertaEnviado(1);
//					this.tccDAO.salvar(projeto);
			}
		}
	}
	
	
	public void verificarDadosDeDefesa(CalendarioSemestre calendario, Calendar dataOffset,  Prazo prazo) {
		Calendar dataFinalPrazo = (dateToCalendar(prazo.getDataFinal()));	// Transforma a data final do calendario do semestre (Date) em Calendar
		boolean datasIguais = this.compareCalendars(dataOffset, dataFinalPrazo, false);
		if(!datasIguais)
			return;
		
		List<TCC> projetos = this.tccBusiness.getNotFinishedProjectsByCalendar(calendario);
		if(projetos == null)
			return;
		
		for(TCC projeto : projetos) {
			if(!projeto.isEmailAlertaPrazoDadosDefesaEnviado()) {
				System.out.println(projeto.getNomeTCC());
				System.out.println(projeto.getIdTCC());
//					EnviadorEmailChain email = new EnviadorEmailAlertaDadosDeDefesa();
//					email.enviarEmail(projeto, null);
//					projeto.setEmailAlertaEnviado(2);
//					this.tccDAO.salvar(projeto);
			}
		}
	}
	
	
	public void verificarSubmissaoTCC(CalendarioSemestre calendario, Calendar dataOffset,  Prazo prazo) {
		Calendar dataFinalPrazo = (dateToCalendar(prazo.getDataFinal()));	// Transforma a data final do calendario do semestre (Date) em Calendar
		boolean datasIguais = this.compareCalendars(dataOffset, dataFinalPrazo, false);
		if(!datasIguais)
			return;
		
		List<TCC> projetos = this.tccBusiness.getNotFinishedProjectsByCalendar(calendario);
		if(projetos == null)
			return;
		
		for(TCC projeto : projetos) {
			if(!projeto.isEmailAlertaPrazoTrabalhoEnviado()) {
				System.out.println(projeto.getNomeTCC());
				System.out.println(projeto.getIdTCC());
//					EnviadorEmailChain email = new EnviadorEmailAlertaSubmissaoTrabalho();
//					email.enviarEmail(projeto, null);
//					projeto.setEmailAlertaEnviado(3);
//					this.tccDAO.salvar(projeto);
			}
		}
	}
	
	
	public void verificarSubmissaoTCCfinal(CalendarioSemestre calendario, Calendar dataOffset,  Prazo prazo) {
		Calendar dataFinalPrazo = (dateToCalendar(prazo.getDataFinal()));	// Transforma a data final do calendario do semestre (Date) em Calendar
		boolean datasIguais = this.compareCalendars(dataOffset, dataFinalPrazo, false);
		if(!datasIguais)
			return;
		
		List<TCC> projetos = this.tccBusiness.getNotFinishedProjectsByCalendar(calendario);
		if(projetos == null)
			return;
		
		for(TCC projeto : projetos) {
			if(!projeto.isEmailAlertaPrazoTrabalhoFinaloEnviado()) {
				System.out.println(projeto.getNomeTCC());
				System.out.println(projeto.getIdTCC());
//					EnviadorEmailChain email = new EnviadorEmailAlertaSubmissaoTrabalhoFinal();
//					email.enviarEmail(projeto, null);
//					projeto.setEmailAlertaEnviado(4);
//					this.tccDAO.salvar(projeto);
			}
		}
	}
	
	private Calendar dateToCalendar(Date date){ 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	
	/**
	 * Compara dois calendários e retorna verdadeiro se forem iguais.
	 *  O parâmetro compareHour indica se a hora será levada em 
	 *  consideração na comparação.
	 */
	private boolean compareCalendars(Calendar cal1, Calendar cal2, boolean compareHour) {
		if(!compareHour) {
			cal1 = getZeroTimeCalendar(cal1);
			cal2 = getZeroTimeCalendar(cal2);
		}
		if(cal1.compareTo(cal2) == 0)
			return true;
		return false;
	}
	
	/**
	 * Zera as horas, minutos e segundos.
	 * Função utilizada em comparações de datas em 
	 * que são considerados apenas os dias, e não a hora exata.
	 */
	private Calendar getZeroTimeCalendar(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
}
