cmake_minimum_required(VERSION 2.6)

find_package(OpenCL REQUIRED)

include_directories(${OpenCL_INCLUDE_DIRS})
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/..)

link_directories(${OpenCL_LIB_DIRS})

add_executable(clinstbench
  cli-app.cpp ../bench/bench.c ../port.c)

if (APPLE)
    target_link_libraries(clinstbench "-framework opencl")
else()
    target_link_libraries(clinstbench OpenCL)
endif()

set(CMAKE_CXX_FLAGS "-std=c++11")
