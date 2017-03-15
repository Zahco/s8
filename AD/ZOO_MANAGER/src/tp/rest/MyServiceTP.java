package tp.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;

import tp.model.Animal;
import tp.model.AnimalNotFoundException;
import tp.model.Cage;
import tp.model.Center;
import tp.model.Position;

@WebServiceProvider
@ServiceMode(value = Service.Mode.MESSAGE)
public class MyServiceTP implements Provider<Source> {

    public final static String url = "http://127.0.0.1:8084/";

    public static void main(String args[]) {
        Endpoint e = Endpoint.create(HTTPBinding.HTTP_BINDING, new MyServiceTP());

        e.publish(url);
        System.out.println("Service started, listening on " + url);
        // pour arrêter : e.stop();
    }

    private JAXBContext jc;

    @javax.annotation.Resource(type = Object.class)
    protected WebServiceContext wsContext;

    private Center center = new Center(new LinkedList<>(), new Position(49.30494d, 1.2170602d), "Biotropica");

    public MyServiceTP() {
        try {
            jc = JAXBContext.newInstance(Center.class, Cage.class, Animal.class, Position.class);
        } catch (JAXBException je) {
            System.out.println("Exception " + je);
            throw new WebServiceException("Cannot create JAXBContext", je);
        }

        // Fill our center with some animals
        Cage usa = new Cage(
                "usa",
                new Position(49.305d, 1.2157357d),
                25,
                new LinkedList<>(Arrays.asList(
                        new Animal("Tic", "usa", "Chipmunk", UUID.randomUUID()),
                        new Animal("Tac", "usa", "Chipmunk", UUID.randomUUID())
                ))
        );

        Cage amazon = new Cage(
                "amazon",
                new Position(49.305142d, 1.2154067d),
                15,
                new LinkedList<>(Arrays.asList(
                        new Animal("Canine", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("Incisive", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("Molaire", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("De lait", "amazon", "Piranha", UUID.randomUUID())
                ))
        );

        center.getCages().addAll(Arrays.asList(usa, amazon));
    }

    public Source invoke(Source source) {
        MessageContext mc = wsContext.getMessageContext();
        String path = (String) mc.get(MessageContext.PATH_INFO);
        String method = (String) mc.get(MessageContext.HTTP_REQUEST_METHOD);
//        if (source == null) System.out.println("NULL ###");

        // determine the targeted ressource of the call
        try {
            // no target, throw a 404 exception.
            if (path == null) {
                throw new HTTPException(404);
            }
            // "/animals" target - Redirect to the method in charge of managing this sort of call.
            else if (path.startsWith("animals")) {
                String[] path_parts = path.split("/");
                switch (path_parts.length){
                    case 1 :
                        return this.animalsCrud(method, source);
                    case 2 :
                        return this.animalCrud(method, source, path_parts[1]);
                    default:
                        throw new HTTPException(404);
                }
            }
            else if (path.startsWith("find/")) {
                String[] path_parts = path.split("/");
                if (path_parts[1].startsWith("byName")) {
                    return this.findAnimalsByName(method, source, path_parts[2]);
                }
                else if (path_parts[1].startsWith("at")) {
                    return this.findAnimalsByAt(method, source);
                }
                else if (path_parts[1].startsWith("near")) {
                    return this.findAnimalsByNear(method, source);
                }
                else {
                    throw new HTTPException(404);
                }
            }
            else if (path.startsWith("cages")) {
                String[] path_parts = path.split("/");
                switch (path_parts.length) {
                    case 1 :
                        return this.cagesCrud(method, source);
                    case 2 :
                        return this.cageCrud(method, source, path_parts[1]);
                    default:
                        throw new HTTPException(404);
                }
            }
            else if ("coffee".equals(path)) {
                throw new HTTPException(418);
            }
            else {
                throw new HTTPException(404);
            }
        } catch (JAXBException e) {
            throw new HTTPException(500);
        } catch (UnsupportedEncodingException e) {
            throw new HTTPException(500);
        }
    }

    private Source cageCrud(String method, Source source, String cage_id) throws JAXBException {
        if ("GET".equals(method)){
            throw new HTTPException(405);
        }
        else{
            throw new HTTPException(405);
        }
    }

    private Source cagesCrud(String method, Source source) throws JAXBException {
        if ("POST".equals(method)){
            Cage cage = unmarshalCage(source);
            if (cage.getResidents() == null) {
                cage.setResidents(new LinkedList<Animal>());
            }
            this.center.getCages().add(cage);
            return new JAXBSource(this.jc, this.center);
        }
        else{
            throw new HTTPException(405);
        }
    }

    /**
     * Method bound to calls on /animals/{something}
     */
    private Source animalCrud(String method, Source source, String animal_id) throws JAXBException {
        if ("GET".equals(method)){
            try {
                return new JAXBSource(this.jc, center.findAnimalById(UUID.fromString(animal_id)));
            } catch (AnimalNotFoundException e) {
                throw new HTTPException(404);
            }
        }
//        Animal Crée l’animal identifié par { animal_id }
        else if ("POST".equals(method)) {
            Animal animal = unmarshalAnimal(source);
            animal.setId(UUID.fromString(animal_id));
            this.center.getCages()
                    .stream()
                    .filter(cage -> cage.getName().equals(animal.getCage()))
                    .findFirst()
                    .orElseThrow(() -> new HTTPException(404))
                    .getResidents()
                    .add(animal);
            return new JAXBSource(this.jc, this.center);
        }
//        Supprime l’animal identifié par { animal_id }
        else if ("DELETE".equals(method)) {
            this.center.getCages()
                    .forEach(cage -> cage.getResidents().removeIf(animal -> animal.getId() == UUID.fromString(animal_id)));
            return new JAXBSource(this.jc, this.center);
        }
//        Modifie l’animal identifié par { animal_id }
        else if ("PUT".equals(method)) {
            Animal animal = unmarshalAnimal(source);
            //Suppression
            this.center.getCages()
                    .forEach(cage -> cage.getResidents().removeIf(animals -> animals.getId() == UUID.fromString(animal_id)));
            //Création
            this.center.getCages()
                    .stream()
                    .filter(cage -> cage.getName().equals(animal.getCage()))
                    .findFirst()
                    .orElseThrow(() -> new HTTPException(404))
                    .getResidents()
                    .add(animal);
            return new JAXBSource(this.jc, this.center);
        }
        else{
            throw new HTTPException(405);
        }
    }

    /**
     * Method bound to calls on /animals
     */
    private Source animalsCrud(String method, Source source) throws JAXBException {
        //Retourne l'ensemble des animaux du centre
        if ("GET".equals(method)){
            return new JAXBSource(this.jc, this.center);
        }
        //Ajoute un animal dans votre centre
        else if ("POST".equals(method)) {
            Animal animal = unmarshalAnimal(source);
            //print(animal.toString());
            Collection<Animal> ca = this.center.getCages()
                    .stream()
                    .filter(cage -> cage.getName().equals(animal.getCage()))
                    .findFirst()
                    .orElseThrow(() -> new HTTPException(404))
                    .getResidents();
//            if (ca == null) print("null");
//            else print("pas null");
            this.center.getCages()
                    .stream()
                    .filter(cage -> cage.getName().equals(animal.getCage()))
                    .findFirst()
                    .orElseThrow(() -> new HTTPException(404))
                    .getResidents()
                    .add(animal);
            return new JAXBSource(this.jc, this.center);
        }
        //Supprime l'ensemble des animaux
        else if ("DELETE".equals(method)) {
            this.center.getCages().forEach(cage -> cage.getResidents().clear());
            return new JAXBSource(this.jc, this.center);
        }
        //Modifie l'ensemble des animaux
        else if ("PUT".equals(method)) {
            Animal animalSource = unmarshalAnimal(source);
            this.center.getCages().stream().map(cage -> cage.getResidents())
                    .forEach(animals -> animals.forEach(animal -> animal.change(animalSource)));
            return new JAXBSource(this.jc, this.center);
        }
        else {
            throw new HTTPException(405);
        }
    }
    /**
     * Method bound to calls on /find/byName/{name}
     * Cherche et renvoie l'animal par son nom
     */
    private Source findAnimalsByName(String method, Source source, String name) throws JAXBException, UnsupportedEncodingException {
        String nameD = URLDecoder.decode(name, "UTF-8");
        print(nameD);
        if ("GET".equals(method)) {
             Optional<Animal> opA = this.center.getCages().stream()
                     .map(Cage::getResidents)
                     .flatMap(Collection::stream)
                     .filter(animal -> animal.getName().equals(nameD))
                     .findFirst();
             if (opA.isPresent()) {
                 return new JAXBSource(this.jc, opA.get());
             } else {
                 return new JAXBSource(this.jc, this.center);
             }

        }
        else {
            throw new HTTPException(405);
        }
    }
    /**
     * Method bound to calls on /find/At
     * Cherche et renvoie l'animal par sa position
     */
    private Source findAnimalsByAt(String method, Source source) throws JAXBException {
        Position position = this.unmarshalPosition(source);

        if ("POST".equals(method)) {
            Optional<Cage> opC = this.center.getCages().stream()
                    .filter(cage -> cage.getPosition().equals(position))
                    .findFirst();
            if (opC.isPresent()) {
                return new JAXBSource(this.jc, opC.get());
            } else {
                return new JAXBSource(this.jc, this.center);
            }

        }
        else {
            throw new HTTPException(405);
        }
    }

    private Source findAnimalsByNear(String method, Source source) throws JAXBException {
        Position position = this.unmarshalPosition(source);

        if ("POST".equals(method)) {
            Optional<Cage> opC = this.center.getCages().stream()
                    .filter(cage -> cage.getPosition().equals(position))
                    .findFirst();
            if (opC.isPresent()) {
                return new JAXBSource(this.jc, opC.get());
            } else {
                throw new HTTPException(404);
            }

        }
        else {
            throw new HTTPException(405);
        }
    }


    private Animal unmarshalAnimal(Source source) throws JAXBException {
        return (Animal) this.jc.createUnmarshaller().unmarshal(source);
    }

    private Cage unmarshalCage(Source source) throws JAXBException {
        return (Cage) this.jc.createUnmarshaller().unmarshal(source);
    }

    private Position unmarshalPosition(Source source) throws JAXBException {
        return (Position) this.jc.createUnmarshaller().unmarshal(source);
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
