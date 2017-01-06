package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class EnviadorEmailChainTrabFinal extends EnviadorEmailChain {

	public EnviadorEmailChainTrabFinal() {
		super(null);
	}

	@Override
	protected EmailBuilder gerarEmail(TCC tcc, String statusInicial) {
		EmailBuilder emailBuilder = null;
		if(tcc.isTrabFinal()) {
			String nomeAluno = tcc.getAluno().getNomeUsuario();
			emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Trabalho aguardando aprovação - " + nomeAluno);
			emailBuilder.appendMensagem("Prezado(a) coordenador(a) de curso, ").breakLine().breakLine();
			emailBuilder.appendMensagem("O aluno(a) <b>"+nomeAluno+"</b> enviou a versão final do seu trabalho de conclusão de curso e aguarda aprovação da coordenação de curso.").breakLine();
			emailBuilder.appendMensagem(" Após a aprovação do trabalho, este estará disponível para acesso público no repositório de trabalhos acadêmicos.").breakLine().breakLine();
			emailBuilder.appendHtmlTopico("Informações do trabalho:").breakLine().breakLine();
			emailBuilder.appendHtmlTextBold("Título: ");
			emailBuilder.appendMensagem(tcc.getNomeTCC()).breakLine();
			emailBuilder.appendHtmlTextBold("Resumo: ");
			emailBuilder.appendMensagem(tcc.getResumoTCC()).breakLine();
			emailBuilder.appendMensagem("<b>Orientador(a):</b> " + tcc.getOrientador().getNomeUsuario()).breakLine();
			if(tcc.possuiCoorientador()) {
				emailBuilder.appendMensagem("<b>Coorientador(a):</b> " + tcc.getCoOrientador().getNomeUsuario()).breakLine();
			}
			emailBuilder.appendHtmlTextBold("Banca examinadora: ").breakLine();
			for(Participacao p : tcc.getParticipacoes()) {
				if(p.isSuplente()){
					emailBuilder.appendMensagem("  - " + p.getProfessor().getNomeUsuario() + " (Suplente)").breakLine();
				} else {
					emailBuilder.appendMensagem("  - " + p.getProfessor().getNomeUsuario()).breakLine();
				}
			}
			String dataFormatada = new DateTime(tcc.getDataApresentacao().getTime()).toString("dd/MM/yyyy - HH:mm");
			emailBuilder.appendMensagem("<b>Data da apresentação:</b> "+dataFormatada).breakLine();
			emailBuilder.appendMensagem("<b>Local de defesa:</b> "+tcc.getSalaDefesa()).breakLine();
			UsuarioBusiness ub = new UsuarioBusiness();
			List<Usuario> destinatarios = new ArrayList<>();
			destinatarios.addAll(ub.getCoordenadoresByCurso(tcc.getAluno().getCurso()));
			destinatarios.addAll(ub.getSecretariasByCurso(tcc.getAluno().getCurso()));
			destinatarios.add(tcc.getOrientador());
			inserirDestinatarios(destinatarios, emailBuilder);
		}
		return emailBuilder;
	}

}
