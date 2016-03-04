package br.ufjf.tcc.teste;

import java.util.List;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.ufjf.tcc.business.TipoUsuarioBusiness;
import br.ufjf.tcc.model.TipoUsuario;

public class JobTeste implements Job {
	
	private TipoUsuarioBusiness tub;
	
	public JobTeste() {
		if(tub==null) {
			tub = new TipoUsuarioBusiness();
		}
	}
	
	@Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        System.err.println("Servico executado conforme agendamento.");
        
        System.out.println(new DateTime().toString());
        
        List<TipoUsuario> tipos = tub.getAll();
        System.out.println(tipos.size());
    }
}
