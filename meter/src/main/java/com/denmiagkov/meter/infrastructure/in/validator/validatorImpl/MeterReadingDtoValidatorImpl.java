package com.denmiagkov.meter.infrastructure.in.validator.validatorImpl;

import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewActualDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewForMonthDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingSubmitDto;
import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.NewMeterValueIsLessThenPreviousException;
import com.denmiagkov.meter.infrastructure.in.validator.DtoValidator;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.InvalidDateException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.SubmitReadingOnTheSameMonthException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.UtilityTypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Класс, валидирующий данные о новом показании счетчика, введенные пользователем
 */
@Component
public class MeterReadingDtoValidatorImpl implements DtoValidator<MeterReadingSubmitDto> {

    DictionaryService dictionaryService;
    MeterReadingService meterReadingService;

    @Autowired
    public MeterReadingDtoValidatorImpl(DictionaryService dictionaryService, MeterReadingService meterReadingService) {
        this.dictionaryService = dictionaryService;
        this.meterReadingService = meterReadingService;
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

    /**
     * Метод создает входящее ДТО для получения актуального показания счетчика пользователя по заданной услуге
     *
     * @param newMeterReading новое показание счетчика
     * @return MeterReadingReviewActualDto ДТО для получения текущего показания счетчика
     */
    private MeterReadingReviewActualDto getActualMeterReadingDto(MeterReadingSubmitDto newMeterReading) {
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

    /**
     * Метод проверяет корректность указания месяца для предоставления списка запрошенных показаний счечиков
     *
     * @param requestDto запрос на предоставление показаний за определенный месяц
     * @return boolean true в случае успешной проверки, в противном случае - false
     */
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

    /**
     * Комплексный метод валидации указанных пользователем данных о типа показаний счетчика, дате предоставления
     * сведений и значении счетчика при подаче нового покащания
     *
     * @param meterReading входящее ДТО на подачу новых показаний счетчика
     * @return boolean true в случае успешной проверки, в противном случае - false
     */
    @Override
    public boolean isValid(MeterReadingSubmitDto meterReading) {
        return isValidMeterReadingUtilityType(meterReading)
               && isValidDate(meterReading)
               && isValidMeterValue(meterReading);
    }
}
