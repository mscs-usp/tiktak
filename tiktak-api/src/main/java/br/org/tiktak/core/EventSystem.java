package br.org.tiktak.core;

import java.util.ArrayList;

public class EventSystem {
	private static EventSystem instanciaSingleton;
	private String systemName;
	private ArrayList<Event> events;

	public String getSystem() {
		return systemName;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}
	
    // Método público estático de acesso único ao objeto!
    public static synchronized EventSystem getInstance(){
    	if (instanciaSingleton == null) {
    		instanciaSingleton = new EventSystem();
    	}
    	return instanciaSingleton;
    }

    // Construtor privado. Suprime o construtor público padrao.
    private EventSystem() {
		this.systemName = null;
		this.events = new ArrayList<Event>();
    }
    
    // Assumimos que o sistema do Usuário não terá nome "System" para respeitar a precedência
    public void init(final String system) {
    	String parameterGetProperty = System.getProperty("tiktak.system");
    	String finalName = system;
    	if(system.equals("System") || system.equals("")){
    		if(parameterGetProperty != null && !parameterGetProperty.equals(""))
    			finalName = parameterGetProperty;
    	} 
    	this.systemName = finalName;
    }

	public void log(final String user, final String funcionality) {
		Event event = new Event(user, funcionality);
		this.events.add(event);
	}
}