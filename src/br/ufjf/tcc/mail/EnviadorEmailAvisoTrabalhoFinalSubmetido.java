package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

//Email número 13 do drive
public class EnviadorEmailAvisoTrabalhoFinalSubmetido extends EnviadorEmailChain{

	public EnviadorEmailAvisoTrabalhoFinalSubmetido() {
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
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Aviso de TCC submetido - "+nomeAluno);
		emailBuilder.appendMensagem("Prezado " + nomeOrientador + ", ").breakLine();
		emailBuilder.appendMensagem("a Versão Final do Trabalho de Conclusão de Curso do(a) discente " + nomeAluno);
		emailBuilder.appendMensagem(", com o título " + titulo + ", que você orientou, ");
		emailBuilder.appendMensagem("se encontra disponível no Sistema de Monografias aguardando a ");
		emailBuilder.appendMensagem("sua avaliação: Aprovação ou Reprovação.").breakLine(); 
		emailBuilder.appendMensagem("No caso de Reprovação, descrever o(s) motivo(s) para ");
		emailBuilder.appendMensagem("ser(em) enviado(s) para o(a) discente. O(a) mesmo(a) terá o prazo ");
		emailBuilder.appendMensagem("de 2 (dois) dias, desde que não ultrapasse o último dia letivo do ");
		emailBuilder.appendMensagem("semestre, para corrigir o(s) problema(s) e submeter uma nova ");
		emailBuilder.appendMensagem("Versão Final, que também deverá receber a sua avaliação.").breakLine();
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
