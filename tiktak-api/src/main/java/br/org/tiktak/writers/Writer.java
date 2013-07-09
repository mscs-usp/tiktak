package br.org.tiktak.writers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import br.org.tiktak.core.Event;
import br.org.tiktak.core.EventSystem;
import br.org.tiktak.core.GsonFactory;

public abstract class Writer {

	protected String path;
	protected EventSystem eventSystem;
	protected String json;
	protected String jsonEvents;

	private String directoryPath;
	private String filePath;

	private String exporter;
	private String wsurl;

	// public abstract void log(final String usuario, final String nomeDoEvento)
	// throws Exception;
	public abstract void send() throws Exception;

	public void log(final String user, final String eventName, final String pathOfWriting) throws Exception {
		path = pathOfWriting;

		eventSystem = EventSystem.getInstance();
		Event evento = new Event(user, eventName);
		eventSystem.getEvents().add(evento);
		json = GsonFactory.getGson().toJson(eventSystem) + "\n";
		jsonEvents = GsonFactory.getGson().toJson(evento);

		send();
	}

	public void setPathOfFile() throws IOException {
		String parameterSetFile = "tik.tak";
		filePath = this.directoryPath + parameterSetFile;
		exporter = getPropriertiesOfPropertiesFile("tiktak.exporter");
		if (exporter != null && exporter.equals("webservice")) {
			wsurl = getPropriertiesOfPropertiesFile("tiktak.ws-url");
		} else {
			exporter = "file";
		}
	}

	public String getPropriertiesOfPropertiesFile(final String proprierty) {
		String property;
		File file = new File("tiktak.properties");
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fis);
			property = properties.getProperty(proprierty);
		} catch (Exception e) {
			property = null;
		}

		return property;
	}
}
