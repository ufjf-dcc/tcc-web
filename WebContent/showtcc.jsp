<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://www.zkoss.org/jsp/zul" prefix="z"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta name="citation_title" content="${tcc.nomeTCC }">
<meta name="citation_author" content="${tcc.aluno.nomeUsuario}">
<meta name="citation_date" content="${tccYear}">
<meta name="citation_publication_date" content="${tccDate}">
<meta name="citation_dissertation_institution" content="Universidade Federal de Juiz de Fora">
<meta name="citation_pdf_url" content="http://www.monografias.ice.ufjf.br/tcc-web/exibePdf?id=${tcc.idTCC}">
<meta name="citation_abstract_html_url" content="http://www.monografias.ice.ufjf.br/tcc-web/tcc?id=${tcc.idTCC}">
<style type="text/css">

html, body { 
	margin:0; height: 100%;

}

label {
	cursor: text;
	
	display: block;
}

.head {
	padding-top: 5px;
}

h3 {
	color: #555;
	padding-left: 1px;
}

ul {
border-collapse: separate;
border-spacing: 0px;
zoom:1;
clear:both;

}

li {
	border-width: 1px 1px 0 1px;
	background-color: #ffffff;
	border-radius: 4px 4px 0 0;
	position: relative;
	float: left;
	padding-top: 1px;
	overflow: hidden; 
}
</style>

<script type="text/javascript">
	
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Visualizar Trabalho</title>
</head>
<body >
<div >
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
		
		</div>
	<div style="display:block ;height: 92%;background-color: #efefef;width: 28%;float: left;padding: 10px;"> 


		<div
			style="position:relative; display: block; overflow: hidden;border: 0px;padding: 0px;margin: 0px ">

			<ul style="width: 100%; padding-left: 0px;padding-top: 0px;margin: 0px">
				<li style="border: 1px solid #cfcfcf ;line-height: 30px;">
				<span style="color: #555;font-weight: bold;font-style: normal;font-size: 12px;font-family: sans-serif;padding: 4px 12px 12px" >Informações do Trabalho</span>
				</li>
			</ul>			
		</div>		
		<div style="border-top: 0px;padding: 5px;zoom:1;border: 1px solid #cfcfcf;overflow: scroll;height: inherit;background-color: #efefef" >
			<label class="head"
				style="font-family: sans-serif; font-weight: bold; font-size: 14px;">
				Titulo </label> 
				<label
				style="font-family: sans-serif; font-weight: normal; font-size: 12px;">
				${tcc.nomeTCC} </label> 
				<label id="subTitulo" class="head"
				style="font-family: sans-serif; font-weight: bold; font-size: 14px;${tcc.subNomeTCC==nul? 'display: none;' : 'display: block;' }">
				Subtítulo </label> 
				<label
				style="font-family: sans-serif; font-weight: normal; font-size: 12px;${tcc.subNomeTCC==nul? 'display: none;' : 'display: block;' }">
				${tcc.subNomeTCC} </label> 
				<label class="head"
				style="font-family: sans-serif; font-weight: bold; font-size: 14px">
				Autor </label> 
				<label
				style="font-family: sans-serif; font-weight: normal; font-size: 12px">
				${tcc.aluno.nomeUsuario } </label> 
				<label class="head"
				style="font-family: sans-serif; font-weight: bold; font-size: 14px">
				Orientador </label> 
				<label
				style="font-family: sans-serif; font-weight: normal; font-size: 12px">
				${tcc.orientador.nomeUsuario } </label> 
				<label class="head"
				style="font-family: sans-serif; font-weight: bold; font-size: 14px">
				Resumo </label> 
				<label
				style="word-wrap: break-word; text-align: justify; font-family: sans-serif; font-weight: normal; font-size: 12px; display: block;">
				${tcc.resumoTCC } </label>
				
			<div class="">
				<label class="head"
					style="font-family: sans-serif; font-weight: bold; font-size: 14px; float: left;">
					Ano: </label>
					<label
					style="font-family: sans-serif; font-weight: normal; font-size: 12px; display: block; padding-top: 7px; padding-left: 35px;">
					${tccYear} </label>
			</div>
			<label class="head"
				style="font-family: sans-serif; font-weight: bold; font-size: 14px">
				Palavras-Chave </label> 
				<label
				style="word-wrap: break-word; font-family: sans-serif; font-weight: normal; font-size: 12px">
				${tcc.palavrasChave } </label>
			<div class="head"
				style=" ${tcc.arquivoTCCFinal==nul? 'visibility: hidden;' : 'visibility: visible;' }">
				<label class="head"
					style="font-family: sans-serif; font-weight: bold; font-size: 14px; float: left;">
					Obter PDF </label> 
					<img
					onClick="location.href='downloadPdf?id=${tcc.idTCC}'"
					src="./img/pdf.png" style="cursor: pointer; display: block;" />

			</div>
			<div class="head"
				style=" ${tcc.arquivoExtraTCCFinal==nul? 'display: none;' : 'display: block;' }">
				<label class="head"
					style="font-family: sans-serif; font-weight: bold; font-size: 14px; float: left;">
					Obter arquivos extras </label> 
					<img
					onClick="location.href='downloadExtra?id=${tcc.idTCC}'"
					src="./img/rar.png" style="cursor: pointer; display: block;" />

			</div>
			<div class="head" >
				<label class="head"
					style="font-family: sans-serif; font-weight: bold; font-size: 14px; float: left;">
					<a href="./bibtex?id=${tcc.idTCC}" target="blank" >Obter Bibtex </a>
					
					 </label> 
					

			</div>
			
		</div>
		

	</div>
	
	<div style="display: block; float: right; width: 70%; height: 95%">
			<iframe style=" height:100% ;width: 100%"
				src="./exibePdf?id=${tcc.idTCC}"></iframe>
		</div>
</body>
</html>