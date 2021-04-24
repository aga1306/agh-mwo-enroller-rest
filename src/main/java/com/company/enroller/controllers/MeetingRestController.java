package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController // obsluga zadan do serwera
@RequestMapping("/meetings")
public class MeetingRestController {
	
	@Autowired
	MeetingService meetingService;

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	// GET //http://localhost:8080/meetings
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	// GET //http://localhost:8080/participants/meetings/2
	public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	// POST //http://localhost:8080/meetings/2/participants
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
		if (meetingService.findById(meeting.getId()) != null) {
			return new ResponseEntity(
					"Unable to create. A meeting with id " + meeting.getId() + "already exist",
					HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);

	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	// DELETE //http://localhost:8080/meetings/2
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meetingService.delete(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
		
	}
	
	// PUT //http://localhost:8080/meetings/2
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMeeting(@PathVariable("id") Long id, @RequestBody Meeting meeting) {
		Meeting foundMeeting = meetingService.findById(id);
		if (foundMeeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		foundMeeting.setDate(meeting.getDate());
		foundMeeting.setTitle(meeting.getTitle());
		foundMeeting.setDescription(meeting.getDescription());
		meetingService.update(foundMeeting);
		return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "{id}/participants", method = RequestMethod.POST)
	// POST //http://localhost:8080/meetings/2/participants
	public ResponseEntity<?> addMeetingParticipant(@PathVariable("id") Long id, @RequestBody Map<String, String> json) {

		Meeting foundMeeting = meetingService.findById(id);
		String login = json.get("login");
		if (login == null) {
			return new ResponseEntity<String>("Unable to find participant's login",
					HttpStatus.BAD_REQUEST);
		}
		Participant requiredParticipant = participantService.findByLogin(login);
		foundMeeting.addParticipant(requiredParticipant);
		meetingService.update(foundMeeting);
		return new ResponseEntity<Collection<Participant>>(foundMeeting.getParticipants(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "{id}/participants", method = RequestMethod.GET)
	// GET //http://localhost:8080/meetings/2/participants
	public ResponseEntity<?> getParticipants(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "{id}/participants/{login}", method = RequestMethod.DELETE)
	// DELETE //http://localhost:8080/meetings/2/participants/login
	public ResponseEntity<?> removeParticipant(@PathVariable("id") Long id, @PathVariable("login") String login) {
		Meeting meeting = meetingService.findById(id);
		Participant participant = participantService.findByLogin(login);
		meeting.removeParticipant(participant);
		meetingService.update(meeting);
		return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
	}


	// POST /http://localhost:8080/participants/meetings/2/participants
	// znajdz spotkanie
	// znajdz uczestnika w systemie
	

	
	//meeting.addParticipant(participant);
	//meeting.update(meeting);
	// HttpStatus.OK, 200 + dane, 200 + sam uczestnik? ok to 200 spokojnie styknie samo 200
	
	
//	Wersja PREMIUM (dodatkowo do GOLD)
//	Sortowanie listy spotkań po tytule spotkania
//	Przeszukiwanie listy spotkań po tytule i opisie (na zasadzie substring)
//	Przeszukiwanie listy spotkań po zapisanym uczestniku spotkania
//	pathwariable @RequestParam(value="title", defaultValue"", ) string title, @PathVariable("id") Long id)
//GET //http://localhost:8080/participants/meetings/2?sort=title&?title=Przeminelo&description=dfsfsf
}
