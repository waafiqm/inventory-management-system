package lowstock.lowstockservice.business;

import io.grpc.stub.StreamObserver;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.event.EventReceive;
import io.kubemq.sdk.event.Subscriber;
import io.kubemq.sdk.subscription.EventsStoreType;
import io.kubemq.sdk.subscription.SubscribeRequest;
import io.kubemq.sdk.subscription.SubscribeType;
import io.kubemq.sdk.tools.Converter;
import lowstock.lowstockservice.helper.ProductInfo;
import lowstock.lowstockservice.persistence.Product_CRUD;

import javax.net.ssl.SSLException;

public class Messaging {
    public static void Receiving_Events_Store(String cname)
            throws SSLException, ServerAddressNotSuppliedException {

        String channelName   = cname;
        String clientID      = "lowstock-service-subscriber";
        String kubeMQAddress = System.getenv("kubeMQAddress");

        Subscriber subscriber = new Subscriber(kubeMQAddress);

        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setChannel(channelName);
        subscribeRequest.setClientID(clientID);
        subscribeRequest.setSubscribeType(SubscribeType.EventsStore);
        subscribeRequest.setEventsStoreType(EventsStoreType.StartAtSequence);
        subscribeRequest.setEventsStoreTypeValue(1);

        StreamObserver<EventReceive> streamObserver = new StreamObserver<EventReceive>() {

            @Override
            public void onNext(EventReceive value) {
                try {
                    String val = (String) Converter.FromByteArray(value.getBody());
                    System.out.printf(
                            "Event Received: EventID: %s, Channel: %s, Metadata: %s, Body: %s%n",
                            value.getEventId(),
                            value.getChannel(),
                            value.getMetadata(),
                            val
                    );

                    String[] msgParts = val.split(":");

                    if (msgParts.length == 5) {
                        if (msgParts[0].equals("STOCK_UPDATE")) {

                            int    productId    = Integer.parseInt(msgParts[1]);
                            String name         = msgParts[2];
                            int    currentStock = Integer.parseInt(msgParts[3]);
                            int    threshold    = Integer.parseInt(msgParts[4]);

                            ProductInfo p = new ProductInfo(productId, name, currentStock, threshold);
                            Product_CRUD.upsertProduct(p);

                            System.out.printf(
                                    ">>> LowStock DB updated: productId=%d, name=%s, stock=%d, threshold=%d%n",
                                    productId, name, currentStock, threshold
                            );
                        }
                    }

                } catch (ClassNotFoundException e) {
                    System.out.printf("ClassNotFoundException: %s%n", e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.printf("Exception in onNext: %s%n", e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.printf("onError: %s%n", t.getMessage());
            }

            @Override
            public void onCompleted() {}
        };

        subscriber.SubscribeToEvents(subscribeRequest, streamObserver);
    }
}