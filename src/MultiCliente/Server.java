/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiCliente;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author DanielSilva
 */
public class Server {

    static Vector<ClientHandler> DadosClientList = new Vector<>();//Armazenar connexões numa lista de connexões
    static int i = 0;//Contador de clientes que já foram recebidos

    public static void main(String[] args) throws IOException {
        System.out.println("Servidor Aceita connecções");
        ServerSocket ss = new ServerSocket(1234); //Espera por pedidos de ligação através da rede

        Socket s;//Vai representar quais foram os clientes que foram connectados

        while (true) {//ciclo infinito até haver connexão

            s = ss.accept();//Espera que uma connecxão seja feita e aceita

            System.out.println("Novo Cliente Se conectou" + s);//

            DataInputStream dis = new DataInputStream(s.getInputStream());//Canal para recever dados
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());//Canal de envio de dados

            ClientHandler client = new ClientHandler("Cliente" + i, dis, dos, s);//Dados do client
            Thread t = new Thread(client);
            System.out.println("Adiciona o cliente" + i + "á Lista");
            DadosClientList.add(client);//Adiciona o cliente com os seus dados á Lista
            t.start();
            i++;//Contador de clientes que já foram recebidos
        }
    }
    
    private static class ClientHandler implements Runnable{//Class para lidar com cada client, 
        private String nome;
        final DataInputStream dis;
        final DataOutputStream dos;
        Socket s;//Socket que o servidor recebeu para estabelecer a connexão
        boolean islogged;//Informa se está ou não connectado

        public ClientHandler(String nome, DataInputStream dis, DataOutputStream dos, Socket s) {
            this.nome = nome;
            this.dis = dis;
            this.dos = dos;
            this.s = s;
            islogged=true;//neste momento o cliente foi connectado
        }

        @Override
        public void run() {
           String recebido;
           
           while(true){
               try{
                   recebido = dis.readUTF();//Fica á espera de receber mensagem de texto
                   System.out.println(recebido);//Informar oq recebeu
                  
                   if(recebido.endsWith("logout")){//Verificar se o cliente quer-se desconnectar
                       this.islogged=false;
                       this.s.close();//Connecxão deste cliente é fechada
                       break;//Sai do ciclo infinito while
                   }
                   
                   //Enviar Mensagem para outro cliente presente na List
                   //Para saber qual utilizador nos referimos dizemos o nome dele através do formato mensagem#Client01
                   StringTokenizer separa = new StringTokenizer(recebido,"#");
                   String msgEnviar = separa.nextToken();
                   String cliDestino = separa.nextToken();
                   
                    
                   
               }catch(IOException e){
                   e.printStackTrace();
               }
                
               
               
               
           }
        }
        
        
    }
    
}
