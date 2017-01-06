package br.ufjf.tcc.pdfHandle;

//CLASSE USADA APENAS PARA DIVIDIR O TITULO PARA CABER NO PDF
public class DivisorString {
	
	public static String[] dividirTitulo(String titulo) {
		String tituAux = titulo;
		int resto = titulo.lastIndexOf(' ');
		String titulo1 = titulo;
		String titulo2 = "";
		String titulo3 = "";
		
		if(titulo.length()<55)
			resto=-1;
		while (resto > 55) {
			tituAux = tituAux.substring(0, resto);
			resto = tituAux.lastIndexOf(' ');
		}

		if (resto != -1) {
			titulo1 = titulo.substring(0, resto);
			titulo2 = titulo.substring(resto, titulo.length());
		}
		
		tituAux = titulo2;
		resto = titulo2.lastIndexOf(' ');
		
		if(titulo2.length() < 55)
			resto=-1;
		while (resto > 55) {
			tituAux = tituAux.substring(0, resto);
			resto = tituAux.lastIndexOf(' ');
		}

		if (resto != -1) {
			titulo3 = titulo2.substring(resto, titulo2.length());
			titulo2 = titulo2.substring(0, resto);
			
		}
		
		String titulos[] = new String[3];

		
			titulos[0] = titulo1;
			titulos[1] = titulo2;
			titulos[2] = titulo3;
		

		return titulos;

	}

}
