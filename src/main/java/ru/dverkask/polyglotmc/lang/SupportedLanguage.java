package ru.dverkask.polyglotmc.lang;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enumeration of supported languages in the PolyglotMC library.
 * <p>
 * This enum represents all the languages that are supported for translation
 * within the PolyglotMC system. Each enum constant corresponds to a specific
 * language or locale, identified by a unique language code.
 * </p>
 * <p>
 * The language codes follow the format of language code (lowercase) followed by
 * country code (uppercase), separated by an underscore. For example, "en_US" for
 * English (United States). Some languages may not have a country code.
 * </p>
 * <p>
 * Usage example:
 * <pre>
 * SupportedLanguage lang = SupportedLanguage.EN_US;
 * String code = lang.getCode(); // Returns "en_us"
 * </pre>
 *
 * @author DverkaSK
 * @version 1.0
 * @since 1.0
 */
public enum SupportedLanguage {
    AF_ZA("af_za"),
    AR_SA("ar_sa"),
    AST_ES("ast_es"),
    AZ_AZ("az_az"),
    BA_RU("ba_ru"),
    BAR("bar"),
    BE_BY("be_by"),
    BG_BG("bg_bg"),
    BR_FR("br_fr"),
    BRB("brb"),
    BS_BA("bs_ba"),
    CA_ES("ca_es"),
    CS_CZ("cs_cz"),
    CY_GB("cy_gb"),
    DA_DK("da_dk"),
    DE_AT("de_at"),
    DE_CH("de_ch"),
    DE_DE("de_de"),
    EL_GR("el_gr"),
    EN_AU("en_au"),
    EN_CA("en_ca"),
    EN_GB("en_gb"),
    EN_NZ("en_nz"),
    EN_PT("en_pt"),
    EN_UD("en_ud"),
    EN_US("en_us"),
    ENP("enp"),
    ENWS("enws"),
    EO_UY("eo_uy"),
    ES_AR("es_ar"),
    ES_CL("es_cl"),
    ES_EC("es_ec"),
    ES_ES("es_es"),
    ES_MX("es_mx"),
    ES_UY("es_uy"),
    ES_VE("es_ve"),
    ESAN("esan"),
    ET_EE("et_ee"),
    EU_ES("eu_es"),
    FA_IR("fa_ir"),
    FI_FI("fi_fi"),
    FIL_PH("fil_ph"),
    FO_FO("fo_fo"),
    FRA_DE("fra_de"),
    FUR_IT("fur_it"),
    FY_NL("fy_nl"),
    GA_IE("ga_ie"),
    GD_GB("gd_gb"),
    GL_ES("gl_es"),
    HAW_US("haw_us"),
    HE_IL("he_il"),
    HI_IN("hi_in"),
    HR_HR("hr_hr"),
    HU_HU("hu_hu"),
    HY_AM("hy_am"),
    ID_ID("id_id"),
    IG_NG("ig_ng"),
    IO_EN("io_en"),
    IS_IS("is_is"),
    ISV("isv"),
    IT_IT("it_it"),
    JA_JP("ja_jp"),
    JBO_EN("jbo_en"),
    KA_GE("ka_ge"),
    KK_KZ("kk_kz"),
    KN_IN("kn_in"),
    KO_KR("ko_kr"),
    KSH("ksh"),
    KW_GB("kw_gb"),
    LA_LA("la_la"),
    LB_LU("lb_lu"),
    LI_LI("li_li"),
    LMO("lmo"),
    LO_LA("lo_la"),
    LOL_US("lol_us"),
    LT_LT("lt_lt"),
    LV_LV("lv_lv"),
    LZH("lzh"),
    MK_MK("mk_mk"),
    MN_MN("mn_mn"),
    MS_MY("ms_my"),
    MT_MT("mt_mt"),
    NAH("nah"),
    NDS_DE("nds_de"),
    NL_BE("nl_be"),
    NL_NL("nl_nl"),
    NN_NO("nn_no"),
    NO_NO("no_no"),
    OC_FR("oc_fr"),
    OVD("ovd"),
    PL_PL("pl_pl"),
    PT_BR("pt_br"),
    PT_PT("pt_pt"),
    QYA_AA("qya_aa"),
    RO_RO("ro_ro"),
    RPR("rpr"),
    RU_RU("ru_ru"),
    RY_UA("ry_ua"),
    SAH_SAH("sah_sah"),
    SE_NO("se_no"),
    SK_SK("sk_sk"),
    SL_SL("sl_sl"),
    SO_SO("so_so"),
    SQ_AL("sq_al"),
    SR_CS("sr_cs"),
    SR_SP("sr_sp"),
    SV_SE("sv_se"),
    SXU("sxu"),
    SZL("szl"),
    TA_IN("ta_in"),
    TH_TH("th_th"),
    TL_PH("tl_ph"),
    TLH_AA("tlh_aa"),
    TOK("tok"),
    TR_TR("tr_tr"),
    TT_RU("tt_ru"),
    UK_UA("uk_ua"),
    VAL_ES("val_es"),
    VEC_IT("vec_it"),
    VI_VN("vi_vn"),
    YI_DE("yi_de"),
    YO_NG("yo_ng"),
    ZH_CN("zh_cn"),
    ZH_HK("zh_hk"),
    ZH_TW("zh_tw"),
    ZLM_ARAB("zlm_arab")
    ;

    /**
     * The language code associated with this language.
     */
    private final String code;

    /**
     * Constructs a SupportedLanguage enum constant.
     *
     * @param code The language code for this language.
     */
    SupportedLanguage(String code) {
        this.code = code;
    }

    /**
     * Returns the language code for this supported language.
     *
     * @return A string representing the language code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Finds a SupportedLanguage by its code.
     *
     * @param code the language code to search for
     * @return an Optional containing the SupportedLanguage if found, or an empty Optional if not found
     */
    public static Optional<SupportedLanguage> findByCode(String code) {
        return Arrays.stream(SupportedLanguage.values())
                .filter(lang -> lang.getCode().equalsIgnoreCase(code))
                .findFirst();
    }
}
