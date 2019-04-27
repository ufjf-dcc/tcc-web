package br.ufjf.tcc.pdfHandle;

import br.ufjf.tcc.library.ConfHandler;
import br.ufjf.tcc.model.TCC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.itextpdf.text.pdf.*;

public class CartaParticipacaoBanca {

	private TCC tcc;
	private String template;
	private FileOutputStream saida;
	private PdfReader leitor;
	private PdfStamper stamper;
	private PdfContentByte over;
	private AcroFields form;
	
	private static final String TEMPLATE = "CartaParticipacaoBanca";
	
	public static final String EXTENSAO_PDF = ".pdf";
	public static final String PASTA_ARQUIVOS_TEMP = ConfHandler.getConf("FILE.PATH") + "arquivosTemporarios/";
	public static final String PASTA_COM_TEMPLATE = ConfHandler.getConf("FILE.PATH") + "templatePDF/";
	
	
	public Ata
	
}
