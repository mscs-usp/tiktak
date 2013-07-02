package br.org.tiktak.writers;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import br.org.tiktak.core.GsonFactory;

public class WriterFile extends Writer {

	private File file;
	
	@Override
	public void send() throws Exception {
		try {
			createLogFile();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		concatenateJson(json, jsonEvents);		
	}
	
	File createLogFile() throws IOException {
		file = new File(this.path);
		if (!file.exists()) {
			file.createNewFile();
			RandomAccessFile writer = new RandomAccessFile(file, "rw");
			String json = GsonFactory.getGson().toJson(eventSystem) + "\n";
			writer.write(json.getBytes());
			writer.close();
		}
		return file;
	}
	
	void concatenateJson(final String json, final String jsonEventos) {
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			if (doesntContainEvents()) {
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
	
	private Boolean doesntContainEvents() {
		boolean isEmpty = false;
		int count = 0;
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			String linha = raf.readLine();
			count = linha.split("\\{", -1).length - 1;
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ((!isEmpty) && (count == 1));
	}

}
