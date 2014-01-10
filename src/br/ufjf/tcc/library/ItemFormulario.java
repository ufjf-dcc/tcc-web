package br.ufjf.tcc.library;

public class ItemFormulario {
	private String pergunta;
	private String resposta;
	
	public ItemFormulario (){
		
	}

	public ItemFormulario(String pergunta, String resposta) {
		super();
		this.pergunta = pergunta;
		this.resposta = resposta;
	}

	public String getPergunta() {
		return pergunta;
	}

	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

}
