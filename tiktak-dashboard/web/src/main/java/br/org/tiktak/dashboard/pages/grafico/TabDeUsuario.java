package br.org.tiktak.dashboard.pages.grafico;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class TabDeUsuario extends AbstractTab {
	private final String usuario;

	public TabDeUsuario(final String usuario) {
		super(new Model<String>(usuario));
		this.usuario = usuario;
	}

	@Override
	public Panel getPanel(final String panelId) {
		return new PainelDeUsuario(panelId, usuario);
	}
}
