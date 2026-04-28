package inventory.inventoryservice.business;

import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.event.Channel;
import io.kubemq.sdk.event.Event;
import io.kubemq.sdk.tools.Converter;

import javax.net.ssl.SSLException;
import java.io.IOException;

public class Messaging {
    public static void sendMessage(String message) throws IOException {

        String channelName   = "stock_update_channel";
        String clientID      = "inventory-service-publisher";
        String kubeMQAddress = System.getenv("kubeMQAddress");

        Channel channel = new Channel(channelName, clientID, false, kubeMQAddress);

        channel.setStore(true);

        Event event = new Event();
        event.setBody(Converter.ToByteArray(message));
        event.setEventId("event-store-");

        try {
            channel.SendEvent(event);
        } catch (SSLException e) {
            System.out.printf("SSLException: %s%n", e.getMessage());
            e.printStackTrace();
        } catch (ServerAddressNotSuppliedException e) {
            System.out.printf("ServerAddressNotSuppliedExceptionException: %s%n", e.getMessage());
            e.printStackTrace();
        }
    }
}
