package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.dto.CreditResponseDto;
import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.Credit;
import com.example.marketAppWithJavaSpring.model.User;
import com.example.marketAppWithJavaSpring.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository creditRepository;
    private final UserService userService;
    public Credit save(Credit credit){
        return creditRepository.save(credit);
    }

    public List<CreditResponseDto> getCreditsByUserId(UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        List<Credit> creditList = getCreditListByUserId(user);

        validateCreditListNotEmpty(creditList);

        return mapCreditListToDtoList(creditList);
    }



    private List<Credit> getCreditListByUserId(User user) {
        return creditRepository.findCreditByCardUserUserId(user.getUserId());
    }

    private void validateCreditListNotEmpty(List<Credit> creditList) {
        if (creditList.isEmpty()) {
            throw new ServiceException(
                    ErrorMessage.CREDIT_LIST_IS_EMPTY.getMessage(),
                    ErrorMessage.CREDIT_LIST_IS_EMPTY.getStatus()
            );
        }
    }

    private List<CreditResponseDto> mapCreditListToDtoList(List<Credit> creditList) {
        return creditList.stream()
                .map(credit -> new CreditResponseDto(
                        credit.getCreditId(),
                        credit.getCard().getIBAN(),
                        credit.getStartTime(),
                        credit.getEndTime(),
                        credit.getMonthlyPayment()
                ))
                .collect(Collectors.toList());
    }
}
