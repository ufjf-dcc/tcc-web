package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class EnviadorEmailChainPAA extends EnviadorEmailChain {

	public EnviadorEmailChainPAA() {
		super(new EnviadorEmailChainTAACoordenador());
	}

	@Override
	protected EmailBuilder gerarEmail(TCC tcc, String statusInicial) {
		EmailBuilder emailBuilder = null;
		if(statusFoiAlteradoPara(tcc, statusInicial, "PAA")){
			String nomeAluno = tcc.getAluno().getNomeUsuario();
			emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Projeto aguardando aprovação - "+nomeAluno);
			emailBuilder.appendMensagem("Prezado(a) coordenador(a) de curso, ").breakLine().breakLine();
			emailBuilder.appendMensagem("O projeto de TCC do aluno(a) <b>" + nomeAluno + "</b> está aguardando sua aprovação no sistema de monografias.");
			emailBuilder.appendMensagem(" Após a aprovação do projeto o discente poderá dar início ao seu TCC e informar os dados de defesa,");
			emailBuilder.appendMensagem(" banca avaliadora e deixar disponível no sistema o TCC para a banca. ").breakLine().breakLine();
			emailBuilder.appendMensagem("Título do projeto: "+tcc.getNomeTCC()).breakLine();
			emailBuilder.appendMensagem("Orientador(a): "+tcc.getOrientador().getNomeUsuario()).breakLine();
			if(tcc.possuiCoorientador()){
				emailBuilder.appendMensagem("Coorientador(a): "+tcc.getCoOrientador().getNomeUsuario()).breakLine();
			}
			emailBuilder.appendMensagem("Resumo: "+tcc.getResumoTCC()).breakLine();
			emailBuilder.appendMensagem("Palavras-chave: "+tcc.getPalavrasChave()).breakLine().breakLine();
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
