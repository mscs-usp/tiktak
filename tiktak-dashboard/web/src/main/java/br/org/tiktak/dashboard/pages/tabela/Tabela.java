package br.org.tiktak.dashboard.pages.tabela;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jmine.tec.web.wicket.pages.Template;

import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;

import bancosys.tec.exception.MessageCreator;
import br.org.tiktak.core.Eventv2;
import br.org.tiktak.core.GsonFactory;

import com.google.gson.reflect.TypeToken;

public class Tabela extends Template {

	@Override
	protected MessageCreator getHelpTextCreator() {
		return null;
	}

	@SuppressWarnings({ "serial", "unchecked" })
	@Override
	protected void onInitialize() {
		super.onInitialize();

		Set<String> nomesSistemas = getNomesSistemas();
		List tabs = new ArrayList();

		for (String nomeDoSistema : nomesSistemas)
			tabs.add(new TabDeSistema(nomeDoSistema));
		
		add(new TabbedPanel("tabs", tabs));
		
	}

	private Set<String> getNomesSistemas() {
		Set<String> nomesSistemas = new HashSet<String>();
		File file = new File("dashboard.bd");
		if (file.exists()) {
			try {
				List<Eventv2> listaDeEventv2 = GsonFactory.getGson().fromJson(
						new FileReader(file), new TypeToken<List<Eventv2>>() {
						}.getType());
				for (Eventv2 eventv2 : listaDeEventv2)
					nomesSistemas.add(eventv2.getSystem());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return nomesSistemas;
	}
}
