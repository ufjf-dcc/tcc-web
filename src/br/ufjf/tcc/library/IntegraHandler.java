package br.ufjf.tcc.library;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.ice.integra3.ws.login.IWsLogin;
import br.ufjf.ice.integra3.ws.login.IntegraSoapServiceException_Exception;
import br.ufjf.ice.integra3.ws.login.Profile;
import br.ufjf.ice.integra3.ws.login.WsLoginResponse;
import br.ufjf.ice.integra3.ws.login.WsUserInfoResponse;
import br.ufjf.ice.integra3.ws.login.WSLogin;

public class IntegraHandler {

	private List<String> profiles;
	private WsUserInfoResponse infos;
	
	public IntegraHandler() {
		profiles = new ArrayList<String>();
	}

	public void doLogin(String login, String password) {
		try {
			IWsLogin integra = new WSLogin().getWsLoginServicePort();
			WsLoginResponse user = integra.login(login, password, ConfHandler.getConf("INTEGRA.APPTOKEN"));

			infos = integra.getUserInformation(user.getToken()); // Pegando informações

			List<Profile> profilesList = (infos.getProfileList()).getProfile(); // Pegando a lista de Profiles

			for (Profile profile : profilesList) { // Varrendo a lista de Profiles e pegando as Matriculas do usuário
				profiles.add(profile.getMatricula());
			}
		}
		catch(IntegraSoapServiceException_Exception integraEx) {
			integraEx.printStackTrace();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<String> getProfiles() {
		return profiles;
	}

	public WsUserInfoResponse getInfos() {
		return infos;
	}

}
