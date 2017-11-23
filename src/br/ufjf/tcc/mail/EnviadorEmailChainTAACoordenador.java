package br.ufjf.tcc.mail;

import org.joda.time.DateTime;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;

public class EnviadorEmailChainTAACoordenador extends EnviadorEmailChain {

	public EnviadorEmailChainTAACoordenador() {
		super(new EnviadorEmailChainTAAProfessor());
	}

	@Override
	protected EmailBuilder gerarEmail(TCC tcc, String statusInicial) {
		EmailBuilder emailBuilder = null;
		if(statusFoiAlteradoPara(tcc, statusInicial, "TAA")){
			String nomeAluno = tcc.getAluno().getNomeUsuario();
			emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Trabalho com defesa agendada - "+nomeAluno);
			emailBuilder.appendMensagem("Prezado(a) coordenador(a) de curso, ").breakLine().breakLine();
			emailBuilder.appendMensagem("O aluno(a) <b>" + nomeAluno + "</b> informou os dados da sua defesa de trabalho de conclusão de curso.");
			emailBuilder.appendMensagem(" Após a defesa, o aluno(a) deverá enviar a versão final do trabalho para ser publicado no repositório de trabalhos acadêmicos.").breakLine().breakLine();
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
			emailBuilder.appendMensagem("<b>Local de defesa:</b> "+tcc.getSalaDefesa()).breakLine().breakLine();
			emailBuilder.appendLinkSistema();
			UsuarioBusiness ub = new UsuarioBusiness();
			inserirDestinatarios(ub.getCoordenadoresByCurso(tcc.getAluno().getCurso()), emailBuilder);
		}
		return emailBuilder;
	}

}
