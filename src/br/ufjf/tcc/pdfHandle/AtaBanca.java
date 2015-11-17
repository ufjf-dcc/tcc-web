package br.ufjf.tcc.pdfHandle;

public class AtaBanca extends Ata {

	@Override
	public void preencherPrincipal() throws Exception {

		try {

			for (int i = 0; i < trabMarcados.size(); i++) 
				PreenchimentoPDF.bancaPDF(trabMarcados.get(i), i, idAluno , pathTemplateAta);

		} catch (Exception e) {
			e.printStackTrace();

		}

		UniaoPDF.unirPDFsExibicaoBanca(trabMarcados.size(), idAluno);
		
	}

}
