package _01_SocketProgramming;

import java.io.*;
import java.net.*;
import java.util.*;

public class CalcClientEx {

	public static void main(String[] args) {
		
		FileReader fileReader = null;
		
		BufferedReader in = null;
		BufferedWriter out = null;
		Socket socket = null;
		Scanner scanner = new Scanner(System.in);
		
		try {
			
			//src/_01_SocketProgramming/server_info.txt, e.g.) "localhost 9999" ->띄어쓰기로 구분되어있음.
			File file = new File("src/_01_SocketProgramming/server_info.txt");
			
			fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String line = bufferedReader.readLine();
			StringTokenizer st = new StringTokenizer(line, " ");
			
			bufferedReader.close();
			
			String IPadress = st.nextToken();
			int port = Integer.parseInt(st.nextToken());
			
			
			socket = new Socket(IPadress, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			while(true) {
				// 프롬프트
				System.out.println("ADD: (+), SUB: (-), MUL: (*), DIV: (/)");
				System.out.println("계산식(연산자 숫자 숫자 순서, 빈칸으로 띄어서 입력, 예: DIV 8 2)");
				
				String outputMessage = scanner.nextLine();// 키보드에서 수식 읽기
				
				if(outputMessage.equalsIgnoreCase("bye")) {
					out.write(outputMessage + "\n");	// "bye" 문자열 전송
					out.flush();
					break;	// 사용자가 "bye"를 입력한 경우 서버로 전송 후 연결 종료
				}
				
				out.write(outputMessage + "\n");	// 키보드에서 읽은 수식 문자열 전송
				out.flush();
				
				String inputMessage = in.readLine();// 서버로부터 계산 결과 수신
				if(inputMessage.equals("C102"))
					System.out.println("Error Code: 102\nError Message: 인자가 너무 적음");
				else if(inputMessage.equals("C404"))
					System.out.println("Error Code: 404\nError Message: 연산자 오타");
				else if(inputMessage.equals("C444"))
					System.out.println("Error Code: 444\nError Message: 잘못된 입력 형식");
				else if(inputMessage.equals("C456"))
					System.out.println("Error Code: 456\nError Message: 인자가 너무 많음");
				else if(inputMessage.equals("C510"))
					System.out.println("Error Code: 510\nError Message: 0으로 나눔");
				else
					System.out.println("계산 결과: " + inputMessage);
				System.out.println("-------------------------------------------\n");
			}

		} catch(IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close();	//클라이언트 소켓 닫기
			} catch(IOException e) {
				System.out.println("서버와 채팅 중 오류가 발생했습니다.");
			} finally {
				try {
					if(fileReader != null)
						fileReader.close();
					if(in != null)
						in.close();
					if(out != null)
						out.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}

				scanner.close();
			}
			
		}

	}

}
