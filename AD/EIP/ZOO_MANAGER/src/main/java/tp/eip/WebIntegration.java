package tp.eip;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import tp.model.Animal;

import javax.xml.bind.JAXBContext;
import java.util.*;

/**
 * Created by geoffrey on 28/04/17.
 */
public class WebIntegration {

    public static void main(String[] args) throws Exception {

        // Configure le logger par défaut
        BasicConfigurator.configure();

        // Contexte Camel par défaut
        CamelContext context = new DefaultCamelContext();

        // Recherche d'un animal par son nom
        RouteBuilder routeBuilderSwitch = new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                // Start MyServiceController before
                from("direct:TestWeb")
                        .setHeader(Exchange.HTTP_METHOD,constant("POST"))
                        .to("http://127.0.0.1:8080/animals/findByName/")
                        .unmarshal().json(JsonLibrary.Jackson)
                        .setBody(simple("${body[cage]}"))
                        .to("http://127.0.0.1:8080/cage/findByName/")
                        .unmarshal().json(JsonLibrary.Jackson)
                        .setHeader("longitude", simple("${body[position][longitude]}"))
                        .setHeader("latitude", simple("${body[position][latitude]}"))
                        .log("reponse received : ${body}\n ${header.latitude} ${header.longitude}");
            }
        };
        routeBuilderSwitch.addRoutesToCamelContext(context);


        // On démarre le contexte pour activer les routes
        context.start(); 

        // On crée un producteur
        ProducerTemplate pt = context.createProducerTemplate();

        String message = "Canine";
        pt.sendBody("direct:TestWeb", message);
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            // Enter the name of an animal or close to exit
//            //message = scanner.nextLine();
//            if (message.equals("close")) break;
//
//        }
    }

}