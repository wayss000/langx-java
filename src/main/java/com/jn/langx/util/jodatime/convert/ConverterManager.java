/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.jn.langx.util.jodatime.convert;

import com.jn.langx.util.jodatime.JodaTimePermission;

/**
 * ConverterManager controls the date and time converters.
 * <p>
 * This class enables additional conversion classes to be added via
 * {@link #addInstantConverter(InstantConverter)}, which may replace an
 * existing converter. Similar methods exist for duration, time period and
 * interval converters.
 * <p>
 * This class is threadsafe, so adding/removing converters can be done at any
 * time. Updating the set of convertors is relatively expensive, and so should
 * not be performed often.
 * <p>
 * The default instant converters are:
 * <ul>
 * <li>ReadableInstant
 * <li>String
 * <li>Calendar
 * <li>Date (includes sql package subclasses)
 * <li>Long (milliseconds)
 * <li>null (now)
 * </ul>
 * <p>
 * The default partial converters are:
 * <ul>
 * <li>ReadablePartial
 * <li>ReadableInstant
 * <li>String
 * <li>Calendar
 * <li>Date (includes sql package subclasses)
 * <li>Long (milliseconds)
 * <li>null (now)
 * </ul>
 * <p>
 * The default duration converters are:
 * <ul>
 * <li>ReadableDuration
 * <li>ReadableInterval
 * <li>String
 * <li>Long (milliseconds)
 * <li>null (zero ms)
 * </ul>
 * <p>
 * The default time period converters are:
 * <ul>
 * <li>ReadablePeriod
 * <li>ReadableInterval
 * <li>String
 * <li>null (zero)
 * </ul>
 * <p>
 * The default interval converters are:
 * <ul>
 * <li>ReadableInterval
 * <li>String
 * <li>null (zero-length from now to now)
 * </ul>
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class ConverterManager {

    /**
     * Singleton instance, lazily loaded to avoid class loading.
     */
    private static ConverterManager INSTANCE;
    private com.jn.langx.util.jodatime.convert.ConverterSet iInstantConverters;
    private com.jn.langx.util.jodatime.convert.ConverterSet iPartialConverters;
    private com.jn.langx.util.jodatime.convert.ConverterSet iDurationConverters;
    private com.jn.langx.util.jodatime.convert.ConverterSet iPeriodConverters;
    private com.jn.langx.util.jodatime.convert.ConverterSet iIntervalConverters;
    /**
     * Restricted constructor.
     */
    protected ConverterManager() {
        super();

        iInstantConverters = new com.jn.langx.util.jodatime.convert.ConverterSet(new com.jn.langx.util.jodatime.convert.Converter[]{
                ReadableInstantConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.StringConverter.INSTANCE,
                CalendarConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.DateConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.LongConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.NullConverter.INSTANCE,
        });

        iPartialConverters = new com.jn.langx.util.jodatime.convert.ConverterSet(new com.jn.langx.util.jodatime.convert.Converter[]{
                ReadablePartialConverter.INSTANCE,
                ReadableInstantConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.StringConverter.INSTANCE,
                CalendarConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.DateConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.LongConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.NullConverter.INSTANCE,
        });

        iDurationConverters = new com.jn.langx.util.jodatime.convert.ConverterSet(new com.jn.langx.util.jodatime.convert.Converter[]{
                ReadableDurationConverter.INSTANCE,
                ReadableIntervalConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.StringConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.LongConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.NullConverter.INSTANCE,
        });

        iPeriodConverters = new com.jn.langx.util.jodatime.convert.ConverterSet(new com.jn.langx.util.jodatime.convert.Converter[]{
                ReadableDurationConverter.INSTANCE,
                ReadablePeriodConverter.INSTANCE,
                ReadableIntervalConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.StringConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.NullConverter.INSTANCE,
        });

        iIntervalConverters = new com.jn.langx.util.jodatime.convert.ConverterSet(new Converter[]{
                ReadableIntervalConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.StringConverter.INSTANCE,
                com.jn.langx.util.jodatime.convert.NullConverter.INSTANCE,
        });
    }

    public static ConverterManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConverterManager();
        }
        return INSTANCE;
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the best converter for the object specified.
     *
     * @param object the object to convert
     * @return the converter to use
     * @throws IllegalArgumentException if no suitable converter
     * @throws IllegalStateException    if multiple converters match the type
     *                                  equally well
     */
    public InstantConverter getInstantConverter(Object object) {
        InstantConverter converter =
                (InstantConverter) iInstantConverters.select(object == null ? null : object.getClass());
        if (converter != null) {
            return converter;
        }
        throw new IllegalArgumentException("No instant converter found for type: " +
                (object == null ? "null" : object.getClass().getName()));
    }

    //-----------------------------------------------------------------------

    /**
     * Gets a copy of the set of converters.
     *
     * @return the converters, a copy of the real data, never null
     */
    public InstantConverter[] getInstantConverters() {
        com.jn.langx.util.jodatime.convert.ConverterSet set = iInstantConverters;
        InstantConverter[] converters = new InstantConverter[set.size()];
        set.copyInto(converters);
        return converters;
    }

    /**
     * Adds a converter to the set of converters. If a matching converter is
     * already in the set, the given converter replaces it. If the converter is
     * exactly the same as one already in the set, no changes are made.
     * <p>
     * The order in which converters are added is not relevent. The best
     * converter is selected by examining the object hierarchy.
     *
     * @param converter the converter to add, null ignored
     * @return replaced converter, or null
     */
    public InstantConverter addInstantConverter(InstantConverter converter)
            throws SecurityException {

        checkAlterInstantConverters();
        if (converter == null) {
            return null;
        }
        InstantConverter[] removed = new InstantConverter[1];
        iInstantConverters = iInstantConverters.add(converter, removed);
        return removed[0];
    }

    /**
     * Removes a converter from the set of converters. If the converter was
     * not in the set, no changes are made.
     *
     * @param converter the converter to remove, null ignored
     * @return replaced converter, or null
     */
    public InstantConverter removeInstantConverter(InstantConverter converter)
            throws SecurityException {

        checkAlterInstantConverters();
        if (converter == null) {
            return null;
        }
        InstantConverter[] removed = new InstantConverter[1];
        iInstantConverters = iInstantConverters.remove(converter, removed);
        return removed[0];
    }

    /**
     * Checks whether the user has permission 'ConverterManager.alterInstantConverters'.
     *
     * @throws SecurityException if the user does not have the permission
     */
    private void checkAlterInstantConverters() throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new JodaTimePermission("ConverterManager.alterInstantConverters"));
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the best converter for the object specified.
     *
     * @param object the object to convert
     * @return the converter to use
     * @throws IllegalArgumentException if no suitable converter
     * @throws IllegalStateException    if multiple converters match the type
     *                                  equally well
     */
    public PartialConverter getPartialConverter(Object object) {
        PartialConverter converter =
                (PartialConverter) iPartialConverters.select(object == null ? null : object.getClass());
        if (converter != null) {
            return converter;
        }
        throw new IllegalArgumentException("No partial converter found for type: " +
                (object == null ? "null" : object.getClass().getName()));
    }

    //-----------------------------------------------------------------------

    /**
     * Gets a copy of the set of converters.
     *
     * @return the converters, a copy of the real data, never null
     */
    public PartialConverter[] getPartialConverters() {
        com.jn.langx.util.jodatime.convert.ConverterSet set = iPartialConverters;
        PartialConverter[] converters = new PartialConverter[set.size()];
        set.copyInto(converters);
        return converters;
    }

    /**
     * Adds a converter to the set of converters. If a matching converter is
     * already in the set, the given converter replaces it. If the converter is
     * exactly the same as one already in the set, no changes are made.
     * <p>
     * The order in which converters are added is not relevent. The best
     * converter is selected by examining the object hierarchy.
     *
     * @param converter the converter to add, null ignored
     * @return replaced converter, or null
     */
    public PartialConverter addPartialConverter(PartialConverter converter)
            throws SecurityException {

        checkAlterPartialConverters();
        if (converter == null) {
            return null;
        }
        PartialConverter[] removed = new PartialConverter[1];
        iPartialConverters = iPartialConverters.add(converter, removed);
        return removed[0];
    }

    /**
     * Removes a converter from the set of converters. If the converter was
     * not in the set, no changes are made.
     *
     * @param converter the converter to remove, null ignored
     * @return replaced converter, or null
     */
    public PartialConverter removePartialConverter(PartialConverter converter)
            throws SecurityException {

        checkAlterPartialConverters();
        if (converter == null) {
            return null;
        }
        PartialConverter[] removed = new PartialConverter[1];
        iPartialConverters = iPartialConverters.remove(converter, removed);
        return removed[0];
    }

    /**
     * Checks whether the user has permission 'ConverterManager.alterPartialConverters'.
     *
     * @throws SecurityException if the user does not have the permission
     */
    private void checkAlterPartialConverters() throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new JodaTimePermission("ConverterManager.alterPartialConverters"));
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the best converter for the object specified.
     *
     * @param object the object to convert
     * @return the converter to use
     * @throws IllegalArgumentException if no suitable converter
     * @throws IllegalStateException    if multiple converters match the type
     *                                  equally well
     */
    public DurationConverter getDurationConverter(Object object) {
        DurationConverter converter =
                (DurationConverter) iDurationConverters.select(object == null ? null : object.getClass());
        if (converter != null) {
            return converter;
        }
        throw new IllegalArgumentException("No duration converter found for type: " +
                (object == null ? "null" : object.getClass().getName()));
    }

    //-----------------------------------------------------------------------

    /**
     * Gets a copy of the list of converters.
     *
     * @return the converters, a copy of the real data, never null
     */
    public DurationConverter[] getDurationConverters() {
        com.jn.langx.util.jodatime.convert.ConverterSet set = iDurationConverters;
        DurationConverter[] converters = new DurationConverter[set.size()];
        set.copyInto(converters);
        return converters;
    }

    /**
     * Adds a converter to the set of converters. If a matching converter is
     * already in the set, the given converter replaces it. If the converter is
     * exactly the same as one already in the set, no changes are made.
     * <p>
     * The order in which converters are added is not relevent. The best
     * converter is selected by examining the object hierarchy.
     *
     * @param converter the converter to add, null ignored
     * @return replaced converter, or null
     */
    public DurationConverter addDurationConverter(DurationConverter converter)
            throws SecurityException {

        checkAlterDurationConverters();
        if (converter == null) {
            return null;
        }
        DurationConverter[] removed = new DurationConverter[1];
        iDurationConverters = iDurationConverters.add(converter, removed);
        return removed[0];
    }

    /**
     * Removes a converter from the set of converters. If the converter was
     * not in the set, no changes are made.
     *
     * @param converter the converter to remove, null ignored
     * @return replaced converter, or null
     */
    public DurationConverter removeDurationConverter(DurationConverter converter)
            throws SecurityException {

        checkAlterDurationConverters();
        if (converter == null) {
            return null;
        }
        DurationConverter[] removed = new DurationConverter[1];
        iDurationConverters = iDurationConverters.remove(converter, removed);
        return removed[0];
    }

    /**
     * Checks whether the user has permission 'ConverterManager.alterDurationConverters'.
     *
     * @throws SecurityException if the user does not have the permission
     */
    private void checkAlterDurationConverters() throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new JodaTimePermission("ConverterManager.alterDurationConverters"));
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the best converter for the object specified.
     *
     * @param object the object to convert
     * @return the converter to use
     * @throws IllegalArgumentException if no suitable converter
     * @throws IllegalStateException    if multiple converters match the type
     *                                  equally well
     */
    public com.jn.langx.util.jodatime.convert.PeriodConverter getPeriodConverter(Object object) {
        com.jn.langx.util.jodatime.convert.PeriodConverter converter =
                (com.jn.langx.util.jodatime.convert.PeriodConverter) iPeriodConverters.select(object == null ? null : object.getClass());
        if (converter != null) {
            return converter;
        }
        throw new IllegalArgumentException("No period converter found for type: " +
                (object == null ? "null" : object.getClass().getName()));
    }

    //-----------------------------------------------------------------------

    /**
     * Gets a copy of the list of converters.
     *
     * @return the converters, a copy of the real data, never null
     */
    public com.jn.langx.util.jodatime.convert.PeriodConverter[] getPeriodConverters() {
        com.jn.langx.util.jodatime.convert.ConverterSet set = iPeriodConverters;
        com.jn.langx.util.jodatime.convert.PeriodConverter[] converters = new com.jn.langx.util.jodatime.convert.PeriodConverter[set.size()];
        set.copyInto(converters);
        return converters;
    }

    /**
     * Adds a converter to the set of converters. If a matching converter is
     * already in the set, the given converter replaces it. If the converter is
     * exactly the same as one already in the set, no changes are made.
     * <p>
     * The order in which converters are added is not relevent. The best
     * converter is selected by examining the object hierarchy.
     *
     * @param converter the converter to add, null ignored
     * @return replaced converter, or null
     */
    public com.jn.langx.util.jodatime.convert.PeriodConverter addPeriodConverter(com.jn.langx.util.jodatime.convert.PeriodConverter converter)
            throws SecurityException {

        checkAlterPeriodConverters();
        if (converter == null) {
            return null;
        }
        com.jn.langx.util.jodatime.convert.PeriodConverter[] removed = new com.jn.langx.util.jodatime.convert.PeriodConverter[1];
        iPeriodConverters = iPeriodConverters.add(converter, removed);
        return removed[0];
    }

    /**
     * Removes a converter from the set of converters. If the converter was
     * not in the set, no changes are made.
     *
     * @param converter the converter to remove, null ignored
     * @return replaced converter, or null
     */
    public com.jn.langx.util.jodatime.convert.PeriodConverter removePeriodConverter(com.jn.langx.util.jodatime.convert.PeriodConverter converter)
            throws SecurityException {

        checkAlterPeriodConverters();
        if (converter == null) {
            return null;
        }
        com.jn.langx.util.jodatime.convert.PeriodConverter[] removed = new PeriodConverter[1];
        iPeriodConverters = iPeriodConverters.remove(converter, removed);
        return removed[0];
    }

    /**
     * Checks whether the user has permission 'ConverterManager.alterPeriodConverters'.
     *
     * @throws SecurityException if the user does not have the permission
     */
    private void checkAlterPeriodConverters() throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new JodaTimePermission("ConverterManager.alterPeriodConverters"));
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the best converter for the object specified.
     *
     * @param object the object to convert
     * @return the converter to use
     * @throws IllegalArgumentException if no suitable converter
     * @throws IllegalStateException    if multiple converters match the type
     *                                  equally well
     */
    public IntervalConverter getIntervalConverter(Object object) {
        IntervalConverter converter =
                (IntervalConverter) iIntervalConverters.select(object == null ? null : object.getClass());
        if (converter != null) {
            return converter;
        }
        throw new IllegalArgumentException("No interval converter found for type: " +
                (object == null ? "null" : object.getClass().getName()));
    }

    //-----------------------------------------------------------------------

    /**
     * Gets a copy of the list of converters.
     *
     * @return the converters, a copy of the real data, never null
     */
    public IntervalConverter[] getIntervalConverters() {
        com.jn.langx.util.jodatime.convert.ConverterSet set = iIntervalConverters;
        IntervalConverter[] converters = new IntervalConverter[set.size()];
        set.copyInto(converters);
        return converters;
    }

    /**
     * Adds a converter to the set of converters. If a matching converter is
     * already in the set, the given converter replaces it. If the converter is
     * exactly the same as one already in the set, no changes are made.
     * <p>
     * The order in which converters are added is not relevent. The best
     * converter is selected by examining the object hierarchy.
     *
     * @param converter the converter to add, null ignored
     * @return replaced converter, or null
     */
    public IntervalConverter addIntervalConverter(IntervalConverter converter)
            throws SecurityException {

        checkAlterIntervalConverters();
        if (converter == null) {
            return null;
        }
        IntervalConverter[] removed = new IntervalConverter[1];
        iIntervalConverters = iIntervalConverters.add(converter, removed);
        return removed[0];
    }

    /**
     * Removes a converter from the set of converters. If the converter was
     * not in the set, no changes are made.
     *
     * @param converter the converter to remove, null ignored
     * @return replaced converter, or null
     */
    public IntervalConverter removeIntervalConverter(IntervalConverter converter)
            throws SecurityException {

        checkAlterIntervalConverters();
        if (converter == null) {
            return null;
        }
        IntervalConverter[] removed = new IntervalConverter[1];
        iIntervalConverters = iIntervalConverters.remove(converter, removed);
        return removed[0];
    }

    /**
     * Checks whether the user has permission 'ConverterManager.alterIntervalConverters'.
     *
     * @throws SecurityException if the user does not have the permission
     */
    private void checkAlterIntervalConverters() throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new JodaTimePermission("ConverterManager.alterIntervalConverters"));
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Gets a debug representation of the object.
     */
    public String toString() {
        return "ConverterManager[" +
                iInstantConverters.size() + " instant," +
                iPartialConverters.size() + " partial," +
                iDurationConverters.size() + " duration," +
                iPeriodConverters.size() + " period," +
                iIntervalConverters.size() + " interval]";
    }

}
