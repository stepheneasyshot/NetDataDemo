package com.example.netdatademo.mqtt

import android.util.Log
import com.example.netdatademo.appContext
import com.example.netdatademo.mqtt.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


/**
 * {
 *   "clientId": "a1DHtiXHDgy.AndroidDemo|securemode=2,signmethod=hmacsha256,timestamp=1737511014134|",
 *   "username": "AndroidDemo&a1DHtiXHDgy",
 *   "mqttHostUrl": "a1DHtiXHDgy.iot-as-mqtt.cn-shanghai.aliyuncs.com",
 *   "passwd": "10e13f5f16e1792487ea50e98f5a5541f27e503e74b43cfd6834e7486318c07c",
 *   "port": 1883
 * }
 */

private const val MQTT_HOST_URL = "a1DHtiXHDgy.iot-as-mqtt.cn-shanghai.aliyuncs.com:1883"
private val ts
    get() = System.currentTimeMillis()
private val mqttClientId =
    "a1DHtiXHDgy.AndroidDemo|securemode=2,signmethod=hmacsha256,timestamp=$ts|"

class MqttClient(
    serverURI: String = MQTT_HOST_URL,
    clientId: String = mqttClientId
) {

    private val TAG = "MqttClient"
    private val mqttAndroidClient: MqttAndroidClient =
        MqttAndroidClient(appContext, serverURI, clientId)

    init {
        mqttAndroidClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost: ${cause?.message}")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "Message arrived: ${message?.payload?.toString(Charsets.UTF_8)}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d(TAG, "Delivery complete")
            }
        })
    }

    fun connect() {
        Log.d(TAG, "Connecting to MQTT broker...")
        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
        options.isCleanSession = false
        options.userName = "AndroidDemo&a1DHtiXHDgy"
        options.password =
            "10e13f5f16e1792487ea50e98f5a5541f27e503e74b43cfd6834e7486318c07c".toCharArray()

        try {
            mqttAndroidClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Connected to MQTT broker")
                    subscribeToTopic("your/topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(TAG, "Failed to connect to MQTT broker", exception)
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribeToTopic(topic: String) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Subscribed to topic: $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(TAG, "Failed to subscribe to topic: $topic", exception)
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publishMessage(topic: String, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            mqttAndroidClient.publish(topic, mqttMessage, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Message published to topic: $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(TAG, "Failed to publish message to topic: $topic", exception)
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            mqttAndroidClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Disconnected from MQTT broker")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(TAG, "Failed to disconnect from MQTT broker", exception)
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}
