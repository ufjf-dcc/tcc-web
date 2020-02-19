package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

//Email número 06 do drive
public class EnviadorEmailAlertaDadosDeDefesa extends EnviadorEmailChain{

	public EnviadorEmailAlertaDadosDeDefesa() {
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
		
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Aviso de prazo de inclusão dos dados da Defesa - "+nomeAluno);
		emailBuilder.appendMensagem("Prezados "+ nomeAluno + " e " + nomeOrientador + ", ").breakLine();
		emailBuilder.appendMensagem("daqui a 2 dias se encerra o prazo para incluir ");
		emailBuilder.appendMensagem("no Sistema de Monografias os dados da Defesa do Trabalho de Conclusão ");
		emailBuilder.appendMensagem("de Curso (TCC). É preciso informar: data, hora, local e ");
		emailBuilder.appendMensagem(" nome dos membros que farão parte da Banca Examinadora do TCC: ").breakLine(); 
		emailBuilder.appendMensagem("Orientador(a): " + nomeOrientador).breakLine();
		emailBuilder.appendMensagem("Co-orientador(a) (se houver)").breakLine();
		for(Participacao participacao : tcc.getParticipacoes()) {
			emailBuilder.appendMensagem("Membro da banca: " + participacao.getProfessor().getNomeUsuario()).breakLine();
		}
//		emailBuilder.appendMensagem("Membro 1: ").breakLine();
//		emailBuilder.appendMensagem("Membro 2: ").breakLine(); 
//		emailBuilder.appendMensagem("Suplente: ").breakLine();
		emailBuilder.appendMensagem("É preciso preencher todas as informações no sistema ");
		emailBuilder.appendMensagem("para esta atividade se tornar completa, pois ainda ");
		emailBuilder.appendMensagem("não consta que o(a) discente realizou esta atividade completamente.").breakLine(); 
		emailBuilder.appendMensagem("Se essa tarefa não for cumprida dentro do prazo, não haverá ");
		emailBuilder.appendMensagem("como dar andamento das demais atividades dessa discplina , e desta forma ");
		emailBuilder.appendMensagem("não será possível gerar a documentação necessária para a Defesa do TCC.").breakLine();
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
