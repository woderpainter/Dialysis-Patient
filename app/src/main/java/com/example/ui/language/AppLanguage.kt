package com.example.ui.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

enum class LanguageCode(val code: String, val displayName: String, val isRtl: Boolean) {
    FRENCH("fr", "Français", false),
    ARABIC("ar", "العربية", true),
    ENGLISH("en", "English", false)
}

typealias AppLanguage = LanguageCode

object LanguageManager {
    var currentLanguage by mutableStateOf(LanguageCode.FRENCH)

    fun setLanguage(lang: LanguageCode) {
        currentLanguage = lang
    }

    fun getString(key: String): String {
        val map = when (currentLanguage) {
            LanguageCode.FRENCH -> frStrings
            LanguageCode.ARABIC -> arStrings
            LanguageCode.ENGLISH -> enStrings
        }
        return map[key] ?: enStrings[key] ?: key
    }
}

val LocalAppLanguage = staticCompositionLocalOf { LanguageCode.FRENCH }

@Composable
fun AppLanguageProvider(
    language: LanguageCode = LanguageManager.currentLanguage,
    content: @Composable () -> Unit
) {
    val layoutDirection = if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    CompositionLocalProvider(
        LocalAppLanguage provides language,
        LocalLayoutDirection provides layoutDirection,
        content = content
    )
}

private val frStrings = mapOf(
    "app_title" to "Carnet de Dialyse",
    "app_subtitle" to "Numérique",
    "home" to "Accueil",
    "passport" to "Passeport",
    "sessions" to "Séances",
    "monitoring" to "Mesures Suivi",
    "labs" to "Laboratoire",
    "meds" to "Traitements",
    "nutrition" to "Nutrition & Eau",
    "docs" to "Documents",
    "emergency" to "CARTE URGENCE",
    "ai_assistant" to "Assistant IA",
    "qr_transfer" to "Passeport QR",
    "security" to "Sécurité & PIN",
    
    // Patient Profile
    "profile" to "Profil Patient",
    "edit_profile" to "Modifier le profil",
    "first_name" to "Prénom",
    "last_name" to "Nom",
    "dob" to "Date de naissance",
    "age" to "Âge",
    "gender" to "Genre",
    "blood_group" to "Groupe sanguin",
    "phone" to "Téléphone",
    "emergency_contact" to "Contact d'urgence",
    "address" to "Adresse",
    "national_id" to "N° Identité Nationale",
    "insurance" to "Assurance Maladie",
    
    // Medical Info
    "medical_info" to "Informations Médicales",
    "diagnosis" to "Diagnostic",
    "kidney_failure_cause" to "Cause de l'insuffisance rénal",
    "dialysis_start_date" to "Date de début de dialyse",
    "dialysis_center" to "Centre de dialyse",
    "nephrologist" to "Néphrologue traitant",
    "general_practitioner" to "Médecin généraliste",
    "allergies" to "Allergies",
    "past_surgeries" to "Chirurgies passées",
    "chronic_diseases" to "Maladies chroniques",
    "vaccinations" to "Vaccinations",
    "emergency_notes" to "Informations d'urgence",
    
    // Passport
    "dry_weight" to "Poids Sec (Target)",
    "dialysis_schedule" to "Planning des séances",
    "duration" to "Durée de séance",
    "vascular_access" to "Accès Vasculaire",
    "access_location" to "Localisation de l'accès",
    "prescription" to "Prescription de Dialyse",
    "blood_flow" to "Débit Sang (Qb)",
    "dialysate_flow" to "Débit Dialysat (Qd)",
    "heparin_protocol" to "Protocole Héparine",
    
    // Sessions
    "new_session" to "Enregistrer une Séance",
    "before_dialysis" to "Avant Dialyse",
    "during_dialysis" to "Pendant Dialyse",
    "after_dialysis" to "Après Dialyse",
    "weight_before" to "Poids Avant (kg)",
    "weight_after" to "Poids Après (kg)",
    "blood_pressure" to "Tension Artérielle",
    "heart_rate" to "Fréquence Cardiaque",
    "temperature" to "Température (°C)",
    "blood_sugar" to "Glycémie (g/L)",
    "ultrafiltration" to "Ultrafiltration (UF)",
    "comments" to "Commentaires",
    "symptoms" to "Symptômes / Incidents",
    "meds_administered" to "Médicaments injectés",
    "recommendations" to "Recommandations",
    "next_session" to "Prochaine séance",
    
    // Home Monitoring
    "home_vitals" to "Suivi Quotidien à Domicile",
    "daily_bp" to "Tension Artérielle Quotidienne",
    "morning" to "Matin",
    "noon" to "Midi",
    "evening" to "Soir",
    "night" to "Nuit",
    "daily_weight" to "Poids Quotidien",
    "interdialytic_weight_gain" to "Prise de poids entre séances",
    "trends" to "Graphiques de Tendance",
    "weekly" to "Semaine",
    "monthly" to "Mois",
    "yearly" to "Année",
    
    // Labs
    "lab_history" to "Historique Laboratoire",
    "add_lab_result" to "Ajouter Bilan",
    "hemoglobin" to "Hémoglobine (Hb)",
    "creatinine" to "Créatinine",
    "urea" to "Urée",
    "potassium" to "Potassium (K+)",
    "calcium" to "Calcium (Ca2+)",
    "phosphorus" to "Phosphore (PO4)",
    "albumin" to "Albumine",
    "ferritin" to "Ferritine",
    "iron" to "Fer Sérique",
    "crp" to "CRP",
    "pth" to "PTH (Parathormone)",
    "vit_d" to "Vitamine D",
    "sodium" to "Sodium (Na+)",
    "wbc" to "Leucocytes",
    "platelets" to "Platelets",
    "abnormal_alert" to "Alerte Valeur Anormale",
    
    // Nutrition
    "fluid_intake" to "Apport Hydrique Quotidien",
    "fluid_limit" to "Limite Hydrique Conseillée",
    "fluid_calculator" to "Calculateur de Restriction d'Eau",
    "food_diary" to "Journal Alimentaire",
    "recommended_foods" to "Aliments Recommandés",
    "foods_to_avoid" to "Aliments à Éviter",
    "potassium_guide" to "Guide Potassium",
    "phosphorus_guide" to "Guide Phosphore",
    "salt_guide" to "Guide Sel & Sodium",
    
    // Emergency Card & Transfer
    "emergency_card_title" to "CARTE URGENCE PATIENT DIALYSE",
    "emergency_card_subtitle" to "Présenter aux secours / hôpital",
    "dialysis_patient_badge" to "PATIENT EN DIALYSE CHRONIQUE",
    "qr_code_passport" to "Code QR Passeport Médical",
    "scan_qr" to "Scanner Code QR Hôpital",
    "patient_transfer" to "Transfert de Centre / Hôpital",
    "transfer_desc" to "Vos données restent à vous. Transférez tout votre historique vers un nouveau centre en 1 clic.",
    "export_full_data" to "Exporter Dossier Médical Complet",
    
    // Actions
    "save" to "Enregistrer",
    "cancel" to "Annuler",
    "sync_now" to "Synchroniser Cloud",
    "synced_badge" to "Données Synchronisées (Offline-First)",
    "offline_notice" to "Mode Hors-Ligne Actif (Synchro Automatique)"
)

private val arStrings = mapOf(
    "app_title" to "دفتر الغسيل الكلوي",
    "app_subtitle" to "الرقمي",
    "home" to "الرئيسية",
    "passport" to "جواز الغسيل",
    "sessions" to "الجلسات",
    "monitoring" to "القياسات",
    "labs" to "المختبر",
    "meds" to "الأدوية",
    "nutrition" to "التغذية والماء",
    "docs" to "الوثائق",
    "emergency" to "بطاقة الطوارئ",
    "ai_assistant" to "المساعد الذكي",
    "qr_transfer" to "رمز الاستجابة QR",
    "security" to "الأمان والرمز",
    
    "profile" to "الملف الشخصي",
    "edit_profile" to "تعديل البيانات",
    "first_name" to "الاسم الأول",
    "last_name" to "اللقب",
    "dob" to "تاريخ الميلاد",
    "age" to "العمر",
    "gender" to "الجنس",
    "blood_group" to "فصيلة الدم",
    "phone" to "الهاتف",
    "emergency_contact" to "اتصال الطوارئ",
    "address" to "العنوان",
    "national_id" to "رقم الهوية الوطنية",
    "insurance" to "الضمان الاجتماعي",
    
    "medical_info" to "المعلومات الطبية",
    "diagnosis" to "التشخيص",
    "kidney_failure_cause" to "سبب الفشل الكلوي",
    "dialysis_start_date" to "تاريخ بدء الغسيل",
    "dialysis_center" to "مركز الغسيل",
    "nephrologist" to "طبيب الكلى المباشر",
    "general_practitioner" to "الطبيب العام",
    "allergies" to "الحساسية",
    "past_surgeries" to "العمليات الجراحية السابقة",
    "chronic_diseases" to "الأمراض المزمنة",
    "vaccinations" to "التطعيمات",
    "emergency_notes" to "تعليمات الطوارئ",
    
    "dry_weight" to "الوزن الجاف Target",
    "dialysis_schedule" to "جدول الجلسات",
    "duration" to "مدة الجلسة",
    "vascular_access" to "المدخل الوعائي",
    "access_location" to "موقع المدخل الوعائي",
    "prescription" to "وصفة الغسيل الكلوي",
    "blood_flow" to "تدفق الدم (Qb)",
    "dialysate_flow" to "تدفق السائل (Qd)",
    "heparin_protocol" to "بروتوكول الهيبارين",
    
    "new_session" to "تسجيل جلسة غسيل",
    "before_dialysis" to "قبل الجلسة",
    "during_dialysis" to "أثناء الجلسة",
    "after_dialysis" to "بعد الجلسة",
    "weight_before" to "الوزن قبل (كغ)",
    "weight_after" to "الوزن بعد (كغ)",
    "blood_pressure" to "ضغط الدم",
    "heart_rate" to "نبضات القلب",
    "temperature" to "الحرارة (°C)",
    "blood_sugar" to "السكر في الدم",
    "ultrafiltration" to "كمية الترشيح (UF)",
    "comments" to "ملاحظات",
    "symptoms" to "الأعراض والإنذارات",
    "meds_administered" to "الأدوية أثناء الجلسة",
    "recommendations" to "التوصيات",
    "next_session" to "الجلسة القادمة",
    
    "home_vitals" to "المتابعة اليومية بالمنزل",
    "daily_bp" to "قياس ضغط الدم اليومي",
    "morning" to "صباحاً",
    "noon" to "ظهراً",
    "evening" to "مساءً",
    "night" to "ليلاً",
    "daily_weight" to "الوزن اليومي",
    "interdialytic_weight_gain" to "الزيادة بين الجلسات",
    "trends" to "الرسوم البيانية",
    "weekly" to "أسبوعي",
    "monthly" to "شهري",
    "yearly" to "سنوي",
    
    "lab_history" to "نتائج التحاليل الطبية",
    "add_lab_result" to "إضافة تحليل جديد",
    "hemoglobin" to "الهيموغلوبين (Hb)",
    "creatinine" to "الكرياتينين",
    "urea" to "اليوريا",
    "potassium" to "البوتاسيوم (K+)",
    "calcium" to "الكالسيوم",
    "phosphorus" to "الفوسفور",
    "albumin" to "الألبومين",
    "ferritin" to "الفيريتين",
    "iron" to "الحديد",
    "crp" to "مؤشر التهاب CRP",
    "pth" to "هرمون PTH",
    "vit_d" to "فيتامين د",
    "sodium" to "الصوديوم",
    "wbc" to "كريات الدم البيضاء",
    "platelets" to "الصفائح الدموية",
    "abnormal_alert" to "تنبيه قيم غير طبيعية",
    
    "fluid_intake" to "كمية السوائل اليومية",
    "fluid_limit" to "حد السوائل المسموح",
    "fluid_calculator" to "حاسبة تحديد السوائل",
    "food_diary" to "مفكرة الطعام",
    "recommended_foods" to "أطعمة موصى بها",
    "foods_to_avoid" to "أطعمة يجب تجنبها",
    "potassium_guide" to "دليل البوتاسيوم",
    "phosphorus_guide" to "دليل الفوسفور",
    "salt_guide" to "دليل الملح والصوديوم",
    
    "emergency_card_title" to "بطاقة طوارئ مريض الغسيل الكلوي",
    "emergency_card_subtitle" to "للعرض الفوري على الإسعاف والمستشفى",
    "dialysis_patient_badge" to "مريض غسيل كلوي مزمن",
    "qr_code_passport" to "رمز QR الجواز الطبي",
    "scan_qr" to "مسح رمز QR بالمستشفى",
    "patient_transfer" to "تحويل إلى مستشفى / مركز جديد",
    "transfer_desc" to "بياناتك ملكك بالكامل. يمكنك الانتقال إلى أي مركز بدون فقدان أي معلومة.",
    "export_full_data" to "تصدير الملف الطبي الكامل",
    
    "save" to "حفظ",
    "cancel" to "إلغاء",
    "sync_now" to "مزامنة السحابة",
    "synced_badge" to "البيانات محمية ومزتمنة",
    "offline_notice" to "يعمل بدون إنترنت (مزامنة تلقائية)"
)

private val enStrings = mapOf(
    "app_title" to "My Dialysis Book",
    "app_subtitle" to "Digital Passport",
    "home" to "Home",
    "passport" to "Passport",
    "sessions" to "Sessions",
    "monitoring" to "Vitals",
    "labs" to "Labs",
    "meds" to "Medications",
    "nutrition" to "Nutrition & Fluid",
    "docs" to "Documents",
    "emergency" to "EMERGENCY CARD",
    "ai_assistant" to "AI Assistant",
    "qr_transfer" to "QR Passport",
    "security" to "Security & PIN",
    
    "profile" to "Patient Profile",
    "edit_profile" to "Edit Profile",
    "first_name" to "First Name",
    "last_name" to "Last Name",
    "dob" to "Date of Birth",
    "age" to "Age",
    "gender" to "Gender",
    "blood_group" to "Blood Group",
    "phone" to "Phone",
    "emergency_contact" to "Emergency Contact",
    "address" to "Address",
    "national_id" to "National ID",
    "insurance" to "Medical Insurance",
    
    "medical_info" to "Medical Information",
    "diagnosis" to "Diagnosis",
    "kidney_failure_cause" to "Cause of Kidney Failure",
    "dialysis_start_date" to "Dialysis Start Date",
    "dialysis_center" to "Dialysis Center",
    "nephrologist" to "Treating Nephrologist",
    "general_practitioner" to "General Practitioner",
    "allergies" to "Allergies",
    "past_surgeries" to "Past Surgeries",
    "chronic_diseases" to "Chronic Diseases",
    "vaccinations" to "Vaccinations",
    "emergency_notes" to "Emergency Notes",
    
    "dry_weight" to "Target Dry Weight",
    "dialysis_schedule" to "Dialysis Schedule",
    "duration" to "Session Duration",
    "vascular_access" to "Vascular Access",
    "access_location" to "Access Location",
    "prescription" to "Dialysis Prescription",
    "blood_flow" to "Blood Flow (Qb)",
    "dialysate_flow" to "Dialysate Flow (Qd)",
    "heparin_protocol" to "Heparin Protocol",
    
    "new_session" to "Record Dialysis Session",
    "before_dialysis" to "Before Dialysis",
    "during_dialysis" to "During Dialysis",
    "after_dialysis" to "After Dialysis",
    "weight_before" to "Weight Before (kg)",
    "weight_after" to "Weight After (kg)",
    "blood_pressure" to "Blood Pressure",
    "heart_rate" to "Heart Rate",
    "temperature" to "Temperature (°C)",
    "blood_sugar" to "Blood Sugar",
    "ultrafiltration" to "Ultrafiltration (UF)",
    "comments" to "Comments",
    "symptoms" to "Symptoms / Incidents",
    "meds_administered" to "Meds Administered",
    "recommendations" to "Recommendations",
    "next_session" to "Next Session",
    
    "home_vitals" to "Daily Home Monitoring",
    "daily_bp" to "Daily Blood Pressure",
    "morning" to "Morning",
    "noon" to "Noon",
    "evening" to "Evening",
    "night" to "Night",
    "daily_weight" to "Daily Weight",
    "interdialytic_weight_gain" to "Weight Gain Between Sessions",
    "trends" to "Trend Graphs",
    "weekly" to "Weekly",
    "monthly" to "Monthly",
    "yearly" to "Yearly",
    
    "lab_history" to "Laboratory History",
    "add_lab_result" to "Add Lab Result",
    "hemoglobin" to "Hemoglobin (Hb)",
    "creatinine" to "Creatinine",
    "urea" to "Urea",
    "potassium" to "Potassium (K+)",
    "calcium" to "Calcium",
    "phosphorus" to "Phosphorus",
    "albumin" to "Albumin",
    "ferritin" to "Ferritin",
    "iron" to "Serum Iron",
    "crp" to "CRP",
    "pth" to "PTH",
    "vit_d" to "Vitamin D",
    "sodium" to "Sodium",
    "wbc" to "White Blood Cells",
    "platelets" to "Platelets",
    "abnormal_alert" to "Abnormal Value Alert",
    
    "fluid_intake" to "Daily Fluid Intake",
    "fluid_limit" to "Recommended Fluid Limit",
    "fluid_calculator" to "Fluid Restriction Calculator",
    "food_diary" to "Food Diary",
    "recommended_foods" to "Recommended Foods",
    "foods_to_avoid" to "Foods to Avoid",
    "potassium_guide" to "Potassium Guide",
    "phosphorus_guide" to "Phosphorus Guide",
    "salt_guide" to "Salt & Sodium Guide",
    
    "emergency_card_title" to "DIALYSIS PATIENT EMERGENCY CARD",
    "emergency_card_subtitle" to "Present immediately to ER or First Responders",
    "dialysis_patient_badge" to "CHRONIC DIALYSIS PATIENT",
    "qr_code_passport" to "QR Code Medical Passport",
    "scan_qr" to "Scan Hospital QR Code",
    "patient_transfer" to "Change Hospital / Dialysis Center",
    "transfer_desc" to "You own your medical data. Transfer your full history to a new hospital without losing anything.",
    "export_full_data" to "Export Complete Medical File",
    
    "save" to "Save",
    "cancel" to "Cancel",
    "sync_now" to "Sync Cloud Now",
    "synced_badge" to "Data Synced & Secured (Offline-First)",
    "offline_notice" to "Offline Mode Active (Auto Sync when online)"
)
