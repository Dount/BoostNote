# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#Cmake变量的使用
set(var hello)
message(${var})

#Cmake常量的使用
message(${CMAKE_CURRENT_LIST_DIR})
message(${CMAKE_CURRENT_LIST_FILE})

#Cmake if的使用
IF(TRUE)
    message("this is true")
ENDIF()

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp)
#add_library的使用

add_library(
        people-lib
        SHARED
        people/people.cpp)

#动态注册
add_library(
        dynamic-lib
        SHARED
        jni/jni_dynamic_load.cpp
        jni/jni_invoke_method.cpp
        thread/jni_thread.cpp
)

#include_directories()的使用
include_directories(people/)
include_directories(${CMAKE_SOURCE_DIR}/base/)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

# 关联so库
target_link_libraries( # Specifies the target library.
        native-lib
        people-lib
        dynamic-lib
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})