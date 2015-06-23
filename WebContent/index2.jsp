<%@page import="br.ufjf.tcc.controller.ListaPublicaController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.zkoss.org/jsp/zul" prefix="z"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lista Pública de Trabalhos Acadêmicos - UFJF</title>
</head>
<body>	
<div>
		<z:page zscriptLanguage="java">

		<z:script type="text/javascript">
			function visualzarTCC(id) {
			window.open('pages/visualiza.zul?id='+id,'_blank'); }
			
			
		</z:script>
		<z:style src="/style.css" />
		
		<z:div apply="org.zkoss.bind.BindComposer"
		viewModel="@id('vm') @init('br.ufjf.tcc.controller.ListaPublicaController')"
		height="100%" class="publicList">
		<z:include src="@load(vm.menu)" />
	
		
		</z:div>
	
		<z:popup id="filterHelp">
			<z:html>

			<![CDATA[ Permite filtrar a lista de TCCs com o curso, o
			termo e o ano <br> escolhidos. O termo pode ser, por
			exemplo: <ul><li>Autor ou orientador;</li> <li>Nome do
			TCC;</li> <li>Palavra-chave;</li> <li>Conteúdo dos
			resumos.</li> <ul>]]>

			</z:html>
		</z:popup>
			
	</z:page>
	
	<div align="center" style="margin: auto; background-color: #CCFFFF; border: 3px;border-color: black;" > <h2>Lista Pública de Trabalhos Acadêmicos</h2> </div>
	<div>
	<form action="index5.jsp" method="post">
		Curso:
		<select id="curso" name="curso">
			
			<c:forEach var="curso" items="${cursos}">
				<option value="${curso.codigoCurso}" ${cursoSelected == curso.codigoCurso? 'selected="selected"' : '' } >${curso.nomeCurso}</option>
			</c:forEach>
			
		</select>
		<div style="float: right;padding: 10px">
			Pesquisar:
			<input id="pesquisa" name="pesquisa" type="text" value="${PalavaPesquisa}">  </input>
			
			Ano:
			<select id="year" name="year">
			<c:forEach var="year" items="${years}">
				<option ${yearSelected == year? 'selected="selected"' : '' }>${year}</option>
			</c:forEach>
				
			</select>
			
			<button type="submit">Filtrar</button>
			
			<img src="./img/help.gif" onmouseover="document.getElementById('filterHelp')" style="cursor: help" />
		</form>
		</div>
	 </div>
	 
	 <div style="width: 100%;font-size: small;">
	 <table style="width: 100%;border: 2px solid black;">
	 		<tr style="background-color: windowframe;">
	 			<th style="width: 70%">Trabalho</th>
	 			<th>Autor</th>
	 			<th>PDF</th>
	 			<th>Extras</th>
	 		</tr>
		  <c:forEach var="tcc" items="${tccs}">
		  
		  	  <tr>
		      <td>${tcc.nomeTCC}</td>
		      <td>${tcc.aluno.nomeUsuario}</td>
		      <td><img src="img/pdf.png" style="cursor: pointer"
						onClick="@command('downloadPDF', tcc=each)" /></td>
				<td><img src="img/rar.png" style="cursor: pointer"
						onClick="@command('downloadPDF', tcc=each)" /></td>
		      
		    </tr>
		  
		  
		  </c:forEach>
		</table>
	 
	 </div>
	


		
	</div>
		
</body>
</html>