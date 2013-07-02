package br.org.tiktak.dashboard.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.org.tiktak.core.Event;
import br.org.tiktak.core.Eventv2;
import br.org.tiktak.core.GsonFactory;

import com.google.gson.reflect.TypeToken;

public class ProcessamentoArquivo {
	private Reader reader;


	public ProcessamentoArquivo(String arquivo) throws FileNotFoundException {
		this.reader = new FileReader(new File(arquivo));
	}
	
	public ProcessamentoArquivo() throws FileNotFoundException{
		this("dashboard.bd");
	}
	
	public ProcessamentoArquivo(Reader reader){
		this.reader = reader;
	}
	
	public List<String> getNomesSistemas() {
		List<String> listaNomesSistemas = new ArrayList<String>();
		List<Eventv2> listaDeEventv2 = GsonFactory.getGson().fromJson(
				reader, new TypeToken<List<Eventv2>>() {
				}.getType());
		
		for (Eventv2 eventv2 : listaDeEventv2)
			if (!listaNomesSistemas.contains(eventv2.getSystem()))
				listaNomesSistemas.add(eventv2.getSystem());
		
		return listaNomesSistemas;
	}

	public Set<String> getNomesUsuarios() {
		Set<String> nomesUsuarios = new HashSet<String>();
		List<Eventv2> listaDeEventv2 = GsonFactory.getGson().fromJson(
				reader, new TypeToken<List<Eventv2>>() {
				}.getType());
		for (Eventv2 eventv2 : listaDeEventv2)
			for (Event event : eventv2.getEvents())
				nomesUsuarios.add(event.getUser());
		return nomesUsuarios;
	}
}
