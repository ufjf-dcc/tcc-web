package br.ufjf.tcc.mail;

public class EmailBuilder {

	private StringBuilder mensagem;
	private StringBuilder destinatarios;
	private String titulo;
	private boolean isHtmlFormat;
	private static String VIRGULA_ESPACO = ", ";
	private static String linkSistema = "http://www.monografias.ice.ufjf.br";

	public EmailBuilder(boolean isHtmlFormat) {
		mensagem = new StringBuilder();
		destinatarios = new StringBuilder();
		titulo = new String();
		this.isHtmlFormat = isHtmlFormat;
	}
	
	public EmailBuilder appendLinkSistema() {
		mensagem.append(String.format("<b><a href=\"%s\">Acessar o sistema </a></b>", linkSistema));
		return this;
	}
	
	public EmailBuilder comTitulo(String titulo) {
		this.titulo = titulo;
		return this;
	}

	public void appendDestinatario(String destinatario) {
		if (!destinatarios.toString().isEmpty()) {
			destinatarios.append(VIRGULA_ESPACO);
		}
		if(destinatario!=null && !destinatario.isEmpty())
			destinatarios.append(destinatario);
	}

	public EmailBuilder appendMensagem(String texto) {
		
		mensagem.append(texto);
		return this;
	}
	
	public EmailBuilder breakLine(){
		if (isHtmlFormat())
			mensagem.append("<br>");
		else
			mensagem.append("\n");
		return this;
	}

	public EmailBuilder appendHtmlTextBold(String texto) {
		mensagem.append("<b>" + texto + "</b>");
		return this;
	}
	
	public EmailBuilder appendHtmlTopico(String texto) {
		mensagem.append("<font size=\'3\'>");
		appendHtmlTextBold(texto);
		mensagem.append("</font>");
		return this;
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

	public String getTitulo() {
		return titulo;
	}
	

}
