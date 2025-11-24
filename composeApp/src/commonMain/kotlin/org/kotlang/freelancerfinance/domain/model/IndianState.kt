package org.kotlang.freelancerfinance.domain.model

enum class IndianState(val code: Int, val stateName: String) {
    JAMMU_AND_KASHMIR(1, "Jammu and Kashmir"),
    HIMACHAL_PRADESH(2, "Himachal Pradesh"),
    PUNJAB(3, "Punjab"),
    CHANDIGARH(4, "Chandigarh"),
    UTTARAKHAND(5, "Uttarakhand"),
    HARYANA(6, "Haryana"),
    DELHI(7, "Delhi"),
    RAJASTHAN(8, "Rajasthan"),
    UTTAR_PRADESH(9, "Uttar Pradesh"),
    BIHAR(10, "Bihar"),
    SIKKIM(11, "Sikkim"),
    ARUNACHAL_PRADESH(12, "Arunachal Pradesh"),
    NAGALAND(13, "Nagaland"),
    MANIPUR(14, "Manipur"),
    MIZORAM(15, "Mizoram"),
    TRIPURA(16, "Tripura"),
    MEGHALAYA(17, "Meghalaya"),
    ASSAM(18, "Assam"),
    WEST_BENGAL(19, "West Bengal"),
    JHARKHAND(20, "Jharkhand"),
    ODISHA(21, "Odisha"),
    CHHATTISGARH(22, "Chhattisgarh"),
    MADHYA_PRADESH(23, "Madhya Pradesh"),
    GUJARAT(24, "Gujarat"),
    DADRA_NAGAR_HAVELI_DAMAN_DIU(26, "Dadra and Nagar Haveli and Daman and Diu"),
    MAHARASHTRA(27, "Maharashtra"),
    KARNATAKA(29, "Karnataka"),
    GOA(30, "Goa"),
    LAKSHADWEEP(31, "Lakshadweep"),
    KERALA(32, "Kerala"),
    TAMIL_NADU(33, "Tamil Nadu"),
    PUDUCHERRY(34, "Puducherry"),
    ANDAMAN_AND_NICOBAR(35, "Andaman and Nicobar Islands"),
    TELANGANA(36, "Telangana"),
    ANDHRA_PRADESH(37, "Andhra Pradesh"),
    LADAKH(38, "Ladakh"),
    OTHER_TERRITORY(97, "Other Territory"),
    CENTRE_JURISDICTION(99, "Centre Jurisdiction");

    val formattedCode: String
        get() = code.toString().padStart(2, '0')

    companion object {
        private val map = entries.associateBy(IndianState::code)

        fun fromCode(code: Int): IndianState? {
            return map[code]
        }
    }
}