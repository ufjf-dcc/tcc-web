package br.ufjf.tcc.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.EncryptionUtil;
import br.ufjf.tcc.model.Usuario;

public class ConfirmacaoEmailController extends CommonsController {
	private String message;
	private UsuarioBusiness usuarioBusiness = new UsuarioBusiness();

	@Init
	public void init() {
		String decodedData = EncryptionUtil.decode(Executions.getCurrent()
				.getParameter("data"));
		String data[] = decodedData.split(";");
		DateTimeFormatter parser = ISODateTimeFormat.dateTime();
		DateTime dt = parser.parseDateTime(data[2]);

		if (dt.plusHours(24).isAfter(DateTime.now())) {
			Usuario user = usuarioBusiness.getByMatricula(data[0]);
			user.setEmail(data[1]);
			usuarioBusiness.editar(user);
			message = "O endereço de e-mail \"" + data[1]
					+ "\" foi confirmado com sucesso.";
		} else {
			message = "O prazo para a confirmação de "
					+ "e-mails terminou. Entre no sistema "
					+ "com o e-mail anterior e tente novamente.";
		}
	}

	public String getMessage() {
		return message;
	}

}
