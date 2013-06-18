package br.org.tiktak.core;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TikTak {
	private String caminhoDoDiretorio;
	private String caminhoDoArquivo;
	private String sistemaAPI;
	private File arquivo;
	private Eventv2 eventov2;
	private String usuario;
	private String evento;
		
	public TikTak (final String sistema) {
		caminhoDoDiretorio = System.getProperty("user.dir");
		caminhoDoArquivo = "";
		sistemaAPI = sistema;
		obterECriarNoDisco();
		this.eventov2 = Eventv2.getInstance();
		this.eventov2.init(sistemaAPI);
	}
	
	public TikTak(){
		this("System");
	}

	public String getCaminhoDoArquivo() {
		return caminhoDoArquivo;
	}

	public void setDir(String nomeDoDiretorio) {
		if (!nomeDoDiretorio.endsWith("/")) {
			nomeDoDiretorio += "/";
		}
		this.caminhoDoDiretorio = nomeDoDiretorio;
		obterECriarNoDisco();
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

	public void log(final String usuario, final String nomeDoEvento) {
		this.usuario = usuario;
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

	private void concatenarJson(final String json) {
		try {
			RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
			raf.readLine();
			char c = raf.readLine().charAt(0);
			boolean estaVazio1 = c == ']';
			if (estaVazio1) {
				raf.seek(2);
				raf.write(json.getBytes());
			} else {
				raf.seek(raf.length() - 2);
				raf.write((",\n" + json).getBytes());
			}
			raf.write("]".getBytes());
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String obterCaminhoDoDiretorio() throws IOException {
		String parametroSetDir, parametroGetProperty;

		parametroSetDir = this.caminhoDoDiretorio;
		parametroGetProperty = System.getProperty("tiktak.dir");
		if (parametroSetDir != null && !parametroSetDir.equals("")) {
			caminhoDoDiretorio = parametroSetDir;
		} else if (parametroGetProperty != null) {
			caminhoDoDiretorio = parametroGetProperty;
		}
		
		if (!caminhoDoDiretorio.endsWith("/")) {
			caminhoDoDiretorio += "/";
		}
		return caminhoDoDiretorio;
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
		
		return caminhoDoArquivo;
	}

	private File criarArquivoLog() throws IOException {
		arquivo = new File(this.caminhoDoArquivo);
		if (!arquivo.exists()) {
			arquivo.createNewFile();
			RandomAccessFile writer = new RandomAccessFile(arquivo, "rw");

			Event evento = new Event(this.usuario, this.evento);
			String json = GsonFactory.getGson().toJson(eventov2) + "\n";

			writer.write(json.getBytes());
			writer.close();
		}
		return arquivo;
	}

}