package com.cspl.common.gen_ai.speechaiengine.utils;

import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Utility class to handle campaign time window calculations
 */
@Component
@Slf4j
public class TimeWindowUtils {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Check if the given time is within campaign's allowed time window
     *
     * @param dateTime The datetime to check
     * @param campaign The campaign with time constraints
     * @return true if within allowed window
     */
    public static boolean isWithinTimeWindow(LocalDateTime dateTime, Campaign campaign) {
        try {
            // Convert to campaign's timezone
            ZonedDateTime campaignTime = dateTime.atZone(ZoneOffset.UTC)
                    .withZoneSameInstant(campaign.getZoneId());

            // Check if within date range
            if (!isWithinDateRange(campaignTime.toLocalDate(), campaign)) {
                return false;
            }

            // Check if within daily time range
            return isWithinDailyTimeRange(campaignTime.toLocalTime(), campaign);

        } catch (Exception e) {
            log.warn("Error checking time window for campaign {}: {}", campaign.getId(), e.getMessage());
            return false;
        }
    }

    /**
     * Calculate the next valid time within campaign window
     *
     * @param proposedTime The initially proposed retry time
     * @param campaign The campaign with time constraints
     * @return The adjusted time within campaign window
     */
    public static LocalDateTime adjustToTimeWindow(LocalDateTime proposedTime, Campaign campaign) {
        try {
            ZonedDateTime campaignTime = proposedTime.atZone(ZoneOffset.UTC)
                    .withZoneSameInstant(campaign.getZoneId());

            // Adjust to valid date range
            campaignTime = adjustToDateRange(campaignTime, campaign);

            // Adjust to valid daily time range
            campaignTime = adjustToDailyTimeRange(campaignTime, campaign);

            // Convert back to UTC
            return campaignTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

        } catch (Exception e) {
            log.warn("Error adjusting time window for campaign {}: {}", campaign.getId(), e.getMessage());
            return proposedTime; // Return original time if adjustment fails
        }
    }

    private static boolean isWithinDateRange(LocalDate date, Campaign campaign) {
        LocalDate startDate = LocalDate.parse(campaign.getStartDate(), DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(campaign.getEndDate(), DATE_FORMATTER);

        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    private static boolean isWithinDailyTimeRange(LocalTime time, Campaign campaign) {
        LocalTime startTime = LocalTime.parse(campaign.getDailyStartTime(), TIME_FORMATTER);
        LocalTime endTime = LocalTime.parse(campaign.getDailyStopTime(), TIME_FORMATTER);

        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    private static ZonedDateTime adjustToDateRange(ZonedDateTime dateTime, Campaign campaign) {
        LocalDate date = dateTime.toLocalDate();
        LocalDate startDate = LocalDate.parse(campaign.getStartDate(), DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(campaign.getEndDate(), DATE_FORMATTER);

        if (date.isBefore(startDate)) {
            return dateTime.withDayOfMonth(startDate.getDayOfMonth())
                    .withMonth(startDate.getMonthValue())
                    .withYear(startDate.getYear());
        }

        if (date.isAfter(endDate)) {
            // If beyond campaign end date, return null or handle as needed
            log.warn("Retry time {} is beyond campaign end date {}", dateTime, endDate);
            return dateTime; // Or you might want to return null to indicate no valid time
        }

        return dateTime;
    }

    private static ZonedDateTime adjustToDailyTimeRange(ZonedDateTime dateTime, Campaign campaign) {
        LocalTime time = dateTime.toLocalTime();
        LocalTime startTime = LocalTime.parse(campaign.getDailyStartTime(), TIME_FORMATTER);
        LocalTime endTime = LocalTime.parse(campaign.getDailyStopTime(), TIME_FORMATTER);

        if (time.isBefore(startTime)) {
            // If before daily start time, move to start time of same day
            return dateTime.withHour(startTime.getHour()).withMinute(startTime.getMinute()).withSecond(0);
        }

        if (time.isAfter(endTime)) {
            // If after daily end time, move to start time of next day
            return dateTime.plusDays(1)
                    .withHour(startTime.getHour())
                    .withMinute(startTime.getMinute())
                    .withSecond(0);
        }

        return dateTime;
    }

    /**
     * Get the next available time slot within campaign window
     *
     * @param campaign The campaign
     * @return The next available datetime in UTC, or null if no valid time exists
     */
    public static LocalDateTime getNextAvailableTime(Campaign campaign) {
        LocalDateTime now = LocalDateTime.now();
        return adjustToTimeWindow(now, campaign);
    }

    /**
     * Check if campaign is currently active (within date and time range)
     *
     * @param campaign The campaign to check
     * @return true if currently active
     */
    public static boolean isCampaignActive(Campaign campaign) {
        return isWithinTimeWindow(LocalDateTime.now(), campaign);
    }
}
