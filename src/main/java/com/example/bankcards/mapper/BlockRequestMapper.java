package com.example.bankcards.mapper;

import com.example.bankcards.dto.block_request.BlockCardDto;
import com.example.bankcards.entity.BlockRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlockRequestMapper {
    BlockRequest toEntity (BlockCardDto blockCardDto);
    BlockCardDto toDto (BlockRequest blockRequest);
}