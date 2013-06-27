package br.org.tiktak.escritores;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLEncoder;

public class EscritorWS extends Escritor{

	@Override
	public void envia() throws Exception {
	    String data = URLEncoder.encode("json", "UTF-8");// + "=" + URLEncoder.encode(json, "UTF-8");
		
	    Socket socket = new Socket(caminho, 8080);

	    String path = "/servlet";
	    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
	    wr.write("POST " + path + " HTTP/1.1\r\n");
	    wr.write("Content-Length: " + data.length() + "\r\n");
	    wr.write("Content-Type: application/json\r\n");
	    wr.write("\r\n");

	    wr.write(data);
	    wr.flush();

//	    BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//	    String line;
//	    while ((line = rd.readLine()) != null) {
//	      System.out.println(line);
//	    }
	    wr.close();
//	    rd.close();
	}

}
