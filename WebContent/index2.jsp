<%@page import="br.ufjf.tcc.controller.ListaPublicaController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.zkoss.org/jsp/zul" prefix="z"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
</style>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript">
	function changeCurso(){
		location.href="./index5.jsp";
	}
	
	
	function visualzarTCC(id) {
		
	window.open('pages/visualiza.zul?id='+id,'_blank'); }
	
	

</script>

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

		<div class="container" align="center"
			style="margin: auto; background-color: #CCFFFF; width: 100%">
			Lista Pública de Trabalhos Acadêmico</div>
		<form action="index5.jsp" method="post">
			<div style="float: left;">

				Curso: <select id="curso" name="curso" onchange="this.form.submit()">

					<c:forEach var="curso" items="${cursos}">
						<option value="${curso.codigoCurso}"
							${cursoSelected == curso.codigoCurso? 'selected="selected"' : '' }>${curso.nomeCurso}</option>
					</c:forEach>

				</select>
			</div>
			<div style="float: right; padding: 5px; width: 50%;">
				Pesquisar: <input id="pesquisa" name="pesquisa" type="text"
					value="${PalavaPesquisa}" /> 
					
					Ano: 
					<select id="year" name="year">
					<c:forEach var="year" items="${years}">
						<option ${yearSelected == year? 'selected="selected"' : '' }>${year}</option>
					</c:forEach>

				</select>

				<button type="submit">Filtrar</button>

				<img src="./img/help.gif"
					onmouseover="document.getElementById('filterHelp')"
					style="cursor: help" />
		</form>
			
	</div>
	</div>

	<div style="position:absolute;top:100px;left:1px; width: 100%; font-size: small;">
		<table class="table table-hover table-bordered" style="width: 100%;">
			<tr style="background-color: windowframe;">
				<th style="width: 60%">Trabalho</th>
				<th>Autor</th>
				<th>PDF</th>
				<th>Extras</th>
			</tr>
			<c:forEach var="tcc" items="${tccs}">

				<tr style="cursor: pointer;">
					<td
						onclick="window.open('pages/visualiza.zul?id='+${tcc.idTCC},'_blank')">${tcc.nomeTCC}</td>
					<td
						onclick="window.open('pages/visualiza.zul?id='+${tcc.idTCC},'_blank')">${tcc.aluno.nomeUsuario}</td>
					<td><img src="img/pdf.png" style="cursor: pointer"
						onClick="./d.jsp" /></td>
					<td><img src="img/rar.png" style="cursor: pointer"
						onClick="./d.jsp" /></td>

				</tr>
			</c:forEach>
		</table>

	</div>


	</div>
</body>
</html>