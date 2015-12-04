package edu.gatech.cc.lostandfound.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.BadRequestException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import edu.gatech.cc.lostandfound.api.model.LostReport;
import edu.gatech.cc.lostandfound.api.model.Token;

/**
 * Created by mkatri on 12/3/15.
 */


@Api(
        name = "lostAndFound",
        version = "v1",
        resource = "notifications",
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
public class NotificationEndpoint {

        @ApiMethod(
                name = "notification.registerToken",
                path = "token",
                httpMethod = ApiMethod.HttpMethod.POST)
        public void registerToken(Token token, User user) throws
                OAuthRequestException, BadRequestException {
                //todo implement
        }
}
