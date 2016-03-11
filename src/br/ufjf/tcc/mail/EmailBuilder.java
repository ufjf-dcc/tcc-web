package br.ufjf.tcc.mail;

public class EmailBuilder {

	private StringBuilder mensagem;
	private StringBuilder destinatarios;
	private boolean isHtmlFormat;
	private static String VIRGULA_ESPACO = ", ";

	public EmailBuilder(boolean isHtmlFormat) {
		mensagem = new StringBuilder();
		destinatarios = new StringBuilder();
		this.isHtmlFormat = isHtmlFormat;
	}

	public void appendDestinatario(String destinatario) {
		if (!destinatarios.toString().isEmpty()) {
			destinatarios.append(VIRGULA_ESPACO);
		}
		if(destinatario!=null && !destinatario.isEmpty())
			destinatarios.append(destinatario);
	}

	public void appendMensagem(String texto) {
		if (isHtmlFormat())
			mensagem.append("<p>" + texto + "</p>");
		else
			mensagem.append(texto);
	}

	public void appendMensagemBreakLine(String texto) {
		if (isHtmlFormat())
			mensagem.append("<p>" + texto + "</p>");
		else
			mensagem.append(texto + "\n");
	}

	public void appendHtmlTextBold(String texto) {
		mensagem.append("<b>" + texto + "</b");
	}
	
	public void appendHtmlTopico(String texto) {
		mensagem.append("<font size=\'3\'>");
		appendHtmlTextBold(texto);
		mensagem.append("</font>");
	}

	public boolean isHtmlFormat() {
		return isHtmlFormat;
	}

	public void setHtmlFormat(boolean isHtmlFormat) {
		this.isHtmlFormat = isHtmlFormat;
	}

	public String getDestinatarios() {
		return this.destinatarios.toString();
	}

	public String getMensagem() {
		return this.mensagem.toString();
	}

}
