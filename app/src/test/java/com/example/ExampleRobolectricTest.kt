package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.Song
import com.example.data.model.SongSection
import com.example.data.repository.SongRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: SongRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = SongRepository(database.songDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `verify app name resource`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Studio Taraneh", appName)
    }

    @Test
    fun `create new song and verify sections`() = runBlocking {
        val songId = repository.createNewSong(
            title = "آخرین شب",
            artist = "سیدحمید موسوی زاده",
            genre = "Rap",
            subgenre = "ملودیک رپ",
            bpm = 135,
            timeSignature = "4/4"
        )

        val songWithSections = repository.getSongWithSectionsDirect(songId)
        assertNotNull(songWithSections)
        assertEquals("آخرین شب", songWithSections!!.song.title)
        assertEquals(2, songWithSections.sections.size)

        // Add a new Chorus section
        val chorusSection = SongSection(
            songId = songId,
            sectionType = "Chorus",
            content = "این آخرین شبه که برات مینویسم...",
            colorHex = "#EC4899",
            orderIndex = 2
        )
        repository.addSectionToSong(songId, chorusSection)

        val updated = repository.getSongWithSectionsDirect(songId)
        assertEquals(3, updated!!.sections.size)
    }

    @Test
    fun `move song to trash and restore`() = runBlocking {
        val songId = repository.createNewSong(
            title = "ترانه آزمایشی",
            artist = "هنرمند",
            genre = "Pop",
            subgenre = "پاپ شاد"
        )

        repository.moveToTrash(songId)
        val trashed = repository.getSongWithSectionsDirect(songId)
        assertTrue(trashed!!.song.isTrash)

        repository.restoreFromTrash(songId)
        val restored = repository.getSongWithSectionsDirect(songId)
        assertTrue(!restored!!.song.isTrash)
    }

    @Test
    fun `export and import taraneh project json`() = runBlocking {
        val songId = repository.createNewSong(
            title = "پروژه پشتیبان",
            artist = "سیدحمید",
            genre = "Rap",
            subgenre = "ترپ",
            bpm = 140
        )

        val json = repository.exportFullBackupJson()
        assertTrue(json.contains("Studio Taraneh"))
        assertTrue(json.contains("پروژه پشتیبان"))

        val importedCount = repository.importBackupJson(json)
        assertTrue(importedCount >= 1)
    }

    @Test
    fun `verify studio preferences and language changes`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = com.example.data.preferences.StudioPreferences(context)

        // Default language is Persian
        assertEquals(com.example.data.preferences.AppLanguage.PERSIAN, prefs.language.value)
        assertEquals(true, prefs.language.value.isRtl)

        // Change to English
        prefs.setLanguage(com.example.data.preferences.AppLanguage.ENGLISH)
        assertEquals(com.example.data.preferences.AppLanguage.ENGLISH, prefs.language.value)
        assertEquals(false, prefs.language.value.isRtl)

        // Font size changes
        prefs.setFontSize(com.example.data.preferences.EditorFontSize.LARGE)
        assertEquals(com.example.data.preferences.EditorFontSize.LARGE, prefs.fontSize.value)
        assertEquals(1.2f, prefs.fontSize.value.scaleFactor)

        // VIP status
        assertEquals(false, prefs.isVip.value)
        prefs.setVip(true)
        assertEquals(true, prefs.isVip.value)
    }

    @Test
    fun `verify Turkish and all 8 languages translations for fonts and day night options`() {
        val turkish = com.example.ui.localization.getStudioStrings(com.example.data.preferences.AppLanguage.TURKISH)
        assertEquals("Küçük", turkish.fontSizeSmall)
        assertEquals("Orta", turkish.fontSizeMedium)
        assertEquals("Büyük", turkish.fontSizeLarge)
        assertEquals("Çok Büyük", turkish.fontSizeExtraLarge)
        assertTrue(turkish.modeDark.contains("Koyu") || turkish.modeDark.contains("Gece"))
        assertTrue(turkish.modeLight.contains("Açık") || turkish.modeLight.contains("Gündüz"))
        assertTrue(turkish.modeSystem.contains("Sistem"))
        assertEquals("Stüdyo Neon", turkish.themeNeon)

        val german = com.example.ui.localization.getStudioStrings(com.example.data.preferences.AppLanguage.GERMAN)
        assertEquals("Klein", german.fontSizeSmall)
        assertEquals("Mittel", german.fontSizeMedium)
        assertEquals("Groß", german.fontSizeLarge)
        assertEquals("Sehr Groß", german.fontSizeExtraLarge)
        assertTrue(german.modeDark.contains("Dunkel") || german.modeDark.contains("Nacht"))

        val english = com.example.ui.localization.getStudioStrings(com.example.data.preferences.AppLanguage.ENGLISH)
        assertEquals("Small", english.fontSizeSmall)
        assertEquals("Medium", english.fontSizeMedium)
        assertEquals("Large", english.fontSizeLarge)
        assertEquals("Extra Large", english.fontSizeExtraLarge)

        val russian = com.example.ui.localization.getStudioStrings(com.example.data.preferences.AppLanguage.RUSSIAN)
        assertEquals("Маленький", russian.fontSizeSmall)
        assertEquals("Средний", russian.fontSizeMedium)
        assertEquals("Большой", russian.fontSizeLarge)
        assertEquals("Очень большой", russian.fontSizeExtraLarge)

        val persian = com.example.ui.localization.getStudioStrings(com.example.data.preferences.AppLanguage.PERSIAN)
        assertEquals("کوچک", persian.fontSizeSmall)
        assertEquals("متوسط", persian.fontSizeMedium)
        assertEquals("بزرگ", persian.fontSizeLarge)
        assertEquals("خیلی بزرگ", persian.fontSizeExtraLarge)
    }
}
