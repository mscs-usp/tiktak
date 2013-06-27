package br.org.tiktak.dashboard.pages.grafico;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;

import bancosys.tec.exception.MessageCreator;
import br.org.tiktak.dashboard.core.ProcessamentoArquivo;
import jmine.tec.web.wicket.pages.Template;

public class InterfaceUsuario extends Template {

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		List<TabDeUsuario> tabs = new ArrayList<TabDeUsuario>();
		try {
			ProcessamentoArquivo processador = new ProcessamentoArquivo();
			Set<String> nomesUsuario = processador.getNomesUsuarios();

			for (String nomeDoUsuario : nomesUsuario)
				tabs.add(new TabDeUsuario(nomeDoUsuario));

			
		} catch (FileNotFoundException e) {

		}
		add(new TabbedPanel("tabs", tabs));
	}
	
	@Override
	protected MessageCreator getHelpTextCreator() {
		return null;
	}

}
