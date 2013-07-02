package br.org.tiktak.dashboard.pages.grafico;

import java.io.FileNotFoundException;
import java.util.List;

import jmine.tec.web.wicket.pages.Template;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import bancosys.tec.exception.MessageCreator;
import br.org.tiktak.dashboard.core.ProcessamentoArquivo;

public class InterfaceUsuario extends Template {
	private String usuarioEscolhido;
	List<String> nomesUsuarios;
	Panel currentPanel;
	@Override
	protected void onInitialize() {
		super.onInitialize();
		try {
			ProcessamentoArquivo processador = new ProcessamentoArquivo();
			nomesUsuarios = processador.getNomesUsuarios();
			usuarioEscolhido = nomesUsuarios.get(0);
		} catch (FileNotFoundException e) {
			error(e);
		}
		
		DropDownChoice<String> dropDown = new DropDownChoice<String>(
				"usuarios", new PropertyModel<String>(this, "usuarioEscolhido"), nomesUsuarios); 
		currentPanel = new PainelDeUsuario("panel", usuarioEscolhido);
		currentPanel.setOutputMarkupId(true);
		add(currentPanel);
		dropDown.add(new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Panel newPanel = new PainelDeUsuario("panel", usuarioEscolhido);
				currentPanel.replaceWith(newPanel);
				currentPanel = newPanel;
				currentPanel.setOutputMarkupId(true);
				target.add(currentPanel);
				setResponsePage(getPage());
			}
		});
		add(dropDown);
	}
	
	@Override
	protected MessageCreator getHelpTextCreator() {
		return null;
	}
	

}
