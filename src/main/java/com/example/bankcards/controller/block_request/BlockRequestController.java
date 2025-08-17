package com.example.bankcards.controller.block_request;

import com.example.bankcards.dto.block_request.BlockCardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Block request management", description = "Operations with block card request")
public interface BlockRequestController {

    @Operation(
            summary = "Create block card request",
            description = "Create a new block card request and return created request details"
    )
    BlockCardDto createBlockCardRequest(BlockCardDto blockCardDto);

    @Operation(
            summary = "Get block request by ID",
            description = "Retrieve block card request details by its ID"
    )
    BlockCardDto getById(Long requestId);

    @Operation(
            summary = "Get all block requests",
            description = "Retrieve a list of all block card requests"
    )
    List<BlockCardDto> getAll();
}
