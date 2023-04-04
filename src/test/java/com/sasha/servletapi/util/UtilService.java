package com.sasha.servletapi.util;

import com.sasha.servletapi.pojo.Event;
import com.sasha.servletapi.pojo.File;
import com.sasha.servletapi.pojo.User;
import com.sasha.servletapi.repository.EventRepository;
import com.sasha.servletapi.repository.FileRepository;
import com.sasha.servletapi.repository.UserRepository;
import com.sasha.servletapi.service.impl.EventServiceImpl;
import com.sasha.servletapi.service.impl.FileServiceImpl;
import com.sasha.servletapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static com.sasha.servletapi.util.Constants.*;

public class UtilService {
    @Mock
    protected HttpServletRequest request;
    @Mock
    protected Part part;
    @Mock
    protected InputStream inputStream;
    @Mock
    protected UserRepository userRepository;
    @Mock
    protected FileRepository fileRepository;
    @Mock
    protected EventRepository eventRepository;
    @InjectMocks
    protected FileServiceImpl fileService;
    @InjectMocks
    protected EventServiceImpl eventService;
    @InjectMocks
    protected UserServiceImpl userService;
    protected Integer userId;
    protected User userWithName;
    protected User expectedUserWithIdName;
    protected List<User> expectedUsers;
    protected Integer eventId;
    protected Event eventWithUserFile;
    protected Event expectedEventWithIdUserFile;
    protected Event newExpectedEventAfterUpdate;
    protected List<Event> expectedEvents;
    protected Integer fileId;
    protected File fileWithNamePath;
    protected File expectedFileWithIdNamePath;
    protected List<File> expectedFiles;

    @BeforeEach
    protected void setUp() {
        this.userId = getExpectedUserWithIdName().getId();
        this.userWithName = getUserWithName();
        this.expectedUserWithIdName = getExpectedUserWithIdName();
        this.expectedUsers = getExpectedUsers();
        this.eventId = getExpectedEventWithIdUserFile().getId();
        this.eventWithUserFile = getEventWithUserFile();
        this.expectedEventWithIdUserFile = getExpectedEventWithIdUserFile();
        this.newExpectedEventAfterUpdate = getNewExpectedEventAfterUpdate();
        this.expectedEvents = getExpectedEvents();
        this.fileId = getExpectedFile().getId();
        this.fileWithNamePath = getFileWithNamePath();
        this.expectedFileWithIdNamePath = getExpectedFile();
        this.expectedFiles = getExpectedFiles();

        Mockito.reset(userRepository);
    }

    protected User getUserWithName() {
        return new User(TEST_TEXT_ANY);
    }

    protected User getExpectedUserWithIdName() {
        return new User(TEST_NUMBER_1, TEST_TEXT_ANY);
    }

    protected List<User> getExpectedUsers() {
        return Arrays.asList(
                new User(TEST_NUMBER_2, TEST_TEXT_ANY),
                new User(TEST_NUMBER_3, TEST_TEXT_ANY));
    }

    protected Event getEventWithUserFile() {
        return new Event(getExpectedUserWithIdName(), getExpectedFile()
        );
    }

    protected Event getExpectedEventWithIdUserFile() {
        return new Event(TEST_NUMBER_1, getExpectedUserWithIdName(), getExpectedFile());
    }

    protected Event getNewExpectedEventAfterUpdate(){
        Event updatedEvent = getExpectedEventWithIdUserFile();
        updatedEvent.setUser(new User(TEST_NUMBER_4, TEST_TEXT_ANY));
        updatedEvent.setFile(new File(TEST_NUMBER_4, TEST_TEXT_ANY, TEST_TEXT_ANY));
        return updatedEvent;
    }

    protected List<Event> getExpectedEvents() {
        return Arrays.asList(
                new Event(TEST_NUMBER_2, getExpectedUserWithIdName(), getExpectedFile()),
                new Event(TEST_NUMBER_3, getExpectedUserWithIdName(), getExpectedFile()));
    }

    protected File getFileWithNamePath() {
        return new File(TEST_TEXT_ANY, TEST_TEXT_ANY);
    }

    protected File getExpectedFile() {
        return new File(1, TEST_TEXT_TEST_FILE, TEST_TEXT_PATH_TO_TEST_FILE);
    }

    protected List<File> getExpectedFiles() {
        return Arrays.asList(
                new File(TEST_NUMBER_2, TEST_TEXT_ANY, TEST_TEXT_ANY),
                new File(TEST_NUMBER_3, TEST_TEXT_ANY, TEST_TEXT_ANY));
    }
}