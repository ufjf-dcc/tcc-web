package br.ufjf.tcc.teste;

/*Exemplo obtido de:
 * http://www.e-zest.net/blog/integrating-apache-fop-with-java-project-to-generate-pdf-files/
 * @author Debasmita.Sahoo
 */

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.library.PDFHandler;

public class TestPDF {
	public TestPDF() {
		// Cria os objetos que serão impressos no PDF
		System.out.println("Hi Testing");
		ArrayList<Employee> employeeList = new ArrayList<Employee>();
		String templateFilePath = FileManager.FILE_PATH;

		Employee e1 = new Employee();
		e1.setName("Debasmita1 Sahoo");
		e1.setEmployeeId("10001");
		e1.setAddress("Pune");
		employeeList.add(e1);

		Employee e2 = new Employee();
		e2.setName("Debasmita2 Sahoo");
		e2.setEmployeeId("10002");
		e2.setAddress("Test");
		employeeList.add(e2);

		Employee e3 = new Employee();
		e3.setName("Debasmita3 Sahoo");
		e3.setEmployeeId("10003");
		e3.setAddress("Mumbai");
		employeeList.add(e3);

		EmployeeData data = new EmployeeData();
		data.setEemployeeList(employeeList);
		PDFHandler handler = new PDFHandler();

		try {
			// Converte os objetos para XML
			ByteArrayOutputStream streamSource = handler.getXMLSource(data);

			// Converte o XML para XSL-FO e então para PDF; Imprieme o diretório
			System.out.println(handler.createPDFFile(streamSource,
					templateFilePath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
