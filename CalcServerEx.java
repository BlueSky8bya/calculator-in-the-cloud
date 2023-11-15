package _01_SocketProgramming;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalcServerEx {
	
	public static String calc(String exp) {
		
		StringTokenizer st = new StringTokenizer(exp, " ");
		
		if(st.countTokens() < 3)
			// 인자가 너무 적다
			return "C102";
		if(st.countTokens() > 3)
			// 인자가 너무 많다
			return "C456";
			
		String res = "";
		String opcode = st.nextToken().toUpperCase();
		System.out.println(opcode);
		

		double op1;
		double op2;
		
		try {
			op1 = Double.parseDouble(st.nextToken());
			op2 = Double.parseDouble(st.nextToken());
		} catch (Exception e) {
			// double형으로 입력받는 데 문제가 있는 경우
			return "C444";
		}
			
		switch(opcode) {
		case "ADD":
			res = Double.toString(op1+op2);
			break;
		case "SUB":
			res = Double.toString(op1-op2);
			break;
		case "MUL":
			res = Double.toString(op1*op2);
			break;
		case "DIV":
			if(op2 != 0)
				res = Double.toString(op1/op2);
			else
				// 0으로 나누는 경우
				return "C510";
			break;
		default:
			// 연산자 오타
			res = "C404";
		}
		return res;
		
	}

	public static void main(String[] args) throws Exception {
		
		//src/_01_SocketProgramming/server_info.txt, e.g.) "localhost 9999" ->띄어쓰기로 구분되어있음.
		File file = new File("src/_01_SocketProgramming/server_info.txt");
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		// 파일에 있는 값 한 줄을 읽어서 " "단위로 나누기
		String line = bufferedReader.readLine();
		StringTokenizer st = new StringTokenizer(line, " ");
		
		//IP address를 지나고 바로 port number를 저장한다
		st.nextToken();
		int port = Integer.parseInt(st.nextToken());
		
		
		ServerSocket listener = new ServerSocket(port);
		Socket socket = null;
		
		try {
			System.out.println("연결을 기다리고 있습니다......");
			ExecutorService pool = Executors.newFixedThreadPool(20);
			while (true) {
				socket = listener.accept();
				pool.execute(new MyRunnable(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				listener.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileReader.close();
			bufferedReader.close();
		}
	}
	
	private static class MyRunnable implements Runnable {
		private Socket socket;
		
		MyRunnable(Socket socket){
			this.socket = socket;
		}
		
		@Override
		public void run() {
			
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				while(true) {
					String inputMessage = in.readLine();
					
					if(inputMessage.equalsIgnoreCase("bye")) {
						System.out.println("클라이언트에서 연결 종료");
						break;	// "bye"를 받으면 연결 종료
					}
					
					System.out.println(inputMessage);	// 받은 메세지를 화면에 출력
					
					String res = calc(inputMessage);	// 계산, 계산 결과는 res
					out.write(res + "\n");	// 계산 결과 문자열 전송
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try { socket.close(); } catch (IOException e) {}
				System.out.println("Closed: " + socket);
			}
		}
	}
}

