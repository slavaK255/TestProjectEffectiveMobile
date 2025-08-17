package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.change_status.ChangeStatusDto;
import com.example.bankcards.dto.create_card.CreateCardDto;
import com.example.bankcards.dto.create_card.CreateCardResponseDto;
import com.example.bankcards.dto.transfer.CardCurrentBalanceDto;
import com.example.bankcards.dto.transfer.TransferRequestDto;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface CardMapper {

    Card toEntity(CreateCardDto createCardDto);

    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "cardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    CreateCardResponseDto toCreateCardResponseDto(Card card);

    @Mapping(target = "cardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    CardDto toDto(Card card);

    @Mapping(target = "cardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    CardCurrentBalanceDto toCardCurrentBalanceDto(Card Card);

    @Mapping(target = "cardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    CreateCardDto toCreateCardDtoWrapper (CreateCardDto createCardDto);

    @Mapping(target = "sourceCard", source = "sourceCard", qualifiedByName = "maskCardNumber")
    @Mapping(target = "targetCard", source = "targetCard", qualifiedByName = "maskCardNumber")
    TransferRequestDto toTransferRequestDtoWrapper (TransferRequestDto TransferRequestDto);

    default Page<CardDto> toDtoPage(Page<Card> cardPage) {
        return cardPage.map(this::toDto);
    }

    @Named("maskCardNumber")
    default String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return cardNumber;
        }

        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }
}
