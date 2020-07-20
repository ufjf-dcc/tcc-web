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

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.model.TCC;

@WebServlet("/exibePdf")
public class ExibiPdfServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TCC tcc = null;

	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String tccId = req.getParameter("id");
		
		if (tccId != null && !tccId.isEmpty() && tccId.matches("[0-9]+")) {
			TCCBusiness tccBusiness = new TCCBusiness();
			tcc = tccBusiness.getTCCById(Integer.parseInt(tccId));
			
			if (tcc != null) {
				File file = FileManager.getFile(tcc.getArquivoTCCFinal());
				
				if (file != null) {
					res.setHeader("Content-Disposition", "inline; filename="+tcc.getNomeTCC());
				
					try {
						outputStreamWrite(res, file);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					return;
				}
			}
		}
		
		File file = FileManager.getFile("modelo.pdf");
		
		try {
			outputStreamWrite(res, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void outputStreamWrite(HttpServletResponse res, File file) throws IOException {
		byte[] bytes = null;
		
		try {
			bytes = fileToByte(file);
		} catch (Exception e) {		
			e.printStackTrace();
		}
		
		res.setHeader("Content-Disposition", "inline; filename=modelo.pdf");
		res.setContentType("application/pdf"); 
		res.setContentLength(bytes.length);
		res.setStatus(200);
		res.getOutputStream().write(bytes);
	}

	public static byte[] fileToByte(File imagem) throws Exception {

		FileInputStream fis = new FileInputStream(imagem);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int bytesRead = 0;
		while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}
		fis.close();
		return baos.toByteArray();
	}


}
