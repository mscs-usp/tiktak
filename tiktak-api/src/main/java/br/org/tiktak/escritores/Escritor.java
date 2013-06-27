package br.org.tiktak.escritores;

import java.io.IOException;

import br.org.tiktak.core.Event;
import br.org.tiktak.core.Eventv2;
import br.org.tiktak.core.GsonFactory;

public abstract class Escritor {
	
	protected String caminho;
	protected Eventv2 eventov2;
	protected String json;
	protected String jsonEventos;
	
	//public abstract void log(final String usuario, final String nomeDoEvento) throws Exception;
	public abstract void envia() throws Exception;
	
	public void log(final String usuario, final String nomeDoEvento, final String caminhoDeEscrita) throws Exception {
		caminho = caminhoDeEscrita;
		
		eventov2 = Eventv2.getInstance();
		Event evento = new Event(usuario, nomeDoEvento);
		eventov2.getEvents().add(evento);
		json = GsonFactory.getGson().toJson(eventov2) + "\n";
		jsonEventos = GsonFactory.getGson().toJson(evento);
		
		envia();
	}
		
}
