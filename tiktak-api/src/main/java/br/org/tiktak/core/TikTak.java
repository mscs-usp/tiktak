package br.org.tiktak.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Properties;

import br.org.tiktak.escritores.Escritor;
import br.org.tiktak.escritores.EscritorArquivo;
import br.org.tiktak.escritores.EscritorWS;

public class TikTak {
	private String caminhoPadraoDoDiretorio;
	private String caminhoDoDiretorio;
	private String caminhoDoArquivo;
	private String sistemaAPI;
	private File arquivo;
	private Eventv2 eventov2;
	private String usuario;
	private String evento;
		
	private Escritor escritor;
	private String exporter;
	private String wsurl;
	
	public TikTak (final String sistema) {
		caminhoPadraoDoDiretorio = System.getProperty("user.dir") + "/";
		caminhoDoDiretorio = "";
		caminhoDoArquivo = "";
		sistemaAPI = sistema;
		this.eventov2 = Eventv2.getInstance();
		this.eventov2.init(sistemaAPI);
		obterECriarNoDisco();
	}
	
	public TikTak(){
		this("System");
	}

	public void obterECriarNoDisco() {
		try {
			obterCaminhoDoDiretorio();
			criarDiretorioLog();
			obterCaminhoDoArquivo();
			criarArquivoLog();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String obterCaminhoDoDiretorio() throws IOException {
		String parametroSetDir, parametroGetProperty, parametroFileProperties;
		
		parametroFileProperties = obterPropriedadeDoArquivoDeProperties("tiktak.dir");
		parametroSetDir = this.caminhoDoDiretorio;
		parametroGetProperty = System.getProperty("tiktak.dir");
		
		if (parametroSetDir != null && !parametroSetDir.equals("")) {
			caminhoDoDiretorio = parametroSetDir;
		} else if (parametroFileProperties != null && !parametroFileProperties.equals("")) {
			caminhoDoDiretorio = parametroFileProperties;
		} else if (parametroGetProperty != null) {
			caminhoDoDiretorio = parametroGetProperty;
		} else {
			caminhoDoDiretorio = caminhoPadraoDoDiretorio;
		}		
		if (!caminhoDoDiretorio.endsWith("/")) {
			caminhoDoDiretorio += "/";
		}
		return caminhoDoDiretorio;
	}

	private String obterPropriedadeDoArquivoDeProperties(String propriedade) {
		String property;
		File arquivo = new File("tiktak.properties");
		FileInputStream fis;
		try {
			fis = new FileInputStream(arquivo);
			Properties properties = new Properties();
			properties.load(fis);
			property = properties.getProperty(propriedade);
		} catch (Exception e) {
			property = null;
		}
		
		return property;
	}
	
	private String criarDiretorioLog() throws IOException {
		String diretorio = caminhoDoDiretorio;
		if (diretorio != "") {
			File diretorioFisico = new File(diretorio);			
			if (!diretorioFisico.exists()) {
				diretorioFisico.mkdir();
			}
		}
		return diretorio;
	}
	
	private String obterCaminhoDoArquivo() throws IOException {
		String parametroSetArquivo;
		parametroSetArquivo = "tik.tak";		
		caminhoDoArquivo = this.caminhoDoDiretorio + parametroSetArquivo;	
		exporter = obterPropriedadeDoArquivoDeProperties("tiktak.exporter");
		if(exporter != null && exporter.equals("webservice")){
			wsurl = obterPropriedadeDoArquivoDeProperties("tiktak.ws-url");
		}
		else{
			exporter = "file";
		} 
		return caminhoDoArquivo;
	}
	
	private File criarArquivoLog() throws IOException {
		arquivo = new File(this.caminhoDoArquivo);
		if (!arquivo.exists()) {
			arquivo.createNewFile();
			RandomAccessFile writer = new RandomAccessFile(arquivo, "rw");
			String json = GsonFactory.getGson().toJson(eventov2) + "\n";
			writer.write(json.getBytes());
			writer.close();
		}
		return arquivo;
	}

	public void log(final String usuario, final String nomeDoEvento) { 
		this.evento = nomeDoEvento;
		String json, jsonEventos;
		try {
		criarArquivoLog();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Event evento = new Event(usuario, nomeDoEvento);
			eventov2.getEvents().add(evento);
			json = GsonFactory.getGson().toJson(eventov2) + "\n";
			jsonEventos = GsonFactory.getGson().toJson(evento);
		}
		concatenarJson(json, jsonEventos);
	}
	
	public void logTeste(final String usuario, final String nomeDoEvento){
		
		if(exporter.equals("webservice")){
			escritor = new EscritorWS();
			try {
				escritor.escreve(usuario, nomeDoEvento, wsurl);
			} catch (Exception e) {
				
			}
		}
		else{
			escritor = new EscritorArquivo();
			try {
				escritor.escreve(usuario, nomeDoEvento, caminhoDoArquivo);
			} catch (Exception e) {
				
			}
		}
	}

	private void concatenarJson(final String json, final String jsonEventos) {
		try {
			RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
			if (naoContemEventos()) {
				raf.write(json.getBytes());
			} else {
				raf.seek(raf.length() - 3);
				raf.write((",\n" + jsonEventos).getBytes());
				raf.write("]}\n".getBytes());
			}
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Boolean naoContemEventos() {
		boolean estaVaziov2 = false;
		int count = 0;
		try {
			RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
			String linha = raf.readLine();
			count = linha.split("\\{", -1).length - 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ((!estaVaziov2) && (count == 1));
	}

	public String getCaminhoDoArquivo() {
		return caminhoDoArquivo;
	}
	
	public String getCaminhoDoDiretorio() {
		return caminhoDoDiretorio;
	}

	public void setDir(String nomeDoDiretorio) {
		if(nomeDoDiretorio.equals("")){
			nomeDoDiretorio = caminhoPadraoDoDiretorio;
		}
		else if (!nomeDoDiretorio.endsWith("/")) {
			nomeDoDiretorio += "/";
		}
		this.caminhoDoDiretorio = nomeDoDiretorio;
		obterECriarNoDisco();
	}
}