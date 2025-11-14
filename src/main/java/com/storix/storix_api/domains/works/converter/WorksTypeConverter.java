package com.storix.storix_api.domains.works.converter;

import com.storix.storix_api.domains.works.domain.WorksType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WorksTypeConverter implements AttributeConverter<WorksType, String> {

    @Override
    public String convertToDatabaseColumn(WorksType attribute) {
        return "";
    }

    @Override
    public WorksType convertToEntityAttribute(String dbData) {
        return null;
    }
}
