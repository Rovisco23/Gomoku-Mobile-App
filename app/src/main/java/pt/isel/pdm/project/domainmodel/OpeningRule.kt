package pt.isel.pdm.project.domainmodel

enum class OpeningRule {
    PRO, LONG_PRO, FREE_STYLE;

    override fun toString(): String {
        return when {
            this == PRO -> "Pro"
            this == LONG_PRO -> "Long Pro"
            this == FREE_STYLE -> "Free Style"
            else -> throw IllegalArgumentException("Invalid value for OpeningRule")
        }
    }
}