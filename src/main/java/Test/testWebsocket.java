package Test;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ServerEndpoint(value = "/webSocketTest/{userId}")
public class testWebsocket {
    private int userId;
    //是在客户端建立连接自动时调用
    //类似于Servlet 中的 doGet(),dopost()


    //参数中@PathParam("userId")-----描述String userId来自于路径中
    //@ServerEndpoint(value = "/webSocketTest/{userId}")的userId

    @OnOpen//如果没有这个注解，此时onOpen方法就无法被正常调用
    public void onOpen(@PathParam("userId") String userIdStr, Session session){
        System.out.println("建立连接"+userIdStr);
        this.userId=Integer.parseInt(userIdStr);
        //创建一个线程，通过线程循环反复地给客户端发送数据
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    Date date=new Date();
                    SimpleDateFormat f = new SimpleDateFormat("今天是 " + "yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒");
                    try {
                        session.getBasicRemote().sendText(f.format(date));
                        Thread.sleep(1000);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    @OnClose//连接被断开时自动被调用
    public void onClose(){
        System.out.println("断开连接"+userId);
    }

    @OnError//连接异常终止时被自动调用
    public void onError(Session session,Throwable error){
        System.out.println("连接异常"+userId);
        System.out.println(error.getMessage());
    }


    @OnMessage//收到消息时自动调用
    //message 收到的消息
    public void onMessage(String message,Session session) throws IOException {
        System.out.print("收到消息了："+message);
        //服务器给客户端返回一个消息
        //返回一个当前的时间戳
        Date date=new Date();
        SimpleDateFormat f = new SimpleDateFormat("今天是 " + "yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒");
        String resp=""+System.currentTimeMillis();
        session.getBasicRemote().sendText(f.format(date));
    }
}
