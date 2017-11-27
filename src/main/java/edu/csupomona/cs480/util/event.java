package edu.csupomona.cs480.util;



import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttachment;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.Calendar;


import edu.csupomona.cs480.rest.utils.RestUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;


//event structured to follow google calendar API format
public class event 
{
	private static final Logger LOG = LoggerFactory.getLogger(event.class);
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    public static final SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	//dateTime in timeZone format. reference here: https://tools.ietf.org/html/rfc3339
	// example: 2017-11-10T16:30:00-08:00
	private String startDateTime;
	private String endDateTime;
	//description of event in String format
	private String description;
	//Title of event 
	private String summaryTitle;
	//color of event displayed in google calendar
	// ranges from [1-11]
	private String colorId;
	//location of event
	private String location;
	//organizer's display name
	private String orgName;
	//organizer's email address (specific to each club)
	private String orgEmail;
	//kind of event
	private String kind = "calendar#event";
	//creators name
	private String creatorName;
	//creator's email
	private String creatorEmail;
	
	private OAuth2RestTemplate oAuth2RestTemplate;
	//constructor
	public event(String startTime, String endTime, String descript, String summary, String color,
			String address, String orgN, String orgE)
	{
		startDateTime = startTime;
		endDateTime = endTime;
		description = descript;
		summaryTitle = summary;
		colorId = color;
		location = address;
		orgName = orgN;
		orgEmail = orgE;
	}
	
	/**
	 * 
	 * @return a valid Event
	 */
	   public Event makeEvent() {
		   	DateTime startT = new DateTime(getStartDateTime());
			DateTime endT = new DateTime(getEndDateTime());
	        Event Aevent = new Event()
	                .setSummary(getSummaryTitle())
	                .setLocation(getLocation())
	                .setDescription(getDescription())
	                .setColorId(getColorId())
	                .setKind(getKind())
	                .setStart(new EventDateTime().setDateTime(startT))
	                .setEnd(new EventDateTime().setDateTime(endT))
	                .setOrganizer(new Event.Organizer().setDisplayName(getOrgName()));
	        Aevent.setFactory(JSON_FACTORY);
	     LOG.info("hit the prod");
	     return Aevent;
	    }

	



	   /**
	    ** @return string representation of post output
	    **/
	    public String createEvent() throws IOException {

	        String res = "nothing happened";
	        String urlBase = "https://www.googleapis.com/calendar/v3";
	        URI url = null;

	        String uriString = urlBase + "/calendars/primary/events?";
	        uriString = uriString + "supportsAttachments=true";
	        uriString = uriString + "&sendNotifications=true";
	        try {
	            url = new URI(uriString);
	        } catch (URISyntaxException ex) {
	            LOG.error("could not create uri " + uriString);
	            res = "could not create uri " + uriString;
	        }
	         
	            Event evs = makeEvent();
	            String input = evs.toPrettyString();
	            //  LOG.debug(input);

	            HttpHeaders headers = new HttpHeaders();
	            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            HttpEntity<String> infoEntity = new HttpEntity<String>(input, headers);
	            LOG.info("temp "+getoAuth2RestTemplate());
	            ResponseEntity<String> responseOut
	                    = getoAuth2RestTemplate().exchange(url,
	                            HttpMethod.POST, infoEntity, String.class);
	            res = responseOut.getBody();

	            if (RestUtil.isError(responseOut.getStatusCode())) {

	                LOG.error("res is " + res);
	            }

	        return res;
	    }
	    
	
	public event(OAuth2RestTemplate t) {
		setoAuth2RestTemplate(t);
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSummaryTitle() {
		return summaryTitle;
	}

	public void setSummaryTitle(String summaryTitle) {
		this.summaryTitle = summaryTitle;
	}

	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgEmail() {
		return orgEmail;
	}

	public void setOrgEmail(String orgEmail) {
		this.orgEmail = orgEmail;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}
	public OAuth2RestTemplate getoAuth2RestTemplate() {
		return oAuth2RestTemplate;
	}
	public void setoAuth2RestTemplate(OAuth2RestTemplate oAuth2RestTemplate) {
		this.oAuth2RestTemplate = oAuth2RestTemplate;
	}
	
}