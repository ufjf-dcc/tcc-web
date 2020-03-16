package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

//Email número 15 do drive
public class EnviadorEmailAvisoTrabalhoFinalAprovadoPorOrientador extends EnviadorEmailChain{
	
	
	public EnviadorEmailAvisoTrabalhoFinalAprovadoPorOrientador() {
		super(null);
	}

	@Override
	protected EmailBuilder gerarEmail(TCC tcc, String statusInicial) {
		EmailBuilder emailBuilder = null;
		
		String nomeAluno = tcc.getAluno().getNomeUsuario();
		String nomeOrientador = tcc.getOrientador().getNomeUsuario();
		String titulo = tcc.getNomeTCC();
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Trabalho Final Aprovado por Orientador - "+nomeAluno);
		emailBuilder.appendMensagem("Prezada Coordenação, ");
		emailBuilder.appendMensagem("a Versão Final do Trabalho de Conclusão de Curso do(a) discente " + nomeAluno + ", com o título ");
		emailBuilder.appendMensagem(titulo + ", foi aprovada pelo(a) orientador(a) " + nomeOrientador + ".").breakLine();
		emailBuilder.appendMensagem("A Coordenação/bolsista da Coordenação precisa avaliar ");
		emailBuilder.appendMensagem("a formatação do TCC, conferir a documentação de Defesa e confirmar ");
		emailBuilder.appendMensagem("no Sistema de Monografias os membros que efetivamente participaram ");
		emailBuilder.appendMensagem("da Banca. No caso de Reprovação, descrever o(s) motivo(s) para ");
		emailBuilder.appendMensagem("ser(em) enviado(s) para o(a) discente que terá o prazo máximo ");
		emailBuilder.appendMensagem("de 2 (dois) dias, desde que não ultrapasse o último dia letivo do ");
		emailBuilder.appendMensagem("semestre, para submeter uma nova versão final, que também deverá receber a ");
		emailBuilder.appendMensagem("sua avaliação. Caso contrário, deve-se aprovar o Trabalho de ");
		emailBuilder.appendMensagem("Conclusão de Curso para se tornar Público no Sistema de Monografias.").breakLine();
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
