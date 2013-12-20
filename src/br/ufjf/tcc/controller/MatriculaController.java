package br.ufjf.tcc.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zhtml.Filedownload;

import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.library.Formulario;
import br.ufjf.tcc.library.ItemFormulario;
import br.ufjf.tcc.library.PDFHandler;

public class MatriculaController extends CommonsController {
	private List<ItemFormulario> itens;

	@Init
	public void init() {
		if (getUsuario() != null)
			redirectHome();

		itens = new ArrayList<ItemFormulario>();

		itens.add(new ItemFormulario("Nome do aluno: ", ""));
		itens.add(new ItemFormulario("Matrícula: ", ""));
		itens.add(new ItemFormulario("Curso: ", ""));
		itens.add(new ItemFormulario("Orientador: ", ""));
		itens.add(new ItemFormulario("Título do Trabalho: ", ""));
	}

	public List<ItemFormulario> getItens() {
		return itens;
	}

	public void setItens(List<ItemFormulario> itens) {
		this.itens = itens;
	}

	@Command
	public void exportPDF() {
		Formulario formulario = new Formulario();
		formulario.setItens(itens);
		PDFHandler handler = new PDFHandler();

		try {
			// Converte o formulário para XML
			ByteArrayOutputStream streamSource = handler
					.getXMLSource(formulario);

			// Converte o XML para XSL-FO e então para PDF; Imprieme o diretório
			String pdfDir = handler.createPDFFile(streamSource);

			// Download
			InputStream is = FileManager.getFileInputSream(pdfDir);
			Filedownload.save(is, "application/pdf", "SolicitacaoDeMatricula.pdf");
			// FileManager.deleteFile(pdfDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
