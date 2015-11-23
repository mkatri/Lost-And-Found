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

import edu.gatech.cc.lostandfound.api.model.LostReport;

import static com.googlecode.objectify.ObjectifyService.ofy;

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
public class LostReportEndpoint {

    private static final Logger logger = Logger.getLogger(LostReportEndpoint
            .class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper.
        // See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(LostReport.class);
    }

    /**
     * Returns the {@link LostReport} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code LostReport} with the
     *                           provided ID.
     */
    @ApiMethod(
            name = "lostReport.get",
            path = "lostReport/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public LostReport get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting LostReport with ID: " + id);
        LostReport lostReport = ofy().load().type(LostReport.class).id(id)
                .now();
        if (lostReport == null) {
            throw new NotFoundException("Could not find LostReport with ID: "
                    + id);
        }
        return lostReport;
    }

    /**
     * Inserts a new {@code LostReport}.
     */
    @ApiMethod(
            name = "lostReport.insert",
            path = "lostReport",
            httpMethod = ApiMethod.HttpMethod.POST)
    public LostReport insert(LostReport lostReport, User user) throws
            OAuthRequestException, BadRequestException {
        if (user == null) {
            logger.exiting(LostReportEndpoint.class.toString(), "Not logged " +
                    "in.");
            throw new OAuthRequestException("You need to login to file " +
                    "reports.");
        }
        if (lostReport.getId() != null) {
            throw new BadRequestException("Invalid report object.");
        }
        logger.info("For user: " + user.getEmail());
        lostReport.setUserId(user.getUserId());
        lostReport.setUserNickname(user.getNickname());
        ofy().save().entity(lostReport).now();
        logger.info("Created LostReport.");
        return ofy().load().entity(lostReport).now();
    }

    /**
     * Updates an existing {@code LostReport}.
     *
     * @param id         the ID of the entity to be updated
     * @param lostReport the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an
     *                           existing
     *                           {@code LostReport}
     */
    @ApiMethod(
            name = "lostReport.update",
            path = "lostReport/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public LostReport update(@Named("id") Long id, LostReport
            lostReport, User user) throws NotFoundException,
            BadRequestException, OAuthRequestException {
        if (user == null) {
            throw new OAuthRequestException("You need to login to modify " +
                    "reports.");
        }
        if (id != lostReport.getId()) {
            throw new BadRequestException("Invalid report object.");
        }
        checkOwns(id, user);
        ofy().save().entity(lostReport).now();
        logger.info("Updated LostReport: " + lostReport);
        return ofy().load().entity(lostReport).now();
    }

    /**
     * Deletes the specified {@code LostReport}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an
     *                           existing
     *                           {@code LostReport}
     */
    @ApiMethod(
            name = "lostReport.remove",
            path = "lostReport/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id, User user) throws
            NotFoundException, OAuthRequestException {
        if (user == null) {
            throw new OAuthRequestException("You need to login to modify " +
                    "reports.");
        }
        logger.info("For user: " + user.getEmail());
        checkOwns(id, user);
        ofy().delete().type(LostReport.class).id(id).now();
        logger.info("Deleted LostReport with ID: " + id);
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
            name = "lostReport.list",
            path = "lostReport",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<LostReport> list(@Nullable @Named("cursor")
                                               String cursor, @Nullable
                                               @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<LostReport> query = ofy().load().type(LostReport.class).limit
                (limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<LostReport> queryIterator = query.iterator();
        List<LostReport> lostReportList = new ArrayList<LostReport>(limit);
        while (queryIterator.hasNext()) {
            lostReportList.add(queryIterator.next());
        }
        return CollectionResponse.<LostReport>builder().setItems
                (lostReportList).setNextPageToken(queryIterator.getCursor()
                .toWebSafeString()).build();
    }

    private void checkOwns(Long id, User user) throws NotFoundException,
            OAuthRequestException {
        try {
            LostReport report = ofy().load().type(LostReport.class).id(id)
                    .safe();
            if (!report.getId().equals(user.getUserId())) {
                throw new OAuthRequestException("You do not have the " +
                        "premission to modify this report.");
            }
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find LostReport with ID: "
                    + id);
        }
    }
}