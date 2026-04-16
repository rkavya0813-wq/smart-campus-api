package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import com.smartcampus.config.ApplicationConfig;

import java.net.URI;

public class Main {

    private static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static void main(String[] args) {
        try {
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                    URI.create(BASE_URI),
                    new ApplicationConfig()
            );

            System.out.println("Smart Campus API server started.");
            System.out.println("Server running at: http://localhost:8080/");
            System.out.println("Press Enter to stop the server...");

            System.in.read();

            server.shutdownNow();
            System.out.println("Server stopped.");
        } catch (Exception e) {
            System.err.println("Failed to start server");
            e.printStackTrace();
        }
    }
}
