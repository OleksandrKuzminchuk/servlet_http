package com.sasha.servletapi.service.impl;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.Event;
import com.sasha.servletapi.util.UtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.sasha.servletapi.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(PER_METHOD)
class EventServiceImplTest extends UtilService {

    @Test
    void shouldSaveEvent() {
        when(userRepository.findById(eventWithUserFile.getUser().getId())).thenReturn(eventWithUserFile.getUser());
        when(fileRepository.findById(eventWithUserFile.getFile().getId())).thenReturn(eventWithUserFile.getFile());
        when(eventRepository.save(eventWithUserFile)).thenReturn(expectedEventWithIdUserFile);

        Event savedEvent = eventService.save(eventWithUserFile);

        assertNotNull(savedEvent);
        assertEquals(expectedEventWithIdUserFile, savedEvent);
        assertEquals(TEST_NUMBER_1, savedEvent.getId());

        verify(eventRepository, times(TEST_NUMBER_1)).save(eventWithUserFile);
        verify(userRepository, times(TEST_NUMBER_1)).findById(eventWithUserFile.getUser().getId());
        verify(fileRepository, times(TEST_NUMBER_1)).findById(eventWithUserFile.getFile().getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSaveEventAndUserNotExists() {
        when(fileRepository.findById(eventWithUserFile.getFile().getId())).thenReturn(eventWithUserFile.getFile());
        when(userRepository.findById(eventWithUserFile.getUser().getId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> eventService.save(eventWithUserFile));

        verify(eventRepository, times(TEST_NUMBER_0)).save(eventWithUserFile);
        verify(userRepository, times(TEST_NUMBER_1)).findById(eventWithUserFile.getUser().getId());
        verify(fileRepository, times(TEST_NUMBER_1)).findById(eventWithUserFile.getFile().getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSaveEventAndFileNotExists() {
        when(fileRepository.findById(eventWithUserFile.getFile().getId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> eventService.save(eventWithUserFile));

        verify(eventRepository, times(TEST_NUMBER_0)).save(eventWithUserFile);
        verify(userRepository, times(TEST_NUMBER_0)).findById(eventWithUserFile.getUser().getId());
        verify(fileRepository, times(TEST_NUMBER_1)).findById(eventWithUserFile.getFile().getId());
    }

    @Test
    void shouldUpdateEvent() {
        when(eventRepository.findById(newExpectedEventAfterUpdate.getId())).thenReturn(newExpectedEventAfterUpdate);
        when(fileRepository.findById(newExpectedEventAfterUpdate.getFile().getId())).thenReturn(newExpectedEventAfterUpdate.getFile());
        when(userRepository.findById(newExpectedEventAfterUpdate.getUser().getId())).thenReturn(newExpectedEventAfterUpdate.getUser());
        when(eventRepository.update(newExpectedEventAfterUpdate)).thenReturn(newExpectedEventAfterUpdate);

        Event updatedEvent = eventService.update(newExpectedEventAfterUpdate);

        assertNotNull(updatedEvent);
        assertEquals(newExpectedEventAfterUpdate, updatedEvent);
        assertEquals(newExpectedEventAfterUpdate.getFile(), updatedEvent.getFile());
        assertEquals(newExpectedEventAfterUpdate.getUser(), updatedEvent.getUser());

        verify(eventRepository, times(TEST_NUMBER_1)).findById(newExpectedEventAfterUpdate.getId());
        verify(fileRepository, times(TEST_NUMBER_1)).findById(newExpectedEventAfterUpdate.getFile().getId());
        verify(userRepository, times(TEST_NUMBER_1)).findById(newExpectedEventAfterUpdate.getUser().getId());
        verify(eventRepository, times(TEST_NUMBER_1)).update(newExpectedEventAfterUpdate);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdateEventAndEventNotExists() {
        when(eventRepository.findById(newExpectedEventAfterUpdate.getId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> eventService.update(newExpectedEventAfterUpdate));

        verify(eventRepository, times(TEST_NUMBER_1)).findById(newExpectedEventAfterUpdate.getId());
        verify(fileRepository, times(TEST_NUMBER_0)).findById(newExpectedEventAfterUpdate.getFile().getId());
        verify(userRepository, times(TEST_NUMBER_0)).findById(newExpectedEventAfterUpdate.getUser().getId());
        verify(eventRepository, times(TEST_NUMBER_0)).update(newExpectedEventAfterUpdate);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdateEventAndFileNotExists() {
        when(eventRepository.findById(newExpectedEventAfterUpdate.getId())).thenReturn(newExpectedEventAfterUpdate);
        when(fileRepository.findById(newExpectedEventAfterUpdate.getFile().getId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> eventService.update(newExpectedEventAfterUpdate));

        verify(eventRepository, times(TEST_NUMBER_1)).findById(newExpectedEventAfterUpdate.getId());
        verify(fileRepository, times(TEST_NUMBER_1)).findById(newExpectedEventAfterUpdate.getFile().getId());
        verify(userRepository, times(TEST_NUMBER_0)).findById(newExpectedEventAfterUpdate.getUser().getId());
        verify(eventRepository, times(TEST_NUMBER_0)).update(newExpectedEventAfterUpdate);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdateEventAndUserNotExists() {
        when(eventRepository.findById(newExpectedEventAfterUpdate.getId())).thenReturn(newExpectedEventAfterUpdate);
        when(fileRepository.findById(newExpectedEventAfterUpdate.getFile().getId())).thenReturn(newExpectedEventAfterUpdate.getFile());
        when(userRepository.findById(newExpectedEventAfterUpdate.getUser().getId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> eventService.update(newExpectedEventAfterUpdate));

        verify(eventRepository, times(TEST_NUMBER_1)).findById(newExpectedEventAfterUpdate.getId());
        verify(fileRepository, times(TEST_NUMBER_1)).findById(newExpectedEventAfterUpdate.getFile().getId());
        verify(userRepository, times(TEST_NUMBER_1)).findById(newExpectedEventAfterUpdate.getUser().getId());
        verify(eventRepository, times(TEST_NUMBER_0)).update(newExpectedEventAfterUpdate);
    }

    @Test
    void shouldFindEventById() {
        when(eventRepository.findById(eventId)).thenReturn(eventWithUserFile);

        Event foundEvent = eventService.findById(eventId);

        assertNotNull(foundEvent);
        assertEquals(eventWithUserFile, foundEvent);
        assertEquals(eventWithUserFile.getUser(), foundEvent.getUser());
        assertEquals(eventWithUserFile.getFile(), foundEvent.getFile());

        verify(eventRepository, times(TEST_NUMBER_1)).findById(eventId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindEventByIdAndEventNotExists() {
        when(eventRepository.findById(eventId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> eventService.findById(eventId));

        verify(eventRepository, times(TEST_NUMBER_1)).findById(eventId);
    }

    @Test
    void shouldFindAllEvents() {
        when(eventRepository.findAll()).thenReturn(expectedEvents);

        List<Event> foundEvents = eventService.findAll();

        assertNotNull(foundEvents);
        assertFalse(foundEvents.isEmpty());
        assertEquals(expectedEvents, foundEvents);
        assertEquals(TEST_NUMBER_2, foundEvents.size());

        verify(eventRepository, times(TEST_NUMBER_1)).findAll();
    }

    @Test
    void shouldDeleteEventById() {
        when(eventRepository.findById(eventId)).thenReturn(expectedEventWithIdUserFile);
        doNothing().when(eventRepository).deleteById(eventId);

        eventService.deleteById(eventId);

        verify(eventRepository, times(TEST_NUMBER_1)).findById(eventId);
        verify(eventRepository, times(TEST_NUMBER_1)).deleteById(eventId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteEventByIdAndEventNotExists() {
        when(eventRepository.findById(eventId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> eventService.deleteById(eventId));

        verify(eventRepository, times(TEST_NUMBER_1)).findById(eventId);
        verify(eventRepository, times(TEST_NUMBER_0)).deleteById(eventId);
    }

    @Test
    void shouldDeleteAllEvents() {
        doNothing().when(eventRepository).deleteAll();

        eventService.deleteAll();

        verify(eventRepository, times(TEST_NUMBER_1)).deleteAll();
    }
}