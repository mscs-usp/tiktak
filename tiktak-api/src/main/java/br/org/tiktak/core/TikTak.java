package br.org.tiktak.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Properties;

import br.org.tiktak.escritores.Escritor;
import br.org.tiktak.escritores.EscritorArquivo;
import br.org.tiktak.escritores.EscritorWS;

/**
 * TikTak API - API to collect feedback on the use of source code.
 * 
 * @since 2013-06
 * @version 0.3
 * @author Albert De La Fuente, Claynon Souza, Julian Monteiro, Julien Renaut,
 *         Leonardo Oliveira, Renan Melo, Renato Minami, Roberto Rodrigues,
 *         Wallace Almeida
 * @license LGPL, for more visit: <a href =
 *          "http://www.gnu.org/copyleft/lesser.html"
 *          >http://www.gnu.org/copyleft/lesser.html</a>
 * */
public class TikTak {
	private final String caminhoPadraoDoDiretorio;
	private String caminhoDoDiretorio;
	private String caminhoDoArquivo;
	private final String sistemaAPI;
	private File arquivo;
	private final Eventv2 eventov2;
	private String usuario;
	private String evento;

	private Escritor escritor;
	private String exporter;
	private String wsurl;

	/**
	 * Constructor with parameter
	 * 
	 * @param system
	 *            String with name of the system in use
	 * */
	public TikTak(final String system) {
		caminhoPadraoDoDiretorio = System.getProperty("user.dir") + "/";
		caminhoDoDiretorio = "";
		caminhoDoArquivo = "";
		sistemaAPI = system;
		this.eventov2 = Eventv2.getInstance();
		this.eventov2.init(sistemaAPI);
		obterECriarNoDisco();
	}

	/**
	 * Constructor without parameter
	 * */
	public TikTak() {
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

	private String obterPropriedadeDoArquivoDeProperties(final String propriedade) {
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
		if (exporter != null && exporter.equals("webservice")) {
			wsurl = obterPropriedadeDoArquivoDeProperties("tiktak.ws-url");
		} else {
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

	/**
	 * Log the event and the user
	 * 
	 * @param user
	 *            String with name of the user of the system
	 * @param eventName
	 *            String with name of the event
	 * */
	public void log(final String user, final String eventName) {
		this.evento = eventName;
		String json, jsonEventos;
		try {
			criarArquivoLog();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Event evento = new Event(user, eventName);
			eventov2.getEvents().add(evento);
			json = GsonFactory.getGson().toJson(eventov2) + "\n";
			jsonEventos = GsonFactory.getGson().toJson(evento);
		}
		concatenarJson(json, jsonEventos);
	}

	public void logTeste(final String usuario, final String nomeDoEvento) {

		if (exporter.equals("webservice")) {
			escritor = new EscritorWS();
			try {
				escritor.log(usuario, nomeDoEvento, wsurl);
			} catch (Exception e) {

			}
		} else {
			escritor = new EscritorArquivo();
			try {
				escritor.log(usuario, nomeDoEvento, caminhoDoArquivo);
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

	/**
	 * Set path of directory
	 * 
	 * @param nameOfDirectory
	 *            String with directory name
	 * */
	public void setDir(String nameOfDirectory) {
		if (nameOfDirectory.equals("")) {
			nameOfDirectory = caminhoPadraoDoDiretorio;
		} else if (!nameOfDirectory.endsWith("/")) {
			nameOfDirectory += "/";
		}
		this.caminhoDoDiretorio = nameOfDirectory;
		obterECriarNoDisco();
	}
}