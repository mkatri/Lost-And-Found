package edu.gatech.cc.lostandfound.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import edu.gatech.cc.lostandfound.api.model.FoundReport;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for
 * using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides
 * no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real
 * users.
 */
@Api(
        name = "lostAndFound",
        version = "v1",
        resource = "reports",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
                com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE},
        namespace = @ApiNamespace(
                ownerDomain = "api.lostandfound.cc.gatech.edu",
                ownerName = "api.lostandfound.cc.gatech.edu",
                packagePath = ""
        )
)
public class FoundReportEndpoint {

    private static final Logger logger = Logger.getLogger(FoundReportEndpoint
            .class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper.
        // See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(FoundReport.class);
    }

    /**
     * Returns the {@link FoundReport} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code FoundReport} with the
     *                           provided ID.
     */
    @ApiMethod(
            name = "foundReport.get",
            path = "foundReport/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public FoundReport get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting FoundReport with ID: " + id);
        FoundReport foundReport = ofy().load().type(FoundReport.class).id(id)
                .now();
        if (foundReport == null) {
            throw new NotFoundException("Could not find FoundReport with ID: " +
                    "" + id);
        }
        return foundReport;
    }

    /**
     * Inserts a new {@code FoundReport}.
     */
    @ApiMethod(
            name = "foundReport.insert",
            path = "foundReport",
            httpMethod = ApiMethod.HttpMethod.POST)
    public FoundReport insert(FoundReport foundReport, User user) throws
            OAuthRequestException, BadRequestException {
        if (user == null) {
            logger.exiting(LostReportEndpoint.class.toString(), "Not logged " +
                    "in.");
            throw new OAuthRequestException("You need to login to file " +
                    "reports.");
        }
        if (foundReport.getId() != null) {
            throw new BadRequestException("Invalid report object.");
        }
        logger.info("For user: " + user.getEmail());
        foundReport.setUserId(user.getUserId());
        foundReport.setUserNickname(user.getNickname());
        ofy().save().entity(foundReport).now();
        logger.info("Created FoundReport with ID: " + foundReport.getId());

        return ofy().load().entity(foundReport).now();
    }

    /**
     * Updates an existing {@code FoundReport}.
     *
     * @param id          the ID of the entity to be updated
     * @param foundReport the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an
     *                           existing
     *                           {@code FoundReport}
     */
    @ApiMethod(
            name = "foundReport.update",
            path = "foundReport/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public FoundReport update(@Named("id") Long id, FoundReport foundReport)
            throws NotFoundException {
        // TODO: You should validate your ID parameter against your
        // resource's ID here.
        checkExists(id);
        ofy().save().entity(foundReport).now();
        logger.info("Updated FoundReport: " + foundReport);
        return ofy().load().entity(foundReport).now();
    }

    /**
     * Deletes the specified {@code FoundReport}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an
     *                           existing
     *                           {@code FoundReport}
     */
    @ApiMethod(
            name = "foundReport.remove",
            path = "foundReport/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws
            NotFoundException {
        checkExists(id);
        ofy().delete().type(FoundReport.class).id(id).now();
        logger.info("Deleted FoundReport with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page
     * token/cursor
     */
    @ApiMethod(
            name = "foundReport.list",
            path = "foundReport",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<FoundReport> list(@Nullable @Named("cursor")
                                                String cursor, @Nullable
                                                @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<FoundReport> query = ofy().load().type(FoundReport.class).limit
                (limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<FoundReport> queryIterator = query.iterator();
        List<FoundReport> foundReportList = new ArrayList<FoundReport>(limit);
        while (queryIterator.hasNext()) {
            foundReportList.add(queryIterator.next());
        }
        return CollectionResponse.<FoundReport>builder().setItems
                (foundReportList).setNextPageToken(queryIterator.getCursor()
                .toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(FoundReport.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find FoundReport with ID: " +
                    "" + id);
        }
    }
}