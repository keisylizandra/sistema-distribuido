package sd;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class MinhaConexao extends Thread{

    Socket conexao;
    public MinhaConexao(Socket c) {
        conexao = c;
    }

    public void run() {
        try {
            System.out.println("CHEGOU NOVA CONEXAO !!!");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conexao.getOutputStream()));

            String firstLine = reader.readLine();
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                System.out.println(line);
                line = reader.readLine();
            }

            String response = handleRoutes(firstLine);
            writer.write(response);
            writer.flush();
            conexao.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nFIM CONEXAO");

    }

    public static String responseWriter(String statusCode, String content) {
        String response = "HTTP/1.1 " + statusCode + "\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                content;
        return response;
    }

    public static String handleRoutes(String line) {

        String route = line.split(" ")[1];

        String statusCode;
        String content;

        if (route.equals("/")) {
            statusCode = "200 OK";
            content = styleInitialPage();
        }

        else if (route.equals("/door1")) {
            statusCode = "200 OK";
            content = styleDoor("Ihuuu, você acertou!!");
        }

        else if (route.equals("/door2")) {
            statusCode = "200 OK";
            content = styleDoor("Nada aconteceu... :/");
        }

        else if (route.equals("/door3")) {
            statusCode = "404 Not Found";
            content = styleDoor("Você perdeu hahah XD");
        }

        else {
            statusCode = "404 Not Found";
            content = styleDoor("Rota desconhecida *O*");
        }

        return responseWriter(statusCode, content);
    }

    public static String styleInitialPage() {
        String serverName = "Desconhecido";
        try {
            serverName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {

        }
        return "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { text-align:center; font-family: Times New Roman; }" +
                ".door {" +
                " display:inline-block;" +
                " width:80px;" +
                " height:140px;" +
                " margin:15px;" +
                " background:#8B4513;" +
                " border:3px solid #5a2e0e;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h2>Escolha uma porta</h2>" +

                "<a href='/door1'><div class='door'></div></a>" +
                "<a href='/door2'><div class='door'></div></a>" +
                "<a href='/door3'><div class='door'></div></a>" +

                "<br>" +
                "<div class='footer'>Respondido pelo servidor: <b>" + serverName + "</b></div>" +

                "</body></html>";
    }

    public static String styleDoor(String phrase) {
        return "<html><head><meta charset='UTF-8'></head>" +
                "<body style='text-align:center; font-family:Times New Roman;'>" +
                "<h1>" + phrase + "</h1>" +
                "<a href='/'>Tentar de novo</a>" +
                "</body></html>";
    }

}