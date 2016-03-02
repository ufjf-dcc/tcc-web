package br.ufjf.tcc.mail;

public class EmailBuilder {

	private final StringBuilder mensagem;
	private final StringBuilder destinatarios;

	public EmailBuilder() {
		mensagem = new StringBuilder();
		destinatarios = new StringBuilder();
	}

	public void appendDestinatario(String email) {
		if (!destinatarios.toString().isEmpty()) {
			destinatarios.append(", ");
		}
		destinatarios.append(email);
	}

	public void appendMensagem(String texto) {
		mensagem.append(texto);
	}

	public String getDestinatarios() {
		return this.destinatarios.toString();
	}

	public String getMensagem() {
		return this.mensagem.toString();
	}

}
