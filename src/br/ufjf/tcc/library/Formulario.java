package br.ufjf.tcc.library;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Formulario")
public class Formulario {
	private List<ItemFormulario> itens;

	public Formulario() {
	}

	@XmlElementWrapper(name = "itens")
	@XmlElement(name = "item")
	public List<ItemFormulario> getItens() {
		return itens;
	}

	public void setItens(List<ItemFormulario> itens) {
		this.itens = itens;
	}

}
