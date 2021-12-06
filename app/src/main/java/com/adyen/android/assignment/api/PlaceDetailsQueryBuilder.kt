package com.adyen.android.assignment.api


class PlaceDetailsQueryBuilder : PlacesQueryBuilder() {
    var fourSquareId : String = ""
    var fields : String? = null
    private val desiredFields = Fields.values()

    fun setFields() {
        val stringBuilder = StringBuilder()
        desiredFields.forEach {
            stringBuilder.append("${it.field},")
        }
        fields = stringBuilder.toString()
    }

    override fun putQueryParams(queryParams: MutableMap<String, String>) {
        fourSquareId.apply { queryParams["fsq_id"] = this }
        fields?.apply { queryParams["fields"] = this }
    }

}


enum class Fields(val field: String) {
    TEL("tel"),
    PHOTO("photos"),
    EMAIL("email"),
    GEOCODES("geocodes"),
    LOCATION("location"),
    CATEGORY("categories"),
    TIMEZONE("timezone"),
    DISTANCE("distance"),
    DESC("description"),
    WEBSITE("website"),
    RATING("rating"),
    PRICE("price"),
    NAME("name"),
    ID("fsq_id")
}