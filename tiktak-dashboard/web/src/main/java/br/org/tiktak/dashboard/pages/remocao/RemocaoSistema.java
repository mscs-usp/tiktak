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
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

import com.google.gson.reflect.TypeToken;

import bancosys.tec.exception.MessageCreator;
import br.org.tiktak.core.Eventv2;
import br.org.tiktak.core.GsonFactory;
import br.org.tiktak.dashboard.core.ProcessamentoArquivo;
import br.org.tiktak.dashboard.pages.tabela.Interface;

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
				setResponsePage(Interface.class);
			}
		}; 
		add(form);
		form.add(listSites); 
	}
	
	//TODO: Refatorar métodos de manipulação de arquivo!
	private void removeSistema(String nomeDoSistema) {		
			FileReader reader;
			try {
				reader = new FileReader("dashboard.bd");
				
				List<Eventv2> listaDeEventv2 = GsonFactory.getGson().fromJson(reader, new TypeToken<List<Eventv2>>() { }.getType());
				
				boolean removeu = false;
				for (int i = 0; !removeu && i < listaDeEventv2.size(); i++){
					Eventv2 eventv2 = listaDeEventv2.get(i);
					boolean ehSistemaParaRemocao = eventv2.getSystem().equals(nomeDoSistema);
					if(ehSistemaParaRemocao){
						listaDeEventv2.remove(i);
						removeu = true;
					}
				}
				
				if(removeu){
					File arquivoAuxiliar = criaArquivoAuxiliar();
					String json = GsonFactory.getGson().toJson(listaDeEventv2);
					try{
						RandomAccessFile raf = new RandomAccessFile(arquivoAuxiliar, "rw");
						raf.write(json.getBytes());
						raf.close();
						
						File dashboard = new File("dashboard.bd");
						arquivoAuxiliar.renameTo(dashboard);
						arquivoAuxiliar.delete();
					} catch (IOException e) {
						e.printStackTrace();
					}			
					
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
