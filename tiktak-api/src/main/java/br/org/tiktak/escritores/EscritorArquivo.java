package br.org.tiktak.escritores;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import br.org.tiktak.core.Event;
import br.org.tiktak.core.Eventv2;
import br.org.tiktak.core.GsonFactory;

public class EscritorArquivo extends Escritor {

	private File arquivo;
	private String caminhoDoArquivo;
	private Eventv2 eventov2;
	
	@Override
	public void escreve(final String usuario, final String nomeDoEvento, final String caminhoDeEscrita) {
		// TODO Auto-generated method stub
		caminhoDoArquivo = caminhoDeEscrita;
		String json, jsonEventos;
		try {
		criarArquivoLog();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			eventov2 = Eventv2.getInstance();
			Event evento = new Event(usuario, nomeDoEvento);
			eventov2.getEvents().add(evento);
			json = GsonFactory.getGson().toJson(eventov2) + "\n";
			jsonEventos = GsonFactory.getGson().toJson(evento);
		}
		concatenarJson(json, jsonEventos);
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

}
