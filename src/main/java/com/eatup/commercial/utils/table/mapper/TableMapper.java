package com.eatup.commercial.utils.table.mapper;

import com.eatup.commercial.domain.table.TableDomain;
import com.eatup.commercial.domain.table.TableReservationDomain;
import com.eatup.commercial.domain.table.TableSessionDomain;
import com.eatup.commercial.dto.table.TableDTO;
import com.eatup.commercial.dto.table.TableReservationDTO;
import com.eatup.commercial.dto.table.TableSessionDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TableMapper {

    public TableDTO toDto(TableDomain domain) {
        TableDTO dto = new TableDTO();
        dto.setId(domain.getId().toString());
        dto.setTableNumber(domain.getTableNumber());
        dto.setLocation(domain.getLocation());
        dto.setIsVip(domain.getIsVip());
        dto.setHasView(domain.getHasView());
        dto.setIsAccessible(domain.getIsAccessible());
        dto.setVenueId(domain.getVenueId().toString());
        dto.setStatus(domain.getStatus());
        dto.setActive(domain.getActive());
        dto.setCreatedDate(domain.getCreatedDate());
        dto.setModifiedDate(domain.getModifiedDate());
        return dto;
    }

    public TableDomain toDomain(TableDTO dto) {
        TableDomain domain = new TableDomain();
        domain.setTableNumber(dto.getTableNumber());
        domain.setLocation(dto.getLocation());
        domain.setIsVip(dto.getIsVip());
        domain.setHasView(dto.getHasView());
        domain.setIsAccessible(dto.getIsAccessible());
        domain.setStatus(dto.getStatus());
        domain.setActive(dto.getActive());
        domain.setCreatedDate(dto.getCreatedDate());
        domain.setModifiedDate(dto.getModifiedDate());
        return domain;
    }

    public TableSessionDTO toSessionDto(TableSessionDomain domain) {
        TableSessionDTO dto = new TableSessionDTO();
        dto.setId(domain.getId().toString());
        dto.setTableId(domain.getTableId().toString());
        dto.setReservationId(domain.getReservationId() != null ? domain.getReservationId().toString() : null);
        dto.setGuestCount(domain.getGuestCount());
        dto.setWaiterId(domain.getWaiterId() != null ? domain.getWaiterId().toString() : null);
        dto.setOpenedAt(domain.getOpenedAt());
        dto.setClosedAt(domain.getClosedAt());
        dto.setDurationMinutes(domain.getDurationMinutes());
        dto.setObservations(domain.getObservations());
        return dto;
    }

    public TableReservationDTO toReservationDto(TableReservationDomain domain) {
        TableReservationDTO dto = new TableReservationDTO();
        dto.setId(domain.getId().toString());
        dto.setTableId(domain.getTableId().toString());
        dto.setReservationDate(domain.getReservationDate());
        dto.setReservationTime(domain.getReservationTime());
        LocalDateTime reservationDateTime = LocalDateTime.of(domain.getReservationDate(), domain.getReservationTime());
        dto.setReservationDateTime(reservationDateTime);
        dto.setReservationLockStartsAt(reservationDateTime.minusHours(1));
        dto.setReservationGraceEndsAt(reservationDateTime.plusMinutes(15));
        dto.setGuestName(domain.getGuestName());
        dto.setGuestCount(domain.getGuestCount());
        dto.setGuestDocumentNumber(domain.getGuestDocumentNumber());
        dto.setStatus(domain.getStatus());
        dto.setCreatedDate(domain.getCreatedDate());
        return dto;
    }
}
