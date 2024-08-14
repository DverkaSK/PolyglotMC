package ru.dverkask.polyglotmc.version;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents the supported Minecraft versions in the PolyglotMC library.
 * <p>
 * This class contains three nested enums: {@link Release}, {@link Snapshot}, and {@link PreRelease},
 * each representing different types of Minecraft versions. All these enums implement the {@link Version} interface.
 * </p>
 *
 * @author DverkaSK
 * @version 1.0
 * @since 1.0
 */
public class SupportedVersion {

    /**
     * Represents official release versions of Minecraft.
     * <p>
     * This enum contains all the stable release versions of Minecraft supported by PolyglotMC.
     * </p>
     */
    public enum Release implements Version {
        V1_21("1.21"),
        V1_20_6("1.20.6"),
        V1_20_5("1.20.5"),
        V1_20_4("1.20.4"),
        V1_20_3("1.20.3"),
        V1_20_2("1.20.2"),
        V1_20_1("1.20.1"),
        V1_20("1.20"),
        V1_19_4("1.19.4"),
        V1_19_3("1.19.3"),
        V1_19_2("1.19.2"),
        V1_19_1("1.19.1"),
        V1_19("1.19"),
        V1_18_2("1.18.2"),
        V1_18_1("1.18.1"),
        V1_18("1.18"),
        V1_17_1("1.17.1"),
        V1_17("1.17"),
        V1_16_5("1.16.5"),
        V1_16_4("1.16.4"),
        V1_16_3("1.16.3"),
        V1_16_2("1.16.2"),
        V1_16_1("1.16.1"),
        V1_16("1.16"),
        V1_15_2("1.15.2"),
        V1_15_1("1.15.1"),
        V1_15("1.15"),
        V1_14_4("1.14.4"),
        V1_14_3("1.14.3"),
        V1_14_2("1.14.2"),
        V1_14_1("1.14.1"),
        V1_14("1.14"),
        V1_13_2("1.13.2"),
        V1_13_1("1.13.1"),
        V1_13("1.13"),
        V1_12_2("1.12.2"),
        V1_12_1("1.12.1"),
        V1_12("1.12"),
        V1_11_2("1.11.2"),
        V1_11_1("1.11.1"),
        V1_11("1.11"),
        V1_10_2("1.10.2"),
        V1_10_1("1.10.1"),
        V1_10("1.10"),
        V1_9_4("1.9.4"),
        V1_9_3("1.9.3"),
        V1_9_2("1.9.2"),
        V1_9_1("1.9.1"),
        V1_9("1.9"),
        V1_8_9("1.8.9"),
        V1_8_8("1.8.8"),
        V1_8_7("1.8.7"),
        V1_8_6("1.8.6"),
        V1_8_5("1.8.5"),
        V1_8_4("1.8.4"),
        V1_8_3("1.8.3"),
        V1_8_2("1.8.2"),
        V1_8_1("1.8.1"),
        V1_8("1.8"),
        V1_7_10("1.7.10"),
        V1_7_9("1.7.9"),
        V1_7_8("1.7.8"),
        V1_7_7("1.7.7"),
        V1_7_6("1.7.6"),
        V1_7_5("1.7.5"),
        V1_7_4("1.7.4"),
        V1_7_3("1.7.3"),
        V1_7_2("1.7.2"),
        V1_6_4("1.6.4"),
        V1_6_2("1.6.2"),
        V1_6_1("1.6.1"),
        V1_5_2("1.5.2"),
        V1_5_1("1.5.1"),
        V1_4_7("1.4.7"),
        V1_4_6("1.4.6"),
        V1_4_5("1.4.5"),
        V1_4_4("1.4.4"),
        V1_4_3("1.4.3"),
        V1_4_2("1.4.2"),
        V1_3_2("1.3.2"),
        V1_3_1("1.3.1"),
        V1_2_5("1.2.5"),
        V1_2_4("1.2.4"),
        V1_2_3("1.2.3"),
        V1_2_2("1.2.2"),
        V1_2_1("1.2.1"),
        V1_1("1.1"),
        V1_0("1.0"),
        ;

        /**
         * The string representation of the Minecraft release version.
         */
        private final String version;

        /**
         * Constructs a Release enum constant with the specified version string.
         *
         * @param version the string representation of the Minecraft version
         */
        Release(String version) {
            this.version = version;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String version() {
            return this.version;
        }
    }

    /**
     * Represents snapshot versions of Minecraft.
     * <p>
     * This enum contains all the snapshot versions of Minecraft supported by PolyglotMC.
     * Snapshots are development versions released before official releases.
     * </p>
     */
    public enum Snapshot implements Version {
        SNAPSHOT_24W21A("24w21a"),
        SNAPSHOT_24W20A("24w20a"),
        SNAPSHOT_20W11A("20w11a"),
        SNAPSHOT_20W07A("20w07a"),
        SNAPSHOT_24W18A("24w18a"),
        SNAPSHOT_24W14A("24w14a"),
        SNAPSHOT_24W13A("24w13a"),
        SNAPSHOT_24W11A("24w11a"),
        SNAPSHOT_24W10A("24w10a"),
        SNAPSHOT_24W09A("24w09a"),
        SNAPSHOT_24W07A("24w07a"),
        SNAPSHOT_24W06A("24w06a"),
        SNAPSHOT_24W05B("24w05b"),
        SNAPSHOT_24W05A("24w05a"),
        SNAPSHOT_23W51B("23w51b"),
        SNAPSHOT_23W46A("23w46a"),
        SNAPSHOT_23W42A("23w42a"),
        SNAPSHOT_23W41A("23w41a"),
        SNAPSHOT_23W35A("23w35a"),
        SNAPSHOT_23W33A("23w33a"),
        SNAPSHOT_23W32A("23w32a"),
        SNAPSHOT_23W31A("23w31a"),
        SNAPSHOT_23W18A("23w18a"),
        SNAPSHOT_23W14A("23w14a"),
        SNAPSHOT_23W13A("23w13a"),
        SNAPSHOT_23W12A("23w12a"),
        SNAPSHOT_23W07A("23w07a"),
        SNAPSHOT_23W06A("23w06a"),
        SNAPSHOT_23W05A("23w05a"),
        SNAPSHOT_23W04A("23w04a"),
        SNAPSHOT_22W46A("22w46a"),
        SNAPSHOT_22W45A("22w45a"),
        SNAPSHOT_22W44A("22w44a"),
        SNAPSHOT_22W43A("22w43a"),
        SNAPSHOT_22W42A("22w42a"),
        SNAPSHOT_22W18A("22w18a"),
        SNAPSHOT_22W17A("22w17a"),
        SNAPSHOT_22W16B("22w16b"),
        SNAPSHOT_22W15A("22w15a"),
        SNAPSHOT_22W14A("22w14a"),
        SNAPSHOT_22W12A("22w12a"),
        SNAPSHOT_22W11A("22w11a"),
        SNAPSHOT_22W07A("22w07a"),
        SNAPSHOT_22W06A("22w06a"),
        SNAPSHOT_22W05A("22w05a"),
        SNAPSHOT_22W03A("22w03a"),
        SNAPSHOT_21W44A("21w44a"),
        SNAPSHOT_21W43A("21w43a"),
        SNAPSHOT_21W42A("21w42a"),
        SNAPSHOT_21W41A("21w41a"),
        SNAPSHOT_21W39A("21w39a"),
        SNAPSHOT_21W37A("21w37a"),
        SNAPSHOT_21W20A("21w20a"),
        SNAPSHOT_21W19A("21w19a"),
        SNAPSHOT_21W18A("21w18a"),
        SNAPSHOT_21W17A("21w17a"),
        SNAPSHOT_21W16A("21w16a"),
        SNAPSHOT_21W15A("21w15a"),
        SNAPSHOT_21W14A("21w14a"),
        SNAPSHOT_21W13A("21w13a"),
        SNAPSHOT_13W43A("13w43a"),
        SNAPSHOT_13W37A("13w37a"),
        SNAPSHOT_18W45A("18w45a"),
        SNAPSHOT_18W44A("18w44a"),
        SNAPSHOT_21W10A("21W10A"),
        SNAPSHOT_21W08B("21W08B"),
        SNAPSHOT_21W08A("21W08A"),
        SNAPSHOT_21W07A("21W07A"),
        SNAPSHOT_19W14B("19W14B"),
        SNAPSHOT_21W06A("21w06a"),
        SNAPSHOT_21W05A("21w05a"),
        SNAPSHOT_21W03A("21w03a"),
        SNAPSHOT_20W51A("20w51a"),
        SNAPSHOT_20W49A("20w49a"),
        SNAPSHOT_20W48A("20w48a"),
        SNAPSHOT_20W46A("20w46a"),
        SNAPSHOT_20W45A("20w45a"),
        SNAPSHOT_20W30A("20w30a"),
        SNAPSHOT_20W29A("20w29a"),
        SNAPSHOT_20W28A("20w28a"),
        SNAPSHOT_20W27A("20w27a"),
        SNAPSHOT_20W22A("20w22a"),
        SNAPSHOT_20W21A("20w21a"),
        SNAPSHOT_20W20A("20w20a"),
        SNAPSHOT_20W19A("20w19a"),
        SNAPSHOT_20W18A("20w18a"),
        SNAPSHOT_20W17A("20w17a"),
        SNAPSHOT_20W16A("20w16a"),
        SNAPSHOT_20W14A("20w14a"),
        SNAPSHOT_20W15A("20w15a"),
        SNAPSHOT_20W10A("20w10a"),
        SNAPSHOT_20W09A("20w09a"),
        SNAPSHOT_20W06A("20w06a"),
        SNAPSHOT_19W39A("19w39a"),
        SNAPSHOT_19W38A("19w38a"),
        SNAPSHOT_19W37A("19w37a"),
        SNAPSHOT_19W36A("19w36a"),
        SNAPSHOT_19W35A("19w35a"),
        SNAPSHOT_19W09A("19w09a"),
        SNAPSHOT_19W08A("19w08a"),
        SNAPSHOT_18W50A("18w50a"),
        SNAPSHOT_18W02A("18w02a"),
        SNAPSHOT_17W18B("17w18b"),
        SNAPSHOT_17W17A("17w17a"),
        SNAPSHOT_17W16A("17w16a"),
        SNAPSHOT_17W15A("17w15a"),
        SNAPSHOT_17W14A("17w14a"),
        SNAPSHOT_17W13B("17w13b"),
        SNAPSHOT_17W13A("17w13a"),
        SNAPSHOT_17W06A("17w06a"),
        SNAPSHOT_16W32B("16w32b"),
        SNAPSHOT_16W33A("16w33a"),
        SNAPSHOT_16W35A("16w35a"),
        SNAPSHOT_16W36A("16w36a"),
        SNAPSHOT_16W38A("16w38a"),
        SNAPSHOT_16W39C("16w39c"),
        SNAPSHOT_16W40A("16w40a"),
        SNAPSHOT_16W41A("16w41a"),
        SNAPSHOT_16W42A("16w42a"),
        SNAPSHOT_16W43A("16w43a"),
        SNAPSHOT_16W44A("16w44a"),
        SNAPSHOT_16W50A("16w50a"),
        ;

        /**
         * The string representation of the Minecraft snapshot version.
         */
        private final String version;

        /**
         * Constructs a Snapshot enum constant with the specified version string.
         *
         * @param version the string representation of the Minecraft snapshot version
         */
        Snapshot(String version) {
            this.version = version;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String version() {
            return this.version;
        }
    }

    /**
     * Represents pre-release versions of Minecraft.
     * <p>
     * This enum contains all the pre-release versions of Minecraft supported by PolyglotMC.
     * Pre-releases are versions released shortly before an official release, typically for final testing.
     * </p>
     */
    public enum PreRelease implements Version {
        V1_21_1_RC1("1.21.1-rc1"),
        V1_21_PRE3("1.21-pre3"),
        V1_21_PRE1("1.21-pre1"),
        V1_20_5_PRE3("1.20.5-pre3"),
        V1_20_5_RC1("1.20.5-rc1"),
        V1_20_5_RC2("1.20.5-rc2"),
        V1_20_5_PRE4("1.20.5-pre4"),
        V1_20_5_PRE1("1.20.5-pre1"),
        V1_20_4_RC1("1.20.4-rc1"),
        V1_20_3_RC1("1.20.3-rc1"),
        V1_20_3_PRE2("1.20.3-pre2"),
        V1_20_2_PRE4("1.20.2-pre4"),
        V1_20_2_PRE1("1.20.2-pre1"),
        V1_20_RC1("1.20-rc1"),
        V1_20_PRE5("1.20-pre5"),
        V1_20_PRE2("1.20-pre2"),
        V1_20_PRE1("1.20-pre1"),
        V1_19_4_PRE4("1.19.4-pre4"),
        V1_19_4_PRE3("1.19.4-pre3"),
        V1_19_4_PRE1("1.19.4-pre1"),
        V1_19_3_PRE3("1.19.3-pre3"),
        V1_19_3_PRE2("1.19.3-pre2"),
        V1_19_1_RC3("1.19.1-rc3"),
        V1_19_1_PRE5("1.19.1-pre5"),
        V1_19_1_PRE4("1.19.1-pre4"),
        V1_19_1_PRE3("1.19.1-pre3"),
        V1_19_1_RC1("1.19.1-rc1"),
        V1_19_1_PRE1("1.19.1-pre1"),
        V1_19_RC2("1.19-rc2"),
        V1_19_RC1("1.19-rc1"),
        V1_19_PRE5("1.19-pre5"),
        V1_19_PRE3("1.19-pre3"),
        V1_19_PRE1("1.19-pre1"),
        V1_18_2_PRE3("1.18.2-pre3"),
        V1_18_1_RC2("1.18.1-rc2"),
        V1_18_PRE8("1.18-pre8"),
        V1_18_PRE3("1.18-pre3"),
        V1_17_1_PRE3("1.17.1-pre3"),
        V1_17_1_PRE1("1.17.1-pre1"),
        V1_17_PRE4("1.17-pre4"),
        V1_17_PRE3("1.17-pre3"),
        V1_17_PRE2("1.17-pre2"),
        V1_17_PRE1("1.17-pre1"),
        V1_16_5_RC1("1.16.5-rc1"),
        V1_16_4_RC1("1.16.4-rc1"),
        V1_16_4_PRE1("1.16.4-pre1"),
        V1_16_3_RC1("1.16.3-rc1"),
        V1_16_2_RC2("1.16.2-rc2"),
        V1_16_2_PRE2("1.16.2-pre2"),
        V1_16_2_PRE1("1.16.2-pre1"),
        V1_16_PRE8("1.16-pre8"),
        V1_16_PRE5("1.16-pre5"),
        V1_16_PRE3("1.16-pre3"),
        V1_16_PRE2("1.16-pre2"),
        V1_15_1_PRE1("1.15.1-pre1"),
        V1_14_4_PRE7("1.14.4-pre7"),
        V1_12_PRE5("1.12-pre5"),
        V1_12_PRE2("1.12-pre2"),
        ;

        /**
         * The string representation of the Minecraft pre-release version.
         */
        private final String version;

        /**
         * Constructs a PreRelease enum constant with the specified version string.
         *
         * @param version the string representation of the Minecraft pre-release version
         */
        PreRelease(String version) {
            this.version = version;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String version() {
            return this.version;
        }
    }

    /**
     * Finds a Version by its string representation.
     *
     * @param version the string representation of the version to search for
     * @return an Optional containing the Version if found, or an empty Optional if not found
     */
    public static Optional<Version> findByVersion(String version) {
        return Stream.of(
                        Arrays.stream(Release.values()),
                        Arrays.stream(Snapshot.values()),
                        Arrays.stream(PreRelease.values())
                )
                .flatMap(s -> s)
                .filter(v -> v.version().equalsIgnoreCase(version))
                .map(v -> (Version) v)
                .findFirst();

    }
}
