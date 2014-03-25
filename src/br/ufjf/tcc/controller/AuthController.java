package br.ufjf.tcc.controller;

import java.util.Map;

import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

public class AuthController extends CommonsController implements Initiator {

	@Override
	public void doInit(Page page, Map<String, Object> args) throws Exception {
		if (this.checkLogin())
			if(!this.checkPermission(page.getDesktop().getRequestPath().toLowerCase()))
				this.redirectHome();
	}

}
