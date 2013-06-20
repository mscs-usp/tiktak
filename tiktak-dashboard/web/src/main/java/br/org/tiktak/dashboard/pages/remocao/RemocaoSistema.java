package br.org.tiktak.dashboard.pages.remocao;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import bancosys.tec.exception.MessageCreator;
import jmine.tec.web.wicket.pages.Template;
import br.org.tiktak.dashboard.core.ProcessamentoArquivo;
import br.org.tiktak.dashboard.pages.tabela.TabDeSistema;

public class RemocaoSistema extends Template {

	private List<String> listaDeSistemas;
	private String sistemaEscolhido;
 
	@Override
	protected void onInitialize() {
		super.onInitialize(); 
		listaDeSistemas = new ArrayList<String>();
		try {
			ProcessamentoArquivo processamentoArquivo = new ProcessamentoArquivo();
			Set<String> setDeSistemas = processamentoArquivo.getNomesSistemas();
			for (String nomeDoSistema : setDeSistemas)
				listaDeSistemas.add(nomeDoSistema);
			sistemaEscolhido = listaDeSistemas.get(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		add(new FeedbackPanel("feedback")); 
		DropDownChoice<String> listSites = new DropDownChoice<String>(
			"sites", new PropertyModel<String>(this, "selected"), listaDeSistemas); 
		Form<Void> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() { 
				info("O sitema " + sistemaEscolhido + " foi excluido."); 
			}
		}; 
		add(form);
		removeSistema(nomeDosistema);
		form.add(listSites); 
	}

	@Override
	protected MessageCreator getHelpTextCreator() {
		return null;
	}

}
