package br.ufjf.tcc.library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

public class IntegraHandler {

	private String error;
	private JSONObject user;

	public IntegraHandler(String cpf) {
		try {
			this.error = null;
			this.user = null;
			URL obj = new URL(
					"http://integra.ice.ufjf.br/integra/DadosPessoa?cpf="
							+ cpf.replace(".", "").replace("-", ""));

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();

			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(response
					.toString());
			if (jsonObject != null) {
				if (((String) jsonObject.get("codeResult")).equals("OK")) {
					this.user = (JSONObject) jsonObject.get("result");
					JSONArray prof = new JSONArray();
					prof.add(this.user.get("profile"));
					prof.add("201335012");
					prof.add("3353417");
					prof.add("1714410");
					this.user.put("profile", prof);
				} else if (((String) jsonObject.get("codeResult")).equals("E_BANCO_DADOS")) {
					this.error = "O Integra não conseguiu se conectar o SIGA. Tente mais tarde.";
				}
			} else
				this.error = "Erro ao tentar se conectar ao Integra. Tente mais tarde.";

		} catch (Exception e) {
			this.error = "Erro ao tentar se conectar ao Integra. Tente mais tarde.";
		}
	}

	public String getError() {
		return error;
	}

	public JSONObject getUser() {
		return user;
	}

}
