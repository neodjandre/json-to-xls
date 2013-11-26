package io.ei.jsontoxls.resources;

import io.ei.jsontoxls.Messages;
import io.ei.jsontoxls.repository.TemplateRepository;
import io.ei.jsontoxls.util.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static java.text.MessageFormat.format;
import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

@Path("/templates")
public class TemplateResource {
    Logger logger = LoggerFactory.getLogger(TemplateResource.class);
    private TemplateRepository templateRepository;

    public TemplateResource(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.TEXT_PLAIN)
    public Response save(byte[] templateData) {
        try {
            if (templateData == null || templateData.length == 0) {
                return ResponseFactory.badRequest(Messages.EMPTY_TEMPLATE);
            }

            String token = UUID.randomUUID().toString();
            templateRepository.add(token, templateData);
            logger.info(format("Saved template with {0} number of bytes. Token: {1}", templateData.length, token));
            return Response.status(Response.Status.CREATED).entity(token).type(MediaType.TEXT_PLAIN).build();
        } catch (Exception e) {
            logger.error(format("Unable to save template. Exception Message: {0}. Stack trace: {1}.", e.getMessage(),
                    getFullStackTrace(e)));
            return ResponseFactory.internalServerError(Messages.UNABLE_TO_SAVE_TEMPLATE);
        }
    }
}
