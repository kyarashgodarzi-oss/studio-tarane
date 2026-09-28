package com.example.data.model

data class GenreItem(
    val id: String,
    val nameFa: String,
    val nameEn: String,
    val subgenres: List<String>
)

object GenreCatalog {
    val popSubgenres = listOf(
        "پاپ عاشقانه (Romantic)",
        "پاپ احساسی (Emotional)",
        "پاپ غمگین (Sad Pop)",
        "پاپ شاد (Happy Pop)",
        "پاپ مدرن (Modern Pop)",
        "پاپ کلاسیک (Classic Pop)",
        "پاپ راک (Pop Rock)",
        "پاپ سنتی (Fusion Pop)"
    )

    val rapSubgenres = listOf(
        "ملودیک رپ (Melodic Rap)",
        "ترپ (Trap)",
        "دریل (Drill)",
        "اولد اسکول (Old School)",
        "زیرزمینی (Underground)",
        "مفهومی و اجتماعی (Conscious)",
        "گنگستا (Gangsta)",
        "فری‌استایل (Freestyle)",
        "بوم‌بپ (Boom Bap)"
    )

    val mainGenres = listOf(
        GenreItem("Pop", "پاپ", "Pop", popSubgenres),
        GenreItem("Rap", "رپ و هیپ‌هاپ", "Rap", rapSubgenres),
        GenreItem("Rock", "راک", "Rock", listOf("آلترناتیو", "هارد راک", "پاپ راک")),
        GenreItem("Traditional", "سنتی و تلفیقی", "Fusion", listOf("تلفیقی", "کلاسیک ایرانی", "عرفانی")),
        GenreItem("Custom", "سبک دلخواه", "Custom", listOf("دلخواه"))
    )

    fun getDefaultColorForSection(type: String): String {
        return when (type) {
            "Chorus" -> "#EC4899"    // Pink
            "Verse" -> "#38BDF8"     // Cyan / Sky
            "PreChorus" -> "#F59E0B" // Amber
            "Bridge" -> "#A855F7"    // Purple
            "Hook" -> "#06B6D4"      // Neon Cyan
            "Intro" -> "#10B981"     // Emerald
            "Outro" -> "#6366F1"     // Indigo
            "Refrain" -> "#D946EF"   // Fuchsia
            else -> "#CBD5E1"        // Silver White
        }
    }

    val availableColors = listOf(
        "#EC4899", // Neon Pink
        "#A855F7", // Neon Purple
        "#06B6D4", // Neon Cyan
        "#38BDF8", // Sky Blue
        "#10B981", // Emerald Green
        "#F59E0B", // Amber Gold
        "#EF4444", // Coral Red
        "#F43F5E", // Rose
        "#8B5CF6", // Violet
        "#FFFFFF", // Pure White
        "#94A3B8"  // Slate Gray
    )
}
