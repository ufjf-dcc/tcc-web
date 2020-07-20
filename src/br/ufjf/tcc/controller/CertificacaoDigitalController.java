package br.ufjf.tcc.controller;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.TCC;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

public class CertificacaoDigitalController extends CommonsController {
	private TCCBusiness tccBusiness = new TCCBusiness();
	private String certificadoDigital;
	private TCC tcc;
	private boolean naoEncontrou = false; 
	
	@Init
	public void init() {
	}
	
	@Command
	@NotifyChange({"tcc", "naoEncontrou"})
	public void validarCertificado() {
		tcc = tccBusiness.getTCCByCertificadoDigital(certificadoDigital);
			// BE929-4B752-83561-D3A87-8000E
		if(tcc == null)
			naoEncontrou = true;
		else
			naoEncontrou = false;
	}
	
	public String getCertificadoDigital() {
		return certificadoDigital;
	}
	
	public void setCertificadoDigital(String certificadoDigital) {
		this.certificadoDigital = certificadoDigital;
	}
	
	public TCC getTcc() {
		return tcc;
	}
	
	public void setTcc(TCC tcc) {
		this.tcc = tcc;
	}
	
	public boolean isNaoEncontrou() {		
		return naoEncontrou;
	}
}
