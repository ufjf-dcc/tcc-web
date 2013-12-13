package br.ufjf.tcc.library;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.TipoUsuarioBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class FileManager {

	// private static final String FILE_PATH = "/TCCFiles/";
	// private static final String FILE_PATH =
	// "/Applications/MAMP/htdocs/tcc/files/";
	private static final String FILE_PATH = "/home/users/jorge/files/";

	public static InputStream getFileInputSream(String fileName) {
		try {
			InputStream is = new FileInputStream(new File(FILE_PATH + fileName));
			return is;
		} catch (Exception e) {
			return null;
		}
	}

	public static Boolean deleteFile(String fileName) {
		try {
			if (fileName != null && fileName.trim() != "") {
				File file = new File(FILE_PATH + fileName);
				file.delete();
				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String saveFileInputSream(InputStream fileIS, String fileExt) {
		String fileName = null;
		OutputStream outputStream = null;
		if (fileExt != null && fileExt.trim() != "") {
			try {
				AbstractChecksum checksum = JacksumAPI
						.getChecksumInstance("md5");
				checksum.update(("" + System.currentTimeMillis()).getBytes());
				fileName = checksum.getFormattedValue() + "." + fileExt;

				outputStream = new FileOutputStream(new File(FILE_PATH
						+ fileName));

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = fileIS.read(bytes)) != -1)
					outputStream.write(bytes, 0, read);

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (fileIS != null)
						fileIS.close();
					if (outputStream != null)
						outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return fileName;
	}

	public List<TCC> readXML(Usuario secretary, Media media) throws Exception {
		String fileName = "temp.xml";

		if (media.isBinary()) {
			Files.copy(new File(FILE_PATH + fileName), media.getStreamData());
		} else {
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH
					+ fileName));
			Files.copy(writer, media.getReaderData());
			writer.flush();
			writer.close();
		}

		File xml = new File(FILE_PATH + fileName);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xml);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("tcc");
		List<TCC> tccs = new ArrayList<TCC>();
		TipoUsuarioBusiness tipoUsuarioBusiness = new TipoUsuarioBusiness();

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				Curso curso = new CursoBusiness()
						.getCursoByCode(eElement.getElementsByTagName("curso")
								.item(0).getTextContent());

				Usuario aluno = new UsuarioBusiness()
						.getByName(eElement.getElementsByTagName("autor")
								.item(0).getTextContent());
				if (aluno == null) {
					aluno = new Usuario();
					aluno.setNomeUsuario(eElement.getElementsByTagName("autor")
							.item(0).getTextContent());
					aluno.setAtivo(false);
					// aluno.setCurso(secretary.getCurso());
					aluno.setCurso(curso);
					aluno.setMatricula(eElement
							.getElementsByTagName("matricula").item(0)
							.getTextContent());
					aluno.setSenha("123");
					aluno.setEmail("a@a.com");
					aluno.setTipoUsuario(tipoUsuarioBusiness
							.getTipoUsuario(Usuario.ALUNO));
				}

				Usuario orientador = new UsuarioBusiness().getByName(eElement
						.getElementsByTagName("orientador").item(0)
						.getTextContent());
				if (orientador == null) {
					orientador = new Usuario();
					orientador.setNomeUsuario(eElement
							.getElementsByTagName("orientador").item(0)
							.getTextContent());
					orientador.setAtivo(false);
					// orientador.setMatricula("seila");
					orientador.setSenha("123");
					aluno.setEmail("a@a.com");
					orientador.setTipoUsuario(tipoUsuarioBusiness
							.getTipoUsuario(Usuario.PROFESSOR));
				}

				Date date = new SimpleDateFormat("dd/MM/yyyy").parse(eElement
						.getElementsByTagName("data").item(0).getTextContent());
				java.sql.Timestamp timestamp = new Timestamp(date.getTime());

				tccs.add(new TCC(eElement.getElementsByTagName("titulotcc")
						.item(0).getTextContent().toUpperCase(), eElement
						.getElementsByTagName("subtitulo").item(0)
						.getTextContent(), eElement
						.getElementsByTagName("resumo").item(0)
						.getTextContent(), eElement
						.getElementsByTagName("palavrasChave").item(0)
						.getTextContent(), eElement.getElementsByTagName("pdf")
						.item(0).getTextContent(), eElement
						.getElementsByTagName("arquivo").item(0)
						.getTextContent(), timestamp, aluno, orientador));

			}
		}
		deleteFile(FILE_PATH + fileName);
		return tccs;
	}
}
