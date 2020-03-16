package br.ufjf.tcc.mail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

//Email número 11 do drive
public class EnviadorEmailAvisoBancaTrabalhoSubmetido extends EnviadorEmailChain{

	public EnviadorEmailAvisoBancaTrabalhoSubmetido() {
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
		String titulo = tcc.getNomeTCC();
		
		Date dataApresentacao = tcc.getDataApresentacao();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dataApresentacaoString = formatter.format(dataApresentacao);
		formatter.applyLocalizedPattern("HH:mm");
		String horaApresentacao = formatter.format(dataApresentacao);
		
		
		for(Participacao participacao : tcc.getParticipacoes()) {
			String nomeMembro = participacao.getProfessor().getNomeUsuario();
			
			
			emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Aviso TCC submetido - " + nomeAluno);
			emailBuilder.appendMensagem("Prezado " + nomeMembro + ", ").breakLine();
			emailBuilder.appendMensagem("o Trabalho de Conclusão de Curso do(a) discente "+ nomeAluno + ", ");
			emailBuilder.appendMensagem("com o título " + titulo +  ", do qual você será membro da Banca ");
			emailBuilder.appendMensagem("Examinadora de defesa, se encontra disponível no Sistema de Monografias.").breakLine(); 
			emailBuilder.appendMensagem("	A Defesa do TCC está marcada para dia " + dataApresentacaoString + ", às  ");
			emailBuilder.appendMensagem(horaApresentacao + ", a ser realizado na(o)" + tcc.getSalaDefesa() + ".").breakLine(); 
			
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
		}
	
		return emailBuilder;
		
	}
	
	
}
