package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Song
import com.example.data.model.SongSection
import com.example.data.model.SongVersion
import com.example.data.model.VoiceRecording
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Song::class,
        SongSection::class,
        SongVersion::class,
        VoiceRecording::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "studio_taraneh_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed initial introductory sample song for first-time artists
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialSample(database.songDao())
                    }
                }
            }

            suspend fun populateInitialSample(dao: SongDao) {
                val sampleSong = Song(
                    title = "آخرین شب (Night Out)",
                    artist = "سیدحمید موسوی زاده",
                    genre = "Rap",
                    subgenre = "ملودیک رپ (Melodic Rap)",
                    status = "در حال تکمیل",
                    bpm = 135,
                    timeSignature = "4/4",
                    tags = "رپ, احساسی, شبانه, ملودیک",
                    description = "پروژه ترانه استودیو ترانه با ساختار ورس و کروس حرفه‌ای",
                    notes = "تست بیت ترپ ملودیک، اجرای پرکاشن در ثانیه ۴۰"
                )
                val songId = dao.insertSong(sampleSong)

                val sections = listOf(
                    SongSection(
                        songId = songId,
                        sectionType = "Intro",
                        customTitle = "مقدمه (Intro)",
                        content = "چراغای شهر آروم خاموش میشن یکی یکی\nصدای بارون روی شیشه، شبِ تاریک و بی‌کسی...",
                        colorHex = "#10B981",
                        orderIndex = 0
                    ),
                    SongSection(
                        songId = songId,
                        sectionType = "Verse",
                        customTitle = "بند اول (Verse 1)",
                        content = "ساعت از دو گذشته و کاغذ پر از خط خطیه\nقافیه‌ها تو ذهنم میچرخه، انگار این رسم بازیه\nقدم میزنم تو این خیابونای خلوت و سرد\nتنها همدمم صدای پای خسته از درد...",
                        colorHex = "#38BDF8",
                        orderIndex = 1
                    ),
                    SongSection(
                        songId = songId,
                        sectionType = "Chorus",
                        customTitle = "هم‌خوان (Chorus)",
                        content = "این آخرین شبه که برات مینویسم\nبا دستایی که می‌لرزه و چشمای خیسم\nاگه فردا صبح ندیدی رد پامو تو کوچه\nبدون ترانه‌هام فقط واسه تو بوده...",
                        colorHex = "#EC4899",
                        orderIndex = 2
                    ),
                    SongSection(
                        songId = songId,
                        sectionType = "Bridge",
                        customTitle = "پل (Bridge)",
                        content = "صداها گم میشن تو عمق تاریکی و دود\nکاشکی یکی بود که قدر احساسم رو میدونست زود...",
                        colorHex = "#A855F7",
                        orderIndex = 3
                    ),
                    SongSection(
                        songId = songId,
                        sectionType = "Outro",
                        customTitle = "پایان (Outro)",
                        content = "استودیو ترانه... پایان آخرین شب...",
                        colorHex = "#6366F1",
                        orderIndex = 4
                    )
                )
                dao.insertSections(sections)
            }
        }
    }
}
