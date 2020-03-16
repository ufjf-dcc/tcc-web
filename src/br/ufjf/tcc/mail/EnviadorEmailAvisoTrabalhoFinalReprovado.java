package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

//Email número 14 do drive
public class EnviadorEmailAvisoTrabalhoFinalReprovado extends EnviadorEmailChain{

	public EnviadorEmailAvisoTrabalhoFinalReprovado() {
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
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Trabalho Final Reprovado - "+nomeAluno);
		emailBuilder.appendMensagem("Prezado " + nomeAluno + ", ").breakLine();
		emailBuilder.appendMensagem("o(a) orientador(a), " + nomeOrientador + ",  Reprovou a versão ");
		emailBuilder.appendMensagem("Final do seu Trabalho de Conclusão de Curso submetido no ").breakLine();
		emailBuilder.appendMensagem("Sistema de Monografias. Segue abaixo o(s) motivo(s) da reprovação:").breakLine();
		emailBuilder.appendMensagem("(descrição dos motivos para a reprovação que o orientador fez).").breakLine().breakLine();
		emailBuilder.appendMensagem("Você tem até 2 (dois) dias, desde que não ultrapasse o último dia ");
		emailBuilder.appendMensagem("letivo do semestre,  para corrigir seu trabalho e submeter a nova ");
		emailBuilder.appendMensagem("versão para ser avaliada pelo(a) orientador(a).").breakLine(); 
		emailBuilder.appendMensagem("Att.,").breakLine();
		emailBuilder.appendMensagem( nomeCoordenador).breakLine();
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
