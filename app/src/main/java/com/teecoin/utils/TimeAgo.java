package com.teecoin.utils;

public class TimeAgo {

    private TimeAgo() {
        super();
    }

    public static String using(final long time) {
        return using(time, new TimeAgoMessages.Builder().defaultLocale().build());
    }

    public static String using(final long time, final TimeAgoMessages resources) {
        final long dim = getTimeDistanceInMinutes(time);
        final StringBuilder timeAgo = buildTimeAgoText(resources, dim);
        return timeAgo.toString();
    }


    private static StringBuilder buildTimeAgoText(TimeAgoMessages resources, long dim) {
        final StringBuilder timeAgo = new StringBuilder();

        final Periods foundTimePeriod = Periods.findByDistanceMinutes(dim);
        if (foundTimePeriod != null) {
            final String periodKey = foundTimePeriod.getPropertyKey();
            switch (foundTimePeriod) {
                case X_MINUTES_PAST:
                    timeAgo.append(resources.getPropertyValue(periodKey, dim));
                    break;
                case X_HOURS_PAST:
                    int hours = Math.round(dim / 60);
                    final String xHoursText = handlePeriodKeyAsPlural(resources,
                            "tc.timeago.aboutanhour.past", periodKey, hours);
                    timeAgo.append(xHoursText);
                    break;
                case X_DAYS_PAST:
                    int days = Math.round(dim / 1440);
                    final String xDaysText = handlePeriodKeyAsPlural(resources,
                            "tc.timeago.oneday.past", periodKey, days);
                    timeAgo.append(xDaysText);
                    break;
                case X_MONTHS_PAST:
                    int months = Math.round(dim / 43200);
                    final String xMonthsText = handlePeriodKeyAsPlural(resources,
                            "tc.timeago.aboutamonth.past", periodKey, months);
                    timeAgo.append(xMonthsText);
                    break;
                case X_YEARS_PAST:
                    int years = Math.round(dim / 525600);
                    timeAgo.append(resources.getPropertyValue(periodKey, years));
                    break;
                case X_MINUTES_FUTURE:
                    timeAgo.append(resources.getPropertyValue(periodKey, Math.abs((float) dim)));
                    break;
                case X_HOURS_FUTURE:
                    int hours1 = Math.abs(Math.round(dim / 60f));
                    final String yHoursText = hours1 == 24
                            ? resources.getPropertyValue("tc.timeago.oneday.future")
                            : handlePeriodKeyAsPlural(resources, "tc.timeago.aboutanhour.future",
                            periodKey, hours1);
                    timeAgo.append(yHoursText);
                    break;
                case X_DAYS_FUTURE:
                    int days1 = Math.abs(Math.round(dim / 1440f));
                    final String yDaysText = handlePeriodKeyAsPlural(resources,
                            "tc.timeago.oneday.future", periodKey, days1);
                    timeAgo.append(yDaysText);
                    break;
                case X_MONTHS_FUTURE:
                    int months1 = Math.abs(Math.round(dim / 43200f));
                    final String yMonthsText = months1 == 12
                            ? resources.getPropertyValue("ml.timeago.aboutayear.future")
                            : handlePeriodKeyAsPlural(resources,
                            "tc.timeago.aboutamonth.future", periodKey, months1);
                    timeAgo.append(yMonthsText);
                    break;
                case X_YEARS_FUTURE:
                    int years1 = Math.abs(Math.round(dim / 525600f));
                    timeAgo.append(resources.getPropertyValue(periodKey, years1));
                    break;
                default:
                    timeAgo.append(resources.getPropertyValue(periodKey));
                    break;
            }
        }
        return timeAgo;
    }

    private static String handlePeriodKeyAsPlural(final TimeAgoMessages resources,
                                                  final String periodKey,
                                                  final String pluralKey, final int value) {
        return value == 1
                ? resources.getPropertyValue(periodKey)
                : resources.getPropertyValue(pluralKey, value);
    }

    private static long getTimeDistanceInMinutes(long time) {
        long timeDistance = System.currentTimeMillis() - time;
        return Math.round((timeDistance / 1000) / 60);
    }

    private enum Periods {

        NOW("tc.timeago.now", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance == 0;
            }
        }),
        ONE_MINUTE_PAST("tc.timeago.oneminute.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance == 1;
            }
        }),
        X_MINUTES_PAST("tc.timeago.xminutes.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance >= 2 && distance <= 44;
            }
        }),
        ABOUT_AN_HOUR_PAST("tc.timeago.aboutanhour.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance >= 45 && distance <= 89;
            }
        }),
        X_HOURS_PAST("tc.timeago.xhours.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance >= 90 && distance <= 1439;
            }
        }),
        ONE_DAY_PAST("tc.timeago.oneday.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance >= 1440 && distance <= 2519;
            }
        }),
        X_DAYS_PAST("tc.timeago.xdays.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance >= 2520 && distance <= 43199;
            }
        }),
        ABOUT_A_MONTH_PAST("tc.timeago.aboutamonth.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance >= 43200 && distance <= 86399;
            }
        }),
        X_MONTHS_PAST("tc.timeago.xmonths.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance >= 86400 && distance <= 525599;
            }
        }),
        ABOUT_A_YEAR_PAST("tc.timeago.aboutayear.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance >= 525600 && distance <= 655199;
            }
        }),
        OVER_A_YEAR_PAST("tc.timeago.overayear.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance >= 655200 && distance <= 914399;
            }
        }),
        ALMOST_TWO_YEARS_PAST("tc.timeago.almosttwoyears.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance >= 914400 && distance <= 1051199;
            }
        }),
        X_YEARS_PAST("tc.timeago.xyears.past", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return Math.round(distance / 525600) > 1;
            }
        }),
        ONE_MINUTE_FUTURE("tc.timeago.oneminute.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance == -1;
            }
        }),
        X_MINUTES_FUTURE("tc.timeago.xminutes.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance <= -2 && distance >= -44;
            }
        }),
        ABOUT_AN_HOUR_FUTURE("tc.timeago.aboutanhour.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance <= -45 && distance >= -89;
            }
        }),
        X_HOURS_FUTURE("tc.timeago.xhours.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance <= -90 && distance >= -1439;
            }
        }),
        ONE_DAY_FUTURE("tc.timeago.oneday.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance <= -1440 && distance >= -2519;
            }
        }),
        X_DAYS_FUTURE("tc.timeago.xdays.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance <= -2520 && distance >= -43199;
            }
        }),
        ABOUT_A_MONTH_FUTURE("tc.timeago.aboutamonth.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance <= -43200 && distance >= -86399;
            }
        }),
        X_MONTHS_FUTURE("tc.timeago.xmonths.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance <= -86400 && distance >= -525599;
            }
        }),
        ABOUT_A_YEAR_FUTURE("tc.timeago.aboutayear.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance <= -525600 && distance >= -655199;
            }
        }),
        OVER_A_YEAR_FUTURE("tc.timeago.overayear.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance <= -655200 && distance >= -914399;
            }
        }),
        ALMOST_TWO_YEARS_FUTURE("tc.timeago.almosttwoyears.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return distance <= -914400 && distance >= -1051199;
            }
        }),
        X_YEARS_FUTURE("tc.timeago.xyears.future", new DistancePredicate() {
            @Override
            public boolean validateDistanceMinutes(final long distance) {
                return Math.round(distance / 525600) < -1;
            }
        });

        private String mPropertyKey;

        private DistancePredicate mPredicate;

        Periods(String propertyKey, DistancePredicate predicate) {
            this.mPropertyKey = propertyKey;
            this.mPredicate = predicate;
        }


        public static Periods findByDistanceMinutes(final long distanceMinutes) {
            final Periods[] values = Periods.values();
            for (final Periods item : values) {
                final boolean successful = item.getPredicate()
                        .validateDistanceMinutes(distanceMinutes);
                if (successful) {
                    return item;
                }
            }
            return null;
        }

        private DistancePredicate getPredicate() {
            return mPredicate;
        }


        public String getPropertyKey() {
            return mPropertyKey;
        }
    }


    private interface DistancePredicate {

        boolean validateDistanceMinutes(final long distance);
    }
}
