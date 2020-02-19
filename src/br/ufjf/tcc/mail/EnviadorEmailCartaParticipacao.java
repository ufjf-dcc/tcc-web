package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;


//Email número 17 do drive
public class EnviadorEmailCartaParticipacao extends EnviadorEmailChain{
	
	
	public EnviadorEmailCartaParticipacao() {
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
		List<Usuario> participacoes;
		for(Participacao participacao : tcc.getParticipacoes()) {
			
		}
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Carta de participação da banca - " + nomeAluno);
		emailBuilder.appendMensagem("Prezado(a) (nome completo do membro da banca), ").breakLine(); 
		emailBuilder.appendMensagem("Gostaríamos de agradecer, em nome do Curso " + nomeCurso + ", ");
		emailBuilder.appendMensagem("a sua participação como Membro em Banca Examinadora do ");
		emailBuilder.appendMensagem("Trabalho de Conclusão de Curso, conforme as especificações:").breakLine(); 
		emailBuilder.appendMensagem("Candidato(a): " + nomeAluno).breakLine();
		emailBuilder.appendMensagem("Orientador(a): " + nomeOrientador).breakLine(); 
		emailBuilder.appendMensagem("Coorientador(a): (nome completo, mas colocar apenas se tiver)").breakLine();
		emailBuilder.appendMensagem("Título: " + titulo).breakLine();
		emailBuilder.appendMensagem("Data da Defesa: (data e hora).").breakLine();
		emailBuilder.appendMensagem("Banca Examinadora:").breakLine();
		emailBuilder.appendMensagem("(membros da banca que o coordenador confirmou a participação)").breakLine(); 
		emailBuilder.appendMensagem("Atenciosamente,").breakLine();
		emailBuilder.appendMensagem("(assinatura digital do Coordenador)").breakLine(); 
		emailBuilder.appendMensagem("______________________________________").breakLine(); 
		emailBuilder.appendMensagem(nomeCoordenador).breakLine();
		emailBuilder.appendMensagem("Coordenador(a) do Curso " + nomeCurso).breakLine();
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
