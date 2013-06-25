package br.org.tiktak.dashboard.pages.remocao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jmine.tec.web.wicket.pages.Template;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

import com.google.gson.reflect.TypeToken;

import bancosys.tec.exception.MessageCreator;
import br.org.tiktak.core.Eventv2;
import br.org.tiktak.core.GsonFactory;
import br.org.tiktak.dashboard.core.ProcessamentoArquivo;

public class RemocaoSistema extends Template {

	private List<String> listaDeSistemas;
	private String sistemaEscolhido;
	private FileReader reader;
 
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
			"sistemas", new PropertyModel<String>(this, "sistemaEscolhido"), listaDeSistemas); 
		Form<Void> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() { 
				removeSistema(sistemaEscolhido);
				info("O sitema " + sistemaEscolhido + " foi excluido."); 
			}
		}; 
		add(form);
		form.add(listSites); 
	}
	
	public void removeSistema(String sistemaEscolhido){
		String linha = "";
		boolean excluiuUltimo = false;
		try {				
			FileReader fileReader = new FileReader("dashboard.bd");
			BufferedReader leitor = new BufferedReader(fileReader);  
			File arquivoAuxiliar = criaArquivoAuxiliar();
			FileWriter fw = new FileWriter(arquivoAuxiliar , true );
			BufferedWriter bw = new BufferedWriter(fw);			
			while( leitor.ready() ){
				linha = leitor.readLine();
				if(linha.contains("sistema")){
					if (ehSistemaEscolhido(linha, sistemaEscolhido) == true){
						linha = leitor.readLine();
						while(leitor.ready() && !linha.contains("sistema")) {
							linha = leitor.readLine();
						}
						if(!leitor.ready())	excluiuUltimo = true;
					}
				}
				bw.write( linha);
				bw.newLine();					
			}
			bw.close();
			fw.close();
			if( excluiuUltimo == true) {
				RandomAccessFile raf = new RandomAccessFile(arquivoAuxiliar, "rw");
				raf.seek(raf.length() - 3);
				raf.write("\n]".getBytes());
			}
			File dashboard = new File("dashboard.bd");
			arquivoAuxiliar.renameTo(dashboard);
			arquivoAuxiliar.delete();
	        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private Boolean ehSistemaEscolhido(String linha, String sistemaEscolhido ) {
		String palavra = "";
		Integer indice = 0;
		while(linha.charAt(indice) != ':') {
			indice++;
		}
		indice++;
		while(linha.charAt(indice) != ',') {
			palavra =  palavra + linha.charAt(indice);
			indice++;;
		}
		return palavra.contains(sistemaEscolhido);
	}

	private File criaArquivoAuxiliar() {
		File arquivoAuxiliar = new File("arquivoAuxiliar.bd");
		try {
			if (!arquivoAuxiliar.exists()) {
				arquivoAuxiliar.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arquivoAuxiliar;
	}
	
	@Override
	protected MessageCreator getHelpTextCreator() {
		return null;
	}
}
