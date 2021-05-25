codehub.sva.deckage org.book;

import org.book.model.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/library")
public class LibraryResource {
    private List<Book> library = new ArrayList();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibrary() {
        return Response.ok(library).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    public Integer countBooks () {
        return  library.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBook (Book newBook) {
        library.add(newBook);
        return Response.ok(library).build();
    }

    @PUT
    @Path("{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook (
            @PathParam("id") Long id,
            @PathParam("title") String title) {
        library = library.stream().map(book -> {
            if(book.getId().equals(id)) {
                book.setTitle(title);
            }
            return book;
        }).collect(Collectors.toList());
        return Response.ok(library).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteBook (
            @PathParam("id") Long id){
        Optional<Book> bookToDelete = library.stream().filter(book -> book.getId().equals(id)).findFirst();
        boolean removed = false;
        if(bookToDelete.isPresent()){
            removed = library.remove(bookToDelete.get());
        }
        if(removed) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}

