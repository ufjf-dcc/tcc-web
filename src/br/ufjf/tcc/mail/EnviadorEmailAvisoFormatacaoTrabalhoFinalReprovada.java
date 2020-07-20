package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

//Email número 16 do drive
public class EnviadorEmailAvisoFormatacaoTrabalhoFinalReprovada extends EnviadorEmailChain{
	
	
	public EnviadorEmailAvisoFormatacaoTrabalhoFinalReprovada() {
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
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Formatação do Trabalho Final Reprovada - "+nomeAluno);
		emailBuilder.appendMensagem("Prezados " + nomeAluno + " e " + nomeOrientador).breakLine();
		emailBuilder.appendMensagem("a Coordenação  Reprovou a formatação da versão Final do seu Trabalho de Conclusão ");
		emailBuilder.appendMensagem( titulo + " de Curso submetido no Sistema de Monografias.").breakLine();
		emailBuilder.appendMensagem("Segue abaixo o(s) motivo(s) da reprovação:").breakLine();
		emailBuilder.appendMensagem("(descrição dos motivos para a reprovação que o coordenador(a) fez).").breakLine().breakLine();
		emailBuilder.appendMensagem("O(a) discente tem até 2 (dois) dias, desde que não ultrapasse o último dia letivo ");
		emailBuilder.appendMensagem("do semestre,  para corrigir o TCC e enviar a nova versão para ser avaliada.").breakLine();
		emailBuilder.appendMensagem("Att.,").breakLine();
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
