package lowstock.lowstockservice.endpoint;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import lowstock.lowstockservice.business.LowStockSyncService;
import lowstock.lowstockservice.business.Messaging;

@WebListener
public class AppStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println(">>> LOW STOCK SERVICE STARTING - AUTO SYNC INIT");

        // One-time full sync with InventoryService on startup
        LowStockSyncService.syncAllProducts();

        System.out.println(">>> AUTO SYNC COMPLETE");

        // Start KubeMQ subscriber in a background thread so the service
        // keeps its database up to date whenever InventoryService publishes
        // a STOCK_UPDATE event (sale recorded or stock manually adjusted).
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Messaging.Receiving_Events_Store("stock_update_channel");
                } catch (javax.net.ssl.SSLException e) {
                    Logger.getLogger(AppStartupListener.class.getName()).log(Level.SEVERE, null, e);
                } catch (io.kubemq.sdk.basic.ServerAddressNotSuppliedException e) {
                    Logger.getLogger(AppStartupListener.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        };

        new Thread(r).start();

        System.out.println(">>> KUBEMQ SUBSCRIBER THREAD STARTED");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(">>> LOW STOCK SERVICE STOPPED");
    }
}