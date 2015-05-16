#define CLLIB_EXTERN
#include "CLlib.h"
#include <dlfcn.h>
#include <android/log.h>


#define  LOG_TAG    "libclminibench"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

static void *handle;

int
cllib_init(void)
{
    /* g620s */
    handle = dlopen("/system/vendor/lib/libOpenCL.so", RTLD_LAZY);
    if (!handle) {
        /* nexus 10 */
        handle = dlopen("/system/vendor/lib/egl/libGLES_mali.so", RTLD_LAZY);
    }

    if (!handle) {
        return -1;
    }

#define LOAD(name)                              \
    p_##name = dlsym(handle, #name);            \
        if (p_##name == NULL) {                 \
            return -1;                          \
        }

    LOAD(clGetDeviceInfo);
    LOAD(clGetPlatformIDs);
    LOAD(clGetDeviceIDs);
    LOAD(clGetPlatformInfo);
    LOAD(clCreateProgramWithSource);
    LOAD(clBuildProgram);
    LOAD(clGetProgramBuildInfo);
    LOAD(clReleaseProgram);
    LOAD(clCreateKernel);
    LOAD(clCreateBuffer);
    LOAD(clEnqueueWriteBuffer);
    LOAD(clFlush);
    LOAD(clReleaseMemObject);
    LOAD(clEnqueueReadBuffer);
    LOAD(clFinish);
    LOAD(clEnqueueNDRangeKernel);
    LOAD(clReleaseKernel);
    LOAD(clSetKernelArg);
    LOAD(clCreateCommandQueue);
    LOAD(clCreateContext);
    LOAD(clReleaseCommandQueue);
    LOAD(clReleaseContext);

    return 0;
}