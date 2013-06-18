package br.org.tiktak.core;

import java.util.ArrayList;

public class Eventv2 {
	private static Eventv2 instanciaSingleton;
	private String sistema;
	private ArrayList<Event> eventos;

	public String getSystem() {
		return sistema;
	}

	public ArrayList<Event> getEvents() {
		return eventos;
	}
	
    // Método público estático de acesso único ao objeto!
    public static synchronized Eventv2 getInstance(){
    	if(instanciaSingleton == null) {
    		instanciaSingleton = new Eventv2();
    	}
    	return instanciaSingleton;
    }

    // Construtor privado. Suprime o construtor público padrao.
    private Eventv2() {
		this.sistema = null;
		this.eventos = new ArrayList<Event>();
    }
    
    // Assumimos que o sistema do Usuário não terá nome "System" para respeitar a precedência
    public void init(final String system) {
    	String parametroGetProperty = System.getProperty("tiktak.system");
    	String nomeFinal = system;
    	if(system.equals("System") || system.equals("")){
    		if(parametroGetProperty != null && !parametroGetProperty.equals(""))
    			nomeFinal = parametroGetProperty;
    	} 
    	this.sistema = nomeFinal;
    }

	public void log(final String user, final String funcionality) {
		Event event = new Event(user, funcionality);
		this.eventos.add(event);
	}
}