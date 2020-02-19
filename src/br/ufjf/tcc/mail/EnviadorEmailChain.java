package br.ufjf.tcc.mail;

import java.util.List;

import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public abstract class EnviadorEmailChain {

	private EnviadorEmailChain next;

	public EnviadorEmailChain(EnviadorEmailChain next) {
		this.next = next;
	}

	protected boolean statusFoiAlteradoPara(TCC tcc,String statusInicial ,String status) {
		if (statusInicial.equals(tcc.getStatusTCC()))
			return false;
		else if (tcc.getStatusTCC().equals(status)) {
			return true;
		}
		return false;
	}
	
	protected void inserirDestinatarios(List<Usuario> usuarios, EmailBuilder builder) {
		for (Usuario usuario : usuarios) {
			if(usuario.isEmailValido()) {
				builder.appendDestinatario(usuario.getEmail());
			}
		}
	}

	public void enviarEmail(TCC tcc, String statusInicial) {
		try {
			Email email = new Email();
			EmailBuilder emailBuilder = gerarEmail(tcc, statusInicial);
			email.enviar(emailBuilder);
			
			//next(tcc, statusInicial);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	protected void next(TCC tcc, String statusInicial) {
		if (next != null)
			next.enviarEmail(tcc, statusInicial);
	}

	protected abstract EmailBuilder gerarEmail(TCC tcc, String statusInicial);

}
