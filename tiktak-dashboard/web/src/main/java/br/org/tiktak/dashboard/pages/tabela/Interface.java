package br.org.tiktak.dashboard.pages.tabela;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.text.html.CSS;

import jmine.tec.web.wicket.pages.Template;

import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;

import bancosys.tec.exception.MessageCreator;
import br.org.tiktak.core.Eventv2;
import br.org.tiktak.core.GsonFactory;
import br.org.tiktak.dashboard.core.ProcessamentoArquivo;

import com.google.gson.reflect.TypeToken;

public class Interface extends Template {

	@Override
	protected MessageCreator getHelpTextCreator() {
		return null;
	}

	@SuppressWarnings({ "serial", "unchecked" })
	@Override
	protected void onInitialize() {
		List tabs = new ArrayList();
		super.onInitialize();
		
		try {
			ProcessamentoArquivo processador = new ProcessamentoArquivo();
			
			Set<String> nomesSistemas = processador.getNomesSistemas();
			
			if ( nomesSistemas != null) {
				for (String nomeDoSistema : nomesSistemas)
					tabs.add(new TabDeSistema(nomeDoSistema));
			}
		} catch (FileNotFoundException e) {
			
		}
		
		add(new TabbedPanel("tabs", tabs));
	}

	/*@Override
	public void renderHead(IHeaderResponse arg0) {
		super.renderHead(arg0);

		arg0.renderCSSReference(new PackageResourceReference(CSS.class,
				"painel_de_sistema.css"));
	}*/
}
