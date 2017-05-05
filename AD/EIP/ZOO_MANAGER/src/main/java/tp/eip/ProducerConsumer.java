package eip;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.BasicConfigurator;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ProducerConsumer {
    public static void main(String[] args) throws Exception {
        // Configure le logger par défaut
        BasicConfigurator.configure();

        // Contexte Camel par défaut
        CamelContext context = new DefaultCamelContext();

        // Création des consumers simples
        addBasicConsumer(context, "direct:consumer-1", "log:affiche-1-log");
        addBasicConsumer(context, "direct:consumer-2", "file:messages");

        // Création du switch
        RouteBuilder routeBuilderSwitch = new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:consumer-all")
                        .choice()
                        .when(header("destinataire").contains("écrire"))
                        .to("direct:consumer-2")
                        .otherwise()
                        .to("direct:consumer-1");
            }
        };
        routeBuilderSwitch.addRoutesToCamelContext(context);


        // On démarre le contexte pour activer les routes
        context.start();

        // On crée un producteur
        ProducerTemplate pt = context.createProducerTemplate();

        String message = "";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            message = scanner.nextLine();
            if (message.equals("close")) break;
            Map headers = new HashMap();
            // Si le message commence par w , on ajoute un header pour rediriger la requête
            if (message.charAt(0) == 'w') {
                headers.put("destinataire", "écrire");
            }
            pt.sendBodyAndHeaders("direct:consumer-all", message, headers);
        }
    }

    //créer un consommer simple et l'ajout au context
    private static void addBasicConsumer(CamelContext context, String uri, String to) throws Exception {
        final String _uri = uri;
        final String _to = to;
        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                // On définit un consommateur 'consumer-1'
                // qui va écrire le message
                from(_uri).to(_to);
            }
        };

        // On ajoute la route au contexte
        routeBuilder.addRoutesToCamelContext(context);
    }
}