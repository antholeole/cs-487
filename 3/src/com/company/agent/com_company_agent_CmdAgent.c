#include <jni.h>

JNIEXPORT jobject JNICALL Java_com_company_agent_CmdAgent_C_1GetLocalTime(
        JNIEnv *env,
        jobject this,
        jobject getLocalTime
) {
    int32_t local_time = time(NULL);

    jfieldID validField = env->GetFieldID(getLocalTime, "valid", "I");
    jfieldID localTimeField = env->GetFieldID(getLocalTime, "localTime", "I");

    env->SetObjectField(getLocalTime, validField, 1);
    env->SetObjectField(getLocalTime, validField, local_time);
}