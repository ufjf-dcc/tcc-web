<%@page import="java.util.ArrayList"%>
<%@page import="br.ufjf.tcc.controller.ListaPublicaController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.zkoss.org/jsp/zul" prefix="z"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	function changeCurso(){
		location.href="./index5.jsp";
	}
	
	
	
	
	

</script>
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
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js">
	


</script>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lista Pública de Trabalhos Acadêmicos - UFJF</title>
</head>

<%
	ArrayList teste = (ArrayList) request.getAttribute("tccs");
	String pesquisaFeita = (String) request.getAttribute("PalavaPesquisa");
	String cursoS = (String) request.getAttribute("cursoSelected");
	String yearSelected = (String) request.getAttribute("yearSelected");
	
	
	request.setAttribute("lista", teste);
	
	if(cursoS==null)
		request.setAttribute("cursoSel", "");
	else
		request.setAttribute("cursoSel", cursoS);
	
	if(pesquisaFeita==null)
		request.setAttribute("strBusca", "");
	else
		request.setAttribute("strBusca", pesquisaFeita);
	
	request.setAttribute("yearSelecionado", yearSelected);
	
%>


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

		<div class="navbar navbar-default" align="center"
			style="margin: auto; background-color: #CCFFFF; width: 100%;">
			<div  class="navbar-header">
			
			<a  class="navbar-brand" style="position: relative;left: 490px;" >Lista Pública de Trabalhos Acadêmico</a>
			</div>
			
		</div>
		
		
		
		<form class="form-inline" action="index5.jsp" method="post">
			<div class="form-group" style="float: left;position: relative;top: 5px">

				<label>Curso:</label> 
				<select class="form-control" style="width: 200px;" id="curso" name="curso" onchange="this.form.submit()">

					<c:forEach var="curso" items="${cursos}">
						<option  value="${curso.codigoCurso}"
							${cursoSel == curso.codigoCurso? 'selected="selected"' : '' }>${curso.nomeCurso}</option>
					</c:forEach>

				</select>
			</div>
			<div style="float: right; padding: 5px; width: 520px;" >
				<label>Pesquisar:</label> 
				<input class="form-control" id="pesquisa" name="pesquisa" type="text"
					value="${strBusca}" /> 
					
					<label>Ano:</label> 
					<select class="form-control"  id="year" name="year">
					<c:forEach var="year" items="${years}">
						<option ${yearSelecionado == year? 'selected="selected"' : '' }>${year}</option>
					</c:forEach>

				</select>

				<button class="btn btn-primary" type="submit">Filtrar</button>

				<img src="./img/help.gif"
					onmouseover="document.getElementById('filterHelp')"
					style="cursor: help" />
		
			
	</div>
	</div>
	
	
	
	<div style="position:absolute;top:140px;left:1px; width: 100%; font-size: small;">
	<pg:pager id="p" maxPageItems="7" maxIndexPages="50" 
	export="offset,currentPageNumber=pageNumber" scope="request">
		<table class="table table-hover table-bordered" style="width: 100%;">
			<tr style="background-color: windowframe;">
				<th style="width: 60%">Trabalho</th>
				<th>Autor</th>
				<th>PDF</th>
				<th>Extras</th>
			</tr>
			<c:forEach var="tcc" items="${lista}">
				<pg:item>
				<tr style="cursor: pointer;">
					<td
						onclick="window.open('./showPdf?id=${tcc.idTCC}','_blank')">${tcc.nomeTCC}</td>
					<td
						onclick="window.open('./showPdf?id=${tcc.idTCC}','_blank')">${tcc.aluno.nomeUsuario}</td>
					<td onClick="location.href='downloadPdf?id=${tcc.idTCC}'"><img src="img/pdf.png" style="cursor: pointer"
					
					 /></td>
					<td><img ${tcc.arquivoExtraTCCFinal != null ? 'src="img/rar.png"' : 'src="img/norar.png"'} style="cursor: pointer"
						onClick="location.href='downloadExtra?id=${tcc.idTCC}'" /></td>

				</tr>
				
				</pg:item>
			</c:forEach>
		</table>
		
		<div align="center">
		<ul class="pagination pagination-sm">
			<pg:index>
	    <pg:prev>
	     <li> <a href="<%= pageUrl.replace("index2.jsp", "index5.jsp")+"&curso="+request.getAttribute("cursoSel")+"&pesquisa="+request.getAttribute("strBusca")+"&year="+request.getAttribute("yearSelecionado")+"&page="+pageNumber %>">&lt;&lt; Anterior</a> </li>
	    </pg:prev>
	    <pg:pages>
	     <li ${pageNumber == page ? 'class="active"' : ''} >   <a href="<%= pageUrl.replace("index2.jsp", "index5.jsp")+"&curso="+request.getAttribute("cursoSel")+"&pesquisa="+request.getAttribute("strBusca")+"&year="+request.getAttribute("yearSelecionado")+"&page="+pageNumber %>"><%= pageNumber %></a> </li> 
	    </pg:pages>
	    <pg:next>
	    <li>  <a href="<%= pageUrl.replace("index2.jsp", "index5.jsp")+"&curso="+request.getAttribute("cursoSel")+"&pesquisa="+request.getAttribute("strBusca")+"&year="+request.getAttribute("yearSelecionado")+"&page="+pageNumber %>">Próximo &gt;&gt;</a> </li>
	    </pg:next>
	  </pg:index>
		</ul>
		</pg:pager>
	</div>
	</div>
	<script type="text/javascript">
	function showError(){
		window.alert("Arquivo não encontrado");
	}
	</script>

	</div>
	
	

  
	
</body>
</html>