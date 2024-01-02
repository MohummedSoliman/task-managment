package com.mohamed.taskmanagement.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Service
public class GoogleCalendarService {

    private final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final String filePath = "src/main/java/com/mohamed/taskmanagement/service/client_secret.json";
    private final String APPLICATION_NAME = "Task Management";
    private final static String CLIENT_SECRET = "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";
    private static String CLIENT_ID = "702302439254-7odeirdd87gubsj5aeageebninqkce42.apps.googleusercontent.com";

    private static Map<String, String> tokens = new HashMap<>();

    private final Reader reader = new FileReader(filePath);
    private final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);

    @Value("${google.client.redirectUri}")
    private String redirectURI;

    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    GoogleAuthorizationCodeFlow flow;

    public GoogleCalendarService() throws IOException {
    }

    public Calendar getCalendarService(String code) throws IOException, GeneralSecurityException {

        Reader reader = new FileReader(filePath);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singletonList(CalendarScopes.CALENDAR))
                .setAccessType("offline")
                .build();

        TokenResponse response = flow.newTokenRequest(code)
                .setRedirectUri(redirectURI)
                .execute();

        Credential credential = flow.createAndStoreCredential(response, "userID");
        credential.refreshToken();
        tokens.put("accessToken", response.getAccessToken());
        tokens.put("refreshToken", response.getRefreshToken());

        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // Update an existing event in the calendar
    public com.google.api.services.calendar.model.Event updateEvent(String eventId, Event event)
            throws IOException, GeneralSecurityException {
        Calendar service = getCalendarService();
        return service.events().update("primary", eventId, event).execute();
    }

    public com.google.api.services.calendar.model.Event inserEvent(Event event)
            throws IOException, GeneralSecurityException {
        Calendar service = getCalendarService();
        return service.events().insert("primary", event).execute();
    }

    // Delete an event from the calendar
    public void deleteEvent(String eventId) throws IOException {
        String accessToken = tokens.get("accessToken");

        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(request ->
                request.getHeaders().setAuthorization("Bearer " + accessToken)
        );

        GenericUrl url = new GenericUrl("https://www.googleapis.com/calendar/v3/calendars/primary/events/" + eventId);

        HttpRequest request = requestFactory.buildDeleteRequest(url);

        request.execute();
    }

    public String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;

        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR)).build();

        authorizationUrl = flow.newAuthorizationUrl().
                setAccessType("offline").
                setRedirectUri(redirectURI);
        return authorizationUrl.build();
    }

    public Calendar getCalendarService() throws IOException {
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        GoogleCredential credentials = new GoogleCredential().
                setAccessToken(accessToken);
//                setRefreshToken(refreshToken);

        return new Calendar.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public Event getEventForUpdate(String eventId) throws IOException {

        Calendar calendar = getCalendarService();

        Event event = calendar.events().get("primary", eventId).execute();

        return event;
    }

}
