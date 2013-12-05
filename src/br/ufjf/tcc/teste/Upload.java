package br.ufjf.tcc.teste;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

public class Upload {

	private static final String SAVE_PATH = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/files/";

	@Command("up")
	public void upload(@BindingParam("evt") UploadEvent evt,
			@BindingParam("lbl") Label lbl) {
		Media media = evt.getMedia();

		File myFile = new File(media.getName());
		Messagebox.show("O \"caminho\" do arquivo enviado é: "
				+ myFile.getAbsolutePath());

		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			InputStream fin = media.getStreamData();
			in = new BufferedInputStream(fin);

			File baseDir = new File(SAVE_PATH);

			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			File file = new File(SAVE_PATH + media.getName());

			OutputStream fout = new FileOutputStream(file);
			out = new BufferedOutputStream(fout);
			byte buffer[] = new byte[1024];
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}
			lbl.setValue("Sucesso no upload de \"" + media.getName() + "\"");
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null)
					out.close();

				if (in != null)
					in.close();

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		//testa se o arquivo foi realmente salvo no servidor fazendo o download:		
		InputStream is = Sessions.getCurrent().getWebApp()
				.getResourceAsStream("files/" + media.getName());
		Filedownload.save(is, "application/pdf", media.getName());

	}
}
