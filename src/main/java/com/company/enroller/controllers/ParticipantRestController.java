package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController // obsluga zadan do serwera
@RequestMapping("/participants")
//http://localhost:8080/participants
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;


	@RequestMapping(value = "", method = RequestMethod.GET)
	// GET //http://localhost:8080/participants
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	// GET //http://localhost:8080/participants/user2
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	// POST //http://localhost:8080/participants
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity(
					"Unable to create. A participant with login " + participant.getLogin() + "already exist",
					HttpStatus.CONFLICT);
		}
		participantService.add(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);

	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	// DELETE //http://localhost:8080/participants/user2
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participantService.delete(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
		
	}
	
	// PUT //http://localhost:8080/participants/user2
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(login);
		if (foundParticipant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		foundParticipant.setPassword(participant.getPassword());;
		participantService.update(foundParticipant);
		return new ResponseEntity<Participant>(foundParticipant, HttpStatus.OK);
		
}

}
