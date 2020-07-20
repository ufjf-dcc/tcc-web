package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;


// Email número 03 do drive
public class EnviadorEmailAlertaSubmissaoProjeto extends EnviadorEmailChain{
	
	public EnviadorEmailAlertaSubmissaoProjeto() {
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
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Aviso de prazo de submissão de projeto - " + nomeAluno);
		emailBuilder.appendMensagem("Prezado " + nomeAluno + " e " + nomeOrientador + ",").breakLine();
		emailBuilder.appendMensagem("daqui a 2 dias se encerra o prazo para Submissão do Projeto ");
		emailBuilder.appendMensagem("de Trabalho de Conclusão de Curso no Sistema de Monografias. ");
		emailBuilder.appendMensagem("Ainda não consta no sistema que o(a) discente realizou esta atividade completamente.").breakLine();
		emailBuilder.appendMensagem("Se essa tarefa não for cumprida dentro do prazo, não haverá como dar andamento das ");
		emailBuilder.appendMensagem("demais atividades dessa disciplina, e desta forma não será possível ");
		emailBuilder.appendMensagem("gerar a documentação necessária para a Defesa do TCC.").breakLine();
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
