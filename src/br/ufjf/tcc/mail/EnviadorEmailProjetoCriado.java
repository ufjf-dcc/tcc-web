package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class EnviadorEmailProjetoCriado extends EnviadorEmailChain {

	public EnviadorEmailProjetoCriado(){
		super(new EnviadorEmailChainPAA());
	}

	@Override
	protected EmailBuilder gerarEmail(TCC tcc, String statusInicial) {
		EmailBuilder emailBuilder = null;
		if(statusInicial.equals("ProjetoCriado")){
			String nomeAluno = tcc.getAluno().getNomeUsuario();
			emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Projeto Criado - "+nomeAluno);
			emailBuilder.appendMensagem("Prezado coordenador de curso, ").breakLine().breakLine();
			emailBuilder.appendMensagem("O aluno <b>"+nomeAluno+"</b> iniciou a criação do projeto de TCC. ");
			emailBuilder.appendMensagem("Por enquanto o projeto ainda está incompleto. ");
			emailBuilder.appendMensagem("Você será notificado quando o discente completar o cadastro do projeto de TCC.").breakLine().breakLine();
			emailBuilder.appendLinkSistema();
			
			UsuarioBusiness ub = new UsuarioBusiness();
			List<Usuario> coordenadoresESecretarias = new ArrayList<>();
			coordenadoresESecretarias.addAll(ub.getCoordenadoresByCurso(tcc.getAluno().getCurso()));
			coordenadoresESecretarias.addAll(ub.getSecretariasByCurso(tcc.getAluno().getCurso()));
			inserirDestinatarios(coordenadoresESecretarias, emailBuilder);
		}
		return emailBuilder;
		
	}
	
	

}
