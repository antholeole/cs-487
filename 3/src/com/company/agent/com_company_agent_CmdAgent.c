#include <jni.h>
#include <time.h>
#include <stdint.h>

#ifdef _WIN32
    #define OS "Windows 32-bit"
#elif _WIN64
    #define OS "Windows 64-bit"
#elif __APPLE__ || __MACH__
    #define OS "Mac OSX"
#elif __linux__
    #define OS "Linux"
#elif __FreeBSD__
    #define OS "FreeBSD"
#elif __unix || __unix__
    #define OS "Unix"
#else
    #define OS "Other"
#endif

JNIEXPORT jobject JNICALL Java_com_company_agent_CmdAgent_C_1GetLocalTime(
        JNIEnv *env,
        jobject this,
        jobject getLocalTime
) {
    jclass getLocalTimeClass = (*env)->GetObjectClass(env, getLocalTime);

    jfieldID validFieldId = (*env)->GetFieldID(env, getLocalTimeClass, "valid", "I");
    (*env)->SetIntField(env, getLocalTime, validFieldId, 1);

    jfieldID localTimeFieldId = (*env)->GetFieldID(env, getLocalTimeClass, "localTime", "I");
    (*env)->SetIntField(env, getLocalTime, localTimeFieldId, time(NULL));

    return getLocalTime;
}

JNIEXPORT jobject JNICALL Java_com_company_agent_CmdAgent_C_1GetLocalOs(
    JNIEnv *env,
    jobject this,
    jobject getLocalOs
) {
    jclass getLocalOsClass = (*env)->GetObjectClass(env, getLocalOs);

    jfieldID validFieldId = (*env)->GetFieldID(env, getLocalOsClass, "valid", "I");
    (*env)->SetIntField(env, getLocalOs, validFieldId, 1);

    jfieldID localOsFieldId = (*env)->GetFieldID(env, getLocalOsClass, "localOs", "Ljava/lang/String;");

    (*env)->SetObjectField(env, getLocalOs, localOsFieldId, (*env)->NewStringUTF(env, OS));

    return getLocalOs;
}