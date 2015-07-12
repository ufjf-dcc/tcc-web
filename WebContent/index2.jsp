<%@page import="java.util.Collections"%>
<%@page import="org.hibernate.mapping.Collection"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar "%>
<%@page import="br.ufjf.tcc.controller.ListaPublicaController"%>
<%@page import="br.ufjf.tcc.model.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.zkoss.org/jsp/zul" prefix="z"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

		<link href="tooltip.css" rel="stylesheet" type="text/css" />
   		 <script src="tooltip.js" type="text/javascript"></script>

    <script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
    <script src="http://cdn.kendostatic.com/2015.2.624/js/kendo.all.min.js"></script>

<script type="text/javascript">


	function changeCurso(){
		location.href="./index5.jsp";
	}
	
	function showPopup(){
		document.getElementById("popup").style.display="block" ;
	}
	
	function hidePopup(){
		document.getElementById("popup").style.display="none" ;
	}
	
	
	

</script>
<style type="text/css">
th {
	height: 10px;
	background: linear-gradient(to bottom, #fefefe 0%, #eeeeee 100%);
}

.table td {
	vertical-align: middle;
}

.table tr:HOVER{
background:	linear-gradient(to right, #fdfdfd 0%, #f1f1f1 100%); /* W3C */
	background:	-webkit-linear-gradient(top, #fdfdfd 0%, #f1f1f1 100%); /* Chrome10+,Safari5.1+ */
	background:	linear-gradient(to bottom, #fdfdfd 0%, #f1f1f1 100%);
	background: linear-gradient(to bottom, #e5f4fb 0%, #d3edfa 100%);
}

.divPesquisa{
	border-left:1px solid #cfcfcf;
	border-right:1px solid #cfcfcf;
	background: linear-gradient(to bottom, #fefefe 0%, #eeeeee 100%);
}

.headerPub{
	 border:1px solid #cfcfcf;
	 background: linear-gradient(to bottom, #fefefe 0%, #eeeeee 100%);
	 
}

#popup {
	box-shadow: 10px 10px 5px #888888;
	left: 50px;
	box-shadow: 0px 1px 10px 3px rgba(0,0,0,0.75);
}



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

<%!
	public static String getEachTccYear(TCC tcc){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
		
		return "" + cal.get(Calendar.YEAR);
	}
%>


<body>
<div>
							

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


		</z:page>

		<div class="headerPub" align="center"
			style="margin: auto; width: 100%;height:32px;font-size: 12px;font-weight: bold;color: #636363">
			<div  style="padding: 8px 5px 3px;vertical-align: middle;" >
			
				Lista Pública de Trabalhos Acadêmico
			</div>
			
		</div>
		
		
		<div class="divPesquisa" style="width: 100%;height: 40px;display: block;">
		<form class="form-inline" action="index5.jsp" method="post">
		
			<div style="float: left;position: relative;top: 5px;left: 20px">

				<label style="font-size: 12px;font-family: Arial,sans-serif;font-weight: normal;" >Curso:</label> 
				<select class="form-control input-sm" style="width: 250px;" id="curso" name="curso" onchange="this.form.submit()">

					<c:forEach var="curso" items="${cursos}">
						<option  value="${curso.codigoCurso}"
							${cursoSel == curso.codigoCurso? 'selected="selected"' : '' }>${curso.nomeCurso}</option>
					</c:forEach>

				</select>
			</div>
			<div style="float: right; padding-top: 5px; width: 520px;" >
				<label style="font-size: 12px;font-family: Arial,sans-serif;font-weight: normal;" >Pesquisar:</label> 
				<input class="form-control input-sm" id="pesquisa" name="pesquisa" type="text"
					value="${strBusca}" /> 
					
					<label style="padding-left: 20px;font-size: 12px;font-family: Arial,sans-serif;font-weight: normal;" >Ano:</label> 
					<select style="" class="form-control input-sm"  id="year" name="year">
					<c:forEach var="year" items="${years}">
						<option ${yearSelecionado == year? 'selected="selected"' : '' }>${year}</option>
					</c:forEach>

				</select>
				
				<button  class="btn btn-primary btn-sm" style="margin-left: 20px;"  type="submit">Filtrar</button>
				
				<img  src="./img/help.gif"
					
					style="cursor: help;padding-left: 20px"
					onmouseover="showPopup()"
					onmouseout="hidePopup()"
					 />
					
					<div id="popup"  align="left"
				style="position:relative;z-index:5 ;margin: auto; width: 380px;height:150px;font-size: 12px;font-weight: bold;color: #636363;display: none;background-color: white;"
				
				>
						<div  style="padding-top: 7px;padding-left: 10px;background-color: white;z-index: 4" >
						
						 
						Permite filtrar a lista de TCCs com o curso, o
					termo e o ano <br> escolhidos. O termo pode ser, por
					exemplo:<br> <ul><li>Autor ou orientador;</li> <li>Nome do
					TCC;</li> <li>Palavra-chave;</li> <li>Conteúdo dos
					resumos.</li> <ul>
						</div>
						
					</div>
	
			
		</div>
	
	</div>
	</div>
	
	
	
	<div style="position:absolute;z-index: 3 ; font-size: 12px;width: 99.25%">
	<pg:pager id="p" maxPageItems="10" maxIndexPages="50"
	export="offset,currentPageNumber=pageNumber" scope="request">
		<table class="table table-bordered" style="z-index: 2;font-family: Arial,sans-serif;font-size: 12px;margin-bottom: 2px;padding-right: 10px;border: 1px solid #cfcfcf"  >
			<tr style="color: #636363;height: 30px; " >
				<th  width= "68%" style="padding: 5px 5px;vertical-align: middle;">Trabalho</th>
				<th width="20%" style="padding: 5px 5px;vertical-align: middle;">Autor</th>
				<th width="5%" style="padding: 5px 5px;vertical-align: middle;">PDF</th>
				<th width="5%" style="padding: 5px 5px;vertical-align: middle;">Extras</th>
			</tr>
			<c:forEach var="tcc" items="${lista}">
				<pg:item>
				<tr style="cursor: pointer;color:#636363;vertical-align: middle;height: 42px;margin: auto;">
					
					<td  style="vertical-align: middle;padding: 2px 5px;" >
						<a  style="color:#636363;vertical-align: middle;" href="./tcc?id=${tcc.idTCC}" target="_blank" >
							<div onmouseover="tooltip.pop(this,'#${tcc.idTCC}',{position:2})" style="padding: 4px 5px;line-height: 24px">
								<div style="line-height: 24px" >
									<span style="line-height: normal;">
									   ${tcc.nomeTCC} 
									</span>
								</div>
							</div>
						</a>
					</td>
					
					<td  style="vertical-align: middle;padding: 5px 5px;"
						onclick="window.open('./tcc?id=${tcc.idTCC}','_blank')">
						<a  style="color:#636363;vertical-align: middle;" href="./tcc?id=${tcc.idTCC}" target="_blank" >
							<div onmouseover="tooltip.pop(this,'#${tcc.idTCC}',{position:3})" style="padding: 4px 5px;line-height: 24px">
								<div style="line-height: 24px" >
									<span style="line-height: normal;">
									    ${tcc.aluno.nomeUsuario}
									</span>
								</div>
							</div>
						</a>
					</td>
					
					<td style="vertical-align: middle ;padding: 5px 5px;" onClick="location.href='downloadPdf?id=${tcc.idTCC}'"><img src="img/pdf.png" style="cursor: pointer;float:right;padding-right: 10px;"
					
					 /></td>
					<td style="vertical-align: middle;padding: 5px 5px;" ><img ${tcc.arquivoExtraTCCFinal != null ? 'src="img/rar.png"' : 'src="img/norar.png"'} style="cursor: pointer;float:right;padding-right: 10px;"
						onClick="location.href='downloadExtra?id=${tcc.idTCC}'" /></td>
					
				</tr>
				<div style="display:none;">
				    <div id="${tcc.idTCC}">
				       <div style="width:550px;">
								<label
									style="font-size: 14px; font-weight: bold; text-align: justify; display: block;" >Resumo </label>
								<label style="font-size: 12px;font-family: Arial,sans-serif;font-weight: normal;" >${tcc.resumoTCC} </label>
								<div>
									<label 
										style="font-weight:bold;" >Ano</label>
									<label style="font-size: 12px;font-family: Arial,sans-serif;font-weight: normal;" > ${tcc.dataEnvioFinal.toString().substring(0,4) }    </label>
								</div>
								<label 
									style="font-size: 14px; font-weight: bold; text-align: justify; display: block;" >Orientador </label>
								<label style="font-size: 12px;font-family: Arial,sans-serif;font-weight: normal;">${tcc.orientador.nomeUsuario } </label>
								<label 
									style="font-size: 14px; font-weight: bold; text-align: justify;${tcc.coOrientador != null? 'display:block' : 'display:none'} ">Co-Orientador </label>
								
								<label style="font-size: 12px;font-family: Arial,sans-serif;font-weight: normal;${tcc.coOrientador != null? 'display:block' : 'display:none'}" > ${tcc.coOrientador.nomeUsuario}</label>
								<label 
									style="font-size: 14px; font-weight: bold; text-align: justify; display: block;" >Palavras-chave</label>
								<label style="font-size: 12px;font-family: Arial,sans-serif;font-weight: normal;" >${tcc.palavrasChave} </label>
							</div>
				    </div>
				</div>
				
				
					
					
				
				</pg:item>
			</c:forEach>
			
			
		</table>
					
		<div style="display: block;position: relative;" >
		<ul  class="pagination pagination-sm" style="background-color: black;margin: auto " >
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
		</div>
		</pg:pager>
		
	</div>
	
	</div>
	<script type="text/javascript">
	function showError(){
		window.alert("Arquivo não encontrado");
	}
	</script>

	
	
				

  
	
</body>
</html>