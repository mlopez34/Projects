using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using ConnectFour_FinalProject.Properties;
using System.Media;
using System.Resources;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.Xml;
using System.Xml.Linq;
using System.Collections;

namespace ConnectFour_FinalProject
{

    public class Network
    {
        //stores the current received move from opponent
        private int receivedMove;
        private string ready = "N";
        Thread thdListener;
        Thread thdHandler;
        TcpListener tcpListener;
        Socket handlerSocket;
        WaitingForPlayer waiting;
        private string[] testIPs;
        //private string[] testIPs = new string[1] { "99.124.150.158" };
        private ArrayList nSockets;
        private CFBoard gameboard;
        private int localOrder;
        public bool legal = true;
        Thread waitingthread;

        public Network(CFBoard board, string IP)
        {
            this.gameboard = board;
            IPHostEntry MyIP = Dns.GetHostByName(Dns.GetHostName());
            testIPs = new string[2];
            testIPs[0] = IP;
            testIPs[1] = MyIP.AddressList[0].ToString();
            SetOrder();
            Listen();
            
            
        }
        public void SendReady()
        {
            waitingthread = new Thread(new ThreadStart(ShowWaiting));
            waitingthread.Start();
            Send(localOrder.ToString());

        }
        public void ShowWaiting()
        {
            waiting = new WaitingForPlayer(gameboard);
            DialogResult result = waiting.ShowDialog();

        }
        public int ReceivedMove
        {
            get { return this.receivedMove; }
            set { this.receivedMove = value; }
        }
        public string Ready
        {
            get { return this.ready; }
            set { this.ready = value; }
        }
        public void CloseConnections()
        {
            this.tcpListener.Stop();
            this.handlerSocket = null;
            this.thdListener.Abort();


        }
        public void SetOrder()
        {
            Random ran = new Random();
            localOrder = ran.Next(8, int.MaxValue);

        }
        public void CloseWaiting()
        {
            try
            {
                
                waiting.Invoke((MethodInvoker)delegate
                {
                    waiting.Close();
                });
                
            }
            catch
            {

            }

        }
        //Listens for incoming data (move)
        public void Listen()
        {
            IPHostEntry IPHost = Dns.GetHostByName(Dns.GetHostName());
            nSockets = new ArrayList();
            thdListener = new Thread(new ThreadStart(listenerThread));
            thdListener.Start();
        }
        public void listenerThread()
        {
            this.tcpListener = new TcpListener(IPAddress.Any, 8080);
            this.tcpListener.Start();
            while (true)
            {
                try
                {
                    handlerSocket = tcpListener.AcceptSocket();
                    if (handlerSocket.Connected)
                    {
                        lock (this)
                        {
                            nSockets.Add(handlerSocket);
                        }
                        ThreadStart thdstHandler = new ThreadStart(handlerThread);
                        thdHandler = new Thread(thdstHandler);
                        thdHandler.Start();
                    }
                }
                catch 
                {
                    // MessageBox.Show("Multiplayer network game canceled!");

                }

            }
        }

        public async void handlerThread()
        {
            handlerSocket = (Socket)nSockets[nSockets.Count - 1];
            NetworkStream networkStream = new NetworkStream(handlerSocket);
            int thisRead = 0;
            int blockSize = 32;
            Byte[] dataByte = new Byte[blockSize];

            string ReceivedData = "";
            if (ready != "R")
            {
                await Task.Delay(3500);
            }
            lock (this)
            {

                while (true)
                {
                    thisRead = networkStream.Read(dataByte, 0, blockSize);
                    ReceivedData = System.Text.Encoding.UTF8.GetString(dataByte);
                    receivedMove = Convert.ToInt32(ReceivedData);
                    if (ReceivedMove < 0)
                    {
                        gameboard.GameOver = true;
                        gameboard.connectionEst = false;
                        CloseConnections();
                        MessageBox.Show("Opponent Left the Game");
                        break;
                    }
                    if (thisRead == 0)
                    {
                        break;
                    }
                    else if (receivedMove > 7)
                    {
                        if (ready != "R")
                        {

                            ready = "R";
                            gameboard.OpponentMove(receivedMove, false);
                            try
                            {
                                if (receivedMove > localOrder)
                                {
                                    waiting.Invoke((MethodInvoker)delegate
                                    {
                                        waiting.Close();
                                    });
                                    gameboard.foundPlayer(localOrder, receivedMove);
                                    //gameboard.connectionEst = true;
                                    //gameboard.GameOver = true;
                                    //gameboard.myTurn = false;
                                    break;


                                }
                                else
                                {
                                    waiting.Invoke((MethodInvoker)delegate
                                    {
                                        waiting.Close();
                                    });
                                    gameboard.foundPlayer(localOrder, receivedMove);
                                    //gameboard.connectionEst = true;
                                    //gameboard.GameOver = false;
                                    //gameboard.myTurn = true;
                                    break;

                                }


                            }
                            catch
                            {
                                //
                            }
                        }
                    }
                    else if (receivedMove == localOrder)
                    {
                        MessageBox.Show("clogged");
                    }
                    else
                    {
                        gameboard.OpponentMove(receivedMove, legal);
                        break;
                    }
                }

            }
            //handlerSocket = null;
        }
        //Sends data to the opponent (move)
        public void Send(string Move)
        {
            try
            {
                // Alocate memory space for the file
                byte[] fileBuffer = new byte[32];
                fileBuffer = System.Text.Encoding.UTF8.GetBytes(Move);
                // Open a TCP/IP Connection and send the data
                TcpClient clientSocket = new TcpClient(testIPs[0], 8081);
                NetworkStream networkStream = clientSocket.GetStream();
                networkStream.Write(fileBuffer, 0, fileBuffer.GetLength(0));
                networkStream.Close();
            }
            catch
            {
                Send(localOrder.ToString());
            }
        }

    }
}
