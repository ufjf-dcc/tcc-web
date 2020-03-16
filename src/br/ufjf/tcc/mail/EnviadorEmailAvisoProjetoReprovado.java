package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class EnviadorEmailAvisoProjetoReprovado extends EnviadorEmailChain{
	
	
	public EnviadorEmailAvisoProjetoReprovado() {
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
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Aviso de projeto reprovado - "+nomeAluno);
		emailBuilder.appendMensagem("Prezados " + nomeAluno+ " e " + nomeOrientador + ", ").breakLine();
		emailBuilder.appendMensagem("o Projeto de Trabalho de Conclusão de Curso, com o título " + titulo + ", ");
		emailBuilder.appendMensagem("submetido no Sistema de Monografias pelo(a) discente foi Reprovado.").breakLine();
		emailBuilder.appendMensagem("Segue abaixo o(s) motivos da reprovação:").breakLine();
		emailBuilder.appendMensagem("<descrição dos motivos para a reprovação digitadas pelo(a) coordenador(a) no sistema>.").breakLine(); 
		emailBuilder.appendMensagem("O(a) discente tem até 7 (sete) dias corridos para corrigir o Projeto e submeter a versão corrigida no Sistema de Monografias.").breakLine(); 
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
