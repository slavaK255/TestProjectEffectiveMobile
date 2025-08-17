package com.example.bankcards.service.block_request;

import com.example.bankcards.dto.block_request.BlockCardDto;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.exception.BlockCardException;
import com.example.bankcards.mapper.BlockRequestMapper;
import com.example.bankcards.repository.BlockRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BlockRequestServiceImpl implements BlockRequestService {

    private final BlockRequestRepository blockRequestRepository;
    private final BlockRequestMapper blockRequestMapper;

    @Override
    public BlockCardDto createBlockCardRequest(BlockCardDto blockCardDto) {
        log.info("Create new block request : " + blockCardDto.toString());
        BlockRequest blockRequest = blockRequestMapper.toEntity(blockCardDto);

        return blockRequestMapper.toDto(blockRequestRepository.save(blockRequest));
    }

    @Override
    public BlockCardDto findById(Long requestId) {
        log.info("Trying to find block card request by id : " + requestId);

        return blockRequestMapper.toDto(blockRequestRepository.findById(requestId)
                .orElseThrow(() -> new BlockCardException("Такого запроса не существует")));
    }

    @Override
    public List<BlockCardDto> findAll() {
        log.info("Get all block card request");

        return blockRequestRepository.findAll().stream()
                .map(blockRequestMapper::toDto)
                .toList();
    }
}
