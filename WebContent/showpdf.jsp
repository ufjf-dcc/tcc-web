<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
label{
	cursor: text;
	padding:4px;
}

h3 {
	color: #555;
	padding:6px;
}

</style>

<script type="text/javascript">

</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="height: 100%;" >
<div>
	

<div  style="float: left;display:inline-block; ;height:600px;width: 29%;border: 3px solid #cfcfcf ;">

	<div style="padding-left: 4px">
		<h3>Informações do trabalho</h3>
	</div>
	<hr>
	<br>
	<label style="font-family: sans-serif;font-weight: bold;font-size: 14px">
		Titulo
	</label>
	<br>
	<label style="font-family: sans-serif;font-weight: normal;font-size: 12px;">
		titulo tal
	</label>
	<br><br>
	<label style="font-family: sans-serif;font-weight: bold;font-size: 14px">
		Autor
	</label>
	<br>
	<label style="font-family: sans-serif;font-weight: normal;font-size: 12px">
		Matheus
	</label>
	<br><br>
	<label style="font-family: sans-serif;font-weight: bold;font-size: 14px">
		Orientador
	</label>
	<br>
	<label style="font-family: sans-serif;font-weight: normal;font-size: 12px">
		Orientador tal da silva x
	</label>
	<br><br>
	<label style="font-family: sans-serif;font-weight: bold;font-size: 14px">
		Resumo
	</label>
	<br>
	<label  style="word-wrap:break-word ;text-align:justify ;font-family:sans-serif;font-weight:normal;font-size:12px;display: block;" >
		asdasodpoasdpoasdopasdp oasdpasdaspdoasdasdas daspod kpoasdsapdasdka oskdpkaspodkokasdpokasp odkaspodkpoaskdpoaskdpoaskdpokapodk poasdkposadk osado ssdhisdhd odkpo skd
	 </label>
	 <br>
	 <label style="font-family: sans-serif;font-weight: bold;font-size: 14px">
		Ano: 
	</label>
	<label style="font-family: sans-serif;font-weight: normal;font-size: 12px">
		2009
	</label>
	<br><br>
	 <label style="font-family: sans-serif;font-weight: bold;font-size: 14px">
		Palavras-Chave
	</label>
	<br>
	<label style="word-wrap:break-word ;font-family: sans-serif;font-weight: normal;font-size: 12px">
		asidh asdiuh sudh s hdias hdi shiu
	</label>
	<br><br>
	 <label style="font-family: sans-serif;font-weight: bold;font-size: 14px">
		Obter PDF
	</label>
	<img src="./img/pdf.png" style="cursor: pointer;" />
		
</div>


		<div style="display: block ;float: right;width: 70%">
			<iframe style="height: 600px;width: 100%"  src="./exibePdf?id=${idTcc}" ></iframe>
		</div>
		
    	

</div>
</body>
</html>