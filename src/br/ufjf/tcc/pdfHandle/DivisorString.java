package br.ufjf.tcc.pdfHandle;

//CLASSE USADA APENAS PARA DIVIDIR O TITULO PARA CABER NO PDF
public class DivisorString {
	
	public static String[] dividirTitulo(String titulo) {
		String tituAux = titulo;
		int resto = titulo.lastIndexOf(' ');
		String titulo1 = "";
		String titulo2 = "";

		while (resto > 60) {
			tituAux = tituAux.substring(0, resto);
			resto = tituAux.lastIndexOf(' ');
		}

		if (resto != -1) {
			titulo1 = titulo.substring(0, resto);
			titulo2 = titulo.substring(resto, titulo.length());

		}
		String titulos[] = new String[2];

		if (titulo.length() < 60) {
			titulos[0] = titulo;
			titulos[1] = "";
		} else {

			titulos[0] = titulo1;
			titulos[1] = titulo2;

		}

		return titulos;

	}

}
