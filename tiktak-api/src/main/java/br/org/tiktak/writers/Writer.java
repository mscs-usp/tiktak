package br.org.tiktak.writers;

import br.org.tiktak.core.Event;
import br.org.tiktak.core.EventSystem;
import br.org.tiktak.core.GsonFactory;

public abstract class Writer {
	
	protected String path;
	protected EventSystem eventSystem;
	protected String json;
	protected String jsonEvents;
	
	//public abstract void log(final String usuario, final String nomeDoEvento) throws Exception;
	public abstract void send() throws Exception;
	
	public void log(final String user, final String eventName, final String pathOfWriting) throws Exception {
		path = pathOfWriting;
		
		eventSystem = EventSystem.getInstance();
		Event evento = new Event(user, eventName);
		eventSystem.getEvents().add(evento);
		json = GsonFactory.getGson().toJson(eventSystem) + "\n";
		jsonEvents = GsonFactory.getGson().toJson(evento);
		
		send();
	}
		
}
