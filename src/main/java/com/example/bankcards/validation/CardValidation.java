package com.example.bankcards.validation;

import com.example.bankcards.dto.create_card.CreateCardDto;
import com.example.bankcards.dto.transfer.TransferRequestDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CardValidation {
    public boolean isExpiryDateValid(CreateCardDto createCardDto) {
        LocalDate checkedDate = createCardDto.getExpiryDate();

        if (checkedDate.isBefore(LocalDate.now())) {
            return false;
        }

        if (checkedDate.isAfter(LocalDate.now().plusYears(8))) {
            return false;
        }

        return true;
    }

    public boolean isUserOwner(TransferRequestDto transferRequestDto, User user) {
        return userContainsCard(user, transferRequestDto.getSourceCard())
                && userContainsCard(user, transferRequestDto.getTargetCard());

    }

    public boolean isSourceCardHaveEnoughMoney(Card card, BigDecimal amount) {
        return card.getBalance().compareTo(amount) >= 0;
    }


    private boolean userContainsCard(User user, String cardNumber) {
        return user.getCards().stream()
                .anyMatch(card -> card.getCardNumber().equals(cardNumber));
    }
}
