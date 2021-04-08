package es.socialmoney.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import es.socialmoney.rest.Resources;

@ApplicationPath("rest")

public class App extends ResourceConfig {

        public App() {

                packages("es.socialmoney.rest");

        }

}