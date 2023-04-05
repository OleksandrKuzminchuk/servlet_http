package com.sasha.servletapi.service.impl;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.File;
import com.sasha.servletapi.util.UtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.List;

import static com.sasha.servletapi.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(PER_METHOD)
class FileServiceImplTest extends UtilService {

    @Test
    void shouldSaveFile() {
        when(fileRepository.save(fileWithNamePath)).thenReturn(expectedFileWithIdNamePath);

        File savedFile = fileService.save(fileWithNamePath);

        assertNotNull(savedFile);
        assertEquals(expectedFileWithIdNamePath, savedFile);
        assertEquals(TEST_NUMBER_1, savedFile.getId());

        verify(fileRepository, times(TEST_NUMBER_1)).save(fileWithNamePath);
    }

    @Test
    void shouldUpdateFile() {
        when(fileRepository.update(expectedFileWithIdNamePath)).thenReturn(expectedFileWithIdNamePath);

        File updatedFile = fileService.update(expectedFileWithIdNamePath);

        assertNotNull(updatedFile);
        assertEquals(expectedFileWithIdNamePath, updatedFile);
        assertEquals(TEST_NUMBER_1, updatedFile.getId());

        verify(fileRepository, times(TEST_NUMBER_1)).update(expectedFileWithIdNamePath);
    }

    @Test
    void shouldUploadFileWhenFileNotToDataBase() throws ServletException, IOException {
        when(request.getPart(TEST_TEXT_FILE)).thenReturn(part);
        when(part.getSubmittedFileName()).thenReturn(TEST_TEXT_TEST_FILE);
        when(fileRepository.isExistsByName(TEST_TEXT_TEST_FILE)).thenReturn(false);
        when(request.getParameter(TEST_TEXT_OVERWRITE)).thenReturn(null);
        when(fileRepository.save(any(File.class))).thenReturn(expectedFileWithIdNamePath);
        when(part.getInputStream()).thenReturn(inputStream);

        File uploadedFile = fileService.uploadFile(request);

        assertNotNull(uploadedFile);
        assertEquals(TEST_TEXT_TEST_FILE, uploadedFile.getName());
        assertEquals(TEST_TEXT_PATH_TO_TEST_FILE, uploadedFile.getFilePath());

        verify(fileRepository, times(TEST_NUMBER_1)).save(any(File.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUploadFileAndFileHasAlreadyTaken() throws ServletException, IOException {
        when(request.getPart(TEST_TEXT_FILE)).thenReturn(part);
        when(part.getSubmittedFileName()).thenReturn(TEST_TEXT_TEST_FILE);
        when(fileRepository.isExistsByName(TEST_TEXT_TEST_FILE)).thenReturn(true);
        when(request.getParameter(TEST_TEXT_OVERWRITE)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> fileService.uploadFile(request));

        verify(fileRepository, times(TEST_NUMBER_0)).save(any(File.class));
    }

    @Test
    void shouldUploadFileWhenFileExistsToDataBaseButThereIsParameterOverwriteTrue() throws ServletException, IOException {
        when(request.getPart(TEST_TEXT_FILE)).thenReturn(part);
        when(part.getSubmittedFileName()).thenReturn(TEST_TEXT_TEST_FILE);
        when(fileRepository.isExistsByName(TEST_TEXT_TEST_FILE)).thenReturn(true);
        when(request.getParameter(TEST_TEXT_OVERWRITE)).thenReturn(TEST_TEXT_TRUE);
        when(fileRepository.findByName(TEST_TEXT_TEST_FILE)).thenReturn(expectedFileWithIdNamePath);
        when(part.getInputStream()).thenReturn(inputStream);

        File uploadedFile = fileService.uploadFile(request);

        assertNotNull(uploadedFile);
        assertEquals(TEST_TEXT_TEST_FILE, uploadedFile.getName());
        assertEquals(TEST_TEXT_PATH_TO_TEST_FILE, uploadedFile.getFilePath());

        verify(fileRepository, times(TEST_NUMBER_0)).save(any(File.class));
    }

    @Test
    void shouldOverwriteFile() throws IOException {
        when(fileRepository.findById(fileId)).thenReturn(expectedFileWithIdNamePath);
        when(fileRepository.update(any(File.class))).thenReturn(expectedFileWithIdNamePath);

        java.io.File tempFile = java.nio.file.Files.createTempFile(TEST_TEXT_TEMP, TEST_TEXT_FILE).toFile();
        try {
            tempFile.deleteOnExit();
            expectedFileWithIdNamePath.setFilePath(tempFile.getAbsolutePath());

            String newName = TEST_TEXT_FILE_NEW_NAME;
            File overwrittenFile = fileService.overwriteFile(fileId, newName);

            assertNotNull(overwrittenFile);
            assertEquals(newName, overwrittenFile.getName());
            assertNotEquals(tempFile.getAbsolutePath(), overwrittenFile.getFilePath());

            verify(fileRepository, times(TEST_NUMBER_1)).findById(fileId);
            verify(fileRepository, times(TEST_NUMBER_1)).update(any(File.class));
        }finally {
            tempFile.delete();
        }
    }

    @Test
    void shouldThrowNotFoundExceptionFileNameHasAlreadyTakenWhenUpdate() {
        when(fileRepository.findById(fileId)).thenReturn(expectedFileWithIdNamePath);

        String newName = expectedFileWithIdNamePath.getName();

        assertThrows(NotFoundException.class, () -> fileService.overwriteFile(fileId, newName));

        verify(fileRepository, times(TEST_NUMBER_1)).findById(fileId);
        verify(fileRepository, times(TEST_NUMBER_0)).update(any(File.class));
    }

    @Test
    void shouldThrowNotFoundExceptionNotExistsFileWhenUpdate() {
        when(fileRepository.findById(fileId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> fileService.overwriteFile(fileId, expectedFileWithIdNamePath.getName()));

        verify(fileRepository, times(TEST_NUMBER_1)).findById(fileId);
        verify(fileRepository, times(TEST_NUMBER_0)).update(any(File.class));
    }

    @Test
    void shouldFindFileById() {
        when(fileRepository.findById(fileId)).thenReturn(expectedFileWithIdNamePath);

        File foundFile = fileService.findById(fileId);

        assertNotNull(foundFile);
        assertEquals(expectedFileWithIdNamePath, foundFile);
        assertEquals(TEST_NUMBER_1, foundFile.getId());

        verify(fileRepository, times(TEST_NUMBER_1)).findById(fileId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindFileByIdAndFileNotExists() {
        when(fileRepository.findById(fileId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> fileService.findById(fileId));

        verify(fileRepository, times(TEST_NUMBER_1)).findById(fileId);
    }

    @Test
    void shouldFindAllFiles() {
        when(fileRepository.findAll()).thenReturn(expectedFiles);

        List<File> foundFiles = fileService.findAll();

        assertNotNull(foundFiles);
        assertEquals(expectedFiles, foundFiles);
        assertEquals(TEST_NUMBER_2, foundFiles.size());

        verify(fileRepository, times(TEST_NUMBER_1)).findAll();
    }

    @Test
    void shouldDeleteFileById() {
        when(fileRepository.findById(fileId)).thenReturn(expectedFileWithIdNamePath);
        doNothing().when(fileRepository).deleteById(fileId);

        fileService.deleteById(fileId);

        verify(fileRepository, times(TEST_NUMBER_1)).findById(fileId);
        verify(fileRepository, times(TEST_NUMBER_1)).deleteById(fileId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteFileByIdAndFileNotExists() {
        when(fileRepository.findById(fileId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> fileService.deleteById(fileId));

        verify(fileRepository, times(TEST_NUMBER_1)).findById(fileId);
        verify(fileRepository, times(TEST_NUMBER_0)).deleteById(fileId);
    }

    @Test
    void shouldDeleteAllFiles() {
        doNothing().when(fileRepository).deleteAll();

        fileService.deleteAll();

        verify(fileRepository, times(TEST_NUMBER_1)).deleteAll();
    }

    @Test
    void shouldCheckIsExistsFileByNameReturnTrue() {
        when(fileRepository.isExistsByName(expectedFileWithIdNamePath.getName())).thenReturn(true);

        boolean isExistsFile = fileService.isExistsByName(expectedFileWithIdNamePath.getName());

        assertTrue(isExistsFile);

        verify(fileRepository, times(TEST_NUMBER_1)).isExistsByName(expectedFileWithIdNamePath.getName());
    }

    @Test
    void shouldCheckIsExistsFileByNameReturnFalse() {
        when(fileRepository.isExistsByName(expectedFileWithIdNamePath.getName())).thenReturn(false);

        boolean isExistsFile = fileService.isExistsByName(expectedFileWithIdNamePath.getName());

        assertFalse(isExistsFile);

        verify(fileRepository, times(TEST_NUMBER_1)).isExistsByName(expectedFileWithIdNamePath.getName());
    }

    @Test
    void shouldFindFileByName() {
        when(fileRepository.findByName(expectedFileWithIdNamePath.getName())).thenReturn(expectedFileWithIdNamePath);

        File foundFile = fileService.findByName(expectedFileWithIdNamePath.getName());

        assertNotNull(foundFile);
        assertEquals(expectedFileWithIdNamePath, foundFile);
        assertEquals(TEST_NUMBER_1, foundFile.getId());

        verify(fileRepository, times(TEST_NUMBER_1)).findByName(expectedFileWithIdNamePath.getName());
    }

    @Test
    void shouldFindFileByNameReturnNull() {
        when(fileRepository.findByName(expectedFileWithIdNamePath.getName())).thenReturn(null);

        File foundFile = fileService.findByName(expectedFileWithIdNamePath.getName());

        assertNull(foundFile);

        verify(fileRepository, times(TEST_NUMBER_1)).findByName(expectedFileWithIdNamePath.getName());
    }
}