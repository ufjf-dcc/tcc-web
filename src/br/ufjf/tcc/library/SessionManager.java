package br.ufjf.tcc.library;
import org.zkoss.zk.ui.Sessions;

public class SessionManager {

	public static Object setAttribute(String name, Object value) {
		return Sessions.getCurrent().setAttribute(name, value);
	}

	public static Object getAttribute(String name) {
		return Sessions.getCurrent().getAttribute(name);
	}

}
