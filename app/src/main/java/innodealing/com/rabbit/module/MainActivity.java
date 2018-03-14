package innodealing.com.rabbit.module;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

import innodealing.com.rabbit.R;
import innodealing.com.rabbit.entity.User;

public class MainActivity extends Activity {

    private ConnectionFactory factory;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFactory();
        initViews();
    }

    private void initViews() {
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = new User();
                user.setName("san dao 001");
                user.setPhone(18217430061L);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pushMessage(user);
                    }
                }).start();
            }
        });
        findViewById(R.id.btnGet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMessage();
                    }
                }).start();
            }
        });
    }

    private void initFactory() {
        factory = new ConnectionFactory();
        factory.setUsername("test");
        factory.setPassword("test");
        factory.setVirtualHost("/");
        factory.setHost("192.168.8.190");
        factory.setPort(5672);
    }

    private void pushMessage(Object msg) {
        try {
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();
            Log.d("lemon", "Sending message");
            channel.basicPublish("message", "qb_collector_android"
                    , null, gson.toJson(msg).getBytes());
            Log.d("lemon", "Sent : '" + gson.toJson(msg) + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMessage() {
        try {
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();
            Log.d("lemon", "Getting message");
            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    Log.d("lemon", "Get : '" + message + "'");
                }
            };
            channel.basicConsume("qb_collector_android", false, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
