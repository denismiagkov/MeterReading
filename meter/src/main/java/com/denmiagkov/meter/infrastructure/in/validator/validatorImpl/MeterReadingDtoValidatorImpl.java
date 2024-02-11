package com.denmiagkov.meter.infrastructure.in.validator.validatorImpl;

import com.denmiagkov.meter.application.dto.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewActualDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewForMonthDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingSubmitDto;
import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.DictionaryServiceImpl;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.application.service.MeterReadingServiceImpl;
import com.denmiagkov.meter.infrastructure.in.validator.exception.NewMeterValueIsLessThenPreviousException;
import com.denmiagkov.meter.infrastructure.in.validator.DtoValidator;
import com.denmiagkov.meter.infrastructure.in.validator.exception.InvalidDateException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.SubmitReadingOnTheSameMonthException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.UtilityTypeNotFoundException;

import java.time.LocalDateTime;

/**
 * Класс, валидирующий данные о новом показании счетчика, введенные пользователем
 */

public class MeterReadingDtoValidatorImpl implements DtoValidator<MeterReadingSubmitDto> {
    public static final MeterReadingDtoValidatorImpl INSTANCE = new MeterReadingDtoValidatorImpl();
    /**
     * Контроллер
     */
    DictionaryService dictionaryService;
    MeterReadingService meterReadingService;

    private MeterReadingDtoValidatorImpl() {
        dictionaryService = DictionaryServiceImpl.INSTANCE;
        meterReadingService = MeterReadingServiceImpl.INSTANCE;
    }

    /**
     * Метод проверки передачи корректного id типа услуг (показаний)
     *
     * @param newMeterReading Новое показание счетчика
     * @return boolean возвращает true - если условие выполняется, false - если нет
     */
    public boolean isValidMeterReadingUtilityType(MeterReadingSubmitDto newMeterReading) {
        int utilityCount = dictionaryService.getUtilitiesDictionary().size();
        if (newMeterReading.getUtilityId() <= utilityCount) {
            return true;
        } else {
            throw new UtilityTypeNotFoundException();
        }
    }

    public boolean isValidMeterReadingUtilityType(MeterReadingReviewActualDto newMeterReading) {
        int utilityCount = dictionaryService.getUtilitiesDictionary().size();
        if (newMeterReading.getUtilityId() <= utilityCount) {
            return true;
        } else {
            throw new UtilityTypeNotFoundException();
        }
    }

    /**
     * Метод проверки, исключающий возможность повторной подачи показаний в текущем месяце
     *
     * @param newMeterReading Новое показание счетчика
     * @return boolean возвращает true - если условие выполняется, false - если нет
     */
    public boolean isValidDate(MeterReadingSubmitDto newMeterReading) {
        MeterReadingReviewActualDto requestActualMeterReading = getActualMeterReadingDto(newMeterReading);
        MeterReadingDto actualMeterReading =
                meterReadingService.getActualMeterReadingOnExactUtilityByUser(requestActualMeterReading);
        LocalDateTime now = newMeterReading.getDate();
        if (actualMeterReading == null
            || (actualMeterReading.getDate().getMonthValue() != now.getMonthValue()
                || actualMeterReading.getDate().getYear() != now.getYear())) {
            return true;
        } else {
            throw new SubmitReadingOnTheSameMonthException();
        }
    }

    public MeterReadingReviewActualDto getActualMeterReadingDto(MeterReadingSubmitDto newMeterReading) {
        MeterReadingReviewActualDto requestActualMeterReadingDto = new MeterReadingReviewActualDto();
        requestActualMeterReadingDto.setUserId(newMeterReading.getUserId());
        requestActualMeterReadingDto.setUtilityId(newMeterReading.getUtilityId());
        return requestActualMeterReadingDto;
    }

    /**
     * Метод проверки, исключающий возможность внесения показаний, меньших актуальных значений по величине
     *
     * @param newMeterReading Новое показание счетчика
     * @return boolean возвращает true - если условие выполняется, false - если нет
     */
    public boolean isValidMeterValue(MeterReadingSubmitDto newMeterReading) {
        MeterReadingReviewActualDto requestActualMeterReading = getActualMeterReadingDto(newMeterReading);
        MeterReadingDto actualMeterReading =
                meterReadingService.getActualMeterReadingOnExactUtilityByUser(requestActualMeterReading);
        if (actualMeterReading == null
            || (actualMeterReading.getValue() <= newMeterReading.getValue()
            )) {
            return true;
        } else {
            throw new NewMeterValueIsLessThenPreviousException();
        }
    }

    public boolean isValidMonth(MeterReadingReviewForMonthDto requestDto) {
        if (
                (requestDto.getMonth() >= LocalDateTime.MIN.getMonthValue())
                && (requestDto.getMonth() <= LocalDateTime.MAX.getMonthValue())
                && ((requestDto.getYear() < LocalDateTime.now().getYear())
                    || requestDto.getMonth() <= LocalDateTime.now().getMonthValue()
                       && requestDto.getYear() == LocalDateTime.now().getYear())
        ) {
            return true;
        } else {
            throw new InvalidDateException();
        }
    }

    @Override
    public boolean isValid(MeterReadingSubmitDto meterReading) {
        return isValidMeterReadingUtilityType(meterReading)
               && isValidDate(meterReading)
               && isValidMeterValue(meterReading);
    }
}
