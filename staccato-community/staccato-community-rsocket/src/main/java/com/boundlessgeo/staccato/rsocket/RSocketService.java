package com.boundlessgeo.staccato.rsocket;

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.extern.slf4j.Slf4j;

/**
 * Starts the RSocket service.
 *
 * @author joshfix
 * Created on 9/27/18
 */
@Slf4j
public class RSocketService {

    public RSocketService(RSocketConfigProps configProps, ItemSocketAcceptor itemSocketAcceptor) {
        log.info("Starting RSocket");
        TcpServerTransport tcp = TcpServerTransport.create("0.0.0.0", configProps.getPort());
        RSocketFactory.receive()
                .acceptor(itemSocketAcceptor)
                .transport(tcp)
                .start().log()
                .subscribe(channel -> log.info("RSocket initialized on port " + configProps.getPort()));
    }

}




