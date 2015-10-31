package br.ufjf.tcc.pdfHandle;

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
