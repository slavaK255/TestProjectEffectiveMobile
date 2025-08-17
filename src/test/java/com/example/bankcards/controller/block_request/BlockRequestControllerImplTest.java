package com.example.bankcards.controller.block_request;

import com.example.bankcards.dto.block_request.BlockCardDto;
import com.example.bankcards.service.block_request.BlockRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
class BlockRequestControllerImplTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BlockRequestControllerImpl blockRequestController;

    @Mock
    private BlockRequestService blockRequestService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final BlockCardDto validDto = new BlockCardDto();

    {
        validDto.setCard_id(12345L);
        validDto.setReason("Card stolen");
        validDto.setExecuted(false);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(blockRequestController).build();
    }

    @Test
    void createBlockCardRequestValidInputReturnsOk() throws Exception {
        Mockito.when(blockRequestService.createBlockCardRequest(Mockito.any()))
                .thenReturn(validDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/block_request/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.card_id").value(12345L))
                .andExpect(jsonPath("$.reason").value("Card stolen"));
    }

    @Test
    void createBlockCardRequestInvalidInputReturnsBadRequest() throws Exception {
        BlockCardDto invalidDto = new BlockCardDto();
        invalidDto.setCard_id(null);
        invalidDto.setReason("");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/block_request/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByIdValidRequestReturnsOk() throws Exception {
        Mockito.when(blockRequestService.findById(1L))
                .thenReturn(validDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/block_request/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.card_id").value(12345L));
    }

    @Test
    void getAllReturnsList() throws Exception {
        Mockito.when(blockRequestService.findAll())
                .thenReturn(Collections.singletonList(validDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/block_request/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].card_id").value(12345L))
                .andExpect(jsonPath("$[0].reason").value("Card stolen"));
    }
}