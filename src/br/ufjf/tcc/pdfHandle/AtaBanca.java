package br.ufjf.tcc.pdfHandle;

import br.ufjf.tcc.model.TCC;

public class AtaBanca extends Ata {
	
	public AtaBanca(TCC tcc){
		super(tcc);
	}

	public void preencherPDF() throws Exception {

		try {
			for (int i = 0; i < trabMarcados.size(); i++) 
				PreenchimentoPDF.preencherBancaPDF(trabMarcados.get(i), i, idAluno , PASTA_COM_TEMPLATE_ATAS);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UniaoPDF.unirPDFsExibicaoBanca(trabMarcados.size(), idAluno);
	}

	@Override
	protected String getPathTemplate() throws Exception {
		return null;
	}

}
