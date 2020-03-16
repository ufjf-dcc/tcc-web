package br.ufjf.tcc.mail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;


//Email número 12 do drive
public class EnviadorEmailAlertaSubmissaoTrabalhoFinal extends EnviadorEmailChain{

	public EnviadorEmailAlertaSubmissaoTrabalhoFinal() {
		super(null);
	}
	
	@Override
	protected EmailBuilder gerarEmail(TCC tcc, String statusInicial) {
		EmailBuilder emailBuilder = null;
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		
		List<Usuario> coordenadores = usuarioBusiness.getCoordenadoresByCurso(tcc.getAluno().getCurso());
		
		String nomeAluno = tcc.getAluno().getNomeUsuario();
		String nomeOrientador = tcc.getOrientador().getNomeUsuario();
		String nomeCoordenador = coordenadores.get(0).getNomeUsuario();
		String nomeCurso = tcc.getAluno().getCurso().getNomeCurso();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dataApresentacao = tcc.getDataApresentacao();
		Calendar calendario = dateToCalendar(dataApresentacao);
		calendario.add(Calendar.DATE, 5);
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Aviso de prazo de submissão de trabalho final - " + nomeAluno);
		emailBuilder.appendMensagem("Prezados " + nomeAluno + " e " + nomeOrientador + ", ").breakLine();
		emailBuilder.appendMensagem("daqui a 2 dias (" + formatter.format(calendario.getTime()));
		emailBuilder.appendMensagem(") se encerra o prazo para incluir no Sistema de Monografias a versão Final ");
		emailBuilder.appendMensagem("do TCC, após correções sugeridas pelos membros da Banca Examinadora. ").breakLine();
		emailBuilder.appendMensagem("Ainda não consta no sistema que o(a) discente realizou esta atividade ");
		emailBuilder.appendMensagem("completamente. Por isso a Coordenação solicita que o(a) discente ");
		emailBuilder.appendMensagem("preencha todas as informações no sistema para esta atividade se ");
		emailBuilder.appendMensagem("tornar completa. Se essa tarefa não for cumprida dentro do prazo, ");
		emailBuilder.appendMensagem("não haverá como dar andamento das demais atividades dessa disciplina. ");
		emailBuilder.appendMensagem("Desta forma não será possível deixar esse TCC Público no Sistema de Monografias.").breakLine();
		emailBuilder.appendMensagem("Att.,").breakLine();
		emailBuilder.appendMensagem(nomeCoordenador).breakLine();
		emailBuilder.appendMensagem("Coordenador(a) do Curso de " + nomeCurso).breakLine();
		emailBuilder.appendLinkSistema();
		
		List<Usuario> aluno = new ArrayList<>();
		UsuarioBusiness ub = new UsuarioBusiness();
		Usuario u = ub.getByMatricula("1010");
		System.out.println(u.getEmail());
		aluno.add(u);
		inserirDestinatarios(aluno, emailBuilder);
	
		return emailBuilder;
		
	}
	
	private Calendar dateToCalendar(Date date){ 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

}
