/*
 * Copyright 2014 matthieu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sfr.tv.jms.client;

import java.util.Map;
import net.sfr.tv.exceptions.ResourceInitializerException;
import net.sfr.tv.jms.model.JndiProviderConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author matthieu
 */
public class RunnableJmsClient implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(RunnableJmsClient.class);
    
    /**
     * Monitor pattern
     */
    public final Object monitor = new Object();
    
    private final JmsClient client;
    
    /**
     * @see net.sfr.tv.jms.client.JmsClient
     */
    public RunnableJmsClient(
            JndiProviderConfiguration jndiProviderConfig,
            String preferredServer,
            Boolean isTopicSubscription,
            Boolean isDurableSubscription,
            String clientId,
            String subscriptionBaseName,
            String selector,
            String lifecycleControllerClassName,
            Map<String, String[]> listenerDestinationsMap,
            String cnxFactoryJndiName) throws ResourceInitializerException {
        
        client = new JmsClient(
                jndiProviderConfig,
                preferredServer,
                isTopicSubscription,
                isDurableSubscription,
                clientId,
                subscriptionBaseName,
                selector,
                lifecycleControllerClassName,
                listenerDestinationsMap,
                cnxFactoryJndiName);
    }
    
    /**
     * Monitor pattern
     */
    @Override
    public void run() {

        // ... forever. The thread is waiting for someone to call notify() on the lock object.
        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException ex) {
                LOGGER.warn("Got interrupted !");
            }
        }
    }
    
    public void shutdown() {
        client.shutdown();
    }
}
