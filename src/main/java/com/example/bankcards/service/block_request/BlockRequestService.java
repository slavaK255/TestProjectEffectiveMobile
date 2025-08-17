package com.example.bankcards.service.block_request;

import com.example.bankcards.dto.block_request.BlockCardDto;

import java.util.List;

public interface BlockRequestService {
    BlockCardDto createBlockCardRequest(BlockCardDto blockCardDto);
    BlockCardDto findById(Long requestId);
    List<BlockCardDto> findAll();
}
