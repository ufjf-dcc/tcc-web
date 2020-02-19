package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;


//Email número 08 do drive
public class EnviadorEmailAlertaSubmissaoTrabalho extends EnviadorEmailChain{
	
	
	public EnviadorEmailAlertaSubmissaoTrabalho() {
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
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Aviso de prazo de submissão de trabalho - " + nomeAluno);
		emailBuilder.appendMensagem("Prezados " + nomeAluno + " e " + nomeOrientador +",").breakLine();
		emailBuilder.appendMensagem("daqui a 2 dias se encerra o prazo para incluir no Sistema de Monografias");
		emailBuilder.appendMensagem("a versão do TCC a ser avaliado pelos membros da Banca Examinadora. ").breakLine();
		emailBuilder.appendMensagem("É necessário preencher todas as informações no sistema para esta atividade se tornar completa, ");
		emailBuilder.appendMensagem("pois ainda não consta que o(a) discente realizou esta atividade completamente.").breakLine();
		emailBuilder.appendMensagem("Se não cumprir essa tarefa dentro do prazo, não haverá como dar andamento das");
		emailBuilder.appendMensagem("demais atividades dessa disciplina, e desta forma não será possível gerar");
		emailBuilder.appendMensagem("a documentação necessária para a Defesa do TCC.").breakLine();
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
