package com.geekhub.secretaryhelperapp.eventattendee.service;

import com.geekhub.secretaryhelperapp.eventattendee.model.EventAttendeeRelation;
import com.geekhub.secretaryhelperapp.eventattendee.repository.EventAttendeeRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventAttendeeRelationService {

	private final EventAttendeeRelationRepository relationRepository;

	@Autowired
	public EventAttendeeRelationService(final EventAttendeeRelationRepository relationRepository) {
		this.relationRepository = relationRepository;
	}

	public List<EventAttendeeRelation> getRelationsByEventId(long id) {
		return relationRepository.getRelationsByEventId(id);
	}

	public List<EventAttendeeRelation> getRelationsByAttendeeId(long id) {
		return relationRepository.getRelationsByAttendeeId(id);
	}

	@Transactional
	public void saveRelations(List<EventAttendeeRelation> relations) {
		for (final var relation : relations) {
			relationRepository.saveRelation(relation);
		}
	}

	@Transactional
	public boolean deleteRelationsByEventId(long id) {
		return relationRepository.deleteRelationsByEventId(id);
	}

}
