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

public class RemocaoSistema extends Template {

	private static final List<String> SEARCH_ENGINES = Arrays.asList(new String[] {
			"Google", "Bing", "Baidu" });
 
	//make Google selected by default
	private String selected = "Google";
 
	@Override
	protected void onInitialize() {
		super.onInitialize();
 
		add(new FeedbackPanel("feedback"));
 
		DropDownChoice<String> listSites = new DropDownChoice<String>(
			"sites", new PropertyModel<String>(this, "selected"), SEARCH_ENGINES);
 
		Form<?> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
 
				info("Selected search engine : " + selected);
 
			}
		};
 
		add(form);
		form.add(listSites);
 
	}

	@Override
	protected MessageCreator getHelpTextCreator() {
		// TODO Auto-generated method stub
		return null;
	}

}
