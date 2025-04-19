package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.LogDTO.LogDTO;
import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.model.DeviceTrigger;
import dadn_SmartFarm.model.FeedInfo;
import dadn_SmartFarm.model.enums.LogType;
import dadn_SmartFarm.model.enums.Status;
import dadn_SmartFarm.repository.DeviceRepository;
import dadn_SmartFarm.repository.DeviceTriggerRepository;
import dadn_SmartFarm.service.interf.ILogService;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MqttService {
    private final DeviceRepository deviceRepository;
    private final DeviceTriggerRepository deviceTriggerRepository;
    private final ILogService logService;

    @Value("${ADAFRUIT_BROKER_URL}")
    private String BROKER_URL;

    @Value("${ADAFRUIT_USERNAME}")
    private String username;

    @Value("${ADAFRUIT_X_AIO_KEY}")
    private String aioKey;

    private boolean triggerValueFlag = false;
    private boolean isMqttConnected = false;
    private List<DeviceTrigger> deviceTriggerNowList = List.of();

    private final Map<Long, MqttClient> deviceClients = new ConcurrentHashMap<>();
    private final Map<String, List<Double>> feedDataBuffer = new ConcurrentHashMap<>();
    private final Map<String, Long> feedLastReceivedTime = new ConcurrentHashMap<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void connectDevice(Long deviceId, Map<String, FeedInfo> feedsList) {
        if (deviceClients.containsKey(deviceId)) {
            log.warn("Device {} is already connected. Subscribing to new feeds.", deviceId);
            feedsList.forEach((feedKey, feedInfo) -> executorService.submit(() -> subscribeToFeed(deviceId, feedKey)));
            return;
        }

        executorService.submit(() -> {
            try {
                MqttClient client = new MqttClient(BROKER_URL, MqttClient.generateClientId());
                MqttConnectOptions options = new MqttConnectOptions();
                options.setUserName(username);
                options.setPassword(aioKey.toCharArray());
                options.setAutomaticReconnect(true);
                options.setCleanSession(true);

                client.setCallback(new MqttCallbackExtended() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        log.error("Device {} lost connection: {}", deviceId, cause.getMessage());
                        isMqttConnected = false;
                    }

                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {
                        log.info("Device {} {}", deviceId, reconnect ? "reconnected" : "connected");
                        isMqttConnected = true;
                        feedsList.forEach((feedKey, feedId) -> executorService.submit(() -> subscribeToFeed(deviceId, feedKey)));
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        try {
                            String feedKey = extractFeedKeyFromTopic(topic);
                            String payload = new String(message.getPayload());

                            if (payload.isEmpty()) {
                                log.warn("Received empty message for device {} from feed {}", deviceId, feedKey);
                                return;
                            }

                            double value;
                            try {
                                value = Double.parseDouble(payload);
                            } catch (NumberFormatException e) {
                                log.error("Invalid message format for device {}: {}", deviceId, payload);
                                return;
                            }

                            log.info("Device {} received: [{}] {}", deviceId, feedKey, value);
                            feedDataBuffer.computeIfAbsent(feedKey, k -> new ArrayList<>()).add(value);
                            feedLastReceivedTime.put(feedKey, System.currentTimeMillis());

                            FeedInfo feedInfo = getFeedInfo(deviceId, feedKey);
                            if (feedInfo == null) {
                                log.warn("FeedInfo not found for device {} and feed {}", deviceId, feedKey);
                                return;
                            }

                            if (feedInfo.getThreshold_max() != null && value > feedInfo.getThreshold_max()) {
                                handleThresholdExceeded(deviceId, feedKey, "MAX", value);
                                triggerValueFlag = true;
                            } else if (feedInfo.getThreshold_min() != null && value < feedInfo.getThreshold_min()) {
                                handleThresholdExceeded(deviceId, feedKey, "MIN", value);
                                triggerValueFlag = true;
                            } else {
                                handleThresholdExceeded(deviceId, feedKey, "DEFAULT", value);
                                triggerValueFlag = false;
                            }

                        } catch (Exception e) {
                            log.error("Error processing message for device {}: {}", deviceId, e.getMessage(), e);
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        log.info("Device {}: Message delivered", deviceId);
                    }
                });

                client.connect(options);
                deviceClients.put(deviceId, client);

                feedsList.forEach((feedKey, feedId) -> executorService.submit(() -> subscribeToFeed(deviceId, feedKey)));

                log.info("Device {} connected to MQTT feeds: {}", deviceId, feedsList.keySet());

            } catch (MqttException e) {
                log.error("Error connecting device {}: {}", deviceId, e.getMessage());
            }
        });
    }

    private void subscribeToFeed(Long deviceId, String feedKey) {
        try {
            MqttClient client = deviceClients.get(deviceId);
            if (client == null || !client.isConnected()) {
                log.warn("Device {} is not connected. Cannot subscribe to feed {}", deviceId, feedKey);
                return;
            }

            String topic = username + "/feeds/" + feedKey;
            client.subscribe(topic);
        } catch (MqttException e) {
            log.error("Error subscribing device {} to feed {}: {}", deviceId, feedKey, e.getMessage());
        }
    }

    public void publishMessage(Long deviceId, String feedKey, String value) {
        try {
            if (!deviceClients.containsKey(deviceId)) {
                log.error("Device {} is not connected. Cannot publish message.", deviceId);
                return;
            }

            MqttClient client = deviceClients.get(deviceId);
            String topic = username + "/feeds/" + feedKey;
            MqttMessage mqttMessage = new MqttMessage(value.getBytes());
            mqttMessage.setQos(1);

            client.publish(topic, mqttMessage);
            log.info("Device {} published value to {}: {}", deviceId, topic, value);
        } catch (MqttException e) {
            log.error("Error publishing value for device {}: {}", deviceId, e.getMessage());
        }
    }

    public void disconnectDevice(Long deviceId) {
        executorService.submit(() -> {
            MqttClient client = deviceClients.remove(deviceId);
            if (client != null) {
                try {
                    client.disconnect();
                    log.info("Device {} disconnected.", deviceId);
                    isMqttConnected = false;
                } catch (MqttException e) {
                    log.error("Error disconnecting device {}: {}", deviceId, e.getMessage());
                }
            }
        });
    }

    @PreDestroy
    public void cleanup() {
        executorService.submit(() -> {
            deviceClients.forEach((deviceId, client) -> disconnectDevice(deviceId));
            log.info("MQTT Service shutting down.");
        });

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    private FeedInfo getFeedInfo(Long deviceId, String feedKey) {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null) return null;
        return device.getFeedsList().get(feedKey);
    }

    private String extractFeedKeyFromTopic(String topic) {
        String[] parts = topic.split("/");
        if (parts.length >= 3) {
            return parts[2];
        }
        return null;
    }

    private void handleThresholdExceeded(Long deviceId, String feedKey, String thresholdType, double value) {
        if (!triggerValueFlag && (thresholdType.equals("MAX") || thresholdType.equals("MIN") )) {
            log.warn("Device {}: [{}] exceeded {} threshold with value {}", deviceId, feedKey, thresholdType, value);
            deviceTriggerNowList = deviceTriggerRepository.findBySensorFeedKeyAndStatusAndThresholdCondition(
                    feedKey,
                    Status.ACTIVE,
                    thresholdType
            );
            deviceTriggerNowList.forEach((deviceTrigger) -> {
                publishMessage(deviceId, deviceTrigger.getControlFeedKey(), deviceTrigger.getValueSend());
            });

            LogDTO logDTO = LogDTO.builder()
                    .feedKey(feedKey)
                    .logType(Objects.equals(thresholdType, "MAX") ? LogType.TRIGGER_MAX : LogType.TRIGGER_MIN)
                    .value(String.valueOf(value))
                    .build();
            logService.createLog(logDTO);
        }
        else if (thresholdType.equals("DEFAULT") && !deviceTriggerNowList.isEmpty()) {
            deviceTriggerNowList.forEach((deviceTrigger) -> {
                publishMessage(deviceId, deviceTrigger.getControlFeedKey(), "0");
            });
            deviceTriggerNowList = null;
        }
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void logSensorDataPeriodically() {
        if (isMqttConnected) {
            long currentTime = System.currentTimeMillis();

            feedDataBuffer.forEach((feedKey, values) -> {
                if (!values.isEmpty()) {
                    long lastReceivedTime = feedLastReceivedTime.get(feedKey);
                    if (currentTime - lastReceivedTime <= 60 * 1000) {
                        double averageValue = values.stream().mapToDouble(val -> val).average().orElse(0.0);

                        LogDTO logDTO = LogDTO.builder()
                                .feedKey(feedKey)
                                .logType(LogType.DATA)
                                .value(String.valueOf(averageValue))
                                .build();
                        logService.createLog(logDTO);
                    }
                }
            });

            feedDataBuffer.clear();
            feedLastReceivedTime.clear();
        }
    }

}