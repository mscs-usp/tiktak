package br.org.tiktak.dashboard.pages.grafico;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import jmine.tec.web.wicket.pages.Template;

import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;

import bancosys.tec.exception.MessageCreator;
import br.org.tiktak.dashboard.core.ProcessamentoArquivo;

public class InterfaceSistema extends Template {

	@Override
	protected MessageCreator getHelpTextCreator() {
		return null;
	}

	@SuppressWarnings({ "serial", "unchecked" })
	@Override
	protected void onInitialize() {
		super.onInitialize();

		List<TabDeSistema> tabs = new ArrayList<TabDeSistema>();
		try {
			ProcessamentoArquivo processador = new ProcessamentoArquivo();
			List<String> nomesSistemas = processador.getNomesSistemas();

			for (String nomeDoSistema : nomesSistemas)
				tabs.add(new TabDeSistema(nomeDoSistema));

			
		} catch (FileNotFoundException e) {

		}
		add(new TabbedPanel("tabs", tabs));
	}
}
