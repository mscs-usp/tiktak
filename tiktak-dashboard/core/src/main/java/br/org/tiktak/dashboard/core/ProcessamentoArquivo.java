package br.org.tiktak.dashboard.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.org.tiktak.core.Eventv2;
import br.org.tiktak.core.GsonFactory;

import com.google.gson.reflect.TypeToken;

public class ProcessamentoArquivo {
	private String arquivo;
	private File file;


	public ProcessamentoArquivo(String arquivo){
		this.arquivo = arquivo; 
		this.file = new File(arquivo);
	}
	
	public ProcessamentoArquivo(){
		this.arquivo = "dashboard.bd";
		this.file = new File(this.arquivo);
	}
	
	public Set<String> getNomesSistemas() {
		Set<String> nomesSistemas = new HashSet<String>();
		if (file.exists()) {
			try {
				List<Eventv2> listaDeEventv2 = GsonFactory.getGson().fromJson(
						new FileReader(file), new TypeToken<List<Eventv2>>() {
						}.getType());
				for (Eventv2 eventv2 : listaDeEventv2)
					nomesSistemas.add(eventv2.getSystem());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return nomesSistemas;
	}
}
