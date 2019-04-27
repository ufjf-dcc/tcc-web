package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.Usuario;

public class EnviadorEmailCartaParticipacaoBanca extends EnviadorEmailChain {
	
	public EnviadorEmailCartaParticipacaoBanca() {
		super(null);
	}

	@Override
	public EmailBuilder gerarEmail(TCC tcc, String statusInicial) {
		EmailBuilder emailBuilder = null;
		if(tcc.isPublicado()) {
			for(Participacao p : tcc.getParticipacoes()) {
				String nomeMembro = p.getProfessor().getNomeUsuario();
				String nomeAluno = tcc.getAluno().getNomeUsuario();
				emailBuilder = new EmailBuilder(true).comTitulo("[TCC_WEB] Carta de participação da banca - " + nomeAluno);
				emailBuilder.appendMensagem("Prezado(a) " + nomeMembro);
				emailBuilder.appendMensagem("");
				
				// anexar pdf TODO: Anexar pdf ao email, ou definir modelo da carta de participação no corpo do email
				
				List<Usuario> destinatarios = new ArrayList<>();
				destinatarios.add(p.getProfessor());				
				inserirDestinatarios(destinatarios, emailBuilder);
			}
		}
		return null;
	}
}
