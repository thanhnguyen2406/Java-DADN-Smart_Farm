package dadn_SmartHome.service.implement;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
    private static final String BROKER_URL = "tcp://io.adafruit.com:1883";
    private static final String USERNAME = "HillsRain";
    private static final String AIO_KEY = System.getProperty("X_AIO_KEY"); // Hoặc lấy từ System properties
    private static final String FEED_NAME = USERNAME + "/feeds/BBC_TEMP";

    private MqttClient client;

    public MqttService() {
        try {
            client = new MqttClient(BROKER_URL, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(USERNAME);
            options.setPassword(AIO_KEY.toCharArray());

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost! " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("Receive data from " + topic + ": " + new String(message.getPayload()));
                    // Xử lý dữ liệu ở đây
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("Send successfully");
                }
            });

            client.connect(options);
            client.subscribe(FEED_NAME);
            System.out.println("Đã kết nối và đăng ký nhận dữ liệu từ " + FEED_NAME);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

