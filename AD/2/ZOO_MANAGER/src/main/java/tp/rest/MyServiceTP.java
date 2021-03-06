package tp.rest;

import tp.model.*;

import javax.ws.rs.*;
import javax.xml.bind.JAXBException;
import javax.xml.ws.http.HTTPException;
import java.sql.SQLException;
import java.util.*;

@Path("/zoo-manager/")
public class MyServiceTP {

    private Center center = new Center(new LinkedList<>(), new Position(49.30494d, 1.2170602d), "Biotropica");
    private MyServiceTPDAO myServiceTPDAO;

    public MyServiceTP() throws Exception {
        // Fill our center with some animals
//        Cage usa = new Cage(
//                "usa",
//                new Position(49.305d, 1.2157357d),
//                25,
//                new LinkedList<>(Arrays.asList(
//                        new Animal("Tic", "usa", "Chipmunk", UUID.randomUUID()),
//                        new Animal("Tac", "usa", "Chipmunk", UUID.randomUUID())
//                ))
//        );
//
//        Cage amazon = new Cage(
//                "amazon",
//                new Position(49.305142d, 1.2154067d),
//                15,
//                new LinkedList<>(Arrays.asList(
//                        new Animal("Canine", "amazon", "Piranha", UUID.randomUUID()),
//                        new Animal("Incisive", "amazon", "Piranha", UUID.randomUUID()),
//                        new Animal("Molaire", "amazon", "Piranha", UUID.randomUUID()),
//                        new Animal("De lait", "amazon", "Piranha", UUID.randomUUID())
//                ))
//        );
//
//        Cage usa = new Cage(
//                "usa",
//                new Position(49.305d, 1.2157357d),
//                25,
//                new LinkedList<>()
//        );
//
//        center.getCages().addAll(Arrays.asList(usa, amazon));
//        center.getCages().addAll(Arrays.asList(usa));
        try {
            myServiceTPDAO = new MyServiceTPDAO();
            myServiceTPDAO.addAnimal(new Animal("Tic", "usa", "Chipmunk", UUID.randomUUID()));
        } catch (Exception e) {
            System.out.println("*** [ERROR] SQL FAILED:");
            e.printStackTrace();
        }

    }

    /**
     * GET method bound to calls on /animals/{something}
     */
    @GET
    @Path("/animals/{id}/")
    @Produces("application/xml")
    public Animal getAnimal(@PathParam("id") String animal_id) throws JAXBException {
        try {
            return center.findAnimalById(UUID.fromString(animal_id));
        } catch (AnimalNotFoundException e) {
            throw new HTTPException(404);
        }
    }

    /**
     * GET method bound to calls on /animals
     */
    @GET
    @Path("/animals/")
    @Produces("application/xml")
    public List<Animal> getAnimals() throws SQLException {
        return myServiceTPDAO.selectAnimal();
    }

    /**
     * POST method bound to calls on /animals
     */
    @POST
    @Path("/animals/")
    @Consumes({"application/xml", "application/json" })
    public List<Animal>  postAnimals(Animal animal) throws SQLException {
        myServiceTPDAO.addAnimal(animal);
        return myServiceTPDAO.selectAnimal();
    }
    /**
     * DELETE method bound to calls on /animals
     */
    @DELETE
    @Path("/animals/")
    @Produces("application/xml")
    public List<Animal> deleteAnimals() throws SQLException {
        myServiceTPDAO.deleteAnimals();
        return myServiceTPDAO.selectAnimal();
    }
    /**
     * PUT method bound to calls on /animals
     */
    @PUT
    @Path("/animals/")
    @Consumes({"application/xml", "application/json" })
    public Center updateAnimals(Animal animalSource) throws JAXBException {
        this.center.getCages().stream().map(cage -> cage.getResidents())
                .forEach(animals -> animals.forEach(animal -> animal.change(animalSource)));
        return this.center;
    }

    /**
     * POST method bound to calls on /cages
     */
    @POST
    @Path("/cages/")
    @Consumes({"application/xml", "application/json" })
    public Center postCages(Cage cage) {
        if (cage.getResidents() == null) {
            cage.setResidents(new LinkedList<>());
        }
        this.center.getCages().add(cage);
        return this.center;
    }

    /**
     * POST method bound to calls on /findAnimalsAt
     */
    @POST
    @Path("/find/at/")
    @Consumes({"application/xml", "application/json" })
    public Cage findAnimalsAt(Position position) {
        Optional<Cage> opC = this.center.getCages().stream()
                .filter(cage -> cage.getPosition().equals(position))
                .findFirst();
        if (opC.isPresent()) {
            return opC.get();
        } else {
            throw new HTTPException(404);
        }
    }

    /**
     * POST method bound to calls on /findAnimalsNear
     */
    @POST
    @Path("/find/near/")
    @Consumes({"application/xml", "application/json" })
    public Cage findAnimalsNear(Position position) {
        return findAnimalsAt(position);
    }


}
