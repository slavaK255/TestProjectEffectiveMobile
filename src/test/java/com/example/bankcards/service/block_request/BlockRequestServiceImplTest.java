package com.example.bankcards.service.block_request;

import com.example.bankcards.dto.block_request.BlockCardDto;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.exception.BlockCardException;
import com.example.bankcards.mapper.BlockRequestMapper;
import com.example.bankcards.repository.BlockRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockRequestServiceImplTest {

    @Mock
    private BlockRequestRepository blockRequestRepository;

    @Mock
    private BlockRequestMapper blockRequestMapper;

    @InjectMocks
    private BlockRequestServiceImpl blockRequestService;

    @Test
    void createBlockCardRequestShouldSaveAndReturnDto() {

        BlockCardDto inputDto = new BlockCardDto();
        inputDto.setCard_id(100L);
        inputDto.setReason("Fraud detected");
        inputDto.setExecuted(false);

        BlockRequest entity = new BlockRequest();
        entity.setId(1L);
        entity.setCard_id(100L);
        entity.setReason("Fraud detected");
        entity.setExecuted(false);

        BlockCardDto expectedDto = new BlockCardDto();
        expectedDto.setCard_id(100L);
        expectedDto.setReason("Fraud detected");
        expectedDto.setExecuted(false);

        when(blockRequestMapper.toEntity(inputDto)).thenReturn(entity);
        when(blockRequestRepository.save(entity)).thenReturn(entity);
        when(blockRequestMapper.toDto(entity)).thenReturn(expectedDto);

        BlockCardDto result = blockRequestService.createBlockCardRequest(inputDto);

        assertNotNull(result);
        assertEquals(100L, result.getCard_id());
        assertEquals("Fraud detected", result.getReason());
        assertFalse(result.isExecuted());

        verify(blockRequestMapper).toEntity(inputDto);
        verify(blockRequestRepository).save(entity);
        verify(blockRequestMapper).toDto(entity);
    }

    @Test
    void findByIdWhenExistsShouldReturnDto() {
        Long requestId = 1L;

        BlockRequest entity = new BlockRequest();
        entity.setId(requestId);
        entity.setCard_id(100L);
        entity.setReason("Suspicious activity");
        entity.setExecuted(true);

        BlockCardDto expectedDto = new BlockCardDto();
        expectedDto.setCard_id(100L);
        expectedDto.setReason("Suspicious activity");
        expectedDto.setExecuted(true);

        when(blockRequestRepository.findById(requestId)).thenReturn(Optional.of(entity));
        when(blockRequestMapper.toDto(entity)).thenReturn(expectedDto);

        BlockCardDto result = blockRequestService.findById(requestId);

        assertNotNull(result);
        assertEquals(100L, result.getCard_id());
        assertEquals("Suspicious activity", result.getReason());
        assertTrue(result.isExecuted());

        verify(blockRequestRepository).findById(requestId);
        verify(blockRequestMapper).toDto(entity);
    }

    @Test
    void findByIdWhenNotExistsShouldThrowException() {
        Long requestId = 99L;
        when(blockRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        BlockCardException exception = assertThrows(
                BlockCardException.class,
                () -> blockRequestService.findById(requestId)
        );

        assertEquals("Такого запроса не существует", exception.getMessage());
        verify(blockRequestRepository).findById(requestId);
        verify(blockRequestMapper, never()).toDto(any());
    }

    @Test
    void findAllWhenRequestsExistShouldReturnDtoList() {
        BlockRequest entity = new BlockRequest();
        entity.setId(1L);
        entity.setCard_id(100L);
        entity.setReason("Lost card");
        entity.setExecuted(false);

        BlockCardDto dto = new BlockCardDto();
        dto.setCard_id(100L);
        dto.setReason("Lost card");
        dto.setExecuted(false);

        when(blockRequestRepository.findAll()).thenReturn(List.of(entity));
        when(blockRequestMapper.toDto(entity)).thenReturn(dto);

        List<BlockCardDto> result = blockRequestService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        BlockCardDto first = result.get(0);
        assertEquals(100L, first.getCard_id());
        assertEquals("Lost card", first.getReason());
        assertFalse(first.isExecuted());

        verify(blockRequestRepository).findAll();
        verify(blockRequestMapper).toDto(entity);
    }

    @Test
    void findAllWhenNoRequestsShouldReturnEmptyList() {
        when(blockRequestRepository.findAll()).thenReturn(Collections.emptyList());

        List<BlockCardDto> result = blockRequestService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blockRequestRepository).findAll();
        verify(blockRequestMapper, never()).toDto(any());
    }
}