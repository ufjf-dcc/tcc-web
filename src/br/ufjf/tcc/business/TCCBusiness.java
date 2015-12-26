package br.ufjf.tcc.business;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.library.ConfHandler;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.TCCDAO;

public class TCCBusiness {
	
	private List<String> errors;
	private TCCDAO tccDao;

	public TCCBusiness() {
		this.tccDao = new TCCDAO();
		this.errors = new ArrayList<String>();
	}

	public List<String> getErrors() {
		return errors;
	}

	public boolean getMissing(TCC tcc, boolean checkFile){
		errors.clear();
		
		validateOrientador(tcc.getOrientador());
		validateName(tcc.getNomeTCC());
		validateResumo(tcc.getResumoTCC());
		validateData(tcc.getDataApresentacao(),tcc);
		validateSala(tcc.getSalaDefesa(),tcc);
		validateBanca(tcc.getParticipacoes(),tcc);
		validateSuplente(tcc.getParticipacoes(),tcc);
		validatePalavraChave(tcc.getPalavrasChave());
		if(checkFile)
			validateArquivoBanca(tcc.getArquivoTCCBanca());
		
		return errors.size() == 0 ? false : true;
	}
	
	public boolean getMissing(TCC tcc){
		return getMissing(tcc, false);
	}
	
	public boolean validate(TCC tcc) {
		errors.clear();

		validateName(tcc.getNomeTCC());
		if(errors.size() < 1)
			tcc.setNomeTCC(tcc.getNomeTCC().toUpperCase());
		validateOrientador(tcc.getOrientador());

		return errors.size() == 0 ? true : false;
	}

	public void validateName(String nomeTCC) {
		if (nomeTCC == null || nomeTCC.trim().length() == 0)
			errors.add("É necessário informar o nome do seu Trabalho\n");		
	}

	public void validateOrientador(Usuario orientador) {
		if (orientador == null)
			errors.add("É necessário informar o orientador\n");
	}
	
	public void validateResumo(String resumo) {
		if (resumo == null || resumo.trim().length() == 0)
			errors.add("É necessário informar o resumo do TCC\n");		
	}

	public void validateData(Timestamp timestamp, TCC tcc) {
		if(tcc!=null)
		if (timestamp == null && !tcc.isProjeto())
			errors.add("É necessário informar a data de apresentação\n");		
	}
	
	public void validateSala(String sala, TCC tcc) {
		if(tcc!=null)
		if ((sala  == null || sala.trim().length() == 0) && !tcc.isProjeto())
			errors.add("É necessário informar a sala de apresentação\n");		
	}
	
	public void validateBanca(List<Participacao> list, TCC tcc) {
		if(tcc!=null)
		if ((list  == null || list.size() == 0) && !tcc.isProjeto())
			errors.add("É necessário informar a banca\n");		
	}
	
	public void validateSuplente(List<Participacao> list, TCC tcc) {
		if(tcc!=null)
		if ((list  == null || list.size() == 0 || !possuiSuplente(list)) && !tcc.isProjeto())
			errors.add("É necessário informar o suplente da banca.\n");		
	}
	
	public void validateArquivoBanca(String arquivo) {
		if (arquivo == null || arquivo.trim().length() == 0)
			errors.add("É necessário fazer o upload do seu trabalho\n");		
	}
	
	public void validatePalavraChave(String palavraschave) {
		if ((palavraschave  == null || palavraschave.trim().length() == 0))
			errors.add("É necessário informar a as palavras chave\n");		
	}
	
	public List<TCC> getAll() {
		return tccDao.getAll();
	}

	public boolean save(TCC tcc) {
		return tccDao.salvar(tcc);
	}
	
	public boolean saveList(List<TCC> tccs){
		return tccDao.salvarLista(tccs);
	}

	public boolean edit(TCC tcc) {
		return tccDao.editar(tcc);
	}
	
	public boolean saveOrEdit(TCC tcc){
		return tccDao.salvaOuEdita(tcc);
	}
	
	public boolean userHasTCC(Usuario user){
		return tccDao.userHasTCC(user);
	}

	public List<TCC> getTCCsByCurso(Curso curso) {
		return tccDao.getTCCsByCurso(curso);
	}
	
	public TCC getCurrentTCCByAuthor(Usuario user, CalendarioSemestre currentCalendar) {
		if(currentCalendar != null)
			return tccDao.getCurrentTCCByAuthor(user, currentCalendar);
		else
			return null;
	}
	
	public List<TCC> getTCCByCursoAndCalendar(Curso curso, CalendarioSemestre currentCalendar) {
		if(currentCalendar != null)
			return tccDao.getTCCByCursoAndCalendar(curso, currentCalendar);
		else
			return null;
	}
	
	public TCC getTCCById(int id) {
		return tccDao.getTCCById(id);
	}
	
	public List<TCC> getTCCsByOrientador(Usuario user) {
		return tccDao.getTCCsByOrientador(user);
	}
	
	public List<TCC> getTCCsByUserParticipacao(Usuario user) {
		return tccDao.getTCCsByUserParticipacao(user);
	}

	public List<TCC> getFinishedTCCsByCurso(Curso curso) {
		return tccDao.getFinishedTCCsByCurso(curso);
	}
	
	public List<TCC> getFinishedTCCsByCurso(Curso curso,int firstResult, int maxResult) {
		return tccDao.getFinishedTCCsByCurso(curso, firstResult, maxResult);
	}
	
	public List<TCC> getAllFinishedTCCs() {
		return tccDao.getAllFinishedTCCs();
	}
	
	public List<TCC> getAllFinishedTCCs(int firstResult, int maxResult) {
		return tccDao.getAllFinishedTCCs(firstResult, maxResult);
	}
	
	public Integer getQuantidadeTCCs(){
		return tccDao.getQuantidadeTCCs();
	}

	public List<TCC> getNewest(int quantidade) {
	    return tccDao.getNewest(quantidade);
	}
	
	public List<TCC> getNotFinishedTCCsByCursoAndCalendar(Curso curso, CalendarioSemestre currentCalendar) {
		if(currentCalendar != null)
			return tccDao.getNotFinishedTCCsByCursoAndCalendar(curso, currentCalendar);
		else
			return null;
	}
	
	public List<TCC> getNotFinishedTCCsAndProjectsByCursoAndCalendar(Curso curso, CalendarioSemestre currentCalendar) {
		if(currentCalendar != null)
			return tccDao.getNotFinishedTCCsAndProjectsByCursoAndCalendar(curso, currentCalendar);
		else
			return null;
	}
	
	public List<TCC> getProjetosByCursoAndCalendar(Curso curso, CalendarioSemestre currentCalendar) {
		if(currentCalendar != null)
			return tccDao.getProjetosByCursoAndCalendar(curso, currentCalendar);
		else
			return null;
	}
	
	public TCC getCurrentNotFinishedTCCByAuthor(Usuario user,CalendarioSemestre currentCalendar) {
		if(currentCalendar != null)
			return tccDao.getCurrentNotFinishedTCCByAuthor(user, currentCalendar);
		else
			return null;
	}
	
	public List<TCC> getNotFinishedTCCsByCurso(Curso curso) {
			return tccDao.getNotFinishedTCCsByCurso(curso);
	}
	
	public List<TCC> getNotFinishedTCCsAndProjectsByCurso(Curso curso) {
		
			return tccDao.getNotFinishedTCCsAndProjectsByCurso(curso);
	}
	
	public List<TCC> getProjetosByCurso(Curso curso) {
			return tccDao.getProjetosByCurso(curso);
	}
	
	public List<TCC> getAllProjetosByCurso(Curso curso) {
		return tccDao.getAllProjetosByCurso(curso);
	}
	
	public List<TCC> getAllProjetosByCursoAndCalendar(Curso curso,CalendarioSemestre currentCalendar){
		return tccDao.getProjetosByCursoAndCalendar(curso, currentCalendar);
	}
	
	public List<TCC> getAllTrabalhosByCurso(Curso curso) {
		return tccDao.getAllTrabalhosByCurso(curso);
	}
	
	public List<TCC> getTrabalhosByCursoAndCalendar(Curso curso,CalendarioSemestre currentCalendar) {
		return tccDao.getTrabalhosByCursoAndCalendar(curso,currentCalendar);
	}
	
	public List<TCC> getAllTrabalhosAndProjetosByCurso(Curso curso){
		return tccDao.getAllTrabalhosAndProjetosByCurso(curso);
	}
	
	public List<TCC> getAllTrabalhosBancaMarcada(Curso curso,CalendarioSemestre currentCalendar){
		return tccDao.getAllTrabalhosBancaMarcada(curso,currentCalendar);
	}
	
	public List<TCC> getTrabalhosAndProjetosByCursoAndCalendar(Curso curso, CalendarioSemestre currentCalendar){
		return tccDao.getTrabalhosAndProjetosByCursoAndCalendar(curso, currentCalendar);
	}
	
	public boolean possuiSuplente(List<Participacao> participacoes){
		for(Participacao p:participacoes ){
			if(p.getSuplente()==1)
				return true;
		}
		
		return false;
	}

	public boolean isProjetoAguardandoAprovacao(TCC tcc)
	{
		if(tcc.isProjeto() && !(tcc.getPalavrasChave()== null || tcc.getPalavrasChave().trim().length() == 0) && tcc.getArquivoTCCBanca()!=null
				&& !(tcc.getResumoTCC()==null || tcc.getResumoTCC().trim().length() == 0) && tcc.getOrientador()!=null && tcc.getNomeTCC()!=null)
			return true;
		return false;
	}
	
	public boolean isProjetoIncompleto(TCC tcc)
	{
		if(tcc.isProjeto() && !isProjetoAguardandoAprovacao(tcc))
			return true;
		return false;
	}
	
	public boolean isTrabalhoAguardandoAprovacao(TCC tcc)
	{
		ParticipacaoBusiness pb = new ParticipacaoBusiness();
				
		if(!tcc.isProjeto() && !(tcc.getPalavrasChave()== null || tcc.getPalavrasChave().trim().length() == 0) && tcc.getArquivoTCCBanca()!=null
				&& !(tcc.getResumoTCC()==null || tcc.getResumoTCC().trim().length() == 0) && tcc.getOrientador()!=null && tcc.getNomeTCC()!=null
				&& !(tcc.getSalaDefesa()== null || tcc.getSalaDefesa().trim().length() == 0) && tcc.getDataApresentacao()!=null 
				&& pb.getParticipacoesByTCC(tcc).size()>0
				&& possuiSuplente(pb.getParticipacoesByTCC(tcc)))
			return true;
		return false;
	}
	
	public boolean isTrabalhoIncompleto(TCC tcc)
	{
		if(!tcc.isProjeto() && !isTrabalhoAguardandoAprovacao(tcc) && tcc.getArquivoTCCFinal()==null)
			return true;
		return false;
	}
	
	public List<TCC> filtraProjetosIncompletos(List<TCC> projetos)
	{
		for(int i=0;i<projetos.size();i++)
			if(!isProjetoIncompleto(projetos.get(i)))
			{
				projetos.remove(i);
				i--;
			}
		return projetos;
	}
	public List<TCC> filtraProjetosAguardandoAprovacao(List<TCC> projetos)
	{
		for(int i=0;i<projetos.size();i++)
			if(!isProjetoAguardandoAprovacao(projetos.get(i)))
			{
				projetos.remove(i);
				i--;
			}
		return projetos;
	}
	public List<TCC> filtraTrabalhosIncompletos(List<TCC> trabalhos)
	{
		for(int i=0;i<trabalhos.size();i++)
			if(!isTrabalhoIncompleto(trabalhos.get(i)))
			{
				trabalhos.remove(i);
				i--;
			}
		return trabalhos;
	}
	public List<TCC> filtraTrabalhosAguardandoAprovacao(List<TCC> trabalhos)
	{
		for(int i=0;i<trabalhos.size();i++)
			if(!isTrabalhoAguardandoAprovacao(trabalhos.get(i)))
			{
				trabalhos.remove(i);
				i--;
			}
		return trabalhos;
	}
	
	public List<TCC> filtraTrabalhosFinalizados(List<TCC> trabalhos)
	{
		for(int i=0;i<trabalhos.size();i++)
			if(getStatusTCC(trabalhos.get(i))!="Aprovado" )
			{
				trabalhos.remove(i);
				i--;
			}
		return trabalhos;
	}
	
	public String getStatusTCC(TCC tcc)
	{
		if(isProjetoAguardandoAprovacao(tcc))
			return "PAA";
		else
		if(isProjetoIncompleto(tcc))
			return "PI";
		else
		if(isTrabalhoAguardandoAprovacao(tcc))
			return "TAA";
		else
		if(isTrabalhoIncompleto(tcc))
			return "TI";
		else
			return "Aprovado";
	}
	
	public boolean excluitTCC(TCC tcc)
	{
		ParticipacaoBusiness PB = new ParticipacaoBusiness();
		PB.excluiLista(PB.getParticipacoesByTCC(tcc));

		File f;
		if(tcc.getArquivoTCCFinal()!=null)
		{
			f = new File(ConfHandler.getConf("FILE.PATH")+tcc.getArquivoTCCFinal());
			if(f!=null)
				f.delete();
		}
		if(tcc.getArquivoExtraTCCFinal()!=null)
		{
			f = new File(ConfHandler.getConf("FILE.PATH")+tcc.getArquivoExtraTCCFinal());
			if(f!=null)
				f.delete();
		}
		if(tcc.getArquivoExtraTCCBanca()!=null)
		{
			f = new File(ConfHandler.getConf("FILE.PATH")+tcc.getArquivoExtraTCCBanca());
			if(f!=null)
				f.delete();
		}
		if(tcc.getArquivoTCCBanca()!=null)
		{
			f = new File(ConfHandler.getConf("FILE.PATH")+tcc.getArquivoTCCBanca());
			if(f!=null)
				f.delete();
		}
		if(tcc.getArqExtraProjFinal()!=null)
		{
			f = new File(ConfHandler.getConf("FILE.PATH")+tcc.getArqExtraProjFinal());
			if(f!=null)
				f.delete();
		}
		if(tcc.getArqProjFinal()!=null)
		{
			f = new File(ConfHandler.getConf("FILE.PATH")+tcc.getArqProjFinal());
			if(f!=null)
				f.delete();
		}
	
		if((new TCCDAO()).exclui(tcc))
			return true;
		return false;
		
	}
}
