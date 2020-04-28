package org.kerminator.portti;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import zipkin2.Span;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.xray_udp.XRayUDPReporter;

@Component
public class TracingConfiguration {
    @Value("${aws.xray.daemon.address:localhost:2000}")
    private String daemonAddress;

    @Bean
    Reporter<Span> xrayReporter() {
        return XRayUDPReporter.create(daemonAddress);
    }
}