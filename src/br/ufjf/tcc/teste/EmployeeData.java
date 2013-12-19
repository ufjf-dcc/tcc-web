package br.ufjf.tcc.teste;

/*Exemplo obtido de:
 * http://www.e-zest.net/blog/integrating-apache-fop-with-java-project-to-generate-pdf-files/
 * @author Debasmita.Sahoo
 */

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EmployeeData")
public class EmployeeData {
	public EmployeeData () {
	}
 
	private List<Employee> employeeList;
 
	@XmlElementWrapper(name = "employeeList")
	@XmlElement(name = "employee")
	public List<Employee> getEmployeeList() {
		return employeeList;
	}
	public void setEemployeeList (List<Employee> employeeList) {
		this.employeeList = employeeList;
	}
 
}

