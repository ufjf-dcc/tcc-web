package br.ufjf.tcc.business;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Curso;
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
	
	public void clearErrors(){
		this.errors.clear();
	}

	// validação dos formulários
	public boolean validate(TCC tcc) {
		errors.clear();

		validateName(tcc.getNomeTCC());
		validateOrientador(tcc.getOrientador());
		validateFile(tcc.getArquivoTCCBanca());

		return errors.size() == 0 ? true : false;
	}

	public void validateName(String nomeTCC) {
		if (nomeTCC == null)
			errors.add("Informe o nome do TCC;\n");
		else if (nomeTCC.trim().length() == 0)
			errors.add("Informe o nome do TCC;\n");
	}

	public void validateOrientador(Usuario orientador) {
		if (orientador == null)
			errors.add("Informe o seu orientador;\n");
	}

	public void validateFile(String fileName) {
		if (fileName != null && fileName.trim().length() != 0)
			errors.add("Envie o arquivo do TCC (PDF);\n");
	}

	// Comunicação com o TCCDAO
	public List<TCC> getPublicListByCurso(Curso curso) {
		return tccDao.getPublicListByCurso(curso);
	}

	public List<TCC> getAll() {
		return tccDao.getAll();
	}
	
	public List<TCC> getTCCByUser(Usuario usuario) {
		return tccDao.getTCCByUser(usuario);
	}

	public boolean save(TCC tcc) {
		return tccDao.salvar(tcc);
	}

	public boolean edit(TCC tcc) {
		return tccDao.editar(tcc);
	}

	public String encriptFileName() {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			String currentTime = "" + System.currentTimeMillis();
			m.update(currentTime.getBytes(), 0, currentTime.length());
			return new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

}
