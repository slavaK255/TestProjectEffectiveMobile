package com.example.bankcards.controller.block_request;

import com.example.bankcards.dto.block_request.BlockCardDto;
import com.example.bankcards.service.block_request.BlockRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/block_request")
public class BlockRequestControllerImpl implements BlockRequestController{

    private final BlockRequestService blockRequestService;

    @PostMapping("new")
    @PreAuthorize("hasAuthority('USER')")
    public BlockCardDto createBlockCardRequest(@Valid @RequestBody BlockCardDto blockCardDto){
        return blockRequestService.createBlockCardRequest(blockCardDto);
    }

    @GetMapping("get/{requestId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public BlockCardDto getById(@NotNull @PathVariable Long requestId){
        return blockRequestService.findById(requestId);
    }

    @GetMapping("get/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<BlockCardDto> getAll(){
        return blockRequestService.findAll();
    }

}
