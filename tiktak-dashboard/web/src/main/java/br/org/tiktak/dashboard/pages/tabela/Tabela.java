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
import br.org.tiktak.dashboard.core.ProcessamentoArquivo;

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

		ProcessamentoArquivo processador = new ProcessamentoArquivo();
		Set<String> nomesSistemas = processador.getNomesSistemas();
		List tabs = new ArrayList();

		for (String nomeDoSistema : nomesSistemas)
			tabs.add(new TabDeSistema(nomeDoSistema));
		
		add(new TabbedPanel("tabs", tabs));
		
	}
}
