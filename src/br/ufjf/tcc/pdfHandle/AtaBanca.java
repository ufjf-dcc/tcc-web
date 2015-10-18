package br.ufjf.tcc.pdfHandle;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.ufjf.tcc.library.ConfHandler;

public class AtaBanca extends Ata {

	// PREENCHE AS PAGINA 2 E 3 DO PDF
	@Override
	public void preenchePrincipal() throws Exception {


		// SEGUNDA
		// PAGINA------------------------------------------------------------------------------------------
		// PREENCHIMENTO DAS PRIMEIRAS INFORMACOES

		try {

			for (int i = 0; i < trabMarcados.size(); i++) {
				PreenchimentoPDF.bancaPDF(trabMarcados.get(i), i, idAluno , pathTemplateAta);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		Unir.tudoBanca(trabMarcados.size(), idAluno);
		


	}

}
