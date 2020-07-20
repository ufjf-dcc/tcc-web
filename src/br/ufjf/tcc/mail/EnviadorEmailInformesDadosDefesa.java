package br.ufjf.tcc.mail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;


// Email número 07 do drive
public class EnviadorEmailInformesDadosDefesa extends EnviadorEmailChain{

	public EnviadorEmailInformesDadosDefesa() {
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
		
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Informes dos Dados de Defesa - " + nomeAluno);
		emailBuilder.appendMensagem("Prezados, ").breakLine();
		emailBuilder.appendMensagem("no dia " + dataApresentacaoString + " às " + horaApresentacao + " na(o) " + tcc.getSalaDefesa() + " acontecerá a ");
		emailBuilder.appendMensagem("Defesa do Trabalho de Conclusão de Curso " + titulo);
		emailBuilder.appendMensagem(" do(a) discente " + nomeAluno + ". A Banca Examinadora será composta por: ").breakLine(); 
		emailBuilder.appendMensagem("Orientador(a): " + nomeOrientador).breakLine();
		emailBuilder.appendMensagem("Co-orientador(a) (se houver)").breakLine();
		for(Participacao participacao : tcc.getParticipacoes()) {
			emailBuilder.appendMensagem("Membro da banca: " + participacao.getProfessor().getNomeUsuario()).breakLine();
		}
		
//		emailBuilder.appendMensagem("Membro 1 ").breakLine();
//		emailBuilder.appendMensagem("Membro 2 ").breakLine(); 
//		emailBuilder.appendMensagem("Suplente").breakLine();
		
		emailBuilder.appendMensagem("A Coordenação do Curso " + nomeCurso + " convida todos os interessados a participarem desta Defesa de TCC.").breakLine(); 
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
		
}
