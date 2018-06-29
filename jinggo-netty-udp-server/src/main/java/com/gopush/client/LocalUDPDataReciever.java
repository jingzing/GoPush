package com.gopush.client;

import com.gopush.utils.ConfigEntity;
import com.gopush.utils.Log;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author wangyj
 * @description
 * @create 2018-06-29 17:36
 **/
public class LocalUDPDataReciever {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(LocalUDPSocketProvider.class);
    private static final String TAG = LocalUDPDataReciever.class.getSimpleName();
    private static LocalUDPDataReciever instance = null;
    private Thread thread = null;

    public static LocalUDPDataReciever getInstance()
    {
        if (instance == null)
            instance = new LocalUDPDataReciever();
        return instance;
    }

    public void startup()
    {
        this.thread = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    LOG.debug(LocalUDPDataReciever.TAG, "本地UDP端口侦听中，端口=" + ConfigEntity.localUDPPort + "...");

                    //开始侦听
                    LocalUDPDataReciever.this.udpListeningImpl();
                }
                catch (Exception eee)
                {
                    LOG.warn(LocalUDPDataReciever.TAG, "本地UDP监听停止了(socket被关闭了?)," + eee.getMessage(), eee);
                }
            }
        });
        this.thread.start();
    }

    private void udpListeningImpl() throws Exception
    {
        while (true)
        {
            byte[] data = new byte[1024];
            // 接收数据报的包
            DatagramPacket packet = new DatagramPacket(data, data.length);

            DatagramSocket localUDPSocket = LocalUDPSocketProvider.getInstance().getLocalUDPSocket();
            if ((localUDPSocket == null) || (localUDPSocket.isClosed()))
                continue;

            // 阻塞直到收到数据
            localUDPSocket.receive(packet);

            // 解析服务端发过来的数据
            String pFromServer = new String(packet.getData(), 0 , packet.getLength(), "UTF-8");
            Log.w(LocalUDPDataReciever.TAG, "【NOTE】>>>>>> 收到服务端的消息："+pFromServer);
        }
    }
}
