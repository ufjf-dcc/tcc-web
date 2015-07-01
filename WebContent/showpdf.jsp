<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
label {
	cursor: text;
	padding-left: 4px;
	display: block;
}

.head {
	padding-top: 10px;
}

h3 {
	color: #555;
	padding-left: 6px;
}
</style>

<script type="text/javascript">
	
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="height: 100%;">
	<div style="height: auto">


		<div
			style="float: left; display: inline-block;; height: auto; width: 29%; border: 3px solid #cfcfcf;">

			<div style="padding-left: 4px">
				<h3>Informações do trabalho</h3>
			</div>
			<hr>

			<label class="head"
				style="font-family: sans-serif; font-weight: bold; font-size: 14px;">
				Titulo </label> 
				<label
				style="font-family: sans-serif; font-weight: normal; font-size: 12px;">
				${tcc.nomeTCC} </label> 
				<label class="head"
				style="font-family: sans-serif; font-weight: bold; font-size: 14px;">
				Subtítulo </label> 
				<label
				style="font-family: sans-serif; font-weight: normal; font-size: 12px;">
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
					style="font-family: sans-serif; font-weight: normal; font-size: 12px; display: block; padding-top: 11px; padding-left: 40px;">
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
				style=" ${tcc.arquivoExtraTCCFinal==nul? 'visibility: hidden;' : 'visibility: visible;' }">
				<label class="head"
					style="font-family: sans-serif; font-weight: bold; font-size: 14px; float: left;">
					Obter arquivos extras </label> 
					<img
					onClick="location.href='downloadExtra?id=${tcc.idTCC}'"
					src="./img/rar.png" style="cursor: pointer; display: block;" />

			</div>
			<br>
			<br>
		</div>


		<div style="display: block; float: right; width: 70%; height: 100%">
			<iframe style="height: 650px; width: 100%"
				src="./exibePdf?id=${tcc.idTCC}"></iframe>
		</div>





	</div>
</body>
</html>