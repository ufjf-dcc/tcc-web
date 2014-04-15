package br.ufjf.tcc.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.DepartamentoBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.library.Formulario;
import br.ufjf.tcc.library.ItemFormulario;
import br.ufjf.tcc.library.PDFHandler;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.model.Usuario;

public class SolicitarAcessoController extends CommonsController {
	private Usuario tempUser = null;
	private List<ItemFormulario> itens;
	private List<Curso> cursos;
	private List<Departamento> departamentos;
	private List<Usuario> orientadores;

	@Init
	public void init() {
		if (getUsuario() != null)
			redirectHome();

		cursos = new CursoBusiness().getAll();
		orientadores = new ArrayList<Usuario>();

		itens = new ArrayList<ItemFormulario>();

		itens.add(new ItemFormulario("Nome do aluno(a): ", ""));
		itens.add(new ItemFormulario("Matrícula: ", ""));
		itens.add(new ItemFormulario("Curso: ", ""));
		itens.add(new ItemFormulario("Orientador(a): ", ""));
		itens.add(new ItemFormulario("Título do Trabalho: ", ""));
	}

	public List<ItemFormulario> getItens() {
		return itens;
	}

	public void setItens(List<ItemFormulario> itens) {
		this.itens = itens;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public List<Usuario> getOrientadores() {
		return orientadores;
	}

	public Usuario getTempUser() {
		return tempUser;
	}

	public void setTempUser(Usuario tempUser) {
		this.tempUser = tempUser;
	}

	@Command
	public void chooseOrientador(@BindingParam("window") Window window) {
		if (departamentos == null) {
			departamentos = new DepartamentoBusiness().getAll();
			System.out.println(departamentos.size());
			BindUtils.postNotifyChange(null, null, this, "departamentos");
		}
		window.doModal();
	}

	@Command
	public void selectedDepartamento(@BindingParam("dep") Comboitem combDep) {
		Departamento dep = (Departamento) combDep.getValue();
		tempUser = null;
		orientadores.clear();
		if (dep != null)
			orientadores = new UsuarioBusiness().getAllByDepartamento(dep);

		BindUtils.postNotifyChange(null, null, this, "tempUser");
		BindUtils.postNotifyChange(null, null, this, "orientadores");
	}

	@Command
	public void selectOrientador(@BindingParam("window") Window window) {
		if (tempUser != null) {
			itens.get(3).setResposta(tempUser.getNomeUsuario());
			BindUtils.postNotifyChange(null, null, this, "itens");
		}
		window.setVisible(false);
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
			Filedownload.save(is, "application/pdf",
					"SolicitacaoDeMatricula.pdf");
			// FileManager.deleteFile(pdfDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
