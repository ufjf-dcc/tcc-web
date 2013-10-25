package br.ufjf.tcc.business;

import java.util.List;

import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.ParticipacaoDAO;

public class ParticipacaoBusiness {

	public List<Participacao> getParticipacoesByProfessor(Usuario professor) {
		return new ParticipacaoDAO().getParticipacoesByProfessor(professor);
	}

}
