package br.ufjf.tcc.mail;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;


// Email de número 09 do drive
public class EnviadorEmailOrientacoesGeraisOrientador extends EnviadorEmailChain{

	public EnviadorEmailOrientacoesGeraisOrientador() {
		super(null);
	}
	
	@Override
	protected EmailBuilder gerarEmail(TCC tcc, String statusInicial) {
		EmailBuilder emailBuilder = null;
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		
		List<Usuario> coordenadores = usuarioBusiness.getCoordenadoresByCurso(tcc.getAluno().getCurso());
		
		String nomeAluno = tcc.getAluno().getNomeUsuario();
		String nomeOrientador = tcc.getOrientador().getNomeUsuario();
		String nomeCoordenador = coordenadores.get(0).getNomeUsuario();
		String nomeCurso = tcc.getAluno().getCurso().getNomeCurso();
		String titulo = tcc.getNomeTCC();
		
		emailBuilder = new EmailBuilder(true).comTitulo("[TCC-WEB] Orientações Gerais - " + nomeAluno);
		emailBuilder.appendMensagem("ORIENTAÇÕES GERAIS PARA AVALIAÇÃO DOS TRABALHOS DE CONCLUSÃO DE CURSO").breakLine(); 
		emailBuilder.appendMensagem("Na condição de Presidente da Banca de defesa de TCC com o título " + titulo);
		emailBuilder.appendMensagem(" do(a) discente " + nomeAluno + ", o(a) orientador(a) deve observar as seguintes instruções: ").breakLine(); 
		
		emailBuilder.appendMensagem("1. O(a) discente será avaliado em duas modalidades - avaliação da apresentação oral ");
		emailBuilder.appendMensagem("e análise do trabalho escrito - por uma Banca Examinadora composta por ao ");
		emailBuilder.appendMensagem("menos dois membros que não participaram do trabalho (nem orientador(a) e nem co-orientador(a)), ");
		emailBuilder.appendMensagem("que atribuirão, individualmente, nota ao trabalho nos documentos destinados à avaliação;").breakLine();
		
		emailBuilder.appendMensagem("2. No trabalho escrito, cada membro deve avaliar: organização sequencial, argumentação, ");
		emailBuilder.appendMensagem("profundidade do tema, relevância e contribuição acadêmica da pesquisa ou sistema ");
		emailBuilder.appendMensagem("desenvolvido, correção gramatical, clareza, apresentação estética e adequação aos ");
		emailBuilder.appendMensagem("aspectos formais e às normas da ABNT;").breakLine();
		
		emailBuilder.appendMensagem("3. Na apresentação oral, cada membro deve avaliar: domínio do conteúdo, organização da ");
		emailBuilder.appendMensagem("apresentação, habilidades de comunicação e expressão, capacidade de argumentação, uso ");
		emailBuilder.appendMensagem("dos recursos audiovisuais, correção gramatical e apresentação estética do trabalho;").breakLine();
		
		emailBuilder.appendMensagem("4. Recomenda-se que a defesa do TCC siga a seguinte sequência:").breakLine();
		emailBuilder.appendMensagem("a) O(a) discente tem 35 (trinta e cinco minutos) para apresentação oral do trabalho;").breakLine();
		emailBuilder.appendMensagem("b) Logo após, o trabalho é arguido pelos membros da banca examinadora.").breakLine();
		
		emailBuilder.appendMensagem("5. A nota de cada examinador (totalizando valores de 0 (zero) a 100 (cem) ) será a soma ");
		emailBuilder.appendMensagem("da nota do trabalho escrito (com valores de 0 (zero) a 70 (setenta) ) com a nota da ");
		emailBuilder.appendMensagem("apresentação oral (com valor de 0 (zero) a 30 (trinta)).").breakLine();
		
		emailBuilder.appendMensagem("6. A avaliação será documentada em uma Ficha de Avaliação Final e Fichas Individuais de ");
		emailBuilder.appendMensagem("cada membro da banca, onde devem constar as notas que cada examinador ");
		emailBuilder.appendMensagem("atribuiu ao aluno (vide documento em anexo).").breakLine();
		
		emailBuilder.appendMensagem("7. Ao término da defesa, o(a) discente ou o(a) orientador(a) deverá entregar a Ata de Defesa ");
		emailBuilder.appendMensagem("devidamente assinada e as Fichas de Avaliação (final e individuais) à Coordenação do Curso.\n");
		
		emailBuilder.appendMensagem("8. O(a) discente tem o prazo máximo de 7 (sete) dias corridos após a defesa, ");
		emailBuilder.appendMensagem("desde que não ultrapasse o último dia letivo do semestre, para submeter a ");
		emailBuilder.appendMensagem("versão final do Trabalho de Conclusão de Curso no Sistema de Monografias, ");
		emailBuilder.appendMensagem("com as correções sugeridas pela banca.").breakLine();
		
		emailBuilder.appendMensagem("9. O(a) orientador(a) tem o prazo máximo de 2 (dois) dias após a ");
		emailBuilder.appendMensagem("submissão da versão final do TCC, desde que não ultrapasse o último ");
		emailBuilder.appendMensagem("dia letivo do semestre, para verificar se a versão final contém as correções ");
		emailBuilder.appendMensagem("sugeridas pela Banca Examinadora. Se estiver tudo correto o(a) orientador(a) ");
		emailBuilder.appendMensagem("deve Aprovar essa versão final no Sistema de Monografias e lançar ");
		emailBuilder.appendMensagem("o resultado: Aprovado no SIGA. ").breakLine();
		
		emailBuilder.appendMensagem("10. Caso o(a) orientador(a) não concorde com a versão final ");
		emailBuilder.appendMensagem("submetida pelo(a) discente, o(a) mesmo(a) deve Reprovar a ");
		emailBuilder.appendMensagem("Versão Final informando o(s) motivo(s). O(a) discente tem o prazo ");
		emailBuilder.appendMensagem("máximo de 2 (dois) dias, desde que não ultrapasse o último dia letivo ");
		emailBuilder.appendMensagem("do semestre, para corrigir o TCC e submeter a versão corrigida no ");
		emailBuilder.appendMensagem("sistema para ser avaliada.").breakLine();
		
		emailBuilder.appendMensagem("11. Uma vez Aprovada a versão final do TCC pelo(a) orientador(a) ");
		emailBuilder.appendMensagem("e entregue toda a documentação de Defesa do TCC, a Coordenação do ");
		emailBuilder.appendMensagem("Curso deve avaliar se a formatação do TCC no sistema atende aos ");
		emailBuilder.appendMensagem("padrões de monografia estabelecidos no PPC do curso. Se estiver ");
		emailBuilder.appendMensagem("tudo correto, a Coordenação Aprova o TCC e o torna público para ");
		emailBuilder.appendMensagem("leitura no Sistema de Monografias. Caso contrário, a Coordenação ");
		emailBuilder.appendMensagem("Reprova o TCC informando o(s) motivo(s) e o(a) discente terá o prazo ");
		emailBuilder.appendMensagem("máximo de 2 (dois) dias, desde que não ultrapasse o último dia letivo ");
		emailBuilder.appendMensagem("do semestre, para corrigir a formatação do TCC e submeter a versão ");
		emailBuilder.appendMensagem("corrigida no sistema para ser avaliada.").breakLine(); 
		
		emailBuilder.appendMensagem("12. A identificação de qualquer tipo de plágio ou a não adoção ");
		emailBuilder.appendMensagem("do padrão de monografia disponibilizado no PPC do curso resulta ");
		emailBuilder.appendMensagem("em Reprovação do Trabalho de Conclusão do Curso com nota 0 (zero).").breakLine();
		
		emailBuilder.appendMensagem("Att.,").breakLine();
		emailBuilder.appendMensagem(nomeCoordenador).breakLine();
		emailBuilder.appendMensagem("Coordenador(a) do Curso de " + nomeCurso).breakLine();
		emailBuilder.appendLinkSistema();
		
		
		List<Usuario> aluno = new ArrayList<>();
		UsuarioBusiness ub = new UsuarioBusiness();
		Usuario u = ub.getByMatricula("1010");
		System.out.println(u.getEmail());
		aluno.add(u);
		inserirDestinatarios(aluno, emailBuilder);
	
		return emailBuilder;
		
	}
	
}
