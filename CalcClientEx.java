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
				System.out.println("계산식(빈칸으로 띄어 입력, 예: 24 + 42)>>");	// 프롬프트
				
				String outputMessage = scanner.nextLine();// 키보드에서 수식 읽기
				
				if(outputMessage.equalsIgnoreCase("bye")) {
					out.write(outputMessage + "\n");	// "bye" 문자열 전송
					out.flush();
					break;	// 사용자가 "bye"를 입력한 경우 서버로 전송 후 연결 종료
				}
				
				out.write(outputMessage + "\n");	// 키보드에서 읽은 수식 문자열 전송
				out.flush();
				
				String inputMessage = in.readLine();// 서버로부터 계산 결과 수신
				System.out.println("계산 결과: " + inputMessage);
				
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
