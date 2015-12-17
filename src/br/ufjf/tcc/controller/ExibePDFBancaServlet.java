package br.ufjf.tcc.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.pdfHandle.Ata;

@WebServlet("/exibePdfBanca")
public class ExibePDFBancaServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String Id = req.getParameter("id");

		
		File file = new File(Ata.PASTA_ARQUIVOS_TEMP + Ata.COMPOSICAO_BANCA_FINAL+ Id + Ata.EXTENSAO_PDF);
		
		
		if(file==null){
			file = FileManager.getFile("modelo.pdf");
			res.setHeader("Content-Disposition",
					"inline; filename=modelo.pdf");
		}else{
			res.setHeader("Content-Disposition",
					"inline; filename=BancasMarcadas.pdf");
		}
		byte[] bytes = null;
		try {
			bytes = fileToByte(file);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		res.setContentType("application/pdf"); 
		res.setContentLength(bytes.length);
		
		res.getOutputStream().write(bytes);
		
		File d = new File(Ata.PASTA_ARQUIVOS_TEMP + Ata.COMPOSICAO_BANCA_FINAL + Id + Ata.EXTENSAO_PDF);
		
		if (d.delete())
			System.out.println("deletado");
		else
			System.out.println("NAO deletado");

	}

	public static byte[] fileToByte(File imagem) throws Exception {

		FileInputStream fis = new FileInputStream(imagem);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int bytesRead = 0;
		while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}
		return baos.toByteArray();
	}


}
