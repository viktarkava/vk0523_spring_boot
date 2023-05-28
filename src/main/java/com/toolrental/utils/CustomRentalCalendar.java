package com.toolrental.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

//vk0523

@Component
public class CustomRentalCalendar {
	private Set<LocalDate> holidays = new HashSet<>();

	public CustomRentalCalendar() {
	}

	public void calculateHolidays(int year) {
		for (int i = year - 5; i <= year + 5; i++) {
			holidays.add(IndependenceDayObserved(year));
			holidays.add(LaborDayObserved(year));
		}
	}

	public boolean isHoliday(LocalDate localDate) {
		return holidays.contains(localDate);
	}

	private boolean isWeekend(LocalDate localDate) {
		final DayOfWeek dow = localDate.getDayOfWeek();
		return dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
	}

	public void addHoliday(LocalDate monthDay) {
		holidays.add(monthDay);
	}

	public Set<LocalDate> getHolidays() {
		return holidays;
	}

	public void setHolidays(Set<LocalDate> holidays) {
		this.holidays = holidays;
	}

	public LocalDate IndependenceDayObserved(int year) {
		DayOfWeek dayOfWeek;
		int month = 7; // July
		LocalDate date = LocalDate.of(year, month, 4);
		dayOfWeek = date.getDayOfWeek();
		switch (dayOfWeek) {
		case SUNDAY:
			return LocalDate.of(year, month, 5);
		case MONDAY:
		case TUESDAY:
		case WEDNESDAY:
		case THURSDAY:
		case FRIDAY:
			return LocalDate.of(year, month, 4);
		default:
			// Saturday
			return LocalDate.of(year, month, 3);
		}
	}

	public LocalDate LaborDayObserved(int nYear) {
		// The first Monday in September
		DayOfWeek dayOfWeek;
		int nMonth = 9; // September
		LocalDate date = LocalDate.of(nYear, 9, 1);
		dayOfWeek = date.getDayOfWeek();
		switch (dayOfWeek) {
		case SUNDAY:
			return LocalDate.of(nYear, nMonth, 2);
		case MONDAY:
			return LocalDate.of(nYear, nMonth, 1);
		case TUESDAY:
			return LocalDate.of(nYear, nMonth, 7);
		case WEDNESDAY:
			return LocalDate.of(nYear, nMonth, 6);
		case THURSDAY:
			return LocalDate.of(nYear, nMonth, 5);
		case FRIDAY:
			return LocalDate.of(nYear, nMonth, 4);
		default:
			return LocalDate.of(nYear, nMonth, 3);
		}
	}

	public int getNumberOfWorkdaysBetween(final LocalDate startInclusive, final LocalDate endInclusive) {
		int ret = 0;
		for (LocalDate i = startInclusive; !i.isAfter(endInclusive); i = i.plusDays(1)) {
			if (!isHoliday(i) && !isWeekend(i)) {
				ret++;
			}
		}
		return ret;
	}

	public int getNumberOfWeekendsBetween(final LocalDate startInclusive, final LocalDate endInclusive) {
		int ret = 0;
		for (LocalDate i = startInclusive; !i.isAfter(endInclusive); i = i.plusDays(1)) {
			if (isWeekend(i)) {
				ret++;
			}
		}
		return ret;
	}

	public int getNumberOfHolidaysBetween(final LocalDate startInclusive, final LocalDate endInclusive) {
		int ret = 0;
		for (LocalDate i = startInclusive; !i.isAfter(endInclusive); i = i.plusDays(1)) {
			if (isHoliday(i)) {
				ret++;
			}
		}
		return ret;
	}
}
