<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://www.zkoss.org/jsp/zul" prefix="z"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista Pública de Trabalhos Acadêmicos - UFJF</title>
    </head>
    <body>
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
		<z:grid model="@load(vm.filterTccs) @template('lista')"
			emptyMessage="@load(vm.emptyMessage)" mold="paging" pageSize="10"
			vflex="true">
			<z:auxhead style="text-align: center;">
				<z:auxheader label="Lista Pública de Trabalhos Acadêmicos"
					colspan="4" />
			</z:auxhead>
			<z:auxhead>
				<z:auxheader colspan="4">
					<z:div>
						<z:hlayout
							style="float:left;padding-left:10px;">
							<z:label value="Curso: "
								style="margin-left=10px;margin-top:6px;" />
							<z:combobox id="cmbCurso" width="220px"
								readonly="true" model="@load(vm.cursos)"
								value="Todos (trabalhos mais recentes)" selectedItem="@bind(vm.curso)"
								onChange="@command('changeCurso')">
 								<z:template name="model">
									<z:comboitem label="@load(each.nomeCurso)"/> 	
						
								</z:template>
								
							</z:combobox>
						</z:hlayout>
						<z:hlayout
							style="float:right;padding-right:10px;">
							<z:label value="Pesquisar: "
								style="margin-top:6px;" />
							<z:textbox id="filtro"
								value="@bind(vm.filterString)" />
							<z:separator orient="vertical" spacing="1px" />

							<z:label value="Ano: "
								style="margin-left:10px;margin-top:6px;" />
							<z:combobox id="cmbYear" width="150px"
								readonly="true" model="@load(vm.years)"
								value="@load(filterYear)" selectedItem="@bind(vm.filterYear)">
								<z:template name="model">
									<z:comboitem label="@load(each)" />
								</z:template>
							</z:combobox>
							<z:separator orient="vertical" spacing="1px" />

							<z:button label="Filtrar" width="60px"
								onClick="@command('filtra')" style="margin-left:20px;" />

							<z:image src="/img/help.gif"
								tooltip="filterHelp" style="cursor: help" />

						</z:hlayout>
					</z:div>
				</z:auxheader>
			</z:auxhead>
			<z:columns>
				<z:column hflex="13" label="Trabalho" sort="auto(nomeTCC)" />
				<z:column hflex="4" label="Autor"
					sort="auto(aluno.nomeUsuario)" />
				<z:column hflex="1" label="PDF" />
				<z:column hflex="1" label="Extras" />
			</z:columns>
			<z:template name="lista">
				<z:row xmlns:w="client"
					w:onClick="visualzarTCC(${each.idTCC});">
					<z:div>
						<z:label value="@load(each.nomeTCC)"
							tooltip="${each.idTCC}, position=at_pointer" />
						<z:popup id="${each.idTCC}">
							<vlayout width="550px">
								<z:label value="Resumo"
									style="font-size: 14px; font-weight: bold; text-align: justify; display: block;" />
								<z:label value="@load(each.resumoTCC)" />
								<z:hlayout>
									<z:label value="Ano: "
										style="font-weight:bold;" />
									<z:label
										onCreate="@command('getEachTccYear', tcc=each, lbl=self)" />
								</z:hlayout>
								<z:label value="Orientador"
									style="font-size: 14px; font-weight: bold; text-align: justify; display: block;" />
								<z:label
									value="@load(each.orientador.nomeUsuario)" />
								<z:label value="Co-Orientador" visible="@load(not empty each.coOrientador)"
									style="font-size: 14px; font-weight: bold; text-align: justify; display: block;" />
								<z:label
									value="@load(each.coOrientador.nomeUsuario)" />
								<z:label value="Palavras-chave"
									visible="@load(not empty each.palavrasChave)"
									style="font-size: 14px; font-weight: bold; text-align: justify; display: block;" />
								<z:label
									value="@load(each.palavrasChave)" />
							</vlayout>
						</z:popup>
					</z:div>
					<z:label value="@load(each.aluno.nomeUsuario)"
						tooltip="${each.idTCC}, position=at_pointer" />
					<z:image src="/img/pdf.png" style="cursor: pointer"
						onClick="@command('downloadPDF', tcc=each)" />
					<z:image style="cursor: pointer"
						src="@load(not empty each.arquivoExtraTCCFinal ? '/img/rar.png' : '/img/norar.png')"
						onClick="@command('downloadExtra', tcc=each)" />
				</z:row>
			</z:template>
			
		</z:grid>
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
    </body>
</html>