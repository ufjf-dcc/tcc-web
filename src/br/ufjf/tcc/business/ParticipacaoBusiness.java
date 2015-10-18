package br.ufjf.tcc.business;

import java.util.List;

import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.ParticipacaoDAO;

public class ParticipacaoBusiness {
	
	private ParticipacaoDAO participacaoDAO;

	public ParticipacaoBusiness() {
		this.participacaoDAO = new ParticipacaoDAO();
	}

	public List<Participacao> getParticipacoesByUser(Usuario user) {
		return participacaoDAO.getParticipacoesByUser(user);
	}
	
	public List<Participacao> getParticipacoesByTCC(TCC tcc) {
		return participacaoDAO.getParticipacoesByTCC(tcc);
	}
	
	public List<Participacao> getParticipacoesUsuarioByTCC(TCC tcc) {
		return participacaoDAO.getParticipacoesUsuarioByTCC(tcc);
	}
	
	public boolean updateList(TCC tcc) {
		return participacaoDAO.updateList(tcc);
	}
	
	public boolean excluiLista(List<Participacao> participacoes)
	{
		return participacaoDAO.excluiLista(participacoes);
	}
}
