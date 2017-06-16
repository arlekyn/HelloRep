import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class Main1 {
	static Socket socket;
	static BufferedWriter writer;
	static {
		try {
			socket = new Socket("10.4.8.13", 8281);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void read() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String line = null;
			System.out.println("read line");
			// System.out.println(line);
			// цикл пока читаемые данные существуют(не пусты)
			while (!(line = reader.readLine()).equals("null")) {
				// line = reader.readLine();
				// вывод читаемых данных
				System.out.println(line);

				// прерывание цикла, когда клиент напишет exit
				if (line.equalsIgnoreCase("exit")) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write() {
		Scanner in = new Scanner(System.in);
		String line = null;
		// получаем исходящий стрим из сокета
		try {
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			// while(!(line = in.nextLine()).equals(null)){
			while (in.hasNext()) {
				// пишем данные на сервер + конец строки
				line = in.nextLine();
				writer.write(line + "\n");
				// очищаем буфер для передачи данных
				writer.flush();
				// System.out.println(line);
				if (line.equalsIgnoreCase("exit")) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(() -> {
			read();
		});
		executor.execute(() -> {
			write();
		});
		// //создаем клиент сокет для подключения к сервер сокету по айпи адресу
		// Socket socket = new Socket("localhost", 8081);
		// Scanner in = new Scanner(System.in);
		// String line = null;
		// //получаем исходящий стрим из сокета
		// OutputStream out = socket.getOutputStream();
		// BufferedWriter writer = new BufferedWriter(new
		// OutputStreamWriter(out));
		// //BufferedOutputStream writer = new BufferedOutputStream(out);
		// while(!(line = in.nextLine()).equals(null)){
		// //пишем данные на сервер + конец строки
		// writer.write(line+"\n");
		// //очищаем буфер для передачи данных
		// writer.flush();
		// System.out.println(line);
		// if(line.equalsIgnoreCase("exit")){
		// break;
		// }
		// }
		// writer.close();
		// out.close();
		if(executor.isShutdown()){
			socket.close();
		}
		

	}

}
