package com.example.ui.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.data.preferences.AppLanguage

interface StudioStrings {
    // App info & Splash
    val appName: String
    val appTitle: String
    val creatorName: String
    val appVersion: String
    val appSubtitle: String

    // Main Menu Hub
    val menuMainTitle: String
    val menuMySongs: String
    val menuMySongsSub: String
    val menuNewSong: String
    val menuNewSongSub: String
    val menuVoiceRecording: String
    val menuVoiceRecordingSub: String
    val menuRhythmMaker: String
    val menuRhythmMakerSub: String
    val menuFavorites: String
    val menuFavoritesSub: String
    val menuRecent: String
    val menuRecentSub: String
    val menuTrash: String
    val menuTrashSub: String
    val menuBackup: String
    val menuBackupSub: String
    val menuSettings: String
    val menuSettingsSub: String
    val menuVip: String
    val menuVipSub: String
    val vipActiveBadge: String

    // Song Editor & Notebook
    val searchHint: String
    val allGenres: String
    val noSongsFound: String
    val noFavoritesFound: String
    val noTrashFound: String
    val noRecordingsFound: String
    val songTitle: String
    val artistName: String
    val genreLabel: String
    val subgenreLabel: String
    val bpmLabel: String
    val timeSignatureLabel: String
    val tagsLabel: String
    val notesLabel: String
    val songCreatedSuccess: String

    // Section Types
    val sectionIntro: String
    val sectionVerse: String
    val sectionChorus: String
    val sectionPreChorus: String
    val sectionBridge: String
    val sectionHook: String
    val sectionOutro: String
    val sectionRefrain: String
    val sectionCustom: String

    // Actions & Tools
    val actionSave: String
    val actionCancel: String
    val actionDelete: String
    val actionPermanentDelete: String
    val actionRestore: String
    val actionEmptyTrash: String
    val actionCopyAll: String
    val actionCopySection: String
    val actionShare: String
    val actionUndo: String
    val actionRedo: String
    val actionRecord: String
    val actionStopRecord: String
    val actionPlay: String
    val actionPause: String
    val actionBack: String
    val actionCreate: String
    val actionAdd: String
    val actionClose: String

    // Voice Studio
    val voiceStudioTitle: String
    val voiceStudioSubtitle: String
    val readyToRecord: String
    val recordingActive: String
    val savedRecordingsTitle: String
    val saveVoiceDialogTitle: String
    val micPermissionRequired: String

    // Rhythm Maker & Metronome
    val rhythmMakerTitle: String
    val rhythmMakerSubtitle: String
    val tapTempoBtn: String
    val hapticOn: String
    val hapticOff: String
    val stepSequencerTitle: String
    val soundEffectsTitle: String

    // Settings
    val settingsTitle: String
    val settingsSubtitle: String
    val themeSectionTitle: String
    val dayNightSectionTitle: String
    val fontSizeSectionTitle: String
    val languageSectionTitle: String
    val livePreviewTitle: String
    val livePreviewSample: String
    val livePreviewVerse: String
    val adsPrivacyTitle: String
    val tapsellAdsTitle: String
    val tapsellSubtitle: String
    val tapsellVipRemoved: String
    val tapsellAdsBanner: String
    val aboutTitle: String
    val termsTitle: String
    val privacyTitle: String

    // Font Sizes
    val fontSizeSmall: String
    val fontSizeMedium: String
    val fontSizeLarge: String
    val fontSizeExtraLarge: String

    // Day / Night Modes
    val modeDark: String
    val modeLight: String
    val modeSystem: String

    // Theme Names
    val themeNeon: String
    val themeMidnight: String
    val themeGraphite: String
    val themePurpleNight: String
    val themeSilverStudio: String
    val themeAmoled: String

    // Legal, About & Dialogs
    val aboutCreatorTile: String
    val termsTile: String
    val termsSubtitle: String
    val privacyTile: String
    val privacySubtitle: String
    val aboutDescription: String
    val aboutDeveloper: String
    val aboutVersion: String
    val aboutFocus: String
    val aboutNoAiNotice: String
    val termsP1: String
    val termsP2: String
    val termsP3: String
    val privacyDialogTitle: String
    val privacyDialogContent: String
    val understood: String

    // Trash & Extra
    val trashDialogTitle: String
    val trashDialogConfirm: String
    val trashEmptied: String

    // Backup & VIP
    val backupTitle: String
    val backupSubtitle: String
    val exportTaraneh: String
    val exportTxt: String
    val shareProjectJson: String
    val restoreProject: String
    val vipScreenTitle: String
    val vipScreenSubtitle: String
    val buyFromBazaar: String
    val restorePurchase: String

    // Convenience aliases
    val mySongs: String get() = menuMySongs
    val mySongsSubtitle: String get() = menuMySongsSub
    val newSong: String get() = menuNewSong
    val newSongSubtitle: String get() = menuNewSongSub
    val voiceStudio: String get() = menuVoiceRecording
    val rhythmHub: String get() = menuRhythmMaker
    val rhythmHubSubtitle: String get() = menuRhythmMakerSub
    val favorites: String get() = menuFavorites
    val favoritesSubtitle: String get() = menuFavoritesSub
    val recent: String get() = menuRecent
    val recentSubtitle: String get() = menuRecentSub
    val trash: String get() = menuTrash
    val trashSubtitle: String get() = menuTrashSub
    val backupExport: String get() = menuBackup
    val vipHubTitle: String get() = menuVip
    val vipHubSubtitle: String get() = menuVipSub
    val versionName: String get() = appVersion
    val mainSectionsTitle: String get() = menuMainTitle
    val cancel: String get() = actionCancel
    val close: String get() = actionClose
    val languageSettingTitle: String get() = languageSectionTitle
    val themeSettingTitle: String get() = themeSectionTitle
    val dayNightTitle: String get() = dayNightSectionTitle
    val fontSizeTitle: String get() = fontSizeSectionTitle
    val fontPreviewTitle: String get() = livePreviewTitle
    val fontPreviewSample: String get() = livePreviewVerse
    val tapsellTitle: String get() = tapsellAdsTitle
    val tapsellBannerText: String get() = tapsellAdsBanner
    val aboutAndLegalTitle: String get() = aboutTitle
}

// 1. 🇮🇷 PERSIAN (Default & Primary)
object PersianStrings : StudioStrings {
    override val appName = "استودیو ترانه"
    override val appTitle = "استودیو ترانه"
    override val creatorName = "سیدحمید موسوی زاده"
    override val appVersion = "نسخه 1.0.0"
    override val appSubtitle = "دفترچه تخصصی شعر، رپ، پاپ و ضبط صدا"

    override val menuMainTitle = "بخش‌های اصلی استودیو"
    override val menuMySongs = "ترانه‌های من"
    override val menuMySongsSub = "آرشیو اشعار و قطعات"
    override val menuNewSong = "ترانه جدید"
    override val menuNewSongSub = "نگارش شعر با ساختار حرفه‌ای"
    override val menuVoiceRecording = "استودیو ضبط صدا"
    override val menuVoiceRecordingSub = "ضبط نامحدود ایده‌ها و وکال"
    override val menuRhythmMaker = "ریتم‌ساز و مترونوم"
    override val menuRhythmMakerSub = "ضرب‌آهنگ، تمپو و بیت‌ساز"
    override val menuFavorites = "علاقه‌مندی‌ها"
    override val menuFavoritesSub = "ترانه‌های برگزیده و ستاره‌دار"
    override val menuRecent = "ترانه‌های اخیر"
    override val menuRecentSub = "آخرین ویرایش‌ها و تغییرات"
    override val menuTrash = "سطل زباله"
    override val menuTrashSub = "بازیابی یا حذف دائمی"
    override val menuBackup = "پشتیبان‌گیری"
    override val menuBackupSub = "خروجی .taraneh، TXT و بازیابی"
    override val menuSettings = "تنظیمات استودیو"
    override val menuSettingsSub = "پوسته‌ها، زبان، فونت و درباره"
    override val menuVip = "اشتراک ویژه VIP"
    override val menuVipSub = "امکانات نامحدود و بدون تبلیغات"
    override val vipActiveBadge = "حساب طلایی فعال"

    override val searchHint = "جستجو در عنوان، متن شعر، سبک و برچسب‌ها..."
    override val allGenres = "همه سبک‌ها"
    override val noSongsFound = "هیچ ترانه‌ای در این بخش وجود ندارد"
    override val noFavoritesFound = "هیچ ترانه‌ای در لیست علاقه‌مندی‌ها نیست"
    override val noTrashFound = "سطل زباله خالی است"
    override val noRecordingsFound = "هنوز صدایی ضبط نشده است"
    override val songTitle = "عنوان ترانه"
    override val artistName = "نام خواننده یا ترانه‌سرا"
    override val genreLabel = "سبک اصلی"
    override val subgenreLabel = "زیرسبک (Subgenre)"
    override val bpmLabel = "ضرب‌آهنگ (BPM)"
    override val timeSignatureLabel = "میزان‌نما"
    override val tagsLabel = "برچسب‌ها"
    override val notesLabel = "یادداشت‌ها و توضیحات"
    override val songCreatedSuccess = "ترانه ایجاد شد و آماده نگارش است"

    override val sectionIntro = "مقدمه (Intro)"
    override val sectionVerse = "بند / ورس (Verse)"
    override val sectionChorus = "هم‌خوان / کروس (Chorus)"
    override val sectionPreChorus = "پیش‌هم‌خوان (Pre-Chorus)"
    override val sectionBridge = "پل (Bridge)"
    override val sectionHook = "هوک (Hook)"
    override val sectionOutro = "بخش پایانی (Outro)"
    override val sectionRefrain = "ترجیع‌بند (Refrain)"
    override val sectionCustom = "بخش دلخواه"

    override val actionSave = "ذخیره"
    override val actionCancel = "انصراف"
    override val actionDelete = "حذف"
    override val actionPermanentDelete = "حذف دائمی"
    override val actionRestore = "بازیابی"
    override val actionEmptyTrash = "تخلیه کامل سطل زباله"
    override val actionCopyAll = "کپی متن کامل ترانه"
    override val actionCopySection = "کپی این بخش"
    override val actionShare = "اشتراک‌گذاری"
    override val actionUndo = "بازگشت"
    override val actionRedo = "تکرار مجدد"
    override val actionRecord = "ضبط صدا"
    override val actionStopRecord = "اتمام ضبط"
    override val actionPlay = "پخش"
    override val actionPause = "مکث"
    override val actionBack = "بازگشت"
    override val actionCreate = "ایجاد و ورود به دفترچه"
    override val actionAdd = "افزودن"
    override val actionClose = "بستن"

    override val voiceStudioTitle = "🎙 استودیو ضبط صدا و ملودی"
    override val voiceStudioSubtitle = "ضبط نامحدود ایده‌ها، اتودها و اجرای وکال"
    override val readyToRecord = "آماده برای ضبط ایده"
    override val recordingActive = "در حال ضبط صدا... دکمه توقف را بزنید"
    override val savedRecordingsTitle = "آرشیو صداها و ملودی‌های ضبط شده"
    override val saveVoiceDialogTitle = "ذخیره فایل صوتی در آرشیو"
    override val micPermissionRequired = "مجوز دسترسی به میکروفون نیاز است"

    override val rhythmMakerTitle = "🎵 ریتم‌ساز و مترونوم پیشرفته"
    override val rhythmMakerSubtitle = "ضرب‌آهنگ، بیت‌باکس و سنجش تمپو با تپ"
    override val tapTempoBtn = "تشخیص با ضربه (Tap Tempo)"
    override val hapticOn = "لرزش فعال"
    override val hapticOff = "لرزش خاموش"
    override val stepSequencerTitle = "🥁 بیت‌ساز استودیو (Step Sequencer)"
    override val soundEffectsTitle = "🔊 کتابخانه افکت‌های صوتی استودیو"

    override val settingsTitle = "⚙️ تنظیمات استودیو"
    override val settingsSubtitle = "پوسته‌ها، ظاهر، اندازه قلم، زبان و درباره"
    override val themeSectionTitle = "پوسته‌های اختصاصی استودیو (Theme)"
    override val dayNightSectionTitle = "حالت شب و روز (Day / Night)"
    override val fontSizeSectionTitle = "اندازه قلم ویرایشگر (Font Size)"
    override val languageSectionTitle = "زبان برنامه (Language)"
    override val livePreviewTitle = "پیش‌نمایش زنده قلم (Live Preview)"
    override val livePreviewSample = "اندازه قلم متن به صورت آنی تغییر می‌یابد:"
    override val livePreviewVerse = "«این آخرین شبه که برات می‌نویسم... با دستایی که می‌لرزه و چشمای خیسم»"
    override val adsPrivacyTitle = "تبلیغات و حریم خصوصی"
    override val tapsellAdsTitle = "سیستم تبلیغات تپسّل (Tapsell Ads)"
    override val tapsellSubtitle = "تبلیغات غیرمزاحم جهت حمایت از توسعه برنامه"
    override val tapsellVipRemoved = "تبلیغات در نسخه طلایی VIP حذف شده است"
    override val tapsellAdsBanner = "📢 بنر تبلیغاتی تپسّل (غیرمزاحم در محیط نوشتن)"
    override val aboutTitle = "درباره سازنده و استودیو ترانه"
    override val termsTitle = "قوانین و شرایط استفاده"
    override val privacyTitle = "حریم خصوصی و ذخیره‌سازی محلی"

    // Font Sizes
    override val fontSizeSmall = "کوچک"
    override val fontSizeMedium = "متوسط"
    override val fontSizeLarge = "بزرگ"
    override val fontSizeExtraLarge = "خیلی بزرگ"

    // Day / Night Modes
    override val modeDark = "🌙 تاریک (شب)"
    override val modeLight = "☀️ روشن (روز)"
    override val modeSystem = "⚙️ خودکار با سیستم"

    // Theme Names
    override val themeNeon = "استودیو نئون"
    override val themeMidnight = "نیمه‌شب"
    override val themeGraphite = "گرافیتی"
    override val themePurpleNight = "شب بنفش"
    override val themeSilverStudio = "استودیو نقره‌ای"
    override val themeAmoled = "مشکی مطلق (AMOLED)"

    // Legal, About & Dialogs
    override val aboutCreatorTile = "درباره سازنده و استودیو ترانه"
    override val termsTile = "قوانین و شرایط استفاده"
    override val termsSubtitle = "قوانین و شرایط استفاده از محتوای شعر و کپی‌رایت ترانه‌ها"
    override val privacyTile = "حریم خصوصی و ذخیره‌سازی محلی"
    override val privacySubtitle = "اطلاعات شما فقط بر روی دستگاه خودتان ذخیره می‌شود"
    override val aboutDescription = "دفترچه تخصصی شعر، رپ، پاپ و استودیو ضبط صدا"
    override val aboutDeveloper = "طراح و توسعه‌دهنده"
    override val aboutVersion = "نسخه"
    override val aboutFocus = "تمرکز: پاپ، رپ، دریل، ترپ، هیپ‌هاپ"
    override val aboutNoAiNotice = "این برنامه بدون وابستگی به هوش مصنوعی یا سرور ابری، به عنوان ابزاری کاملاً محلی برای ترانه‌سرایان مستقل طراحی شده است."
    override val termsP1 = "۱. کلیه حقوق مادی و معنوی اشعار و ملودی‌های نوشته شده متعلق به خود کاربر است."
    override val termsP2 = "۲. برنامه مسئولیتی در قبال حفظ فایل‌ها بدون تهیه نسخه پشتیبان بر عهده ندارد."
    override val termsP3 = "۳. اشتراک ویژه از طریق سامانه درون‌برنامه‌ای کافه‌بازار معتبر است."
    override val privacyDialogTitle = "حریم خصوصی و ذخیره‌سازی محلی"
    override val privacyDialogContent = "طراحی این نرم‌افزار به صورت Local-First و کاملاً محلی است. هیچ شعر، قطعه یا فایل صوتی به سرورهای خارجی ارسال نمی‌گردد و روی دستگاه امن شما باقی می‌ماند."
    override val understood = "متوجه شدم"

    override val trashDialogTitle = "تخلیه کامل سطل زباله"
    override val trashDialogConfirm = "آیا از حذف دائمی تمام ترانه‌های موجود در سطل زباله اطمینان دارید؟ این عملیات غیرقابل بازگشت است."
    override val trashEmptied = "سطل زباله به طور کامل تخلیه شد"

    override val backupTitle = "💾 پشتیبان‌گیری و خروجی (.taraneh)"
    override val backupSubtitle = "خروجی، اشتراک فایل پروژه و بازیابی اشعار"
    override val exportTaraneh = "خروجی فایل اختصاصی پروژه (.taraneh)"
    override val exportTxt = "خروجی دیوان متنی (TXT)"
    override val shareProjectJson = "اشتراک مستقیم متن پروژه"
    override val restoreProject = "بازیابی و وارد کردن پروژه (Restore)"
    override val vipScreenTitle = "👑 اشتراک ویژه استودیو ترانه"
    override val vipScreenSubtitle = "فعال‌سازی از طریق پرداخت درون‌برنامه‌ای کافه‌بازار"
    override val buyFromBazaar = "خرید اشتراک از کافه‌بازار"
    override val restorePurchase = "بازیابی خرید قبلی (Restore Purchase)"
}

// 2. 🇺🇸 ENGLISH
object EnglishStrings : StudioStrings {
    override val appName = "Studio Taraneh"
    override val appTitle = "Studio Taraneh"
    override val creatorName = "Seyed Hamid Mousavizadeh"
    override val appVersion = "Version 1.0.0"
    override val appSubtitle = "Professional Songwriting, Lyrics Notebook & Audio Recording"

    override val menuMainTitle = "Studio Workspaces"
    override val menuMySongs = "My Songs"
    override val menuMySongsSub = "Lyrics & Projects Archive"
    override val menuNewSong = "New Song"
    override val menuNewSongSub = "Compose with Pro Structure"
    override val menuVoiceRecording = "Voice Recording"
    override val menuVoiceRecordingSub = "Unlimited Memos & Vocal Tracks"
    override val menuRhythmMaker = "Rhythm & Metronome"
    override val menuRhythmMakerSub = "Beats, BPM & Tap Tempo"
    override val menuFavorites = "Favorites"
    override val menuFavoritesSub = "Starred Songs & Highlights"
    override val menuRecent = "Recent Songs"
    override val menuRecentSub = "Last Edited & Opened"
    override val menuTrash = "Trash"
    override val menuTrashSub = "Restore or Delete Permanently"
    override val menuBackup = "Backup & Export"
    override val menuBackupSub = "Project .taraneh, TXT & Restore"
    override val menuSettings = "Studio Settings"
    override val menuSettingsSub = "Themes, Language, Font & Info"
    override val menuVip = "VIP Subscription"
    override val menuVipSub = "Ad-free & Full Pro Features"
    override val vipActiveBadge = "Gold VIP Active"

    override val searchHint = "Search in titles, lyrics, tags and genres..."
    override val allGenres = "All Genres"
    override val noSongsFound = "No songs found in this category"
    override val noFavoritesFound = "No favorite songs yet"
    override val noTrashFound = "Trash is empty"
    override val noRecordingsFound = "No voice recordings yet"
    override val songTitle = "Song Title"
    override val artistName = "Artist or Songwriter"
    override val genreLabel = "Main Genre"
    override val subgenreLabel = "Subgenre"
    override val bpmLabel = "Tempo (BPM)"
    override val timeSignatureLabel = "Time Signature"
    override val tagsLabel = "Tags"
    override val notesLabel = "Notes & Arrangement"
    override val songCreatedSuccess = "Song created successfully"

    override val sectionIntro = "Intro"
    override val sectionVerse = "Verse"
    override val sectionChorus = "Chorus"
    override val sectionPreChorus = "Pre-Chorus"
    override val sectionBridge = "Bridge"
    override val sectionHook = "Hook"
    override val sectionOutro = "Outro"
    override val sectionRefrain = "Refrain"
    override val sectionCustom = "Custom Section"

    override val actionSave = "Save"
    override val actionCancel = "Cancel"
    override val actionDelete = "Delete"
    override val actionPermanentDelete = "Delete Permanently"
    override val actionRestore = "Restore"
    override val actionEmptyTrash = "Empty Trash"
    override val actionCopyAll = "Copy Full Lyrics"
    override val actionCopySection = "Copy Section"
    override val actionShare = "Share"
    override val actionUndo = "Undo"
    override val actionRedo = "Redo"
    override val actionRecord = "Record Voice"
    override val actionStopRecord = "Stop Recording"
    override val actionPlay = "Play"
    override val actionPause = "Pause"
    override val actionBack = "Back"
    override val actionCreate = "Create & Open Notebook"
    override val actionAdd = "Add"
    override val actionClose = "Close"

    override val voiceStudioTitle = "🎙 Voice & Melody Studio"
    override val voiceStudioSubtitle = "Unlimited Audio Memos & Vocal Ideation"
    override val readyToRecord = "Ready to record melody"
    override val recordingActive = "Recording audio... Tap stop to finish"
    override val savedRecordingsTitle = "Recorded Audio Memos Archive"
    override val saveVoiceDialogTitle = "Save Audio Recording"
    override val micPermissionRequired = "Microphone permission is required"

    override val rhythmMakerTitle = "🎵 Pro Rhythm Maker & Metronome"
    override val rhythmMakerSubtitle = "Tempo, Step Sequencer & Tap Detection"
    override val tapTempoBtn = "Tap Tempo"
    override val hapticOn = "Haptic On"
    override val hapticOff = "Haptic Off"
    override val stepSequencerTitle = "🥁 Step Sequencer (16 Steps)"
    override val soundEffectsTitle = "🔊 Studio Drum Sound Effects"

    override val settingsTitle = "⚙️ Studio Settings"
    override val settingsSubtitle = "Themes, Typography, Language & About"
    override val themeSectionTitle = "Studio Color Themes"
    override val dayNightSectionTitle = "Day / Night Mode"
    override val fontSizeSectionTitle = "Editor Font Size"
    override val languageSectionTitle = "App Language"
    override val livePreviewTitle = "Live Font Preview"
    override val livePreviewSample = "Font size applies immediately across the app:"
    override val livePreviewVerse = "\"This is the last night I write for you... Under the glowing studio lights.\""
    override val adsPrivacyTitle = "Monetization & Privacy"
    override val tapsellAdsTitle = "Tapsell Ads Network"
    override val tapsellSubtitle = "Non-intrusive ads supporting app development"
    override val tapsellVipRemoved = "Ads removed in Gold VIP version"
    override val tapsellAdsBanner = "📢 Tapsell Banner (Unobtrusive writing mode)"
    override val aboutTitle = "About Creator & Studio Taraneh"
    override val termsTitle = "Terms of Use"
    override val privacyTitle = "Local-First Privacy"

    // Font Sizes
    override val fontSizeSmall = "Small"
    override val fontSizeMedium = "Medium"
    override val fontSizeLarge = "Large"
    override val fontSizeExtraLarge = "Extra Large"

    // Day / Night Modes
    override val modeDark = "🌙 Dark (Night)"
    override val modeLight = "☀️ Light (Day)"
    override val modeSystem = "⚙️ System Default"

    // Theme Names
    override val themeNeon = "Studio Neon"
    override val themeMidnight = "Midnight"
    override val themeGraphite = "Graphite"
    override val themePurpleNight = "Purple Night"
    override val themeSilverStudio = "Silver Studio"
    override val themeAmoled = "AMOLED Black"

    // Legal, About & Dialogs
    override val aboutCreatorTile = "About Creator & Studio Taraneh"
    override val termsTile = "Terms and Conditions"
    override val termsSubtitle = "Terms of use and copyright rules for songs and lyrics"
    override val privacyTile = "Privacy & Local-First Storage"
    override val privacySubtitle = "Your data stays solely and securely on your device"
    override val aboutDescription = "Professional songwriting notebook, rhythm maker and audio recording"
    override val aboutDeveloper = "Lead Designer & Developer"
    override val aboutVersion = "Version"
    override val aboutFocus = "Focus: Pop, Rap, Drill, Trap, Hip-Hop"
    override val aboutNoAiNotice = "This app is built with a 100% Local-First architecture without AI or cloud dependencies, crafted for songwriters."
    override val termsP1 = "1. All rights and ownership of written lyrics and melodies belong exclusively to the user."
    override val termsP2 = "2. The app holds no responsibility for lost data without user-created backups."
    override val termsP3 = "3. VIP subscriptions are verified through Cafe Bazaar in-app purchasing."
    override val privacyDialogTitle = "Privacy & Local-First Architecture"
    override val privacyDialogContent = "This app is built with a Local-First architecture. No lyrics, melodies, or voice recordings are ever sent to external servers; everything stays securely on your device."
    override val understood = "Understood"

    override val trashDialogTitle = "Empty Trash"
    override val trashDialogConfirm = "Are you sure you want to permanently delete all items in trash? This cannot be undone."
    override val trashEmptied = "Trash emptied successfully"

    override val backupTitle = "💾 Backup & Export (.taraneh)"
    override val backupSubtitle = "Export project files, TXT lyrics and restore"
    override val exportTaraneh = "Export .taraneh Project"
    override val exportTxt = "Export Lyrics TXT"
    override val shareProjectJson = "Share Project JSON"
    override val restoreProject = "Restore Project Backup"
    override val vipScreenTitle = "👑 VIP Studio Subscription"
    override val vipScreenSubtitle = "Subscribe via Cafe Bazaar In-App Purchase"
    override val buyFromBazaar = "Purchase from Cafe Bazaar"
    override val restorePurchase = "Restore Previous Purchase"
}

// 3. 🇸🇦 ARABIC
object ArabicStrings : StudioStrings {
    override val appName = "استوديو ترانه"
    override val appTitle = "استوديو ترانه"
    override val creatorName = "سيد حميد موسوي زاده"
    override val appVersion = "الإصدار 1.0.0"
    override val appSubtitle = "دفتر كتابة الأغاني وتسجيل الألحان الاحترافي"

    override val menuMainTitle = "أقسام الاستوديو"
    override val menuMySongs = "أغانيي وأشعاري"
    override val menuMySongsSub = "أرشيف النصوص والألحان"
    override val menuNewSong = "أغنية جديدة"
    override val menuNewSongSub = "كتابة بهيكل احترافي"
    override val menuVoiceRecording = "استوديو تسجيل الصوت"
    override val menuVoiceRecordingSub = "تسجيل غير محدود للأفكار والفوكال"
    override val menuRhythmMaker = "صانع الإيقاع والمترونوم"
    override val menuRhythmMakerSub = "التمپو، النبض وصانع الإيقاعات"
    override val menuFavorites = "المفضلة"
    override val menuFavoritesSub = "الأغاني المميزة بنجمة"
    override val menuRecent = "الأغاني الأخيرة"
    override val menuRecentSub = "آخر التعديلات"
    override val menuTrash = "سلة المحذوفات"
    override val menuTrashSub = "استعادة أو حذف نهائي"
    override val menuBackup = "النسخ الاحتياطي"
    override val menuBackupSub = "تصدير .taraneh و TXT والاستعادة"
    override val menuSettings = "إعدادات الاستوديو"
    override val menuSettingsSub = "المظهر، اللغة والخط"
    override val menuVip = "الاشتراك الذهبي VIP"
    override val menuVipSub = "ميزات كاملة وبدون إعلانات"
    override val vipActiveBadge = "الحساب الذهبي نشط"

    override val searchHint = "بحث في العناوين والكلمات والتصنيفات..."
    override val allGenres = "جميع الأنواع"
    override val noSongsFound = "لا توجد أغاني في هذا القسم"
    override val noFavoritesFound = "لا توجد أغانٍ مفضلة بعد"
    override val noTrashFound = "سلة المهملات فارغة"
    override val noRecordingsFound = "لا توجد تسجيلات صوتية بعد"
    override val songTitle = "عنوان الأغنية"
    override val artistName = "اسم الفنان أو الكاتب"
    override val genreLabel = "النوع الرئيسي"
    override val subgenreLabel = "النوع الفرعي"
    override val bpmLabel = "النبض (BPM)"
    override val timeSignatureLabel = "الميزان الموسيقي"
    override val tagsLabel = "الوسوم"
    override val notesLabel = "الملاحظات والترتيب"
    override val songCreatedSuccess = "تم إنشاء الأغنية بنجاح"

    override val sectionIntro = "المقدمة (Intro)"
    override val sectionVerse = "المقطع (Verse)"
    override val sectionChorus = "اللازمة (Chorus)"
    override val sectionPreChorus = "ما قبل اللازمة"
    override val sectionBridge = "الجسر (Bridge)"
    override val sectionHook = "الهوك (Hook)"
    override val sectionOutro = "الخاتمة (Outro)"
    override val sectionRefrain = "الترجيع"
    override val sectionCustom = "مقطع مخصص"

    override val actionSave = "حفظ"
    override val actionCancel = "إلغاء"
    override val actionDelete = "حذف"
    override val actionPermanentDelete = "حذف نهائي"
    override val actionRestore = "استعادة"
    override val actionEmptyTrash = "إفراغ سلة المهملات"
    override val actionCopyAll = "نسخ كامل النص"
    override val actionCopySection = "نسخ هذا المقطع"
    override val actionShare = "مشاركة"
    override val actionUndo = "تراجع"
    override val actionRedo = "إعادة"
    override val actionRecord = "تسجيل صوت"
    override val actionStopRecord = "إيقاف التسجيل"
    override val actionPlay = "تشغيل"
    override val actionPause = "إيقاف مؤقت"
    override val actionBack = "رجوع"
    override val actionCreate = "إنشاء وفتح الدفتر"
    override val actionAdd = "إضافة"
    override val actionClose = "إغلاق"

    override val voiceStudioTitle = "🎙 استوديو تسجيل الصوت والألحان"
    override val voiceStudioSubtitle = "تسجيل غير محدود للأفكار والفوكال"
    override val readyToRecord = "جاهز لتسجيل الفكرة"
    override val recordingActive = "جارٍ التسجيل..."
    override val savedRecordingsTitle = "أرشيف التسجيلات الصوتية"
    override val saveVoiceDialogTitle = "حفظ التسجيل الصوتي"
    override val micPermissionRequired = "إذن الميكروفون مطلوب"

    override val rhythmMakerTitle = "🎵 صانع الإيقاع والمترونوم"
    override val rhythmMakerSubtitle = "التحكم بالتمپو، الدقات وإيقاعات الراب والبوب"
    override val tapTempoBtn = "تحديد النبض باللمس (Tap)"
    override val hapticOn = "الاهتزاز مفعل"
    override val hapticOff = "الاهتزاز معطل"
    override val stepSequencerTitle = "🥁 مصفوفة الإيقاع (Step Sequencer)"
    override val soundEffectsTitle = "🔊 مكتبة المؤثرات الصوتية"

    override val settingsTitle = "⚙️ إعدادات الاستوديو"
    override val settingsSubtitle = "المظاهر، حجم الخط، اللغة ومعلومات التطبيق"
    override val themeSectionTitle = "مظاهر الاستوديو (Theme)"
    override val dayNightSectionTitle = "الوضع الليلي والنهاري"
    override val fontSizeSectionTitle = "حجم خط المحرر"
    override val languageSectionTitle = "لغة التطبيق (Language)"
    override val livePreviewTitle = "معاينة الخط المباشرة"
    override val livePreviewSample = "يتغير حجم الخط في كل الشاشات فوراً:"
    override val livePreviewVerse = "«هذه آخر ليلة أكتب لك فيها... بأنامل مرتجفة وعينين دامعتين»"
    override val adsPrivacyTitle = "الإعلانات والخصوصية"
    override val tapsellAdsTitle = "شبكة إعلانات تپسل"
    override val tapsellSubtitle = "إعلانات غير مزعجة لدعم التطوير"
    override val tapsellVipRemoved = "تمت إزالة الإعلانات في VIP الذهبي"
    override val tapsellAdsBanner = "📢 إعلان تپسل (غير مزعج)"
    override val aboutTitle = "عن المطور واستوديو ترانه"
    override val termsTitle = "الشروط والأحكام"
    override val privacyTitle = "الخصوصية المحلية"

    // Font Sizes
    override val fontSizeSmall = "صغير"
    override val fontSizeMedium = "متوسط"
    override val fontSizeLarge = "كبير"
    override val fontSizeExtraLarge = "كبير جداً"

    // Day / Night Modes
    override val modeDark = "🌙 داكن (ليلي)"
    override val modeLight = "☀️ فاتح (نهاري)"
    override val modeSystem = "⚙️ تلقائي مع النظام"

    // Theme Names
    override val themeNeon = "استوديو نيون"
    override val themeMidnight = "منتصف الليل"
    override val themeGraphite = "غرافيت"
    override val themePurpleNight = "ليلة بنفسجية"
    override val themeSilverStudio = "استوديو فضي"
    override val themeAmoled = "أسود مطلق (AMOLED)"

    // Legal, About & Dialogs
    override val aboutCreatorTile = "عن المطور واستوديو ترانه"
    override val termsTile = "الشروط والأحكام"
    override val termsSubtitle = "شروط الاستخدام وقواعد حقوق الطبع والنشر للأغاني والكلمات"
    override val privacyTile = "الخصوصية والتخزين المحلي"
    override val privacySubtitle = "بياناتك مخزنة فقط وبأمان على جهازك"
    override val aboutDescription = "دفتر احترافي لكتابة الأغاني وصناعة الإيقاع وتسجيل الصوت"
    override val aboutDeveloper = "المصمم والمطور"
    override val aboutVersion = "الإصدار"
    override val aboutFocus = "التركيز: بوب، راب، دريل، تراب، هيب هوب"
    override val aboutNoAiNotice = "تم تصميم هذا التطبيق بهندسة محلية بالكامل دون أي اعتماد على الذكاء الاصطناعي أو السحابة."
    override val termsP1 = "١. جميع حقوق الملكية الفكرية للأشعار والألحان تعود حصراً للمستخدم."
    override val termsP2 = "٢. التطبيق غير مسؤول عن فقدان البيانات في حال عدم أخذ نسخ احتياطية."
    override val termsP3 = "٣. اشتراك VIP سارٍ عبر الشراء داخل كافيه بازار."
    override val privacyDialogTitle = "الخصوصية والتخزين المحلي"
    override val privacyDialogContent = "تم تصميم هذا البرنامج ليعمل محلياً بالكامل (Local-First). لا يتم إرسال أي شعر أو لحن أو ملف صوتي إلى خوادم خارجية؛ تظل على جهازك بأمان."
    override val understood = "مفهوم"

    override val trashDialogTitle = "إفراغ سلة المهملات"
    override val trashDialogConfirm = "هل أنت متأكد من حذف جميع العناصر بشكل دائم؟"
    override val trashEmptied = "تم إفراغ سلة المهملات بنجاح"

    override val backupTitle = "💾 النسخ الاحتياطي (.taraneh)"
    override val backupSubtitle = "تصدير ملفات المشروع واستعادتها"
    override val exportTaraneh = "تصدير ملف المشروع (.taraneh)"
    override val exportTxt = "تصدير نص كامل (TXT)"
    override val shareProjectJson = "مشاركة المشروع مباشرة"
    override val restoreProject = "استعادة المشروع"
    override val vipScreenTitle = "👑 اشتراك الاستوديو الذهبي VIP"
    override val vipScreenSubtitle = "الاشتراك عبر كافيه بازار"
    override val buyFromBazaar = "شراء من كافيه بازار"
    override val restorePurchase = "استعادة المشتريات السابقة"
}

// 4. 🇹🇷 TURKISH
object TurkishStrings : StudioStrings {
    override val appName = "Studio Taraneh"
    override val appTitle = "Studio Taraneh"
    override val creatorName = "Seyed Hamid Mousavizadeh"
    override val appVersion = "Sürüm 1.0.0"
    override val appSubtitle = "Profesyonel Şarkı Sözü Defteri ve Ses Kaydı"

    override val menuMainTitle = "Stüdyo Alanları"
    override val menuMySongs = "Şarkılarım"
    override val menuMySongsSub = "Şarkı Sözleri ve Projeler"
    override val menuNewSong = "Yeni Şarkı"
    override val menuNewSongSub = "Profesyonel Yapıyla Yaz"
    override val menuVoiceRecording = "Ses Kayıt Stüdyosu"
    override val menuVoiceRecordingSub = "Sınırsız Melodi ve Vokal Kaydı"
    override val menuRhythmMaker = "Ritim & Metronom"
    override val menuRhythmMakerSub = "Tempo, Vuruş ve Beat Yapıcı"
    override val menuFavorites = "Favoriler"
    override val menuFavoritesSub = "Yıldızlı ve Seçilmiş Eserler"
    override val menuRecent = "Son Şarkılar"
    override val menuRecentSub = "Son Düzenlenen Eserler"
    override val menuTrash = "Çöp Kutusu"
    override val menuTrashSub = "Geri Yükle veya Kalıcı Sil"
    override val menuBackup = "Yedekleme & Dışa Aktar"
    override val menuBackupSub = ".taraneh ve TXT Proje Dosyaları"
    override val menuSettings = "Stüdyo Ayarları"
    override val menuSettingsSub = "Temalar, Dil, Yazı Boyutu ve Bilgi"
    override val menuVip = "VIP Üyelik"
    override val menuVipSub = "Reklamsız ve Sınırsız Özellikler"
    override val vipActiveBadge = "Altın VIP Aktif"

    override val searchHint = "Başlık, şarkı sözü, tür veya etiketlerde ara..."
    override val allGenres = "Tüm Türler"
    override val noSongsFound = "Bu kategoride şarkı bulunamadı"
    override val noFavoritesFound = "Henüz favori şarkı eklenmedi"
    override val noTrashFound = "Çöp kutusu boş"
    override val noRecordingsFound = "Henüz ses kaydı yapılmadı"
    override val songTitle = "Şarkı Başlığı"
    override val artistName = "Sanatçı veya Yazar"
    override val genreLabel = "Ana Tür"
    override val subgenreLabel = "Alt Tür"
    override val bpmLabel = "Tempo (BPM)"
    override val timeSignatureLabel = "Ölçü (Time Signature)"
    override val tagsLabel = "Etiketler"
    override val notesLabel = "Notlar ve Düzenleme"
    override val songCreatedSuccess = "Şarkı başarıyla oluşturuldu"

    override val sectionIntro = "Giriş (Intro)"
    override val sectionVerse = "Kıta (Verse)"
    override val sectionChorus = "Nakarat (Chorus)"
    override val sectionPreChorus = "Ön Nakarat"
    override val sectionBridge = "Köprü (Bridge)"
    override val sectionHook = "Kanca (Hook)"
    override val sectionOutro = "Bitiş (Outro)"
    override val sectionRefrain = "Nakarat Tekrarı"
    override val sectionCustom = "Özel Bölüm"

    override val actionSave = "Kaydet"
    override val actionCancel = "İptal"
    override val actionDelete = "Sil"
    override val actionPermanentDelete = "Kalıcı Olarak Sil"
    override val actionRestore = "Geri Yükle"
    override val actionEmptyTrash = "Çöpü Boşalt"
    override val actionCopyAll = "Tüm Sözleri Kopyala"
    override val actionCopySection = "Bu Bölümü Kopyala"
    override val actionShare = "Paylaş"
    override val actionUndo = "Geri Al"
    override val actionRedo = "Yinele"
    override val actionRecord = "Ses Kaydet"
    override val actionStopRecord = "Kaydı Durdur"
    override val actionPlay = "Oynat"
    override val actionPause = "Duraklat"
    override val actionBack = "Geri"
    override val actionCreate = "Oluştur ve Defteri Aç"
    override val actionAdd = "Ekle"
    override val actionClose = "Kapat"

    override val voiceStudioTitle = "🎙 Ses & Melodi Stüdyosu"
    override val voiceStudioSubtitle = "Sınırsız Ses Kaydı ve Fikir Notları"
    override val readyToRecord = "Melodi kaydına hazır"
    override val recordingActive = "Ses kaydediliyor... Bitirmek için dokunun"
    override val savedRecordingsTitle = "Kaydedilen Sesler Arşivi"
    override val saveVoiceDialogTitle = "Ses Kaydını Kaydet"
    override val micPermissionRequired = "Mikrofon izni gereklidir"

    override val rhythmMakerTitle = "🎵 Ritim Yapıcı & Metronom"
    override val rhythmMakerSubtitle = "Tempo, Vuruşlar ve Tap Tempo"
    override val tapTempoBtn = "Dokunarak Tempo Bul (Tap Tempo)"
    override val hapticOn = "Titreşim Açık"
    override val hapticOff = "Titreşim Kapalı"
    override val stepSequencerTitle = "🥁 Step Sequencer (16 Adım)"
    override val soundEffectsTitle = "🔊 Stüdyo Davul Sesleri"

    override val settingsTitle = "⚙️ Stüdyo Ayarları"
    override val settingsSubtitle = "Temalar, Yazı Tipi, Dil ve Hakkında"
    override val themeSectionTitle = "Stüdyo Renk Temaları"
    override val dayNightSectionTitle = "Gündüz / Gece Modu"
    override val fontSizeSectionTitle = "Yazı Tipi Boyutu"
    override val languageSectionTitle = "Uygulama Dili (Language)"
    override val livePreviewTitle = "Canlı Yazı Tipi Önizleme"
    override val livePreviewSample = "Yazı boyutu uygulama genelinde anında değişir:"
    override val livePreviewVerse = "«Bu sana son yazdığım gece... Işıklar altında titreyen ellerle.»"
    override val adsPrivacyTitle = "Reklamlar ve Gizlilik"
    override val tapsellAdsTitle = "Tapsell Reklam Ağı"
    override val tapsellSubtitle = "Geliştirmeyi destekleyen reklamlar"
    override val tapsellVipRemoved = "Gold VIP sürümünde reklamlar kaldırıldı"
    override val tapsellAdsBanner = "📢 Tapsell Reklam Alanı"
    override val aboutTitle = "Geliştirici ve Studio Taraneh Hakkında"
    override val termsTitle = "Kullanım Koşulları"
    override val privacyTitle = "Yerel Gizlilik (Local-First)"

    // Font Sizes - Turkish (Fixes the reported bug!)
    override val fontSizeSmall = "Küçük"
    override val fontSizeMedium = "Orta"
    override val fontSizeLarge = "Büyük"
    override val fontSizeExtraLarge = "Çok Büyük"

    // Day / Night Modes - Turkish (Fixes the reported bug!)
    override val modeDark = "🌙 Koyu (Gece)"
    override val modeLight = "☀️ Açık (Gündüz)"
    override val modeSystem = "⚙️ Sistemle Otomatik"

    // Theme Names - Turkish
    override val themeNeon = "Stüdyo Neon"
    override val themeMidnight = "Gece Yarısı"
    override val themeGraphite = "Grafit"
    override val themePurpleNight = "Mor Gece"
    override val themeSilverStudio = "Gümüş Stüdyo"
    override val themeAmoled = "AMOLED Siyah"

    // Legal, About & Dialogs
    override val aboutCreatorTile = "Geliştirici ve Studio Taraneh Hakkında"
    override val termsTile = "Kullanım Koşulları"
    override val termsSubtitle = "Şarkı ve sözler için kullanım koşulları ve telif hakları"
    override val privacyTile = "Gizlilik ve Yerel Depolama"
    override val privacySubtitle = "Verileriniz yalnızca kendi cihazınızda güvenle saklanır"
    override val aboutDescription = "Profesyonel şarkı sözü defteri, ritim yapıcı ve ses kayıt stüdyosu"
    override val aboutDeveloper = "Tasarım ve Geliştirici"
    override val aboutVersion = "Sürüm"
    override val aboutFocus = "Odak: Pop, Rap, Drill, Trap, Hip-Hop"
    override val aboutNoAiNotice = "Bu uygulama yapay zeka veya bulut bağımlılığı olmadan tamamen yerel (Local-First) olarak besteciler için tasarlanmıştır."
    override val termsP1 = "1. Yazılan tüm şarkı sözleri ve melodilerin mülkiyet hakları tamamen kullanıcıya aittir."
    override val termsP2 = "2. Yedekleme yapılmadan yaşanan veri kayıplarından uygulama sorumlu tutulamaz."
    override val termsP3 = "3. VIP abonelikleri Cafe Bazaar uygulama içi satın alma yoluyla geçerlidir."
    override val privacyDialogTitle = "Gizlilik ve Yerel Depolama"
    override val privacyDialogContent = "Bu uygulama tamamen Yerel (Local-First) mimariyle tasarlanmıştır. Hiçbir şarkı sözü, melodi veya ses kaydı harici sunuculara gönderilmez; tamamen cihazınızda güvenle saklanır."
    override val understood = "Anladım"

    override val trashDialogTitle = "Çöpü Boşalt"
    override val trashDialogConfirm = "Tüm öğeleri kalıcı olarak silmek istediğinizden emin misiniz?"
    override val trashEmptied = "Çöp kutusu tamamen boşaltıldı"

    override val backupTitle = "💾 Yedekleme & Dışa Aktar (.taraneh)"
    override val backupSubtitle = "Proje ve TXT dosyalarını dışa aktar ve geri yükle"
    override val exportTaraneh = ".taraneh Projesini Dışa Aktar"
    override val exportTxt = "TXT Olarak Dışa Aktar"
    override val shareProjectJson = "Projeyi Paylaş"
    override val restoreProject = "Projeyi Geri Yükle"
    override val vipScreenTitle = "👑 VIP Stüdyo Üyeliği"
    override val vipScreenSubtitle = "Cafe Bazaar üzerinden güvenli abonelik"
    override val buyFromBazaar = "Cafe Bazaar ile Satın Al"
    override val restorePurchase = "Satın Alımları Geri Yükle"
}

// 5. 🇪🇸 SPANISH
object SpanishStrings : StudioStrings {
    override val appName = "Studio Taraneh"
    override val appTitle = "Studio Taraneh"
    override val creatorName = "Seyed Hamid Mousavizadeh"
    override val appVersion = "Versión 1.0.0"
    override val appSubtitle = "Cuaderno Profesional de Composición y Grabación"

    override val menuMainTitle = "Espacios del Estudio"
    override val menuMySongs = "Mis Canciones"
    override val menuMySongsSub = "Archivo de Letras y Proyectos"
    override val menuNewSong = "Nueva Canción"
    override val menuNewSongSub = "Escribe con Estructura Pro"
    override val menuVoiceRecording = "Grabación de Voz"
    override val menuVoiceRecordingSub = "Memos y Pistas Ilimitadas"
    override val menuRhythmMaker = "Ritmo y Metrónomo"
    override val menuRhythmMakerSub = "BPM, Beats y Secuenciador"
    override val menuFavorites = "Favoritos"
    override val menuFavoritesSub = "Canciones Destacadas"
    override val menuRecent = "Recientes"
    override val menuRecentSub = "Últimas Modificaciones"
    override val menuTrash = "Papelera"
    override val menuTrashSub = "Restaurar o Eliminar"
    override val menuBackup = "Copia de Seguridad"
    override val menuBackupSub = "Proyectos .taraneh y TXT"
    override val menuSettings = "Ajustes del Estudio"
    override val menuSettingsSub = "Temas, Idioma, Letra y Más"
    override val menuVip = "Suscripción VIP"
    override val menuVipSub = "Sin Anuncios y Pro Ilimitado"
    override val vipActiveBadge = "VIP Oro Activo"

    override val searchHint = "Buscar en títulos, letras, géneros y etiquetas..."
    override val allGenres = "Todos los géneros"
    override val noSongsFound = "No se encontraron canciones"
    override val noFavoritesFound = "No hay canciones favoritas aún"
    override val noTrashFound = "La papelera está vacía"
    override val noRecordingsFound = "No hay grabaciones de voz aún"
    override val songTitle = "Título de la Canción"
    override val artistName = "Artista o Compositor"
    override val genreLabel = "Género Principal"
    override val subgenreLabel = "Subgénero"
    override val bpmLabel = "Tempo (BPM)"
    override val timeSignatureLabel = "Compás"
    override val tagsLabel = "Etiquetas"
    override val notesLabel = "Notas y Arreglos"
    override val songCreatedSuccess = "Canción creada con éxito"

    override val sectionIntro = "Intro"
    override val sectionVerse = "Verso"
    override val sectionChorus = "Estribillo"
    override val sectionPreChorus = "Pre-Estribillo"
    override val sectionBridge = "Puente (Bridge)"
    override val sectionHook = "Hook"
    override val sectionOutro = "Outro"
    override val sectionRefrain = "Refrán"
    override val sectionCustom = "Sección Personalizada"

    override val actionSave = "Guardar"
    override val actionCancel = "Cancelar"
    override val actionDelete = "Eliminar"
    override val actionPermanentDelete = "Eliminar Definitivamente"
    override val actionRestore = "Restaurar"
    override val actionEmptyTrash = "Vaciar Papelera"
    override val actionCopyAll = "Copiar Letra Completa"
    override val actionCopySection = "Copiar Sección"
    override val actionShare = "Compartir"
    override val actionUndo = "Deshacer"
    override val actionRedo = "Rehacer"
    override val actionRecord = "Grabar Voz"
    override val actionStopRecord = "Detener Grabación"
    override val actionPlay = "Reproducir"
    override val actionPause = "Pausar"
    override val actionBack = "Volver"
    override val actionCreate = "Crear y Abrir Cuaderno"
    override val actionAdd = "Añadir"
    override val actionClose = "Cerrar"

    override val voiceStudioTitle = "🎙 Estudio de Voz y Melodía"
    override val voiceStudioSubtitle = "Grabaciones Ilimitadas de Memos"
    override val readyToRecord = "Listo para grabar idea"
    override val recordingActive = "Grabando audio... Pulsa detener"
    override val savedRecordingsTitle = "Archivo de Grabaciones Guardadas"
    override val saveVoiceDialogTitle = "Guardar Grabación de Audio"
    override val micPermissionRequired = "Se requiere permiso de micrófono"

    override val rhythmMakerTitle = "🎵 Creador de Ritmos y Metrónomo"
    override val rhythmMakerSubtitle = "Tempo, Secuenciador y Detección Tap"
    override val tapTempoBtn = "Tap Tempo"
    override val hapticOn = "Vibración Activada"
    override val hapticOff = "Vibración Desactivada"
    override val stepSequencerTitle = "🥁 Secuenciador (16 Pasos)"
    override val soundEffectsTitle = "🔊 Efectos de Batería del Estudio"

    override val settingsTitle = "⚙️ Ajustes del Estudio"
    override val settingsSubtitle = "Temas, Tamaño de Letra, Idioma y Acerca de"
    override val themeSectionTitle = "Temas Visuales Neón"
    override val dayNightSectionTitle = "Modo Día / Noche"
    override val fontSizeSectionTitle = "Tamaño de Letra del Editor"
    override val languageSectionTitle = "Idioma (Language)"
    override val livePreviewTitle = "Vista Previa de Letra en Vivo"
    override val livePreviewSample = "El tamaño de letra cambia al instante en toda la app:"
    override val livePreviewVerse = "«Esta es la última noche que escribo para ti... Bajo las luces de neón.»"
    override val adsPrivacyTitle = "Publicidad y Privacidad"
    override val tapsellAdsTitle = "Red de Anuncios Tapsell"
    override val tapsellSubtitle = "Anuncios no intrusivos que apoyan el desarrollo"
    override val tapsellVipRemoved = "Anuncios eliminados en la versión VIP"
    override val tapsellAdsBanner = "📢 Banner publicitario (Modo escritura limpio)"
    override val aboutTitle = "Acerca del Creador y Studio Taraneh"
    override val termsTitle = "Términos y Condiciones"
    override val privacyTitle = "Privacidad Local (Local-First)"

    // Font Sizes - Spanish
    override val fontSizeSmall = "Pequeño"
    override val fontSizeMedium = "Mediano"
    override val fontSizeLarge = "Grande"
    override val fontSizeExtraLarge = "Muy Grande"

    // Day / Night Modes - Spanish
    override val modeDark = "🌙 Oscuro (Noche)"
    override val modeLight = "☀️ Claro (Día)"
    override val modeSystem = "⚙️ Automático del Sistema"

    // Theme Names - Spanish
    override val themeNeon = "Estudio Neón"
    override val themeMidnight = "Medianoche"
    override val themeGraphite = "Grafito"
    override val themePurpleNight = "Noche Púrpura"
    override val themeSilverStudio = "Estudio Plateado"
    override val themeAmoled = "Negro AMOLED"

    // Legal, About & Dialogs
    override val aboutCreatorTile = "Acerca del Creador y Studio Taraneh"
    override val termsTile = "Términos y Condiciones"
    override val termsSubtitle = "Términos de uso y derechos de autor para canciones y letras"
    override val privacyTile = "Privacidad y Almacenamiento Local"
    override val privacySubtitle = "Tus datos se guardan únicamente en tu dispositivo"
    override val aboutDescription = "Cuaderno profesional de composición, creación de ritmo y grabación de voz"
    override val aboutDeveloper = "Diseñador y Desarrollador"
    override val aboutVersion = "Versión"
    override val aboutFocus = "Enfoque: Pop, Rap, Drill, Trap, Hip-Hop"
    override val aboutNoAiNotice = "Esta aplicación funciona 100% en local sin IA ni nube, diseñada para compositores independientes."
    override val termsP1 = "1. Todos los derechos morales y patrimoniales de las letras y melodías pertenecen al usuario."
    override val termsP2 = "2. La aplicación no se responsabiliza por pérdidas sin copia de seguridad previa."
    override val termsP3 = "3. La suscripción VIP se valida mediante compras en Cafe Bazaar."
    override val privacyDialogTitle = "Privacidad y Almacenamiento Local"
    override val privacyDialogContent = "Esta aplicación está diseñada con arquitectura Local-First. Ninguna letra, canción o archivo de audio se envía a servidores externos; todo permanece seguro en tu dispositivo."
    override val understood = "Entendido"

    override val trashDialogTitle = "Vaciar Papelera"
    override val trashDialogConfirm = "¿Estás seguro de que deseas eliminar permanentemente todos los elementos?"
    override val trashEmptied = "Papelera vaciada correctamente"

    override val backupTitle = "💾 Copia de Seguridad (.taraneh)"
    override val backupSubtitle = "Exporta proyectos, letras TXT y restaura"
    override val exportTaraneh = "Exportar Proyecto .taraneh"
    override val exportTxt = "Exportar Letras TXT"
    override val shareProjectJson = "Compartir Proyecto JSON"
    override val restoreProject = "Restaurar Copia de Seguridad"
    override val vipScreenTitle = "👑 Suscripción VIP Studio"
    override val vipScreenSubtitle = "Compra mediante Cafe Bazaar"
    override val buyFromBazaar = "Comprar en Cafe Bazaar"
    override val restorePurchase = "Restaurar Compras Anteriores"
}

// 6. 🇫🇷 FRENCH
object FrenchStrings : StudioStrings {
    override val appName = "Studio Taraneh"
    override val appTitle = "Studio Taraneh"
    override val creatorName = "Seyed Hamid Mousavizadeh"
    override val appVersion = "Version 1.0.0"
    override val appSubtitle = "Cahier Professionnel d'Écriture et d'Enregistrement"

    override val menuMainTitle = "Espaces du Studio"
    override val menuMySongs = "Mes Chansons"
    override val menuMySongsSub = "Archives des Paroles et Projets"
    override val menuNewSong = "Nouvelle Chanson"
    override val menuNewSongSub = "Structure Professionnelle"
    override val menuVoiceRecording = "Enregistrement Vocal"
    override val menuVoiceRecordingSub = "Mémos et Pistes Illimités"
    override val menuRhythmMaker = "Rythme & Métronome"
    override val menuRhythmMakerSub = "Tempo, Beats et Séquenceur"
    override val menuFavorites = "Favoris"
    override val menuFavoritesSub = "Chansons Étoilées"
    override val menuRecent = "Chansons Récentes"
    override val menuRecentSub = "Dernières Modifications"
    override val menuTrash = "Corbeille"
    override val menuTrashSub = "Restaurer ou Supprimer"
    override val menuBackup = "Sauvegarde & Export"
    override val menuBackupSub = "Fichiers .taraneh et TXT"
    override val menuSettings = "Paramètres du Studio"
    override val menuSettingsSub = "Thèmes, Langue, Police et Info"
    override val menuVip = "Abonnement VIP"
    override val menuVipSub = "Sans Publicité et Illimité"
    override val vipActiveBadge = "VIP Or Actif"

    override val searchHint = "Rechercher titres, paroles, genres..."
    override val allGenres = "Tous les genres"
    override val noSongsFound = "Aucune chanson trouvée"
    override val noFavoritesFound = "Aucun favori pour le moment"
    override val noTrashFound = "La corbeille est vide"
    override val noRecordingsFound = "Aucun enregistrement pour le moment"
    override val songTitle = "Titre de la Chanson"
    override val artistName = "Artiste ou Auteur"
    override val genreLabel = "Genre Principal"
    override val subgenreLabel = "Sous-genre"
    override val bpmLabel = "Tempo (BPM)"
    override val timeSignatureLabel = "Signature Rythmique"
    override val tagsLabel = "Tags"
    override val notesLabel = "Notes et Arrangements"
    override val songCreatedSuccess = "Chanson créée avec succès"

    override val sectionIntro = "Intro"
    override val sectionVerse = "Couplet (Verse)"
    override val sectionChorus = "Refrain (Chorus)"
    override val sectionPreChorus = "Pré-Refrain"
    override val sectionBridge = "Pont (Bridge)"
    override val sectionHook = "Hook"
    override val sectionOutro = "Outro"
    override val sectionRefrain = "Refrain"
    override val sectionCustom = "Section Personnalisée"

    override val actionSave = "Enregistrer"
    override val actionCancel = "Annuler"
    override val actionDelete = "Supprimer"
    override val actionPermanentDelete = "Supprimer Définitivement"
    override val actionRestore = "Restaurer"
    override val actionEmptyTrash = "Vider la Corbeille"
    override val actionCopyAll = "Copier Toutes les Paroles"
    override val actionCopySection = "Copier la Section"
    override val actionShare = "Partager"
    override val actionUndo = "Annuler"
    override val actionRedo = "Rétablir"
    override val actionRecord = "Enregistrer"
    override val actionStopRecord = "Arrêter l'Enregistrement"
    override val actionPlay = "Lire"
    override val actionPause = "Pause"
    override val actionBack = "Retour"
    override val actionCreate = "Créer et Ouvrir le Cahier"
    override val actionAdd = "Ajouter"
    override val actionClose = "Fermer"

    override val voiceStudioTitle = "🎙 Studio Voix & Mélodie"
    override val voiceStudioSubtitle = "Enregistrements Audio Illimités"
    override val readyToRecord = "Prêt à enregistrer"
    override val recordingActive = "Enregistrement en cours..."
    override val savedRecordingsTitle = "Archives des Mémos Enregistrés"
    override val saveVoiceDialogTitle = "Enregistrer l'Audio"
    override val micPermissionRequired = "Autorisation de microphone requise"

    override val rhythmMakerTitle = "🎵 Générateur de Rythme & Métronome"
    override val rhythmMakerSubtitle = "Tempo, Séquenceur et Tap Tempo"
    override val tapTempoBtn = "Tap Tempo"
    override val hapticOn = "Vibration Activée"
    override val hapticOff = "Vibration Désactivée"
    override val stepSequencerTitle = "🥁 Séquenceur (16 Pas)"
    override val soundEffectsTitle = "🔊 Effets Sonores Batterie"

    override val settingsTitle = "⚙️ Paramètres du Studio"
    override val settingsSubtitle = "Thèmes, Taille de Police, Langue et Info"
    override val themeSectionTitle = "Thèmes Néon du Studio"
    override val dayNightSectionTitle = "Mode Jour / Nuit"
    override val fontSizeSectionTitle = "Taille de Police"
    override val languageSectionTitle = "Langue (Language)"
    override val livePreviewTitle = "Aperçu en Direct de la Police"
    override val livePreviewSample = "La taille change immédiatement dans toute l'application :"
    override val livePreviewVerse = "« C'est la dernière nuit où je t'écris... Sous les lumières du studio. »"
    override val adsPrivacyTitle = "Publicités et Confidentialité"
    override val tapsellAdsTitle = "Réseau Publicitaire Tapsell"
    override val tapsellSubtitle = "Publicités discrètes soutenant le développement"
    override val tapsellVipRemoved = "Publicités retirées en version VIP"
    override val tapsellAdsBanner = "📢 Bannière publicitaire discrète"
    override val aboutTitle = "À propos du Créateur et de Studio Taraneh"
    override val termsTitle = "Conditions d'Utilisation"
    override val privacyTitle = "Confidentialité Locale"

    // Font Sizes - French
    override val fontSizeSmall = "Petit"
    override val fontSizeMedium = "Moyen"
    override val fontSizeLarge = "Grand"
    override val fontSizeExtraLarge = "Très Grand"

    // Day / Night Modes - French
    override val modeDark = "🌙 Sombre (Nuit)"
    override val modeLight = "☀️ Clair (Jour)"
    override val modeSystem = "⚙️ Automatique Système"

    // Theme Names - French
    override val themeNeon = "Studio Néon"
    override val themeMidnight = "Minuit"
    override val themeGraphite = "Graphite"
    override val themePurpleNight = "Nuit Pourpre"
    override val themeSilverStudio = "Studio Argenté"
    override val themeAmoled = "Noir AMOLED"

    // Legal, About & Dialogs
    override val aboutCreatorTile = "À propos du Créateur et du Studio"
    override val termsTile = "Conditions d'Utilisation"
    override val termsSubtitle = "Conditions d'utilisation et droits d'auteur des chansons"
    override val privacyTile = "Confidentialité et Stockage Local"
    override val privacySubtitle = "Vos données restent uniquement et en toute sécurité sur votre appareil"
    override val aboutDescription = "Cahier professionnel d'écriture, boîte à rythmes et enregistreur vocal"
    override val aboutDeveloper = "Concepteur & Développeur"
    override val aboutVersion = "Version"
    override val aboutFocus = "Genres : Pop, Rap, Drill, Trap, Hip-Hop"
    override val aboutNoAiNotice = "Cette application est 100% locale sans IA ni cloud, conçue pour les auteurs-compositeurs indépendants."
    override val termsP1 = "1. Tous les droits sur les textes et mélodies appartiennent exclusivement à l'utilisateur."
    override val termsP2 = "2. L'application décline toute responsabilité en cas de perte sans sauvegarde préalable."
    override val termsP3 = "3. L'abonnement VIP est validé via Cafe Bazaar In-App."
    override val privacyDialogTitle = "Confidentialité et Stockage Local"
    override val privacyDialogContent = "Cette application est conçue selon une architecture locale (Local-First). Aucun texte, mélodie ou enregistrement vocal n'est transmis à des serveurs distants."
    override val understood = "Compris"

    override val trashDialogTitle = "Vider la Corbeille"
    override val trashDialogConfirm = "Êtes-vous sûr de vouloir supprimer définitivement tous les éléments ?"
    override val trashEmptied = "Corbeille vidée avec succès"

    override val backupTitle = "💾 Sauvegarde & Export (.taraneh)"
    override val backupSubtitle = "Exporter projets, textes TXT et restaurer"
    override val exportTaraneh = "Exporter Projet .taraneh"
    override val exportTxt = "Exporter Paroles TXT"
    override val shareProjectJson = "Partager Projet JSON"
    override val restoreProject = "Restaurer une Sauvegarde"
    override val vipScreenTitle = "👑 Abonnement VIP Studio"
    override val vipScreenSubtitle = "Achat sécurisé via Cafe Bazaar"
    override val buyFromBazaar = "Acheter sur Cafe Bazaar"
    override val restorePurchase = "Restaurer les Achats"
}

// 7. 🇩🇪 GERMAN
object GermanStrings : StudioStrings {
    override val appName = "Studio Taraneh"
    override val appTitle = "Studio Taraneh"
    override val creatorName = "Seyed Hamid Mousavizadeh"
    override val appVersion = "Version 1.0.0"
    override val appSubtitle = "Professionelles Songwriting-, Notiz- und Audio-Studio"

    override val menuMainTitle = "Studio-Bereiche"
    override val menuMySongs = "Meine Songs"
    override val menuMySongsSub = "Liedtexte und Projektarchiv"
    override val menuNewSong = "Neuer Song"
    override val menuNewSongSub = "Mit Profi-Struktur schreiben"
    override val menuVoiceRecording = "Sprachaufnahme"
    override val menuVoiceRecordingSub = "Unbegrenzte Melodien und Gesangsspuren"
    override val menuRhythmMaker = "Rhythmus & Metronom"
    override val menuRhythmMakerSub = "Tempo, Beats und Step-Sequenzer"
    override val menuFavorites = "Favoriten"
    override val menuFavoritesSub = "Mit Stern markierte Songs"
    override val menuRecent = "Zuletzt bearbeitet"
    override val menuRecentSub = "Aktuelle Projekte"
    override val menuTrash = "Papierkorb"
    override val menuTrashSub = "Wiederherstellen oder Löschen"
    override val menuBackup = "Sicherung & Export"
    override val menuBackupSub = ".taraneh Projektdateien & TXT"
    override val menuSettings = "Studio-Einstellungen"
    override val menuSettingsSub = "Designs, Sprache, Schriftgröße"
    override val menuVip = "VIP-Abonnement"
    override val menuVipSub = "Werbefrei & Vollversion"
    override val vipActiveBadge = "Gold VIP Aktiv"

    override val searchHint = "Suche in Titeln, Texten, Genres und Tags..."
    override val allGenres = "Alle Genres"
    override val noSongsFound = "Keine Songs gefunden"
    override val noFavoritesFound = "Noch keine Favoriten"
    override val noTrashFound = "Papierkorb ist leer"
    override val noRecordingsFound = "Noch keine Sprachaufnahmen"
    override val songTitle = "Songtitel"
    override val artistName = "Künstler oder Songwriter"
    override val genreLabel = "Hauptgenre"
    override val subgenreLabel = "Subgenre"
    override val bpmLabel = "Tempo (BPM)"
    override val timeSignatureLabel = "Taktart"
    override val tagsLabel = "Tags"
    override val notesLabel = "Notizen und Arrangement"
    override val songCreatedSuccess = "Song erfolgreich erstellt"

    override val sectionIntro = "Intro"
    override val sectionVerse = "Strophe (Verse)"
    override val sectionChorus = "Refrain (Chorus)"
    override val sectionPreChorus = "Pre-Chorus"
    override val sectionBridge = "Bridge"
    override val sectionHook = "Hook"
    override val sectionOutro = "Outro"
    override val sectionRefrain = "Kehrreim"
    override val sectionCustom = "Eigener Abschnitt"

    override val actionSave = "Speichern"
    override val actionCancel = "Abbrechen"
    override val actionDelete = "Löschen"
    override val actionPermanentDelete = "Dauerhaft löschen"
    override val actionRestore = "Wiederherstellen"
    override val actionEmptyTrash = "Papierkorb leeren"
    override val actionCopyAll = "Gesamten Text kopieren"
    override val actionCopySection = "Abschnitt kopieren"
    override val actionShare = "Teilen"
    override val actionUndo = "Rückgängig"
    override val actionRedo = "Wiederholen"
    override val actionRecord = "Aufnehmen"
    override val actionStopRecord = "Aufnahme stoppen"
    override val actionPlay = "Abspielen"
    override val actionPause = "Pause"
    override val actionBack = "Zurück"
    override val actionCreate = "Erstellen und Notizbuch öffnen"
    override val actionAdd = "Hinzufügen"
    override val actionClose = "Schließen"

    override val voiceStudioTitle = "🎙 Gesangs- & Melodiestudio"
    override val voiceStudioSubtitle = "Unbegrenzte Audioaufnahmen für Songideen"
    override val readyToRecord = "Bereit zur Aufnahme"
    override val recordingActive = "Aufnahme läuft... Zum Beenden tippen"
    override val savedRecordingsTitle = "Gespeicherte Audio-Memos"
    override val saveVoiceDialogTitle = "Audioaufnahme speichern"
    override val micPermissionRequired = "Mikrofonberechtigung erforderlich"

    override val rhythmMakerTitle = "🎵 Rhythmus-Macher & Metronom"
    override val rhythmMakerSubtitle = "Tempo, Sequenzer & Tap Tempo"
    override val tapTempoBtn = "Tap Tempo"
    override val hapticOn = "Vibration Ein"
    override val hapticOff = "Vibration Aus"
    override val stepSequencerTitle = "🥁 Step-Sequenzer (16 Schritte)"
    override val soundEffectsTitle = "🔊 Studio-Schlagzeug-Sounds"

    override val settingsTitle = "⚙️ Studio-Einstellungen"
    override val settingsSubtitle = "Designs, Schriftgröße, Sprache und Info"
    override val themeSectionTitle = "Neon Studio-Designs"
    override val dayNightSectionTitle = "Tag / Nacht Modus"
    override val fontSizeSectionTitle = "Schriftgröße des Editors"
    override val languageSectionTitle = "Sprache (Language)"
    override val livePreviewTitle = "Live-Schriftvorschau"
    override val livePreviewSample = "Die Schriftgröße ändert sich sofort in der gesamten App:"
    override val livePreviewVerse = "«Dies ist die letzte Nacht, in der ich für dich schreibe... Unter den Studiolichtern.»"
    override val adsPrivacyTitle = "Werbung und Datenschutz"
    override val tapsellAdsTitle = "Tapsell Werbenetzwerk"
    override val tapsellSubtitle = "Dezente Werbung zur Unterstützung der App"
    override val tapsellVipRemoved = "Werbung in der Gold-VIP-Version entfernt"
    override val tapsellAdsBanner = "📢 Tapsell Werbebanner (Unaufdringlich)"
    override val aboutTitle = "Über den Entwickler & Studio Taraneh"
    override val termsTitle = "Nutzungsbedingungen"
    override val privacyTitle = "Lokaler Datenschutz (Local-First)"

    // Font Sizes - German
    override val fontSizeSmall = "Klein"
    override val fontSizeMedium = "Mittel"
    override val fontSizeLarge = "Groß"
    override val fontSizeExtraLarge = "Sehr Groß"

    // Day / Night Modes - German
    override val modeDark = "🌙 Dunkel (Nacht)"
    override val modeLight = "☀️ Hell (Tag)"
    override val modeSystem = "⚙️ Systemstandard"

    // Theme Names - German
    override val themeNeon = "Studio Neon"
    override val themeMidnight = "Mitternacht"
    override val themeGraphite = "Graphit"
    override val themePurpleNight = "Violette Nacht"
    override val themeSilverStudio = "Silber-Studio"
    override val themeAmoled = "AMOLED-Schwarz"

    // Legal, About & Dialogs
    override val aboutCreatorTile = "Über den Entwickler & Studio Taraneh"
    override val termsTile = "Nutzungsbedingungen"
    override val termsSubtitle = "Nutzungsbedingungen und Urheberrechtsregeln für Liedtexte"
    override val privacyTile = "Datenschutz & Lokaler Speicher"
    override val privacySubtitle = "Ihre Daten verbleiben sicher und ausschließlich auf Ihrem Gerät"
    override val aboutDescription = "Professionelles Songwriting-Notizbuch, Beat-Maker und Tonstudio"
    override val aboutDeveloper = "Designer & Entwickler"
    override val aboutVersion = "Version"
    override val aboutFocus = "Fokus: Pop, Rap, Drill, Trap, Hip-Hop"
    override val aboutNoAiNotice = "Diese App arbeitet vollständig lokal ohne KI- oder Cloud-Abhängigkeiten, optimiert für Songwriter."
    override val termsP1 = "1. Alle geistigen Eigentumsrechte an geschriebenen Texten und Melodien liegen beim Nutzer."
    override val termsP2 = "2. Die App übernimmt keine Haftung für Datenverlust ohne eigens erstellte Backups."
    override val termsP3 = "3. Das VIP-Abo wird über Cafe Bazaar In-App-Kauf abgewickelt."
    override val privacyDialogTitle = "Datenschutz & Lokaler Speicher"
    override val privacyDialogContent = "Diese App wurde mit einer Local-First-Architektur entwickelt. Weder Texte noch Melodien oder Sprachaufnahmen werden an externe Server übertragen."
    override val understood = "Verstanden"

    override val trashDialogTitle = "Papierkorb leeren"
    override val trashDialogConfirm = "Möchten Sie alle Elemente endgültig löschen?"
    override val trashEmptied = "Papierkorb erfolgreich geleert"

    override val backupTitle = "💾 Sicherung & Export (.taraneh)"
    override val backupSubtitle = "Projektdateien & TXT exportieren und wiederherstellen"
    override val exportTaraneh = ".taraneh Projekt exportieren"
    override val exportTxt = "Text als TXT exportieren"
    override val shareProjectJson = "Projekt-JSON teilen"
    override val restoreProject = "Projekt wiederherstellen"
    override val vipScreenTitle = "👑 VIP Studio-Abonnement"
    override val vipScreenSubtitle = "Kauf über Cafe Bazaar"
    override val buyFromBazaar = "Über Cafe Bazaar kaufen"
    override val restorePurchase = "Einkäufe wiederherstellen"
}

// 8. 🇷🇺 RUSSIAN
object RussianStrings : StudioStrings {
    override val appName = "Studio Taraneh"
    override val appTitle = "Studio Taraneh"
    override val creatorName = "Сейед Хамид Мусавизаде"
    override val appVersion = "Версия 1.0.0"
    override val appSubtitle = "Профессиональный блокнот поэта-песенника и студия звукозаписи"

    override val menuMainTitle = "Разделы студии"
    override val menuMySongs = "Мои песни"
    override val menuMySongsSub = "Архив текстов и проектов"
    override val menuNewSong = "Новая песня"
    override val menuNewSongSub = "Создание с профессиональной структурой"
    override val menuVoiceRecording = "Студия звукозаписи"
    override val menuVoiceRecordingSub = "Неограниченная запись идей и вокала"
    override val menuRhythmMaker = "Ритм и метроном"
    override val menuRhythmMakerSub = "Темп, бит-мейкер и секвенсор"
    override val menuFavorites = "Избранное"
    override val menuFavoritesSub = "Отмеченные треки"
    override val menuRecent = "Недавние песни"
    override val menuRecentSub = "Последние редактирования"
    override val menuTrash = "Корзина"
    override val menuTrashSub = "Восстановление или окончательное удаление"
    override val menuBackup = "Резервное копирование"
    override val menuBackupSub = "Экспорт в .taraneh, TXT и восстановление"
    override val menuSettings = "Настройки студии"
    override val menuSettingsSub = "Темы, язык, размер шрифта и о программе"
    override val menuVip = "VIP подписка"
    override val menuVipSub = "Без рекламы и все функции"
    override val vipActiveBadge = "Золотой VIP активен"

    override val searchHint = "Поиск по названиям, текстам, жанрам и тегам..."
    override val allGenres = "Все жанры"
    override val noSongsFound = "Песни не найдены"
    override val noFavoritesFound = "В избранном пока ничего нет"
    override val noTrashFound = "Корзина пуста"
    override val noRecordingsFound = "Записей пока нет"
    override val songTitle = "Название песни"
    override val artistName = "Исполнитель или автор"
    override val genreLabel = "Основной жанр"
    override val subgenreLabel = "Поджанр"
    override val bpmLabel = "Темп (BPM)"
    override val timeSignatureLabel = "Размер такта"
    override val tagsLabel = "Теги"
    override val notesLabel = "Заметки и аранжировка"
    override val songCreatedSuccess = "Песня успешно создана"

    override val sectionIntro = "Интро (Intro)"
    override val sectionVerse = "Куплет (Verse)"
    override val sectionChorus = "Припев (Chorus)"
    override val sectionPreChorus = "Предприпев"
    override val sectionBridge = "Бридж (Bridge)"
    override val sectionHook = "Хук (Hook)"
    override val sectionOutro = "Аутро (Outro)"
    override val sectionRefrain = "Рефрен"
    override val sectionCustom = "Пользовательский раздел"

    override val actionSave = "Сохранить"
    override val actionCancel = "Отмена"
    override val actionDelete = "Удалить"
    override val actionPermanentDelete = "Удалить навсегда"
    override val actionRestore = "Восстановить"
    override val actionEmptyTrash = "Очистить корзину"
    override val actionCopyAll = "Копировать весь текст"
    override val actionCopySection = "Копировать раздел"
    override val actionShare = "Поделиться"
    override val actionUndo = "Отменить"
    override val actionRedo = "Повторить"
    override val actionRecord = "Запись голоса"
    override val actionStopRecord = "Остановить запись"
    override val actionPlay = "Воспроизвести"
    override val actionPause = "Пауза"
    override val actionBack = "Назад"
    override val actionCreate = "Создать и открыть"
    override val actionAdd = "Добавить"
    override val actionClose = "Закрыть"

    override val voiceStudioTitle = "🎙 Студия записи голоса и мелодий"
    override val voiceStudioSubtitle = "Неограниченная запись демо-треков"
    override val readyToRecord = "Готово к записи"
    override val recordingActive = "Идет запись аудио..."
    override val savedRecordingsTitle = "Архив сохраненных записей"
    override val saveVoiceDialogTitle = "Сохранить аудиозапись"
    override val micPermissionRequired = "Требуется разрешение на запись с микрофона"

    override val rhythmMakerTitle = "🎵 Ритм-машина и метроном"
    override val rhythmMakerSubtitle = "Темп, пошаговый секвенсор и Tap Tempo"
    override val tapTempoBtn = "Определить темп (Tap Tempo)"
    override val hapticOn = "Вибрация включена"
    override val hapticOff = "Вибрация выключена"
    override val stepSequencerTitle = "🥁 16-шаговый секвенсор"
    override val soundEffectsTitle = "🔊 Звуки ударных инструментов"

    override val settingsTitle = "⚙️ Настройки студии"
    override val settingsSubtitle = "Темы, размер шрифта, язык и сведения"
    override val themeSectionTitle = "Неоновые темы оформления"
    override val dayNightSectionTitle = "Дневной / Ночной режим"
    override val fontSizeSectionTitle = "Размер шрифта редактора"
    override val languageSectionTitle = "Язык приложения (Language)"
    override val livePreviewTitle = "Живой предпросмотр шрифта"
    override val livePreviewSample = "Размер шрифта меняется мгновенно во всем приложении:"
    override val livePreviewVerse = "«Это последняя ночь, когда я пишу для тебя... Под светом студийных огней.»"
    override val adsPrivacyTitle = "Реклама и конфиденциальность"
    override val tapsellAdsTitle = "Рекламная сеть Tapsell"
    override val tapsellSubtitle = "Ненавязчивая реклама для поддержки проекта"
    override val tapsellVipRemoved = "Реклама отключена в версии Gold VIP"
    override val tapsellAdsBanner = "📢 Баннер Tapsell (не мешает творчеству)"
    override val aboutTitle = "Об авторе и Studio Taraneh"
    override val termsTitle = "Условия использования"
    override val privacyTitle = "Локальная конфиденциальность (Local-First)"

    // Font Sizes - Russian
    override val fontSizeSmall = "Маленький"
    override val fontSizeMedium = "Средний"
    override val fontSizeLarge = "Большой"
    override val fontSizeExtraLarge = "Очень большой"

    // Day / Night Modes - Russian
    override val modeDark = "🌙 Темный (Ночь)"
    override val modeLight = "☀️ Светлый (День)"
    override val modeSystem = "⚙️ Системный режим"

    // Theme Names - Russian
    override val themeNeon = "Студия Неон"
    override val themeMidnight = "Полночь"
    override val themeGraphite = "Графит"
    override val themePurpleNight = "Фиолетовая ночь"
    override val themeSilverStudio = "Серебряная студия"
    override val themeAmoled = "Черный AMOLED"

    // Legal, About & Dialogs
    override val aboutCreatorTile = "Об авторе и Studio Taraneh"
    override val termsTile = "Условия использования"
    override val termsSubtitle = "Условия использования и авторские права на стихи и песни"
    override val privacyTile = "Конфиденциальность и локальное хранилище"
    override val privacySubtitle = "Ваши данные хранятся исключительно на вашем устройстве"
    override val aboutDescription = "Профессиональный блокнот поэта-песенника, бит-мейкер и запись вокала"
    override val aboutDeveloper = "Автор и разработчик"
    override val aboutVersion = "Версия"
    override val aboutFocus = "Жанры: Поп, Рэп, Дрилл, Трэп, Хип-хоп"
    override val aboutNoAiNotice = "Это приложение работает на 100% локально, без ИИ и облачных серверов, для независимых авторов."
    override val termsP1 = "1. Все авторские права на написанные тексты и мелодии принадлежат исключительно пользователю."
    override val termsP2 = "2. Приложение не несет ответственности за потерю данных без резервного копирования."
    override val termsP3 = "3. VIP-подписка активируется через покупки Cafe Bazaar."
    override val privacyDialogTitle = "Конфиденциальность и локальное хранилище"
    override val privacyDialogContent = "Это приложение работает по принципу Local-First. Никакие тексты, мелодии или аудиозаписи не передаются на внешние серверы; все остается на вашем устройстве."
    override val understood = "Понятно"

    override val trashDialogTitle = "Очистить корзину"
    override val trashDialogConfirm = "Вы уверены, что хотите навсегда удалить все элементы?"
    override val trashEmptied = "Корзина успешно очищена"

    override val backupTitle = "💾 Резервное копирование (.taraneh)"
    override val backupSubtitle = "Экспорт проектов, текстов TXT и восстановление"
    override val exportTaraneh = "Экспорт проекта .taraneh"
    override val exportTxt = "Экспорт текста TXT"
    override val shareProjectJson = "Поделиться JSON проекта"
    override val restoreProject = "Восстановить проект"
    override val vipScreenTitle = "👑 VIP подписка студии"
    override val vipScreenSubtitle = "Оплата через Cafe Bazaar"
    override val buyFromBazaar = "Купить в Cafe Bazaar"
    override val restorePurchase = "Восстановить покупки"
}

fun getStringsForLanguage(language: AppLanguage): StudioStrings {
    return when (language) {
        AppLanguage.PERSIAN -> PersianStrings
        AppLanguage.ENGLISH -> EnglishStrings
        AppLanguage.ARABIC -> ArabicStrings
        AppLanguage.TURKISH -> TurkishStrings
        AppLanguage.SPANISH -> SpanishStrings
        AppLanguage.FRENCH -> FrenchStrings
        AppLanguage.GERMAN -> GermanStrings
        AppLanguage.RUSSIAN -> RussianStrings
    }
}

fun getStudioStrings(language: AppLanguage): StudioStrings = getStringsForLanguage(language)

val LocalStudioStrings = staticCompositionLocalOf<StudioStrings> { PersianStrings }

val AppStrings: StudioStrings
    @Composable
    @ReadOnlyComposable
    get() = LocalStudioStrings.current
