package br.org.tiktak.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Properties;

import br.org.tiktak.core.EventSystem;
import br.org.tiktak.core.GsonFactory;
import br.org.tiktak.writers.Writer;
import br.org.tiktak.writers.WriterFile;
import br.org.tiktak.writers.WriterWS;

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
	private String path;
	private String filePath;
	private final String systemAPI;
	private File file;
	private EventSystem eventSystem;

	private String exporter;
	private String wsurl;
	private boolean trueSetDir = false;

	/**
	 * Constructor with parameter
	 * 
	 * @param system
	 *            String with name of the system in use
	 * */
	public TikTak(final String system) {
		path = System.getProperty("user.dir") + "/";
		filePath = "";
		systemAPI = system;
		this.eventSystem = EventSystem.getInstance();
		this.eventSystem.init(systemAPI);
		getAndCreateInFileSystem();
	}

	/**
	 * Constructor without parameter
	 * */
	public TikTak() {
		this("System");
	}

	private void getAndCreateInFileSystem() {
		try {
			setDirectoryPath();
			createDirectoryLog();
			setPathOfFile();
			createLogFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public String getDirectoryPath() {
		return path;
	}

	/**
	 * Set path of directory
	 * 
	 * @param nameOfDirectory
	 *            String with directory name
	 * */
	public void setDir(String nameOfDirectory) {
		trueSetDir = true;
		if (nameOfDirectory.equals("")) {
			nameOfDirectory = path;
		} else if (!nameOfDirectory.endsWith("/")) {
			nameOfDirectory += "/";
		}
		this.path = nameOfDirectory;
		getAndCreateInFileSystem();
	}

	private Writer writer() {
		if (exporter.equals("webservice")) {
			return new WriterWS();

		} else {
			return new WriterFile();
		}
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

		if (exporter.equals("webservice")) {
			try {
				writer().log(user, eventName, wsurl);
			} catch (Exception e) {

			}
		} else {
			try {
				writer().log(user, eventName, filePath);
			} catch (Exception e) {

			}
		}
	}

	private void setDirectoryPath() throws IOException {
		String parametroGetProperty, parametroFileProperties;

		parametroFileProperties = getPropriertiesOfPropertiesFile("tiktak.dir");
		parametroGetProperty = System.getProperty("tiktak.dir");

		if (trueSetDir) {
		} else if (parametroFileProperties != null && !parametroFileProperties.equals("")) {
			path = parametroFileProperties;
		} else if (parametroGetProperty != null) {
			path = parametroGetProperty;
		}
	}

	private String getPropriertiesOfPropertiesFile(final String proprierty) {
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

	private String createDirectoryLog() throws IOException {
		String directory = path;
		if (directory != "") {
			File physicalDirectory = new File(directory);
			if (!physicalDirectory.exists()) {
				physicalDirectory.mkdir();
			}
		}
		return directory;
	}

	private void setPathOfFile() throws IOException {
		String parameterSetFile = "tik.tak";
		filePath = this.path + parameterSetFile;
		exporter = getPropriertiesOfPropertiesFile("tiktak.exporter");
		if (exporter != null && exporter.equals("webservice")) {
			wsurl = getPropriertiesOfPropertiesFile("tiktak.ws-url");
		} else {
			exporter = "file";
		}
	}

	private File createLogFile() throws IOException {
		file = new File(this.filePath);
		if (!file.exists()) {
			file.createNewFile();
			RandomAccessFile writer = new RandomAccessFile(file, "rw");
			String json = GsonFactory.getGson().toJson(eventSystem) + "\n";
			writer.write(json.getBytes());
			writer.close();
		}
		return file;
	}

}