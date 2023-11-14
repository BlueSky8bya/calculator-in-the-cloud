package _01_SocketProgramming;

import java.io.*;
import java.net.*;
import java.util.*;

public class CalcServerEx {
	
	public static String calc(String exp) {
		
		StringTokenizer st = new StringTokenizer(exp, " ");
		
		if(st.countTokens() != 3)
			return "error";
		
		String res = "";
		
		double op1 = Double.parseDouble(st.nextToken());
		String opcode = st.nextToken();
		double op2 = Double.parseDouble(st.nextToken());
		
		switch(opcode) {
		case "+":
			res = Double.toString(op1+op2);
			break;
		case "-":
			res = Double.toString(op1-op2);
			break;
		case "*":
			res = Double.toString(op1*op2);
			break;
		case "/":
			res = Double.toString(op1/op2);
			break;
		default:
			res = "error";
		}
		return res;
	}

	public static void main(String[] args) {
		
		// 서버의 정보를 저장해 둔 "server_info.txt"라는 파일 읽을 준비
		File file = new File("server_info.txt");
		FileReader fileReader = null;
		
		BufferedReader in = null;
		BufferedWriter out = null;
		ServerSocket listener = null;
		Socket socket = null;
		
		try {
			
			fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			// 파일에 있는 값 한 줄을 읽어서 " "단위로 나누기
			String line = bufferedReader.readLine();
			StringTokenizer st = new StringTokenizer(line, " ");
			
			bufferedReader.close();
			
			//IP adress를 지나고 바로 port number를 저장한다
			st.nextToken();
			int port = Integer.parseInt(st.nextToken());


			listener = new ServerSocket(port);	// 서버 소켓 생성
			System.out.println("연결을 기다리고 있습니다......");
			
			socket = listener.accept();	// 클라이언트로부터 연결 요청 대기
			System.out.println("연결되었습니다.");
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			while(true) {
				String inputMessage = in.readLine();
				
				if(inputMessage.equalsIgnoreCase("bye")) {
					System.out.println("클라이언트에서 연결을 종료하였음");
					break;	// "bye"를 받으면 연결 종료
				}
				
				System.out.println(inputMessage);	// 받은 메세지를 화면에 출력
				
				String res = calc(inputMessage);	// 계산, 계산 결과는 res
				out.write(res + "\n");	// 계산 결과 문자열 전송
				out.flush();
				
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(socket != null)
					socket.close();// 통신용 소켓 닫기
				if(listener != null)
					listener.close();// 서버용 소켓 닫기
			} 
			catch (IOException e) {
				System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
			} finally {
				try {
					if(fileReader != null)
						fileReader.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
